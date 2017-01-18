package GUI

import gameEngine._
import scala.swing._
import java.awt.{Color, Graphics2D}
import event._

object TankGame extends SimpleSwingApplication {
  
  val floor = Vector.fill(500)(100)
  val ceiling = Vector.fill(500)(400)
  val world = new World(500, 500)
  val dim = new Dimension(500, 500)
  val blue = new Color(0, 255, 0)
  
  world.setFloor(floor)
  world.setCeiling(ceiling)
  world.createTank("Samuel", 100)
  world.createTank("Ismael", 400)
  
  def onDraw(g: Graphics2D) = {
    
  }
  
  val mainTable = new Table(500, 500) {
    preferredSize = dim
    focusable = true
    
  }
  
  def paint(g: Graphics2D) {
    g.setColor(blue)
    
  }
  
  def top = new MainFrame {
    title = "TankGame"
    contents = mainTable
  }
  
}