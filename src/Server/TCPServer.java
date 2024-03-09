package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * TCPServer is a simple implementation of a TCP server in Java.
 *
 * It listens for incoming TCP connections, processes client requests, and sends
 * back responses.
 */

public class TCPServer extends GeneralServer{

    /**
     * Constructor for the TCPServer class.
     * 
     * @param port The port number on which the server will listen for TCP connections.
     */
    public TCPServer(int portNo)
    {
      try{
        this.port = portNo;
        this.mapUtils = new MapUtils("Server/map.properties");
        this.logger = new Logger();
      }
      catch(Exception e)
      {
        logger.errorLogger(e.getMessage());
      }
    }


    /**
     * TCP server implementation of abstract function Server.
     * 
     * @param tcpPortNo The port number on which the server will listen for incoming TCP connections.
     */
    void Server(int tcpPortNo) {
    try (ServerSocket tcpServerSocket = new ServerSocket(tcpPortNo)) {
      String start = logger.getTimeStamp();
      System.out.println(start + "\nTCP Server is started on port number " + tcpPortNo + ".");


      Socket tcpClientSocket = tcpServerSocket.accept();
      DataInputStream inputStream = new DataInputStream(tcpClientSocket.getInputStream());
      DataOutputStream outputStream = new DataOutputStream(tcpClientSocket.getOutputStream());


      while (true) {
        String input = inputStream.readUTF();
        logger.requestLogger(input, String.valueOf(tcpClientSocket.getInetAddress()), String.valueOf(tcpClientSocket.getPort()));

        String[] tokens = input.split(" ");
        String output = performOperation(tokens);
        logger.responseLogger(output);
        outputStream.writeUTF(output);
        outputStream.flush();
      }

    } catch (Exception e) {
      String errorTimestamp = logger.getTimeStamp();
      System.out.println(errorTimestamp + "\nError: " + e.getMessage());
      logger.errorLogger(e.getMessage());
    }
  }
    
}
