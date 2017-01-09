package gameEngine


class Tank(val id: String) extends GameObject {
  
  /**trying to move tank left*/
  def moveLeft(): Unit = ???
  
  /**trying to move tank right*/
  def moveRight(): Unit = ???
  
  def turnCannonLeft(): Unit = ???
  
  def turnCannonRight(): Unit = ???
  
  def increaseShootPower(): Unit = ???
  
  def decreaseShootPower(): Unit = ???
  
  def getPosition():Pos = ???
  
  def setPosition(location: Pos) = ???
  
  def typeString = "Tank"
  
  def shoot(): Unit = ???
  
  
}