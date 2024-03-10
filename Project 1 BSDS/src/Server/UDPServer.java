package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * UDPServer is a simple implementation of a UDP server in Java.
 *
 * It listens for incoming UDP packets, processes client requests, and sends
 * back responses.
 */

public class UDPServer extends GeneralServer{

  /**
   * Constructor for the UDPServer class.
   * 
   * @param port The port number on which the server will listen for UDP packets.
   */
  public UDPServer(int portNo)
  {
    try
    {
      this.port = portNo;
      this.mapUtils = new MapUtils("Server/map.properties");  
      this.logger  = new Logger(); 
    }
    catch(Exception e)
    {
      logger.errorLogger(e.getMessage());
    }
  }

  /**
   * UDP server implementation of abstract function Server.
   * 
   * @param udpPortNo The port number on which the server will listen for incoming UDP packets.
   */
  void Server(int udpPortNo) {
    try (DatagramSocket datagramSocket = new DatagramSocket(udpPortNo)) {

      String start = logger.getTimeStamp();
      System.out.println(start + "\nUDP Server is started on port number " + udpPortNo+".");
      byte[] serverResponseBuffer = new byte[512];
      byte[] serverRequestBuffer = new byte[65499];
      
     

      while (true) {
        DatagramPacket packetReceived = new DatagramPacket(serverRequestBuffer, serverRequestBuffer.length);
        datagramSocket.receive(packetReceived);
        InetAddress clientIPAddress = packetReceived.getAddress();
        int clientPortNo = packetReceived.getPort();


        byte[] payload = extractPayload(packetReceived);
        byte[] checksum = extractChecksum(packetReceived);


        long clientChecksum = bytesToLong(checksum);
        long serverChecksum = generateChecksum(payload,packetReceived.getLength());


        if(serverChecksum != clientChecksum)
        {
          logger.errorLogger("Malformed data packet!");
          continue;
        }

        String requestString = new String(payload, packetReceived.getOffset(), payload.length);
        logger.requestLogger(requestString, clientIPAddress.toString(), String.valueOf(clientPortNo));

        // Validate packet size
        // Max packet size is lesser than the usual for UDP as 8 bytes are used for checksum purpose.
        if (packetReceived.getLength() > 65499) {
          logger.errorLogger("Received packet exceeds maximum allowed size for a UDP packet.");
          continue;
        }

        
        try {
          String[] input = requestString.split(" ");

          if (input.length < 2) {
            throw new IllegalArgumentException("Malformed request from client!");
          }

          String result = performOperation(input);

          String[] pairs = result.split("\n");

          if(pairs.length == 0)
          {
            pairs[0] = result;
          }

          for(int i = 0; i < pairs.length - 1; i++)
          {
            
            logger.responseLogger(pairs[i].trim());
            byte[] preResponseBuffer = pairs[i].trim().getBytes();

            serverResponseBuffer = generateResponse(preResponseBuffer,serverResponseBuffer.length);

            serverResponseBuffer[serverResponseBuffer.length - 1] = 1;

            DatagramPacket packetSent = new DatagramPacket(serverResponseBuffer, serverResponseBuffer.length, clientIPAddress,
             clientPortNo);
            datagramSocket.send(packetSent);
            serverResponseBuffer = new byte[512];
            
          }

            
            logger.responseLogger(pairs[pairs.length - 1].trim());
            byte[] preResponseBuffer = pairs[pairs.length - 1].trim().getBytes();
            serverResponseBuffer = generateResponse(preResponseBuffer,serverResponseBuffer.length);


        } catch (IllegalArgumentException e) {
          String errorMessage = e.getMessage();
          logger.errorLogger(errorMessage);
          serverResponseBuffer = errorMessage.getBytes();
        }

        DatagramPacket responsePacket = new DatagramPacket(serverResponseBuffer, serverResponseBuffer.length,
            clientIPAddress, clientPortNo);
        datagramSocket.send(responsePacket);
        serverResponseBuffer = new byte[512];

      
    }
    } catch (Exception e) {
      logger.errorLogger("Error! Please make sure IP and Port are valid and try again."+e.getMessage());
    }
  
  }

  private long bytesToLong(byte[] bytes) {
    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
    buffer.put(bytes);
    buffer.flip();
    return buffer.getLong();
  }


  private byte[] extractChecksum(DatagramPacket packet)
  {

    byte[] data = packet.getData();
    byte[] cs = new byte[8];

    for(int i = 0; i < 8; i++)
    {
      cs[i] = data[i];
    }

    return cs;
  }

  private byte[] extractPayload(DatagramPacket packet)
  {
    int length = packet.getLength();
    byte[] data = packet.getData();
    byte[] payload = new byte[length - 8];

    int j = 0;

    for(int i = 8; i < length; i++)
    {
      payload[j] = data[i];
      j++;
    }

    return payload;
  }


  private long generateChecksum(byte[] bytes, int length)
  {
    Checksum crc32 = new CRC32();
    crc32.update(bytes, 0, length - 8);
    return crc32.getValue();
  }

  private byte[] generateResponse(byte[] preResponse, int responseBufferLength)
  {
    int i = 0;
    byte[] finalResponse = new byte[responseBufferLength];

    for(byte b : preResponse)
    {
        finalResponse[i] = b;
        i++;
    }

    return finalResponse;
  }
    
}
