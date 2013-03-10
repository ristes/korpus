package lucenesearch;

import java.util.Comparator;

import utils.Entry;

public class ValueComparator implements Comparator<Entry> {

	@Override
	public int compare(Entry arg0, Entry arg1) {
		return arg0.value.compareTo(arg1.value);
	}

}
