package actors.source

import akka.actor.Actor.Receive
import akka.actor.{ActorLogging, Actor}

object AppLogListener {

}

class AppLogListener extends Actor with ActorLogging {
  override def receive: Receive = ???
}
