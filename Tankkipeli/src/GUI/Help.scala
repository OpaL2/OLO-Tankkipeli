package GUI

object Help {
  
  
  //coordination conversion functions:
  
  def WorldXToUI(x: Int): Int = x* TankGame.imageSize
    
  def WorldYToUI(y: Int): Int = (TankGame.WorldHeight-y)*TankGame.imageSize-TankGame.imageSize
  
  def ScaleWorldYToUI(y: Int): Int = y*TankGame.imageSize
  
}