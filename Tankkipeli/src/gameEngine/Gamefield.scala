package gameEngine

import scala.collection.mutable.Buffer

class Gamefield(val width: Int, val height: Int) {
  
  private val contents = Buffer.tabulate[GameObject](height, width)((y,x) => new Empty(new Pos(x,y)))
  
  def update(element: GameObject, location: Pos) = {
    this.contents(location.y)(location.x) = element
    element.setPosition(location)
  }
  
  def elementAt(location: Pos) = this.contents(location.y)(location.x)
  
  def apply(location: Pos): GameObject = this.contents(location.y)(location.x)
  
  def apply(x: Int, y: Int): GameObject = this.apply(new Pos(x,y))
  
  def isEmpty(location: Pos): Boolean = this.contains(location) && this.apply(location).typeString == "Empty"
  
  def isEmpty(x: Int, y: Int): Boolean = this.isEmpty(new Pos(x,y))
  
  def isWall(location: Pos): Boolean = this.contains(location) && this.apply(location).typeString == "Wall"
  
  def isWall(x: Int, y: Int): Boolean = this.isWall(new Pos(x,y))
  
  def isTank(location: Pos): Boolean = this.contains(location)  && this.apply(location).typeString == "Tank"
  
  def isTank(x: Int, y: Int): Boolean = this.isTank(new Pos(x,y))
  
  private def nearTank(location: Pos): Boolean = this.isTank(location) || this.isTank(location.up) || this.isTank(location.down)
  
  def canMove(location: Pos): Boolean = this.contains(location)&&this.isEmpty(location)&&(!this.nearTank(location))
  
  def doIFall(x: Int, y: Int): Boolean = this.doIFall(new Pos(x,y))
  
  def doIFall(pos: Pos): Boolean = {
    var position = pos
    while(this.contains(position)) {
      if(this.isWall(position)) return false
      position = position.down
    }
    true
  }
 
  
  def contains(x: Int, y: Int): Boolean = 
    x >= 0 && x < this.width && 
    y >= 0 && y < this.height 
    
  def contains(location: Pos): Boolean = this.contains(location.x, location.y)
  
  /**gets content of gamefield object as vector, notice coordinates are (y,x)*/
  def toVector(): Vector[Vector[GameObject]] = this.contents.toVector.map(_.toVector)
  
  override def toString() = {
    var str = ""
    this.contents.reverse.foreach(y => {
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
  
  def getHP: Int
}

abstract class DestroyableObject(private var Hp: Int) extends GameObject {
  def getPosition: Pos
  
  def causeDmg(dmg: Int): Unit = {
    this.Hp = this.Hp - dmg
    if(this.Hp <0) this.Hp = 0
  }
  
  def getHP = this.Hp
  
  def isDestroyed: Boolean = this.Hp <= 0
}

class Empty(private var pos: Pos) extends GameObject {
  
  def getPosition = this.pos
  
  def setPosition(location: Pos) = this.pos = location
  
  def typeString = "Empty"
  
  def isDestroyed = false
  
  def causeDmg(dmg: Int): Unit = Unit  
  
  def getHP = -100
}

class Wall(private var pos: Pos) extends DestroyableObject(World.WALLHP) {
  
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