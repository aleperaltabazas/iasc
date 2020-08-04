package utn.frba.iasc.babylon

import com.google.inject.Guice
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.FilterHolder
import org.eclipse.jetty.servlet.ServletContextHandler
import org.slf4j.LoggerFactory
import spark.servlet.SparkApplication
import spark.servlet.SparkFilter
import utn.frba.iasc.babylon.config.*
import utn.frba.iasc.babylon.controller.Controller
import utn.frba.iasc.babylon.extension.drop
import utn.frba.iasc.babylon.util.LoggingFilter
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    BabylonIn().run(args)
}

class BabylonIn {
    companion object {
        private const val JMX_PORT = 29290
        private const val JMX_LOCALHOST = "127.0.0.1"
        private const val CONTEXT_PATH = "/"
        private val LOGGER = LoggerFactory.getLogger(BabylonIn::class.java)
    }

    fun run(args: Array<String>) {
        LOGGER.info("Args passed: ${args.joinToString(", ")}")
        val port = parsePort(args) ?: 9290
        LOGGER.info("Using port $port.")

        val handler = ServletContextHandler()
        handler.contextPath = CONTEXT_PATH

        listOf(
            object : FilterHolder(SparkFilter()) {
                init {
                    this.setInitParameter("applicationClass", App::class.java.name)
                }
            }
        ).forEach { handler.addFilter(it, "*", null) }

        try {
            val server = Server(port)
            server.handler = handler
            server.start()
            server.join()
        } catch (e: Exception) {
            LOGGER.error("Error starting the application", e)
            exitProcess(-1)
        }

        LOGGER.info("App up and running!")
    }

    private fun parsePort(args: Array<String>) =
        args.toList().find { it.matches("--port=\\d+".toRegex()) }?.drop("--port=")?.toInt()

    class App : SparkApplication {
        override fun init() {
            val injector = Guice.createInjector(
                ClientModule,
                ConfigModule,
                ConnectionModule,
                ControllerModule,
                ObjectMapperModule,
                ServiceModule
            )

            injector.allBindings.keys
                .filter { Controller::class.java.isAssignableFrom(it.typeLiteral.rawType) }
                .forEach {
                    val controller = injector.getInstance(it) as Controller
                    controller.register()
                    LOGGER.info("Registered controller ${controller.javaClass.simpleName}")
                }

            LoggingFilter.register()
        }
    }
}