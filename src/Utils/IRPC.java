package Utils;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRPC extends Remote{

    String Operation(String input) throws RemoteException;

}
