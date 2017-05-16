package com.itm.utils

import java.util.Properties

import kafka.producer.async.EventHandler
import kafka.producer.{ProducerConfig, OldProducer, KeyedMessage}
import kafka.serializer.StringEncoder
import kafka.javaapi.producer.Producer
import java.util.List
import java.util.ArrayList

import scala.util.Random

/**
  * Created by gaochuang on 2017/2/17.
  */
object StringUtil {

  def getgetRandomString(length:Int): String ={
    val str ="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    val random = new Random()
    val sb=new StringBuffer()
    for(i <- 0 until length){
      val number = random.nextInt(62)
      sb.append(str.charAt(number))
    }
    sb.toString
  }


}
