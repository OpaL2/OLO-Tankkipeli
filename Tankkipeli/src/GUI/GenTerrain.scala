package GUI

import scala.util.Random
import scala.math._

/* This class generates random vector for gameField creation. It takes base value for distance from the
 * bottom or top of the screen. Length is the length of the vector needed and intensity affects the 
 * differences in the terrain.*/ 

class GenTerrain {
  
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
  
  //Counts average of two numbers and returns int.
  
  private def average(a: Int, b: Int) = (a+b)/2
  
}