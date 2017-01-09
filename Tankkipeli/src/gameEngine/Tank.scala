package gameEngine

object Tank{
  def clamp8bit(value: Int) = {
    if(value < 0) 0
    else if (value > 255) 255
    else value
  }
}


class Tank(val id: String,private var position: Pos, private val world: World) extends GameObject {
  
  private var shootDirection = 128 //8-bit value, 0 means straight left and 255 straight right
  private var shootPower = 128 //8-bit value
  
  /**trying to move tank left*/
  def moveLeft(): Boolean = {
    if (this.world.gamefield.canMove(this.position.left.down)){
      this.updateWorld(this.position.left.down)
    }
    else if (this.world.gamefield.canMove(this.position.left)){
       this.updateWorld(this.position.left)
    }
    else if (this.world.gamefield.canMove(this.position.left.up)){
      this.updateWorld(this.position.left.up)
    }
    else
      false
  }
  
  /**trying to move tank right*/
  def moveRight(): Boolean = {
    if (this.world.gamefield.canMove(this.position.right.down)){
      this.updateWorld(this.position.right.down)
    }
    else if (this.world.gamefield.canMove(this.position.right)){
       this.updateWorld(this.position.right)
    }
    else if (this.world.gamefield.canMove(this.position.right.up)){
      this.updateWorld(this.position.right.up)
    }
    else
      false
  }
  
  private def updateWorld(newPos: Pos): Boolean = {
    this.world.gamefield.update(new Empty(this.position), this.position)
    this.world.gamefield.update(this, newPos)
    true
  }
  
  def turnCannonLeft(amount: Int): Unit = this.shootDirection = Tank.clamp8bit(this.shootDirection - amount)
  
  def turnCannonRight(amount: Int): Unit = this.shootDirection = Tank.clamp8bit(this.shootDirection + amount)
  
  def increaseShootPower(amount: Int): Unit = this.shootPower = Tank.clamp8bit(this.shootPower + amount)
  
  def decreaseShootPower(amount: Int): Unit = this.shootPower = Tank.clamp8bit(this.shootPower + amount)
  
  def getCannonAngle = this.shootDirection
  
  def getShootPower = this.shootPower
  
  
  
  def getPosition():Pos = this.position
  
  def setPosition(location: Pos) = this.position = location
  
  def typeString = "Tank"
  
  
  
  def shoot(): Unit = ???
  
  override def toString = this.id
  
  
}