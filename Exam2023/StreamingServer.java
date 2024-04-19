import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class StreamingServer {
    public static void main(String args[]) {
        String title = args[0];
        String file = args[1];
        int port = Integer.parseInt(args[2]);

        try {
            Library l = (Library) Naming.lookup("rmi://localhost:8080/library");
            l.register(new VideoImpl(title,"localhost",args[2]));

            ServerSocket ss = new ServerSocket(port);
            System.out.println("Streaming now: "+title+" at "+"localhost:"+port);
            while (true) {
                Socket cs = ss.accept();
                // Create thread
                Slave sl = new Slave(cs, file);
                sl.start();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}