package GUI

import gameEngine._
import scala.swing._
import java.awt.{Color, Graphics2D}
import event._

object TankGame extends SimpleSwingApplication {
  
  val imageSize = 15 //image size in pixels
  
  val floor = Vector.fill(50)(10)
  val ceiling = Vector.fill(50)(40)
  val world = new World(50, 50)
  val dim = new Dimension(1000, 1000)
  val blue = new Color(0, 255, 0)
  
  world.setFloor(floor)
  world.setCeiling(ceiling)
  world.createTank("Samuel", 10)
  world.createTank("Ismael", 40)
  
  def top = new MainFrame {
    title = "TankGame"
    
    preferredSize = dim
    
    val gameWindow = new paintWorld(world) {
      preferredSize = dim
    }
    
    contents = gameWindow
    
    this.peer.setResizable(false) //setting window to non resizeable
    this.peer.setLocationRelativeTo(null)
  }
  
}

