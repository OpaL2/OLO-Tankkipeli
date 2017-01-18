package GUI

import gameEngine._
import scala.swing._
import javax.swing.ImageIcon
import java.net.URL
import java.awt.{Color, Graphics2D, Point, Graphics}
import java.awt.image.BufferedImage
import java.awt.RenderingHints
import event._
import gameEngine.Pos



class paintWorld(val world: World) extends Panel {
  
  override def paintComponent(g: Graphics2D) {
    
    val gamefield = world.gamefield
    
    //clearing screen before redraw
    g.clearRect(0, 0, Help.WorldXToUI(TankGame.WorldWidth), Help.ScaleWorldYToUI(TankGame.WorldHeight))
    
    //prints gamefield to GUI window
    for( y <- 0 until gamefield.height) {
      for( x <- 0 until gamefield.width) {
        if(gamefield.isWall(x, y)){
          //check around tile if there is not tile on one or more side, draw side tile.
          //also draw those half blocks, check them.
          val img = Images.tileFull
          g.drawImage(img, Help.WorldXToUI(x), Help.WorldYToUI(y), null)
        }
        else if(gamefield.isTank(x, y)) g.drawImage(Images.tank, Help.WorldXToUI(x), Help.WorldYToUI(y), null)
      }
    }
    
  }
  
  Timer(100/20) {
    world.update(100/20)
    this.repaint()
  }
  
  //key listener
  listenTo(keys)
  reactions += {
    case KeyPressed(_, Key.Left,  _, _) => {
      world.currentTank.moveLeft()
    }
    case KeyPressed(_, Key.Right, _, _) => {
      world.currentTank.moveRight()
    }
  }
  
  focusable = true
  requestFocus
  
  
  
}
