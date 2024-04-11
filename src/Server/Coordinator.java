package Server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import Utils.IRPC;

public class Coordinator extends Thread 
{
    private static Logger logger;
    private static TwoPCHelper helper;
	 public static void main(String args[]) throws Exception
	 {   	   	
            logger = new Logger();
            helper = new TwoPCHelper();
            RPCServer[] servers = new RPCServer[5];
	    		 
            if (args.length < 6) {
                String message =  "Please specify 5 Port Numbers and one value for key store name";
                logger.errorLogger(message);
            }
            else
            {
                helper.setPorts(args);
            }

            String keyStore = args[args.length - 1];
			// System.out.println("keystore is "+keyStore);
			// helper.setKeyStore(keyStore);

	    	for (int i = 0 ; i < servers.length ; i++)
	    	{
	    		try{
	    			servers[i] = new RPCServer(keyStore);
	    			servers[i].Server(helper.serverPorts[i]);
	    		    passOtherServerInfo(helper.serverPorts, helper.serverPorts[i],keyStore);
	    		    logger.responseLogger("Server " +(i+1)+ " is running at port " + helper.serverPorts[i]);
	    		    
	    		} catch (Exception e) {
	    		    logger.errorLogger("Server exception: " + e.toString());
				}

				Thread server = new Thread();
				server.start();

	    	}
	    }

	private static void passOtherServerInfo(int[] servers, int port, String keyStore) 
	{
		try{
			Registry registry = LocateRegistry.getRegistry(port);
		    
		    IRPC stub = (IRPC) registry.lookup(keyStore);
		    
		    int j = 0;
		    int[] other = new int[servers.length -1];
		    for (int i = 0 ; i < servers.length ; i++)
		    {
		    	if (servers[i] != port)
		    	{
		    		other[j] = servers[i];
		    		j++;
		    	}
		    }
		    		
		    stub.setServersInfo(other, port);
		}
		catch(Exception ex)
		{
			logger.errorLogger("Cannot connect to server!" + port);
			logger.errorLogger(ex.getMessage());
		}
	}
}