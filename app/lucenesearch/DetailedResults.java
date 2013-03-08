import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;



public class DetailedResults {
	
	private ArrayList<String> words;
	
	public DetailedResults(String phrase) {
		String s_words[] = phrase.split("[^"+QueryInterface.DEFAULT_ALPHANUM+"]+");
		words = new ArrayList<String>(s_words.length);
		for ( String s : s_words ) {
			words.add(s);
		}
	}
	
	public QueryInterface getQueryInterface() {
		return new MultipleWordsQuery(getQueryWords(words), new ArrayList<Integer>(Collections.nCopies(words.size()-1,0)), new ArrayList<Integer>(Collections.nCopies(words.size()-1,1)));
	}

	private ArrayList<QueryWord> getQueryWords(ArrayList<String> words) {
		ArrayList<QueryWord> res = new ArrayList<QueryWord>(words.size());
		for ( String word : words ) {
			res.add(new QueryWord(0, 0, word));
		}
		return res;
	}	
	
	public String getHighlited(String content,Matcher phrase_matcher, int k) {
		System.out.println(content);
		int start = phrase_matcher.start();
		int end = phrase_matcher.end();
		return content.substring(kWordsBefore(content,k,start),start)+"<b>"+content.substring(start,end)+"<\\b>"+content.substring(end,kWordsAfter(content,k,end));
	}
	
	public int kWordsAfter(String content, int k, int end) {
		int count = 0;
		int i = end;
		int last = i-1;
		while ( count<k && i < content.length() ) {
			char c = content.charAt(i);
			if ( Character.isWhitespace(c) || c == '-' || c == ',' || c == ';' || c == ':' ) {
				if ( last < i-1 ) ++count;
				last = i;
			}
			++i;
		}
		return i;
	}

	public int kWordsBefore(String content, int k, int start) {
		int count = 0;
		int i = start;
		int last = i+1;
		while ( count<k && i > 0 ) {
			char c = content.charAt(i);
			if ( Character.isWhitespace(c) || c == '-' || c == ',' || c == ';' || c == ':' ) {
				if ( last > i+1 ) ++count;
				last = i;
			}
			--i;
		}
		return i;
	}
	
	public ArrayList<String> getResults(String searched, int max) {
		ArrayList<String> results = new ArrayList<String>();
		QueryInterface qi = new DetailedResults(searched).getQueryInterface();
		Query q = qi.generateQuery();
		IndexSearcher searcher = getIndexSearcher();
		TopScoreDocCollector collector = TopScoreDocCollector.create(max, false);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		Pattern phrase_pattern = qi.generatePattern();
		for(int i=0;i<hits.length;++i) {
			int docId = hits[i].doc;
		    Document d = searcher.doc(docId);
		    String content = d.get("content");
		    Matcher phrase_matcher = phrase_pattern.matcher(content);
		    while ( phrase_matcher.find() ) {
		    	String phrase = phrase_matcher.group(1);
		    	results.add(getHighlited(content,phrase_matcher,5));
		    }
		}
		return results;
	}

}
