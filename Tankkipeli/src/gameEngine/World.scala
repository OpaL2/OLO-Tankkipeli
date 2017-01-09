package gameEngine

import org.jbox2d.common.Vec2
import org.jbox2d.dynamics
import LinkedList.List

object World {
  val VELCITER = 6
  val POSITER = 2
}

<<<<<<< HEAD
class World (val width: Int, val heigth: Int, gravity: Double) {
=======

class World (width: Int, heigth: Int, gravity: Double) {
  
  val worldWidth = Conversion.toBox2DXcoords(width)
  val worldHeigth = Conversion.toBox2DYcoords(heigth)
>>>>>>> refs/remotes/origin/master
  
  //creating new Box2D world to run this game simulation
  val b2gravity = Conversion.convertGravity(gravity)
  val b2world = new dynamics.World(b2gravity)
<<<<<<< HEAD
=======
  
  //creating tank list
  val tankList = List.empty[Tank]
>>>>>>> refs/remotes/origin/master
  
  /**creates floor from given vector of Y-axis coordinates*/
  def setFloor(coordinates: Vector[Int]) = ???
  
  /**creates ceiling from given vector of Y-axis coordinates*/
  def setCeiling(coordinates: Vector[Int]) = ???
  
  /**creates tank with given id and start position, inserts it to world*/
  def createTank(id: String, xPos: Int):Tank = ???
  
  /**defining size of tank body objects*/
  def setTankSize(xSize: Int, ySize: Int): Unit = {
    Tank.setSize(Conversion.toBox2DXcoords(xSize), Conversion.toBox2DYcoords(ySize))
  }
  
  def currentTank: Tank = this.tankList.current.get
  
  /**makes currentTank selection to forward by one*/
  def nextTank: Unit = this.tankList.nextItem()
  
  /**advances Box2D simulation with given time*/
  def update(time: Double):Unit = this.b2world.step(time.toFloat, World.VELCITER, World.POSITER)
  
  def addAmmunition(ammunition: Ammunition) = ???
  
  
}


