package com.itm.hdfs1

import java.io.FileInputStream
import java.util.Properties

import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by gaochuang on 2017/2/26.
 */
object sparkTestWithTwoSC {


  def main(args: Array[String]) {
    val sc = initSparkConText()

    val properties = new Properties()
    var path:String = null
    if(args.length == 1)
      path = args(0)
    else path = "pro.properties"
   // loadProperties(properties,path)

    val FILE_PATH:String=  properties.getProperty("filePath")
    val KPI_NAME:String = properties.getProperty("kpiName")
    val START_TIME :String =properties.getProperty("startTime")

    // val fRdd = textFileRdd.flatMap { _.split(",") }
    // val mrdd = fRdd.map { (_, 1) }
    //val rbkrdd = mrdd.reduceByKey(_+_)
   // val textFileRdd = sc.textFile(FILE_PATH)
    val textFileRdd = sc.textFile("d:\\test.txt")
    var startTime = START_TIME
    val timeLength = textFileRdd.collect().length
    val text = textFileRdd.collect()

    for( a <- 0 until timeLength){
      println(text(a))

    }
    textFileRdd.collect().foreach(record =>{
       startTime = START_TIME
      val kpiName = KPI_NAME
      val predictData = record

    })


    sc.stop()

    val df = 0 until 10
    df.foreach(println)
    println(text(0)+"llllllllllllllllllllllllllllllllllllll")



    val sc2 = initSparkConText()
    sc2.textFile("d:\\test.txt").collect().foreach(println)

  }

  def initSparkConText():SparkContext= {
    val sparkConf = new SparkConf()
    .setAppName("readHDFSLogAndParse")
    .setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
          sc
  }

  //加载配置文件
  def loadProperties(properties:Properties,filePath:String): Unit = {
    properties.load((new FileInputStream(filePath)))
  }




}
