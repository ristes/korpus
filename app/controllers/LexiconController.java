package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import models.Lexicon;
import play.Play;
import play.mvc.Controller;
import play.mvc.With;

@With(Authentication.class)
public class LexiconController extends Controller {

	public static void initLexicon() throws Exception {
		File lexicon = Play.getFile("/uploads/lexicon.tbl");

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(lexicon)));
		String line = null;

		while ((line = reader.readLine()) != null) {
			String[] elms = line.split("	");
			Lexicon l = new Lexicon();
			l.word = elms[0];
			l.tag = elms[2].substring(0, 1);
			l.save();
			System.out.println(l);

		}
		
		reader.close();
	}
}
