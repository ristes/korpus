package controllers.admin;

import play.mvc.With;
import models.User;
import controllers.Authentication;
import controllers.CRUD;
import controllers.CRUD.For;

@For(User.class)
@With(Authentication.class)
public class UserAdmin extends CRUD {

}
