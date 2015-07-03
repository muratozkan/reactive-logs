package controllers

import play.api.mvc.Action
import play.api.mvc.Controller


class Application extends Controller {

  def index: Unit = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}
