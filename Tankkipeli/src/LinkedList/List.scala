package LinkedList

import scala.reflect.ClassTag
import scala.collection.mutable.Buffer

object List {
  
  def empty[ElementType] = new List[ElementType](Vector[ElementType]())
  
  def apply[ElementType](items: ElementType*): List[ElementType] = new List[ElementType](items.toVector)
}

final class List[ElementType](items: Vector[ElementType]) {
  
  private var firstItem: ListItem[ElementType] = NoItem[ElementType]
  private var lastItem: ListItem[ElementType] = NoItem[ElementType]
  private var currentItem: ListItem[ElementType] = NoItem[ElementType]
  
  items.foreach(x => this.append(x))
  
  def append(item: ElementType) = {
    val tmp = new Item[ElementType](item, NoItem[ElementType])
    this.firstItem match {
      case _ : NoItem[ElementType] => {
        this.firstItem = tmp
        this.currentItem = tmp
        this.lastItem = tmp
      }
      case _ : Item[ElementType] => {
        this.lastItem.setNext(tmp)
        this.lastItem = tmp
      }
    }
  }
  
  def nextItem() ={
    this.currentItem = this.currentItem.next
    if (this.currentItem == NoItem[ElementType]) 
      this.currentItem = this.firstItem
  }
  
  def current(): Option[ElementType] = this.currentItem.value
  
  def toVector(): Vector[ElementType] = {
    this.firstItem match {
      case _ : NoItem[ElementType] => Vector.empty[ElementType]
      case _ : Item[ElementType] => {
        val buf = Buffer.empty[ElementType]
        var curr: ListItem[ElementType] = this.firstItem
        while(curr != NoItem[ElementType]) {
          buf.append(curr.value.get)
          curr = curr.next
        }
        buf.toVector
      }
    }
  }
  
  def foreach(f: ElementType => Unit): Unit = {
    this.toVector().foreach(f)
  }
}

private sealed trait ListItem[ItemType] {
  def value:Option[ItemType]
  def next:ListItem[ItemType]
  def setNext(item:ListItem[ItemType]): Unit
}

private final case class NoItem[ItemType]() extends ListItem[ItemType] {
  def value = None
  def next = NoItem[ItemType]
  def setNext(item: ListItem[ItemType]) = ()
}
private final case class Item[ItemType](private val content: ItemType, private var nextItem: ListItem[ItemType]) extends ListItem[ItemType] {
  def value = Some(content)
  def next = this.nextItem
  def setNext(item: ListItem[ItemType]) = this.nextItem = item
}