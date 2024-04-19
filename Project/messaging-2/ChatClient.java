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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.*;
import java.net.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.FileOutputStream;

public class ChatClient {
    
	public static TextArea		text;
	public static TextField		data;
	public static Frame 		frame;
	public static Boolean 		isEntered = false;

	public static Vector<String> users = new Vector<String>();
	public static String myName;
	public static String serverAddress;
	public static int serverPort;

    public static ChatServer server;
    public static ChatClientCallback clientCallback;

	private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;

	public static void main(String argv[]) {

		if (argv.length != 3) {
            System.out.println("java ChatClient <name> <client_server_address> <client_server_port>");
            return;
        }
		myName = argv[0];
		serverAddress = argv[1];
		serverPort = Integer.parseInt(argv[2]);

        // Locate the RMI registry and obtain a reference to the server object
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            server = (ChatServer) registry.lookup("ChatServer");
        } catch (Exception e) {
            e.printStackTrace();
        }

		try {
			clientCallback = new ChatClientCallbackImpl(myName, serverAddress, serverPort);
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

		Button upload_button = new Button("upload");
		upload_button.addActionListener(new UploadListener());
		frame.add(upload_button);

		frame.setSize(470,300);
		text.setBackground(Color.black); 
		frame.setVisible(true);

        ChatClient.text.append("Welcome to group chat!");
		System.out.println("Connect at socket:"+myName+" at "+serverAddress+"/"+serverPort);
	}

    ///////// SEND REQUEST TO SERVER /////////
	public static void enter(String username) {
        try {
            server.enter(username, serverAddress, serverPort, clientCallback);
			isEntered = true;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
	
	public static void leave(String username) {
		ChatClient.text.append("You left the chat");
        try {
            server.leave(username, serverAddress, serverPort, clientCallback);
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

	public static void attach(String username, String filename, String filepath, String serverAddress, int serverPort) {
		// Send information of sender to RMI server
		if (!isEntered) {
			print("admin", " you have to enter to attach file.");
			return;
		} 
        try {
            server.attach(username, filename, filepath, serverAddress, serverPort);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
		// Socket
		try {
            // Open a ServerSocket from sender
			// Make sure it use same address + port
			InetSocketAddress serverSocketAddress = new InetSocketAddress(serverAddress, serverPort);
            ServerSocket serverSocket = new ServerSocket();
			serverSocket.bind(serverSocketAddress);

			System.out.println("Host: " + serverSocketAddress.getHostName());
			System.out.println("Port: " + serverSocketAddress.getPort());
			System.out.println("Full address: " + serverSocketAddress);

            System.out.println("Server socket opened on port " + serverPort + ". Waiting for connections...");

            // Accept connections from other clients
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connection accepted from " + clientSocket.getInetAddress());

            // Save file to buffer
            File file = new File(filepath);
            byte[] buffer = new byte[(int) file.length()];
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(buffer, 0, buffer.length);

			// Send the file to the client
            OutputStream os = clientSocket.getOutputStream();
            os.write(buffer, 0, buffer.length);
            os.flush();
            System.out.println("File sent successfully.");

            // Close sockets and streams
            bis.close();
            fis.close();
            os.close();
            clientSocket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

	public static void download(String senderAddress, int senderPort, String filename) {
		System.out.println("System initiating download "+filename+" from "+senderAddress+":"+senderPort);
		ChatClient.text.append("System initiating download "+filename+" from "+senderAddress+":"+senderPort);
        try {
            // Establish connection with the sender
            Socket socket = new Socket(senderAddress, senderPort);
			System.out.println("Connected to server.");

            // // Get input stream to read the file content from the socket
            // InputStream inputStream = socket.getInputStream();
            // // Create file output stream to write the received file content to a file
            // FileOutputStream fileOutputStream = new FileOutputStream(filename);
            // // Create a buffer to store data chunks
            // byte[] buffer = new byte[1024];
            // int bytesRead;
            // // Read from input stream and write to the file
            // while ((bytesRead = inputStream.read(buffer)) != -1) {
            //     fileOutputStream.write(buffer, 0, bytesRead);
            // }
            // // Close streams and socket
            // fileOutputStream.close();
            // inputStream.close();
            // socket.close();

			socket.close();
            System.out.println("Connection closed.");

            System.out.println("File downloaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
}

    ///////// GUI LISTENER /////////
	// action invoked when the "upload" button is clicked
	class UploadListener implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String filepath = selectedFile.getAbsolutePath();
				String filename = selectedFile.getName();
                ChatClient.text.append("\nFile attached:" + filename);
                ChatClient.attach(
					ChatClient.myName, 
					filename, 
					filepath, 
					ChatClient.serverAddress, 
					ChatClient.serverPort
				);
            }
        }
    }

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


