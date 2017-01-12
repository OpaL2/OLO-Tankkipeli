package gameEngine

import scala.collection.mutable.Stack

object Tank{
  def clamp8bit(value: Int) = {
    if(value < 0) 0
    else if (value > 255) 255
    else value
  }
}

/** class represents tank in a game, ALERT: Before executing any actions, check that tank is working, with isDestoryed method, none of the command methods will check this*/
class Tank(val id: String,private var position: Pos, private val world: World) extends DestroyableObject(World.TANKHP) with GameObject {
  
  private var shootDirection = 128 //8-bit value, 0 means straight left and 255 straight right
  private var shootPower = 128 //8-bit value
  private var fuel = 100
  private val magazine = Stack.empty[Ammunition]
  
  //moving related methods
  
  /**trying to move tank left*/
  def moveLeft(): Boolean = {
    if(this.canMove){
    if (this.world.gamefield.canMove(this.position.left.down)){
      this.consumeFuel(2) 
      this.updateWorld(this.position.left.down)
    }
    else if (this.world.gamefield.canMove(this.position.left)){
      this.consumeFuel(3) 
      this.updateWorld(this.position.left)
    }
    else if (this.world.gamefield.canMove(this.position.left.up)){
      this.consumeFuel(5)
      this.updateWorld(this.position.left.up)
    }
    else
      false
    }
    else
      false
  }
  
  /**trying to move tank right*/
  def moveRight(): Boolean = {
    if (this.canMove){
    if (this.world.gamefield.canMove(this.position.right.down)){
      this.consumeFuel(2)
      this.updateWorld(this.position.right.down)
    }
    else if (this.world.gamefield.canMove(this.position.right)){
       this.consumeFuel(3)
       this.updateWorld(this.position.right)
    }
    else if (this.world.gamefield.canMove(this.position.right.up)){
      this.consumeFuel(5)
      this.updateWorld(this.position.right.up)
    }
    else
      false
    }
    else
      false
  }
  
  private def updateWorld(newPos: Pos): Boolean = {
    this.world.gamefield.update(new Empty(this.position), this.position)
    this.world.gamefield.update(this, newPos)
    true
  }
  
  private def consumeFuel(amount: Int) = {
    this.fuel -= amount
    if(this.fuel <= 0) this.fuel = 0
  }
  
  def getFuelLevel = this.fuel
  
  private def canMove: Boolean = this.fuel > 0
  
  //cannon related methods:
  
  def turnCannonLeft(amount: Int): Unit = this.shootDirection = Tank.clamp8bit(this.shootDirection - amount)
  
  def turnCannonRight(amount: Int): Unit = this.shootDirection = Tank.clamp8bit(this.shootDirection + amount)
  
  def increaseShootPower(amount: Int): Unit = this.shootPower = Tank.clamp8bit(this.shootPower + amount)
  
  def decreaseShootPower(amount: Int): Unit = this.shootPower = Tank.clamp8bit(this.shootPower + amount)
  
  def getCannonAngle = this.shootDirection
  
  def getShootPower = this.shootPower
  
  def shoot(): Unit = {
    this.magazine.pop().shoot(this.position, this.shootDirection, this.shootPower)
  }
  
  
  //magazine related methdos:
  def addAmmunition(items: Ammunition*) = {
    items.foreach { this.magazine.push(_) }
  }
  
  def getMagazine = this.magazine.toVector
  
  def getMagazineSize = this.magazine.length
  
  def getCurrentAmmunition = this.magazine.top
  
  
  //GameObject methods:
  
  def getPosition():Pos = this.position
  
  def setPosition(location: Pos) = this.position = location
  
  def typeString = "Tank"
  
  
  //Other methods: 
  
  override def toString = this.id
  
  def update() = ???
  
  
}