package utn.frba.iasc.babylon.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import utn.frba.iasc.babylon.service.DataService
import utn.frba.iasc.babylon.service.GossipService

object ServiceModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("dataService")
    fun dataService() = DataService()

    @Provides
    @Singleton
    @Named("gossipService")
    fun gossipService(
        @Named("dataService") dataService: DataService,
        @Named("objectMapper") objectMapper: ObjectMapper
    ) = GossipService(dataService, objectMapper)
}