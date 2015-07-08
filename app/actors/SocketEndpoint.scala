package actors

import actors.SocketEndpoint.Command
import akka.actor.{ActorLogging, Actor, ActorRef, Props}

object SocketEndpoint {

  trait Command
  case class From(seq: Int, filter: String) extends Command
  case class Unknown(command: String) extends Command
  

  def props(out: ActorRef, arbiter: ActorRef, parse: String => Command) = Props(new SocketEndpoint(out, arbiter, parse))
}

class SocketEndpoint(out: ActorRef, arbiter: ActorRef, parse: String => Command) extends Actor with ActorLogging {

  import actors.SocketEndpoint._
  
  private [this] var messageSeq: Int = _

  override def receive: Receive = {
    case msg: String => //parse bare message
      self ! parse(msg)
    case Unknown(c) =>
      out ! error("Unknown message 1")
    case msg: Command =>
    // send to arbiter
      arbiter ! msg
  }

  override def postStop(): Unit = log.info("Disconnected")

  private def error(message: String) = {
    s"E $message"
  }

  private def logLine(payload: String) = {
    s"L $payload"
  }  
}
