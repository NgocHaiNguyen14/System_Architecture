import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatClientCallbackImpl extends UnicastRemoteObject implements ChatClientCallback {
    private String name;
    private String address;
    private int port;

    public ChatClientCallbackImpl(String name, String address, int port) throws RemoteException {
        super();
        this.name = name;
        this.address = address;
        this.port = port;
    }

    @Override
    public void handleResponse(String username, String message) throws RemoteException {
        ChatClient.print(username, message);
    }

    @Override
    public void handleDownload(String serverAddress, int serverPort, String filename) throws RemoteException {
        ChatClient.download(serverAddress, serverPort, filename);
    }
}
