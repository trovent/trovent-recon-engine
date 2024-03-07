package io.trovent.wi;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.drools.core.process.instance.WorkItemHandler;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

import io.trovent.Banner;
import io.trovent.Executor;
import io.trovent.dt.Properties;
import io.trovent.dt.Results;

public class DnsDumpsterWI implements WorkItemHandler {

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		Banner b = new Banner(workItem.getName());
		Executor ex = new Executor();
		Results res = new Results();
		String result = "{\"results\": []}";
		boolean dnsdumpster_run = (boolean) workItem.getParameter("dnsdumpster_run");

		if (dnsdumpster_run) {
			b.start();
			
			try {
				String domain = (String) workItem.getParameter("input1");
				b.param(1, domain);
				File file = new File(Properties.workitems_dir + "/dnsdumpster_exec.py");
				result = ex.execute(true, file.getAbsolutePath(),"--target",domain);
				b.result(result);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

		Map<String,Object> output = new HashMap<String,Object>();
		output.put("output1", res.parseResults(result));
		try 
		{
			manager.completeWorkItem(workItem.getId(), output);
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
