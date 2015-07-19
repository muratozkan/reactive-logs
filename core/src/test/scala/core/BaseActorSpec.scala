package core

import akka.actor.ActorSystem
import org.scalatest.BeforeAndAfterAll

abstract class BaseActorSpec extends BaseSpec with BeforeAndAfterAll {

  implicit val system: ActorSystem = ActorSystem("test-system")

  override protected def afterAll(): Unit = {
    system.shutdown()
    system.awaitTermination()
  }
}
