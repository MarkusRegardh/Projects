package Game
import scala.collection.mutable.Buffer
import o1._
import Game.Constants._

class Game(map: Map) {
  
var hp = initialHP                         //pelaajan HP
var gold = initialGold                           //rahat millä voi ostaa torneja
var wave = 0                         //mones kierros on meneillään
var ticks = 0                           //tick systeemi


var towers = Buffer[Tower]()                                  //Bufferi missä on kentän tornit


var enemies= Buffer[Enemy]()                                  //Bufferi missä kentän viholliset

var enemiesToSpawn = Buffer[Enemy]()                          //Viholliset jotka ei vielä ole spawnattu

def waveOver = {                                              //katsoo onko wave loppunut sekä lisää ensi wavein viholliset
  if (enemies.isEmpty && enemiesToSpawn.isEmpty) {
    wave += 1
    if (wave%4 == 0) {
    enemiesToSpawn = Buffer.tabulate(wave + 5)(n => new speedyBoi(wave, map.start).setHP)
    } else if (wave%7 == 0) {
    enemiesToSpawn = Buffer.tabulate(1)(n => new thickBoi(wave,map.start).setHP)
    } else {
    enemiesToSpawn = Buffer.tabulate(wave + 3)(n => new normal(wave, map.start).setHP)
  }
  }
}

def spawn ={                                                    // spawnaa viholliset jota ei ole vielä spawnattu
   if (!enemiesToSpawn.isEmpty && ticks%20 == 0) {
     enemies = enemies :+ enemiesToSpawn.head
     enemiesToSpawn = enemiesToSpawn.tail
   }
  
}



def upgradeTower(location: GridPos): Unit = {
  towers.find(_.location == location) match {
    case Some(e) => {
      if (gold-e.upgradePrice >= 0) {
      gold = gold - e.upgradePrice
      e.upgrade
    }
    }
    case None =>
  }
 
}



def addSniper(location: GridPos): Unit = {                             //metodit joka lisää tornit (jos mahdollista)
  if (!towers.map(_.location).contains(location) && !map.path.contains(location) && gold-costOfSniper >= 0) {
  towers += new Sniper(location)
  gold = gold - costOfSniper 
}
}

def addAll(location: GridPos): Unit = {                             //metodit joka lisää tornit (jos mahdollista)
  if (!towers.map(_.location).contains(location) && !map.path.contains(location) && gold-costOfAll >= 0) {
  towers += new AllAround(location)
  gold = gold - costOfAll 
}
}

def addSuper(location: GridPos): Unit = {                             //metodit joka lisää tornit (jos mahdollista)
  if (!towers.map(_.location).contains(location) && !map.path.contains(location) && gold-costOfSuper >= 0) {
  towers += new SuperTower(location)
  gold = gold - costOfSuper
}
}

def addClassic(location: GridPos): Unit = {
  if (!towers.map(_.location).contains(location) && !map.path.contains(location) && gold-costOfClassic >= 0) {
 towers += new Classic(location)
   gold = gold - costOfClassic
}
}

def kill = {                                                     // ottaa pois kuolleet viholliset sekä lisää rahaa 
  gold = gold + enemies.filter(_.dead).size * income
  enemies = enemies.filterNot(_.dead)
}

def takeDmg = {                                                  // vihollinen maalissa -> HP menee alas
  hp = hp - enemies.map(_.dealDmg(map)).sum
}

def gameOver = {
  hp<1
}

def timePasses() = {                                              // kaikki mitä tapahtuu joka Tick
  ticks += 1
  spawn
  takeDmg
  kill
  gameOver
  enemies.map(_.moveForward(map))
  towers.map(_.chooseTarget(enemies))
  towers.map(_.tickCounter += 1)
  towers.map(_.shoot)
}
}