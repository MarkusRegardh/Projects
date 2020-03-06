package Game
import o1._
import TDGUI.TD._

class Enemy(initialHP: Int, start: GridPos, speed: Int) {
  
var hp = initialHP

var v = speed

var tickC = 0

var gridLocation = start

var x = toPixelPos(start).x

var y = toPixelPos(start).y

var pLocation = toPixelPos(start)




var dir = South
  
def dead = (hp<=0) 

def move(d: CompassDir) = {
  if (d == South) {
    y += speed
  } else if (d == North) {
    y -= speed
  } else if (d == East) {
    x += speed
  } else if (d == West) {
    x -= speed
  }
  
  pLocation = new Pos(x,y)
}


def dealDmg(map: Map) = {
  if (gridLocation == map.goal) {
    this.hp=0
    1
} else {
  0
}
}

def moveForward(map: Map) = {
  while (!map.path.contains(gridLocation.neighbor(dir))){
    dir = dir.clockwise
    if (!map.path.contains(gridLocation.neighbor(dir))){
      dir = dir.counterClockwise.counterClockwise
    }
  }
  tickC += 1
  move(dir)
  if (tickC%20 == 0) {
    gridLocation = gridLocation.neighbor(dir)
    tickC = 0
  }
}
}