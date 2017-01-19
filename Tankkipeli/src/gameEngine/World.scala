package gameEngine


import LinkedList.List
import scala.collection.mutable.Buffer
import scala.collection.mutable.Stack

/** object storing constants for game engine*/
object World {
  val WALLHP = 20
  val GRAVITY = 5.0
  val MULTIPLIER = 20
  val TANKHP = 40
  val DMGDIVIDER = 3
  val MAXDMGITER = 3
  val TANKINITIALFUEL = 100
  val TANKSPEED = 0.5
  val TANKANIMATIONBOUN = 0.05
}

class World (width: Int, height: Int) {

 
  //creating gamefield and tank list
  val gamefield = new gameEngine.Gamefield(width, height)
  private val tankList = List.empty[Tank]
  private val explosionStack = Stack.empty[Pos]
  var bulletBuffer = Buffer.empty[Bullet]
  var endGame = false
  
  //generating terrain
  this.setFloor(GenTerrain.generate(5, width, 2))
  
  /**creates floor from given vector of Y-axis coordinates*/
  def setFloor(coordinates: Vector[Int]) = {
    var x = 0
    coordinates.foreach { y => {
      for(dy <- 0 to y) {
        val pos = new Pos(x,dy)
        this.gamefield.update(new Wall(pos),pos)
      }
      x = x + 1
    }
    }
  }
  
  /**creates ceiling from given vector of Y-axis coordinates*/
  def setCeiling(coordinates: Vector[Int]) = {
    var x = 0
    coordinates.foreach { y => {
      for(dy <- y until this.gamefield.height) {
        val pos = new Pos(x, dy)
        this.gamefield.update(new Wall(pos), pos)
      }
      x = x + 1
    }
    }
  }
  
  /**creates tank with given id and start position, inserts it to world*/
  def createTank(id: String, xPos: Int):Boolean = {
    var y = 0
    while(y < this.gamefield.height && !this.gamefield.isEmpty(new Pos(xPos, y))) {
      y = y + 1
    }
    if( y < this.gamefield.height) {
      val pos = new Pos(xPos, y)
      val tank = new Tank(id, pos, this)
      this.tankList.append(tank)
      this.gamefield.update(tank, pos)
      true
    }
    else false
  }
  
  def addAmmosToTanks(rounds: Int): Unit = {
    val ammo = GenTerrain.fillClips(this.tankList.toVector().size, rounds , this)
    for(i <- 0 until ammo.size) {
      this.tankList.toVector()(i).addAmmunition(ammo(i))
    }
  }
  
  /**get current tank in turn*/
  def currentTank: Tank = this.tankList.current.get
  
  /**makes currentTank selection to forward by one*/
  def nextTank: Unit = this.tankList.nextItem()
  
  /**returns all tanks*/
  def getTanks: Vector[Tank] = this.tankList.toVector()
  
  override def toString() = this.gamefield.toString()
  
  def addBullet(bullet: Bullet) = this.bulletBuffer.append(bullet)
  
  def removeBullet(bullet: Bullet) = this.bulletBuffer = this.bulletBuffer.filterNot { _ == bullet }
  
  def addExpolsionPosition(position: Pos) = this.explosionStack.push(position)
  
  def isExpolded(): Boolean = !this.explosionStack.isEmpty
  
  def getExplosionPosition: Pos = this.explosionStack.pop()
  
  /**call this mehtod to make ammunitions fly, use small dt value, */
  def update(dt: Double) = {
    this.bulletBuffer.foreach { x => x.update(dt) }
    this.tankList.foreach(_.update(dt))
  }
  
}


