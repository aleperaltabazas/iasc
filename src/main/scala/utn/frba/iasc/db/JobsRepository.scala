package utn.frba.iasc.db

import akka.actor.Cancellable

class JobsRepository(
  private val jobs: Map[String, Cancellable]
) {
  def cancel(id: String): Unit = jobs.find(_._1 == id).foreach(_._2.cancel())
}
