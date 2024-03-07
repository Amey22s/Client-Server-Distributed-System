package Client;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class GeneralClient {

    MapUtils mapUtils;
    String request;

    FileWriter myWriter;
   


  /**
   * Helper method to print Request messages.
   *
   * @param str message string
   */
   void requestLog(String str) {
    System.out.println(getTimeStamp() +
        " Request -> " + str);
  }

  /**
   * Helper method to print Response messages.
   *
   * @param str message string
 * @throws IOException 
   */
  void responseLog(String str) throws IOException {

    String val = getTimeStamp() + " Response -> " + str + "\n";
    System.out.println(val);

    myWriter.write(val);
  }

  /**
   * Helper method to return the current timestamp.
   *
   * @return the current timestamp
   */
   String getTimeStamp() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
    return "[Time: " + simpleDateFormat.format(new Date()) + "]";
  }
  

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
            request = "GET-ALL";
            break;
        case "5":
            request = "DELETE-ALL";
            break;
        // case "6":
        //     System.out.println("Shutting down client...");
        //     System.exit(0);
        default:
            System.out.println("Please choose a valid operation!");
            break;
    }
  }



    abstract void Client(InetAddress serverIP, int serverPort) throws IOException;
    
}
