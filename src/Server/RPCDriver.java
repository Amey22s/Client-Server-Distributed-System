package Server;

public class RPCDriver {

    public static void main(String[] args)
    {
        try {

            int portNo = Integer.parseInt(args[0]);

            if (portNo > 65535) {
                throw new IllegalArgumentException("Invalid input!" 
                    + "Please provide a valid input as mentioned in the README file.");
              }

            String keyStore = args[1];
            RPCServer rpc = new RPCServer(keyStore);
            rpc.Server(portNo);
        }
        catch(Exception e)
        {
            System.out.println("Program exited with error: "+e.getMessage());
        }
    }
    
}
