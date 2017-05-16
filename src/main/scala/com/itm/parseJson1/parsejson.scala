package com.itm.parseJson1

import scala.util.parsing.json.JSON
import scala.collection.immutable.Map
import scala.util.parsing.json.JSONObject
/**
 * Created by gaochuang on 2017/2/26.
 */
object parsejson {
  def main(args: Array[String]) {
    val str2 = "{\"et\":\"kanqiu_client_join\",\"vtm\":1435898329434,\"body\":{\"client\":\"866963024862254\",\"client_type\":\"android\",\"room\":\"NBA_HOME\",\"gid\":\"\",\"type\":\"\",\"roomid\":\"\"},\"time\":1435898329}"
    val strTest =
      """{"@hostname":"HD-NODE04 0",
        |"@linenum":"530362   1",
        |"@filepath":"/opt/asafirewall-CSV/ 2",
        |"@message":"\"    3",
        |"filename":"asa_02_20170116-1.csv  4",
        |"starttime":"Jan 16 13:00:57   5",
        |"firewallip":"10.183.13.29   6",
        |"endtime":"Jan 16 2017 13:00:57    7",
        |"messageno":"ASA-6-302013   8",
        |"action":"Built inbound   9",
        |"protocol":"TCP   //10",
        |"connectionid":"3315416639   11",
        |"interface1":"outside  12",
        |"realaddr1":"183.39.46.41   13",
        |"realport1":"21433   14",
        |"mappedaddr1":"183.39.46.41  15",
        |"mappedport1":"21433   16",
        |"interface2":"inside    17",
        |"realaddr2":"192.168.36.117   18",
        |"realport2":"8088   19",
        |"mappedaddr2":"124.74.244.19   20",
        |"mappedport2":"8088    21",
        |"option":"    22",
        |"@rule_alias":"ASASYSLOG   23",
        |"@store_name":"ASA302013   24",
        |"@rule_name":"ASASYSLOG   25",
        |"@timestamp":"2017-03-16 16:58:51.263    26",
        |"@date":"2017-03-16   27"}""".stripMargin

    println(strTest.contains("host")+"ll")
    val b = JSON.parseFull(str2)
    println(str2)
    println(strTest)
    val c = JSON.parseFull(strTest)


    val dss = (0 to 100) map{

      i=>{
          println(i+"fffffffffff")
        i+1
      }

    } toList

    print(dss)

   // JSONObject.apply(b).toString()

   //  println(d.toString())
    //  val c : Map[String, Any]= JSON.parseFull(str2).toMap
    //println(c.getOrElse("et"))

    //c.foreach{case (m,n) => println("userName=" + m+" ,age=" +n)}
    c match {
      case Some(map:Map[String,Any]) =>{
        println(map)
        println(map.get("@hostname").get)
      }
    }

    b match {
      // Matches if jsonStr is valid JSON and represents a Map of Strings to Any
      case Some(map: Map[String, Any]) => {
        println(map)
        println(map.get("et").get)
        println(map.get("vtm").get)
        val time = map.get("time").get.toString
        println(time+"ttttt")
        println(map.filterKeys(rs=>rs.contains("et")))
        val paw= map.filterKeys(_.contains("et"))
        val et : String= paw.values.mkString


        println(et)


      }
      case None => println("Parsing failed")
      case other => println("Unknown data structure: " + other)
    }



  }
}
