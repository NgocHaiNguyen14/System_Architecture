import java.io.*;
import java.net.*;

public class server extends Thread {
    final static int ports[] = {8081, 8082, 8083};
    final static int nb = 3;

    public static void main(String[] args) {
        try {
            // Parse the index of the server from command line arguments
            int i = Integer.parseInt(args[0]);
            System.out.println("I am server: " + i);

            // Create a ServerSocket on the specified port
            ServerSocket ss = new ServerSocket(ports[i]);

            while (true) {
                // Accept client connection
                Socket cs = ss.accept();

                // Create input and output streams for communication with client
                OutputStream os = cs.getOutputStream();
                InputStream is = cs.getInputStream();

                // Create object streams for serialization
                ObjectOutputStream oos = new ObjectOutputStream(os);
                ObjectInputStream ois = new ObjectInputStream(is);

                // Read the requested fragment from the client
                int frag = (int) ois.readObject();
                System.out.println("Fragment " + frag + " requested.");

                // Send the response to the client
                oos.writeObject("Here is fragment " + frag + " served by server " + i + ".");

                // Close the streams and socket
                os.close();
                is.close();
                cs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
