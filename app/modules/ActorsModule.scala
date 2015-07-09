package modules

import actors.Arbiter
import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport

class ActorsModule extends AbstractModule with AkkaGuiceSupport {

  override def configure(): Unit = {
    bindActor[Arbiter]("arbiter")
  }

}
