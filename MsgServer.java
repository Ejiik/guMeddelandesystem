package guMeddelandesystem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.net.ssl.SNIServerName;

/**
 * A server for a message system. Can handle registering users, accepting
 * messages and distributing these messages to the correct users.
 * 
 * @author Viktor Ekström, Erik Johansson, Simon Börjesson
 *
 */
public class MsgServer extends Thread {
	private Thread thread;
	private ArrayList<String> usersOnline = new ArrayList<String>();
	private LinkedList<Message> msgBuffer = new LinkedList<Message>();
	private HashMap<String, User> users = new HashMap<>();
	private ArrayList<ClientHandler> chList = new ArrayList<ClientHandler>();
	private ServerSocket serverSocket;
	private Socket socket;
	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	private LocalDateTime dateAndTime;
	private final static Logger logger = Logger.getLogger("Serverlog");
	private FileHandler log;

	/**
	 * Initiates the object with the port the server will listen on.
	 * 
	 * @param port
	 *            Port for the ServerSocket.
	 */
	public MsgServer(int port) {
		try {
			serverSocket = new ServerSocket(port);
			log = new FileHandler("files/Serverlog.log");
			log.setFormatter(new SimpleFormatter());
			logger.addHandler(log);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method for starting the thread on which the server runs.
	 */
	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	
	/**
	 * Method that tells all client handlers to update their userlist and messages.
	 */
	
	public void uppdateClientHandelers(){
		boolean tempBool;
		if (!chList.isEmpty()){
			for(ClientHandler ch : chList){
				tempBool = false;
				for(String uo : usersOnline){
					if(uo.equals(ch.name())){
						tempBool = true;
					}
				}
				if(tempBool){
					ch.uppdate();
				}
			}
		}
	}

	/**
	 * The run method of the thread. Initiates new ClientHandlers when a new
	 * client connects to the server.
	 */
	public void run() {
		System.out.println("Server startad");
		try {
			while (true) {
				try {
					socket = serverSocket.accept();
					chList.add(new ClientHandler(socket, this));
					logger.info("New connection: " + socket.getLocalAddress());
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
	 * Method for returning the current date and time of the server in the
	 * format specified by DateTimeFormatter.
	 * 
	 * @return A String with the current date and time.
	 */
	public String dateAndTime() {
		dateAndTime = LocalDateTime.now();
		String date = dateTimeFormatter.format(dateAndTime);
		return date;
	}
	
	/**
	 * synchronized get method to userOlnine list
	 * @param x	int
	 * @return	String
	 */
	
	private synchronized String getUserOnline(int x){
		synchronized (usersOnline) {
			return (usersOnline.get(x));
		}
	}
	
	/**
	 * synchronized add method to userOlnine list
	 * @param name	String
	 */
	
	private synchronized void addUserOnline(String name){
		synchronized (usersOnline) {
			usersOnline.add(name);
		}
	}
	
	/**
	 * synchronized remove method to userOlnine list
	 * @param x	int
	 */
	
	private synchronized void removeUserOnline(int x){
		synchronized (usersOnline) {
			usersOnline.remove(x);	
		}
	}
	
	/**
	 * synchronized method to get the hole array
	 * @return	ArrayList <String>
	 */
	private synchronized ArrayList<String> userOnline(){
		synchronized (usersOnline) {
			return (usersOnline);
		}
	}
	
	/**
	 * synchronized size method to userOlnine list
	 * @return	int
	 */
	private synchronized int sizeUserOnline(){
		synchronized (usersOnline) {
			return (usersOnline.size());
		}
	}
	
	/**
	 * synchronized add method to msgBuffer list
	 * @param msg	Message
	 */
	
	private synchronized void addMsgBuffer(Message msg){
		synchronized (msgBuffer) {
			msgBuffer.add(msg);
		}
	}
	
	/**
	 * synchronized get method to user map
	 * @param name	String
	 * @return	User
	 */
	
	private synchronized User getUsers(String name){
		synchronized (users) {
			return (users.get(name));
		}
	}
	
	/**
	 * synchronized put method to user map
	 * @param user	User
	 */
	
	private synchronized void addUsers(User user){
		synchronized (users) {
			if(!users.containsKey(user.getUsername())){
				users.put(user.getUsername(), user);
			}
			System.out.println("Fins redan");
		}
	}
	
	/**
	 * synchronized size method to user map
	 * @return	int
	 */
	
	private synchronized int sizeUsers(){
		synchronized (users) {
			return (users.size());
		}
	}
	
	/**
	 * synchronized containsKey method to user map
	 * @param name
	 * @return
	 */
	
	private synchronized boolean containsUsers(String name){
		synchronized (users) {
			return (users.containsKey(name));
		}
	}

	/**
	 * Inner class handling each client connected to the server and their
	 * communciation with the server.
	 * 
	 * @author Viktor Ekström
	 *
	 */
	private class ClientHandler extends Thread {
		private ObjectOutputStream oos;
		private ObjectInputStream ois;
		private Socket socket;
		private MsgServer server;
		
		private String username;

		/**
		 * Initiates the ClientHandler with the socket the server is using.
		 * 
		 * @param	socket		Socket used for the streams.
		 * @param	server		MsgServer 
		 */
		public ClientHandler(Socket socket, MsgServer server) {
			this.socket = socket;
			this.server = server;
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
			Object obj;
			boolean userInReg = false;
			boolean userOnline = false;

			try {
				username = (String) ois.readObject();
				for (int i = 0; i < sizeUserOnline(); i++) {
					if (getUserOnline(i).equals(username)) {
						userOnline = true;
					}
				}
				if (!userOnline) {
					addUserOnline(username);
					logger.info("User " + username + " is online.");
					System.out.println("Server: Added to list of online users");
				} else {
					System.out.println("Server: User already online.");
				}
//				for (int i = 0; i < sizeUsers(); i++) {
//					if (username.equals(getUsers(i).getUsername())) {
//						userInReg = true;
//					}
//				}
				if (containsUsers(username)){
					userInReg = true;
				}
				if (!userInReg) {
					addUsers(new User(username));
					System.out.println("Server: Added user " + username + " to Users list.");
					logger.info("New user: " + socket.getLocalAddress() + " is user " + username);
				} else {
					System.out.println("Server: Did not add user");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			server.uppdateClientHandelers();
			
			while (!Thread.interrupted()) {
				try {
					obj = ois.readObject();
					if (obj instanceof Message) {
						msg = (Message) obj;
						msg.setTimeRecievedServer(dateAndTime());
						if (msg.getImageIcon() == null) {
							logger.info("Server receieved message with body: " + msg.getMessage());
						} else {
							logger.info("Server receieved message with body: " + msg.getMessage() + ". And image: "
									+ msg.getImageIcon().getDescription());
						}
//						for (int i = 0; i < sizeUsers(); i++) {
//							if (msg.getReceivers().contains(getUsers(i).getUsername())) {
//								getUsers(i).addMessage(msg);
//							} else {
//								addMsgBuffer(msg);
//							}
//						}
						for (int i = 0; i < msg.getReceivers().size(); i++){
							getUsers(msg.getReceivers().get(i)).addMessage(msg);
						}
						System.out.println("Server: Message added to buffer");
						server.uppdateClientHandelers();
					}
					if (obj instanceof String) {
						if (obj.equals("getUserReg")) {
							oos.writeUnshared(userOnline());
							oos.flush();
							System.out.println("Server: User list sent to client");
						}
						if (obj.equals("getMsgBuffer")) {
							int nbrOfMessages = 0;
//							for (int i = 0; i < sizeUsers(); i++) {
//								if (username.equals(getUsers(i).getUsername())) {
//									nbrOfMessages = getUsers(i).getMessages().size();
//								}
//							}
							nbrOfMessages = getUsers(username).getMessages().size();
									
							Message[] messages = new Message[nbrOfMessages];
//							for (int i = 0; i < sizeUsers(); i++) {
//								if (getUsers(i).getUsername().equals(username)) {
//									for (int j = 0; j < getUsers(i).getMessages().size(); j++) {
//										messages[j] = getUsers(i).getMessages().remove(j);
//									}
//								}
//							}
							for (int j = 0; j < getUsers(username).getMessages().size(); j++) {
								messages[j] = getUsers(username).getMessages().remove(j);
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
							for (int i = 0; i < sizeUserOnline(); i++) {
								if (getUserOnline(i).equals(removeUser)) {
									removeUserOnline(i);
									logger.info("User " + username + " is no longer online.");
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
		
		private String name(){
			return username;
		}
		
		/**
		 * Send out "changes" to the client making the client to triggering an update to its usersonline list
		 */
		
		public void uppdate(){
			try {
				oos.writeObject("changes");
				oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
