package com.itm.utils

import org.apache.spark.SparkConf

/**
 * Created by gaochuang on 2017/3/28.
 */
object Test {
  def main(args: Array[String]) {
    val ss = 1 to 10
    val conf = new SparkConf()

    val sd = ss.reduce(_+_)

    val df = ss.map(s=> (s,2*s))
   // val ssd = df.reduce(_+_)((x,y:Int) =>y)

  //  print(ssd)
  }

}
