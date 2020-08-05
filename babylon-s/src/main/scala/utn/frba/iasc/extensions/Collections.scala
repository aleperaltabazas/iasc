package utn.frba.iasc.extensions

object Collections {
  implicit class RichList[T](list: List[T]) {
    def without(t: T): List[T] = list.filterNot(_ == t)
  }
}
