import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;
import java.util.Map.Entry;

class ValueComparator implements Comparator<Map.Entry<String,? extends Comparable>> {

	@Override
	public int compare(Entry<String, ? extends Comparable> arg0, Entry<String, ? extends Comparable> arg1) {
		return arg0.getValue().compareTo(arg1.getValue());
	}
	
	
}


public class MapCounter {
	
	private HashMap<String,Double> counts;
	private ArrayList<String> all_words;
	
	public MapCounter( ) {
		counts = new HashMap<String,Double>();
		all_words = new ArrayList<String>(); 
	}
	
	public void addTerm( String term , double weight ) {
		Double d = counts.get(term);
		if ( d == null ) {
			d = 0.0;
			all_words.add(term);
		}
		d += weight;
		counts.put(term,d);
	}
	
	public void addTerm(String term) {
		addTerm(term,1.0);
	}
	
	public ArrayList<Map.Entry<String, Double>> getTermSorted(boolean asc)  {
		ArrayList<Map.Entry<String, Double>> res = new ArrayList<Map.Entry<String, Double>>(counts.entrySet());
		Collections.sort(res,asc?new ValueComparator():Collections.reverseOrder(new ValueComparator()));
		return res;
	}
	
	public double getCount(String term) {
		return counts.get(term);
	}
	
	public boolean encountered(String term) {
		return counts.containsKey(term);
	}
	
	
	

}
