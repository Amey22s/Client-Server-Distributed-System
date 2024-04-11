package Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class TPCDriver {

    public static int[] serverPorts = new int[5];

/**
   * The main start point of the TPCDriver program.
   *
   * @param args command line arguments containing port numbers and key value store name.
   * @throws IOException if an I/O error occurs.
   */
    public static void main(String[] args) throws UnknownHostException, IOException
    {
        try
        {
            if(args[0].equalsIgnoreCase("Quit"))
                System.exit(serverPorts[0]);
            else
            {
                if (args.length != 6) 
                {
                    String message = "You specified just " + args.length + " arguments.\n" + "Please specify 5 ports and 1 key value store.";
                    throw new IllegalArgumentException(message);
                    }
                for (int i = 0 ; i < args.length - 1; i++)
                {
                    serverPorts[i] = Integer.parseInt(args[i]);
                }
            }

            TPCClient tpcClient = new TPCClient(args[args.length-1], serverPorts);
            tpcClient.Client(InetAddress.getByName("localhost"), 0);
        }
        catch(Exception e)
        {
            System.out.println("Something went wrong. Error is "+e.getMessage());
        }
    }
    
}
