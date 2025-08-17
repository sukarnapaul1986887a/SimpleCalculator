import java.rmi.*;
import java.util.List;

public interface Calculator extends Remote {
    void pushValue(int val) throws RemoteException;
    List<Integer> getStack() throws RemoteException;
    boolean isEmpty() throws RemoteException;
}