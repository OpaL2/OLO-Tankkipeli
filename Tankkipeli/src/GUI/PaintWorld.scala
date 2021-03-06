package GUI

import gameEngine._
import scala.swing._
import java.awt.{Color, Graphics2D, Point, Graphics}
import java.awt.image.BufferedImage
import java.awt.RenderingHints
import event._
import scala.math

import scala.collection.mutable.Buffer



class PaintWorld() extends Panel {
  
  //flag determining if game is running
  var running = false
  
  //flag determining, if this is a first start
  var firstGame = true
  
  //diffculty dialog flags
  var displayDifficulty = false
  var difficulty = 1
  
  //creating game:
  var world = new World(TankGame.WorldWidth, TankGame.WorldHeight, this.difficulty)
  
  world.createTank("Player", GenTerrain.tankLocation(0, TankGame.WorldWidth/2 -1))
  world.createTank("AI", GenTerrain.tankLocation(TankGame.WorldWidth/2,  TankGame.WorldWidth - 1))
  
  world.addAmmosToTanks(10)
  
  def newGame(): Unit = {
    world = new World(TankGame.WorldWidth, TankGame.WorldHeight, this.difficulty)
  
    world.createTank("Player", GenTerrain.tankLocation(0, TankGame.WorldWidth/2 -1))
    world.createTank("AI", GenTerrain.tankLocation(TankGame.WorldWidth/2,  TankGame.WorldWidth - 1))
  
    world.addAmmosToTanks(10)
    
    explosions.clear()

  }
  
  val explosions = Buffer.empty[ExplosionAnimation]
  
