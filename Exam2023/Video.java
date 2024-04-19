// package video;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Video extends Serializable {
    
    public String getTitle();
    public String getHost();
    public String getPort();
}