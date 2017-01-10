package gameEngine


import LinkedList.List
import scala.collection.mutable.Buffer



class World (width: Int, height: Int) {

 
  //creating gamefield and tank list
  val gamefield = new gameEngine.Gamefield(width, height)
  val tankList = List.empty[Tank]
  var bulletBuffer = Buffer.empty[Bullet]
  
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
  
  /**get current tank in turn*/
  def currentTank: Tank = this.tankList.current.get
  
  /**makes currentTank selection to forward by one*/
  def nextTank: Unit = this.tankList.nextItem()
  
  override def toString() = this.gamefield.toString()
  
  def addBullet(bullet: Bullet) = this.bulletBuffer.append(bullet)
  
  def removeBullet(bullet: Bullet) = this.bulletBuffer = this.bulletBuffer.filterNot { _ == bullet }
  
  /**call this mehtod to make ammunitions fly, use small dt value, */
  def update(dt: Double) = {
    this.bulletBuffer.foreach { x => x.update(dt) }
    this.tankList.foreach(_.update)
  }
  
}


