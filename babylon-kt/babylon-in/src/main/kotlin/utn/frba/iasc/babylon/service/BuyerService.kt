package utn.frba.iasc.babylon.service

import utn.frba.iasc.babylon.client.BabylonStorageClient
import utn.frba.iasc.babylon.dto.InCreateBuyerDTO
import utn.frba.iasc.babylon.dto.StorageCreateBuyerDTO
import utn.frba.iasc.babylon.util.IdGen

class BuyerService(
    private val babylonStorageClient: BabylonStorageClient
) {
    fun create(inCreate: InCreateBuyerDTO) {
        babylonStorageClient.createBuyer(
            StorageCreateBuyerDTO(
                buyerId = IdGen.buyer(),
                username = inCreate.username,
                ip = inCreate.ip,
                interestTags = inCreate.interestTags
            )
        )
    }
}