package com.itm.utils

import java.io.File

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
 * Created by gaochuang on 2017/3/28.
 */
object fileUtils {
  var maps = Map.empty[String,Int]
  def main(args: Array[String]) {



    scanDir(new File("D://aa"))
    maps.foreach(f =>
    println(f+"ssssssssssss")
    )
  }

  def scanDir(dir : File):Unit ={
    dir.listFiles.foreach{
      file =>
        if(file.isFile){
          readFile(file)
          println(file)
        }
    }
  }

  def readFile(file : File): Unit ={
    val f = Source.fromFile(file)
    var sd = ""
    val list  = new ArrayBuffer[Array[Double]]
   // val array = new Array[]
    //list.append(array)
    for(line <-f.getLines()){
    //  count(line)
    //  val dd = line.split(" ").map(f => (f,1))
      sd+=line
    }
    val countMap = sd.split(" ").map(f => (f,1))
   // val count=countMap.reduce(_._2 + _._2)
  //  println(count)

  }
}
