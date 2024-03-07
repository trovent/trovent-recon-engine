package io.trovent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;

import io.trovent.dt.Properties;
import io.trovent.wi.CrtshWI;
import io.trovent.wi.DnsDumpsterWI;
import io.trovent.wi.DorkerWI;
import io.trovent.wi.EmailWI;
import io.trovent.wi.GobusterWI;
import io.trovent.wi.IpResolverWI;
import io.trovent.wi.JsonResolverWI;
import io.trovent.wi.NmapWI;
import io.trovent.wi.OpensslWI;
import io.trovent.wi.ReporterWI;

import io.trovent.dt.Module;

public class ProcessMain {

	public static void main(String[] args) {
		
		Banner banner = new Banner();
		banner.print();
		
		if (args.length < 1) {
			System.out.println("Please provide a target");
			System.exit(1);
		}
		
		String domain = args[0];
		System.out.printf("Target: [%s]\n", domain);
		ConfigReader configReader = new ConfigReader(Properties.config);

		try
		{
			KieServices ks = KieServices.Factory.get();
			KieContainer kContainer = ks.getKieClasspathContainer();
			KieBase kbase = kContainer.getKieBase("kbase");
	
			RuntimeManager manager = createRuntimeManager(kbase);
			RuntimeEngine engine = manager.getRuntimeEngine(null);
			KieSession ksession = engine.getKieSession();
			ksession.getWorkItemManager().registerWorkItemHandler("DnsDumpster", new DnsDumpsterWI());
			ksession.getWorkItemManager().registerWorkItemHandler("Nmap", new NmapWI());
			ksession.getWorkItemManager().registerWorkItemHandler("Crtsh", new CrtshWI());
			ksession.getWorkItemManager().registerWorkItemHandler("Gobuster", new GobusterWI());
			ksession.getWorkItemManager().registerWorkItemHandler("JsonResolver", new JsonResolverWI());
			ksession.getWorkItemManager().registerWorkItemHandler("IpResolver", new IpResolverWI());
			ksession.getWorkItemManager().registerWorkItemHandler("Reporter", new ReporterWI());
			ksession.getWorkItemManager().registerWorkItemHandler("Openssl", new OpensslWI());
			ksession.getWorkItemManager().registerWorkItemHandler("Email", new EmailWI());
			ksession.getWorkItemManager().registerWorkItemHandler("Dork", new DorkerWI());
		
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("domain", args[0]);
			for (Module m : configReader.parseModules()) {
				if (m.getName().equals("DnsDumpster")) {
					params.put("dnsdumpster_run", m.isActive());
				}
				if (m.getName().equals("Gobuster")) {
					params.put("gobuster_run", m.isActive());
				}
				if (m.getName().equals("Crtsh")) {
					params.put("crtsh_run", m.isActive());
				}
				if (m.getName().equals("Dork")) {
					params.put("dork_run", m.isActive());
				}
				if (m.getName().equals("HunterIO")) {
					params.put("hunterio_run", m.isActive());
					params.put("hunterio_apikey", m.getApikey());
				}
				if (m.getName().equals("Nmap")) {
					params.put("nmap_run", m.isActive());
				}
			}
			
			List<Module> modules = configReader.parseModules();	
			ksession.startProcess("io.trovent.engine",params);	
	
			manager.disposeRuntimeEngine(engine);
			System.exit(0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static RuntimeManager createRuntimeManager(KieBase kbase) {
		RuntimeEnvironmentBuilder builder = RuntimeEnvironmentBuilder.Factory.get()
				.newEmptyBuilder().persistence(false).knowledgeBase(kbase);
		return RuntimeManagerFactory.Factory.get()
				.newSingletonRuntimeManager(builder.get(), "io:trovent:engine:1.0");
	}

}