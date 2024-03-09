package Server;

/**
 * UDPDriver is a simple implementation of a driver program in Java 
 * to create a UDP Server attached to port number provided by the user.
 */
public class UDPDriver {

  /**
   * Entrypoint point of the program for UDP Server.
   *
   * @param args command line arguments
   * @throws Exception if an error occurs during initialization.
   */
  public static void main(String[] args) throws Exception {


    int udpPortNo = Integer.parseInt(args[0]);
    if (udpPortNo > 65535) {
      throw new IllegalArgumentException("Invalid input!" 
          + "Please provide a valid input as mentioned in the README file.");
    }

    UDPServer udp = new UDPServer(udpPortNo);
    udp.Server(udpPortNo);
  }

}