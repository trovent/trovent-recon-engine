package io.trovent.wi;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

public class NmapWI implements WorkItemHandler {

	List<String> results = new ArrayList<>();

	@SuppressWarnings("unchecked")
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		Executor ex = new Executor();
		//{bofrost-eagle.trovent.io=[35.242.248.130], dl.trovent.io=[80.241.214.116], nextcloud.trovent.io=[80.241.214.116], vm.trovent.io=[51.105.147.116], cloud.trovent.io=[217.160.0.10], www.pt.trovent.io=[217.160.0.10], ci.trovent.io=[80.241.214.116], www.trovent.io=[217.160.0.10], chat.trovent.io=[80.241.214.116], registry.trovent.io=[80.241.214.116], sky.trovent.io=[217.160.0.10], trovent.io=[217.160.0.10], int.trovent.io=[217.160.0.10], sec.trovent.io=[217.160.0.78], WWW.trovent.io=[217.160.0.10], www.cloud.trovent.io=[217.160.0.10], scan.trovent.io=[51.105.147.116], matomo.trovent.io=[80.241.214.116]}
		Map<String,Set<String>> domain_ips = (Map<String,Set<String>>) workItem.getParameter("ip_list");
		boolean nmap_run = (boolean) workItem.getParameter("nmap_run");

		Banner b = new Banner(workItem.getName());
			
		b.param(1, domain_ips.toString());
		// extract IPs
		Set<String> ips = new HashSet<>();
		for (Map.Entry<String,Set<String>> entry : domain_ips.entrySet()) {
			for (String ip : entry.getValue()) {
				ips.add(ip);
			}
		}
		
		Map<String,List<Integer>> domain_ports = new HashMap<>();
		Map<String,List<Integer>> ip_ports = new HashMap<>();
		
		if (nmap_run) {
			b.start();
			File file = new File(Properties.workitems_dir + "/nmap_exec.py");
			String res = ex.execute(true, file.getPath(), "--targets", ips.stream().collect(Collectors.joining(",")));

			b.result(res);

			//collect IPs and Ports

			JSONObject json = new JSONObject(res);
			JSONArray jarr = json.getJSONArray("results");

			for (int i = 0; i < jarr.length(); i++) {
				JSONObject obj = jarr.getJSONObject(i);
				String ip = obj.getString("ip");
				ip_ports.put(ip, new ArrayList<Integer>());
				List<Object> ports = obj.getJSONArray("ports").toList();
				for (Object port : ports) {
					ip_ports.get(ip).add((Integer)port);
				}
			}

			//map ports to domain
			for (Map.Entry<String,Set<String>> entry : domain_ips.entrySet()) {
				for (String ip : entry.getValue()) {
					for (Map.Entry<String,List<Integer>> e : ip_ports.entrySet()) {
						if (e.getKey().equals(ip)) {
							domain_ports.put(entry.getKey(), ip_ports.get(ip));
						}
					}
				}
			}
		}
		
		Map<String,Object> results = new HashMap<String,Object>();
		results.put("output1", domain_ports);
		results.put("output2", ip_ports);
		results.put("output3", ips);
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
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		manager.completeWorkItem(workItem.getId(), null);
	}
}
