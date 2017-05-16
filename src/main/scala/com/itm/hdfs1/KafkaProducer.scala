package com.itm.hdfs1

import java.util.Properties

import kafka.javaapi.producer.Producer
import kafka.producer.{KeyedMessage, ProducerConfig}
import kafka.serializer.StringEncoder

/**
  * Created by gaochuang on 2017/2/17.
  */
object KafkaProducer {
  val METADATA_BROKER_LIST_KEY: String = "metadata.broker.list"
  val SERIALIZER_CLASS_KEY: String = "serializer.class"
  val SERIALIZER_CLASS_VALUE: String = "kafka.serializer.StringEncoder"
  //val producer: Product[String,String] = null



  def getProducerConfig(brokerAddr: String): ProducerConfig = {
    val props: Properties = new Properties()
    props.put("metadata.broker.list", brokerAddr)
    props.put("serializer.class", classOf[StringEncoder].getName)
    props.put("key.serializer.class", classOf[StringEncoder].getName)
    val config : ProducerConfig =new ProducerConfig(props)
    config
  }

  def getProducer(config: ProducerConfig):Producer[String,String]={


    val producer : Producer[String,String] = new  Producer(config)
    producer
  }


  def getProducerBybrokerAddr(brokerAddr: String) :Producer[String,String]= {
    val config = getProducerConfig(brokerAddr)
    getProducer(config)
  }

  def sendMessages(topic: String, messages: String, brokerAddr: String) {
    val producer : Producer[String,String] =getProducerBybrokerAddr(brokerAddr)

    //关于KeyedMessage(topic,"key",messages) 指定key的话只会被一个consumer消费，不指定则以轮询的方式
    //指定了key值的话，partition的值实际上是由Utils.murmur2(keyBytes)哈希计算出来，这样自然每次都是被同一个消费者接收到。
    // 如果没有指定的话，就会通过轮询的方式逐个发送。
    val message : KeyedMessage[String,String] = new KeyedMessage(topic,"ss",messages)
    producer.send(message)
    producer.close
  }


  def main(args: Array[String]) {
    sendMessages("sparkTopic","dsssssaqqqqqqqqqqqqqqqqqqqqq","10.28.0.83:9092")
  }

}
