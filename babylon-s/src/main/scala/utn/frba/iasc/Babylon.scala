package utn.frba.iasc

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.google.inject.name.Names
import com.google.inject.{Guice, Key}
import utn.frba.iasc.controller.Controller
import utn.frba.iasc.injection._
import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import utn.frba.iasc.actors.{RouterActor, UsersActor}

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

    implicit val executionContext: ExecutionContextExecutor = injector
      .getInstance(Key.get(classOf[ExecutionContextExecutor], Names.named("executionContext")))

    val ports = Seq("2551", "2552", "0")
    // In a production application you wouldn't typically start multiple ActorSystem instances in the
    // same JVM, here we do it to easily demonstrate these ActorSytems (which would be in separate JVM's)
    // talking to each other.
    ports foreach { port =>
      // Override the configuration of the port
      val config = ConfigFactory.parseString("akka.remote.artery.canonical.port=" + port + "\nakka.cluster.jmx.multi-mbeans-in-same-jvm = on").
        withFallback(ConfigFactory.load())

      val usersActor: ActorRef = injector
        .getInstance(Key.get(classOf[ActorRef], Names.named("usersActorRef")))
      val auctionActor: ActorRef = injector
        .getInstance(Key.get(classOf[ActorRef], Names.named("auctionActorRef")))
      // Create an Akka system
      val system = ActorSystem("ShardingSystem", config)
      // Create an actor that starts the sharding and sends random messages
      system.actorOf(Props(
        new RouterActor(
          system = system,
          executionContext = executionContext,
          usersActor = usersActor,
          auctionActor = auctionActor
        )
      ))
    }


    implicit val system: ActorSystem = injector
      .getInstance(Key.get(classOf[ActorSystem], Names.named("actorSystem")))
    implicit val materializer: ActorMaterializer = injector
      .getInstance(Key.get(classOf[ActorMaterializer], Names.named("materializer")))

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
