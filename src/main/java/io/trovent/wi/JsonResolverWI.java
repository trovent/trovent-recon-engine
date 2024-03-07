package io.trovent.wi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.drools.core.process.instance.WorkItemHandler;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

import io.trovent.Banner;

public class JsonResolverWI implements WorkItemHandler {

	@SuppressWarnings("unchecked")
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		Banner b = new Banner(workItem.getName());
		b.start();
		
		Set<String> results_set = new HashSet<>();
		
		try {
			Set<String> res1 = (HashSet<String>) workItem.getParameter("input1");
			Set<String> res2 = (HashSet<String>) workItem.getParameter("input2");
			Set<String> res3 = (HashSet<String>) workItem.getParameter("input3");

			b.param(1, res1.toString());
			b.param(2, res2.toString());
			b.param(3, res3.toString());
			
			results_set.addAll(res1);
			results_set.addAll(res2);
			results_set.addAll(res3);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		b.result(results_set.toString());
		
		Map<String,Object> results = new HashMap<>();
		results.put("output1", results_set);
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
