package controllers.http

import play.api.mvc.{Action, Controller}

class AppController extends Controller {

  def index() = Action { request =>
    Ok(views.html.index("Your new application is ready."))
  }

}
