package utn.frba.iasc.babylon.service

import arrow.core.extensions.set.foldable.nonEmpty
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

    fun listBuyers(tags: List<String>?): List<Buyer> = if (tags == null) buyerStorage.findAll()
    else buyerStorage.findAll { it.interestTags.intersect(tags).nonEmpty() }
}