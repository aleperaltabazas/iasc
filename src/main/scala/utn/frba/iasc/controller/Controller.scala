package utn.frba.iasc.controller

import akka.http.scaladsl.server.Route

trait Controller {
  def routes: Route
}
