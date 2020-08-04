package utn.frba.iasc.babylon.config

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import utn.frba.iasc.babylon.storage.AuctionStorage

object StorageModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("auctionStorage")
    fun auctionStorage() = AuctionStorage()
}