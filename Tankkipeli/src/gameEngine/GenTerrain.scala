package gameEngine

import scala.util.Random
import scala.math.abs
import scala.Vector
import scala.collection.mutable.Buffer

/* This class generates random vector for gameField creation. It takes base value for distance from the
 * bottom or top of the screen. Length is the length of the vector needed and intensity affects the 
 * differences in the terrain.*/ 

object GenTerrain {
  
  val randy = new Random
  
  /* Random terrain recursive generating method. It makes a new vector according to given parameters */
  
  def generate(base: Int, length: Int, factor: Int): Vector[Int] = {
     
    if(length >= 2 && base >= 0) {
      
      val empty: Array[Int] = Array.fill(length)(0)
      empty(0) = base
      empty(empty.size - 1) = abs(base + randy.nextInt()%factor)
      empty(empty.size/2) = this.average(empty.last, empty.head) + randy.nextInt()%factor
      generate(empty, factor)
     
     } else {
       Vector(0)
     }  
  }
  
  
  def generate(container: Array[Int], factor: Int): Vector[Int] = {
    
    if(!container.contains(0)) {
      container.toVector
    } else {
      val first = container.indexWhere( _ <= 0 )
      val second = first + container.slice(first, container.size).indexWhere(_ != 0)
      
      val locAverage = this.average(first - 1, second)
      val loc = if(locAverage == first - 1) first else locAverage
      
      val value = abs(this.average(container(first - 1), container(second)) + randy.nextInt()%factor)
      container(loc) = if(value == 0) 1 else value
      
      generate(container, factor)
    }
  }
  
  // the tank location picker (first most useless comment)
  
  def tankLocation(start: Int, end: Int) = start + abs(randy.nextInt() % (end - start))
  
  // fills the clips (most pointless comment)
  
  def fillClips(tanks: Int, clips: Int, world: World): Vector[Vector[Ammunition]] = {
    
    val basicAmount = abs(randy.nextInt % clips)
    val heavyAmount = clips - basicAmount
    
    def ammoSuffler = {
      val ammo = Buffer[Ammunition]()
      
      var b = basicAmount
      var h = heavyAmount
      
      while (b > 0 || h > 0) {
        if(abs(randy.nextInt()%2) == 0 && b > 0) {
          ammo += new BasicAmmunition(world)
          b -= 1
        } else if(h > 0){
          ammo += new HeavyAmmunition(world)
          h -= 1
        } else {
          ammo += new BasicAmmunition(world)
          b -= 1
        }
      }
      ammo.toVector
    }
    
    Vector.tabulate(tanks)(i => ammoSuffler)
    
  }
  
  /* Counts average of two numbers and returns int. */
  
  private def average(a: Int, b: Int) = (a+b)/2
  
}