package utn.frba.iasc.actors

import akka.actor.Cancellable

case class AddJob(id: String, cancelRef: Cancellable)

sealed trait Op[T]

case class Create[C](c: C) extends Op[C]
case class Read[R](r: R, table: Table) extends Op[R]
case class Update[U](u: U) extends Op[U]
case class Delete[D](d: D) extends Op[D]

case class Combine[A, B](op1: Op[A], op2: Op[B]) extends Op[(A, B)]

sealed trait Table

case object Auctions extends Table
case object Bids extends Table
case object Users extends Table
case object Jobs extends Table
