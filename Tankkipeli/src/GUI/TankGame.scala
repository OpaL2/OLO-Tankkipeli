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
  val WorldWidth = 60 //gameEngine squares
  val WorldHeight = 40 //gameEngine squares
  val InfoPanelHeight = 50 //in pixels
  val InfoPanelPaddings = 5 //in pixels
  
  val dim = new Dimension(Help.WorldXToUI(WorldWidth), Help.ScaleWorldYToUI(WorldHeight))
  
  def top = new MainFrame {
    title = "TankGame"
    
    preferredSize = dim
    
    val gameWindow = new PaintWorld {
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