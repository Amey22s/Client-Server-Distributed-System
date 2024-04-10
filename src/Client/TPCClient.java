package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

import Utils.IRPC;

public class TPCClient extends GeneralClient{

    static IRPC[] stubs;
    static Registry[] registries;

	public static void main(String[] args) throws Exception 
	{	
    	Helper hl = new Helper();
        // InetAddress serverIP = InetAddress.getByName(args[0]);
		hl.ClientParseArgs(args);
		stubs = new IRPC[5];
		registries = new Registry[5];

	try 
    {
		for (int i = 0 ; i < hl.serverPorts.length ; i++)
        {
            registries[i] = LocateRegistry.getRegistry("localhost",hl.serverPorts[i]);
            stubs[i] = (IRPC) registries[i].lookup("kv");  
        }


        
		
		while(true)
        {
            System.out.print("Please enter machine number, function and values:");
            System.out.print("\nWe have 5 machines, you should choose between 0-4" + "\n");
            System.out.print("If it is a PUT, the input format is: <SERVER No.>:<PUT>,<KEY>,<VALUE>" +  "\n" );
            System.out.print("If it is a GET or DEL, the format is: <SERVER No.>:<GET/DEL>,<KEY>" + "\n");
            System.out.print("Example  0 PUT 100 188 or 0 DEL 100"+ "\n");
            String input = GetStringFromTerminal();
            
            String[] formattedInput = input.split(":");
            int serverNo = Integer.parseInt(formattedInput[0]);
            String operation = formattedInput[1];

            hl.log("Successfully performed operation: \n"+stubs[serverNo].TPCPreprocess(UUID.randomUUID(), operation));

        }
		 
	   
	    
	} 
    catch (Exception e) 
    {
        hl.log("This is the error "+e);
	} 
    finally
    {
        for (int i = 0; i < hl.serverPorts.length ; i++)
        {
        System.exit(hl.serverPorts[i]);
        }
    }
	}  
	
public static String GetStringFromTerminal() throws IOException 
{
				BufferedReader stringIn = new BufferedReader (new InputStreamReader(System.in));
				return  stringIn.readLine();
			}

@Override
void Client(InetAddress serverIP, int serverPort) throws IOException {

        String operation = mapUtils.getOperation();
        createrequest(operation);
        logger.requestLogger(request);


        // Send request packet to the server
        // String response = store.Operation(request);

        // Receive response packet from the server
        // String response = receivePacket(dataInputStream);

        // if (response.startsWith("ERROR")) {
        //   // System.out.println("Received error response from the server: " + response);
        //   logger.errorLogger("Received error response from the server: " + response);
        // } else {
        //   logger.responseLogger(response);
        // }

}
}