package Client;

import java.io.IOException;
import java.net.InetAddress;


public abstract class GeneralClient {

    MapUtils mapUtils;
    Logger logger;
    String request;   
  

  void createrequest(String operation) throws IOException
  {

    // System.out.println("operation is "+operation);
    switch (operation) {
        case "1":
            request = "PUT " + mapUtils.getKey() + " , " + mapUtils.getValue();
            break;
        case "2":
            request = "GET " + mapUtils.getKey();
            break;
        case "3":
            request = "DELETE " + mapUtils.getKey();
            break;
        case "4":
            request = "GET-ALL pairs";
            break;
        case "5":
            request = "DELETE-ALL pairs";
            break;
        // case "6":
        //     System.out.println("Shutting down client...");
        //     System.exit(0);
        default:
            System.out.println("Please choose a valid operation!");
            logger.errorLogger("Please choose a valid operation!");
            break;
    }
  }



    abstract void Client(InetAddress serverIP, int serverPort) throws IOException;
    
}
