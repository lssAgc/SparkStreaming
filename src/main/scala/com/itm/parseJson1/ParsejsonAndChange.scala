package com.itm.parseJson1

import java.io.IOException

import com.fasterxml.jackson.databind.{ObjectReader, JsonNode, ObjectMapper}
import com.fasterxml.jackson.databind.node.ObjectNode

import scala.collection.immutable.Map
import scala.util.parsing.json.JSON

/**
 * Created by gaochuang on 2017/2/26.
 */
object ParsejsonAndChange {
  def main(args: Array[String]) {
    val strTest =
      """{"@hostname":"HD-NODE04 0",
        |"@linenum":"530362   1",
        |"@filepath":"/opt/asafirewall-CSV/ 2",
        |"@message":"\"    3",
        |"filename":"asa_02_20170116-1.csv  4",   //
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
        |"@store_name":"ASA302013   24",   //
        |"@rule_name":"ASASYSLOG   25",    //
        |"@timestamp":"2017-03-16 16:58:51.263    26",
        |"@date":"2017-03-16   27"}""".stripMargin


    val str2 = "{\"et\":\"kanqiu_client_join\",\"vtm\":1435898329434,\"body\":{\"client\":\"866963024862254\",\"client_type\":\"android\",\"room\":\"NBA_HOME\",\"gid\":\"\",\"type\":\"\",\"roomid\":\"\"},\"time\":1435898329}"


    val resultStr = strTest.split(",")
    //resultStr.foreach(s =>println(s))
    var resultStr2 = "{"
    if(0 != resultStr.length) {
      if (28 == resultStr.length) {
        for (a <- 5 to 22) {
          resultStr2 += resultStr(a)+","
        }
      }
    }
    resultStr2 += resultStr(3).replace("@message", "raw_enent").replace("\\\"", "") + "}"


    resultStr2 += resultStr(25)
    val ssd = str2.split(",")
    var ss = "{"
    for(a <- 1 to 5){
      ss+=ssd(a)
    }
    ss+="}"
    println(ss)
    println(resultStr2+"llll")


    val builder = new StringBuilder
    builder.append("{")
    if(strTest.contains("starttime"))(

      builder.append("et")
      )




    /*val qw :ObjectReader= new ObjectReader()
    val  sd =  qw.readTree(str2)
    if(sd.has("et")){
      val sa = sd.get("et").textValue()

      print(sa)
    }*/
    val m: ObjectMapper = new ObjectMapper
    var rootNode: JsonNode = null
    try {
      rootNode = m.readTree(str2)
    }
    catch {
      case e: IOException => {
        e.printStackTrace
      }
    }
    val weaNode: JsonNode = rootNode.get("et")
    println(weaNode)
    println(m.writeValueAsString(weaNode))

    // 5 6 7 8 9 10 11 ...22  3 raw_event


  }
}
