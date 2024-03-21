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

Executive Summary:

Assignment Overview
This assignment focused on developing a client-server application using Java RMI (Remote Method Invocation) for communication. RMI allows us to interact with remote objects as if they were local, enabling method invocations and data exchange. It utilizes object serialization, dynamic class loading, and a security manager for secure data transfer. While multithreaded, RMI itself isn't thread-safe by default, requiring careful implementation for concurrent access. This project involved building a basic key-value store where the client could perform PUT, GET, GETALL, DELETE, and DELETEALL operations on the server's data. The core objective was to solidify our understanding of RMI, multithreading principles, and fundamental client-server architecture.


Technical Impression
This assignment provided valuable hands-on experience in implementing a distributed application using Java RMI. The implementation process involved:

1. Designing the remote interface
2. Creating the remote interface implementation
3. Compiling the implementation and generating skeleton objects
4. Register skeleton object in registry and create a stub object by using look up in registry.
5. Compiling and launching the server and client applications


Challenges Encountered:
- Code reusability: Challenges in properly defining the remote interface, and implementing it in such a way that I could use majority of my existing code and logic. 
- RMI registry: How to create a rmi registry and use it to register objects in it which will be then looked up at client side and used to perform desired action.
- Thread Safety: Implementing thread-safe functionality to handle concurrent client requests required careful consideration of synchronization mechanisms and utilizing concurrency control provided by Properties class in Java.

RMI and Thread Safety:
While RMI offers an inbuilt thread pool and can handle concurrent requests, it's crucial to ensure server-side data is synchronized when accessed by multiple clients. This is achieved by using the synchronized keyword in the functionality implementation methods, ensuring exclusive access to shared resources during modification. Additionally, Properties class provides a thread-safe object, offering fine-grained locking mechanisms.

Key Learnings:
This assignment significantly enhanced my understanding of distributed systems, RMI communication, and concurrent programming. Areas for potential improvement include optimizations for scalability and performance.

Overall:
This project provided a valuable learning experience, solidifying core concepts in RMI, multithreading, client-server communication, robust error handling and advanced logging mechanisms,. Future endeavors could focus on implementing the suggested improvements for a more scalable  and efficient application.

Note: 
The GET-ALL and DELETE-ALL functions are optional implementation from last make up assignments.



