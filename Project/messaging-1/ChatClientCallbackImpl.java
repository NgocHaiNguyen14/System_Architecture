import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatClientCallbackImpl extends UnicastRemoteObject implements ChatClientCallback {
    private final String name;

    public ChatClientCallbackImpl(String name) throws RemoteException {
        super();
        this.name = name;
    }

    @Override
    public void handleResponse(String username, String message) throws RemoteException {
        ChatClient.print(username, message);
    }
}
