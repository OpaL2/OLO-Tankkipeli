package gameEngine

import org.jbox2d.dynamics
import org.jbox2d.collision.shapes

object Tank {
  
  //size of tank
  var XSIZE = 1f
  var YSIZE = 1f
  
  def setSize(x: Float, y:Float) = {
    Tank.XSIZE = x
    Tank.YSIZE = y
  }
  
  //body parameters
  
  //other parameters, such as max amount of fuel
  val MAXFUEL = 500
  val MAXAMMOLOAD = 500
  
  //box2d definitions:
  //Body definition:
  val b2TankBodyDef = new dynamics.BodyDef()
  b2TankBodyDef.setType(dynamics.BodyType.DYNAMIC) //setting tank body to dynamic body type
  
  //Fixture definition:
  //defining shape for tank hit box (here rectangle)
  val b2TankShape = new shapes.PolygonShape()
  b2TankShape.setAsBox(this.YSIZE, this.XSIZE)
  
  //fixture definition
  val b2TankFixtureDef = new dynamics.FixtureDef()
  b2TankFixtureDef.shape = this.b2TankShape
  b2TankFixtureDef.density = 0.5f
  b2TankFixtureDef.friction = 0.5f
  b2TankFixtureDef.restitution = 0.5f
  
  //factory method for creating tanks
  def apply(id: String, world: World, xPos: Int) = {
  
    //setting position for tank, y value must be calculated so that tank rests on ground
    Tank.b2TankBodyDef.position.set(xPos, ???)
    
    //creating body for tank
    val b2TankBody = world.b2world.createBody(Tank.b2TankBodyDef)
  }
}

class Tank(val id: String,val body: dynamics.Body) {
  
  /**applying force to left, trying to move tank*/
  def moveLeft(): Unit = ???
  
  /**applying force to right, trying to move tank*/
  def moveRight(): Unit = ???
  
  def turnCannonLeft(): Unit = ???
  
  def turnCannonRight(): Unit = ???
  
  def increaseShootPower(): Unit = ???
  
  def decreaseShootPower(): Unit = ???
  
  def getPosition():(Int, Int) = ???
  
  def shoot(): Unit = ???
  
  
}