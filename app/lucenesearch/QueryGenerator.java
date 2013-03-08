package lucenesearch;

import java.util.ArrayList;

public class QueryGenerator {
	
	public QueryInterface getQueryInterface(String term,int suffix,int prefix) {
		QueryWord qw = new QueryWord(suffix, prefix, term);
		return qw;
	}

	public QueryInterface getQueryInterface(String term1,int suffix1,int prefix1,
			String term2,int suffix2,int prefix2,
			int min , int max) {
		QueryWord qw1 = new QueryWord(suffix1, prefix1, term1);
		QueryWord qw2 = new QueryWord(suffix2, prefix2, term2);
		ArrayList<QueryWord> queries = new ArrayList<QueryWord>();
		queries.add(qw1);queries.add(qw2);
		ArrayList<Integer> mins = new ArrayList<Integer>();
		mins.add(min);
		ArrayList<Integer> maxs = new ArrayList<Integer>();
		maxs.add(max);
		MultipleWordsQuery pq = new MultipleWordsQuery(queries, mins, maxs);
		return pq;
	}
	
	public QueryInterface getQueryInterface(String term1,int suffix1,int prefix1,
			String term2,int suffix2,int prefix2,
			int min12 , int max12,
			String term3,int suffix3,int prefix3,
			int min23 , int max23) {
		QueryWord qw1 = new QueryWord(suffix1, prefix1, term1);
		QueryWord qw2 = new QueryWord(suffix2, prefix2, term2);
		QueryWord qw3 = new QueryWord(suffix3, prefix3, term3);
		ArrayList<QueryWord> queries = new ArrayList<QueryWord>();
		queries.add(qw1);queries.add(qw2);queries.add(qw3);
		ArrayList<Integer> mins = new ArrayList<Integer>();
		mins.add(min12);
		mins.add(min23);
		ArrayList<Integer> maxs = new ArrayList<Integer>();
		maxs.add(max12);
		maxs.add(max23);
		MultipleWordsQuery pq = new MultipleWordsQuery(queries, mins, maxs);
		return pq;
	}
	
}
