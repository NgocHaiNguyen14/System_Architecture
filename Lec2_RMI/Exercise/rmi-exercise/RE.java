import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

public class RE {
    public static void main(String args[]) {
        
        String host = args[0];
        String command = args[1];

        try {
            // Console on clinet
            Console c = new ConsoleImpl();

            // Get the registry on the server
            Registry registry = LocateRegistry.getRegistry(host);

            // Look up the Daemon object from the registry
            Daemon daemon = (Daemon) registry.lookup("//"+host+"/daemon");

            // Invoke the exec method on the Daemon object
            daemon.exec(command,c);

            System.out.println("Command executed successfully on remote machine.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}