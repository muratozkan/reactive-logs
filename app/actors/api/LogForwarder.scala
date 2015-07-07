package actors.api

import akka.actor.{ActorLogging, Actor, Props, ActorRef}

object LogForwarder {
  def props(out: ActorRef) = Props(new LogForwarder(out))
}

class LogForwarder(out: ActorRef) extends Actor with ActorLogging {

  override def receive: Receive = {
    case msg: String =>
      log.debug(s"Processing $msg")
      out ! msg.toLowerCase
  }

  override def postStop(): Unit = log.info("Disconnected")
}
