package utn.frba.iasc

import akka.http.scaladsl.model.{StatusCode, StatusCodes}

package object exception {
  object HttpException {
    def unapply(e: HttpException): Option[(String, StatusCode)] = Some((e.getMessage, e.status))
  }

  abstract class HttpException(message: String, val status: StatusCode) extends RuntimeException(message)

  class BadRequestException(message: String) extends HttpException(message, StatusCodes.BadRequest)
  class NotFoundException(message: String) extends HttpException(message, StatusCodes.NotFound)

  class UsernameAlreadyExistsException(val username: String)
    extends BadRequestException(s"Username $username is already taken")
}
