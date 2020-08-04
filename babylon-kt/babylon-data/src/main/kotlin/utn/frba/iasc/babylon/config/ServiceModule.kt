package utn.frba.iasc.babylon.config

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import utn.frba.iasc.babylon.service.AuctionService
import utn.frba.iasc.babylon.service.BuyerService
import utn.frba.iasc.babylon.storage.AuctionStorage
import utn.frba.iasc.babylon.storage.BuyerStorage

object ServiceModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("auctionService")
    fun auctionService(
        @Named("auctionStorage") auctionStorage: AuctionStorage,
        @Named("buyerStorage") buyerStorage: BuyerStorage
    ) = AuctionService(auctionStorage, buyerStorage)

    @Provides
    @Singleton
    @Named("buyerService")
    fun buyerService(
        @Named("buyerStorage") buyerStorage: BuyerStorage
    ) = BuyerService(buyerStorage)

}