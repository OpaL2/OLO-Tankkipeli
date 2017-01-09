package gameEngine


class Tank(val id: String,private var position: Pos) extends GameObject {
  
  /**trying to move tank left*/
  def moveLeft(): Unit = ???
  
  /**trying to move tank right*/
  def moveRight(): Unit = ???
  
  def turnCannonLeft(): Unit = ???
  
  def turnCannonRight(): Unit = ???
  
  def increaseShootPower(): Unit = ???
  
  def decreaseShootPower(): Unit = ???
  
  def getPosition():Pos = ???
  
  def setPosition(location: Pos) = this.position = location
  
  def typeString = "Tank"
  
  def shoot(): Unit = ???
  
  override def toString = this.id
  
  
}