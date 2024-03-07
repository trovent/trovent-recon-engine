package io.trovent;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;

public class Executor {
	@SuppressWarnings("deprecation")
	public String execute(boolean debugger, String ... processargs) {
		String result = "";
		try {
			String line;
			Process p = Runtime.getRuntime().exec(processargs);
			if (debugger) System.out.println(IOUtils.toString(p.getErrorStream()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line=reader.readLine()) != null) { 
				result = line;
			}
			reader.close();
		} catch (Exception e) { e.printStackTrace();}
		return result;
	}
}