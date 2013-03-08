import java.util.regex.Pattern;

import org.apache.lucene.search.Query;


public abstract class QueryInterface {
	
	public static final String ALL_ALPHA_CYRILIC = "абвгдѓежзѕијклљмнњопрстќуфхцчџш";
	public static final String ALL_NUMBER = "0123456789";
	public static final String ALL_ALPHA_ALPHABET = "abcdefghijklmnopqrstuvwxyz";
	public static final String ALl_ALHPANUM_CYRILIC =  ALL_ALPHA_CYRILIC+ALL_NUMBER;
	public static final String ALL_ALPHANUM = ALl_ALHPANUM_CYRILIC+ALL_ALPHA_ALPHABET;
	
	public static final String DEFAULT_ALPHANUM = ALL_ALPHANUM;
	
	
	public abstract Query generateQuery();
	
	public abstract Pattern generatePattern();
	
}
