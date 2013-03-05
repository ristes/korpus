package utils;

import models.Dokument;

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

public class LuceneUtils {

	public static void addToIndex(Dokument doc) throws Exception {
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_41);
		Directory index = new SimpleFSDirectory(Play.getFile(Play.configuration
				.getProperty("index.path")));

		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_41,
				analyzer);

		IndexWriter writer = new IndexWriter(index, config);
		Document luceneDoc = new Document();
		luceneDoc.add(new TextField("content", doc.text, Field.Store.NO));
		luceneDoc.add(new StringField("name", doc.name, Field.Store.YES));
		luceneDoc.add(new StringField("category", doc.category.name,
				Field.Store.YES));

		writer.addDocument(luceneDoc);
		writer.close();

	}
	
	public static void search(String... args){
		
	}

}
