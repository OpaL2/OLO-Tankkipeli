

package gameEngine

import scala.util.Random
import scala.math


// AI is the class for all AI operations it takes difficulty and World as input
class AI (val difficulty : Int, world: World){
  
  //just easier not beautiful
  val r :String = "right"
  val l :String = "left"
  
  val rand = new Random()
  
  var movesToTake = 0
  var startedTurn = false
  var shooted = false
  
  def initTurn() = {
    this.startedTurn = true
    this.movesToTake = math.abs(this.rand.nextInt()) % (this.difficulty+4)
    this.shooted = false
  }
 
  
  // move takes in positions and spits out right,left or null (direction of AI movement)
  def move(ownp: Pos, enemyp: Pos): String = {
      
    val remainingammo = world.currentTank.getMagazineSize
    val remainingfuel = world.currentTank.getFuelLevel
   
    if (remainingfuel > 0) {
      if (remainingammo >0 ){
     if (ownp.x > enemyp.x && ownp.y >1 && world.currentTank.moveLeft == true && world.gamefield.doIFall(ownp.x-1, ownp.y) == false ) return l
     else if (ownp.x < enemyp.x && ownp.y >1 && world.currentTank.moveRight == true && world.gamefield.doIFall(ownp.x+1, ownp.y) == false ) return r
     else return null
   } 
   else if (ownp.x > enemyp.x && ownp.y >1 && world.currentTank.moveRight == true && world.gamefield.doIFall(ownp.x+1, ownp.y) == false ) return r
   else if (ownp.x < enemyp.x && ownp.y >1 && world.currentTank.moveLeft == true&& world.gamefield.doIFall(ownp.x-1, ownp.y) == false) return l
   else return null
    }
    else return null
  }
  
  //shoot takes in positions and spits out parametres for shooting it 
  def shoot(ownp: Pos, enemyp: Pos): Unit = {
    
    val enemyPosition = enemyp
    // distance between tanks
    val startAngle = 128
    // ammo that has been selected
    var angle = startAngle
    // shooting power
    var power = 10
    // test hit location
    
    if(this.world.currentTank.getMagazineSize == 0) this.world.currentTank.AIshoot(angle, power) //passes turn to next tank if have no ammo
    
    var hitPos = this.world.currentTank.testshoot(angle, power)
    //distance between testhit location and target  
    while(true) {
      if(hitPos == enemyPosition || power > 255){
        
        //in this case we shoot and return from this function
        if(power > 255) {
          angle = if(ownp.x > enemyp.x) 70 else 180
        }
        variableShoot()
        this.shooted = true
        return
      }
      else if( error > 0) {
        //error is positive
        positiveError
      }
      else {
        //error is negative
        negativeError
      }
    
      
      hitPos = this.world.currentTank.testshoot(angle, power)
    }
    
    def variableShoot() = {
      //add variable amount of error, based on difficulty
      
      this.world.currentTank.AIshoot(angle + randOffset,power + randOffset)
    }
    
    def error(): Int = enemyPosition.x - hitPos.x
    
    def negativeError() = {
      if(angle >= 0) angle -= 1
      else {
        power +=1
        angle = startAngle
      }
    }
    
    def positiveError() = {
      if(angle <= 255) angle += 1
      else {
        power += 1
        angle = startAngle
      }
    }
    
    def randOffset: Int = {
      val sing = this.rand.nextBoolean()
      val amount = this.rand.nextInt() % (40 - this.difficulty*10)
      if(sing) -amount
      else amount
    }
    
  } 
   

}


/* 
in difficulty 


-------------UI loops this!-------------
ai move (ownposition, enemyposition)
get remaining fuel
get enemy direction
	if enemy left
	check if left clear if clear left
	else if --> if clear right
	else dont move
	if enemy right --> samat jutut

return (movedirection) 





ai shoot (ownposition, enemyposition)
get enemy direction left or right

check magazine
if empty move away---> ruuuuuun!!!


select best weapon

precalculate shoot 
	if right 45deg
	if left 135deg
	set to var try_angle

simulate shot (try_angle)
	shoot(ownpos,tryangle, calculate needed power) 
	if hit tryangle = shootangle
	if miss tryangle adjust (difficulty)
	
	shoot(ownpos,tryangle + adjust , calculate needed power) 
	shoot(ownpos,tryangle - adjust , calculate needed power)

adjust = adjust + difficulty

---> until hit
if not hit shoot random :)

Return shoot (ownpos, shootangle, powerneeded + randommodifier(difficulty))








*/