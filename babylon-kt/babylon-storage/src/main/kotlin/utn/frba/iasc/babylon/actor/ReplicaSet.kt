package utn.frba.iasc.babylon.actor

import utn.frba.iasc.babylon.client.BabylonDataClient
import utn.frba.iasc.babylon.dto.PlaceBidDTO

data class Add(val host: String)

data class Clients(val clients: List<BabylonDataClient>)

object All

object HealthCheck

data class PlaceBid(val dto: PlaceBidDTO, val auctionId: String)