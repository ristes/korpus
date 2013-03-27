package controllers.admin;


import play.mvc.With;
import models.Dokument;
import controllers.Authentication;
import controllers.CRUD;
import controllers.CRUD.For;

@For(Dokument.class)
@With(Authentication.class)
public class DocumentsAdmin extends CRUD{

}
