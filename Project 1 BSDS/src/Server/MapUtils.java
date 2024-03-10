package Server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.Properties;


/**
 * This class provides utility methods for working with a key-value map stored in a properties file.
 */
public class MapUtils {

    private  Properties properties;
    private OutputStream write;
    // private InputStream read;


    /**
     * Constructor for the MapUtils class.
     * 
     * Initializes the `properties` object and attempts to load data from the specified properties file.
     * If loading fails, an exception is thrown.
     * 
     * @param propertiesFile The path to the properties file used for storing the key-value map.
     * @throws Exception If there is an error loading the properties file.
     */
    public MapUtils(String propertiesFile) throws Exception
    {
        try {
        // read = new FileInputStream(propertiesFile);
        properties = new Properties();
        properties.clear();
        // properties.load(read);
        write = new FileOutputStream(propertiesFile);
        properties.store(write, null);
        }
        catch(Exception e)
        {
            throw new Exception("Something went wrong while loading "+propertiesFile+". Please check the file and try again!");
        }
    }


  /**
   * Adds a key-value pair to the map stored in the properties file.
   * 
   * @param key The unique key for the data.
   * @param value The data associated with the key.
   * @return A message indicating the success of the operation.
   * @throws Exception If there is an error adding the key-value pair.
   */

   protected String addToMap(String key, String value) throws Exception {
    properties.setProperty(key, value);
    properties.store(write, null);
    String result = "Key = \"" + key + "\" with value = \"" + value + "\" inserted successfully!";
    return result;
  }

  /**
   * Deletes a key-value pair from the map stored in the properties file.
   * 
   * @param key The key of the data to be removed.
   * @return A message indicating the success of the operation.
   * @throws IOException If there is an error during deletion.
   */

   protected String deleteFromMap(String key) throws IOException {
    String result = "";
    if (properties.containsKey(key)) {
      properties.remove(key);
      properties.store(write, null);
      result = "Deleted key \"" + key + "\"" + " !";
    } else {
      result = "Key not found.";
    }
    return result;
  }

  /**
   * Retrieves the value associated with a key from the map stored in the properties file.
   * 
   * @param key The key to search for.
   * @return The value associated with the key, or a message indicating the key wasn't found.
   * @throws IOException If there is an error during retrieval.
   */

   protected String getFromMap(String key) throws IOException {
    String value = properties.getProperty(key);
    String result = value == null ? "Value not found for key = \"" + key + "\""
        : "Key = \"" + key + "\" ,Value = \"" + value + "\"";
    return result;
  }


  /**
   * Retrieves all key-value pairs from the map stored in the properties file.
   * 
   * @return A string representation of all key-value pairs, or a message indicating the map is empty.
   * @throws IOException If there is an error during retrieval.
   */

  protected String getAll() throws IOException
  {
    StringBuilder sb = new StringBuilder();

    for(Object key : properties.keySet())
    {
      sb.append("\n"+getFromMap(key.toString()));
    }
    // sb.append("\n");

    return sb.toString().length() > 0 ? sb.toString() : "No data in key value store to fetch!";
  }


  /**
   * Deletes all key-value pairs from the map stored in the properties file.
   * 
   * @return A message indicating the success of the operation.
   * @throws IOException If there is an error during deletion.
   */
  
  protected String deleteAll() throws IOException {
    String result = "";
    properties.clear();
    if (properties.size() == 0) {
      result = "Deleted all keys successfully!";
    } else {
      result = "Error deleting all keys!";
    }
    return result;
  }

    
}
