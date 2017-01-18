package GUI

import gameEngine._
import scala.swing._
import scala.swing.event._
import java.awt.{Color, Graphics2D}
import java.awt.event._
import event._

object TankGame extends SimpleSwingApplication {
  
  //constants defining rest of the game
  val imageSize = 20 //image size in pixels, also defines size of single game square
  val WorldWidth = 60
  val WorldHeight = 40
  
  val world = new World(WorldWidth, WorldHeight)
  val dim = new Dimension(Help.WorldXToUI(WorldWidth), Help.ScaleWorldYToUI(WorldHeight))
  val blue = new Color(0, 255, 0)
  
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


/** timer object for triggering redraw and update*/
object Timer {
  def apply(interval: Int, repeats: Boolean = true)(op: => Unit) {
    val timeOut = new javax.swing.AbstractAction() {
      def actionPerformed(e : java.awt.event.ActionEvent) = op
    }
    val t = new javax.swing.Timer(interval, timeOut)
    t.setRepeats(repeats)
    t.start()
  }
}