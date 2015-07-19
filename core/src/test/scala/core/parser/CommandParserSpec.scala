package core.parser

import core.BaseSpec
import core.protocol.{Command, SourceCommandParser, LogCommandParser}

class CommandParserSpec extends BaseSpec {

  val logParser = new LogCommandParser
  val sourceParser = new SourceCommandParser

  "Commands can parse" should {

    "list command `l` or `L`" in {
      val parsedLowercase = sourceParser.parse(0, "l")
      val parsedUpperCase = sourceParser.parse(1, "L")

      parsedLowercase shouldBe Command.List(0)
      parsedUpperCase shouldBe Command.List(1)
    }

    "from command `f s=1 l=DEBUG` or `F ...`" in {
      val parsedLowercase = logParser.parse(0, "f s=1 l=DEBUG")
      val parsedUpperCase = logParser.parse(1, "F s=2 l=DEBUG")

      parsedLowercase shouldBe Command.From(0, 1, "DEBUG")
      parsedUpperCase shouldBe Command.From(1, 2, "DEBUG")
    }
  }

  "commands should return Unknown" should {
    "when the first character is invalid" in {
      val parsedLog = logParser.parse(0, "asd")
      val parsedSource = sourceParser.parse(0, "asd")

      parsedLog shouldBe Command.Unknown("asd")
      parsedSource shouldBe Command.Unknown("asd")
    }

    "when from doesn't contain a source id" in {
      val missingId = logParser.parse(0, "f s l=DEBUG")
      val missingBoth = logParser.parse(0, "f l=DEBUG")

      missingId shouldBe Command.Unknown("f s l=DEBUG")
      missingBoth shouldBe Command.Unknown("f l=DEBUG")
    }
  }
}
