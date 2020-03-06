package TDGUI

import Game.FileReader._
import java.io._

object world {
  def loader(input: File) = {
    readMap(new FileReader(input))
  }
}