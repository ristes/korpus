package controllers;

import java.io.PrintStream;

import javax.persistence.OneToMany;

import play.exceptions.UnexpectedException;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Http.Response;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class JsonController extends Controller {

	protected static void toJson(Object o) {
		String encoding = Http.Response.current().encoding;
		response.setContentTypeIfNotSet("application/json; charset="
				+ encoding);
		Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy")
				.setExclusionStrategies(ignoreManySet).create();
		
		Appendable writer=new PrintStream(response.out);
		gson.toJson(o, writer);

		
	}

	

	private static ExclusionStrategy ignoreManySet = new ExclusionStrategy() {

		@Override
		public boolean shouldSkipField(FieldAttributes attrs) {
			OneToMany otm = attrs.getAnnotation(OneToMany.class);
			if (otm != null) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}
	};
}
