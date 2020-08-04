package utn.frba.iasc.babylon.util

import java.text.SimpleDateFormat
import java.util.*

object IdGen {
    private val sdf = SimpleDateFormat("yyyyMMddHHmm")

    fun buyer(): String = randomId("U")

    fun auction(): String = randomId("A")

    fun bid(): String = randomId("B")

    private fun randomId(prefix: String): String = "$prefix-${sdf.format(Date())}-${UUID.randomUUID()}"
}