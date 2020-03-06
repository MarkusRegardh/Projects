package Game
import o1._
import scala.collection.mutable.Buffer

abstract class Tower(loc: GridPos) {
  
  val color: Color
  var dmg: Int
  val name: String
  var range: Int
  var fireRate: Double
  var target: Option[Enemy]
  var tickCounter: Int
  val location: GridPos
  
def upgrade: Unit

def shoot: Unit

def chooseTarget(enemies: Buffer[Enemy]): Unit

}

class Sniper(loc: GridPos) extends Tower(loc){
 
var dmg = 50
val color= Red
val name = "Sniper"
var range = 100000
var fireRate = 30
var target: Option[Enemy] = None
var tickCounter= 0
val location = loc

def upgrade = {
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
  val color = Blue
  val name = "Classic"
  var range = 10
  var fireRate = 10
  var target: Option[Enemy] = None
  var tickCounter= 0
  val location = loc
  
  def chooseTarget(enemies: Buffer[Enemy]) = {
   target = enemies.filter(range >= _.gridLocation.distance(this.location)).headOption
  }
  
  def upgrade = {
    fireRate = fireRate*2
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