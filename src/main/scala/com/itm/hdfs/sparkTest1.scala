package com.itm.hdfs

import java.io.FileInputStream
import java.sql.{Connection, DriverManager}
import java.util.Properties

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by gaochuang on 2017/2/26.
  */
object sparkTest1 {


   def main(args: Array[String]) {
     val sc = initSparkConText()

     val properties = new Properties()
     var path:String = null
     if(args.length == 1)
       path = args(0)
     else path = "pro.properties"
    // loadProperties(properties,path)

     val FILE_PATH:String=  properties.getProperty("filePath")
     val KPI_NAME:String = properties.getProperty("kpiName")
     val START_TIME :String =properties.getProperty("startTime")
     val textFileRdd = sc.textFile(FILE_PATH)
     val timeLength = textFileRdd.collect().length
     val text = textFileRdd.collect()

     for( a <- 0 until timeLength){
       val timeStemp = START_TIME+0*a
       val kpiName = KPI_NAME
       val predictData = text(a)

       //入库
       val sql = "replace  into model_predata_tbl values(?,?,?)"
       updateToSql(sql,path,timeStemp,kpiName,predictData)

     }

     sc.stop()
   }




   def initSparkConText():SparkContext= {
     val sparkConf = new SparkConf()
     .setAppName("readHDFSLogAndParse")
     .setMaster("local[*]")
     val sc = new SparkContext(sparkConf)
           sc
   }

   //加载配置文件
   def loadProperties(properties:Properties,filePath:String): Unit = {
     properties.load((new FileInputStream(filePath)))
   }

  def updateToSql(sql:String,path:String,timeStemp:String,kpiName:String,predictData:String): Unit = {
  // 获取数据库配置
  val properties = new Properties()
  loadProperties(properties,path)
  val (driver, url, username, password) = (properties.getProperty("jdbc.driver"), properties.getProperty("jdbc.url"), properties.getProperty("jdbc.user"), properties.getProperty("jdbc.password"))
  // 获取数据库连接
  Class.forName(driver)
  val connection: Connection = DriverManager.getConnection(url, username, password)
  connection.setAutoCommit(false)
  // 创建数据输出对象
  val pstmt = connection.prepareStatement(sql)
  // 设置数据
  pstmt.setString(1, timeStemp)
  pstmt.setString(2, kpiName)
  pstmt.setString(3, predictData)
  // 添加批量执行
  pstmt.addBatch()
  // 提交
  pstmt.executeBatch()
  connection.commit()
  // 关闭连接
  connection.close()
  }






 }
