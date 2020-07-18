package utn.frba.iasc.controller

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.slf4j.{Logger, LoggerFactory}
import utn.frba.iasc.service.UserService

class BuyersController(
  private val userService: UserService,
) extends Controller {
  private val LOGGER: Logger = LoggerFactory.getLogger(classOf[BuyersController])

  override def routes: Route = concat(
    (path("buyers") & post) {
      LOGGER.info("New buyer")
      complete((StatusCodes.OK, ""))
    }
  )

  //  def createBuyer(req: Request, res: Response): String = {
  //    //    val auctionDTO = decode[BuyerDTO](req.body()).getOrThrow
  //    //    userService.register(buyerDTO)
  //    emptyBody
  //  }
}
