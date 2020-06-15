package utn.frba.iasc.controller

import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import spark.{Request, Response, Spark}
import utn.frba.iasc.dto.BuyerDTO
import utn.frba.iasc.service.UserService

class BuyersController(
  private val userService: UserService,
  private val mapper: ObjectMapper
) extends Controller {
  private val emptyBody: String = ""

  override def register(): Unit = {
    Spark.post("/buyers", (req: Request, res: Response) => createBuyer(req, res))
  }

  def createBuyer(req: Request, res: Response): String = {
    val buyerDTO: BuyerDTO = mapper.readValue(req.body(), new TypeReference[BuyerDTO] {})
    userService.register(buyerDTO)
    emptyBody
  }
}
