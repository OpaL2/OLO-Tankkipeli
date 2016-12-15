package gameEngine

import org.jbox2d.common.Vec2

object Ammunition {
  
  val SIZE = 5.0
  
  val MASS : Float = 1
  
}

abstract class Ammunition(val impulse: Vec2,val startPos: Vec2,val mass: Float)  {
  
  def explosion(): Unit = ???
  
  def getPosition():(Int, Int) = ???
  
}

class BasicAmmunition(impulse: Vec2, startPos: Vec2) extends Ammunition(impulse, startPos, Ammunition.MASS) {
  
}