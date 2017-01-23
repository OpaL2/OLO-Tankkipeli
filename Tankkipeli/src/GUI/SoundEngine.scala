package GUI

import java.io._
import javax.sound.sampled._


object SoundEngine {
  
  val explosion = Sound("explosion.wav")
  val bigExplosion = Sound("big_explosion.wav")
  val cannonFire = Sound("cannon_fire.wav")
  val groundHit = Sound("ground_hit.wav")
  val lobbyMusic = Sound("lobby_music.wav")
  val music = Sound("music.wav")
  val tankDrive = Sound("tank_Drive.wav")
  val tankBarrel = Sound("tank_barrel.wav")
  val tankPower = Sound("adjust_power.wav")
  val bigHit = Sound("big_hit.wav")
  
  def closeAllStreams() = {
    explosion.close()
    bigExplosion.close()
    cannonFire.close()
    groundHit.close()
    lobbyMusic.close()
    music.close()
    tankDrive.close()
    tankBarrel.close()
    
  }
  
}

class SoundEngine {
  var muted = false
  
  def playSound(sound: Sound) = {
    if(!this.muted) sound.play()
  }
  
  def stopSound(sound: Sound) = {
    sound.stop()
  }
  
  def loopSound(sound: Sound) =  {
    if(!this.muted) sound.loop()
  }
  
  def unmute() = {
    this.muted = false
  }
  
  def mute() = {
    this.muted = true
    SoundEngine.music.stop()
    SoundEngine.lobbyMusic.stop()
  }
}

case class Sound(path: String) {
  private val file = new File("./sounds/" + path)
  private val sound = AudioSystem.getAudioInputStream(file)
  private val clip = AudioSystem.getClip()
  clip.open(sound)
  
  def play() = {
    clip.setFramePosition(0)
    clip.start()
  }
  
  def loop() = {
    clip.loop(Clip.LOOP_CONTINUOUSLY)
  }
  
  def stop() = {
    clip.stop()
  }
  
  def close() = {
     sound.close()
  }
  
}