package core.protocol

trait Command

object Command {

   case class From(seq: Int, sourceId: Int, level: String) extends Command
   case class List(seq: Int) extends  Command

   case class Unknown(command: String) extends Command
 }
