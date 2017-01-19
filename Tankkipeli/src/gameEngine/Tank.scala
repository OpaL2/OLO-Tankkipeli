package gameEngine

import scala.collection.mutable.Stack
import GUI.SoundEngine

object Tank{
  def clamp8bit(value: Int) = {
    if(value < 0) 0
    else if (value > 255) 255
    else value
  }
}

/** class represents tank in a game, ALERT: Before executing any actions, check that tank is working, with isDestoryed method, none of the command methods will check this*/
class Tank(val id: String,private var position: Pos, private val world: World) extends DestroyableObject(World.TANKHP) {
  
  private var shootDirection = 128 //8-bit value, 0 means straight left and 255 straight right
  private var shootPower = 128 //8-bit value
  private var fuel = World.TANKINITIALFUEL
  private val magazine = Stack.empty[Ammunition]
  
  //these properties are for tank animations only
  var vectorPosition= new Vector2(position.x, position.y)
  var reachedDestination: Boolean = true //check that this flag is true, before moving tank again
  private var isFalling: Boolean = false
  
  //barrel sound and animation
  private var barrelMoving = false
  private var barrelMovingInt = 0
  private var barrelSoundPlaying = false
  
  private var AIBarrel = false
  private var AIBarrelValue = 0
  
  //power set sound and animation
  private var powerChange = false
  private var powerChangeInt = 0
  private var powerSoundPlaying = false
  
  private var AIPower = false
  private var AIPowerValue = 0
  
  private var AICannonLoaded = false
  private var AICannonDelay = 0
  
  //moving related methods
  
  /**trying to move tank left*/
  def moveLeft(): Boolean = {
    if(this.canMove){
    if (this.world.gamefield.canMove(this.position.left.down)){
      this.consumeFuel(2)
      this.world.sounds.loopSound(SoundEngine.tankDrive)
      this.updateWorld(this.position.left.down)
    }
    else if (this.world.gamefield.canMove(this.position.left)){
      this.consumeFuel(3)
      this.world.sounds.loopSound(SoundEngine.tankDrive)
      this.updateWorld(this.position.left)
    }
    else if (this.world.gamefield.canMove(this.position.left.up)){
      this.consumeFuel(5)
      this.world.sounds.loopSound(SoundEngine.tankDrive)
      this.updateWorld(this.position.left.up)
    }
    else
      false
    }
    else
      false
  }
  
  /**trying to move tank right*/
  def moveRight(): Boolean = {
    if (this.canMove){
    if (this.world.gamefield.canMove(this.position.right.down)){
      this.consumeFuel(2)
      this.world.sounds.loopSound(SoundEngine.tankDrive)
      this.updateWorld(this.position.right.down)
    }
    else if (this.world.gamefield.canMove(this.position.right)){
       this.consumeFuel(3)
       this.world.sounds.loopSound(SoundEngine.tankDrive)
       this.updateWorld(this.position.right)
    }
    else if (this.world.gamefield.canMove(this.position.right.up)){
      this.consumeFuel(5)
      this.world.sounds.loopSound(SoundEngine.tankDrive)
      this.updateWorld(this.position.right.up)
    }
    else
      false
    }
    else
      false
  }
  
  private def updateWorld(newPos: Pos): Boolean = {
    this.world.gamefield.update(new Empty(this.position), this.position)
    this.world.gamefield.update(this, newPos)
    this.reachedDestination = false
    true
  }
  
  private def consumeFuel(amount: Int) = {
    this.fuel -= amount
    if(this.fuel <= 0) this.fuel = 0
  }
  
  def getFuelLevel = this.fuel
  
  
  private def canMove: Boolean = this.fuel > 0 && (!this.isDestroyed) && this.reachedDestination && !this.world.gamefield.isEmpty(this.getPosition.down)
  
  //cannon related methods:
  
