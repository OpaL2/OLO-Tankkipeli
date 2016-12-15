package gameEngine

object Tank {
  
  //size of tank
  var XSIZE = 10
  var YSIZE = 10
  
  //body parameters
  
  //other parameters, such as max amount of fuel
  val MAXFUEL = 500
  val MAXAMMOLOAD = 500
}

class Tank(val id: String, val world: World, yPos: Int) {
  
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