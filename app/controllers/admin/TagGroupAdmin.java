package controllers.admin;

import play.mvc.With;
import models.Category;
import models.Dokument;
import models.TagGroup;
import controllers.Authentication;
import controllers.CRUD;
import controllers.CRUD.For;

@For(TagGroup.class)
@With(Authentication.class)
public class TagGroupAdmin extends CRUD {

}
