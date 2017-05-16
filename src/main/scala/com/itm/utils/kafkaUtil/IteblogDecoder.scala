package com.itm.utils.kafkaUtil

import java.io.{ByteArrayInputStream, ObjectInputStream}

import kafka.serializer.Decoder
import kafka.utils.VerifiableProperties

/**
 * Created by gaochuang on 2017/3/29.
 */
class IteblogDecoder[T](props: VerifiableProperties = null) extends Decoder[T] {

  def fromBytes(bytes: Array[Byte]): T = {
    var t: T = null.asInstanceOf[T]
    var bi: ByteArrayInputStream = null
    var oi: ObjectInputStream = null
    try {
      bi = new ByteArrayInputStream(bytes)
      oi = new ObjectInputStream(bi)
      t = oi.readObject().asInstanceOf[T]
    }
    catch {
      case e: Exception => {
        e.printStackTrace(); null
      }
    } finally {
      bi.close()
      oi.close()
    }
    t
  }
}
