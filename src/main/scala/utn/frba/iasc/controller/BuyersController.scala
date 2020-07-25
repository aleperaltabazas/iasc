package utn.frba.iasc.controller

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import org.slf4j.{Logger, LoggerFactory}
import utn.frba.iasc.dto.{BuyerCodec, BuyerDTO, UserCodec}
import utn.frba.iasc.exception.HttpException
import utn.frba.iasc.service.UserService
import utn.frba.iasc.utils.IdGen

import scala.util.{Failure, Success}

class BuyersController(
  private val userService: UserService,
  private val idGen: IdGen
) extends Controller with BuyerCodec with UserCodec {
  private val LOGGER: Logger = LoggerFactory.getLogger(classOf[BuyersController])

  override def routes: Route = concat(
    (path("buyers") & post) {
      entity(as[BuyerDTO]) { buyer: BuyerDTO =>
        LOGGER.info("New buyer")
        val buyerId = idGen.user
        onComplete(userService.register(buyer, buyerId)) {
          case Failure(exception) => exception match {
            case HttpException(message, status) => complete(status, message)
            case _ => complete(StatusCodes.InternalServerError, exception.getMessage)
          }
          case Success(_) => complete(StatusCodes.OK)
        }
      }
    }
  )
}
