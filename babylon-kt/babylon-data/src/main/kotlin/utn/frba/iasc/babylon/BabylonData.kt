package utn.frba.iasc.babylon

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Guice
import com.google.inject.Key
import com.google.inject.name.Names
import com.typesafe.config.ConfigFactory
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.FilterHolder
import org.eclipse.jetty.servlet.ServletContextHandler
import org.slf4j.LoggerFactory
import spark.servlet.SparkApplication
import spark.servlet.SparkFilter
import utn.frba.iasc.babylon.client.BabylonDataClient
import utn.frba.iasc.babylon.client.BabylonStorageClient
import utn.frba.iasc.babylon.config.ControllerModule
import utn.frba.iasc.babylon.config.ObjectMapperModule
import utn.frba.iasc.babylon.config.ServiceModule
import utn.frba.iasc.babylon.config.StorageModule
import utn.frba.iasc.babylon.connector.Connector
import utn.frba.iasc.babylon.controller.Controller
import utn.frba.iasc.babylon.extension.drop
import utn.frba.iasc.babylon.storage.AuctionStorage
import utn.frba.iasc.babylon.storage.BuyerStorage
import utn.frba.iasc.babylon.util.LoggingFilter
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    BabylonData().run(args)
}

var port: Int = 0

class BabylonData {
    companion object {
        private const val CONTEXT_PATH = "/"
        private val LOGGER = LoggerFactory.getLogger(BabylonData::class.java)
    }

    fun run(args: Array<String>) {
        LOGGER.info("Args passed: ${args.joinToString(", ")}")
        port = parsePort(args) ?: 9290
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
                ControllerModule,
                ObjectMapperModule,
                ServiceModule,
                StorageModule
            )

            val objectMapper = injector.getInstance(Key.get(ObjectMapper::class.java, Names.named("objectMapper")))

            val auctionStorage = injector.getInstance(Key.get(AuctionStorage::class.java, Names.named("auctionStorage")))
            val buyerStorage = injector.getInstance(Key.get(BuyerStorage::class.java, Names.named("buyerStorage")))

            val config = ConfigFactory.load()
            val storageConfig = config.getConfig("storage")
            storageConfig.getStringList("nodes").forEach {
                val storageConnector = Connector.create(objectMapper, it)
                val storageClient = BabylonStorageClient(storageConnector)
                val host = "http://localhost:$port"
                val neighbours = storageClient.registerSelf(host)

                neighbours.hosts.forEach { neighbour ->
                    val dataConnector = Connector.create(objectMapper, neighbour)
                    val dataClient = BabylonDataClient(dataConnector)
                    val auctions = dataClient.askAuctions()
                    val buyers = dataClient.askBuyers()

                    auctionStorage.merge(auctions)
                    buyerStorage.merge(buyers)
                }
            }

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