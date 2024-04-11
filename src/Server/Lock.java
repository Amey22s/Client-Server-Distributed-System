package Server;

public class Lock{
		 
	private int readerCount = 0;
	private int writerCount      = 0;
		 
	public synchronized void activateReadLock() throws InterruptedException
	{
		while(writerCount > 0)
		{
			wait();
		}
		readerCount++;
	}
		 
	public synchronized void deactivateReadLock()
	{
		readerCount--;
		notifyAll();
	}
		 
	public synchronized void activateModifyLock() throws InterruptedException
	{
		 
		while(readerCount > 0 || writerCount > 0)
		{
			wait();
		}
		writerCount++;
	}
		 
    public synchronized void deactivateModifyLock() throws InterruptedException
    {
        writerCount--;
        notifyAll();
    }

}
