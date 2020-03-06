package TDGUI
import o1._
import Game._
import Game.Constants._
import scala.swing._
import scala.swing.BorderPanel.Position._
import event._
import java.awt.{ Color, Graphics2D }
import scala.util.Random
import FileReader._
import java.io._

object TD extends App {
  val backGround = rectangle(width,height,Green)
  var gamePic = backGround
  val mapTest = new File("./Maps/TestMap.txt")
  val map = world.loader(mapTest)
  val tdg = new Game(map)
  val pathPic = square(pixelsPerGridSquare, Gray)
  val ui = rectangle(uiWidth,uiHeight,White)
  val goldPic = circle(25,Gold)
  gamePic = gamePic.place(ui, toPixelPos(new GridPos(20,36)))
  val mapPath = map.path.map(n => toPixelPos(n))
  gamePic = mapPath.foldLeft(gamePic)((a,b)=> a.place(pathPic, b))
  
  
  def enemyPic(enemy: Enemy) = if (enemy.isdmged)circle(15, Red) else circle(15,Pink)

  val gui = new View (tdg,gameSpeed,"Tower Defence") {
    def makePic = {
      
      gamePic = tdg.towers.foldLeft(gamePic)((a,b)=> a.place(square(pixelsPerGridSquare,b.color), toPixelPos(b.location)))
      tdg.enemies.foldLeft(gamePic)((a,b)=> a.place(enemyPic(b),(b.pLocation)))
     }
     override def onTick() {
       tdg.timePasses()
     }
     override def isDone = {
       tdg.hp < 1
     }
     }
  
 
  
  def toPixelPos(gridPos: GridPos) = Pos(gridPos.x * pixelsPerGridSquare, gridPos.y * pixelsPerGridSquare)
  
  gui.start()
  
}