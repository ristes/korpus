package controllers;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import models.Category;
import models.Dokument;
import models.WordFilter;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import play.Play;
import play.mvc.Before;
import play.mvc.Controller;
import utils.LuceneUtils;

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
			manage(doc);
		} catch (Exception ex) {

			ex.printStackTrace();
		}

	}

	public static void search(WordFilter first, WordFilter firstSeparator,
			WordFilter second, WordFilter secondSeparator, WordFilter third) {
		
		
		
		

		index();
	}

	

}
