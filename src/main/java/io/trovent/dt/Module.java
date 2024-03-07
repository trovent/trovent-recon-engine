package io.trovent.dt;

public class Module {

	private String name;
	private boolean active;
	private String apikey;
	
	public Module() {}

	public Module (String name, boolean active) {
		this.name = name;
		this.active = active;
	}
	
	public Module (String name, boolean active, String apikey) {
		this.name = name;
		this.active = active;
		this.apikey = apikey;
	}
	
	public String getApikey() {
		return apikey;
	}

	public String getName() {
		return name;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}	
	
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
}