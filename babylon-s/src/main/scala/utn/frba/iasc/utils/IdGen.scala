package utn.frba.iasc.utils

import java.text.SimpleDateFormat
import java.util.{Date, UUID}

object IdGen {
  private val sdf = new SimpleDateFormat("yyyyMMddHHmm")

  def user: String = randomId("U")

  def auction: String = randomId("A")

  def bid: String = randomId("B")

  private def randomId(prefix: String): String = s"$prefix-${sdf.format(new Date())}-${UUID.randomUUID()}"
}
