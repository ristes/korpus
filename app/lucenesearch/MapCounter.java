package lucenesearch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import utils.Entry;

public class MapCounter {

	private HashMap<String, Double> counts;
	private ArrayList<String> all_words;

	public MapCounter() {
		counts = new HashMap<String, Double>();
		all_words = new ArrayList<String>();
	}

	public void addTerm(String term, double weight) {
		Double d = counts.get(term);
		if (d == null) {
			d = 0.0;
			all_words.add(term);
		}
		d += weight;
		counts.put(term, d);
	}

	public void addTerm(String term) {
		addTerm(term, 1.0);
	}

	public ArrayList<Entry> getEntries(
			Collection<Map.Entry<String, Double>> entries) {
		ArrayList<Entry> results = new ArrayList<Entry>();
		for (Map.Entry<String, Double> e : entries) {
			results.add(new Entry(e.getKey(), e.getValue()));
		}
		return results;

	}

	public ArrayList<Entry> getTermSorted(boolean asc) {
		ArrayList<Entry> res = getEntries(counts.entrySet());
		Collections.sort(
				res,
				asc ? new ValueComparator() : Collections
						.reverseOrder(new ValueComparator()));
		return res;
	}

	public double getCount(String term) {
		return counts.get(term);
	}

	public boolean encountered(String term) {
		return counts.containsKey(term);
	}

}
