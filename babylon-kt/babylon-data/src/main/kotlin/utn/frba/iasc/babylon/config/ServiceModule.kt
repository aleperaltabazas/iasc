package utn.frba.iasc.babylon.config

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import utn.frba.iasc.babylon.service.AuctionService
import utn.frba.iasc.babylon.storage.AuctionStorage

object ServiceModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("auctionService")
    fun auctionService(
        @Named("auctionStorage") auctionStorage: AuctionStorage
    ) = AuctionService(auctionStorage)
}