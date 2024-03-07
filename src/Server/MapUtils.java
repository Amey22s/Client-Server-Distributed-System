package Server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MapUtils {

    private  Properties properties;
    private OutputStream write;
    private InputStream read;

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
   * Add the key-value pair to the map.
   *
   * @param key   the key
   * @param value the value
   * @return a message indicating the success of the operation
   * @throws Exception if there is an error adding to the map
   */
   protected String addToMap(String key, String value) throws Exception {
    properties.setProperty(key, value);
    properties.store(write, null);
    String result = "Inserted key \"" + key + "\" with value \"" + value + "\"";
    return result;
  }

  /**
   * Delete the key from the map.
   *
   * @param key the key to delete
   * @return a message indicating the success of the operation
   * @throws IOException if there is an error deleting from the map
   */
   protected String deleteFromMap(String key) throws IOException {
    String result = "";
    if (properties.containsKey(key)) {
      properties.remove(key);
      properties.store(write, null);
      result = "Deleted key \"" + key + "\"" + " successfully!";
    } else {
      result = "Key not found.";
    }
    return result;
  }

  /**
   * Get the value associated with the key from the map.
   *
   * @param key the key to retrieve the value for
   * @return the value associated with the key or a message indicating that the
   *         key was not found
   * @throws IOException if there is an error retrieving from the map
   */
   protected String getFromMap(String key) throws IOException {
    String value = properties.getProperty(key);
    String result = value == null ? "No value found for key \"" + key + "\""
        : "Key: \"" + key + "\" ,Value: \"" + value + "\"";
    return result;
  }


  /**
   * @return
   * @throws IOException 
   */
  protected String getAll() throws IOException
  {
    StringBuilder sb = new StringBuilder();

    for(Object key : properties.keySet())
    {
      sb.append(getFromMap(key.toString())+"\n");
    }

    return sb.toString().length() > 0 ? sb.toString() : "No data in key value store to fetch!";
  }


  /**
   * @param key
   * @return
   * @throws IOException
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
