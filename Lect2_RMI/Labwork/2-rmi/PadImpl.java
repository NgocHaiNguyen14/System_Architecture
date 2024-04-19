import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

public class PadImpl extends UnicastRemoteObject implements Pad {
    public void add(SRecord sr) throws RemoteException {
        System.out.println("Received an ADD");
    }
    public RRecord consult(String n, boolean forward) throws RemoteException  {
        return null;
    }
    public PadImpl() throws RemoteException {

    }
    public static void main(String args[]) {
        try {
            Registry registry = LocateRegistry.createRegistry(4000);
            Naming.rebind("//localhost:4000/pad", new PadImpl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}