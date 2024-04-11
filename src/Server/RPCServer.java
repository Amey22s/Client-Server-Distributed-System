package Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import Utils.IRPC;

/**
 * RPCServer
 */
public class RPCServer extends GeneralServer implements IRPC{

    private TwoPCHelper helper;
    private int[] otherServers;

    
    /**
     * Constructor for RPCServer class.
     * @param keyStore key value store name for RMI registry.
     */

    public RPCServer(String keyStore)
    {
        try{
            this.keyStore = keyStore;
            String mapFilename = "Server/map"+GeneralServer.serverNo+".properties";
            GeneralServer.serverNo++;
            this.mapUtils = new MapUtils(mapFilename);
            this.logger = new Logger();
            this.helper = new TwoPCHelper();
            
            this.otherServers = new int[4];
            this.pendingChanges = Collections.synchronizedMap(new HashMap<UUID, Entry>());
            this.pendingPrepareAcks = Collections.synchronizedMap(new HashMap<UUID,Map<Integer,Boolean>>());
            this.pendingGoAcks = Collections.synchronizedMap(new HashMap<UUID,Map<Integer,Boolean>>());

          }
          catch(Exception e)
          {
            logger.errorLogger(e.getMessage());
          }
    }


    public String Operation(String input) throws RemoteException {
        logger.responseLogger("Input from client is "+input);
        String[] tokens = input.split(" ");
        return performOperation(tokens);
    }

    void Server(int portNo) {

        try {
        IRPC stub = (IRPC) UnicastRemoteObject.exportObject(this, portNo);
        Registry registry = LocateRegistry.createRegistry(portNo); 
        
        registry.rebind(keyStore, stub); 
        logger.responseLogger("Server started...");
        }
        catch(Exception e)
        {
            logger.errorLogger(e.getMessage());
        }
            
    }

    public String TPCPreprocess(UUID messageId, String input) throws RemoteException 
    {
        entry = createEntry(input); 
        if (entry.getOp().equalsIgnoreCase("GET"))
        {
            return Operation(input);
        }

        boolean canCommit = prepareCommit(messageId, entry, otherServers);
   
        if (!canCommit)
        {
            return "Operation could not be performed.";
        }

        helper.commit(messageId, otherServers);
        boolean commit = helper.commitAck(messageId, otherServers);
        if (!commit)
        {
            return "Operation could not be performed.";
        }

        Entry entryToBeCommited = this.pendingChanges.get(messageId);

        if (entryToBeCommited == null)
        {
            throw new IllegalArgumentException("Message with message id "+messageId+" not found in pending changes.");
        }

        String message = Operation(entryToBeCommited.toString());
        this.pendingChanges.remove(messageId);
        return message;
    }

    public void acknowledge(UUID messageId, int yourPort, AckType type) throws RemoteException{

        try {
        // logger.responseLogger("in acknowledge port is "+yourPort);

        if (type == AckType.acknowledgeGo)
        {
            this.pendingGoAcks.get(messageId).put(yourPort,true) ;
        } else if (type == AckType.acknowledgePrep)
        {
            // logger.responseLogger("in ack prep");
            // logger.responseLogger("pending prep acks"+pendingPrepareAcks);
            this.pendingPrepareAcks.get(messageId).put(yourPort,true);
            logger.responseLogger("after");
        }
    }
    catch(Exception e)
    {
        logger.errorLogger("Error in acknowledge is "+e.getMessage());
    }
   }
   
   public void processCommit(UUID messageId, int callBackServer) throws RemoteException{
       
       entry = this.pendingChanges.get(messageId);
       
       if (entry == null)
       {
           throw new IllegalArgumentException("Message with message id "+messageId+" not found in pending changes.");
       }
       
       Operation(entry.toString());
       this.pendingChanges.remove(messageId);
       helper.sendAck(messageId, callBackServer, AckType.acknowledgeGo);
   }

   public void setServersInfo(int[] otherServersPorts, int yourPort)
           throws RemoteException {
       
       this.otherServers = otherServersPorts;
       this.port = yourPort;
       this.helper.setPort(yourPort);
       this.helper.setLogger(logger);
       this.helper.setKeyStore(keyStore);
       this.helper.setMaps(pendingChanges,pendingPrepareAcks,pendingGoAcks);
   }

   public void processEntry(UUID messageId, Entry entry, int callBackServer) throws RemoteException{

        logger.responseLogger("In prepareKeyvalue");

        if (this.pendingChanges.containsKey(messageId)){
            helper.sendAck(messageId, callBackServer, AckType.acknowledgePrep);
        }

        this.pendingChanges.put(messageId, entry);
        helper.sendAck(messageId, callBackServer, AckType.acknowledgePrep);
    }

    private boolean prepareCommit(UUID messageId, Entry entry, int[] otherServers)
    {
        this.pendingChanges.put(messageId, entry);
        helper.canCommit(messageId, entry, otherServers);
        return helper.canCommitAck(messageId, entry, otherServers);
    }

}