package Utils;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRPC extends Remote{
    
    public String Operation(String input) throws RemoteException;
}
