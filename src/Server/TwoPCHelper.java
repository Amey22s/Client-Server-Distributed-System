package Server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import Utils.IRPC;

public class TwoPCHelper extends GeneralServer{

    protected int[] serverPorts;
	private String keyStore;


    public TwoPCHelper()
    {
        this.serverPorts = new int[5];
    }

	protected void setKeyStore(String keyStore)
	{
		this.keyStore = keyStore;
	}

	protected void setPort(int port)
	{
		this.port = port;
	}

	protected void setMaps(Map<UUID, Entry> pendingChanges, Map<UUID,Map<Integer,Boolean>> pendingPrepareAcks, 
	Map<UUID,Map<Integer,Boolean>> pendingGoAcks)
	{
		this.pendingChanges = pendingChanges;
		this.pendingGoAcks = pendingGoAcks;
		this.pendingPrepareAcks = pendingPrepareAcks;
	}

	protected void setLogger(Logger logger)
	{
		this.logger = logger;
	}

    protected void setPorts(String[] args)
    {
        for (int i = 0 ; i < args.length - 1; i++)
        {
            serverPorts[i] = Integer.parseInt(args[i]);
        }
    }


	protected boolean commitAck(UUID messageId, int[] otherServers) {

		int areAllAck = 0;
		int retry = 3;
		
		while (retry != 0)
		{
			try{
			  Thread.sleep(500);
			}catch(Exception ex)
			{
				logger.errorLogger("wait fail.");
			}
			
			areAllAck = 0;
			retry--;
			Map<Integer,Boolean> map = this.pendingGoAcks.get(messageId);
			
			for (int server : otherServers)
			{
				if (map.get(server))
				{
					areAllAck++;
				}
				else
				{
					prepCommit(messageId, server);
				}
			}
			if (areAllAck == 4)
			{
				return true;
			}
		}
		
		return false;
	}

	protected boolean canCommitAck(UUID messageId, Entry entry, int[] otherServers) {

		int areAllAck = 0;
		int retry = 3;
		
		while (retry != 0)
		{
			try{
			  Thread.sleep(500);
			}catch(Exception ex)
			{
				logger.errorLogger("wait fail.");
			}
			areAllAck = 0;
			retry--;
			Map<Integer,Boolean> map = this.pendingPrepareAcks.get(messageId);
			for (int server : otherServers)
			{
				if (map.get(server))
				{
					areAllAck++;
				}
				else
				{
					prepEntry(messageId, entry, server);
				}
			}
			
			if (areAllAck == 4)
			{
				return true;
			}
		}
		
		return false;
	}

	protected void canCommit(UUID messageId, Entry entry, int[] otherServers) {

		this.pendingPrepareAcks.put(messageId, Collections.synchronizedMap(new HashMap<Integer,Boolean>()));
		
		for (int server : otherServers)
		{
			prepEntry(messageId, entry, server);
		}
		
	}
    
	protected void commit(UUID mesUuid, int[] otherServers)
	{
		this.pendingGoAcks.put(mesUuid, Collections.synchronizedMap(new HashMap<Integer, Boolean>()));
		
		for (int server : otherServers)
		{
			prepCommit(mesUuid, server);
		}

	}
	
	protected void prepCommit(UUID messageId, int server)
	{
		try{
			this.pendingGoAcks.get(messageId).put(server, false);
			Registry registry = LocateRegistry.getRegistry(server);
		    IRPC stub = (IRPC) registry.lookup(keyStore);
		    stub.processCommit(messageId, port);
		}catch(Exception ex)
		{
			logger.errorLogger("Something went wrong in sending go, removing data from temporary storage");
		}
		
	}
	
	private void prepEntry(UUID messageId, Entry entry, int server)
	{
		
		try{
			this.pendingPrepareAcks.get(messageId).put(server, false);
			Registry registry = LocateRegistry.getRegistry(server);
		    IRPC stub = (IRPC) registry.lookup(keyStore);

		    stub.processEntry(messageId, entry, port);
		}catch(Exception ex)
		{
			logger.errorLogger("Something went wrong in sending Ack, removing data from temporary storage"+ex.getMessage());
		}
		
	}


	protected void sendAck(UUID messageId, int destination, AckType type)
	{
		try{
			Registry registry = LocateRegistry.getRegistry(destination);
		    IRPC stub = (IRPC) registry.lookup(keyStore);		    
		    stub.acknowledge(messageId, port, type);
		    
		}catch(Exception ex)
		{
			logger.errorLogger("Something went wrong in sending Ack, removing data from temporary storage"+ex.getMessage());
			this.pendingChanges.remove(messageId);
		}
	}
    


    void Server(int PORT) {
        
    }



    
}
