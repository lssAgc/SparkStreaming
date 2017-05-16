package com.itm.parseJson1

import java.io.FileInputStream
import java.util.Properties

// import com.ibm.tss.bigdata.rule.base.TssRule
import com.ibm.tss.bigdata.rule.ancient.TssRuleParserImpl
// import com.ibm.tss.bigdata.rule.api.TssRuleApiEchoImpl
import kafka.serializer.StringDecoder
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Milliseconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}


/**
 * Created by gaochuang on 2017/2/17.
 */
object KafkaOutput {
  def main(args: Array[String]) {
     println("------------- begin ----------------")
    val properties = new Properties()
    loadProperties(properties)
    val KAFKA_CONSUMER_GROUP_ID: String = properties.getProperty("kafka.consumer.goupid")
    val KAFKA_METADATA_BROKER_LIST: String = properties.getProperty("metadata.broker.list")
    val ORIGIN_TOPIC : String =properties.getProperty("originTopic")
    val WRITE_TOPIC : String = properties.getProperty("writeTopic")
    var RULE_ALIAS : String = properties.getProperty("ruleAlias")
    var CHECKPOINT_PATH : String = properties.getProperty("checkpointPath")
    val SETSECONDS : String = properties.getProperty("setSparkSeconds")
    val APPNAME : String = properties.getProperty("setSparkAppName")


    val topics: Set[String] = Set(ORIGIN_TOPIC)

    val kafkaParams: Map[String, String] = Map(
      "metadata.broker.list" -> KAFKA_METADATA_BROKER_LIST,
      "group.id" -> KAFKA_CONSUMER_GROUP_ID
    )

    // Create SparkConf Instance
    val sparkConf = new SparkConf()
      // 设置应用的名称, 4040监控页面显示
      .setAppName(APPNAME)
    // 设置程序运行环境, local
     // .setMaster("local[*]")
    // Create SparkContext
    val sc = new SparkContext(sparkConf)
    // Create StreamingContext
    val ssc = new StreamingContext(sc, Milliseconds(SETSECONDS.toInt)) // 设置间隔为毫秒
    ssc.checkpoint(CHECKPOINT_PATH)
    val brokerListBroadcast: Broadcast[String] = ssc.sparkContext.broadcast(KAFKA_METADATA_BROKER_LIST)
    val topicBroadcast: Broadcast[String] = ssc.sparkContext.broadcast(WRITE_TOPIC)

    val lineDStreamMessage: DStream[String] = KafkaUtils.createDirectStream[
      String, String, StringDecoder, StringDecoder](
        ssc, // StreamingContext
        kafkaParams, // Map[String, String]
        topics // Set[String]
      ).map(tuple => tuple._2)


    /*
        ..............
        其他业务逻辑
        .............

       */

    lineDStreamMessage.foreachRDD(rdd => {
      rdd.foreachPartition(iter => {
        // val kafkaProducer: KafkaProducer = KafkaProducer2.getInstance(brokerListBroadcast.getValue)
        // val kafkaProducer: KafkaProducer = KafkaProducer.getProducerConfig(brokerListBroadcast.getValue)
        iter.foreach (
            record =>{
              //k topic  v  message
             /* TssRuleApi api = new com.ibm.tss.bigdata.rule.api.TssRuleApiEchoImpl();
              api.parse( eventText, "N/A" , props );*/
              println("------------- start parse --------------")
              val api :TssRuleParserImpl = new TssRuleParserImpl()
              val resultString = null
                try{
                  api.parse(record, RULE_ALIAS, properties)
                }
              catch {
                case ex:Exception =>{
                  ex.printStackTrace()
                  println(record)
                  println(RULE_ALIAS)

                }
              }
              println(record)
              println("------------- end parse ----------------")
              KafkaProducer.sendMessages(topicBroadcast.value,resultString,brokerListBroadcast.value)
            }
          )
      })
    })


    /** ======================================================================= */
    // Start SparkStreaming Application
    ssc.start()

    ssc.awaitTermination()
    // Stop
    ssc.stop()
  }




    //加载配置文件
    def loadProperties(properties: Properties): Unit = {
        val filePath = "pro.properties"
        properties.load((new FileInputStream(filePath)))
    }
}

