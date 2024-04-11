package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.UUID;

import Utils.IRPC;

public class TPCClient extends GeneralClient{

    private IRPC[] stubs;
    private Registry[] registries;
    private String keystore;
    private int[] serverPorts;


	public TPCClient(String keyStoreName, int[] serverPorts)
	{	
        this.mapUtils = new MapUtils(new Scanner(System.in));
        this.logger = new Logger();
        this.keystore = keyStoreName;
        // InetAddress serverIP = InetAddress.getByName("localhost");
		this.serverPorts = serverPorts;
		stubs = new IRPC[5];
		registries = new Registry[5];
    }

    void Client(InetAddress serverIP, int serverPort) throws IOException {
    try {

        String start = logger.getTimeStamp();
        logger.responseLogger(start + " Client started...");
        for (int i = 0 ; i < serverPorts.length ; i++)
        {
            registries[i] = LocateRegistry.getRegistry("localhost",serverPorts[i]);
            stubs[i] = (IRPC) registries[i].lookup(keystore); 
            prepopulate2PCRequests(stubs[i]);
        }

        while (true) {
            Scanner sc = new Scanner(System.in);
            logger.requestLogger("Please mention the server you want to use to perform your action (Enter a number between 0-4):");
            logger.requestLogger("Enter server no.:");
            String serverNo = sc.next();
            int server = Integer.parseInt(serverNo);

            if(server > 4 || server < 0)
            {
                logger.errorLogger("Please ensure the server number is between 0-4.");
                continue;
            }

            String operation = mapUtils.getOperation();
            createrequest(operation);
            logger.requestLogger(request);
    
    
            // Send request packet to the server
            String response = stubs[server].TPCPreprocess(UUID.randomUUID(),request);
    
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
    private void prepopulate2PCRequests(IRPC store)  throws IOException 
    {
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