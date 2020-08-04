package utn.frba.iasc.babylon.actor

import utn.frba.iasc.babylon.client.BabylonDataClient
import utn.frba.iasc.babylon.dto.PlaceBidDTO
import utn.frba.iasc.babylon.dto.UpdateStatusDTO
import utn.frba.iasc.babylon.model.Buyer

data class Add(val host: String)

data class Clients(val clients: List<BabylonDataClient>)

object All

object Any

data class Client(val client: BabylonDataClient)

object HealthCheck

data class PlaceBid(val dto: PlaceBidDTO, val auctionId: String)

data class UpdateStatus(val updateStatusDTO: UpdateStatusDTO, val auctionId: String)

data class BuyersInterestedIn(val tags: String)

data class Buyers(val buyers: List<Buyer>)