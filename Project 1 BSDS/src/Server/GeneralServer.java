package Server;

/**
 * An abstract class GeneralServer is used to abstract out code common to both TCP and UDP server.
 * An abstract function Server is decaled which has different implementation defined in respective classes.
 * It also declares obbjects from helper classes like MapUtils and Logger which are initialized in respective classes.
 */

public abstract class GeneralServer {

    MapUtils mapUtils;
    Logger logger;
    int port;


  /**
   * Method to process user request.
   * 
   * @param tokens user request specifying the operation to be performed.
   * @return result of the operation, either PUT, GET, DELETE, GET-ALL or DELETE-ALL.
   * @throws IllegalArgumentException in case a invalid input is provided by user.
   */
    String performOperation(String[] tokens) throws IllegalArgumentException {
    try {

      String op = tokens[0].toUpperCase();
      String key = "";
      int j = 0;
      for (int i = 1; i < tokens.length; i++) {
        if (tokens[i].equals(",")) {
          j = i;
          break;
        } else
          key = key + tokens[i] + " ";
      }
      key = key.trim();

      switch (op) {
        case "PUT": {
          String value = "";
          for (int i = j + 1; i < tokens.length; i++)
            value = value + " " + tokens[i].trim();
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
          throw new IllegalArgumentException("Please provide a valid operation.");
      }
    } catch (Exception e) {
      return "BAD REQUEST!:  Please view README.md to check available operations." + e.getMessage();
    }

  }


    abstract void Server(int PORT);
} 
