package com.itm.streaming.realTimeCount

import java.io.{FileInputStream, BufferedInputStream, InputStream}
import java.sql.{Connection, DriverManager}
import java.text.SimpleDateFormat
import java.util.{Date, Properties}

import kafka.serializer.StringDecoder
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
/**
  * 从Kafka中获取数据，并进行实时的处理
  */

object UpdateKafkaTransTotalCountStreaming {

  def main(args: Array[String]) {
     // Create SparkConf Instance
     val sparkConf = new SparkConf()
       // 设置应用的名称, 4040监控页面显示
       .setAppName("TransCountStreamingApplication")

       // 设置程序运行环境, local
     // .setMaster("local[*]")
      // .setMaster("spark://hadoop3:7070")

     // Create SparkContext
     val sc = new SparkContext(sparkConf)

     // Create StreamingContext
     val ssc = new StreamingContext(sc, Seconds(1)) // 设置间隔为1s

     // 设置检查点目录，通常设置在HDFS上的某个目录
     ssc.checkpoint("./checkpoint/w")

     /**  ======================================================================= */
     // Kafka Cluster Params

     val properties = new Properties()
       loadProperties(properties)
     val kafkaParams: Map[String, String] = Map("metadata.broker.list"
            -> properties.getProperty("metadata.broker.list"))

     // Kafka Topics

     val topics: Set[String] = Set(properties.getProperty("kafka.topics"))


     val lineDStream: DStream[String] = KafkaUtils.createDirectStream[
       String, String, StringDecoder, StringDecoder](
       ssc, // StreamingContext
       kafkaParams, // Map[String, String]
       topics// Set[String]
     ).map(tuple => tuple._2)

     /**
      * DStream Transformation
      */

     //数据的初步过滤，去除不满足格式条件的数据
     //样例数据
     //      2017-2-7 10:8:25, ECUPQ123,  0,  60

     val filtedLineDstream:DStream[String]= lineDStream.filter(_.split(",").length==4)

        //每个交易码下的交易量的实时统计

         val transDStream: DStream[(String, (Int,Int,Int))] = filtedLineDstream.map(line => {
           val tuple = line.split(",")
           var minusTime = 0
           val minusTime1= tuple(3).trim
           if(minusTime1.equals("null"))
              minusTime = 0
           else
             minusTime = minusTime1.toInt
           val severName = tuple(1) //交易码
           val sOrF = tuple(2).toInt  //成功失败标志，0为成功，1为失败
           (severName, (minusTime,1,sOrF))
         })


       //实时更新交易数据

             val totalTransDstream: DStream[(String, (Int,Int,Int))] = transDStream.updateStateByKey(
            (values: Seq[(Int,Int,Int)], state: Option[(Int,Int,Int)]) => {
              // 获取当前Key传递进来的值
              val currentCount:(Int,Int,Int)= {
                val totalTime =  values.map(_._1).sum
                val totalCount =  values.map(_._2).sum
                val totalSOrF = values.map(_._3).sum
                (totalTime,totalCount,totalSOrF)
              }
              // 获取Key以前状态中的值
              val previousCount = state.getOrElse((0,0,0))
              val total:(Int,Int,Int) = {
                val count1 = currentCount._1 + previousCount._1
                val count2 =currentCount._2 + previousCount._2
                val count3 = currentCount._3 + previousCount._3
                (count1,count2,count3)
              }
              // update state and return
              Some(total)
            }
          )

          totalTransDstream.print()


       //设置时间窗口，1/待统计的流数据  2/ 计算周期 秒s 3/窗口大小即计算范围 s 秒

      //每1分钟统计一次,每次统计最近1分钟
     // calculateTransByWindow(filtedLineDstream,60,60)

      //每5分钟统计一次,每次统计最近5分钟
      calculateTransByWindow(filtedLineDstream,300,300)

      //每10分钟统计一次,每次统计最近10分钟
     // calculateTransByWindow(filtedLineDstream,600,600)

         //将按交易码数据实时统计的交易量和交易耗时更新到Mysql
          val sql = "replace  into poc_deal_count_by_servicename values(?,?,?,?)"
          totalTransResuleToSql(totalTransDstream,sql)


         /** =======================================================================*/
         // Start SparkStreaming Application
         ssc.start()

         ssc.awaitTermination()
         // Stop
         ssc.stop()
       }



  //总交易量结果实时持久化同步到Mysql
  def totalTransResuleToSql(totalTransDstream: DStream[(String, (Int, Int, Int))],sql:String): Unit = {
    totalTransDstream.foreachRDD(rdd => {
      // 按照每个分区进行数据输出
      rdd.foreachPartition(iter => {
        var count = 0
        //获取数据库配置信息
        val properties = new Properties()
        loadProperties(properties)
        val (driver, url, username, password) = (properties.getProperty("jdbc.driver"), properties.getProperty("jdbc.url"), properties.getProperty("jdbc.user"), properties.getProperty("jdbc.password"))

        // 获取数据库连接
        Class.forName(driver)
        val connection: Connection = DriverManager.getConnection(url, username, password)
        connection.setAutoCommit(false)
        // 创建数据输出对象
        val pstmt = connection.prepareStatement(sql)
        // 数据输出
         iter.foreach(record => {
          // 从record中获取数据

          val first = record._1.toString
          val second = record._2._1.toDouble
          val third = record._2._2.toInt
          val fourth = record._2._3.toInt

          // 设置数据
          pstmt.setString(1, first)
          pstmt.setDouble(2, second)
          pstmt.setInt(3, third)
          pstmt.setInt(4, fourth)

          // 添加批量执行
          pstmt.addBatch()
          count += 1
          // 如果数量超过5个，进行提交
            pstmt.executeBatch()
            connection.commit()
        })
        // 关闭连接
        connection.close()
      })
    })
  }

