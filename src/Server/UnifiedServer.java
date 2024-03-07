package Server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import java.util.Properties;

/**
 * UDPServer is a simple implementation of a UDP server in Java.
 *
 * It listens for incoming UDP packets, processes client requests, and sends
 * back responses.
 */
public class UnifiedServer {
  static InputStream read;
  static OutputStream write;
  static Properties properties;

  /**
   * The main start point of the UDPServer program.
   *
   * @param args command line arguments
   * @throws SocketException exception
   */
  public static void main(String[] args) throws Exception {

    // System.out.print("Enter a port Number: ");
    int tcpPort = Integer.parseInt(args[0]);
    int udpPort = Integer.parseInt(args[1]);
    if (udpPort > 65535 || tcpPort > 65535) {
      throw new IllegalArgumentException("Invalid input!"
          + "Please provide a valid IP address and Port number.");
    }

    try {
      // Start TCP server thread
      Thread tcpThread = new Thread(() -> {
        TCPServer tcp = new TCPServer(tcpPort);
        tcp.Server(tcpPort);
      });
      tcpThread.start();

      // Start UDP server thread
      Thread udpThread = new Thread(() -> {
        UDPServer udp = new UDPServer(udpPort);
        udp.Server(udpPort);
      });
      udpThread.start();

    } catch (NumberFormatException e) {
      e.printStackTrace();
    }
  }

}