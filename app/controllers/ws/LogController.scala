package controllers.ws

import javax.inject.{Named, Singleton}

import actors.SocketEndpoint
import actors.SocketEndpoint.{Command, Unknown}
import akka.actor.ActorRef

import com.google.inject.Inject
import play.api.mvc.{Controller, WebSocket}

class LogController @Inject() (@Named("arbiter") arbiter: ActorRef) extends Controller {

  import play.api.Play.current

  def parse(msg: String): Command = Unknown(msg)

  def log = WebSocket.acceptWithActor[String, String] { request => out =>
    SocketEndpoint.props(out, arbiter, parse(_: String))
  }

  def source = WebSocket.acceptWithActor[String, String] { request => out =>
    // TODO: Will be routed to SourceForwarder,
    SocketEndpoint.props(out, arbiter, parse(_: String))
  }
}
