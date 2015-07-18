package actors

import org.specs2.mutable.Specification

class CommandTest extends Specification {

  "Commands can parse" >> {
    "list command `l` or `L`" >> {
      val parsedLowercase = Command(0, "l")
      val parsedUpperCase = Command(1, "L")

      parsedLowercase mustEqual Command.List(0)
      parsedUpperCase mustEqual Command.List(1)
    }

    "from command `f s=1 l=DEBUG` or `F ...`" >> {
      val parsedLowercase = Command(0, "f s=1 l=DEBUG")
      val parsedUpperCase = Command(1, "F s=2 l=DEBUG")

      parsedLowercase mustEqual Command.From(0, 1, "DEBUG")
      parsedUpperCase mustEqual Command.From(1, 2, "DEBUG")
    }
  }

  "commands should return Unknown" >> {
    "when the first character is invalid" >> {
      val parsed = Command(0, "asd")

      parsed mustEqual Command.Unknown("asd")
    }

    "when from doesn't contain a source id" >> {
      val missingId = Command(0, "f s l=DEBUG")
      val missingBoth = Command(0, "f l=DEBUG")

      missingId mustEqual Command.Unknown("f s l=DEBUG")
      missingBoth mustEqual Command.Unknown("f l=DEBUG")
    }
  }
}
