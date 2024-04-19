import java.rmi.*;

public interface RmiServer extends Remote {
    public void sayHello() throws RemoteException;
    void registerClientCallback(ClientCallback clientCallback) throws RemoteException;
}