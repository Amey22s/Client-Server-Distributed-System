package Server;


/**
 * TCPDriver is a simple implementation of a driver program in Java 
 * to create a TCP Server attached to port number provided by the user.
 */
public class TCPDriver {

  /**
   * Entrypoint point of the program for TCP Server.
   *
   * @param args command line arguments to be taken as input from user.
   * @throws Exception if an error occurs during initialization.
   */
  public static void main(String[] args) throws Exception {


    int tcpPortNo = Integer.parseInt(args[0]);
    if (tcpPortNo > 65535) {
      throw new IllegalArgumentException("Invalid input!" 
          + "Please provide a valid input as mentioned in the README file.");
    }

    TCPServer tcp = new TCPServer(tcpPortNo);
    tcp.Server(tcpPortNo);
  }

}