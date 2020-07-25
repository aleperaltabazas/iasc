package utn.frba.iasc

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.google.inject.name.Names
import com.google.inject.{Guice, Key}
import utn.frba.iasc.controller.Controller
import utn.frba.iasc.injection._

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import scala.jdk.CollectionConverters._

object Babylon {
  def main(args: Array[String]) {
    val injector = Guice.createInjector(
      ActorsModule,
      ContextModule,
      ControllerModule,
      RepositoryModule,
      ServiceModule,
      UtilsModule,
    )

    implicit val system: ActorSystem = injector
      .getInstance(Key.get(classOf[ActorSystem], Names.named("actorSystem")))
    implicit val materializer: ActorMaterializer = injector
      .getInstance(Key.get(classOf[ActorMaterializer], Names.named("materializer")))
    implicit val executionContext: ExecutionContextExecutor = injector
      .getInstance(Key.get(classOf[ExecutionContextExecutor], Names.named("executionContext")))

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
