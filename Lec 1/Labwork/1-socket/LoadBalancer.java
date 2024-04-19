import java.io.*;
import java.net.*;
import java.util.Random;


public class LoadBalancer {
    static String[] hosts = {"localhost", "localhost"};
    static int[] ports = {8081, 8082};
    static int nbHosts = 2;

    public static void main(String[] args) throws IOException {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(8080);
            while (true) {
                Socket s = ss.accept();
                Slave sl = new Slave(s);
                sl.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ss != null) {
                ss.close();
            }
        }
    }
}

class Slave extends Thread {
    private Socket csock;
    private Random rand = new Random();

    public Slave(Socket s) {
        this.csock = s;
    }

    public void run() {
        try {
            InputStream csi = csock.getInputStream();
            OutputStream cso = csock.getOutputStream();

            int target = rand.nextInt(2);
            Socket ssock = new Socket(LoadBalancer.hosts[target], LoadBalancer.ports[target]);

            InputStream ssi = ssock.getInputStream();
            OutputStream sso = ssock.getOutputStream();

            byte[] buffer = new byte[1024];
            // read request from client
            int nb_read = csi.read(buffer);

            // write request to server
            sso.write(buffer, 0, nb_read);

            // read response from server
            nb_read = ssi.read(buffer);

            // write response to client
            cso.write(buffer, 0, nb_read);

            csock.close();
            ssock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}