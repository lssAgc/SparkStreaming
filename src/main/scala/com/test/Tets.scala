package com.test

import com.itm.utils.StringUtil

/**
 * Created by gaochuang on 2017/5/15.
 */
object Tets {

  def main(args: Array[String]) {
    for(i <- 0 to 10){
     val ss = StringUtil.getgetRandomString(10)
      val ha = Math.abs(ss.hashCode)
      val num = ha%16
      println(s"$ss hashCode: $ha partion$i: $num")
    }

    val ha = Math.abs("ss".hashCode)
    val num = ha%16
    println(s"ss  hashCode: $ha partion: $num")
  }
}
