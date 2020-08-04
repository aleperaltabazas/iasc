package utn.frba.iasc.babylon.controller

import spark.Request
import spark.Response
import spark.Spark

class HealthCheckController : Controller {
    override fun register() {
        Spark.get("/babylon-data/health-check", this::healthCheck)
    }

    private fun healthCheck(req: Request, res: Response) {
        res.status(200)
        res.body("I feel fantastic and I'm still alive")
    }
}