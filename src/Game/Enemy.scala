package Game
import o1._
import Constants._
import java.awt.{ Graphics2D, Color, BasicStroke }

abstract class Enemy(initialHP: Int, start: GridPos) {
  
var v: Int                              //monta pixeliä/tick
  
var color: Color

var dmgColor: Color

var size: Int

var hpMult: Int

var hp = initialHP*hpMult        

var tickC = 0                            //tick systeemi

var gridLocation = start

def toPixelPos(gridPos: GridPos) = new Pos(gridPos.x * Constants.pixelsPerGridSquare, gridPos.y * Constants.pixelsPerGridSquare)

var x = toPixelPos(start).x

var y = toPixelPos(start).y

var pLocation = toPixelPos(start)

def setHP = {
  hp = initialHP*hpMult
  this
}
 
def isdmged = {
 this.hp != initialHP*hpMult   
}

var dir = South
  
def dead = (hp<=0) 

def move(d: CompassDir) = {               //Mihin suuntaan liikutaan
  if (d == South) {
    y += v
  } else if (d == North) {
    y -= v
  } else if (d == East) {
    x += v
  } else if (d == West) {
    x -= v
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

def moveForward(map: Map) = {                                //liikkuu gridissä sekä pixeleissä (että liikkuvuus näyttäisi sujuvalta), katsoo myös mihin suntaan kartan "path" liikkuu 
  while (!map.path.contains(gridLocation.neighbor(dir))){
    dir = dir.clockwise
    if (!map.path.contains(gridLocation.neighbor(dir))){
      dir = dir.counterClockwise.counterClockwise
    }
  }
  tickC += 1
  move(dir)
  if (tickC%(pixelsPerGridSquare/v) == 0) {
    gridLocation = gridLocation.neighbor(dir)
    tickC = 0
  }
}
}

class normal(intialHP: Int, start: GridPos) extends Enemy(intialHP,start) {
  var v = 2
  var color = Color.PINK
  var dmgColor = Color.RED
  var size = 10
  var hpMult = 2
}

class speedyBoi(intialHP: Int, start: GridPos) extends Enemy(intialHP,start) {

var v = 4
var color = Color.YELLOW
var dmgColor = Color.ORANGE
var size = 20
var hpMult = 1
}

class thickBoi(intialHP: Int, start: GridPos) extends Enemy(intialHP,start) {
  var v = 1
  var color = Color.BLACK
  var dmgColor = Color.WHITE
  var size = 0
  var hpMult = 20
}


