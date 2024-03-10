package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * TCPClient is a simple implementation of a TCP client in Java.
 *
 * It sets up TCP connection with TCP server, sends client requests to the server,
 *  and receives back responses from server.
 */
public class TCPClient extends GeneralClient {

    /**
     * Constructor for the TCPClient class.
     * 
     */
    public TCPClient()
    {
        mapUtils = new MapUtils(new Scanner(System.in));
        logger = new Logger();
    }

    /**
     * TCP implementation of Client method which sets up a TCP client connection to the server.
     * 
     * @param serverIP IP address of the server with which TCP client wants to establish a connection.
     * @param serverPort portnumber on the server with which TCP client wants to establish a connection.
     * 
     * @throws IoException if any error occurs.
     */
    void Client(InetAddress serverIP, int serverPort) throws IOException{
    try (Socket s = new Socket()) {
      int timeout = 10000;
      s.connect(new InetSocketAddress(serverIP, serverPort), timeout);

      DataInputStream dataInputStream = new DataInputStream(s.getInputStream());
      DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());
      String start = logger.getTimeStamp();
      System.out.println(start + " Client started on port: " + s.getPort());

      prepopulateTCPRequests(dataOutputStream, dataInputStream);

      while (true) {
        String operation = mapUtils.getOperation();
        createrequest(operation);
        logger.requestLogger(request);


        // Send request packet to the server
        sendPacket(dataOutputStream, request);

        // Receive response packet from the server
        String response = receivePacket(dataInputStream);

        if (response.startsWith("ERROR")) {
          // System.out.println("Received error response from the server: " + response);
          logger.errorLogger("Received error response from the server: " + response);
        } else {
          logger.responseLogger(response);
        }
      }
    } catch (UnknownHostException | SocketException e) {
      // System.out.println("Host or Port unknown, please provide a valid hostname and port number.");
      logger.errorLogger("Host or Port unknown, please provide a valid hostname and port number.");
    } catch (SocketTimeoutException e) {
      logger.errorLogger("Connection timed out. Please check the server availability and try again.");
      // System.out.println("Connection timed out. Please check the server availability and try again.");
    } catch (Exception e) {
      // System.out.println("Exception occurred!" + e);
      logger.errorLogger("Exception occurred!" + e);
    }
  }

  /**
   * Method to automate simulation of 5+ operation of PUT, GET and DELETE each.
   * 
   * @param dataOutputStream the output stream to write the packet
   * @param dataInputStream the input stream to read the packet
   * @throws IOException if an error occurs during writing or reading
   */

    private void prepopulateTCPRequests(DataOutputStream dataOutputStream, DataInputStream dataInputStream)
      throws IOException {
      for (int i = 0; i < 10; i++) {
        String request = "PUT " + i + " , " + i * 10;
        sendPacket(dataOutputStream, request);
        String response = receivePacket(dataInputStream);
        if (response.startsWith("ERROR")) {
          // System.out.println("Received error response from the server: " + response);
          logger.errorLogger("Received error response from the server: " + response);
        } else {
          logger.responseLogger(response);
        }
      }

      for (int i = 0; i < 5; i++) {
        String request = "GET " + i;
        sendPacket(dataOutputStream, request);
        String response = receivePacket(dataInputStream);
        if (response.startsWith("ERROR")) {
          // System.out.println("Received error response from the server: " + response);
          logger.errorLogger("Received error response from the server: " + response);
        } else {
          logger.responseLogger(response);
        }
      }

      for (int i = 0; i < 5; i++) {
        String request = "DELETE " + i;
        sendPacket(dataOutputStream, request);
        String response = receivePacket(dataInputStream);
        if (response.startsWith("ERROR")) {
          // System.out.println("Received error response from the server: " + response);
          logger.errorLogger("Received error response from the server: " + response);
        } else {
          logger.responseLogger(response);
        }
      }

  }



  /**
   * Method to send a packet to the server.
   *
   * @param outputStream the output stream to write the packet
   * @param packet       the packet to send
   * @throws IOException if an error occurs during writing
   */
  private void sendPacket(DataOutputStream outputStream, String packet) throws IOException {
    outputStream.writeUTF(packet);
    outputStream.flush();
    logger.requestLogger(packet);
  }

  /**
   * Method to receive a packet from the server.
   *
   * @param inputStream the input stream to read the packet
   * @return the received packet
   * @throws IOException if an error occurs during reading
   */
  private String receivePacket(DataInputStream inputStream) throws IOException {
    return inputStream.readUTF();
  }
    
}
