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
var range = 100000
var fireRate = 100
var target: Option[Enemy] = None
var tickCounter= 0
val location = loc
var price = costOfSniper
var lvl = 1

def upgrade = {
  lvl += 1
  dmg = dmg * 2
}

def chooseTarget(enemies: Buffer[Enemy]) = {
   target = enemies.filter(range >= _.gridLocation.distance(this.location)).headOption
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
  var dmg = 15
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
    dmg = dmg*2
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