  //加载配置文件
  def loadProperties(properties: Properties): Unit = {
    val filePath = "pro.properties"
    properties.load(new FileInputStream(filePath))

    /*val path = Thread.currentThread().getContextClassLoader.getResourceAsStream("pro.properties") //文件要放到resource文件夹下
    //System.out.print(path + "-----------------PPPPPPPPPPPPPPPP_________________________________________")
    properties.load(path)*/
  }



  def realTimeUpdate(TransDStream: DStream[(String, Int)]): DStream[(String, Int)] = {
    val eachTransDStream = TransDStream.updateStateByKey(
      (values: Seq[Int], state: Option[Int]) => {
        // 获取当前Key传递进来的值
        val currentCount = values.sum

        // 获取Key以前状态中的值
        val previousCount = state.getOrElse(0)

        // update state and return
        Some(currentCount + previousCount)
      }
    )
    eachTransDStream
  }



   def calculateTransByWindow(filtedLineDstream:DStream[String],calPeriod:Int,windowSizee: Int) : Unit= {
    // 转换数据
    val pairDStream = filtedLineDstream.map(
      record => {
        val tuple = record.split(",")
        //交易耗时
        var transTime = 0.0
        val transTime1= tuple(3).trim
        if(transTime1.equals("null"))
          transTime = 0
        else
          transTime = transTime1.toDouble
        ("totalTimeAndCount",(transTime,1))

      })

    //  使用窗口分析函数进行分析
    val aggrDStream = pairDStream.reduceByKeyAndWindow(
      (a:(Double,Int), b:(Double,Int)) =>
        (a._1 + b._1 , a._2 + b._2),
      Seconds(windowSizee), // window interval
      Seconds(calPeriod) // slider interval
    )

    //检测数据集中是否为空，为空则插入0
    /* aggrDStream.foreachRDD(
      arr => if(arr.count() == 0) {
          val sql = "replace  into poc_time_interval_count values(?,?,?,?)"
          inserToSqlBy0(sql,calPeriod)
      }
      )*/
    // 将数据缓存到RDBMs中，由于这个数据是60s更新一次，建议页面每60s从数据库获取一次(highstock可以做实时数据展示)

    val sql = "replace  into poc_time_interval_count values(?,?,?,?)"
      updateToSql(aggrDStream,sql,calPeriod)

    }



  def updateToSql(aggrDStream: DStream[(String, (Double, Int))],sql:String,calPeriod:Int): Unit = {
    aggrDStream.foreachRDD(rdd => {
      // rdd数据持久化到关系型数据库
      // 按照每个分区进行数据输出

      //检测数据集中是否为空，为空则插入0
      if (rdd.count() == 0)
        inserToSqlBy0(sql, calPeriod)

      rdd.foreachPartition(iter => {
        var count = 0
        // 获取数据库配置
        val properties = new Properties()
        loadProperties(properties)
        val (driver, url, username, password) = (properties.getProperty("jdbc.driver"), properties.getProperty("jdbc.url"), properties.getProperty("jdbc.user"), properties.getProperty("jdbc.password"))

        // 获取数据库连接
        Class.forName(driver)
        val connection: Connection = DriverManager.getConnection(url, username, password)
        connection.setAutoCommit(false)
        // 创建数据输出对象

        val pstmt = connection.prepareStatement(sql)
        // 数据输出
        iter.foreach(record => {
          // 从record中获取数据

          val now = new Date().getTime
          val first = now
          val second = record._2._2.toInt
          val third = record._2._1.toDouble
          val window = calPeriod
          // 设置数据
          pstmt.setLong(1, first)
          pstmt.setInt(2, second)
          pstmt.setDouble(3, third)
          pstmt.setInt(4, window)

          // 添加批量执行
          pstmt.addBatch()
          count += 1
          // 提交
          pstmt.executeBatch()
          connection.commit()
          // 关闭连接
          connection.close()
        })
      })
    })
  }

  def getNowDate():String={
      val now:Date = new Date()
      val  dateFormat:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      val nowTime = dateFormat.format(now)
      nowTime
    }

  def inserToSqlBy0(sql:String,calPeriod:Int): Unit = {

    var count = 0
    // 获取数据库配置
    val properties = new Properties()
    loadProperties(properties)
    val (driver, url, username, password) = (properties.getProperty("jdbc.driver"), properties.getProperty("jdbc.url"), properties.getProperty("jdbc.user"), properties.getProperty("jdbc.password"))

    // 获取数据库连接
    Class.forName(driver)
    val connection: Connection = DriverManager.getConnection(url, username, password)
    connection.setAutoCommit(false)
    // 创建数据输出对象

    val pstmt = connection.prepareStatement(sql)
    // 数据输出
      val now = new Date().getTime
      val first = now
      val second = 0
      val third = 0
      val window = calPeriod
        pstmt.setLong(1, first)
        pstmt.setInt(2, second)
        pstmt.setDouble(3, third)
        pstmt.setInt(4, window)
      // 添加批量执行
        pstmt.addBatch()
    // 提交
      pstmt.executeBatch()
      connection.commit()
    // 关闭连接
    connection.close()
      }



}


