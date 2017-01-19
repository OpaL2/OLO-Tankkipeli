

package gameEngine

import scala.math

// AI is the class for all AI operations it takes difficulty as a parametre in.
class AI (val difficulty : Int){
  
  //just easier not beautiful
  val r :String = "right"
  val l :String = "left"
 
  
  // move takes in positions and spits out right,left or null (direction of AI movement)
  def move(ownposition: Pos, enemyposition: Pos) = {
      
    val remainingammo = world.currentTank.getMagazineSize
    val remainingfuel = world.currentTank.getFuelLevel
   
    if (remainingfuel > 0) {
      if (remainingammo >0 ){
     if (ownposition.x > enemyposition.x && ownposition.y >1  ) l
     else if (ownposition.x < enemyposition.x && ownposition.y >1 ) r
     else null
   } 
   else if (ownposition.x > enemyposition.x && ownposition.y >1  ) r
   else if (ownposition.x < enemyposition.x && ownposition.y >1 ) l
   else null
    }
    else null
  }
  
  
  
  //shoot takes in positions and spits out parametres for shooting it 
  def shoot(ownposition: Pos, enemyposition: Pos) = {
    ???
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