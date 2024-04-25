package Server;
import java.net.SocketTimeoutException;
import java.rmi.RemoteException;
import java.util.Random;

public class Acceptor extends GeneralServer implements Runnable{

	private static int myproposalId;
	
	private boolean active;
	
	private int serverNumber;

	public Acceptor(String keyStore) {
		this.keyStore = keyStore;
	}

	public int getMyproposalId() {
		return myproposalId;
	}

	public void setMyproposalId(int myproposalId) {
		Acceptor.myproposalId = myproposalId;
	}

	public void setMapUtils(MapUtils mp)
	{
		this.mapUtils = mp;
	}

	public void setLogger(Logger logger)
	{
		this.logger = logger;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean isAlive) {
		this.active = isAlive;
	}
	
	public void start(){
		active = true;
	}
	
	public void kill(){
		active = false;
	}
	
	public boolean accept(int proposalId, String input) throws RemoteException, SocketTimeoutException
	{
		return check(proposalId, input);
	}
	
	public boolean prepare(int proposalId, String input) throws RemoteException, SocketTimeoutException
	{
		return check(proposalId, input);
	}

	private boolean check(int proposalId, String input) throws RemoteException, SocketTimeoutException{
		//Randomly put a server to sleep
		try{
			Random random = new Random();
    		int randomNumber = random.nextInt(GeneralServer.NUMBER_OF_SERVERS) + 1;
			if(randomNumber == serverNumber){
				System.out.println("Server"+serverNumber+" going to sleep");
				Thread.sleep(5000);
				throw new InterruptedException("Server"+serverNumber+" is sleeping");
			}
		} catch (InterruptedException ie){
			logger.errorLogger("Error in check of acceptor "+ie.getMessage());
			return false;
		} 

		// System.out.println("Proposal id passed is "+proposalId+" myProposal id is "+myproposalId);
		if(proposalId < myproposalId){
			return false;
		}

		// if(super.isValid(input)){
		setMyproposalId(proposalId);
		// 	return true;
		// }
		return true;
	}

	public int getServerNumber() {
		return serverNumber;
	}

	public void setServerNumber(int serverNumber) {
		this.serverNumber = serverNumber;
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
