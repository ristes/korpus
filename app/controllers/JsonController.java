package controllers;

import javax.persistence.OneToMany;

import play.mvc.Controller;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class JsonController extends Controller{
	
	protected static String toJson(Object o) {
		Gson gson = new GsonBuilder().setDateFormat("dd.MM.yyyy")
				.setExclusionStrategies(ignoreManySet).create();
		String s = gson.toJson(o);
		return s;
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
