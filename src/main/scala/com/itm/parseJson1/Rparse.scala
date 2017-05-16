package com.itm.parseJson1

import java.io.FileInputStream
import java.util.Properties

import com.google.common.collect.Maps
import com.ibm.tss.bigdata.rule.ancient.TssRuleParserImpl
import com.ibm.tss.bigdata.rule.base.Context

/**
 * Created by gaochuang on 2017/3/25.
 */
object Rparse {
  def main(args: Array[String]) {
    val properties = new Properties()
    var path: String = null
    if (args.length == 1)
      path = args(0)
    else path = "pro.properties"
    loadProperties(properties, path)

    val RULE_ALIAS : String = properties.getProperty("ruleAlias")

     val lineData: String = "\"Nov 12 08:41:53 10.183.13.27 Nov 12 2016 08:41:53: %ASA-6-302015: Built inbound UDP connection 367839396 for outside:119.84.104.218/5053 (119.84.104.218/5053) to inside:192.168.36.14/53 (210.13.92.205/53)\""

    val ctx: Context = new Context()
    ctx.putAll(Maps.fromProperties(properties))

    val api: TssRuleParserImpl = new TssRuleParserImpl()

    var resultString: String = null
    try {
      //   api.parse(record, RULE_ALIAS, properties)
      resultString = api.parse(lineData, RULE_ALIAS, ctx)

      println(resultString + "________________resultSSSSS-------------------------------")
    }
    catch {
      case ex: Exception => {
        ex.printStackTrace()
        println(lineData)
        println(RULE_ALIAS)

      }

    }

  }


  def loadProperties(properties:Properties,filePath:String): Unit = {
    val path = Thread.currentThread().getContextClassLoader.getResourceAsStream("pro.properties") //文件要放到resource文件夹下
    //System.out.print(path + "-----------------PPPPPPPPPPPPPPPP_________________________________________")
    properties.load(path)
  }

}
