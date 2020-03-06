package Game
import scala.collection.mutable.Buffer
import o1._
import Game.Constants._

class Game(map: Map) {
  
var hp = 50
var gold = 50
var wave = 1



var towers = Buffer[Tower](new Sniper(new GridPos(10,10)))

var enemies= Buffer[Enemy](new Enemy(10,map.start,2))

var enemiesToSpawn = Buffer[Enemy]()

def spawn = {
  if (enemies.isEmpty && enemiesToSpawn.isEmpty) {
    
  }
}




def addSniper(location: GridPos) = {
  towers += (new Sniper(location))
}

def addClassic(location: GridPos) = {
  towers += (new Classic(location))
}

def kill = {
  gold = gold + enemies.filter(_.dead).size * income
  enemies = enemies.filterNot(_.dead)
}

def takeDmg = {
  hp = hp - enemies.map(_.dealDmg(map)).sum
}


def timePasses() = {
  takeDmg
  kill
  enemies.map(_.moveForward(map))
  towers.map(_.chooseTarget(enemies))
  towers.map(_.tickCounter += 1)
  towers.map(_.shoot)
}
}