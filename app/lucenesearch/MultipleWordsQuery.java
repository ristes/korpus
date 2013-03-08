import java.util.ArrayList;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;


public class MultipleWordsQuery extends QueryInterface {
	

	private ArrayList<QueryWord> words;
	private ArrayList<Integer> mins;
	private ArrayList<Integer> maxs;
	
	public MultipleWordsQuery(ArrayList<QueryWord> words, ArrayList<Integer> mins,
			ArrayList<Integer> maxs) {
		this.words = words;
		this.mins = mins;
		this.maxs = maxs;
	}

	@Override
	public Query generateQuery()  {
		BooleanQuery res = new BooleanQuery();
		for ( QueryWord qw : words ) {
			res.add(qw.generateQuery(),Occur.MUST);
		}
		return res;
	}


	public Pattern generatePattern(String word_matcher) {
		QueryWord last_word = words.get(0);
		String final_query = "[^"+word_matcher+"]+"+last_word.getWordPatternString(word_matcher);
		for ( int i = 0 ; i < mins.size() ; ++i ) {
			QueryWord current_word = words.get(i+1);
			int min = mins.get(i);
			int max = maxs.get(i);
			String word_space = getWordSpaceCounter(word_matcher,min,max);
			final_query += word_space+"[^"+word_matcher+"]+"+current_word.getWordPatternString(word_matcher);
			last_word = current_word;
		}
		final_query += "[^"+word_matcher+"]+";
		Pattern res = Pattern.compile("("+final_query+")");
		return res;
	}

	public Pattern generatePattern() {
		return generatePattern(QueryInterface.DEFAULT_ALPHANUM);
	}

	private String getWordSpaceCounter(String word_matcher,int min, int max) {
		return "(?:[^"+word_matcher+"]+"+"["+word_matcher+"]+){"+min+","+max+"}";
	}
	

}
