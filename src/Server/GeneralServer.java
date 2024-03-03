package Server;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public abstract class GeneralServer {

    MapUtils mapUtils;
    int port;

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
   */
   static void responseLog(String str) {
    System.out.println(getTimeStamp() +
        " Response -> " + str + "\n");
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

      // System.out.println(Arrays.toString(input));
      // System.out.println(input[0]);
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
        default:
          throw new IllegalArgumentException();
      }
    } catch (Exception e) {
      return "BAD REQUEST!:  Please view README.md to check available operations." + e;
    }

  }


    abstract void Server(int PORT);
} 
