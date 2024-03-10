package Client;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Scanner;
import java.util.zip.CRC32;
import java.util.zip.Checksum;


/**
 * UDPClient is a simple implementation of a UDP client in Java.
 *
 * It sends UDP packets to UDP server and receives UDP packets from it.
 * It sends client requests to server, and receives back responses from the same.
 */
public class UDPClient extends GeneralClient{


    /**
     * Constructor for UDPClient class.
     *
     */
    public UDPClient()
    {
        mapUtils = new MapUtils(new Scanner(System.in));
        logger = new Logger();
     
    }

    /**
     * UDP implementation of Client method which sets up a UDP client connection to the server.
     * 
     * @param serverIP IP address of the server with which UDP client wants to establish a connection.
     * @param serverPort portnumber on the server with which UDP client wants to establish a connection.
     * 
     * @throws IoException incase of an invalid operation or failure in IO operation.
     */
    void Client(InetAddress serverIP, int serverPort) throws IOException {

    try (DatagramSocket datagramSocket = new DatagramSocket()) {
      datagramSocket.setSoTimeout(10000);
      String start = logger.getTimeStamp();
      System.out.println(start + " UDP Client started");

      prepopulateUDPRequests(serverPort);

      while (true) {

        String operation = mapUtils.getOperation();

        createrequest(operation);

        logger.requestLogger(request);

        sendpacket(datagramSocket, request, serverIP, serverPort);

      }

    } catch (UnknownHostException | SocketException e) {
      System.out.println(
          "Host or Port unknown error, try again!");
    }

  }

/**
   * Method to automate simulation of 5+ operation of PUT, GET and DELETE each.
   * 
   * @param port the port on which UDP connection is set up.

   * @throws IOException if an error occurs during writing or reading
   */

  private void prepopulateUDPRequests(int port) throws IOException {
    DatagramSocket datagramSocket = new DatagramSocket();
    InetAddress serverIP = InetAddress.getLocalHost();
    System.out.println("Prepopulating UDP");
    for (int i = 0; i < 10; i++) {
      String request = "PUT " + i + " , " + i * 10;
      sendpacket(datagramSocket, request, serverIP, port);
    }

    for (int i = 0; i < 5; i++) {
      String request = "GET " + i;
      sendpacket(datagramSocket, request, serverIP, port);
    }

    for (int i = 0; i < 5; i++) {
      String request = "DELETE " + i;
      sendpacket(datagramSocket, request, serverIP, port);
    }
  }


  /**
   * Method to generate checksum for a given packet of data.
   * @param bytes byte array with data to be send to the server. Checksum is calculated for this data.
   * @return a long integer representing the checksum calculated.
   */
  private long generateChecksum(byte[] bytes)
  {
    Checksum crc32 = new CRC32();
    crc32.update(bytes, 0, bytes.length);
    return crc32.getValue();
  }

  
  /**
   * Method to generate the final data which is to be sent to server. 
   * This data includes the checksum along with the original data.
   * 
   * @param request original data to be sent.
   * @param checksum checksum for validating the original data.
   * @return a byte array including both original data and checksum which is ultimately sent to server for processing.
   */
  private byte[] generateData(byte[] request, long checksum)
  {
    byte[] data = new byte[request.length+8];
    byte [] cs = ByteBuffer.allocate(8).putLong(checksum).array();

    int i = 0;

    for(byte b : cs)
    {
        data[i] = b;
        i++;
    }

    for(byte b : request)
    {
        data[i] = b;
        i++;
    }

    return data;
  }

  
  /**
   * 
   * Method to send a UDP packet to UDP server.
   * 
   * @param datagramSocket socket used for UDP communication between client and server.
   * @param request the message(request) user has created to send to the server for processing.
   * @param serverIP IP address of UDP server.
   * @param port port number on which UDP server is listening.
   * @throws IOException if an error occurs during writing or reading.
   */
  private void sendpacket(DatagramSocket datagramSocket, String request, InetAddress serverIP, int port) throws IOException
  {
    byte[] requestBuffer = request.getBytes();

      if (requestBuffer.length > 65499) {
        System.out.println("Error: Request size exceeds the maximum allowed limit.");
      }

      long checksum = generateChecksum(requestBuffer);

      byte[] requestData = generateData(requestBuffer,checksum);



      DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length, serverIP, port);
      datagramSocket.send(requestPacket);

      byte[] resultBuffer = new byte[512];
        DatagramPacket resultPacket = new DatagramPacket(resultBuffer, resultBuffer.length);

      try {

        do
        {
          datagramSocket.receive(resultPacket);

          String result = new String(resultBuffer);
          logger.responseLogger(result);
        } while(resultBuffer[resultBuffer.length - 1] == 1);
        
        
      } catch (java.net.SocketTimeoutException e) {
        System.out.println("Timeout occurred. " +
            "The server did not respond within the specified time.");
            logger.errorLogger("Timeout occurred. " +
            "The server did not respond within the specified time.");
      } catch (IOException e) {
        logger.errorLogger(e.getMessage());
        e.printStackTrace();
      }

  }
}
