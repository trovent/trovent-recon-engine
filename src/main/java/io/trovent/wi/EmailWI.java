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

public class EmailWI implements WorkItemHandler {

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		Banner b = new Banner(workItem.getName());
		Executor ex = new Executor();
		Results res = new Results();
		String result = "{\"results\": []}";
		String domain = (String) workItem.getParameter("domain");
		boolean hunterio_run = (boolean) workItem.getParameter("hunterio_run");
		String hunterio_apikey = (String) workItem.getParameter("hunterio_apikey");
		
		if (hunterio_run) {
			b.start();
			try {
				
				b.param(1, domain);
				b.param(2, hunterio_apikey);

				File file = new File(Properties.workitems_dir + "/email_exec.py");
				result = ex.execute(true, file.getAbsolutePath(), "--domain", domain, "--token", hunterio_apikey);
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} 
		
		/*
		 * sample results:
		 * {'data': {'domain': 'trovent.io', 'disposable': False, 'webmail': False, 'accept_all': False, 'pattern': '{f}{last}', 'organization': 'Trovent Security', 'country': None, 'state': None, 'emails': [{'value': 'max.mustermann@trovent.io', 'type': 'personal', 'confidence': 79, 'sources': [{'domain': 'trovent.io', 'uri': 'http://trovent.io', 'extracted_on': '2022-03-10', 'last_seen_on': '2022-06-12', 'still_on_page': True}, {'domain': 'trovent.io', 'uri': 'http://trovent.io/penetration-testing', 'extracted_on': '2022-03-10', 'last_seen_on': '2022-06-12', 'still_on_page': True}], 'first_name': None, 'last_name': None, 'position': None, 'seniority': None, 'department': None, 'linkedin': None, 'twitter': None, 'phone_number': None, 'verification': {'date': None, 'status': None}}, {'value': 'proof@trovent.io', 'type': 'generic', 'confidence': 78, 'sources': [{'domain': 'trovent.io', 'uri': 'http://trovent.io/security-advisory-2104-03', 'extracted_on': '2022-03-10', 'last_seen_on': '2022-06-12', 'still_on_page': True}], 'first_name': None, 'last_name': None, 'position': None, 'seniority': None, 'department': 'communication', 'linkedin': None, 'twitter': None, 'phone_number': None, 'verification': {'date': None, 'status': None}}, {'value': 'info@trovent.io', 'type': 'generic', 'confidence': 77, 'sources': [{'domain': 'eurobits.de', 'uri': 'http://eurobits.de/mitglieds-details/troventsecurity', 'extracted_on': '2022-06-19', 'last_seen_on': '2022-06-19', 'still_on_page': True}, {'domain': 'trovent.io', 'uri': 'http://trovent.io/impressum', 'extracted_on': '2022-03-10', 'last_seen_on': '2022-06-12', 'still_on_page': True}], 'first_name': None, 'last_name': None, 'position': None, 'seniority': None, 'department': 'support', 'linkedin': None, 'twitter': None, 'phone_number': None, 'verification': {'date': None, 'status': None}}, {'value': 'bewerbung@trovent.io', 'type': 'generic', 'confidence': 77, 'sources': [{'domain': 'trovent.io', 'uri': 'http://trovent.io/karriere', 'extracted_on': '2022-03-10', 'last_seen_on': '2022-06-12', 'still_on_page': True}, {'domain': 'trovent.io', 'uri': 'http://trovent.io/karriere/penetrationstester', 'extracted_on': '2022-03-10', 'last_seen_on': '2022-06-12', 'still_on_page': True}, {'domain': 'trovent.io', 'uri': 'http://trovent.io/karriere/werkstudent-security-analyst', 'extracted_on': '2022-03-10', 'last_seen_on': '2022-06-12', 'still_on_page': True}, {'domain': 'trovent.io', 'uri': 'http://trovent.io/karriere/vertriebsinnendienst', 'extracted_on': '2022-03-10', 'last_seen_on': '2022-03-10', 'still_on_page': False}], 'first_name': None, 'last_name': None, 'position': None, 'seniority': None, 'department': None, 'linkedin': None, 'twitter': None, 'phone_number': None, 'verification': {'date': None, 'status': None}}, {'value': 'datenschutz@trovent.io', 'type': 'generic', 'confidence': 76, 'sources': [{'domain': 'trovent.io', 'uri': 'http://trovent.io', 'extracted_on': '2022-03-10', 'last_seen_on': '2022-06-12', 'still_on_page': True}, {'domain': 'trovent.io', 'uri': 'http://trovent.io/penetration-testing', 'extracted_on': '2022-03-10', 'last_seen_on': '2022-06-12', 'still_on_page': True}, {'domain': 'trovent.io', 'uri': 'http://trovent.io/datenschutz', 'extracted_on': '2022-03-10', 'last_seen_on': '2022-03-10', 'still_on_page': False}], 'first_name': None, 'last_name': None, 'position': None, 'seniority': None, 'department': None, 'linkedin': None, 'twitter': None, 'phone_number': None, 'verification': {'date': None, 'status': None}}], 'linked_domains': []}, 'meta': {'results': 5, 'limit': 10, 'offset': 0, 'params': {'domain': 'trovent.io', 'company': None, 'type': None, 'seniority': None, 'department': None}}}
		 */
		
		b.result(result);
		Map<String,Object> output = new HashMap<>();
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
