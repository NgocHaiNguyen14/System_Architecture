import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClientCallback extends Remote {
    public void handleResponse(String name, String message) throws RemoteException;
}