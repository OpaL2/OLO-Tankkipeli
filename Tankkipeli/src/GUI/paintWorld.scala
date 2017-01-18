package GUI

import gameEngine._
import scala.swing._
import javax.swing.ImageIcon
import java.net.URL
import java.awt.{Color, Graphics2D, Point, Graphics}
import java.awt.image.BufferedImage
import java.awt.RenderingHints
import event._



class paintWorld(val world: World) extends Panel {
  
  override def paintComponent(g: Graphics2D) {
    
    val gamefield = world.gamefield
    
    for( y <- 0 until gamefield.height) {
      for( x <- 0 until gamefield.width) {
        if(gamefield.isWall(x, y)) g.drawImage(Images.tileFull, Help.WorldXToUI(x), Help.WorldYToUI(y), null)
        else if(gamefield.isTank(x, y)) g.drawImage(Images.tank, Help.WorldXToUI(x), Help.WorldYToUI(y), null)
      }
    }
    
  }
  
  
  
}
