package actors

import actors.Command.From

import akka.actor.{ActorRef, ActorLogging, Actor, Props}

import scala.collection.mutable

object Arbiter {

  case class Subscribe(seq: Int, subject: ActorRef, level: String)
  case class Line(payload: String)

  case class Error(seq: Int, code: String)

  def props() = Props(new Arbiter)
}

class Arbiter extends Actor with ActorLogging {

  import Arbiter._
  import SourceDB._

  val sourceDb = context.actorOf(SourceDB.props(), "source-db")
  val sourceMap = mutable.Map.empty[Int, ActorRef]

  val awaitingLookup = mutable.Map.empty[Int, Subscribe]

  log.info("Arbiter started")

  override def receive: Receive = {
    case From(seq, sourceId, level) =>
      // validate sourceId

      awaitingLookup += (seq -> Subscribe(seq, sender(), level))
      sourceDb ! SourceLookup(seq, sourceId)
    case LookupResult(seq, None) =>
      awaitingLookup.get(seq) match {
        case None => log.debug(s"Request $seq already served")
        case Some(subscribe) => subscribe.subject ! Error(seq, "no-such-source")
      }
      awaitingLookup -= seq
    case LookupResult(seq, Some(source)) =>
      awaitingLookup.get(seq) match {
        case None => log.debug(s"Request $seq already served")
        case Some(subscribe) =>
          // lookup source id to get matching source actor
          //    - if no actor exits, lookup source ids to create the source actor
          val sourceRef = sourceMap.getOrElseUpdate(source.id, {
            context.actorOf(AppLog.props(source))
          })

          // forward subscription message to source actor
          sourceRef ! subscribe
      }
      awaitingLookup -= seq
    case List(seq) =>
      // Get source data App(id, host, app, sourceType) for all registered sources
      // forward to client
    case _ =>
  }

  override def postStop(): Unit = {
    log.info("Arbiter shut down")
  }
}
