

package gameEngine

import scala.math

// AI is the class for all AI operations it takes difficulty and World as input
class AI (val difficulty : Int, world: World){
  
  //just easier not beautiful
  val r :String = "right"
  val l :String = "left"
 
  
  // move takes in positions and spits out right,left or null (direction of AI movement)
  def move(ownp: Pos, enemyp: Pos) = {
      
    val remainingammo = world.currentTank.getMagazineSize
    val remainingfuel = world.currentTank.getFuelLevel
   
    if (remainingfuel > 0) {
      if (remainingammo >0 ){
     if (ownp.x > enemyp.x && ownp.y >1 && world.currentTank.moveLeft == true && world.gamefield.doIFall(ownp.x-1, ownp.y) == false ) l
     else if (ownp.x < enemyp.x && ownp.y >1 && world.currentTank.moveRight == true && world.gamefield.doIFall(ownp.x+1, ownp.y) == false ) r
     else null
   } 
   else if (ownp.x > enemyp.x && ownp.y >1 && world.currentTank.moveRight == true && world.gamefield.doIFall(ownp.x+1, ownp.y) == false ) r
   else if (ownp.x < enemyp.x && ownp.y >1 && world.currentTank.moveLeft == true&& world.gamefield.doIFall(ownp.x-1, ownp.y) == false) l
   else null
    }
    else null
  }
  
  //shoot takes in positions and spits out parametres for shooting it 
  def shoot(ownp: Pos, enemyp: Pos) = {
    
    val leftOrRight = (if (ownp.x >enemyp.x) r else l)
    // angle = 0-255 90deg=127,5 45deg=63,75 135deg=191,25 
    var shootingAngle = (if (leftOrRight == "right") 191 else 64  )
    
    var tankdistance =  math.sqrt((ownp.x + enemyp.x)^2 + (ownp.y + enemyp.y)^2 )
    
    var currentAmmo = world.currentTank.getCurrentAmmunition
    
    world.currentTank.testshoot(ownp, shootingAngle, power, currentAmmo)
    
    var hitdistance =
      
      
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