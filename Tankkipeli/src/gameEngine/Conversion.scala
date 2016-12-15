package gameEngine

import org.jbox2d.common.Vec2

/**Here is defined all help functions to move between Box2D and pixel coordinates*/
object Conversion {
  
  val SCALE = 100 //scale between screen pixels and Box2D coordinates
  val INVERT_Y = true //boolean value, true if screen origo is at upper left corner
  
  def toBox2DXcoords(pix: Int): Float = ???
  
  def toBox2DYcoords(pix: Int): Float = ???
  
  def toPixYcoords(coords: Float): Int = ???
  
  def toPixXcoords(coords: Float): Int = ???
  
  def convertGravity(gravity: Double): Vec2 = ???
}