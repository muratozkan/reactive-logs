package actors

import actors.Arbiter.Subscribe
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

  val sourceMap = mutable.Map.empty[Int, ActorRef]

  log.info("Arbiter started")

  override def receive: Receive = {
    case from@ From(seq, sourceId, level) =>
      // validate sourceId


      // lookup source id to get matching source actor
      //    - if no actor exits, lookup source ids to create the source actor
      val sourceRef = sourceMap.getOrElseUpdate(sourceId, {
        context.actorOf(AppLog.props(sourceId))
      })

      // forward subscription message to source actor
      sourceRef ! Subscribe(seq, sender(), level)
    case List(seq) =>
      // Get source data App(id, host, app, sourceType) for all registered sources
      // forward to client
    case _ =>
  }

  override def postStop(): Unit = {
    log.info("Arbiter shut down")
  }
}
