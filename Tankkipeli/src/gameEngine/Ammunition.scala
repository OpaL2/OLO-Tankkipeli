package gameEngine

import scala.math
import scala.collection.mutable.Buffer

object Ammunition{
  val GRAVITY = World.GRAVITY
  val MULTIPLIER = World.MULTIPLIER
}

class BasicAmmunition(world: World) extends Ammunition(world) {
  val massMultiplier = 1.0
  val dmg = 50
}

class HeavyAmmunition(world: World) extends Ammunition(world) {
  val massMultiplier = 1.2
  val dmg = 70
}

abstract class Ammunition(val world: World)  {
  
  val massMultiplier: Double
  val dmg: Int

  def shoot(startPos: Pos, angle: Int, power: Int) = {
    val bullet = new Bullet(startPos, angle, power, this.massMultiplier, this.world, this)
    this.world.addBullet(bullet)
  }
  
  /** makes ammunition to explode at given position, greater damage causes bigger expolsion*/
  def explode(position: Pos) = {
    val gamefield = this.world.gamefield
    
    var tmpDmg = this.dmg
    var iterRound = 0
    var positionVector = Vector(position)
    val damagedPositions = Buffer.empty[Pos]
    //loop causing damage to positions, quite complex, hopefully works
    while (tmpDmg.toInt > 2 && iterRound < World.MAXDMGITER) {
      //damaging each position on a position filter
      positionVector.foreach { this.dmgPosition(_, tmpDmg) }
      positionVector.foreach { damagedPositions.append(_) }
      tmpDmg = (tmpDmg/World.DMGDIVIDER).toInt
      iterRound += 1
      val tmp = Buffer.empty[Pos]
      positionVector.foreach{ x => {
        def filtering(position: Pos) = {
          if(tmp.filter(_ == position).isEmpty && damagedPositions.filter(_ == position).isEmpty) tmp.append(position)
        }
        filtering(x.up)
        filtering(x.down)
        filtering(x.left)
        filtering(x.right)
      }
      }
      positionVector = tmp.toVector
    }
  }
  
  private def dmgPosition(position: Pos, dmg: Int) = {
    if(this.world.gamefield.contains(position)) {
    val content = this.world.gamefield(position)
    content.causeDmg(dmg)
    if(content.typeString == "Wall" && content.isDestroyed) {
      this.world.gamefield.update(new Empty(position), position)
    }
  }
  }
  
//  /** currently does nothing, notifyes the ammunition that it is flied off the game*/
  def outOfGame() = Unit
}


/**flying object*/
class Bullet(startPos: Pos, angle: Int, power: Int, val massMultiplier: Double, val world: World, val ammunition: Ammunition) {
  
  val startPosition = new Vector2(startPos.x, startPos.y)
  val startSpeed = this.calcSpeedVect((255-angle)/255.0*math.Pi, power/255.0)
  var speed = this.startSpeed
  var position = this.startPosition
  var time = 0.0
  
  private def calcSpeedVect(angle: Double, power: Double): Vector2 = {
    new Vector2(math.acos(angle),math.asin(angle)) * power * Ammunition.MULTIPLIER * this.massMultiplier
  }
  
  private def calcXPos(dt: Double): Double = this.startPosition.x+this.startSpeed.x*dt
    
  private def calcYPos(dt: Double): Double = this.startPosition.y+this.startSpeed.y*dt-0.5*Ammunition.GRAVITY*dt*dt
  
  private def calcPos(): Vector2 = new Vector2(this.calcXPos(this.time), this.calcYPos(this.time))
  
  private def calcXSpeed(dt: Double): Double = this.startSpeed.x
  
  private def calcYSpeed(dt: Double): Double = this.startSpeed.y-Ammunition.GRAVITY*dt
  
  private def calcSpeed(): Vector2 = new Vector2(this.calcXSpeed(this.time), this.calcYSpeed(this.time))
  
  private def testCollision(): Boolean = !this.world.gamefield.isEmpty(this.getPosition)
  

  def update(dt: Double): Unit= {
    this.time = this.time + dt
    this.position = this.calcPos()
    this.speed = this.calcSpeed()
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
  
  def getPositionVector = this.position
  
  def getSpeedVector = this.speed
  
}

final class Vector2(val x: Double, val y: Double) {
  
  def *(i: Double): Vector2 = new Vector2(x*i, y*i)
  
  def abs: Double = math.sqrt(x*x + y*y)
}