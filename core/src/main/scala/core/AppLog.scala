package core

import java.io.File

import core.Arbiter.{Line, Subscribe}
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import org.apache.commons.io.input.{Tailer, TailerListenerAdapter}

import scala.collection.mutable

object AppLog {
  def props(source: Source) = Props(new AppLog(source))
}

class AppLog(source: Source) extends Actor with ActorLogging {

  val subscribers = mutable.Map.empty[ActorRef, String]

  val tailer = new Tailer(new File(source.file), new TailerListenerAdapter {
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

  override def postStop(): Unit = isRunning = false
}
