package utn.frba.iasc.babylon.service

import utn.frba.iasc.babylon.client.BabylonDataClient
import utn.frba.iasc.babylon.dto.CreateAuctionDTO
import utn.frba.iasc.babylon.dto.PlaceBidDTO

class DataService {
    private val babylonDataClients: MutableList<BabylonDataClient> = ArrayList()

    fun createAuction(createAuction: CreateAuctionDTO) {
        babylonDataClients.forEach { it.createAuction(createAuction) }
    }

    fun placeBid(placeBid: PlaceBidDTO, auctionId: String) {
        babylonDataClients.forEach { it.placeBid(placeBid, auctionId) }
    }
}