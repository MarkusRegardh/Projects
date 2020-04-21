package Game
import o1.GridPos
import scala.collection.mutable.Buffer
import java.awt.{ Graphics2D, Color, BasicStroke }
import Constants._

abstract class Tower(loc: GridPos) {
  
  val color: Color  
  var dmg: Int                    // tornin dmg numero
  val name: String       
  var range: Int                  // kuinka pitkälle torni voi ampua
  var fireRate: Double            // kuinka nopeasti torni ampuu (joka n ticki)
  var target: Option[Enemy]       //mikä vihollinen on tähtäimessä, jos mikään
  var tickCounter: Int            // fireratein apuun
  val location: GridPos   
  var price: Int
  var upgradePrice = costOfUpgrade
  var lvl: Int
  
def upgrade: Unit                 //päivittää esim. dmgen tai fireratin

def shoot: Unit                   //ampuu targettia 

def chooseTarget(enemies: Buffer[Enemy]): Unit     //valitsee targetin(se vihollinen joka on rangein sisällä ja joka on liikkunut eniten)

}

class Sniper(loc: GridPos) extends Tower(loc){         
 
var dmg = 50
val color= Color.RED
val name = "Sniper"
var range = 100
var fireRate = 100
var target: Option[Enemy] = None
var tickCounter= 0
val location = loc
var price = costOfSniper
var lvl = 1

def upgrade = {
  lvl += 1
  dmg = dmg + 20
}

def chooseTarget(enemies: Buffer[Enemy]) = {
   target = enemies.filter(range >= _.gridLocation.distance(this.location)).sortBy(_.hp).lastOption
  }

def shoot= {
  if (tickCounter%fireRate == 0) {
    target match {
      case Some(enemy) => enemy.hp = enemy.hp - dmg
      case None => 
    }
    tickCounter = 1
  }
}
}

class Classic(loc: GridPos) extends Tower(loc){
  var dmg = 5
  val color = Color.BLUE
  val name = "Classic"
  var range = 4
  var fireRate = 20
  var target: Option[Enemy] = None
  var tickCounter= 0
  val location = loc
  var price = costOfClassic
  var lvl = 1
  
  def chooseTarget(enemies: Buffer[Enemy]) = {
   target = enemies.filter(range >= _.gridLocation.distance(this.location)).headOption
  }
  
  def upgrade = {
    lvl += 1
    dmg = dmg + 3
  }
  def shoot= {
  if (tickCounter%fireRate == 0) {
    target match {
      case Some(enemy) => enemy.hp = enemy.hp - dmg
      case None => 
    }
    tickCounter = 1
  }
}
}

class AllAround(loc: GridPos) extends Tower(loc) {
  var dmg = 8
  val color = Color.GREEN
  val name = "AllAround"
  var range = 3
  var fireRate = 50
  var target: Option[Enemy] = None
  var targets = Buffer[Enemy]()
  var tickCounter= 0
  val location = loc
  var price = costOfAll
  var lvl = 1
  
  def chooseTarget(enemies: Buffer[Enemy]) = {
    targets = enemies.filter(range >= _.gridLocation.distance(this.location))
  }
  
  
  
  def upgrade = {
    lvl += 1
    range = range + 1
    dmg = dmg + 3
  }
  def shoot= {
  if (tickCounter%fireRate == 0) {
   for (a <- targets){
     a.hp = a.hp - dmg
    }
    tickCounter = 1
  }
}   
}
class SuperTower(loc: GridPos) extends Tower(loc){
  var dmg = 40
  val color = Color.BLACK
  val name = "Super Tower"
  var range = 8
  var fireRate = 5
  var target: Option[Enemy] = None
  var tickCounter= 0
  val location = loc
  var price = costOfSuper
  var lvl = 1
  
  def chooseTarget(enemies: Buffer[Enemy]) = {
   target = enemies.filter(range >= _.gridLocation.distance(this.location)).headOption
  }
  
  def upgrade = {
    lvl += 1
    dmg = dmg + 5
  }
  def shoot= {
  if (tickCounter%fireRate == 0) {
    target match {
      case Some(enemy) => enemy.hp = enemy.hp - dmg
      case None => 
    }
    tickCounter = 1
  }
}
}