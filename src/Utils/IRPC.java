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
    public void go(UUID messageId,  int callBackServer) throws RemoteException;
    public void setServersInfo(int[] OtherServersPorts, int yourPorts ) throws RemoteException;
    public void prepareKeyValue(UUID messageId, Entry entry, int callBackServer) throws RemoteException;
    // public int getPort() throws RemoteException;
}
