package utn.frba.iasc

import com.google.inject.Guice
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{FilterHolder, ServletContextHandler}
import spark.servlet.{SparkApplication, SparkFilter}
import utn.frba.iasc.controller.Controller
import utn.frba.iasc.injection._

import scala.jdk.CollectionConverters._

object Babylon {
  def main(args: Array[String]): Unit = {
    val port = 9290

    val handler = new ServletContextHandler()
    handler.setContextPath("/")

    List(
      new FilterHolder(new SparkFilter()) {
        this.setInitParameter("applicationClass", classOf[App].getName)
      }
    ).foreach(it => handler.addFilter(it, "*", null))

    val server = new Server(port)
    server.setHandler(handler)
    server.start()
    server.join()

    println("App up and running.")
  }

  class App extends SparkApplication {
    override def init(): Unit = {
      val injector = Guice.createInjector(
        ControllerModule,
        ObjectMapperModule,
        RepositoryModule,
        ServiceModule,
        UtilsModule
      )

      injector.getAllBindings.asScala.keys
        .filter { it => classOf[Controller].isAssignableFrom(it.getTypeLiteral.getRawType) }
        .foreach(it => injector.getInstance(it).asInstanceOf[Controller].register())
    }
  }
}
