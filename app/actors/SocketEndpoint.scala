package actors

import actors.Arbiter.Line
import akka.actor.{ActorLogging, Actor, ActorRef, Props}

object SocketEndpoint {
  def props(out: ActorRef, arbiter: ActorRef) = Props(new SocketEndpoint(out, arbiter))
}

class SocketEndpoint(out: ActorRef,
                     arbiter: ActorRef) extends Actor with ActorLogging {

  private [this] var messageSeq = 0

  override def receive: Receive = {
    case msg: String => //parse bare message
      self ! Command.apply(messageSeq, msg)
      messageSeq = messageSeq + 1
    case Command.Unknown(c) =>
      out ! error("Unknown")
    case msg: Command =>
    // send to arbiter
      arbiter ! msg
    case Line(payload) =>
      out ! logLine(payload)
  }

  override def postStop(): Unit = log.debug("Disconnected")

  private def error(message: String) = {
    s"E $message"
  }

  private def logLine(payload: String) = {
    s"L $payload"
  }  
}
