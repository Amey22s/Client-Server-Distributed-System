package Client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
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

public class UDPClient extends GeneralClient{

    public UDPClient() throws IOException
    {
        mapUtils = new MapUtils(new Scanner(System.in));
     
    }

    /**
     * UDP implementation of Client method which sets up a UDP client connection to the server.
     * 
     * @param serverIP IP address of the server with which UDP client wants to establish a connection.
     * @param serverPort portnumber on the server with which UDP client wants to establish a connection.
     * 
     * @throws IoException 
     */
    void Client(InetAddress serverIP, int serverPort) throws IOException {

    try (DatagramSocket datagramSocket = new DatagramSocket()) {
      datagramSocket.setSoTimeout(10000);
      String start = getTimeStamp();
      System.out.println(start + " Client started");

      // prepopulate
      prepopulateUDPRequests(serverPort);

      while (true) {

        String operation = mapUtils.getOperation();

        createrequest(operation);

        requestLog(request);

        sendpacket(datagramSocket, request, serverIP, serverPort);

        // byte[] requestBuffer = request.getBytes();

        // /**
        //  * Max allowed bytes in a UDP packet is 65507. 
        //  * We are reserving first 8 bytes of datapacket for checksum to validate payload data.
        //  * So the max allowed bytes in our UDP packet will be 65499.
         
        //  */ 
 
        // if (requestBuffer.length > 65499) {
        //   System.out.println("Error: Request size exceeds the maximum allowed limit.");
        //   continue;
        // }

        // long checksum = generateChecksum(requestBuffer);

        // byte[] requestData = generateData(requestBuffer,checksum);


        // DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length,
        //     serverIP, serverPort);
        // datagramSocket.send(requestPacket);

        // byte[] resultBuffer = new byte[512];
        // DatagramPacket resultPacket = new DatagramPacket(resultBuffer, resultBuffer.length);

        // try {

        //   do
        //   {
        //     datagramSocket.receive(resultPacket);
        //     String result = new String(resultBuffer);
        //     responseLog(result);
        //   } while(resultBuffer[resultBuffer.length - 1] == 0b11111111);
         
          
        // } catch (java.net.SocketTimeoutException e) {
        //   System.out.println("Timeout occurred. " +
        //       "The server did not respond within the specified time.");
        // } catch (IOException e) {
        //   e.printStackTrace();
        // }
      }

    } catch (UnknownHostException | SocketException e) {
      System.out.println(
          "Host or Port unknown error, try again!");
    }

  }

/**
   * Helper method to automate simulation of 5+ operation of PUT, GET and DELETE each.
   * 
   * @param port the port on which UDP connection is set up.

   * @throws IOException if an error occurs during writing or reading
   */

  private void prepopulateUDPRequests(int port) throws IOException {
    DatagramSocket datagramSocket = new DatagramSocket();
    InetAddress serverIP = InetAddress.getLocalHost();
    System.out.println("Prepopulating UDP");
    for (int i = 0; i < 5000; i++) {
      String request = "PUT " + i + " , " + i * 100;
      sendpacket(datagramSocket, request, serverIP, port);
    }

    // datagramSocket.close();
  }


  private long generateChecksum(byte[] bytes)
  {
    Checksum crc32 = new CRC32();
    crc32.update(bytes, 0, bytes.length);
    return crc32.getValue();
  }

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

          // System.out.println("Last byte in result buffer is "+resultBuffer[resultBuffer.length - 1]);
          // System.out.println("Result Byte array in client is "+resultBuffer.length);
          // resultBuffer[resultBuffer.length - 1] = 0;
          String result = new String(resultBuffer);
          responseLog(result);
        } while(resultBuffer[resultBuffer.length - 1] == 1);
        
        
      } catch (java.net.SocketTimeoutException e) {
        System.out.println("Timeout occurred. " +
            "The server did not respond within the specified time.");
      } catch (IOException e) {
        e.printStackTrace();
      }

  }
}
