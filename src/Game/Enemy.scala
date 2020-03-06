package Game
import o1._
import TDGUI.TD._
import Constants._

class Enemy(initialHP: Int, start: GridPos, speed: Int) {
  
var hp = initialHP

var v = speed                            //monta pixeliä/tick

var tickC = 0                            //tick systeemi

var gridLocation = start

var x = toPixelPos(start).x

var y = toPixelPos(start).y

var pLocation = toPixelPos(start)

def isdmged = {
 this.hp != initialHP 
}

var dir = South
  
def dead = (hp<=0) 

def move(d: CompassDir) = {               //Mihin suuntaan liikutaan
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


def dealDmg(map: Map) = {                  //pelaaja ottaa dmg jos vihollinen maalissa, sekä tappaa vihollisen
  if (gridLocation == map.goal) {
    this.hp=0
    1
} else {
  0
}
}

def moveForward(map: Map) = {                                //liikkuu gridissä sekä pixeleissä (että liikkuvuus olisi sujuvaa), katsoo myös mihin suntaan kartan "path" liikkuu 
  while (!map.path.contains(gridLocation.neighbor(dir))){
    dir = dir.clockwise
    if (!map.path.contains(gridLocation.neighbor(dir))){
      dir = dir.counterClockwise.counterClockwise
    }
  }
  tickC += 1
  move(dir)
  if (tickC%(pixelsPerGridSquare/speed) == 0) {
    gridLocation = gridLocation.neighbor(dir)
    tickC = 0
  }
}
}