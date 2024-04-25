package Server;

/**
 * Learner is responsible to maintain the state of the server and handles the commits to it
 
 */
public class Learner extends GeneralServer implements Runnable{

	public void start(){
		
	}
	
	public String commit(String input){
		String[] tokens = input.split(" ");
		return performOperation(tokens);
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

	public void setMapUtils(MapUtils mp)
	{
		this.mapUtils = mp;
	}
}
