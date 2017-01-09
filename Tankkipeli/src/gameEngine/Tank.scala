package gameEngine


class Tank(val id: String,private var position: Pos, private val world: World) extends GameObject {
  
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
  
  def turnCannonLeft(): Unit = ???
  
  def turnCannonRight(): Unit = ???
  
  def increaseShootPower(): Unit = ???
  
  def decreaseShootPower(): Unit = ???
  
  def getPosition():Pos = this.position
  
  def setPosition(location: Pos) = this.position = location
  
  def typeString = "Tank"
  
  def shoot(): Unit = ???
  
  override def toString = this.id
  
  
}