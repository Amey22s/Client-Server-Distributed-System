package Utils;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

import Server.AckType;
import Server.Entry;

public interface IRPC extends Remote{
    
    public String Operation(String input) throws RemoteException;
    public String TPCPreprocess(UUID messageId, String input) throws RemoteException;
    public void acknowledge(UUID messageId, int callBackServer, AckType type) throws RemoteException;
    public void processCommit(UUID messageId,  int callBackServer) throws RemoteException;
    public void processEntry(UUID messageId, Entry entry, int callBackServer) throws RemoteException;
    public void setServersInfo(int[] otherServersPorts, int yourPorts) throws RemoteException;

    // // For PAXOS
    // public boolean prepare(int proposalId, int key, int action) throws RemoteException;
	// public boolean accept(int proposalId, int key, int action) throws RemoteException;
	// public String commit(int key, int action) throws RemoteException;
}
