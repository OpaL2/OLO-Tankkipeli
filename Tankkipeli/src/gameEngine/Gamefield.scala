package gameEngine

import scala.collection.mutable.Buffer

class Gamefield(val width: Int, val heigth: Int) {
  
  private val contents = Buffer.tabulate[GameObject](heigth, width)((y,x) => new Empty(new Pos(x,y)))
  
  def update(element: GameObject, location: Pos) = {
    this.contents(location.y)(location.x) = element
    element.setPosition(location)
  }
  
  def elementAt(location: Pos) = this.contents(location.y)(location.x)
  
  def apply(location: Pos) = this.contents(location.y)(location.x)
  
  def isEmpty(location: Pos): Boolean = this.apply(location).typeString == "Empty"
  
  def isWall(location: Pos): Boolean = this.apply(location).typeString == "Wall"
  
  override def toString() = {
    var str = ""
    this.contents.foreach(y => {
      y.foreach(x => {
      x.typeString match {
        case "Empty" => str = str + " "
        case "Wall" => str = str + "#"
        case "Tank" => str = str + "T"
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
  
  
}

class Empty(private var pos: Pos) extends GameObject {
  
  def getPosition = this.pos
  
  def setPosition(location: Pos) = this.pos = location
  
  def typeString = "Empty"
  
}

class Wall(private var pos: Pos) extends GameObject {
  def getPosition = this.pos
  
  def setPosition(location: Pos) = this.pos = location
  
  def typeString = "Wall"
  
}

class Pos(val x: Int, val y: Int)