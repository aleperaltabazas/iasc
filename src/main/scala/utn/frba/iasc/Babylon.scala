package utn.frba.iasc

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.google.inject.Guice
import utn.frba.iasc.controller.Controller
import utn.frba.iasc.injection.{ControllerModule, RepositoryModule, ServiceModule, UtilsModule}

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import scala.jdk.CollectionConverters._

object Babylon {
  def main(args: Array[String]) {
    implicit val system: ActorSystem = ActorSystem()
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val injector = Guice.createInjector(
      ControllerModule,
      RepositoryModule,
      ServiceModule,
      UtilsModule
    )

    val routes = injector.getAllBindings.asScala.keys
      .filter { it => classOf[Controller].isAssignableFrom(it.getTypeLiteral.getRawType) }
      .map(it => injector.getInstance(it).asInstanceOf[Controller].routes)
      .reduceLeft { (acc, e) => acc ~ e }

    val bindingFuture = Http().bindAndHandle(routes, "localhost", 8080)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
