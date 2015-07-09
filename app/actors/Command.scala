package actors

import scala.util.parsing.combinator.RegexParsers

trait Command

object Command {

  case class From(seq: Int, sourceId: Int, level: String) extends Command
  case class List(seq: Int) extends  Command

  case class Unknown(command: String) extends Command

  def apply(seq: Int, command: String): Command =
    CommandParser.parseAsCommand(seq, command)
}

private object CommandParser extends RegexParsers {

  def int: Parser[Int] =
    """\d+""".r ^^ (_.toInt)

  def from(seq: Int): Parser[Command.From] =
    "f|F".r ~> ("s=" ~> int) ~ ("l=" ~> "[a-zA-Z][a-zA-Z0-9-]*".r) ^^ {
      case (sourceId ~ level) =>
        Command.From(seq, sourceId, level)
    }

  def list(seq: Int): Parser[Command.List] =
    "l|L".r ^^ (_ => Command.List(seq))

  private def parser(seq: Int): CommandParser.Parser[Command] =
    CommandParser.from(seq) | CommandParser.list(seq)

  def parseAsCommand(seq: Int, s: String): Command =
    parseAll(parser(seq), s) match {
      case Success(command, _) => command
      case _                   => Command.Unknown(s)
    }
}
