package Client;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class provides utility methods for working with a key-value map stored in a properties file.
 */
public class MapUtils {

    private String key, value, operation;
    private Scanner scanner;


    /**
     * Constructor for the MapUtils class.
     * 
     * @param sc scanner object passed on by unified client to initialize the scanner object
     *           required to take in input from user.
     */
    public MapUtils(Scanner sc)
    {
        this.scanner = sc;
    }



  /**
   * Gets the operation to be performed from the user via the console input.
   *
   * @throws IOException if an error occurs during input reading
   */
  protected String getOperation() throws IOException {
    System.out.println("---------------------------------------");
    System.out.print("Operations: \n1. PUT\n2. GET\n3. DELETE\n4. GET-ALL\n5. DELETE-ALL\nChoose operation number: ");
    operation = scanner.nextLine().trim();

    return operation;
  }

  /**
   * Gets the key from the user via the console input.
   *
   * @throws IOException if an error occurs during input reading
   */
    protected String getKey() throws IOException {
    System.out.print("Enter key: ");
    this.key = scanner.nextLine();

    return key;
  }

  /**
   * Gets the value from the user via the console input.
   *
   * @throws IOException if an error occurs during input reading
   */
    protected String getValue() throws IOException {
    System.out.print("Enter Value: ");
    this.value = scanner.nextLine();

    return value;
  }

    
}
