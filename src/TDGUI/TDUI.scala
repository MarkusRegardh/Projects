package TDGUI
import scala.swing._
import scala.swing.event
import swing._
import event._
import BorderPanel.Position._
import java.awt.{ Graphics2D, Color, BasicStroke }
import Game.Constants
import Game.Game
import Game.FileReader._
import java.io._
import o1.GridPos
import java.awt.RenderingHints
import java.awt.event.ActionListener

object TDUI extends SimpleSwingApplication{
  
  def top = new MainFrame {
    title = "Tower Defence"
    resizable = false
    
    var savedPoint: Option[GridPos] = None
    
    val width = Constants.width
    val height = Constants.height + Constants.uiHeight
    
    minimumSize   = new Dimension(width,height)
    preferredSize = new Dimension(width,height)
    maximumSize   = new Dimension(width,height)
    
    
   val addClassic = new Action("Add Classic: " + Constants.costOfClassic.toString() + "GP") {
    def  apply = {
      if (savedPoint != None) {
      game.addClassic(savedPoint.get)
      savedPoint = None
    }
    }
    }
    
    val addSniper = new Action("Add Sniper: " + Constants.costOfSniper.toString() + "GP") {
    def  apply = {
      if (savedPoint != None) {
      game.addSniper(savedPoint.get)
      savedPoint = None
    }
    }
    }
    
    val upgradeTower = new Action("Upgrade: " + Constants.costOfUpgrade.toString() + "GP") {
      def apply = {
        if (savedPoint != None) {
         game.upgradeTower(savedPoint.get)
         savedPoint = None
        }
      }
    }
    
    val classicInfo = new Action("Classic") {
      def apply = {
        Dialog.showMessage(top,"The Classic tower is a medium range and medium DPS tower")
        }
      }
    
    val sniperInfo = new Action("Sniper") {
      def apply = {
        Dialog.showMessage(top, "The Sniper tower is a longrange, high dmg but slow tower")
      }
    }
    
    val howTo = new Action("How to") {
      def apply = {
        Dialog.showMessage(top, "Press a location on the map to place a tower or to upgrade an existing tower \n Press Next Wave to spawn in a set of enemies \n Use gold to buy towers, and make sure your HP doesn't drop down to 0")
      }
    }
    
   
    
    val infoPopup = new PopupMenu {
      contents += new Menu("Towers") {
        contents += new MenuItem ("Classic") { 
          this.action_=(classicInfo)
        }
        contents += new MenuItem ("Sniper") {
          this.action_=(sniperInfo)
        }
      }
      contents += new MenuItem("How to") {
       this.action_=(howTo)
      }
    }
    
    
     
    
    val popupMenu = new PopupMenu {
    contents += new Menu("Add Towers") {
      contents += new MenuItem ("Add Classic")   {
        this.action_=(addClassic)
      }
      contents += new MenuItem ("Add Sniper") {
        this.action_=(addSniper)
      }
    }
    contents += new MenuItem("Upgrade Towers") {
      this.action_=(upgradeTower)
    }
  }
    

    
    this.menuBar = new MenuBar {
      contents += new MenuItem(Action("Next Wave") { game.waveOver } )
      contents += new MenuItem(Action("Info"){infoPopup.show(this, 200, 20)})
      contents += new MenuItem(Action("Quit") { dispose() } )
    }
  
    
    
    
    
    val mapTest = new File("./Maps/TestMap.txt")
    val map = world.loader(mapTest)
    val game = new Game(map)

    
    def drawEnemies(g: Graphics2D) = {
      for (a <- game.enemies) {
        if (a.isdmged){
          g.setColor(a.dmgColor)
        } else {
        g.setColor(a.color)
      }
        g.fillOval(a.x.toInt + a.size/2, a.y.toInt + a.size/2, Constants.pixelsPerGridSquare-a.size, Constants.pixelsPerGridSquare-a.size)
      }
      
    }
    
    def drawTowers(g: Graphics2D) = {
      for (a <- game.towers) {
        g.setColor(a.color)
        g.fillRect(toPixelPos(a.location)._1 + 2,toPixelPos(a.location)._2 + 2, Constants.pixelsPerGridSquare - 4, Constants.pixelsPerGridSquare - 4)
        g.setColor(Color.WHITE)
        g.drawString(a.lvl.toString(), toPixelPos(a.location)._1 + 17,toPixelPos(a.location)._2 + 24)
      }
    }
    
    
    def drawMap(g: Graphics2D) = {
      for (a <- map.path) {
        g.setColor(Color.GRAY)
        g.fillRect(toPixelPos(a)._1,toPixelPos(a)._2, Constants.pixelsPerGridSquare, Constants.pixelsPerGridSquare)
      }
    }
    
   def drawWave (g: Graphics2D) = {
     g.setColor(Color.BLACK)
     g.drawString("Wave: " + game.wave.toString(), Constants.uiWidth/13, Constants.height+Constants.uiHeight/8+18)
    }
   
    
    
    def drawGold (g: Graphics2D) = {
      g.setColor(Color.YELLOW)
      g.fillOval(Constants.uiWidth/15, Constants.height+Constants.uiHeight/8+60, 50, 50)
      g.drawString(game.gold.toString() + "GP",Constants.uiWidth/6, Constants.height+Constants.uiHeight/8+88)
    }
    
    def drawHP (g: Graphics2D) = {
       g.setColor(Color.RED)
      g.fillRoundRect(Constants.uiWidth/15, Constants.height+Constants.uiHeight/2, 50, 50,30,30)
      g.drawString(game.hp.toString() + "HP", Constants.uiWidth/6 , Constants.height+Constants.uiHeight/2 + 28)
    }
    
    def drawInfo (g: Graphics2D) {
        g.setColor(Color.LIGHT_GRAY)
        g.fillRect(0, Constants.height, Constants.uiWidth, Constants.uiHeight)
        drawGold(g)
        drawHP(g)
        drawWave(g)
    }
    
    val over = new Panel {
      override def paintComponent(g: Graphics2D) = {
        g.setColor(Color.BLACK)
        g.fillRect(0,0,width,height)
        g.setColor(Color.RED)
        g.drawString("GAME OVER", width/2, height/2)
      }
    }
    
    
    
    val field = new Panel {
     var a = listenTo(mouse.clicks)
      reactions += {
        case e: MouseClicked => {
          if (e.point.y<Constants.height) {
          popupMenu.show(this, e.point.x, e.point.y)
          savedPoint = Option(toGridPos(e.point.x,e.point.y))
     }
        }
      }
      override def paintComponent(g: Graphics2D) = {
        g.setColor(new Color(237,201,175))
        g.fillRect(0, 0, Constants.width, Constants.height)
        drawMap(g)
        drawEnemies(g)
        drawTowers(g)
        drawInfo(g)
      }
    }
    
   
    contents = field
    
    def toGridPos(x: Int, y: Int): GridPos = {
      new GridPos(x/Constants.pixelsPerGridSquare, y/Constants.pixelsPerGridSquare)
    }
    
    def toPixelPos(gridPos: GridPos): (Int,Int) = (gridPos.x * Constants.pixelsPerGridSquare, gridPos.y * Constants.pixelsPerGridSquare)
    
     val listener = new ActionListener(){
      def actionPerformed(e : java.awt.event.ActionEvent) = {
        if (game.gameOver) {
          contents=over
        } else
        field.repaint()  
        game.timePasses()
      }
      }
    
    val timer = new javax.swing.Timer(Constants.gameSpeed, listener)
    timer.start()
    
  
  }
  
 
  
  
  
  
  
  
  
}