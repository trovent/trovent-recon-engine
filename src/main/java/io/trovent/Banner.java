package io.trovent;

public class Banner {
	
	private String name;
	
	public Banner() {
		
	}
	
	public Banner(String name) {
		this.name = name;
	}
	
	public void print() {
		System.out.println(
				"____            ____            _____   ____                        _____             _            \n"
				+ "|_   _| __ ___   / /\\ \\   ___ _ _|_   _| |  _ \\ ___  ___ ___  _ __   | ____|_ __   __ _(_)_ __   ___ \n"
				+ "  | || '__/ _ \\ / /  \\ \\ / _ \\ '_ \\| |   | |_) / _ \\/ __/ _ \\| '_ \\  |  _| | '_ \\ / _` | | '_ \\ / _ \\\n"
				+ "  | || | | (_) / /    \\ \\  __/ | | | |   |  _ <  __/ (_| (_) | | | | | |___| | | | (_| | | | | |  __/\n"
				+ "  |_||_|  \\___/_/      \\_\\___|_| |_|_|   |_| \\_\\___|\\___\\___/|_| |_| |_____|_| |_|\\__, |_|_| |_|\\___|\n"
				+ "                                                                                  |___/              \n"
				+ "******************************************************************************************************\n");
	}
	
	public void start() {
		System.out.println("\n");
		System.out.println("Started " + this.name);
		System.out.println("*****************************");
	}
	
	public void result(String result) {
		System.out.printf("%s.%s", this.name, result);
		System.out.println("\n");
	}
	
	public void result(String result, String parameter) {
		System.out.printf("%s.%s.%s", this.name, parameter, result);
		System.out.println("\n");
	}
	
	public void param(int number, String value) {
		System.out.printf("%s.param%d: %s", this.name, number, value);
		System.out.println("\n");
	}
	
	public void finish() {
		System.out.println("Finished " + this.name);
	}
}
