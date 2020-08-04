package utn.frba.iasc.babylon.service

import utn.frba.iasc.babylon.dto.CreateBuyerDTO
import utn.frba.iasc.babylon.model.Buyer
import utn.frba.iasc.babylon.storage.BuyerStorage

class BuyerService(
    private val buyerStorage: BuyerStorage
) {
    fun createBuyer(createBuyer: CreateBuyerDTO) {
        val buyer = Buyer(
            id = createBuyer.buyerId,
            username = createBuyer.username,
            ip = createBuyer.ip,
            interestTags = createBuyer.interestTags
        )

        buyerStorage.add(buyer)
    }

    fun listBuyers(): List<Buyer> = buyerStorage.findAll()
}