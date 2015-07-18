package controllers.ws

import javax.inject.Named

import actors.SocketEndpoint

import akka.actor.ActorRef
import com.google.inject.Inject
import play.api.mvc.{Controller, WebSocket}

class LogController @Inject() (@Named("arbiter") arbiter: ActorRef) extends Controller {

  import play.api.Play.current

  def log = WebSocket.acceptWithActor[String, String] { request => out =>
    SocketEndpoint.props(out, arbiter)
  }

  def source = WebSocket.acceptWithActor[String, String] { request => out =>
    // TODO: Will be routed to SourceForwarder,
    SocketEndpoint.props(out, arbiter)
  }
}
