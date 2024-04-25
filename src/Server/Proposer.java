package Server;

import java.net.SocketTimeoutException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

import Utils.IPaxos;


public class Proposer extends GeneralServer implements Runnable{
	
	private static int proposalId;
	
	public int getProposalId() {
		return proposalId;
	}
	
	public Proposer(String keystore){
		this.keyStore = keystore;
		this.logger = new Logger();
	}

	public void setProposalId(int proposalId) {
		Proposer.proposalId = proposalId;
	}

	public void start(){
		proposalId = 0;
	}
	
	public synchronized String propose(String input){
		// String response = "";
		Map<String, Integer> serverMap = GeneralServer.serverMap;

		Registry registry = null;
		GeneralServer.COUNT_OF_SERVERS = 0;
		proposalId ++;
		try{
			// for(Map.Entry<String, Integer> entry : serverMap.entrySet()){						
			// 	try{
			// 		registry = LocateRegistry.getRegistry("localhost",entry.getValue());
			// 		IPaxos stub = (IPaxos) registry.lookup(keyStore);
			// 		if(stub.prepare(proposalId, input)){
			// 			GeneralServer.COUNT_OF_SERVERS++;
			// 			// System.out.print("Count of yes is "+GeneralServer.COUNT_OF_SERVERS);
			// 		}
			// 		System.out.println("Count of servers who said yes to prepare "+GeneralServer.COUNT_OF_SERVERS);	
			// 	}catch(SocketTimeoutException se){
			// 		//Continue the process even if one server times out
			// 		continue;
			// 	}catch(RemoteException re){
			// 		//Continue the process even if one server was not reachable
			// 		continue;
			// 	} 	
			// }

			askToPrepare(registry, serverMap, input);

			// System.out.print("Count of yes is "+GeneralServer.COUNT_OF_SERVERS+" number of servers is "+GeneralServer.NUMBER_OF_SERVERS);

			//Ensure atleast 3 servers reply with ok
			if(GeneralServer.COUNT_OF_SERVERS>(GeneralServer.NUMBER_OF_SERVERS/2)){
				// System.out.println(GeneralServer.COUNT_OF_SERVERS + " servers replied with prepare ok");
				GeneralServer.COUNT_OF_SERVERS = 0;
				// for(Map.Entry<String, Integer> entry : serverMap.entrySet()){					
				// 	try{
				// 		registry = LocateRegistry.getRegistry("localhost",entry.getValue());
				// 		IPaxos stub = (IPaxos) registry.lookup(keyStore);
				// 		//Check with all servers if they can accept the proposal
				// 		if(stub.accept(proposalId, input)){
				// 			GeneralServer.COUNT_OF_SERVERS ++;
				// 		}
				// 	}catch(SocketTimeoutException se){
				// 		//Continue the process even if one server times out
				// 		continue;
				// 	}catch(RemoteException re){
				// 		//Continue the process even if one server was not reachable
				// 		continue;
				// 	} 	
				// }
				askToAccept(registry, serverMap, input);
			} else {
				String response = "Consensus could not be reached as only " + GeneralServer.COUNT_OF_SERVERS +
						"servers replied to the prepare request";
				System.out.println(response);
				return response;
			}
			//Ensure at least 3 servers reply with ok
			if(GeneralServer.COUNT_OF_SERVERS>(GeneralServer.NUMBER_OF_SERVERS/2)){
				// System.out.println(GeneralServer.COUNT_OF_SERVERS + " servers replied with accept ok");
				// for(Map.Entry<String, Integer> entry : serverMap.entrySet()){						
				// 	try{
				// 		registry = LocateRegistry.getRegistry("localhost",entry.getValue());
				// 		IPaxos stub = (IPaxos) registry.lookup(keyStore);
				// 		//Ask all servers to commit as quorum number has accepted
				// 		response = stub.commit(input);
				// 	}catch(SocketTimeoutException se){
				// 		//Continue the process even if one server times out
				// 		continue;
				// 	}catch(RemoteException re){
				// 		//Continue the process even if one server was not reachable
				// 		continue;
				// 	} 
				// }
				return askToCommit(registry, serverMap, input);
			} else {
				String response = "Consensus could not be reached as only " + GeneralServer.COUNT_OF_SERVERS +
						"servers replied to the accept request";
				System.out.println(response);
				return response;
			}
		} 
		catch(NotBoundException nbe){
			System.out.println("Remote Exception" + nbe);
		}	
		return "Something went wrong. Try again!";
	}

	private void askToPrepare(Registry registry, Map<String,Integer> serverMap, String input) throws NotBoundException
	{
		System.out.println("In ask prepare"+serverMap);
		for(Map.Entry<String, Integer> entry : serverMap.entrySet()){						
			try{
				registry = LocateRegistry.getRegistry("localhost",entry.getValue());
				IPaxos stub = (IPaxos) registry.lookup(keyStore);
				if(stub.prepare(proposalId, input)){
					GeneralServer.COUNT_OF_SERVERS++;
					// System.out.print("Count of yes is "+GeneralServer.COUNT_OF_SERVERS);
				}
				// System.out.println("Count of servers who said yes to prepare "+GeneralServer.COUNT_OF_SERVERS);	
			}catch(SocketTimeoutException se){
				//Continue the process even if one server times out
				continue;
			}catch(RemoteException re){
				//Continue the process even if one server was not reachable
				continue;
			} 	
		}
	}

	private void askToAccept(Registry registry, Map<String,Integer> serverMap, String input) throws NotBoundException
	{
		System.out.println("In ask accept"+serverMap);

		for(Map.Entry<String, Integer> entry : serverMap.entrySet()){					
			try{
				registry = LocateRegistry.getRegistry("localhost",entry.getValue());
				IPaxos stub = (IPaxos) registry.lookup(keyStore);
				//Check with all servers if they can accept the proposal
				if(stub.accept(proposalId, input)){
					GeneralServer.COUNT_OF_SERVERS ++;
				}
			}catch(SocketTimeoutException se){
				//Continue the process even if one server times out
				continue;
			}catch(RemoteException re){
				//Continue the process even if one server was not reachable
				continue;
			} 	
		}
	}

	private String askToCommit(Registry registry, Map<String,Integer> serverMap, String input) throws NotBoundException
	{
		String response = "";
		System.out.println("In ask commit"+serverMap);
		for(Map.Entry<String, Integer> entry : serverMap.entrySet()){						
			try{
				registry = LocateRegistry.getRegistry("localhost",entry.getValue());
				IPaxos stub = (IPaxos) registry.lookup(keyStore);
				//Ask all servers to commit as quorum number has accepted
				response = stub.commit(input);
			}catch(SocketTimeoutException se){
				//Continue the process even if one server times out
				continue;
			}catch(RemoteException re){
				//Continue the process even if one server was not reachable
				continue;
			} 
		}

		return response;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	void Server(int PORT) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'Server'");
	}
}
