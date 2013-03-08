import java.util.regex.Pattern;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.WildcardQuery;


public class QueryWord extends QueryInterface {

	private int suffix;
	private int prefix;
	private String term;
	
		
	public QueryWord(int suffix, int prefix, String term) {
		super();
		this.suffix = suffix;
		this.prefix = prefix;
		this.term = term;
	}
	
	public Query generateQuery() {
		return new WildcardQuery(new Term("content", (prefix>0?"*":"")+term+(suffix>0?"*":"")));
	}
	
	public Pattern generatePattern(String word_matcher) {
		String pattern_string = getFullPatternString(word_matcher);
		Pattern word_pattern = Pattern.compile(pattern_string);
		return word_pattern;   
	}
	
	public String getFullPatternString(String word_matcher) {
		String pattern_string = "[^"+word_matcher+"]+("+getPatternString(word_matcher,prefix)+term+getPatternString(word_matcher,suffix)+")[^"+word_matcher+"]+";
		
		return pattern_string;
	}
	
	public String getWordPatternString(String word_matcher) {
		return getPatternString(word_matcher,prefix)+term+getPatternString(word_matcher,suffix);
	}
	
	public String getFullPatternString() {
		return getFullPatternString(DEFAULT_ALPHANUM);
	}
	
	public String getPatternString(String word_matcher,int max) {
		return getPatternString(word_matcher,0,max);
	}
	
	public String getPatternString(String word_matcher,int min,int max) {
		return "["+word_matcher+"]{"+min+","+max+"}";
	}
	
	public Pattern generatePattern() {
		return generatePattern(DEFAULT_ALPHANUM);  
	}	
	
	public String getTerm() {
		return term;
	}
	
}
