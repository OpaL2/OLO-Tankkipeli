package gameEngine

abstract class Ammunition()  {
  
  def explosion(): Unit = ???
  
  def getPosition():(Int, Int) = ???
  
}

class BasicAmmunition() extends Ammunition() {
  
}