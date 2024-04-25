package Server;

public class PaxosDriver extends Thread{
    private static Logger logger;
    private static PaxosHelper helper;
	 public static void main(String args[]) throws Exception
	 {   	   	
            logger = new Logger();
            helper = new PaxosHelper();
            PaxosServer[] servers = new PaxosServer[5];
	    		 
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
	    			servers[i] = new PaxosServer(keyStore);
	    			servers[i].Server(helper.serverPorts[i]);
	    		    // passOtherServerInfo(helper.serverPorts, helper.serverPorts[i],keyStore);
	    		    logger.responseLogger("Server " +(i+1)+ " is running at port " + helper.serverPorts[i]);
	    		    
	    		} catch (Exception e) {
	    		    logger.errorLogger("Server exception: " + e.toString());
				}

				Thread server = new Thread();
				server.start();

	    	}

			GeneralServer.NUMBER_OF_SERVERS = servers.length;
	    }
}
