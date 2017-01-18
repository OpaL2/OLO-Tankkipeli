package GUI

import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.RenderingHints

/**object storing all images used in game, also contains function for creating them*/
object Images {
  
    def loadImage(path: String) = {
  
    val image: BufferedImage = ImageIO.read(new File("./assets/tank.png"))
    val scaledImage = new BufferedImage(TankGame.imageSize,TankGame.imageSize,image.getType())
    val d = scaledImage.createGraphics()
    d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
    d.drawImage(image, 0,0, TankGame.imageSize, TankGame.imageSize,0,0, image.getWidth(), image.getHeight(), null)
    d.dispose()
    
    scaledImage
  }
  
}