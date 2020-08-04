package utn.frba.iasc.babylon.service

import com.fasterxml.jackson.databind.ObjectMapper
import utn.frba.iasc.babylon.client.BabylonDataClient
import utn.frba.iasc.babylon.connector.Connector

class GossipService(
    private val dataService: DataService,
    private val objectMapper: ObjectMapper
) {
    private val babylonDataClients: MutableList<BabylonDataClient> = ArrayList()

    fun dataNeighbourHosts() = babylonDataClients.map { it.host() }

    fun register(host: String) {
        val connector = Connector.create(objectMapper, host)
        val client = BabylonDataClient(connector)

        babylonDataClients.add(client)
        dataService.register(client)
    }
}