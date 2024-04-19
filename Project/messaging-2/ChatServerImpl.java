import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {
    private List<String> connectedUsers;
    private List<String> connectedUserAddress;
    private List<String> connectedUserPort;
    private List<ChatClientCallback> connectedUsersCallback;

    protected ChatServerImpl() throws RemoteException {
        super();
        connectedUsers = new ArrayList<>();
        connectedUserAddress = new ArrayList<>();
        connectedUserPort = new ArrayList<>();
        connectedUsersCallback = new ArrayList<>();
    }

    @Override
    public void enter(String name, String address, int port, ChatClientCallback callback) throws RemoteException {
        if (!connectedUsers.contains(name)) {
            connectedUsers.add(name);
            connectedUserAddress.add(address);
            connectedUserPort.add(Integer.toString(port));
            connectedUsersCallback.add(callback);
            broadcastServer(name, " entered RMI server.");
            broadcastClient("admin", name + " entered");
        } else {
            broadcastServer(name, " is already in the RMI server.");
            broadcastClient("admin", name + " is already in the chat.");
        }
    }

    @Override
    public void leave(String name, String address, int port,  ChatClientCallback callback) throws RemoteException {
        connectedUsers.remove(name);
        connectedUserAddress.remove(address);
        connectedUsersCallback.remove(callback);
        connectedUserPort.remove(Integer.toString(port));
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

    @Override
    public void attach(String name, String filename, String filepath, String serverAddress, int serverPort) {
        String message = "File " + filename + " attached by " + name + ". From: " + serverAddress + ":" + serverPort;
        broadcastClient("admin",message);
        for (int i = 0; i < connectedUsers.size(); i++) {
            String username = connectedUsers.get(i);        
            if (!username.equals(name)) {
                ChatClientCallback client = connectedUsersCallback.get(i);
                try {
                    client.handleDownload(serverAddress, serverPort, filename);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        // RMI Server
        try {
            ChatServer server = new ChatServerImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ChatServer", server);
            System.out.println("ChatServer ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
