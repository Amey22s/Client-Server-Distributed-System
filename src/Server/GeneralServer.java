package Server;


import java.util.Map;
import java.util.UUID;

/**
 * An abstract class GeneralServer is used to abstract out code common to both TCP and UDP server.
 * An abstract function Server is decaled which has different implementation defined in respective classes.
 * It also declares obbjects from helper classes like MapUtils and Logger which are initialized in respective classes.
 */

public abstract class GeneralServer {


    static int serverNo;
    MapUtils mapUtils;
    Logger logger;
    int port;
    Entry entry;
    String keyStore;
	  Map<UUID, Entry> pendingChanges;
	  Map<UUID,Map<Integer,Boolean>> pendingPrepareAcks;
	  Map<UUID,Map<Integer,Boolean>> pendingGoAcks;


  /**
   * Method to process user request.
   * 
   * @param tokens user request specifying the operation to be performed.
   * @return result of the operation, either PUT, GET, DELETE, GET-ALL or DELETE-ALL.
   * @throws IllegalArgumentException in case a invalid input is provided by user.
   */
    String performOperation(String[] tokens) throws IllegalArgumentException {
    try {

      if(tokens.length > 1)
        entry = createEntry(String.join(" ",tokens));
      

      String op = entry.getOp();
      String key = entry.getKey();
      String value = entry.getValue();

      switch (op) {
        case "PUT": {
          
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

/**
 * Creates an Entry object by parsing a given input string.
 *
 * @param input The string to be parsed.
 * @return A newly created Entry object containing the parsed information.
 */

  protected Entry createEntry(String input)
  {
    String[] tokens = input.split(" ");

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

    String value = "";
    for (int i = j + 1; i < tokens.length; i++)
    {
      value = value + " " + tokens[i].trim();
    }
    value = value.trim();

    // System.out.println("Key is "+key+" Value is "+value+" op is "+op);

    return new Entry(key, value, op);

  }


    abstract void Server(int PORT);
} 
