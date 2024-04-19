import java.io.*;
import java.net.Socket;

public class Slave extends Thread {

    private Socket cs;
    private String file;

    public Slave(Socket s, String file) {
        cs = s;
        this.file = file;
    }

    @Override
    public void run() {
        try {
            OutputStream os = cs.getOutputStream();
            FileInputStream fis = new FileInputStream(file);

            byte[] buffer = new byte[1024];
            int length = fis.read(buffer);
            while (length != -1) {
                os.write(buffer,0,length);
            }
            fis.close();
            cs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
