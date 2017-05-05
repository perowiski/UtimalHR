package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.login;

/**
 * @author peter
 */
public class IndexController extends Controller {

    public Result index() {
        return ok(login.render());
    }

}
