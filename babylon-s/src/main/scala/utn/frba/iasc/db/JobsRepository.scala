package utn.frba.iasc.db

import akka.actor.Cancellable

import scala.collection.mutable

class JobsRepository(
  private val jobs: mutable.Map[String, Cancellable] = mutable.Map.empty
) {
  def cancel(id: String): Unit = jobs.find(_._1 == id).foreach(_._2.cancel())

  def add(id: String, cancellable: Cancellable): Unit = {
    jobs.put(id, cancellable)
  }
}
