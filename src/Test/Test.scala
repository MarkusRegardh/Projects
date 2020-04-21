package Test

import Game._
import TDGUI._
import java.io._
import org.scalatest._
import o1.GridPos._
import scala.collection.mutable.Buffer

class Test extends FlatSpec {
  
  val map1 =  world.loader(new File("./Maps/map1.txt"))
  val game1 = new Game(map1)
  var gp = game1.gold
  
  "A map" should "have corrent path placement" in {
    assert(map1.path.contains(new o1.GridPos(1,2)))
    assert(map1.path.contains(o1.GridPos(5,11)))
  }
  
  "Game" should "not place tower ontop of path" in {
    game1.addClassic(o1.GridPos(1,2))
    assert(game1.towers.isEmpty)
  }
  
  "Towers" should "correctly place on the map" in {
     game1.addClassic(o1.GridPos(10,10))
     assert(game1.gold != gp)
     assert(game1.towers.head.location == o1.GridPos(10,10))
    }
  
   game1.gold = game1.gold + 100
   gp = game1.gold
   
   
   "Game "should "Not place tower when not enough gold" in {
     game1.addSuper(o1.GridPos(4,2))
     assert(game1.towers.size == 1)
   }
  
  
  "Towers" should "upgrade correctly" in {
    val originalDmg = game1.towers.head.dmg
    game1.upgradeTower(o1.GridPos(10,10))
    assert(game1.towers.head.lvl == 2)
    assert(game1.towers.head.dmg != originalDmg)
    assert(game1.gold != gp)
  }
  gp=game1.gold
  
  "Enemies" should "die when in going through the map" in {
    game1.enemies = Buffer(new normal(1,map1.start).setHP) 
    for (i <- 0 to 2000) {
      game1.enemies.map(_.moveForward(map1))
      game1.towers.map(_.chooseTarget(game1.enemies))
      game1.towers.map(_.shoot)
      game1.towers.map(_.tickCounter += 1)
      game1.ticks += 1
      game1.kill
    }
    assert(game1.enemies.isEmpty && gp!=game1.gold)
  }
  
  var map2 = world.loader(new File("./Maps/map2.txt"))
  var game2 = new Game(map2)
  
  "Enemies" should "deal dmg when going through map without towers" in {
    game2.enemies = Buffer(new normal(10000,map2.start).setHP)
    var enemyX = map2.start.x.toDouble
    var enemyY = map2.start.y.toDouble
    assert(!game2.enemies.isEmpty && game2.towers.isEmpty)
    while (!game2.enemies.isEmpty) {
      assert(game2.enemies.head.x != enemyX || game2.enemies.head.y != enemyY)
      enemyX = game2.enemies.head.x 
      enemyY = game2.enemies.head.y
      game2.timePasses()
    }
    game2.timePasses()
     assert(game2.enemies.isEmpty && Constants.initialHP!=game2.hp && Constants.initialGold!=game2.gold)
  }
  
  "Waveover" should "spawn a new set of enemies" in {
    game2.waveOver
    assert(!game2.enemiesToSpawn.isEmpty)
    for (i <- 0 to 200) {
      game2.timePasses()
    }
    assert(game2.enemiesToSpawn.isEmpty && !game2.enemies.isEmpty)
  }
  
}