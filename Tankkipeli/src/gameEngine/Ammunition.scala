package gameEngine

abstract class Ammunition()  {
  
  def explosion(): Unit = ???
  
  def getPosition():(Int, Int) = ???
  
  def shoot(startPos: Pos, angle: Int, power: Int) = ???
  
}

class BasicAmmunition() extends Ammunition() {
  
}