package utn.frba.iasc.babylon.config

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import utn.frba.iasc.babylon.client.BabylonOutClient
import utn.frba.iasc.babylon.client.BabylonStorageClient
import utn.frba.iasc.babylon.connector.Connector

object ClientModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("storageClient")
    fun storageClient(
        @Named("storageConnector") storageConnector: Connector
    ) = BabylonStorageClient(storageConnector)

    @Provides
    @Singleton
    @Named("outClient")
    fun outClient(
        @Named("outConnector") outConnector: Connector
    ) = BabylonOutClient(outConnector)
}