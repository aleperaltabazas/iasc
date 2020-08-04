package utn.frba.iasc.babylon.util

import org.slf4j.LoggerFactory
import spark.Request
import spark.Response
import spark.Spark
import utn.frba.iasc.babylon.extension.prettyHeaders

object LoggingFilter {
    private val LOGGER = LoggerFactory.getLogger(LoggingFilter::class.java)

    fun register() {
        Spark.before("/babylon-data/auction*", this::before)
        Spark.after("/babylon-data/auction*")
        Spark.before("/babylon-data/buyer*", this::before)
        Spark.after("/babylon-data/buyer*")
    }

    private fun before(req: Request, res: Response) {
        LOGGER.info("[REQUEST] -> ${req.requestMethod()}: ${req.pathInfo()}")
        LOGGER.info("Headers: [${req.prettyHeaders()}]")
        LOGGER.info("Body: [${req.body()}]")
    }

    private fun after(req: Request, res: Response) {
        LOGGER.info("[RESPONSE] -> Status: ${res.status()}")
        LOGGER.info("Headers: [${res.prettyHeaders()}]")
        LOGGER.info("Body: ${res.body()}")
    }
}