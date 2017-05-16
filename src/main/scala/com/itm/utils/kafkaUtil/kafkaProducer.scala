package com.itm.utils.kafkaUtil

/**
 * Created by gaochuang on 2017/3/29.
 */

import java.util.{Date, Properties}

import kafka.producer.{KeyedMessage, ProducerConfig, Producer}
import kafka.serializer.StringEncoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Milliseconds, StreamingContext}

case class Person(var name: String, var age: Int)
object kafkaProducer {

  def getProducerConfig(brokerAddr: String): Properties = {
    val props = new Properties()
    props.put("metadata.broker.list", brokerAddr)
    props.put("serializer.class", classOf[IteblogEncoder[Person]].getName)
    props.put("key.serializer.class", classOf[StringEncoder].getName)
    props
  }

  def sendMessages(topic: String, messages: List[Person], brokerAddr: String) {
    val producer = new Producer[String, Person](new ProducerConfig(getProducerConfig(brokerAddr)))
   /* producer.send(messages.map {
      new KeyedMessage[String, Person](topic,"Iteg",_)}: _*)*/
    val ss = messages(0)
    producer.send(new KeyedMessage[String, Person](topic,"Tieg",ss))
    producer.send(new KeyedMessage[String, Person](topic,"Tieg",messages(1)))
    producer.close()
  }

  def main(args: Array[String]) {
    //val sparkConf = new SparkConf().setAppName(this.getClass.getSimpleName)
  //  val ssc = new StreamingContext(sparkConf, Milliseconds(500))
    //val topic = args(0)
    val topic ="sparkTopic"
    val brokerAddr ="10.28.0.83:9092"

    val data = List(Person("wyp", 23), Person("spark", 34), Person("kafka", 23),
      Person("iteblog", 23))
    sendMessages(topic, data, brokerAddr)
    println(data)
  }

}
