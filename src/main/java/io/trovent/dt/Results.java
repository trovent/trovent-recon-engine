package io.trovent.dt;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

/*
 * JSON Schema: { 'results': [<String>] }
 */

public class Results implements Serializable {
	
	static final long serialVersionUID = 1L;
	
	private Set<String> results;
	
	public Results() {
		results = new HashSet<>();
	}
	
	public Set<String> getResults() {
		return results;
	}
	
	public Set<String> parseResults(String json) {
		try 
		{
			JSONArray arr = (new JSONObject(json)).getJSONArray("results");
			setResults(arr);			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return results;
	}

	private void setResults(JSONArray array) {
		for (int i = 0; i < array.length(); i++) {
			results.add(array.getString(i));
		}
	}
}
