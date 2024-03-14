package Client;

import java.net.InetAddress;

public class RPCClientDriver {
    public static void main(String[] args)
    {
        try {
            if (args.length != 1) {
                throw new IllegalArgumentException("Invalid argument! " +
                    "Please provide valid input parameters and start again");
                }
    
            String keyStore = args[0];

            RPCClient rpc = new RPCClient(keyStore);
            rpc.Client(InetAddress.getByName("localhost"), 0);

    }
    catch(Exception e)
    {
        System.out.println("Client error at "+e.getMessage());
    }
}
}
