package Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import Utils.IRPC;

/**
 * RPCServer
 */
public class RPCServer extends GeneralServer implements IRPC{

    private String keyStore;
    public RPCServer(String keyStore)
    {
        try{
            this.keyStore = keyStore;
            this.mapUtils = new MapUtils("Server/map.properties");
            this.logger = new Logger();

            // String start = logger.getTimeStamp();
            // System.out.println(start + "\nServer is started ...");
          }
          catch(Exception e)
          {
            logger.errorLogger(e.getMessage());
          }
    }


    public String Operation(String input) throws RemoteException {
        String[] tokens = input.split(" ");
        return performOperation(tokens);
    }

    void Server(int portNo) {

        try {
        IRPC stub = (IRPC) UnicastRemoteObject.exportObject(this, portNo);
        Registry registry = LocateRegistry.getRegistry(); 
        
        registry.bind(keyStore, stub); 
        logger.responseLogger("Server started...");
        }
        catch(Exception e)
        {
            logger.errorLogger(e.getMessage());
        }
            
        // throw new UnsupportedOperationException("Unimplemented method 'Server' for RPC.");
    }

    
}