package GUI

import gameEngine._
import scala.swing._
import java.awt.{Color, Graphics2D, Point, Graphics}
import java.awt.image.BufferedImage
import java.awt.RenderingHints
import event._
import gameEngine.Pos
import gameEngine.World



class paintWorld(val world: World) extends Panel {
  
  override def paintComponent(g: Graphics2D) {
    
    val gamefield = world.gamefield
    
    //clearing screen before redraw
    g.clearRect(0, 0, Help.WorldXToUI(TankGame.WorldWidth), Help.ScaleWorldYToUI(TankGame.WorldHeight))
    g.setBackground(Color.WHITE)
    
    
    //drawing info panel
    
    //drawing frame:
    g.setColor(Color.BLACK)
    g.drawRect(0, 0, Help.WorldXToUI(TankGame.WorldWidth), TankGame.InfoPanelHeight)
    val fract = Help.WorldXToUI(TankGame.WorldWidth)/4
    g.drawLine(fract, 0, fract, TankGame.InfoPanelHeight)
    g.drawLine(fract*2, 0, fract*2, TankGame.InfoPanelHeight)
    g.drawLine(fract*3, 0, fract*3, TankGame.InfoPanelHeight)
    
    //drawing lables:
    g.drawString("Health:", 0 + TankGame.InfoPanelPaddings, 10 + TankGame.InfoPanelPaddings)
    g.drawString("Fuel:", fract + TankGame.InfoPanelPaddings, 10 + TankGame.InfoPanelPaddings)
    g.drawString("Ammunition:", fract * 2 + TankGame.InfoPanelPaddings, 10 + TankGame.InfoPanelPaddings)
    g.drawString("Next ammunition:", fract * 3 + TankGame.InfoPanelPaddings, 10 + TankGame.InfoPanelPaddings)
    
    //drawing data:
    val playerTank = world.getTanks.filter {_.id == "Player"}(0)
    g.drawString(playerTank.getHp.toString + "/"+ World.TANKHP.toString , 0 + TankGame.InfoPanelPaddings, 30 + TankGame.InfoPanelPaddings)
    g.drawString(playerTank.getFuelLevel.toString + "/" + World.TANKINITIALFUEL.toString, fract + TankGame.InfoPanelPaddings, 30 + TankGame.InfoPanelPaddings)
    g.drawString(playerTank.getMagazineSize.toString, fract * 2 + TankGame.InfoPanelPaddings, 30 + TankGame.InfoPanelPaddings)
    g.drawString(playerTank.getCurrentAmmunition, fract * 3 + TankGame.InfoPanelPaddings, 30 + TankGame.InfoPanelPaddings)
    
    
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
  
  
  //timer for requesting updates
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
