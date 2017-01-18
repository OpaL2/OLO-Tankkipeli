package GUI

import gameEngine._
import scala.swing._
import javax.swing.ImageIcon
import java.net.URL
import java.awt.{Color, Graphics2D, Point, Graphics}
import java.awt.image.BufferedImage
import java.awt.RenderingHints
import event._
import java.io.File
import javax.imageio.ImageIO


class paintWorld(world: World) extends Panel {
  
  val image: BufferedImage = ImageIO.read(new File("./assets/tank.png"))
  val scaledImage = new BufferedImage(20,20,image.getType())
  val d = scaledImage.createGraphics()
  d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
  d.drawImage(image, 0,0, 20, 20,0,0, image.getWidth(), image.getHeight(), null)
  d.dispose()


  
  override def paintComponent(g: Graphics2D) {
    g.drawImage(scaledImage,0,0,null)
    
  }
  
  
  
}
