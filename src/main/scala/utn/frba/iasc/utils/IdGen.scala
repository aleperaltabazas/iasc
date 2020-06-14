package utn.frba.iasc.utils

import java.text.SimpleDateFormat
import java.util.{Date, UUID}

class IdGen(
  private val sdf: SimpleDateFormat
) {
  def user: String = randomId("U")

  def auction: String = randomId("A")

  def bid: String = randomId("B")

  private def randomId(prefix: String): String = s"$prefix-${sdf.format(new Date())}-${UUID.randomUUID()}"
}
