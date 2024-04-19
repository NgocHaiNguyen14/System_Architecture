import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public interface ChatServer extends Remote {
    void enter(String name, ChatClientCallback client) throws RemoteException;
    void leave(String name, ChatClientCallback client) throws RemoteException;
    String[] who() throws RemoteException;
    void write(String name, String text) throws RemoteException;
}
