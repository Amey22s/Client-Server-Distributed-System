package Server;

public class PaxosHelper extends GeneralServer{


    protected int[] serverPorts;
	private String keyStore;


    public PaxosHelper()
    {
        this.serverPorts = new int[5];
    }

	protected void setKeyStore(String keyStore)
	{
		this.keyStore = keyStore;
	}

	protected void setPort(int port)
	{
		this.port = port;
	}


	protected void setLogger(Logger logger)
	{
		this.logger = logger;
	}

    protected void setPorts(String[] args)
    {
        for (int i = 0 ; i < args.length - 1; i++)
        {
            serverPorts[i] = Integer.parseInt(args[i]);
        }
    }

    void Server(int PORT) {
        
    }

    
}
