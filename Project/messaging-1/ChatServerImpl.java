import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {
    private List<String> connectedUsers;
    private List<ChatClientCallback> connectedUsersCallback;

    protected ChatServerImpl() throws RemoteException {
        super();
        connectedUsers = new ArrayList<>();
        connectedUsersCallback = new ArrayList<>();
    }

    @Override
    public void enter(String name, ChatClientCallback callback) throws RemoteException {
        if (!connectedUsers.contains(name)) {
            connectedUsers.add(name);
            connectedUsersCallback.add(callback);
            broadcastServer(name, " enterd");
            broadcastClient("admin", name + " entered");
            System.out.println(connectedUsers);
        } else {
            broadcastServer(name, " is already in the chat.");
            broadcastClient("admin", name + " is already in the chat.");
        }
    }

    @Override
    public void leave(String name, ChatClientCallback callback) throws RemoteException {
        connectedUsers.remove(name);
        connectedUsersCallback.remove(callback);
        broadcastServer(name, "left the chat.");
        broadcastClient("admin", name + " left the chat.");
    }

    @Override
    public String[] who() throws RemoteException {
        return connectedUsers.toArray(new String[0]);
    }

    @Override
    public void write(String name, String text) throws RemoteException {
        for (int i = 0; i < connectedUsers.size(); i++) {
            String username = connectedUsers.get(i);        
            if (!username.equals(name)) {
                ChatClientCallback client = connectedUsersCallback.get(i);
                try {
                    client.handleResponse(name, text);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void broadcastServer(String username, String message) {
        System.out.println(username + message);
    }

    private void broadcastClient(String username, String message) {
        // Loop to print on all chat window
        for (ChatClientCallback client : connectedUsersCallback) {
            try {
                client.handleResponse(username, message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            // Create the server 
            ChatServer server = new ChatServerImpl();

            // Create the RMI registry on port 1099 (create on same machine/localhost)
            Registry registry = LocateRegistry.createRegistry(1099);

            // Bind the sever object to the registry
            registry.rebind("ChatServer", server);

            System.out.println("ChatServer ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
