package controllers.admin;

import play.mvc.With;
import models.Category;
import models.Dokument;
import controllers.Authentication;
import controllers.CRUD;
import controllers.CRUD.For;

@For(Category.class)
@With(Authentication.class)
public class CategoryAdmin extends CRUD {

}
