package GUI

object tester extends App {
  
  val n = new GenTerrain
  
  val b = n.generate(10, 100, 1)
  
  println(b)
}
