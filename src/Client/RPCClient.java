package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import Utils.IRPC;

public class RPCClient extends GeneralClient{

    private String keyStore;
    /**
     * Constructor for the TCPClient class.
     * 
     */
    public RPCClient(String keyStoreName)
    {
        mapUtils = new MapUtils(new Scanner(System.in));
        logger = new Logger();
        keyStore = keyStoreName;
    }

    void Client(InetAddress serverIP, int serverPort) throws IOException {
        try {

            String start = logger.getTimeStamp();
            System.out.println(start + " Client started...");

            Registry registry = LocateRegistry.getRegistry(null); 
            IRPC store = (IRPC) registry.lookup(keyStore);

            prepopulateRPCRequests(store);

            while (true) {
                String operation = mapUtils.getOperation();
                createrequest(operation);
                logger.requestLogger(request);
        
        
                // Send request packet to the server
                String response = store.Operation(request);
        
                // Receive response packet from the server
                // String response = receivePacket(dataInputStream);
        
                if (response.startsWith("ERROR")) {
                  // System.out.println("Received error response from the server: " + response);
                  logger.errorLogger("Received error response from the server: " + response);
                } else {
                  logger.responseLogger(response);
                }
              }

        } catch (MalformedURLException e) {
            logger.errorLogger(e.getMessage());
        } catch (RemoteException e) {
            logger.errorLogger(e.getMessage());
        } catch (NotBoundException e) {
            logger.errorLogger(e.getMessage());
        }
    }


  /**
   * Method to automate simulation of 5+ operation of PUT, GET and DELETE each.
   * 
   * @param store the stub fetched from the RMI registry
   * @throws IOException if an error occurs during writing or reading
   */

    private void prepopulateRPCRequests(IRPC store) throws IOException {
      for (int i = 0; i < 10; i++) {
        String request = "PUT " + i + " , " + i * 10;
        String response = store.Operation(request);
        if (response.startsWith("ERROR")) {
          // System.out.println("Received error response from the server: " + response);
          logger.errorLogger("Received error response from the server: " + response);
        } else {
          logger.responseLogger(response);
        }
      }

      for (int i = 0; i < 5; i++) {
        String request = "GET " + i;
        String response = store.Operation(request);
        if (response.startsWith("ERROR")) {
          // System.out.println("Received error response from the server: " + response);
          logger.errorLogger("Received error response from the server: " + response);
        } else {
          logger.responseLogger(response);
        }
      }

      for (int i = 0; i < 5; i++) {
        String request = "DELETE " + i;
        String response = store.Operation(request);
        if (response.startsWith("ERROR")) {
          // System.out.println("Received error response from the server: " + response);
          logger.errorLogger("Received error response from the server: " + response);
        } else {
          logger.responseLogger(response);
        }
      }

  }

}
