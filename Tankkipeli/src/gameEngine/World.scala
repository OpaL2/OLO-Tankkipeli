package gameEngine


import LinkedList.List




class World (val width: Int, val heigth: Int, gravity: Double) {

 
  //creating tank list
  val tankList = List.empty[Tank]
  
  /**creates floor from given vector of Y-axis coordinates*/
  def setFloor(coordinates: Vector[Int]) = ???
  
  /**creates ceiling from given vector of Y-axis coordinates*/
  def setCeiling(coordinates: Vector[Int]) = ???
  
  /**creates tank with given id and start position, inserts it to world*/
  def createTank(id: String, xPos: Int):Tank = ???

  
  def currentTank: Tank = this.tankList.current.get
  
  /**makes currentTank selection to forward by one*/
  def nextTank: Unit = this.tankList.nextItem()
  
  /**updates game to next frame*/
  def update(): Unit = ???
  
}


