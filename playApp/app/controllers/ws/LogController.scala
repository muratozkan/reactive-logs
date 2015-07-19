package controllers.ws

import javax.inject.Named

import core.SocketEndpoint

import akka.actor.ActorRef
import com.google.inject.Inject
import core.protocol.{LogCommandParser, SourceCommandParser}
import play.api.mvc.{Controller, WebSocket}

class LogController @Inject() (@Named("arbiter") arbiter: ActorRef) extends Controller {

  import play.api.Play.current

  private val sourceParser = new SourceCommandParser
  private val logParser = new LogCommandParser

  def log = WebSocket.acceptWithActor[String, String] { request => out =>
    SocketEndpoint.props(out, arbiter, logParser)
  }

  def source = WebSocket.acceptWithActor[String, String] { request => out =>
    SocketEndpoint.props(out, arbiter, sourceParser)
  }
}
