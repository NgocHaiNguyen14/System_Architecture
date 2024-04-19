import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Console extends Remote {
    void println(String s) throws RemoteException;
}