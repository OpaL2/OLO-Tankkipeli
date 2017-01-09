package gameEngine


import LinkedList.List




class World (width: Int, heigth: Int) {

 
  //creating gamefield and tank list
  val gamefield = new gameEngine.Gamefield(width, heigth)
  val tankList = List.empty[Tank]
  
  /**creates floor from given vector of Y-axis coordinates*/
  def setFloor(coordinates: Vector[Int]) = {
    var x = 0
    coordinates.foreach { y => {
      for(dy <- 0 to y) {
        val pos = new Pos(x,dy)
        this.gamefield.update(new Wall(pos),pos)
      }
      x = x + 1
    }
    }
  }
  
  /**creates ceiling from given vector of Y-axis coordinates*/
  def setCeiling(coordinates: Vector[Int]) = {
    var x = 0
    coordinates.foreach { y => {
      for(dy <- y until this.gamefield.heigth) {
        val pos = new Pos(x, dy)
        this.gamefield.update(new Wall(pos), pos)
      }
      x = x + 1
    }
    }
  }
  
  /**creates tank with given id and start position, inserts it to world*/
  def createTank(id: String, xPos: Int):Tank = ???
  
  /**get current tank in turn*/
  def currentTank: Tank = this.tankList.current.get
  
  /**makes currentTank selection to forward by one*/
  def nextTank: Unit = this.tankList.nextItem()
  
  /**updates game to next frame*/
  def update(): Unit = ???
  
  override def toString() = this.gamefield.toString()
  
}


