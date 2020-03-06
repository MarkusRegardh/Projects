package Game
import java.io.Reader
import java.io.IOException
import java.io.BufferedReader._
import o1._
import java.io.BufferedReader



object FileReader {
  
  //lukee kartan jonka pelaaja voi laatia
  
   def readMap(input: Reader): Map = {
     
     var lineReader = new BufferedReader(input)
    var line = lineReader.readLine().trim()
     
    
     
     
     var map = Buffer[(Int,Int)]()
     var start = (0,0)
     var goal = (0,0)
       
     


           
            if (line.startsWith("MAP")) {
                throw new MapError("Unknown file type");
            }
     
            var x = 0
            for (y <- 0 to 11) {
              line = lineReader.readLine().trim
              x=0
              for (a <- line) {
                a match {
                  case 'S' => start = (x,y)
                  case 'G' => goal = (x,y)
                  case '>' => map += ((x,y))
                  case '+' =>
                  case '-' =>
                  case _ =>
                }
                x += 1
              }
            }
            
   def toPos(a:(Int,Int)) = {
     new GridPos(a._1  ,a._2 )
   }
            
   map = Buffer(start) ++ map ++ Buffer(goal)
   
   new Map(toPos(start) ,toPos(goal),map.map(n => toPos(n)))
  
   }










}