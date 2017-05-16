package com.itm.utils

import java.io.{FileInputStream, File}
import java.util.Properties

import scala.io.Source

/**
 * Created by gaochuang on 2017/3/28.
 */
object ReadFile {
  def main(args: Array[String]) {

    val path = "D:/test.txt"
    val path2 = "D:/pro.properties"
    readFile(path)
    val pro = new Properties()
    loadProperties(pro,path2)
   val ss = pro.getProperty("originTopic")
    println(ss)
  }
  def readFile(path : String): Unit ={

    val file = new File(path)
    val f = Source.fromFile(file)
  }
  //加载配置文件
  def loadProperties(properties:Properties,filePath:String): Unit = {
    properties.load((new FileInputStream(filePath)))
  }
}
