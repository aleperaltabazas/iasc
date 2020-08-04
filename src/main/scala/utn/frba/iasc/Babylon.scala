package utn.frba.iasc

import akka.actor.{Actor, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, path, post, _}
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import utn.frba.iasc.dto.{BabylonCodec, CreateAuctionDTO}
import utn.frba.iasc.utils.IdGen
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

import scala.io.StdIn

class Babylon

class UnActor extends Actor  {
  override def receive: Receive = {
    case s: String => println(s)
  }
}

object Babylon extends BabylonCodec {
  private val LOGGER = LoggerFactory.getLogger(classOf[Babylon])

  def main(args: Array[String]): Unit = {
    val ports = Seq("2551", "2552", "0")

    ports foreach { port =>
      val config = ConfigFactory.parseString(
        "akka.remote.artery.canonical.port=" + port + "\nakka.cluster.jmx.multi-mbeans-in-same-jvm = on"
      ).
        withFallback(ConfigFactory.load())

      implicit val system = ActorSystem("ShardingSystem", config)
      implicit val materializer = ActorMaterializer()
      implicit val executionContext = system.dispatcher

      val unActor = system.actorOf(Props[UnActor])

      val routes = concat(
        (path("auctions") & post) {
          entity(as[CreateAuctionDTO]) { auction: CreateAuctionDTO =>
            LOGGER.info("Create new auction")
            val id = IdGen.auction
            unActor ! id
            complete(StatusCodes.OK)
          }
        }, {
          (path("hello") & get) {
            LOGGER.info("Hello")
            complete(StatusCodes.OK)
          }
        }
      )

      val bindingFuture = Http().bindAndHandle(routes, "localhost", 8080)
      println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
      StdIn.readLine()
      bindingFuture
        .flatMap(_.unbind())
        .onComplete(_ => system.terminate())
    }
  }
}
