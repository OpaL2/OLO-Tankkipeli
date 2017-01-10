package gameEngine

import scala.math

object Ammunition{
  val GRAVITY = 1.0
  val MULTIPLIER = 1.0
}

abstract class Ammunition(val world: World)  {
  
  val massMultiplier: Double
  val dmg: Double 

  def shoot(startPos: Pos, angle: Int, power: Int) = {
    val bullet = new Bullet(startPos, angle, power, this.massMultiplier, this.world, this)
    this.world.addBullet(bullet)
  }
  
  def explode(position: Pos) = {
    
  }
  
  def outOfGame()
}


/**flying object*/
class Bullet(startPos: Pos, angle: Int, power: Int, val massMultiplier: Double, val world: World, val ammunition: Ammunition) extends GameObject {
  
  val startPosition = new Vector2(startPos.x, startPos.y)
  var speed = this.calcSpeedVect(angle/255.0*math.Pi, power/255.0)
  var position = this.startPosition
  var time = 0.0
  
  private def calcSpeedVect(angle: Double, power: Double): Vector2 = {
    new Vector2(-1 * math.acos(angle),math.asin(angle)) * power * Ammunition.MULTIPLIER * this.massMultiplier
  }
  
  private def calcXPos(dt: Double): Double = this.startPosition.x+this.speed.x*dt
    
  private def calcYPos(dt: Double): Double = this.startPosition.y+this.speed.y*dt-0.5*Ammunition.GRAVITY*dt*dt
  
  private def calcPos(): Vector2 = new Vector2(this.calcXPos(this.time), this.calcYPos(this.time))
  
  private def testCollision(): Boolean = !this.world.gamefield.isEmpty(this.getPosition)
  

  def update(dt: Double): Unit= {
    this.time = this.time + dt
    this.position = this.calcPos()
    if (this.testCollision()) {
      if(this.world.gamefield.contains(this.getPosition)) {
        this.ammunition.explode(this.getPosition)
        this.removeSelf()
      }
      else
        this.ammunition.outOfGame
        this.removeSelf()
    }
  }
  
  private def removeSelf() = this.world.removeBullet(this)
  
  def setPosition(location: Pos) = this.position = new Vector2(location.x, location.y)
  
  def getPosition: Pos = new Pos(this.position.x.toInt, this.position.y.toInt)
  
  def typeString = "Bullet"
  
}

final class Vector2(val x: Double, val y: Double) {
  
  def *(i: Double): Vector2 = new Vector2(x*i, y*i)
  
  def abs: Double = math.sqrt(x*x + y*y)
}