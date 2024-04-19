import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

public class DaemonImpl extends UnicastRemoteObject implements Daemon {

    public DaemonImpl() throws RemoteException {}

    private void LocalExec(String command, Console c) {
        try {
            System.out.println("Executing command on server: " + command);
            c.println("Executing command on client: " + command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exec(String command, Console c) throws RemoteException {
        LocalExec(command, c);
    }

    public static void main(String[] args) {
        try {
            // Create the Daemon object
            Daemon daemon = new DaemonImpl();

            // Create the RMI registry on port 1099 (create on same machine/localhost)
            Registry registry = LocateRegistry.createRegistry(1099);

            // Bind the Daemon object to the registry
            registry.rebind("//localhost/daemon", daemon);

            System.out.println("Daemon ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}