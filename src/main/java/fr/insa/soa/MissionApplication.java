package fr.insa.soa;

import javax.xml.ws.Endpoint;

public class MissionApplication {
	
	  public static String host = "localhost";
	    public static short port = 8089;

	    public void demarrerService() {
	        String url = "http://" + host + ":" + port + "/";
	        Endpoint.publish(url, new MissionService());
	    }

	    public static void main(String[] args) {
	        new MissionApplication().demarrerService();
	        System.out.println("Service démarré");
	        
	    }

}
