package core

import akka.testkit.TestProbe
import core.protocol.Command.From
import core.protocol.{Command, LogCommandParser, SourceCommandParser}

import scala.concurrent.duration.DurationInt

class SocketEndpointTest extends BaseActorSpec {

  val logParser = new LogCommandParser
  val sourceParser = new SourceCommandParser

  "log socket endpoint" when {
    val arbiterProbe = TestProbe()
    val outProbe = TestProbe()

    val logEndpointRef = system.actorOf(SocketEndpoint.props(outProbe.ref, arbiterProbe.ref, logParser))

    "receives unknown message" should {
      "return error" in {
        logEndpointRef ! "INVALID"

        outProbe expectMsg(100.milliseconds, "E Unknown")
      }
    }

    "receives from message" should {
      "direct to arbiter on well formed message" in {
        logEndpointRef ! "F s=1 l=DEBUG"

        outProbe expectNoMsg 100.milliseconds
        arbiterProbe expectMsg(100.milliseconds, From(1, 1, "DEBUG"))
      }

      "increment sequence number" in {
        logEndpointRef ! "f s=1 l=DEBUG"

        outProbe expectNoMsg 100.milliseconds
        arbiterProbe expectMsg(100.milliseconds, From(2, 1, "DEBUG"))
      }

      "increment after unknown message" in {
        logEndpointRef ! "INV"

        outProbe expectMsg(100.milliseconds, "E Unknown")

        logEndpointRef ! "F s=1 l=DEBUG"

        arbiterProbe expectMsg(100.milliseconds, From(4, 1, "DEBUG"))
      }
    }
  }

  "source socket endpoint" when {
    val arbiterProbe = TestProbe()
    val outProbe = TestProbe()

    val logEndpointRef = system.actorOf(SocketEndpoint.props(outProbe.ref, arbiterProbe.ref, sourceParser))

    "receives unknown message" should {
      "return error" in {
        logEndpointRef ! "INVALID"

        outProbe expectMsg(100.milliseconds, "E Unknown")
      }
    }

    "receives list message" should {
      "direct to arbiter on well formed message" in {
        logEndpointRef ! "L"

        outProbe expectNoMsg 100.milliseconds
        arbiterProbe expectMsg(100.milliseconds, Command.List(1))
      }

      "increment sequence number" in {
        logEndpointRef ! "l"

        outProbe expectNoMsg 100.milliseconds
        arbiterProbe expectMsg(100.milliseconds, Command.List(2))
      }

      "increment after unknown message" in {
        logEndpointRef ! "INV"

        outProbe expectMsg(100.milliseconds, "E Unknown")

        logEndpointRef ! "l"

        arbiterProbe expectMsg(100.milliseconds, Command.List(4))
      }
    }
  }
}
