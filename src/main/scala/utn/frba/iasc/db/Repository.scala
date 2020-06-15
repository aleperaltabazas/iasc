package utn.frba.iasc.db

import utn.frba.iasc.model.Entity

abstract class Repository[T <: Entity](
  private var elements: List[T]
) {
  def find(id: String): Option[T] = elements.find(_.id == id)

  def register(e: T): Unit = {
    elements = e :: elements
  }

  def remove(e: T): Unit = removeBy(_.id == e.id)

  def removeBy(f: T => Boolean): Unit = {
    elements = elements.filterNot(f)
  }

  def all: List[T] = all(_ => true)

  def all(f: T => Boolean): List[T] = elements.filter(f)

  def update(t: T): Unit = {
    elements = t :: elements.filterNot(_.id == t.id)
  }
}
