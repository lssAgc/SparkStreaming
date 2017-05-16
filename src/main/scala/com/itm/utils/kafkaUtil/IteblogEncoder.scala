package com.itm.utils.kafkaUtil

import java.io.{ObjectOutputStream, ByteArrayOutputStream}

import kafka.serializer.Encoder
import kafka.utils.VerifiableProperties

/**
 * Created by gaochuang on 2017/3/29.
 */
class IteblogEncoder[T](props: VerifiableProperties = null) extends Encoder[T] {

  override def toBytes(t: T): Array[Byte] = {
    if (t == null)
      null
    else {
      var bo: ByteArrayOutputStream = null
      var oo: ObjectOutputStream = null
      var byte: Array[Byte] = null
      try {
        bo = new ByteArrayOutputStream()
        oo = new ObjectOutputStream(bo)
        oo.writeObject(t)
        byte = bo.toByteArray
      } catch {
        case ex: Exception => return byte
      } finally {
        bo.close()
        oo.close()
      }
      byte
    }
  }
}
