// package video;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Library extends Remote {
    void register(Video v) throws RemoteException;
    Video lookup(String title) throws RemoteException;
}