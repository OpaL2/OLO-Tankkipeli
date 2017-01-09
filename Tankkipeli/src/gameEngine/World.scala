package gameEngine

import org.jbox2d.common.Vec2
import org.jbox2d.dynamics

class World (val width: Int, val heigth: Int, gravity: Double) {
  
  //creating new Box2D world to run this game simulation
  val b2gravity = Conversion.convertGravity(gravity)
  val b2world = new dynamics.World(b2gravity)
  
  /**creates floor from given vector of Y-axis coordinates*/
  def setFloor(coordinates: Vector[Int]) = ???
  
  /**creates ceiling from given vector of Y-axis coordinates*/
  def setCeiling(coordinates: Vector[Int]) = ???
  
  /**creates tank with given id and start position, inserts it to world*/
  def createTank(id: String, yPos: Int):Tank = ???
  
  /**defining size of tank body objects*/
  def setTankSize(xSize: Int, ySize: Int): Unit = ???
  
  def currentTank: Tank = ???
  
  /**makes currentTank selection to forward by one*/
  def nextTank: Unit = ???
  
  /**advances Box2D simulation with given time*/
  def update(time: Double):Unit = ???
  
  def addAmmunition(ammunition: Ammunition) = ???
  
  
}


