package fr.insa.soa;

import javax.xml.ws.Endpoint;

public class Application {
	
	  public static String host = "localhost";
	    public static short port = 8089;

	    public void demarrerService() {
	        String url = "http://" + host + ":" + port + "/";
	        Endpoint.publish(url, new MissionService());
	    }

	    public static void main(String[] args) {
	        new Application().demarrerService();
	        System.out.println("Service démarré");
	        
	    }

}
                                                                                                                         