package Server;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketPermission;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class UDPServer extends GeneralServer{



    public UDPServer(int port)
    {
      try
      {
        this.port = port;
        this.mapUtils = new MapUtils("Server/map.properties");
      }
      catch(Exception e)
      {
        errorLog(e.getMessage());
      }
    }

    void Server(int PORT) {
    try (DatagramSocket datagramSocket = new DatagramSocket(PORT)) {

      String start = getTimeStamp();
      System.out.println(start + " UDP Server started on port: " + PORT);
      byte[] requestBuffer = new byte[65499];
      byte[] responseBuffer;
     

      while (true) {
        DatagramPacket receivePacket = new DatagramPacket(requestBuffer, requestBuffer.length);
        datagramSocket.receive(receivePacket);
        InetAddress client = receivePacket.getAddress();
        int clientPort = receivePacket.getPort();


        byte[] payload = extractPayload(receivePacket);
        // System.out.println("Payload is "+new String(pl));

        byte[] checksum = extractChecksum(receivePacket);

        long clientChecksum = bytesToLong(checksum);

        // System.out.println("ClientCS "+clientChecksum);

        long serverChecksum = generateChecksum(payload,receivePacket.getLength());

        // System.out.println("ServerCS "+serverChecksum);

        if(serverChecksum != clientChecksum)
        {
          errorLog("Malformed data packet!");
          continue;
        }

        String request = new String(payload, receivePacket.getOffset(), payload.length);
        requestLog(request, client.toString(), String.valueOf(clientPort));

        // Validate packet size
        if (receivePacket.getLength() > 65499) {
          errorLog("Received packet exceeds maximum allowed size.");
          continue;
        }

        
        // System.out.print("request is "+request);

        try {
          String[] input = request.split(" ");
          if (input.length < 2) {
            throw new IllegalArgumentException("Malformed request!");
          }
          String result = performOperation(input);
          responseLog(result);
          responseBuffer = result.getBytes();

        } catch (IllegalArgumentException e) {
          String errorMessage = e.getMessage();
          errorLog(errorMessage);
          responseBuffer = errorMessage.getBytes();
        }

        DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length,
            client, clientPort);
        datagramSocket.send(responsePacket);
        requestBuffer = new byte[512];

      
    }
    } catch (Exception e) {
      errorLog("Error! Please make sure IP and Port are valid and try again."+e);
    }
  
  }

  private long bytesToLong(byte[] bytes) {
    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
    buffer.put(bytes);
    buffer.flip();//need flip 
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
    // System.out.println("crc32 object created "+bytes.length+" packet length "+length);
    crc32.update(bytes, 0, length - 8);
    return crc32.getValue();
  }
    
}
