package utn.frba.iasc.extensions

import scala.collection.mutable

trait CollectionsSyntax {
  implicit class RichList[T](list: List[T]) {
    def without(t: T): List[T] = list.filterNot(_ == t)
  }

  implicit class RichBuffer[T](buffer: mutable.Buffer[T]) {
    def removeBy(f: T => Boolean): Unit = {
      var length = buffer.length
      var i = 0; while(i < length) {
        if (f(buffer(i))) {
          i += 1
        } else {
          length -= 1
          buffer.remove(i)
        }
      }
    }
  }
}
