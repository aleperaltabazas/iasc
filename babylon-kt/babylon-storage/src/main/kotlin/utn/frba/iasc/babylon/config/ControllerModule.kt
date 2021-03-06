package utn.frba.iasc.babylon.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import utn.frba.iasc.babylon.controller.DataController
import utn.frba.iasc.babylon.controller.GossipController
import utn.frba.iasc.babylon.service.DataService
import utn.frba.iasc.babylon.service.GossipService

object ControllerModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("dataController")
    fun dataController(
        @Named("objectMapper") objectMapper: ObjectMapper,
        @Named("dataService") dataService: DataService
    ) = DataController(objectMapper, dataService)

    @Provides
    @Singleton
    @Named("gossipController")
    fun gossipController(
        @Named("objectMapper") objectMapper: ObjectMapper,
        @Named("gossipService") gossipService: GossipService
    ) = GossipController(objectMapper, gossipService)
}