  //===================================================================================================================
  //DRAW METHDO STARTS HERE
  //===================================================================================================================
  override def paintComponent(g: Graphics2D) {
    
    val gamefield = world.gamefield
    
    //clearing screen before redraw
    g.clearRect(0, 0, Help.WorldXToUI(TankGame.WorldWidth), Help.ScaleWorldYToUI(TankGame.WorldHeight))
    g.drawImage(Images.game, 0, 0, null)
    
    //=================================================================================================================
    //DRAW WORLD FUNCTION STARTS HERE
    //=================================================================================================================
    
    def drawWorld(): Unit =  {
    //drawing info panel
    
    //drawing frame:
    g.setColor(Color.green)
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
    g.drawString(playerTank.getHP.toString + "/"+ World.TANKHP.toString , 0 + TankGame.InfoPanelPaddings, 30 + TankGame.InfoPanelPaddings)
    g.drawString(playerTank.getFuelLevel.toString + "/" + World.TANKINITIALFUEL.toString, fract + TankGame.InfoPanelPaddings, 30 + TankGame.InfoPanelPaddings)
    g.drawString(playerTank.getMagazineSize.toString, fract * 2 + TankGame.InfoPanelPaddings, 30 + TankGame.InfoPanelPaddings)
    g.drawString(playerTank.getCurrentAmmunition, fract * 3 + TankGame.InfoPanelPaddings, 30 + TankGame.InfoPanelPaddings)
    
    
    //draws gamefield 
    for( y <- 0 until gamefield.height) {
      for( x <- 0 until gamefield.width) {
        if(gamefield.isWall(x, y)){
          //check around tile if there is not tile on one or more side, draw side tile.
          //also draw those half blocks, check them.
          var img = Images.tileFull
          
          //if tile up is empty
          if(!gamefield.isWall(x, y + 1)) {
            img = Images.tileUp
            //left slope
            if(!gamefield.isWall(x-1,y)) {
              img = Images.tileLeftRight
            }
            //right slope
            else if(!gamefield.isWall(x+1, y)) {
              img = Images.tileRightLeft
            }
            //top cone
            if(!gamefield.isWall(x-1,y) && !gamefield.isWall(x+1,y)) {
              img = Images.tilePeak
              
              //if floating around
              if(!gamefield.isWall(x,y-1)) {
                img = Images.tileFull
              }
            }
          }
          //if tile up contains wall
          else {
            //left straight wall
            if(!gamefield.isWall(x-1,y)) {
              img = Images.tileLeft
            }
            //right straight wall
            else if(!gamefield.isWall(x+1,y)) {
              img = Images.tileRight
            }
            
            //both straight walls
            if(!gamefield.isWall(x-1, y) && !gamefield.isWall(x+1, y)) {
              img = Images.tileLeftAndRight
            }
          }
          
          
          g.drawImage(img, Help.WorldXToUI(x), Help.WorldYToUI(y), null)
        }
      }
    }
    
    //draws tanks
    world.getTanks.foreach( x => {
      g.drawImage(Images.tank, Help.WorldXToUI(x.vectorPosition.x),
          Help.WorldYToUI(x.vectorPosition.y)+ 3, null)
      drawBarrel(x)
      drawNameLabel(x)
      }
    )
    
    def drawBarrel(tank: Tank): Unit = {
    //draws shoot pointer to gamefield
      val len = TankGame.imageSize /2
      val offset = TankGame.imageSize/2 -1
      val pos = tank.vectorPosition
      val angle =math.Pi*(255 - tank.getCannonAngle)/255 //angle in deg
      val x0 = Help.WorldXToUI(pos.x) + offset
      val y0 = Help.WorldYToUI(pos.y) + offset +3
      val x1 = math.round((x0 + math.cos(angle)* len)).toInt
      val y1 = math.round((y0 - math.sin(angle)* len)).toInt  
      
      g.setColor(Color.black)
      g.drawLine(x0, y0, x1, y1)
    }
    
    def drawNameLabel(tank: Tank): Unit = {
      val pos = tank.vectorPosition
      val x = Help.WorldXToUI(pos.x)
      val y = Help.WorldYToUI(pos.y) - 5
      val label = tank.id
      g.setColor(Color.green)
      g.drawString(label, x, y)
    }
    
    def drawPowerIndicator(tank: Tank): Unit = {
      //draws shoot power indicator for tank
      val pos = tank.vectorPosition
      val size = TankGame.imageSize - 4
      val width = TankGame.imageSize/4
      val relativeHeight= tank.getShootPower.toDouble/255
      val height = (TankGame.imageSize*relativeHeight*1.5).toInt
      val x = Help.WorldXToUI(pos.x) + (size*1.2).toInt - width + 6
      val y = Help.WorldYToUI(pos.y) + size - height
      
      g.setColor(new Color((relativeHeight*255).toInt,255-(relativeHeight*255).toInt,0))
      g.fillRect(x, y, width, height)
      g.setColor(Color.black)
      g.drawRect(x,y,width, height)
    }
    
    drawPowerIndicator(playerTank)
    
    //draw bullets
    def drawBullet(bullet: Bullet): Unit = {
      var img = Images.cannonball
      bullet.ammunition match {
        case x:BasicAmmunition => img = Images.cannonball
        case x:HeavyAmmunition => img = Images.missile
      }
      val direction = bullet.getSpeedVector
      val pos = bullet.getPositionVector
      val x = Help.WorldXToUI(pos.x)
      val y = Help.WorldYToUI(pos.y)
      var rotation = math.Pi/2 - math.asin(direction.y)
      if(direction.x < 0)
        rotation = -rotation

      
      g.rotate(rotation, x + img.getWidth/2, y + img.getHeight/2)
      g.drawImage(img, x, y, null)
      g.rotate(0.0, img.getWidth/2, img.getHeight/2)
      
    }
    
    if(!world.bulletBuffer.isEmpty) {
      world.bulletBuffer.foreach { drawBullet(_) }
    }
    
    
    //create explosions
    
    if(world.isExpolded()) {
      val pos = world.getExplosionPosition
      this.explosions.append(ExplosionAnimation(pos, TankGame.imageSize*3))
    }
    
    //draw explosions
    def drawExplosions(): Unit = {
      this.explosions.foreach{ x => {
        if(x.active) g.drawImage( x.getNextImage(), Help.WorldXToUI(x.pos.x) - x.size/3, Help.WorldYToUI(x.pos.y) - x.size/3, null)
        }
      }
      val removeBuf = Buffer.empty[Int]
      this.explosions.foreach { x => if(!x.active) removeBuf.append(this.explosions.indexOf(x)) }
      removeBuf.foreach { this.explosions.remove(_)}
    }
    
    drawExplosions
    }
    
    //=================================================================================================================
    //DRAW WORLD FUNCTION ENDS HERE
    //=================================================================================================================
    
    
    //draw mesh if game over
    def drawEnd(): Unit = {
      for (x <- 0 until gamefield.width) {
        for(y <- 0 until gamefield.height) {
          g.drawImage(Images.mesh, Help.WorldXToUI(x), Help.WorldYToUI(y), null)
        }
      }
      
      val y = Help.ScaleWorldYToUI(TankGame.WorldHeight) / 6
      val x = Help.WorldXToUI(TankGame.WorldWidth)/2
      val width = x/2
      
      g.setColor(Color.white)
      scaleFont(5)
      g.drawString("Game over!", x - width/2- 20, y)
      scaleFont(1.0/5)
      scaleFont(7)
      
      var label ="Draw"
      val tanks = world.getTanks
      val Player = tanks.filter(_.id == "Player")(0)
      val Ai = tanks.filter(_.id == "AI")(0)
      if(Player.isDestroyed){
        g.setColor(Color.red)
        label = "You Lost"
      }
      if(Ai.isDestroyed){
        g.setColor(Color.blue)
        label = "You Won"
      }
      if(Player.isDestroyed && Ai.isDestroyed) {
        g.setColor(Color.white)
        label = "Draw"
      }
      
      g.drawString(label, x - width/2- 30, y* 2)
      
      scaleFont(1.0/7)
      g.setColor(Color.white)
      
      g.drawString("Press enter to play again", x- width/2 + 60 , y*2 + 30)
      
      g.setColor(Color.black)
      
    }

    def scaleFont(amount: Double) = {
      val currentFont = g.getFont
      val newFont = currentFont.deriveFont(currentFont.getSize * amount.toFloat)
      g.setFont(newFont)
    }
    
    def drawStart(): Unit = {
      g.clearRect(0, 0, Help.WorldXToUI(TankGame.WorldWidth), Help.ScaleWorldYToUI(TankGame.WorldHeight))
      g.setColor(Color.white)
      g.drawImage(Images.menu,0,0,null)
      scaleFont(5)
      g.drawString("The Tank game", 200, 200)
      g.drawString("Press ENTER to start", 140, 300)
      scaleFont(1.0/5)
     
      
      //draw control section
      val x = Help.WorldXToUI(TankGame.WorldWidth) -350
      g.drawString("Controls:",x,100)
      
      def drawControlElement(name: String, action: String, x: Int, y: Int) = {
        g.drawString(name,x,y)
        g.drawString("=",x +100, y)
        g.drawString(action, x + 150, y)
      }
      drawControlElement("Left arrow", "move left", x, 120)
      drawControlElement("Right arrow", "move right", x, 140)
      drawControlElement("Up Arrow", "turn cannon right", x, 160) 
      drawControlElement("Down Arrow", "turn cannon left", x, 180)
      drawControlElement("W","increase cannon power", x, 200)
      drawControlElement("S","decrease cannon power", x, 220)
      drawControlElement("Space","shoot",x,240)
      drawControlElement("M","mute sounds",x,260)
      drawControlElement("Enter","Start new game/select",x, 280)

      world.sounds.loopSound(SoundEngine.lobbyMusic)
    }
  
    
    def drawSelectDifficulty(): Unit = {
      g.clearRect(0, 0, Help.WorldXToUI(TankGame.WorldWidth), Help.ScaleWorldYToUI(TankGame.WorldHeight))
      g.drawImage(Images.menu,0,0,null)
      
      scaleFont(5)
      
      def drawDifficultyRect(width: Int, height: Int, x: Int, y: Int, label: String, hilighted: Boolean) = {
        val x0 = x - width/2
        val y0 = y - height/2
        val pad = 10
        
        g.setColor(Color.black)
        g.fillRect(x0, y0, width, height)
        
        if (hilighted) g.setColor(Color.green)
        else g.setColor(Color.white)
        
        g.drawRect(x0, y0, width, height)
        g.drawRect(x0 + pad, y0 +pad, width - pad*2, height - pad*2)
        g.drawString(label, x0+ 2*pad, y0 + 2*pad + height/2)
        
        g.setColor(Color.black)
        
      }
      
      
      val y = Help.ScaleWorldYToUI(TankGame.WorldHeight) / 6
      val x = Help.WorldXToUI(TankGame.WorldWidth)/2
      val width = x/2
      val height = y -20
      
      drawDifficultyRect(width, height, x, y*2, "Easy", difficulty == 1)
      drawDifficultyRect(width, height, x, y*3, "Normal", difficulty == 2)
      drawDifficultyRect(width, height, x, y*4, "Hard", difficulty == 3)
      g.setColor(Color.white)
      g.drawString("Select Difficulty:", x - width/2- 20, y)
      
      scaleFont(1.0/5)
      
    }
    
    //selects based on flags what to draw
    
    if(running) drawWorld()
    if(world.endGame) {
      drawWorld()
      drawEnd()
    } 
    
     if(firstGame) drawStart()
    if(displayDifficulty) drawSelectDifficulty()
    
  }
  
