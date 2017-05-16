package com.itm.parseJson1

import java.util._

/**
 * Created by gaochuang on 2017/2/28.
 */
object subList {
  def main(args: Array[String]) {
    val ss: List[Int] = new ArrayList[Int]()
    ss.add(1)
    ss.add(2)
    ss.add(3)
    ss.add(4)
    ss.add(5)
    ss.add(21)
    println(ss)
   // val sd= ss.subList(0,5)
    val sd= ss.subList(0,ss.size())
    println(sd)

  }

}
