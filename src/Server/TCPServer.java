package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends GeneralServer{

    public TCPServer(int port)
    {
      try{
        this.port = port;
        this.mapUtils = new MapUtils("Server/map.properties");
      }
      catch(Exception e)
      {
        errorLog(e.getMessage());
      }
    }

    void Server(int TCPPORT) {
    try (ServerSocket serverSocket = new ServerSocket(TCPPORT)) {
      String start = getTimeStamp();
      System.out.println(start + " TCP Server started on port: " + TCPPORT);
      Socket clientSocket = serverSocket.accept();
      DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
      DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());


      while (true) {
        String input = dataInputStream.readUTF();
        requestLog(input, String.valueOf(clientSocket.getInetAddress()),
            String.valueOf(clientSocket.getPort()));

        String result = performOperation(input.split(" "));
        responseLog(result);
        dataOutputStream.writeUTF(result);
        dataOutputStream.flush();
      }

    } catch (Exception e) {
      System.out.println(getTimeStamp() + " Error: " + e);
    }
  }
    
}
