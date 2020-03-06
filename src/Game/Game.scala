package Game
import scala.collection.mutable.Buffer
import o1._
import Game.Constants._

class Game(map: Map) {
  
var hp = 20                             //pelaajan HP
var gold = 50                           //rahat millä voi ostaa torneja
var wave = 1                            //mones kierros on meneillään
var ticks = 0                           //tick systeemi


var towers = Buffer[Tower]( new Classic(new GridPos(5,4)))    //Bufferi missä on kentän tornit

var enemies= Buffer[Enemy](new Enemy(15,map.start,2))         //Bufferi missä kentän viholliset

var enemiesToSpawn = Buffer[Enemy]()                          //Viholliset jotka ei vielä ole spawnattu

def waveOver = {                                              //katsoo onko wave loppunut sekä lisää ensi wavein viholliset
  if (enemies.isEmpty && enemiesToSpawn.isEmpty) {
    wave += 1
    enemiesToSpawn = Buffer.tabulate(wave + 3)(n => new Enemy(wave*2 + 10, map.start, 2))
  }
  
}

def spawn ={                                                    // spawnaa viholliset jota ei ole vielä spawnattu
   if (!enemiesToSpawn.isEmpty && ticks%10 == 0) {
     enemies = enemies :+ enemiesToSpawn.head
     enemiesToSpawn = enemiesToSpawn.tail
   }
  
}



def addSniper(location: GridPos) = {                            //metodi joka lisää tornit
  towers += (new Sniper(location))
}

def addClassic(location: GridPos) = {
  towers += (new Classic(location))
}

def kill = {                                                     // ottaa pois kuolleet viholliset sekä lisää rahaa 
  gold = gold + enemies.filter(_.dead).size * income
  enemies = enemies.filterNot(_.dead)
}

def takeDmg = {                                                  // vihollinen maalissa -> HP menee alas
  hp = hp - enemies.map(_.dealDmg(map)).sum
}


def timePasses() = {                                              // kaikki mitä tapahtuu joka Tick
  ticks += 1
  waveOver
  spawn
  takeDmg
  kill
  enemies.map(_.moveForward(map))
  towers.map(_.chooseTarget(enemies))
  towers.map(_.tickCounter += 1)
  towers.map(_.shoot)
}
}