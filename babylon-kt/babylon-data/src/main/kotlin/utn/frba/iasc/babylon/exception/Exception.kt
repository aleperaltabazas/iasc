package utn.frba.iasc.babylon.exception

import java.lang.RuntimeException

abstract class HttpException(val status: Int, message: String) : RuntimeException(message)

class NotFoundException(message: String) : HttpException(404, message)