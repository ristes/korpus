package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lucenesearch.DetailedResults;
import lucenesearch.MapCounter;
import lucenesearch.QueryGenerator;
import lucenesearch.QueryInterface;
import models.Dokument;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import play.Play;

public class LuceneUtils {

	public static Directory getLuceneIndex() throws IOException {

		Directory index = new SimpleFSDirectory(Play.getFile(Play.configuration
				.getProperty("index.path")));

		return index;
	}

	public static IndexWriter getIndexWriter() throws IOException {
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_41);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_41,
				analyzer);
		IndexWriter writer = new IndexWriter(getLuceneIndex(), config);
		return writer;
	}

	public static IndexReader getIndexReader() throws IOException {
		return DirectoryReader.open(getLuceneIndex());

	}

	public static IndexSearcher getIndexSearcher() throws IOException {
		return new IndexSearcher(getIndexReader());
	}

	public static void addToIndex(Dokument doc) throws Exception {

		IndexWriter writer = getIndexWriter();

		Document luceneDoc = new Document();
		luceneDoc.add(new TextField("content", " " + doc.text + " ",
				Field.Store.YES));
		luceneDoc.add(new StringField("name", doc.name, Field.Store.YES));
		luceneDoc.add(new StringField("category", doc.category.name,
				Field.Store.YES));

		writer.addDocument(luceneDoc);
		writer.close();

	}

	public static ArrayList<Entry> search(int precision, String term1,
			int suffix1, int prefix1, String term2, int suffix2, int prefix2,
			int min, int max) throws IOException {
		QueryGenerator generator = new QueryGenerator();
		QueryInterface qi = generator.getQueryInterface(term1, suffix1,
				prefix1, term2, suffix2, prefix2, min, max);

		return serach(precision, qi);

	}

	public static ArrayList<Entry> search(int precision, String term,
			int suffix, int prefix) throws IOException {

		QueryGenerator generator = new QueryGenerator();
		QueryInterface qi = generator.getQueryInterface(term, suffix, prefix);
		return serach(precision, qi);
	}

	public static ArrayList<Entry> search(int precision, String term1,
			int suffix1, int prefix1, String term2, int suffix2, int prefix2,
			String term3, int suffix3, int prefix3, int min12, int max12,
			int min23, int max23) throws IOException {

		QueryGenerator generator = new QueryGenerator();
		QueryInterface qi = generator.getQueryInterface(term1, suffix1,
				prefix1, term2, suffix2, prefix2, min12, max12, term3, suffix3,
				prefix3, min23, max23);

		return serach(precision, qi);

	}

	private static ArrayList<Entry> serach(int precision, QueryInterface qi)
			throws IOException {
		Query q = qi.generateQuery();

		IndexSearcher searcher = getIndexSearcher();

		TopScoreDocCollector collector = TopScoreDocCollector.create(precision,
				false);

		searcher.search(q, collector);
		MapCounter mc = new MapCounter();
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		Pattern pattern = qi.generatePattern();
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			String content = d.get("content");
			if (content != null) {
				Matcher word_matcher = pattern.matcher(content);
				while (word_matcher.find()) {
					String word = word_matcher.group(1);
					mc.addTerm(word.trim().replaceAll(
							"[^" + QueryInterface.DEFAULT_ALPHANUM + "]+", " "));
				}
			}
		}
		ArrayList<Entry> wc = mc.getTermSorted(false);

		return wc;
	}

	public static ArrayList<String> getResults(String searched, int max) throws IOException {
		return DetailedResults.getResults(getIndexSearcher(), searched, max);
	}
}
