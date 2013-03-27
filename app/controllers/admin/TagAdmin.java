package controllers.admin;

import play.mvc.With;
import models.Category;
import models.Dokument;
import models.Tag;
import controllers.Authentication;
import controllers.CRUD;
import controllers.CRUD.For;

@For(Tag.class)
@With(Authentication.class)
public class TagAdmin extends CRUD {

}
