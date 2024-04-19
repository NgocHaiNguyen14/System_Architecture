import java.io.*;
import java.net.*;

public class client extends Thread {
    final static String[] hosts = {"localhost", "localhost", "localhost"};
    final static int[] ports = {8081, 8082, 8083};
    final static int nb = 3;
    static String[] document = new String[nb];

    private int frag;

    // Constructor
    public client(int frag) {
        this.frag = frag;
    }

    // Thread run method
    @Override
    public void run() {
        try {
            System.out.println("Download fragment " + frag);
            // Connect to the server on the specified port
            Socket cs = new Socket(hosts[frag], ports[frag]);

            // Create input and output streams for communication with the server
            OutputStream os = cs.getOutputStream();
            InputStream is = cs.getInputStream();

            // Create object streams for serialization
            ObjectOutputStream oos = new ObjectOutputStream(os);
            ObjectInputStream ois = new ObjectInputStream(is);

            // Request the fragment from the server
            oos.writeObject(frag);

            // Read the fragment from the server
            document[frag] = (String) ois.readObject();

            System.out.println("End of download fragment " + frag);

            // Close streams and socket
            os.close();
            is.close();
            cs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            // Create an array of threads for downloading each fragment
            Thread[] t = new Thread[nb];
            for (int i = 0; i < nb; i++) {
                // Create and start each thread
                t[i] = new client(i);
                t[i].start();
            }

            // Wait for all threads to complete
            for (int i = 0; i < nb; i++) {
                t[i].join();
                System.out.println("Thread " + i + " has finished.");
            }

            // Print all downloaded fragments
            for (int i = 0; i < nb; i++) {
                System.out.println("Fragment " + i + ": " + document[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
