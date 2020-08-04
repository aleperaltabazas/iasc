package utn.frba.iasc.babylon.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import utn.frba.iasc.babylon.controller.CallbackController
import utn.frba.iasc.babylon.service.CallbackService

object ControllerModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("callbackController")
    fun callbackController(
        @Named("objectMapper") objectMapper: ObjectMapper,
        @Named("callbackService") callbackService: CallbackService
    ) = CallbackController(objectMapper, callbackService)
}