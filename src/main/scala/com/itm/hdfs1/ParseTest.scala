package com.itm.hdfs1

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by gaochuang on 2017/2/22.
  */
object ParseTest {


   def main(args: Array[String]) {

     val sparkConf = new SparkConf()
       .setAppName("readHDFSLogAndParse")
       .setMaster("local[*]")

     val sc = new SparkContext(sparkConf)
     // 读取hdfs数据
  //   val textFileRdd = sc.textFile("/tmp/fund_20160517_all.csv")
     val textFileRdd = sc.textFile("/user/spark/tssg.log")
     val fRdd = textFileRdd.flatMap { _.split(",") }
     val mrdd = fRdd.map { (_, 1) }
     val rbkrdd = mrdd.reduceByKey(_+_)
     // 写入数据到hdfs系统
     rbkrdd.saveAsTextFile("/user/spark/tssc1.txt")
     rbkrdd.foreach(print)
     // SparkContext Stop
     sc.stop()


   }



 }
