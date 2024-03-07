package io.trovent.wi;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.drools.core.process.instance.WorkItemHandler;
import org.json.JSONObject;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

import io.trovent.Banner;
import io.trovent.Executor;
import io.trovent.dt.Properties;

public class ReporterWI implements WorkItemHandler {

	@SuppressWarnings("unchecked")
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		Banner b = new Banner(workItem.getName());
		b.start();
		Executor ex = new Executor();
		Set<String> sha1list = (Set<String>) workItem.getParameter("input1");
		Set<String> sha256list = (Set<String>) workItem.getParameter("input2");
		Set<String> ips = (Set<String>) workItem.getParameter("input3");
		Set<String> domains = (Set<String>) workItem.getParameter("input4");
		Set<String> emails = (Set<String>) workItem.getParameter("input5");
		Map<String,List<Integer>> ip_ports = (HashMap<String,List<Integer>>) workItem.getParameter("input6");
		Set<String> urls = (Set<String>) workItem.getParameter("input7");
		
		b.param(1, sha1list.toString());
		b.param(2, sha256list.toString());
		b.param(3, ips.toString());
		b.param(4, domains.toString());
		b.param(5, emails.toString());
		b.param(6, ip_ports.toString());
		b.param(7, urls.toString());
		
		JSONObject json = new JSONObject();
		json.put("sha1list", sha1list);
		json.put("sha256list", sha256list);
		json.put("ips", ips);
		json.put("domains", domains);
		json.put("emails", emails);
		json.put("ports", ip_ports);
		json.put("urls", urls);
		
		b.result(json.toString());

		try {
			File file = new File(Properties.workitems_dir + "/reporter_exec.py");
			ex.execute(false, file.getAbsolutePath(),json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try 
		{
			manager.completeWorkItem(workItem.getId(), null);
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
