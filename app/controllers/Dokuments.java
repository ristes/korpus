package controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.OneToMany;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import models.Category;
import models.Dokument;
import models.Tag;
import models.TaggedWord;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
import utils.Entry;
import utils.LuceneUtils;
import utils.WordFilter;

@With(Authentication.class)
public class Dokuments extends JsonController {

	@Before
	public static void init() {
		List<Category> categories = Category.findAll();
		renderArgs.put("categories", categories);
		renderArgs.put("doc", new Dokument());
	}

	public static void index() {

		render();
	}

	public static void list() {
		List<Dokument> dokuments = Dokument.findAll();
		render(dokuments);
	}

	public static void manage(Dokument doc) {
		if (doc != null) {
			renderArgs.put("doc", doc);
		}
		render();
	}

	public static void upload(Dokument doc) {

		try {
			doc.save();
			LuceneUtils.addToIndex(doc);
			renderArgs.put("doc", doc);
			manage(null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static void search(Integer precision, WordFilter first,
			WordFilter firstSeparator, WordFilter second,
			WordFilter secondSeparator, WordFilter third) throws IOException {

		if (precision == null) {
			precision = 10000;
		}

		ArrayList<Entry> results = null;

		if (third != null && third.word != null) {
			results = LuceneUtils.search(precision, first.word,
					first.sufixChars, first.prefixChars, second.word,
					second.sufixChars, second.prefixChars, third.word,
					third.sufixChars, third.prefixChars,
					firstSeparator.minWords, firstSeparator.maxWords,
					secondSeparator.minWords, secondSeparator.maxWords);
		} else if (second != null && second.word != null) {
			results = LuceneUtils.search(precision, first.word,
					first.sufixChars, first.prefixChars, second.word,
					second.sufixChars, second.prefixChars,
					firstSeparator.minWords, firstSeparator.maxWords);
		} else if (first != null && first.word != null) {
			results = LuceneUtils.search(precision, first.word,
					first.sufixChars, first.prefixChars);
		}

		renderJSON(results);
	}

	public static void details(String word) throws IOException {
		ArrayList<String> details = new ArrayList<String>();

		if (word != null) {
			details = LuceneUtils.getResults(word, 10);
		}
		renderJSON(details);
	}

	public static void editor(long id) {
		Dokument doc = Dokument.findById(id);
		List<Tag> tags = Tag.findAll();
		render(doc, tags);
	}

	public static void add(TaggedWord taggedWord) {
		taggedWord.save();

		toJson(taggedWord);

	}

	public static void taggedWords(Dokument dokument) {
		toJson(dokument.taggedWords, "dokument","user");
	}

	public static void remove(TaggedWord word) {
		word.delete();
		toJson(word);
	}

}
