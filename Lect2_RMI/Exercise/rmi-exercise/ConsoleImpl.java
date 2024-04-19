import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ConsoleImpl extends UnicastRemoteObject implements Console {
   public ConsoleImpl() throws RemoteException {}
   
   public void println(String s) throws RemoteException {
        System.out.println(s);
    }
}