  def turnCannonLeft(amount: Int): Unit = {
    this.shootDirection = Tank.clamp8bit(this.shootDirection - amount)
    this.barrelMoving = true
  }
  
  def turnCannonRight(amount: Int): Unit = {
    this.shootDirection = Tank.clamp8bit(this.shootDirection + amount)
    this.barrelMoving = true
  }
  
  def increaseShootPower(amount: Int): Unit = {
    this.shootPower = Tank.clamp8bit(this.shootPower + amount)
    this.powerChange = true
  }
  
  def decreaseShootPower(amount: Int): Unit = {
    this.shootPower = Tank.clamp8bit(this.shootPower - amount)
    this.powerChange = true
  }
  
  def getCannonAngle = this.shootDirection
  
  def getShootPower = this.shootPower
  
  def shoot(): Unit = {
    if(!this.magazine.isEmpty) {
      this.magazine.pop().shoot(this.position, this.shootDirection, this.shootPower)
      this.world.sounds.playSound(SoundEngine.cannonFire)
    }
    this.world.nextTank
    this.world.endTurn = true
  }
  //testshoot is used by AI to check if it hits opponent or not. 
  //Returns position of the hit that can be compared to position of the enemy 
  //Testshoot does not consume any resources nor cause damage or have animations.
  def testshoot(angle :Int, power:Int):Pos ={
      val ang = Tank.clamp8bit(angle)
      val pow = Tank.clamp8bit(power)
      var dmg = 0
      var mass = 0.0
      
      this.magazine.top match {
        case x:BasicAmmunition => {
          mass = x.massMultiplier
          dmg = x.dmg
        }
        case x:HeavyAmmunition => {
          mass = x.massMultiplier
          dmg = x.dmg
        }
      }
      val bullet = (new testAmmunition(dmg, mass)).testShoot(this.getPosition, ang, pow)
      
      while(!bullet.isHit) {
        bullet.update(1.0/100)
      }
      bullet.getPosition
    }
  
  //these private classes are only meant to use by testShoot method
  private class testAmmunition(val dmg: Int, val massMultiplier: Double) extends Ammunition(this.world) {
    val description = "test ammo"
    
    def testShoot(startPos: Pos, angle: Int, power: Int): testBullet = {
      new testBullet(startPos, angle, power, this.massMultiplier, this.world, this)
    }
    
    override def explode(position: Pos) = Unit
  }
  
  private class testBullet(startPos: Pos, angle: Int, power: Int, massMultiplier: Double, world: World, 
      ammunition: Ammunition) extends Bullet(startPos, angle, power, massMultiplier, world, ammunition) {
    
    var isHit = false
    
    
    override def removeSelf() = {
      this.isHit = true
    }
  }
  
  
  //for AI-tank animations AIshoot.
  def AIshoot(angle:Int, power:Int){
     //currently doing it's stuff, but not ready one
    //method should be returned immidaetly
    this.AIBarrel = true
    this.AICannonLoaded = true
    this.AIPower = true
    this.AIBarrelValue = Tank.clamp8bit(angle)
    this.AIPowerValue = Tank.clamp8bit(power)
     
   }
  
  //magazine related methdos:
  def addAmmunition(items: Ammunition*): Unit = {
    items.foreach { this.magazine.push(_) }
  }
  
  def addAmmunition(items: Vector[Ammunition]): Unit =items.foreach { this.addAmmunition(_) }
  
  def getMagazine = this.magazine.toVector
  
  def getMagazineSize = this.magazine.length
  
  def getCurrentAmmunition = {
    if(this.magazine.isEmpty) "None"
    else this.magazine.top.toString
  }
  
  
  //GameObject methods:
  
  def getPosition():Pos = this.position
  
  def setPosition(location: Pos) = this.position = location
  
  def typeString = "Tank"
  
  
  //Other methods: 
  
  override def toString = this.id
  
