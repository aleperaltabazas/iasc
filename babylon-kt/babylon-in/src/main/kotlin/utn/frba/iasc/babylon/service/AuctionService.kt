package utn.frba.iasc.babylon.service

import utn.frba.iasc.babylon.client.BabylonStorageClient
import utn.frba.iasc.babylon.dto.InCreateAuctionDTO
import utn.frba.iasc.babylon.dto.PlaceBidDTO
import utn.frba.iasc.babylon.dto.StorageCreateAuctionDTO
import utn.frba.iasc.babylon.util.IdGen

class AuctionService(
    private val babylonStorageClient: BabylonStorageClient
) {
    fun create(inCreate: InCreateAuctionDTO): String {
        val id = IdGen.auction()

        val body = StorageCreateAuctionDTO(
            auctionId = id,
            seller = inCreate.seller,
            article = inCreate.article,
            tags = inCreate.tags,
            basePrice = inCreate.basePrice
        )

        babylonStorageClient.createAuction(body)

        return id
    }

    fun placeBid(placeBid: PlaceBidDTO, auctionId: String) {
        babylonStorageClient.placeBid(placeBid, auctionId)
    }
}