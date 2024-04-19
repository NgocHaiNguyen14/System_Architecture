import java.io.*;
import java.net.*;

public class Server {
    //final static String hosts[] = {"localhost", "localhost", "localhost"}; 
    final static int ports[] = {8081,8082,8083};
    final static int nb = 3;
    static String document[] = {
        "Fragment 1 content",
        "Fragment 2 content",
        "Fragment 3 content"
    };
    public static void main(String[] args) {
        try {
            int me = Integer.parseInt(args[0]);
            ServerSocket ss = new ServerSocket(ports[me]);
            System.out.println("Server is listening at port" + ports[me]);

            while(true) {
                Socket cs = ss.accept();
                System.out.println("Client connecting successfully");

                //// IN OBJ
                // ObjectInputStream soi = new ObjectInputStream(cs.getInputStream());
                // int fragmentNumberObj = soi.readInt();
                // System.out.println("Client request fragment number");

                /// IN BYTES
                InputStream ssi = cs.getInputStream();
                byte[] messBytes = new byte[1024];
                int bytesRead = ssi.read(messBytes);
                String fragmentNumber = new String(messBytes,0,bytesRead);
                System.out.println("Receive client request")
                
                //// OUT
                //OutputStream sfo  = ss.getOutputStream();
                //FileInputStream sfi = new FileInputStream(path);
                ObjectOutputStream soo = new ObjectOutputStream(cs.getOutputStream());
                soo.writeObject(document[fragmentNumber]);
                soo.flush();
                System.out.println("Send exact fragment");

                cs.close();
            }

        }
        catch (e)  {
            //...
        }
    }
}


//

// java server 1 -> localhost:8080