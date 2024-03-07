
# TCP/UDP Server and Client

The TCP Server and Client are two Java programs that enable communication between a server and a client using the TCP/IP protocol. The TCP Server listens for client connections on a specified port, while the TCP Client connects to the server's IP address and port. The server and client exchange data through TCP sockets, allowing for reliable and ordered transmission of messages.

## TCPServer

The TCPServer program represents the server-side of the TCP communication. It creates a server socket and listens for incoming client connections on a specified port. Once a client connects, the server can send and receive messages from the client.

### Usage

To use the TCPServer, follow these steps:

1. Compile the TCPServer.java file by navigating to the src folder and run the below command:

   ```
   javac Server/TCPServer.java
   ```

2. Run the compiled TCPServer class, providing the desired port number as a command-line argument:

   ```
   java Server/TCPServer <port>
   ```

3. Add the `<port>` number on which you want the server to listen for incoming connections.

4. The server will start and display a message indicating the IP address and port it is listening on. It will then wait for client connections.

## UDPServer

The UDP Server listens for client connections on a specified port, while the UDP Client connects to the server's IP address and port. The server and client exchange data through UDP Datagram sockets, allowing for faster transmission of messages.

### Usage

Follow the below steps carefully, to get the servers and client up and running:

1. Compile the UDPServer.java file by navigating to the src folder and run the below command:

   ```
   javac Server/UDPServer.java
   ```

2. Run the compiled UDPServer class, providing the desired port number as a command-line argument:

   ```
   java Server/UDPServer <port>
   ```

3. Add the `<port>` number on which you want the server to listen for incoming connections.

4. The server will start and display a message indicating the IP address and port it is listening on. It will then wait for client connections.


## UnifiedClient

The UnifiedClient program represents the client-side of the communication. It connects to a UDP/TCP server using the server's IP address and port number. Once connected, the client can send messages to the server and receive responses.

### Usage

To use the UnifiedClient, follow these steps:

1. Navigate to the src folder. Compile the UnifiedClient.java file using the Java compiler:

   ```
   javac Client/UnifiedClient.java
   ```

2. Run the compiled TCPClient class, providing the server's IP address and port number as command-line arguments:

   ```
   java Client/TCPClient <server-ip> <server-port> <protocol>
   ```

   Replace `<server-ip>` with the IP address (In our case `localhost`) of the server you want to connect to, and `<server-port>` with the corresponding port number and `<protocol>` either `tcp` or `udp`

3. The client will attempt to establish a connection with the server. If successful, it will display a message indicating the connection status.

4. Once connected, you can enter messages to send to the server. The client will display the responses received from the server.

5. To terminate the client program, simply close the client window or use the appropriate termination command.

-------

# Docker Setup

We have also done the docker setup for the project implementation.

## DocKer Installation

Make sure you have docker running on your local computer.



### Usage

To use the UDPServer, follow these steps:

1. Build the docker images for Client, TCP server and UDP server by running the below script:

   ```
   ./build_server_client.sh
   ```

2. To Run the TCP Server, run the below script:

   ```
   ./run_server_tcp.sh
   ```

3. To Run the UDP Server, run the below script:

    ```
   ./run_server_udp.sh
   ```

4. To Run the Unified Client, run the below script:

    ```
   ./run_client.sh <protocol>
   ```
   Protocol will be either `tcp` or `udp`

#Screenshots

## TCP Prepopulate & perform operations 10 PUT, 5 GET, 5 DELETE
![TCP Prepopulate & perform operations 10 PUT, 5 GET, 5 DELETE](Screenshots/tcp-populate.png)

## TCP PUT
![TCP PUT](Screenshots/tcp-put.png)

## TCP GET
![TCP GET](Screenshots/tcp-get.png)

## TCP DELETE
![TCP DELETE](Screenshots/tcp-delete.png)

## UDP Prepopulate & perform operations 10 PUT, 5 GET, 5 DELETE
![UDP Prepopulate & perform operations 10 PUT, 5 GET, 5 DELETE](Screenshots/udp-populate.png)

## UDP PUT
![UDP PUT](Screenshots/udp-put.png)

## UDP GET
![UDP GET](Screenshots/udp-get.png)

## UDP DELETE
![UDP DELETE](Screenshots/udp-delete.png)