  /**updates tank, responsible for animating a lot of stuff in game*/
  def update(dt:Double) = {
   //drops tank one position down if it does not have ground below it
   if(this.reachedDestination && this.world.gamefield.isEmpty(this.position.down)) {
      this.updateWorld(this.position.down)
      this.isFalling = true
   }
    //triggered if tank is dropped out of the gamefield
    if(!this.world.gamefield.contains(this.position.down)) {
      this.world.gamefield.update(new Empty(this.position), this.position)
      this.causeDmg(10000)
    }
    
   //animates tank to move it's vectorPosition towards it's real position
   
    //determine move direction
    val destination = new Vector2(this.position.x, this.position.y)
    val direction = (destination - this.vectorPosition)
    
    //move if required
    val tmp = World.TANKANIMATIONBOUN
    if(-tmp > direction.x || tmp < direction.x || -tmp > direction.y || tmp < direction.y) {
      if(this.isFalling) this.vectorPosition = this.vectorPosition + new Vector2(0, -10) * dt
      else this.vectorPosition = this.vectorPosition + (direction.unitVector() * World.TANKSPEED*dt)

      
      
    }

    
    //if close enough, make vector position constant
    else {
      this.vectorPosition = new Vector2(this.position.x, this.position.y)
      
      if(this.isFalling) {this.world.sounds.playSound(SoundEngine.groundHit)}
      
      if(!this.reachedDestination) this.world.sounds.stopSound(SoundEngine.tankDrive)
      this.reachedDestination = true
      this.isFalling = false
    }
    
    //if tank is destroyded play explosion animation and trigger end game
    if(this.isDestroyed) {
      this.world.sounds.playSound(SoundEngine.bigExplosion)
      this.world.addExpolsionPosition(this.getPosition)
      this.world.endGame = true
    }
    
    
    //make here object to play move barrel sounds
    this.barrelMovingInt = this.barrelMovingInt + 1
    
    if(this.barrelMoving) {
      this.barrelMovingInt = 0
      this.barrelMoving = false
    }
    
    if((this.barrelMovingInt == 0) && (!this.barrelSoundPlaying)) {
      this.world.sounds.playSound(SoundEngine.tankBarrel)
      this.barrelSoundPlaying = true
    }
    
    
    if(this.barrelMovingInt > 50 && this.barrelSoundPlaying){
      this.world.sounds.stopSound(SoundEngine.tankBarrel)
      this.barrelSoundPlaying = false
    }
    
    //make here object to play change power sounds
    this.powerChangeInt = this.powerChangeInt + 1
    
    if(this.powerChange) {
      this.powerChangeInt = 0
      this.powerChange = false
    }
    
    if((this.powerChangeInt == 0) && (!this.powerSoundPlaying)) {
      this.world.sounds.playSound(SoundEngine.tankPower)
      this.powerSoundPlaying = true
    }
    
    if(this.powerChangeInt > 50 && this.powerSoundPlaying) {
      this.world.sounds.stopSound(SoundEngine.tankPower)
      this.powerSoundPlaying = false
    }
    
    //make here AI tank animations for power and angle settings
    
    if(this.AIBarrel) {
      if(this.shootDirection == this.AIBarrelValue) this.AIBarrel = false
      else {
        if(this.shootDirection < this.AIBarrelValue) this.turnCannonRight(1)
        else this.turnCannonLeft(1)
      }
    }
    
    if(this.AIPower) {
      if(this.shootPower == this.AIPowerValue) this.AIPower = false
      else {
        if(this.shootPower < this.AIPowerValue) this.increaseShootPower(1)
        else this.decreaseShootPower(1)
      }
    }
    
    
    
    if(!this.AIPower && !this.AIBarrel && this.AICannonLoaded) {
      this.AICannonDelay = 50
      this.AICannonLoaded = false
    }
    
    this.AICannonDelay -= 1
    if(this.AICannonDelay < -10) this.AICannonDelay = -5
    
    if(this.AICannonDelay == 0) this.shoot()
    
  }
  
  
}