  //===================================================================================================================
  //DRAW PART END HERE
  //===================================================================================================================
  
  
  //timer for requesting updates, this stuff is runned regulary
  
  val timer = Timer(1000/100) {
    world.update(1.0/100)
    this.repaint()
    if(world.endGame) stop()
  }
  
  var StopCounter = 10 //after game has ended animate 10 frames
  
  def stop(): Unit = {
    if(StopCounter > 0) {
      StopCounter = StopCounter - 1
    }
    else{
      //this is triggered after game is finished
      timer.stop()
      running = false
      world.sounds.stopSound(SoundEngine.music)
      world.sounds.loopSound(SoundEngine.lobbyMusic)
    }
  }
  
  def startGame(): Unit = {
    timer.start
    world.sounds.loopSound(SoundEngine.music)
    world.sounds.stopSound(SoundEngine.lobbyMusic)
    running = true
    firstGame = false
  }
  
  def playerTank(): Boolean = world.currentTank.id == "Player" && running && !world.endTurn
  
  //key listener
  listenTo(keys)
  reactions += {
    case KeyPressed(_, Key.Left,  _, _) => {
      if(playerTank() && world.currentTank.reachedDestination) 
        world.currentTank.moveLeft()
    }
    case KeyPressed(_, Key.Right, _, _) => {
      if(playerTank()&& world.currentTank.reachedDestination)
        world.currentTank.moveRight()
    }
    
    case KeyPressed(_, Key.Up, _, _) => {
      if(playerTank())
        world.currentTank.turnCannonRight(1)
      else if(this.displayDifficulty) {
        if(this.difficulty > 1) this.difficulty -= 1
        this.repaint()
        world.sounds.playSound(SoundEngine.groundHit)
      }
    }
    
    case KeyPressed(_, Key.Down, _, _) => {
      if(playerTank())
        world.currentTank.turnCannonLeft(1)
      else if(this.displayDifficulty) {
        if(this.difficulty < 3) this.difficulty += 1
        this.repaint()
        world.sounds.playSound(SoundEngine.groundHit)
      }
    }
    case KeyPressed(_, Key.W, _, _) => {
      if(playerTank()) {
        world.currentTank.increaseShootPower(2)
      }
    }
    case KeyPressed(_, Key.S, _, _) => {
      if(playerTank()) {
        world.currentTank.decreaseShootPower(2)
      }
    }
    
    case KeyPressed(_, Key.Space, _, _) => {
      if(playerTank()){
        world.currentTank.shoot()
      }
    }
    
    case KeyPressed(_, Key.M, _, _) => {
      if(world.sounds.muted) {
        world.sounds.unmute()
        if(running) world.sounds.loopSound(SoundEngine.music)
        else SoundEngine.lobbyMusic.loop()
      }
      else world.sounds.mute()
    }
    
    case KeyPressed(_, Key.Enter, _, _) => {
      if(!running) {
        if(!this.displayDifficulty){
          this.displayDifficulty = true
          this.repaint()
          this.world.sounds.playSound(SoundEngine.groundHit)
        }
        else {
          if(this.displayDifficulty) this.displayDifficulty = false
          this.newGame()
          this.startGame()
          this.world.sounds.playSound(SoundEngine.bigExplosion)
        }
      }
    }
  }
  
  focusable = true
  requestFocus
  
  case class ExplosionAnimation(pos: Pos, size: Int) {
    private val images = Vector(Images.loadImage("explosion_1.png", size),
                                Images.loadImage("explosion_2.png", size),
                                Images.loadImage("explosion_3.png", size),
                                Images.loadImage("explosion_4.png", size),
                                Images.loadImage("smoke.png", size))
   private var n = -1
   var active = true
   private var ret = images(0)
   
   def getNextImage(): BufferedImage= {
      n = n + 1
      if(n < images.size*2) {
        ret = images(n/2)
        ret
      }
      else {
        active = false
        ret
      }
    }
  }
  
}
