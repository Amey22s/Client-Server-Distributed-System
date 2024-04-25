package Utils;

import java.net.SocketTimeoutException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPaxos extends Remote{
    public String Operation(String input) throws RemoteException;
    public boolean prepare(int proposalId, String input) throws RemoteException, SocketTimeoutException;
    public boolean accept(int proposalId, String input) throws RemoteException, SocketTimeoutException;
    public String commit(String input) throws RemoteException, SocketTimeoutException;
}
