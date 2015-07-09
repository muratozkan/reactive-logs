package actors

import java.io.File


import actors.Arbiter.{Line, Subscribe}
import akka.actor.{ActorRef, Actor, ActorLogging, Props}
import org.apache.commons.io.input.{TailerListenerAdapter, Tailer}

import scala.collection.mutable

object AppLog {
  def props(sourceId: Int) = Props(new AppLog(sourceId))
}

class AppLog(sourceId: Int) extends Actor with ActorLogging {

  val subscribers = mutable.Map.empty[ActorRef, String]

  val tailer = new Tailer(new File("./test.log"), new TailerListenerAdapter {
    override def handle(line: String): Unit =
      self ! line

    override def handle(ex: Exception): Unit =
      log.error(ex, "Listener fail")

  }, 0, true)

  var isRunning = false

  override def receive: Receive = {
    case Subscribe(seq, subscriber, level) =>
      log.debug(s"Subscribed $seq for $level")
      subscribers.update(subscriber, level.toLowerCase)

      if (!isRunning) {

        val thread = new Thread(tailer)
        thread.start()

        isRunning = true
      }

    case line: String =>
      log.debug("Received new line")
      subscribers.foreach(pair =>
        if (line.toLowerCase.startsWith(pair._2)) {
          pair._1 ! Line(line)
        })

    case _ =>
  }
}
