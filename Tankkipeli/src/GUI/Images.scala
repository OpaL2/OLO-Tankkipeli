package GUI

import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.RenderingHints

/**object storing all images used in game, also contains function for creating them*/
object Images {
    
  
  /**loads images from given path, and scales them to TankGame.imageSize size*/  
  def loadImage(path: String, size: Int = TankGame.imageSize) = {
  
    val image: BufferedImage = ImageIO.read(new File("./assets/" + path))
    val scaledImage = new BufferedImage(size,size,image.getType())
    val d = scaledImage.createGraphics()
    d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
    d.drawImage(image, 0,0, size, size,0,0, image.getWidth(), image.getHeight(), null)
    d.dispose()
    
    scaledImage
  }
  
    
  //terraing images
  val tileLeftRight = loadImage("ramp_left_right.png")
  val tileRightLeft = loadImage("ramp_right_left.png")
  val tileFull = loadImage("tile_full.png")
  val tileDown = loadImage("tile_down.png")
  val tileLeft = loadImage("tile_left.png")
  val tileRight = loadImage("tile_right.png")
  val tileUp = loadImage("tile_up.png")
  //val tileUpCone = ???
  //val tileDownCone = ???
  
  //tank image
  val tank = loadImage("tank.png")
  
  //ammunition images
  val cannonball = loadImage("cannonball.png")
  val missile = loadImage("missile.png")
  
  //explosion images
  val expl1 = loadImage("explosion_1.png")
  val expl2 = loadImage("explosion_2.png")
  val expl3 = loadImage("explosion_3.png")
  val expl4 = loadImage("explosion_4.png")
  val expl5 = loadImage("smoke.png")
}