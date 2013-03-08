package lucenesearch;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import utils.Entry;

public class Test {

	public static void main(String[] args) throws Exception {
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_41);
		Directory index = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_41,
				analyzer);
		IndexWriter w = new IndexWriter(index, config);
		int counter = 0;
		BufferedReader jin = new BufferedReader(new InputStreamReader(
				new FileInputStream("comments_data_text.txt"), "UTF-8"));
		counter = 0;
		int num_lines = 100000;
		int num_words = 0;
		while (jin.ready()) {
			counter++;
			if (counter % 10000 == 0)
				System.out.println(counter);
			if (counter > num_lines)
				break;
			String line = jin.readLine();
			String words[] = line.split(" ");
			int k = 0;
			for (int i = 0; i < words.length / 15; ++i) {
				String sentence_line = "";
				for (int j = 0; j < 15; ++j) {
					sentence_line += words[k++] + " ";
				}
				addDoc(w, " " + sentence_line + " ", Integer.toString(counter));
			}
		}
		w.close();
		QueryGenerator generator = new QueryGenerator();
		// QueryInterface qi = generator.getQueryInterface("за", 0, 0);
		QueryInterface qi = generator.getQueryInterface("за", 0, 0, "да", 0, 0,
				0, 4);
		// QueryInterface qi = generator.getQueryInterface("з1", 5,
		// 5,"з2",5,5,0,5,"з3",5,5,0,5);

		long start = System.currentTimeMillis();
		Query q = qi.generateQuery();
		IndexReader reader = IndexReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(10000,
				false);
		searcher.search(q, collector);
		MapCounter mc = new MapCounter();
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		Pattern pattern = qi.generatePattern();
		System.out.println(hits.length);
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			// System.out.println((i + 1) + ". " + d.get("id") + "\t" +
			// d.get("content"));
			String content = d.get("content");
			Matcher word_matcher = pattern.matcher(content);
			while (word_matcher.find()) {
				String word = word_matcher.group(1);
				mc.addTerm(word.trim().replaceAll(
						"[^" + QueryInterface.DEFAULT_ALPHANUM + "]+", " "));
			}
		}
		ArrayList<Entry> wc = mc.getTermSorted(false);
		for (Entry e : wc) {
			// System.out.println(e);
		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		/*
		 * 
		 * Matcher word_matcher = pattern.matcher(mega_string); while (
		 * word_matcher.find() ) { String word = word_matcher.group(1); //
		 * mc.addTerm(word); } ArrayList<Map.Entry<String, Double>> wc =
		 * mc.getTermSorted(false); for ( Map.Entry<String, Double> e : wc ) {
		 * // System.out.println(e); }
		 */
	}

	private static void addDoc(IndexWriter w, String title, String isbn)
			throws IOException {
		Document doc = new Document();
		doc.add(new TextField("content", title, Field.Store.YES));
		doc.add(new StringField("id", isbn, Field.Store.YES));
		w.addDocument(doc);
	}
}
