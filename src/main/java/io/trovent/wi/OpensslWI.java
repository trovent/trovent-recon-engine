package io.trovent.wi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.drools.core.process.instance.WorkItemHandler;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

import io.trovent.Banner;
import io.trovent.Executor;

public class OpensslWI implements WorkItemHandler {
	
	Integer ssl = 443;

	@SuppressWarnings("unchecked")
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		Banner b = new Banner(workItem.getName());
		Executor ex = new Executor();
		Map<String,List<Integer>> domain_ports = (HashMap<String,List<Integer>>) workItem.getParameter("input1");
		List<String> domains = new ArrayList<>();
		String ip;
		List<Integer> ports;
		
		b.start();
		b.param(1, domain_ports.toString()); 
		
		for (Map.Entry<String,List<Integer>> entry : domain_ports.entrySet()) {
			ip = entry.getKey();
			ports = entry.getValue();
			if (ports.contains(ssl)) {
				domains.add(ip.trim());
			}
		}
		//String results = "{'results': [{'domain': 'dl.trovent.io', 'fingerprint': {'sha1': 'bdf6609dfe9ffd71fc91f2bc297bd97491cacf8c', 'sha256': '929153d13a43b414bd64f4bab6642909df10ef904261298699f8f7dcd65299a3'}}, {'domain': 'nextcloud.trovent.io', 'fingerprint': {'sha1': '488451102a2e9fb79a8b08dbf7ab7cdcb776b3ee', 'sha256': '27dc5e6e12e19dd774bb550299693b16e1e1d9f7c6daefafa7d8a5b4d8e508d7'}}, {'domain': 'ci.trovent.io', 'fingerprint': {'sha1': 'ac9f396b1bc72bd53ab86b6e4b798a51616b168a', 'sha256': 'c211152b16b9487bbcf42f7440dd27f2e306207a0f36d245c6b2b28dda6843d2'}}, {'domain': 'www.trovent.io', 'fingerprint': {'sha1': '85ea4c0773938dd8ff79a318039dbbcddd4c15c0', 'sha256': '5dbf8f6b21d46301185bdd8b5d11d8f88c2c175fa6f14e236c9973f8af91d81c'}}, {'domain': 'chat.trovent.io', 'fingerprint': {'sha1': 'a5696fe041f26c03f60c00da12b672fd76e7a992', 'sha256': 'bd36fc11d48d3664a3197410b7cc79840e9f94438ee57af216a25142e2ef2dac'}}, {'domain': 'registry.trovent.io', 'fingerprint': {'sha1': '1cdf24470176fe59bb90a28f8939740a65a9e5e7', 'sha256': '01bb6aa9b71dc0aed55164ea5710b03d2078799967361585856e02e2a13fdca6'}}, {'domain': 'WWW.trovent.io', 'fingerprint': {'sha1': '85ea4c0773938dd8ff79a318039dbbcddd4c15c0', 'sha256': '5dbf8f6b21d46301185bdd8b5d11d8f88c2c175fa6f14e236c9973f8af91d81c'}}, {'domain': 'matomo.trovent.io', 'fingerprint': {'sha1': '607aff9a25073f71650ee5c8307165ebe874714a', 'sha256': 'c224a45fbe19b5183e05ce32f1ddfbf9e448013c187aa37bec7c1e462501cf3b'}}]}";
		//System.out.println("Parameter1: " + domains.stream().collect(Collectors.joining(",")));
		String fingerprint256;
		String fingerprint1;
		Set<String> sha1list = new HashSet<>();
		Set<String> sha256list = new HashSet<>();
		for (String domain: domains) {
			fingerprint1 = ex.execute(false, "/bin/sh", "-c", "openssl s_client -connect " + domain + ":443 </dev/null 2>/dev/null | openssl x509 -fingerprint -noout -sha1 | awk -F= '{print $2}' | sed 's/://g'");
			fingerprint256 = ex.execute(false, "/bin/sh", "-c", "openssl s_client -connect " + domain + ":443 </dev/null 2>/dev/null | openssl x509 -fingerprint -noout -sha256 | awk -F= '{print $2}' | sed 's/://g'");
			sha1list.add(fingerprint1.toLowerCase());
			sha256list.add(fingerprint256.toLowerCase());
		}
		
		b.result(sha1list.toString(), "sha1list");
		b.result(sha256list.toString(), "sha256list");
		
		Map<String,Object> output = new HashMap<>();
		output.put("output1", sha1list);
		output.put("output2", sha256list);
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
