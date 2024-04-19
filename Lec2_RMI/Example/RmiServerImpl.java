import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.*;

public class RmiServerImpl extends UnicastRemoteObject implements RmiServer {

    private ClientCallback clientCallback;

    // Constructor
    public RmiServerImpl() throws RemoteException {
        super(); 
    }

    public static void main(String[] args) {
        try {
            // Create an RMI registry on port 4000
            Registry registry = LocateRegistry.createRegistry(8080);

            // Create an instance of the server implementation
            RmiServerImpl server = new RmiServerImpl();

            // Bind the server to the registry
            Naming.rebind("//localhost:8080/foo", server);
            System.out.println("Server bound in registry at port 8080");
        } catch (RemoteException e) {
            System.err.println("Failed to create registry or bind the server: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void sayHello() throws RemoteException {
        // Corrected system to System
        System.out.println("Hello World");
        clientCallback.receiveMessage("Hello World from server");
    }

    @Override
    public void registerClientCallback(ClientCallback clientCallback) throws RemoteException {
        this.clientCallback = clientCallback;
    }
}
