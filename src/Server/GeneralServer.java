package Server;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public abstract class GeneralServer {

    MapUtils mapUtils;
    int port;
    static FileWriter myWriter;

  /**
   * Helper method to return the current timestamp.
   *
   * @return the current timestamp
   */
   static String getTimeStamp() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
    return "<Time: " + simpleDateFormat.format(new Date()) + ">";
  }

  /**
   * Helper method to print Response messages.
   *
   * @param str message string
   * @throws IOException 
   */
   static void responseLog(String str) throws IOException {
    String val = getTimeStamp() + " Response -> " + str + "\n";
    System.out.println(val);

    myWriter.write(val);
  }

  /**
   * Helper method to print Error messages.
   *
   * @param err error message string
   */
   static void errorLog(String err) {
    System.out.println(getTimeStamp() +
        " Error -> " + err);
  }


/**
   * Helper method to print Request messages.
   *
   * @param str  message string
   * @param ip   client IP address
   * @param port client port number
   */
   static void requestLog(String str, String ip, String port) {
    System.out.println(getTimeStamp() + " Request from: " + ip + ":" + port + " -> " + str);
  }




  /**
   * Helper method to process user request
   * 
   * @param input user request
   * @return result of PUT/GET/DELETE operation
   * @throws IllegalArgumentException in case of invalid input
   */
    String performOperation(String[] input) throws IllegalArgumentException {
    try {

      String operation = input[0].toUpperCase();
      String key = "";
      int j = 0;
      for (int i = 1; i < input.length; i++) {
        if (Objects.equals(input[i], ",")) {
          j = i;
          break;
        } else
          key = key + input[i] + " ";
      }
      key = key.trim();

      switch (operation) {
        case "PUT": {
          String value = "";
          for (int i = j + 1; i < input.length; i++)
            value = value + " " + input[i].trim();
          value = value.trim();
          return mapUtils.addToMap(key, value);
        }
        case "DELETE": {
          return mapUtils.deleteFromMap(key);
        }
        case "GET": {
          return mapUtils.getFromMap(key);
        }
        case "GET-ALL": {
         return mapUtils.getAll();
        }
        case "DELETE-ALL": {
          return mapUtils.deleteAll();
        }
        default:
          throw new IllegalArgumentException();
      }
    } catch (Exception e) {
      return "BAD REQUEST!:  Please view README.md to check available operations." + e;
    }

  }


    abstract void Server(int PORT);
} 
