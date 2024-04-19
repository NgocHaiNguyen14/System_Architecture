import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Vector;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class ChatClient {
    
	public static TextArea		text;
	public static TextField		data;
	public static Frame 		frame;

	public static Vector<String> users = new Vector<String>();
	public static String myName;

    public static ChatServer server;
    public static ChatClientCallback clientCallback;

	public static void main(String argv[]) {

		if (argv.length != 1) {
			System.out.println("java ChatClient <name>");
			return;
		}
		myName = argv[0];

        // Locate the RMI registry and obtain a reference to the server object
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            server = (ChatServer) registry.lookup("ChatServer");
        } catch (Exception e) {
            e.printStackTrace();
        }

		try {
			clientCallback = new ChatClientCallbackImpl(myName);
		} catch (Exception e) {
            e.printStackTrace();
        }

		// creation of the GUI 
		frame=new Frame();
		frame.setLayout(new FlowLayout());

		text=new TextArea(10,55);
		text.setEditable(false);
		text.setForeground(Color.red);
		frame.add(text);

		data=new TextField(55);
		frame.add(data);

		Button write_button = new Button("write");
		write_button.addActionListener(new WriteListener());
		frame.add(write_button);

		Button enter_button = new Button("enter");
		enter_button.addActionListener(new EnterListener());
		frame.add(enter_button);

		Button who_button = new Button("who");
		who_button.addActionListener(new WhoListener());
		frame.add(who_button);

		Button leave_button = new Button("leave");
		leave_button.addActionListener(new LeaveListener());
		frame.add(leave_button);

		frame.setSize(470,300);
		text.setBackground(Color.black); 
		frame.setVisible(true);

        ChatClient.text.append("Welcome to group chat!");
	}

    ///////// SEND REQUEST TO SERVER /////////
	public static void enter(String username) {
        try {
            server.enter(username, clientCallback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
	
	public static void leave(String username) {
		ChatClient.text.append("You left the chat");
        try {
            server.leave(username, clientCallback);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	
    public static void who() {
        try {
            // Get the list of connected users from the server
            String[] connectedUsers = server.who();
        
            // Display the list of connected users in the chat window
            if (connectedUsers.length > 0) {
                StringBuilder userList = new StringBuilder("Users currently in the chatroom:\n");
                for (String user : connectedUsers) {
                    userList.append(user).append("\n");
                }
                print("admin", userList.toString());
            } else {
                print("admin", "No users currently in the chatroom.");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
	
	public static void write(String username, String text) {
		print(username, text);
        try {
            server.write(username, text);
        } catch (RemoteException e) {
            e.printStackTrace();
        }	
	}


    ////////////////////////////////////////////////
    // User print
	public static void print(String username, String text) {
		try {
			ChatClient.text.append(username+" says : "+text+"\n");
		} catch (Exception ex) {
			ex.printStackTrace();
		}	
	}
}
    ///////// GUI LOGIC /////////
	// action invoked when the "write" button is clicked
	class WriteListener implements ActionListener {
		public void actionPerformed (ActionEvent ae) {
			try {
				ChatClient.write(ChatClient.myName, ChatClient.data.getText());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	// action invoked when the "connect" button is clicked
	class EnterListener implements ActionListener {
		public void actionPerformed (ActionEvent ae) {
			try {  
				ChatClient.enter(ChatClient.myName);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}  

	// action invoked when the "who" button is clicked
	class WhoListener implements ActionListener {
		public void actionPerformed (ActionEvent ae) {
			try {
				ChatClient.who();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	// action invoked when the "leave" button is clicked
	class LeaveListener implements ActionListener {
		public void actionPerformed (ActionEvent ae) {
			try {
				ChatClient.leave(ChatClient.myName);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}


