# JAVA RMI

A Java-specific protocol enabling applications on separate machines to interact as if they were local objects. It lets you call methods on remote objects transparently. It utilizes object serialization, dynamic class loading, and a security manager for secure data transfer. While multithreaded, RMI itself isn't thread-safe by default, requiring careful implementation for concurrent access.

### Usage

To use the RPCServer:

1. To compile the files needed for RPC communication, navigate to the src folder and run the below commands:

   ```
   javac Server/RPCDriver.java
   ```

   This will compile all the necessary java files required to perform RPC communication between client and server.

2. Run the compiled RPCDriver class, providing the desired port number for RMI registry and the desired name for the key corresponding to stub/skeleton object in RMI registry as a command line argument:

   ```
   java Server/RPCDriver <port> <keystore name>
   ```


Note: Provide `<keystore name>` to which you want the RMI registry to link your stub/skeleton object to. The same name needs to be provided in client. It will be used to successfully lookup the name in RMI registry to fetch stub object. Provide `<port>` as the corresponding port number where you want to run your RMI registry.


To use the UnifiedClient:

1. To compile the files needed for RPC communication on client side, navigate to the src folder and run the below commands:

   ```
   javac Client/UnifiedClient.java
   ```

2. Run the compiled UnifiedClient class, providing the server's IP address, port number, protocol to be used and desired name for the key corresponding to stub/skeleton object in RMI registry as command line arguments:

   ```
   java Client/UnifiedClient <server-ip> <rmi-port> <protocol> <keystore name>
   ```

   
Note: Provide `<keystore name>` to which the RMI registry linked your stub/skeleton object to. The same name as provided in server. It will be used to successfully lookup the name in RMI registry to fetch stub object. Provide `<server-ip>` as the IP address of the server(In case of a local machine test run it will be `localhost`), and `<rmi-port>` as the corresponding port number where you run your RMI registry earlier in server. Provide `<protocol>` as 'rpc' in this case.

3. On successful connection, it will display a message indicating the connection status.

4. Once connected, you can choose the operation to be performed from the list of options available. Follow the instructions on screen to interact with the server. The client will display the responses received from the server.

5. To terminate the client program, simply close the client window or use the appropriate termination command (control(^) + C in Mac).

-------


