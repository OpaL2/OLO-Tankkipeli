package gameEngine


class Tank(val id: String) {
  
  /**applying force to left, trying to move tank*/
  def moveLeft(): Unit = ???
  
  /**applying force to right, trying to move tank*/
  def moveRight(): Unit = ???
  
  def turnCannonLeft(): Unit = ???
  
  def turnCannonRight(): Unit = ???
  
  def increaseShootPower(): Unit = ???
  
  def decreaseShootPower(): Unit = ???
  
  def getPosition():(Int, Int) = ???
  
  def shoot(): Unit = ???
  
  
}