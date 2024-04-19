// package video;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class LibraryImpl extends UnicastRemoteObject implements Library {

    private HashMap<String, Video> map = new HashMap<>();

    public LibraryImpl() throws RemoteException {}

    // Register a video
    @Override
    public void register(Video v) throws RemoteException {
        map.put(v.getTitle(), v);
        System.out.println("Uploaded to library: "+v.getTitle());
    }

    @Override
    public Video lookup(String title) throws RemoteException {
        return map.get(title);
    }

    public static void main(String[] args) {
        try {

            LocateRegistry.createRegistry(8080);
            Naming.rebind("rmi://localhost:8080/library", new LibraryImpl());
            System.out.println("Library server is running at rmi://localhost:8080/library");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
