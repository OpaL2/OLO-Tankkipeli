package gameEngine

import org.jbox2d.common.Vec2

/**Here is defined all help functions to move between Box2D and pixel coordinates*/
object Conversion {
  
  val SCALE = 100 //scale between screen pixels and Box2D coordinates
  val INVERT_Y = true //boolean value, true if screen origo is at upper left corner
  
  def toBox2DXcoords(pix: Int): Float = pix/SCALE.toFloat
  
  def toBox2DYcoords(pix: Int): Float = {
    if(INVERT_Y) 
      -1*pix/SCALE.toFloat
    else 
      pix/SCALE.toFloat
  }
  
  def toPixYcoords(coords: Float): Int = {
    if(INVERT_Y) (-1*coords*SCALE).toInt
    else (coords*SCALE).toInt
  }
  
  def toPixXcoords(coords: Float): Int = (coords*SCALE).toInt
  
  def convertGravity(gravity: Double): Vec2 = {
    if(INVERT_Y) 
  }

}