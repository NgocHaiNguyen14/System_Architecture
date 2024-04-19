// package video;

import java.rmi.Naming;

public class Client {
    public static void main(String args[]) {
        try {
            Library l = (Library) Naming.lookup("rmi://localhost:8080/library");
            
            // Registering a video
            l.register(new VideoImpl("harrypotter","localhost","9999"));
            System.out.println("Register video successfully");

            // Accessing a video
            Video v = (Video) l.lookup("harrypotter");
            System.out.println("Accessing video: "+v.getTitle()+" "+v.getHost()+" "+v.getPort());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}