package Server;

import java.net.SocketTimeoutException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import Utils.IPaxos;


public class PaxosServer extends GeneralServer implements IPaxos{
	
	private Proposer proposer;
	
	private Learner learner;
	private Acceptor acceptor;

	public PaxosServer(String keystore) throws RemoteException {
		try {
			this.keyStore = keystore;
			this.proposer = new Proposer(keystore);
			this.learner = new Learner();
			this.acceptor = new Acceptor(keystore);
			this.proposer.start();
			this.learner.start();
			this.acceptor.start();
			GeneralServer.serverNo++;
			this.acceptor.setServerNumber(serverNo);

			String mapFilename = "Server/map"+GeneralServer.serverNo+".properties";
			this.logger = new Logger();
			this.mapUtils = new MapUtils(mapFilename);
		} 
		catch (Exception e) 
		{
			logger.errorLogger("Error in constructor of paxos "+e.getMessage());
		}
		
		// this.helper = new PaxosHelper();
	}
	

	public String Operation(String input) throws RemoteException {
		logger.responseLogger("Input from client is "+input);
        Entry entry = createEntry(input);
		if(entry.getOp().toUpperCase().startsWith("GET"))
		{
			return learner.commit(input);
		}

        return proposer.propose(input);
	}


	public boolean accept(int proposalId, String input) throws RemoteException, SocketTimeoutException {
		return acceptor.accept(proposalId, input);
	}


	public boolean prepare(int proposalId, String input) throws RemoteException, SocketTimeoutException {
		return acceptor.prepare(proposalId, input);
	}

	public String commit(String input) throws RemoteException, SocketTimeoutException {
		return learner.commit(input);
	}

	@Override
	void Server(int PORT) {
		try {
			IPaxos stub = (IPaxos) UnicastRemoteObject.exportObject(this, PORT);
			Registry registry = LocateRegistry.createRegistry(PORT); 
			
			registry.rebind(keyStore, stub);
			GeneralServer.serverMap.put("Server"+serverNo, PORT);
			setUtils();
			logger.responseLogger("Server started...");
			}
			catch(Exception e)
			{
				logger.errorLogger("Error in Server function of paxos server "+e.getMessage());
			}
	}

	private void setUtils()
	{
		acceptor.setMapUtils(this.mapUtils);
		learner.setMapUtils(this.mapUtils);
		acceptor.setLogger(logger);
	}

}
