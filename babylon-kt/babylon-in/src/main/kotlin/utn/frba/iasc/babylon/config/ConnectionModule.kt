package utn.frba.iasc.babylon.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import com.typesafe.config.Config
import utn.frba.iasc.babylon.connector.Connector

object ConnectionModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("storageConnector")
    fun storageClient(
        config: Config,
        @Named("objectMapper") objectMapper: ObjectMapper
    )  = Connector.create(objectMapper, config.getConfig("storage"))
}