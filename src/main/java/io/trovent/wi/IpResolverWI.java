package io.trovent.wi;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.drools.core.process.instance.WorkItemHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

import io.trovent.Banner;
import io.trovent.Executor;
import io.trovent.dt.Properties;

public class IpResolverWI implements WorkItemHandler {

	@SuppressWarnings("unchecked")
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		Executor ex = new Executor();
		Banner b = new Banner(workItem.getName());
		Set<String> domain_set = (HashSet<String>) workItem.getParameter("input1");
		String domains = domain_set.stream().map(x -> x.trim()).collect(Collectors.joining(","));
		
		b.start();

		b.param(1, domains);

		File file = new File(Properties.workitems_dir + "/ipresolver_exec.py");
		String json = ex.execute(true, file.getAbsolutePath(), "-d", domains);
		
		Map<String,Set<String>> domain_ip = new HashMap<>();
		JSONArray arr = (new JSONObject(json)).getJSONArray("results");
		String domain;
		for (int i = 0; i < arr.length(); i++) {
			domain = (String)arr.getJSONObject(i).get("domain");
			domain_ip.put(domain,new HashSet<>());
			for (int j = 0; j < arr.getJSONObject(i).getJSONArray("ips").length(); j++) {
				domain_ip.get(domain).add(arr.getJSONObject(i).getJSONArray("ips").getString(j));
			}
		}
		
		b.result(domain_ip.toString());
		
		Map<String,Object> results = new HashMap<>();
		results.put("output1", domain_ip);
		try 
		{
			manager.completeWorkItem(workItem.getId(), results);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		b.finish();
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {}

}
