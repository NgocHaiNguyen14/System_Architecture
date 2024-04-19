import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Pad extends Remote {
	// ADd send data in type SRecord (interface)
	public void add(SRecord sr) throws RemoteException;
	public RRecord consult(String n, boolean forward) throws RemoteException;
}

