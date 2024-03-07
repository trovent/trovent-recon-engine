package io.trovent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import io.trovent.dt.Module;

public class ConfigReader {
	File file = null;
	List<Map<String,Object>> map = null;

	public ConfigReader(String path) {
		file = new File(path);
		Yaml yaml = new Yaml();
		try {
			Map<String,List<Map<String,Object>>> data = yaml.load(new FileInputStream(file));
			map = data.get("modules");
		} catch (FileNotFoundException e) {
			System.err.println("Config file not found or cannot be read");
			System.exit(1);
		}
	}

	public List<Module> parseModules() {
		List<Module> modules = new ArrayList<>();
		for (Map<String,Object> m : map) {
			try {
				if (m.containsKey("apikey")) {
					modules.add(new Module((String)m.get("name"), (boolean)m.get("active"), (String)m.get("apikey")));
				} else {
					modules.add(new Module((String)m.get("name"), (boolean)m.get("active")));
				}				
			}
			catch (Exception e)
			{
				System.err.println(String.format("Error while parsing modules: %s", e.getMessage()));
			}
		}
		return modules;
	}
}
