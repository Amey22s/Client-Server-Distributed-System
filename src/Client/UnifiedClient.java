package Client;

import java.io.IOException;
import java.net.InetAddress;

/**
 * UDPClient is a simple implementation of a UDP client in Java.
 * <p>
 * It sends requests to a UDP server, receives responses, and handles
 * exceptions.
 */
public class UnifiedClient {

  /**
   * The main start point of the UDPClient program.
   *
   * @param args command line arguments containing server IP and port.
   * @throws IOException if an I/O error occurs.
   */
  public static void main(String[] args) throws IOException {
    if (args.length < 3 || args.length > 4 || Integer.parseInt(args[1]) > 65535) {
      throw new IllegalArgumentException("Invalid argument! " +
          "Please provide valid IP and Port number and start again");
    }
    
    InetAddress serverIP = InetAddress.getByName(args[0]);
    int serverPort = Integer.parseInt(args[1]);
    String protocol = args[2];
    if (protocol.toLowerCase().equals("udp"))
    {
      UDPClient udp = new UDPClient();
      udp.Client(serverIP, serverPort);
    }
    else if (protocol.toLowerCase().equals("tcp"))
    {
      TCPClient tcp = new TCPClient();
      tcp.Client(serverIP, serverPort);
    }
    else if (protocol.toLowerCase().equals("rpc"))
    {
      String keyStore = args[3];
      RPCClient rpc = new RPCClient(keyStore);
      rpc.Client(serverIP, serverPort);
    }
    else {
      throw new IllegalArgumentException("Invalid argument! " +
          "Please provide valid protocol tcp or udp and start again");
    }

  }

}
