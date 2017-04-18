package guMeddelandesystem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
/**
 * A server for a message system. Can handle registering users, accepting messages and distributing
 * these messages to the correct users. 
 * @author Viktor Ekström, Erik Johansson, Simon Börjesson
 *
 */
public class MsgServer extends Thread {
	private Thread thread;
	private ArrayList<String> usersOnline = new ArrayList<String>();
	private LinkedList<Message> msgBuffer = new LinkedList<Message>();
	private ArrayList<User> users = new ArrayList<User>();
	private ServerSocket serverSocket;
	private Socket socket;
	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	private LocalDateTime dateAndTime;
	private final static Logger logger = Logger.getLogger("requests");
	private FileHandler requests;
	/**
	 * Initiates the object with the port the server will listen on.
	 * @param port Port for the ServerSocket.
	 */
	public MsgServer(int port) {
		try {
			serverSocket = new ServerSocket(port);
			requests = new FileHandler("files/requestLog.log");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Method for starting the thread on which the server runs.
	 */
	public void start() {
		if(thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	/**
	 * The run method of the thread. Initiates new ClientHandlers when a new client
	 * connects to the server.
	 */
	public void run() {
		System.out.println("Server startad");
		try {
			while (true) {
				try {
					socket = serverSocket.accept();
					new ClientHandler(socket);
				} catch (IOException e) {
					e.printStackTrace();
					socket.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Method for returning the current date and time of the server in the format
	 * specified by DateTimeFormatter.
	 * @return A String with the current date and time.
	 */
	public String dateAndTime() {
		dateAndTime = LocalDateTime.now();
		String date = dateTimeFormatter.format(dateAndTime);
		return date;
	}
	/**
	 * Inner class handling each client connected to the server and their communciation
	 * with the server. 
	 * @author Viktor Ekström
	 *
	 */
	private class ClientHandler extends Thread {
		private ObjectOutputStream oos;
		private ObjectInputStream ois;
		/**
		 * Initiates the ClientHandler with the socket the server is using.
		 * @param socket Socket used for the streams.
		 */
		public ClientHandler(Socket socket) {
			try {
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
				start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		/**
		 * The run method of the thread. Handles the communcation between the
		 * server and this specific client.
		 */
		public void run() {
			Message msg;
			String username = new String();
			Object obj;
			boolean userInReg = false;
			boolean userOnline = false;
			
			try {
				username = (String) ois.readObject();	
				for(int i = 0; i < usersOnline.size(); i++) {
					if(usersOnline.get(i).equals(username)) {
						userOnline = true;
					}
				}
				if(!userOnline) {
					usersOnline.add(username);
					System.out.println("Server: Added to list of online users");
				} else {
					System.out.println("Server: User already online.");
				}
				for(int i = 0; i < users.size(); i++) {
					if(username.equals(users.get(i).getUsername())) {
						userInReg = true;
					}
				}
				if(!userInReg) {
					users.add(new User(username));
					System.out.println("Server: Added user " + username + " to Users list.");
				} else {
					System.out.println("Server: Did not add user");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			while (true) {
				try {	
					obj = ois.readObject();
					if (obj instanceof Message) {
						msg = (Message) obj;
						msg.setTimeRecievedServer(dateAndTime());
						for(int i = 0; i < users.size(); i++) {
							if(msg.getReceivers().contains(users.get(i).getUsername())) {
								users.get(i).addMessage(msg);
							} else {
								msgBuffer.add(msg);
							}
						}
						System.out.println("Server: Message added to buffer");
					}
					if (obj instanceof String) {
						if (obj.equals("getUserReg")) {
							oos.writeUnshared(usersOnline);
							oos.flush();
							System.out.println("Server: User list sent to client");
						}
						if (obj.equals("getMsgBuffer")) {
							int nbrOfMessages = 0;
							for(int i = 0; i < users.size(); i++) {
								if(username.equals(users.get(i).getUsername())) {
									nbrOfMessages = users.get(i).getMessages().size();
									}
								}
							Message[] messages = new Message[nbrOfMessages];
							for(int i = 0; i < users.size(); i++) {
								if(users.get(i).getUsername().equals(username)) {
									for(int j = 0; j < users.get(i).getMessages().size(); j++) {
										messages[j] = users.get(i).getMessages().remove(j);
									}
								}
							}
							oos.writeObject(messages);
							oos.flush();
							System.out.println("Server: List of messages sent");
						}
						if (obj.equals("logOut")) {
							System.out.println("Server: Received logOut");
							oos.writeObject("requestUsername");
							System.out.println("Server: Requests username");
							String removeUser = (String) ois.readObject();
							System.out.println("Server: Received username: " + removeUser);
							for (int i = 0; i < usersOnline.size(); i++) {
								if (usersOnline.get(i).equals(removeUser)) {
									usersOnline.remove(i);
									System.out.println("User " + removeUser + " removed");
									interrupt();
								}
							}
						}
					}
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
}