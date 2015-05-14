package com.muthii;

import java.net.URL;

import javax.websocket.server.ServerContainer;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import com.muthii.websocket.EventSocket;


public class AppTest {
	
	public void runServer() throws Exception
	{
		
		Server server = new Server();
		
		addWebApp(server);
		


	}
	
	private void addWebApp(Server server) 
	{
		ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.addConnector(connector);
        
        //FInd location of index.html and use it as your base path to locate other resources
		String configFile = "index.html";
		URL configURL = ClassLoader.getSystemClassLoader().getResource(configFile);
		if ( configURL == null )
		{
			System.out.println("Cannot find " + configFile);
			return;			
		}		
		
		String path = configURL.getPath();	
		String basePath = configURL.getPath().substring(0, path.indexOf(configFile));
		
		WebAppContext context = new WebAppContext();
		context.setDescriptor(basePath + "/WEB-INF/web.xml");
		context.setResourceBase(basePath);
		
		context.setContextPath("/restwebapp");
		context.setParentLoaderPriority(true);
		context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "true");
		context.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
		
		server.setHandler(context);
		
		try
		{
			// Initialize javax.websocket layer
			ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);
			// Add WebSocket endpoint to javax.websocket layer
			wscontainer.addEndpoint(EventSocket.class);
			server.start();
			server.dump(System.err);
			server.join();
		}
		catch(Exception e){
			e.printStackTrace();
		}
				
	}
		
	
	
	public static void main(String[] args)
    {
    	AppTest rs = new AppTest();
    	try {
			rs.runServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}
