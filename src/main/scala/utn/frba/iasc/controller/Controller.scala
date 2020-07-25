package utn.frba.iasc.controller

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.Route
import utn.frba.iasc.exception.HttpException

trait Controller {
  def routes: Route

  protected def handle(e: Throwable): Route = e match {
    case HttpException(message, status) => complete(status, message)
    case _ => complete(StatusCodes.InternalServerError, e.getMessage)
  }
}
