package Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;


import Utils.IRPC;

/**
 * RPCServer
 */
public class RPCServer extends GeneralServer implements IRPC{

    private TwoPCHelper helper;
    private int[] otherServers;

    public RPCServer(String keyStore)
    {
        try{
            this.keyStore = keyStore;
            this.mapUtils = new MapUtils("Server/map.properties");
            this.logger = new Logger();
            this.helper = new TwoPCHelper();
            this.helper.setKeyStore(keyStore);
            
            this.otherServers = new int[4];

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
        
        registry.rebind("kv", stub); 
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

    GeneralServer.pendingChanges.put(messageId, entry);
    helper.tellToPrepare(messageId, entry, otherServers);
    boolean prepareSucc = helper.waitAckPrepare(messageId, entry, otherServers);
    if (!prepareSucc)
    {
        return "fail";
    }

    helper.tellToGo(messageId, otherServers);
    boolean goSucc = helper.waitToAckGo(messageId, otherServers);
    if (!goSucc)
    {
        return "fail";
    }

    Entry v = GeneralServer.pendingChanges.get(messageId);

    if (v == null)
    {
        throw new IllegalArgumentException("The message is not in the storage");
    }

    String message = Operation(v.toString());
    GeneralServer.pendingChanges.remove(messageId);
    return message;
    }

    public void acknowledge(UUID messageId, int yourPort, AckType type) throws RemoteException{

        try {
        logger.responseLogger("in acknowledge port is "+yourPort);

        if (type == AckType.acknowledgeGo)
        {
            GeneralServer.pendingGoAcks.get(messageId).put(yourPort,true) ;
        } else if (type == AckType.acknowledgePrep)
        {
            // logger.responseLogger("in ack prep");
            // logger.responseLogger("pending prep acks"+pendingPrepareAcks);
            GeneralServer.pendingPrepareAcks.get(messageId).put(yourPort,true);
            logger.responseLogger("after");
        }
    }
    catch(Exception e)
    {
        logger.errorLogger("Error in acknowledge is "+e.getMessage());
    }
        //logger.responseLogger("Ack received from: " + yourPort);
   }
   
   public void go(UUID messageId, int callBackServer) throws RemoteException{
       
       entry = GeneralServer.pendingChanges.get(messageId);
       
       if (entry == null)
       {
           throw new IllegalArgumentException("The message is not in the storage");
       }
       
       Operation(entry.toString());
    //    GeneralServer.pendingChanges.remove(messageId);
       helper.sendAck(messageId, callBackServer, AckType.acknowledgeGo);
   }

   public void setServersInfo(int[] otherServersPorts, int yourPort)
           throws RemoteException {

        // logger.responseLogger("In server with port "+yourPort);
        // for(int port: otherServersPorts)
        // {
        //     System.out.println("Other ports port no is "+port);
        // }
       
       this.otherServers = otherServersPorts;
       this.port = yourPort;
       this.helper.setPort(yourPort);
       this.helper.setLogger(logger);
   }

   public void prepareKeyValue(UUID messageId, Entry entry, int callBackServer) throws RemoteException{

        logger.responseLogger("In prepareKeyvalue");

        if (GeneralServer.pendingChanges.containsKey(messageId)){
            helper.sendAck(messageId, callBackServer, AckType.acknowledgePrep);
        }

        GeneralServer.pendingChanges.put(messageId, entry);
        helper.sendAck(messageId, callBackServer, AckType.acknowledgePrep);
    }
   
   public int getPort() throws RemoteException{
       
       return this.port;
   }

    
}