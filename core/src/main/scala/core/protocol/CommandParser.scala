package core.protocol

import scala.util.parsing.combinator.RegexParsers

trait CommandParser {
  def parse(seq: Int, s: String): Command
}

abstract class BaseParser extends RegexParsers {

  def int: Parser[Int] =
    """\d+""".r ^^ (_.toInt)

  def parser(seq: Int): Parser[Command]

  def parse(seq: Int, s: String): Command =
    parseAll(parser(seq), s) match {
      case Success(command, _) => command
      case _                   => Command.Unknown(s)
    }
}

class SourceCommandParser extends BaseParser with CommandParser {

  def list(seq: Int): Parser[Command.List] =
    "l|L".r ^^ (_ => Command.List(seq))

  def parser(seq: Int): Parser[Command] = list(seq)
}

class LogCommandParser extends BaseParser with CommandParser {

  def from(seq: Int): Parser[Command.From] =
    "f|F".r ~> ("s=" ~> int) ~ ("l=" ~> "[a-zA-Z][a-zA-Z0-9-]*".r) ^^ {
      case (sourceId ~ level) =>
        Command.From(seq, sourceId, level)
    }

  def parser(seq: Int): Parser[Command] = from(seq)
}
