package actors

import akka.actor.{Props, ActorLogging, Actor}

object SourceDB {

  case class SourceLookup(seq: Int, sourceId: Int)
  case class SourceList(seq: Int)

  case class LookupResult(seq: Int, sourceOption: Option[Source])
  case class ListResult(seq: Int, sources: List[Source])

  def props() = Props(new SourceDB)
}

class SourceDB extends Actor with ActorLogging {

  import actors.SourceDB._

  private val sourceTable = Map(1 -> Source(1, "test", "localhost", "FILE", "./test.log"))
  
  override def receive: Receive = {
    case SourceLookup(seq, sourceId) =>
     sender ! LookupResult(seq, sourceTable.get(sourceId))
    case SourceList(seq) =>
      sender ! ListResult(seq, sourceTable.values.toList)
  }
}
