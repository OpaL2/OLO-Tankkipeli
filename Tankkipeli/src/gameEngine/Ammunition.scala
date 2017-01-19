package gameEngine

import GUI.SoundEngine
import scala.math
import scala.collection.mutable.Buffer

object Ammunition{
  val GRAVITY = World.GRAVITY
  val MULTIPLIER = World.MULTIPLIER
}

case class BasicAmmunition(override val world: World) extends Ammunition(world) {
  val massMultiplier = 1.0
  val dmg = 50
  val description = "Basic round"
  
  override def explode(position: Pos) = {
    this.world.sounds.playSound(SoundEngine.explosion)
    super.explode(position)
  }
}

case class HeavyAmmunition(override val world: World) extends Ammunition(world) {
  val massMultiplier = 0.7
  val dmg = 100
  val description = "Heavy missile"
  
  override def explode(position: Pos) = {
    this.world.sounds.playSound(SoundEngine.bigExplosion)
    super.explode(position)
  }
}

sealed abstract class Ammunition(val world: World)  {
  
  val massMultiplier: Double
  val dmg: Int
  val description: String

  def shoot(startPos: Pos, angle: Int, power: Int) = {
    val bullet = new Bullet(startPos, angle, power, this.massMultiplier, this.world, this)
    this.world.addBullet(bullet)
  }
  
  /** makes ammunition to explode at given position, greater damage causes bigger expolsion*/
  def explode(position: Pos) = {

    val gamefield = this.world.gamefield
    this.world.addExpolsionPosition(position)
    /*val element = this.world.gamefield(position)

    element.causeDmg(dmg)

    
    if(this.world.gamefield.isWall(position) && element.isDestroyed) {
      this.world.gamefield.update(new Empty(position), position)
    }*/
    
    
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
  
  override def toString(): String = this.description
}


/**flying object*/
class Bullet(startPos: Pos, angle: Int, power: Int, val massMultiplier: Double, val world: World, val ammunition: Ammunition) {
  
  val startPosition = new Vector2(startPos.x, startPos.y)
  val startSpeed = this.calcSpeedVect(math.Pi*(255-angle)/255.0, power/255.0)
  var speed = this.startSpeed
  var position = this.startPosition
  var time = 0.0
  var leavedTankPos = false
  
  private def calcSpeedVect(angle: Double, power: Double): Vector2 = {
    new Vector2(math.cos(angle),math.sin(angle)) * power * Ammunition.MULTIPLIER * this.massMultiplier
  }
  
  private def calcXPos(dt: Double): Double = this.startPosition.x+this.startSpeed.x*dt
    
  private def calcYPos(dt: Double): Double = this.startPosition.y+this.startSpeed.y*dt-0.5*Ammunition.GRAVITY*dt*dt
  
  private def calcPos(): Vector2 = new Vector2(this.calcXPos(this.time), this.calcYPos(this.time))
  
  private def calcXSpeed(dt: Double): Double = this.startSpeed.x
  
  private def calcYSpeed(dt: Double): Double = this.startSpeed.y-Ammunition.GRAVITY*dt
  
  private def calcSpeed(): Vector2 = new Vector2(this.calcXSpeed(this.time), this.calcYSpeed(this.time))
  
  private def testCollision(): Boolean = !this.world.gamefield.isEmpty(this.getPosition) && this.leavedTankPos
  

  def update(dt: Double): Unit= {
    this.time = this.time + dt
    this.position = this.calcPos()
    if((!this.leavedTankPos) && !(this.getStartPos == this.getPosition)){
      this.leavedTankPos = true
    }
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
  
  def getStartPos = new Pos(math.floor(this.startPosition.x).toInt, math.floor(this.startPosition.y).toInt)
  
  def getPosition: Pos = new Pos(math.round(this.position.x).toInt, math.round(this.position.y).toInt)
  
  def getPositionVector = this.position
  
  def getSpeedVector = this.speed.unitVector()
  
}

final class Vector2(val x: Double, val y: Double) {
  
  def *(i: Double): Vector2 = new Vector2(this.x*i, this.y*i)
  
  def abs: Double = math.sqrt(this.x*this.x + this.y*this.y)
  
  def -(other: Vector2): Vector2 = new Vector2(this.x - other.x, this.y - other.y)
  
  def +(other: Vector2): Vector2 = new Vector2(this.x + other.x, this.y + other.y)
  
  def unitVector(): Vector2 = new Vector2(this.x/this.abs, this.y/this.abs)
}