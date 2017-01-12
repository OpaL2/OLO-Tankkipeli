package gameEngine

import scala.collection.mutable.Buffer

class Gamefield(val width: Int, val height: Int) {
  
  private val contents = Buffer.tabulate[GameObject](height, width)((y,x) => new Empty(new Pos(x,y)))
  
  def update(element: GameObject, location: Pos) = {
    this.contents(location.y)(location.x) = element
    element.setPosition(location)
  }
  
  def elementAt(location: Pos) = this.contents(location.y)(location.x)
  
  def apply(location: Pos) = this.contents(location.y)(location.x)
  
  def isEmpty(location: Pos): Boolean = this.contains(location) && this.apply(location).typeString == "Empty"
  
  def isWall(location: Pos): Boolean = this.contains(location) && this.apply(location).typeString == "Wall"
  
  def isTank(location: Pos): Boolean = this.contains(location)  && this.apply(location).typeString == "Tank"
  
  private def nearTank(location: Pos): Boolean = this.isTank(location) || this.isTank(location.up) || this.isTank(location.down)
  
  def canMove(location: Pos): Boolean = this.contains(location)&&this.isEmpty(location)&&(!this.nearTank(location))
  
  private def contains(x: Int, y: Int): Boolean = 
    x >= 0 && x < this.width && 
    y >= 0 && y < this.height 
    
  def contains(location: Pos): Boolean = this.contains(location.x, location.y)
  
  override def toString() = {
    var str = ""
    this.contents.foreach(y => {
      y.foreach(x => {
      x.typeString match {
        case "Empty" => str = str + " "
        case "Wall" => str = str + "#"
        case "Tank" => str = str + "T"
        case "Bullet" => str = str + "B"
      }
    })
    str = str + ";\n"
  })
    str
  }
}

trait GameObject {
  
  def getPosition():Pos
  
  def setPosition(location: Pos): Unit
  
  def typeString: String
  
  def causeDmg(dmg: Int): Unit
  
  def isDestroyed: Boolean
}

abstract class DestroyableObject(private val Hp: Int) {
  def getPosition: Pos
  
  def causeDmg(dmg: Int): Unit = this.Hp - dmg
  
  def getHp = this.Hp
  
  def isDestroyed: Boolean = this.Hp <= 0
}

class Empty(private var pos: Pos) extends GameObject {
  
  def getPosition = this.pos
  
  def setPosition(location: Pos) = this.pos = location
  
  def typeString = "Empty"
  
  def isDestroyed = false
  
  def causeDmg(dmg: Int): Unit = Unit  
}

class Wall(private var pos: Pos) extends DestroyableObject(World.WALLHP) with GameObject {
  
  def getPosition = this.pos
  
  def setPosition(location: Pos) = this.pos = location
  
  def typeString = "Wall"
}

class Pos(val x: Int, val y: Int) {
  
  def left: Pos = new Pos(this.x - 1, this.y)
  
  def right: Pos = new Pos(this.x + 1, this.y)
  
  def up: Pos = new Pos(this.x, this.y + 1)
  
  def down: Pos = new Pos(this.x, this.y - 1)
  
  override def equals(other: Any) = {
    other match {
      case other : Pos => this.x == other.x && this.y == other.y
      case _ => false
    }
  }
}