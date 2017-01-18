package GUI

import scala.math

object Help {
  
  
  //coordination conversion functions:
  
  def WorldXToUI(x: Int): Int = x* TankGame.imageSize
  
  def WorldXToUI(x: Double): Int = math.round(x* TankGame.imageSize).toInt
    
  def WorldYToUI(y: Int): Int = (TankGame.WorldHeight-y)*TankGame.imageSize-TankGame.imageSize
  
  def WorldYToUI(y: Double): Int = math.round((TankGame.WorldHeight - y)*TankGame.imageSize - TankGame.imageSize).toInt
  
  def ScaleWorldYToUI(y: Int): Int = y*TankGame.imageSize
  
}