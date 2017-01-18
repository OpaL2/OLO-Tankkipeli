package GUI

import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.RenderingHints

/**object storing all images used in game, also contains function for creating them*/
object Images {
    
  
  /**loads images from given path, and scales them to TankGame.imageSize size*/  
  def loadImage(path: String) = {
  
    val image: BufferedImage = ImageIO.read(new File("./assets/" + path))
    val scaledImage = new BufferedImage(TankGame.imageSize,TankGame.imageSize,image.getType())
    val d = scaledImage.createGraphics()
    d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
    d.drawImage(image, 0,0, TankGame.imageSize, TankGame.imageSize,0,0, image.getWidth(), image.getHeight(), null)
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
}