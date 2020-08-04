package utn.frba.iasc.babylon.actor

data class CloseIn(val id: String, val timeout: Int)

data class DoClose(val id: String)

data class Inform(val tags: List<String>, val auctionId: String)