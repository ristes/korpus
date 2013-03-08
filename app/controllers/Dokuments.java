package controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.Category;
import models.Dokument;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;
import utils.Entry;
import utils.LuceneUtils;
import utils.WordFilter;

public class Dokuments extends Controller {

	@Before
	public static void init() {
		List<Category> categories = Category.findAll();
		renderArgs.put("categories", categories);
		renderArgs.put("doc", new Dokument());
	}

	public static void index() {

		render();
	}

	public static void manage(Dokument doc) {
		if (doc != null) {
			renderArgs.put("doc", doc);
		}
		render();
	}

	public static void upload(Dokument doc) {

		try {
			File target = new File(Play.getFile(Play.configuration
					.getProperty("upload.path")), String.format("%s.txt",
					doc.name));

			if (!target.exists()) {
				target.createNewFile();
			}

			FileWriter fr = new FileWriter(target);
			fr.write(doc.text);
			fr.close();

			doc.path = String.format("%s%s.txt",
					Play.configuration.getProperty("upload.path"), doc.name);

			doc.save();
			LuceneUtils.addToIndex(doc);
			renderArgs.put("doc", doc);
			render("/dokuments/manage.html");
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
}
