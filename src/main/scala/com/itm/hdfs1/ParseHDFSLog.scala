package com.itm.hdfs1

import java.util.Properties

import com.ibm.tss.bigdata.rule.base.TssRuleHelper
import com.ibm.tss.bigdata.rule.base.TssRuleHelper.Builder
import org.apache.hadoop.conf._
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.immutable.Map
import scala.util.parsing.json.JSON

//import com.ibm.tss.bigdata.rule.serverImpl.RuleService
/**
 * Created by gaochuang on 2017/2/22.
 */
object ParseHDFSLog {


  def main(args: Array[String]) {

    val properties = new Properties()
    loadProperties(properties)
    val FILENAME:String = properties.getProperty("filename")
    val KAFKA_METADATA_BROKER_LIST: String = properties.getProperty("metadata.broker.list")
    val WRITE_TOPIC : String = properties.getProperty("writeTopic")
    val DIRNAME : String = properties.getProperty("parseLogPath")
    val sparkConf = new SparkConf()
                        .setAppName("readHDFSLogAndParse")
                        .setMaster("local[*]")

    val helper : TssRuleHelper = new Builder().build()


    val sc = new SparkContext(sparkConf)
    val brokerListBroadcast: Broadcast[String] = sc.broadcast(KAFKA_METADATA_BROKER_LIST)
    val topicBroadcast: Broadcast[String] = sc.broadcast(WRITE_TOPIC)



    val fs : FileSystem  = FileSystem.get(new Configuration())
    traversalDirName(DIRNAME)
    //遍历顶级目录，得到所有服务器名字文件夹路径
    def traversalDirName(dirName:String) {
      val hostNamrFilePaths= fs.listStatus(new Path(dirName))
      hostNamrFilePaths.foreach(
        dataFileDir =>{
          val fileHostNamePath = dataFileDir.getPath.toString
          traversalHostnameDir(fileHostNamePath)
        })
    }

   //遍历服务器名字下的文件夹 得到相应服务器下所有日期的文件夹
    def traversalHostnameDir(hostNameFile:String){
      val filePaths= fs.listStatus(new Path(hostNameFile))
      filePaths.foreach(
      dataFileDir =>{
        val fileDataPath = dataFileDir.getPath.toString
        parseDataFile(fileDataPath)
      })
    }

    //遍历日期文件夹下所有的文件
 //   val filePaths= fs.listStatus(new Path("/tmp/hadoop7.zj.tss.ibm.test/2017-02-23/"))
  //  val filePaths= fs.listStatus(new Path("/gaochuang/hadoop7.zj.tss.ibm.test/"))
    def parseDataFile(fileDataPath:String)  {
      val filePaths= fs.listStatus(new Path(fileDataPath))
      filePaths.foreach({
        file =>
          val path = file.getPath.toString
          println(path)
          parseEachFile(path, sc,helper,topicBroadcast,brokerListBroadcast)
      })

    }


    // SparkContext Stop
    sc.stop()


  }

  def parseEachFile(FILENAME: String, sc: SparkContext,helper:TssRuleHelper,topicBroadcast:Broadcast[String],brokerListBroadcast:Broadcast[String]): Unit = {
    // 读取hdfs数据
    val textFileRdd = sc.textFile(FILENAME)

    //val textFileRdd = sc.textFile("/tmp/hadoop7.zj.tss.ibm.test/2017-02-23/FlumeData.1487837355126")

    val lindsRdd: RDD[Array[String]] = textFileRdd.map(rdd => {

      val splitArray = rdd.split("\\:\\[")
      splitArray
    })

    lindsRdd.foreachPartition(
      iter => {
        iter.foreach(
          record => {
            //     String aString=tssRuleApi.parse("\"Nov 12 08:41:53 10.183.13.27 Nov 12 2016 08:41:53: %ASA-6-302015: Built inbound UDP connection 367839396 for outside:119.84.104.218/5053 (119.84.104.218/5053) to inside:192.168.36.14/53 (210.13.92.205/53)\"", "asa1.csv");
            //     val parseString : String = tssRuleApi.parse(record,"asa1.csv")
            record.foreach(println)

            /* val api :TssRuleApiEchoImpl = new TssRuleApiEchoImpl()
            val resultString = api.parse(record(2),FILENAME, properties)*/
            var resultString: String =null

            //alt + shift +z
            try{
              resultString = helper.createFilenameEvent(record(1), FILENAME)
            }catch {
              case  ex: Exception =>  ex.printStackTrace()

            }
            println(resultString)
            var ss:String =null
            var ssArray:Array[String] =null
            val b = JSON.parseFull(resultString)
            b match {
              // Matches if jsonStr is valid JSON and represents a Map of Strings to Any
              case Some(map: Map[String, Any]) => {
                println(map)
                ss = map.get("message").get.toString
                val ssA:Array[String] =ss.split("\\]\\[")
                var ress:String =null
                  try{
                    ress=ssA(0)+","+ssA(2)+","+ssA(3)+","+ssA(6)
                    println(ress+"-------------------------------------------------")
                    ress=ssA(0)+","+ssA(2)+","+ssA(3)+","+ssA(6)+","+ssA(9)+","+ssA(10).substring(0,ssA.length-1)
                  }catch {
                    case ex:Exception => ex.printStackTrace()
                  }
                KafkaProducer.sendMessages(topicBroadcast.value, ress, brokerListBroadcast.value)


               /* println(map.get("et").get)
                println(map.get("vtm").get)
                val time:String = map.get("time").get.toString
                println(time)
                println(map.filterKeys(rs=>rs.contains("et")))
                val paw= map.filterKeys(_.contains("et"))
                val et : String= paw.values.mkString
                println(et)*/
              }
              case None => println("Parsing failed")
              case other => println("Unknown data structure: " + other)
            }

           // KafkaProducer.sendMessages(topicBroadcast.value, resultString, brokerListBroadcast.value)
           // KafkaProducer.sendMessages(topicBroadcast.value, ss, brokerListBroadcast.value)
          })
      })
  }

  //加载配置文件
  def loadProperties(properties: Properties): Unit = {
   /* val filePath = "pro.properties"
    properties.load((new FileInputStream(filePath)))*/

    val path = Thread.currentThread().getContextClassLoader.getResourceAsStream("pro.properties") //文件要放到resource文件夹下
    //System.out.print(path + "-----------------PPPPPPPPPPPPPPPP_________________________________________")
    properties.load(path)
  }

}
