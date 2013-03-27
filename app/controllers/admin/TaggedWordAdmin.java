package controllers.admin;

import play.mvc.With;
import models.Category;
import models.Dokument;
import models.TaggedWord;
import controllers.Authentication;
import controllers.CRUD;
import controllers.CRUD.For;

@For(TaggedWord.class)
@With(Authentication.class)
public class TaggedWordAdmin extends CRUD {

}
