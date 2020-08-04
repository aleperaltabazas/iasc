package utn.frba.iasc.babylon.config

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import utn.frba.iasc.babylon.service.DataService

object ServiceModule : AbstractModule() {
    @Provides
    @Singleton
    @Named("dataService")
    fun dataService() = DataService()
}