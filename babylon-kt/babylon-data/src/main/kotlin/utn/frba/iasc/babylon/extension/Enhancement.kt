package utn.frba.iasc.babylon.extension

import arrow.core.Either
import arrow.fx.IO
import spark.Request
import spark.Response

fun String.drop(s: String): String = this.drop(s.length)

fun List<IO<Unit>>.sequenceIO(): IO<Unit> = this.foldRight(IO.unit) { e, acc -> e.followedBy(acc) }

fun <T, R : Throwable> Either<R, T>.rightOrThrow(): T = this.fold(
    { throw it },
    { it }
)

fun Response.headers(): Map<String, String> {
    val headers = mutableMapOf<String, String>()
    for (header in this.raw().headerNames) {
        headers[header] = this.raw().getHeader(header)
    }

    return headers
}

fun Request.prettyHeaders(): String = this.headers().joinToString(",") { "$it: ${this.headers(it)}" }

fun Response.prettyHeaders(): String = this.headers()
    .map { (header, value) -> "$header: $value" }
    .joinToString(",")

fun <T> id(): (T) -> T = { it }

