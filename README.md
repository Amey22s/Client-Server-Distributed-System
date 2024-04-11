### Usage

To use the Coordinator:

1. To compile the files needed for 2 PC communication, navigate to the src folder and run the below commands:

   ```
   javac Server/Coordinator.java
   ```

   This will compile all the necessary java files required to perform 2 PC communication between client and server.

2. Run the compiled Coordinator class, providing the desired port numbers for RMI registries to hold server stubs and the desired name for the key corresponding to stub/skeleton object in RMI registry as a command line argument:

   ```
   java Server/Coordinator <port1> <port2> <port3> <port4> <port5> <keystore name>
   ```


Note: Provide `<keystore name>` to which you want the RMI registry to link your stub/skeleton object to. The same name needs to be provided in client. It will be used to successfully lookup the name in RMI registry to fetch stub object. Provide `<port1/2/3/4/5>` as the corresponding port numbers where you want to run your RMI stub servers hosted.

Note: This project is developed to handle exactly 5 server stubs and these port numbers need to be given as an input in client as well.


To use the TPCClient:

1. To compile the files needed for 2 PC communication on client side, navigate to the src folder and run the below commands:

   ```
   javac Client/TPCDriver.java
   ```

2. Run the compiled TPCDriver class, providing the port numbers and desired name for the key corresponding to stub/skeleton object in RMI registry as command line arguments:

   ```
   java Client/TPCDriver <port1> <port2> <port3> <port4> <port5> <keystore name>
   ```

   
Note: Provide `<keystore name>` to which the RMI registry linked your stub/skeleton object to. The same name as provided in server. It will be used to successfully lookup the name in RMI registry to fetch stub object. Provide `<port1/2/3/4/5>` as the corresponding port number where you run your RMI registry earlier in server.

3. On successful connection, it will display a message indicating the connection status.

4. Once connected, you can choose the operation to be performed from the list of options available. Follow the instructions on screen to interact with the server. The client will display the responses received from the server.

5. To terminate the client program, simply close the client window or use the appropriate termination command (control(^) + C in Mac).

-------

Executive Summary:

Assignment Overview:
This project builds upon the prior project's Key-Value Store Server by achieving high availability and increased bandwidth through replication across five distinct servers. This distributed system ensures consistent data retrieval and modification by enabling clients to interact with any of the replicas seamlessly. To guarantee consistency across these replicated stores, a two-phase commit protocol is implemented for PUT and DELETE operations.  While the core client functionality remains largely unchanged, it now has the flexibility to contact any replica for its operations.

Technical Impression:
Replicating the Key-Value Store Server across multiple servers presented an engaging challenge.  My main focus was on designing and implementing the two-phase commit protocol to coordinate updates and maintain consistency between replicas. The two-phase commit protocol involved sending a "prepare commit" message to all replicas in the first phase, followed by a "commit" message upon receiving acknowledgments from all. This process ensured that all replicas were prepared to receive the update before committing it. Additional considerations included implementing read-write locking mechanisms for the data files to prevent concurrent write operations and utilizing properties files on each server to manage data and updates efficiently. Looking forward, potential improvements could involve implementing key-based locking for a more granular approach to data access control, rather than relying on file-level locking.

This project provided valuable experience in distributed system design, particularly regarding data consistency and fault tolerance in replicated systems. The importance of robust protocols and careful handling of communication and failures were all emphasized throughout the development process.

Note: 
The GET-ALL and DELETE-ALL functions are optional implementation from last make up assignments.