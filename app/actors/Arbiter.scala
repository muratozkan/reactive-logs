package actors

import akka.actor.Actor.Receive
import akka.actor.{ActorLogging, Actor, Props}

object Arbiter {
  def props() = Props(new Arbiter)
}

class Arbiter extends Actor with ActorLogging {

  log.info("Arbiter started")

  override def receive: Receive = {
    case _ =>
  }

  override def postStop(): Unit = {
    log.info("Arbiter shut down")
  }
}
