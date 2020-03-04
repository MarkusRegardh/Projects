package Game
import o1._

abstract class Tower(Color: Color, initialDmg: Int, name: String, range: Int) {
  
def upgrade: Unit

def shoot(enemy: Enemy): Unit
  
}