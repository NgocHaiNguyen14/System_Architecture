import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiClient extends UnicastRemoteObject implements ClientCallback {

    public RmiClient() throws RemoteException {
        super(); 
    }

    public static void main(String[] args) {
        try {
            // Look up the server object in the RMI registry at the specified address
            RmiServer server = (RmiServer) Naming.lookup("//localhost:8080/foo");

            // Send callback
            RmiClient clientCallback = new RmiClient();
            server.registerClientCallback(clientCallback);

            // Call the sayHello() method on the server
            server.sayHello();

        } catch (RemoteException e) {
            System.err.println("RemoteException occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void receiveMessage(String message) throws RemoteException {
        System.out.println("Received message from server: " + message);
    }
}
