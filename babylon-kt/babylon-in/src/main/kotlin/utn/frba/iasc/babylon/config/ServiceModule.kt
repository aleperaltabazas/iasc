package utn.frba.iasc.babylon.config

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import utn.frba.iasc.babylon.client.BabylonOutClient
import utn.frba.iasc.babylon.client.BabylonStorageClient
import utn.frba.iasc.babylon.service.AuctionService
import utn.frba.iasc.babylon.service.BuyerService

object ServiceModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("auctionService")
    fun auctionService(
        @Named("storageClient") babylonStorageClient: BabylonStorageClient,
        @Named("outClient") babylonOutClient: BabylonOutClient
    ) = AuctionService(babylonStorageClient, babylonOutClient)

    @Provides
    @Singleton
    @Named("buyerService")
    fun buyerService(
        @Named("storageClient") babylonStorageClient: BabylonStorageClient
    ) = BuyerService(babylonStorageClient)
}