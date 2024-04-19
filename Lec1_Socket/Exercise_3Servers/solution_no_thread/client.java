import java.io.*;
import java.net.*;

public class Client {
    final static String hosts[] = {"localhost", "localhost", "localhost"}; 
    final static int ports[] = {8081,8082,8083};
    final static int nb = 3;
    static String document[] = new String[nb];

    public static void main(String[] args) {
        try {
            for (int i = 0; i<nb; i++) {
                Socket cs = new Socket(hosts[i], ports[i]);
                System.out.print("Clinect connecting to host successfully")

                //// OUT OBJ
                // ObjectOutputStream coo = new ObjectOutputStream(cs.getOutputStream());
                // coo.writeInt(i);
                // coo.flush();

                /// OUT BYTES
                OutputStream cso = cs.getOutputStream();
                cso.write(i.toString().getBytes()); // i.byteValue
                cso.flush();
                System.out.print("Request to server");

                /// IN
                ObjectInputStream csi = new ObjectInputStream(cs.getInputStream())
                document[i] = (String) csi.readObject();
                System.out.print("Fragment" + (i+1) + "downloaded.");

                cs.close();


            }


        } catch (e) {

        }
    }
}

// java client 1