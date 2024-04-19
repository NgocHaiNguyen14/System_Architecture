import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Daemon extends Remote {
	public void exec(String command, Console c) throws RemoteException;
}

