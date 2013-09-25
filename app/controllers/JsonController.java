package controllers;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	protected static void toJson(Object o, String... excludeFields) {
		String encoding = Http.Response.current().encoding;
		response.setContentTypeIfNotSet("application/json; charset=" + encoding);
		GsonBuilder builder = new GsonBuilder().setDateFormat("dd.MM.yyyy");

		if (excludeFields != null && excludeFields.length > 0) {
			builder.setExclusionStrategies(ignoreManySet,
					new ExcludeFieldsStrategy(excludeFields));
		} else {
			builder.setExclusionStrategies(ignoreManySet);
		}
		Gson gson = builder.create();

		Appendable writer = new PrintStream(response.out);
		gson.toJson(o, writer);
		renderJSON("");

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

	private static class ExcludeFieldsStrategy implements ExclusionStrategy {

		Set<String> excludedFields = new HashSet<String>();

		public ExcludeFieldsStrategy(String[] excluded) {
			for (String s : excluded) {
				excludedFields.add(s);
			}
		}

		@Override
		public boolean shouldSkipField(FieldAttributes attrs) {
			if (excludedFields.contains(attrs.getName())) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public boolean shouldSkipClass(Class<?> clazz) {
			if (excludedFields.contains(clazz.getSimpleName() + ".class")) {
				return true;
			} else {
				return false;
			}
		}
	}

}
