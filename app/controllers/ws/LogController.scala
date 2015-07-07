package controllers.ws

import actors.api.LogForwarder
import play.api.mvc.{WebSocket, Controller}

class LogController extends Controller {

  import play.api.Play.current

  def log = WebSocket.acceptWithActor[String, String] { request => out =>
    LogForwarder.props(out)
  }

  def source = WebSocket.acceptWithActor[String, String] { request => out =>
    // TODO: Will be routed to SourceForwarder
    LogForwarder.props(out)
  }
}
