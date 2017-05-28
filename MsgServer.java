package guMeddelandesystem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
	private ArrayList<User> users = new ArrayList<User>();
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
	 * Tells all the clienthandelers to update
	 */
	
	public void updateCH(){
		for(ClientHandler ch : chList){
			ch.update();
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
	 * The run method of the thread. Initiates new ClientHandlers when a new
	 * client connects to the server.
	 */
	public void run() {
		System.out.println("Server startad");
		try {
			while (true) {
				try {
					socket = serverSocket.accept();
					new ClientHandler(socket);
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
	 * synchronized get method to user list
	 * @param int	i
	 * @return	User
	 */
	
	private synchronized User getUsers(int i){
		synchronized (users) {
			return (users.get(i));
		}
	}
	
	/**
	 * synchronized add method to user list
	 * @param user	User
	 */
	
	private synchronized void addUsers(User user){
		synchronized (users) {
				users.add(user);
			System.out.println("Fins redan");
		}
	}
	
	/**
	 * synchronized size method to user list
	 * @return	int
	 */
	
	private synchronized int sizeUsers(){
		synchronized (users) {
			return (users.size());
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
		
		String username = new String();

		/**
		 * Initiates the ClientHandler with the socket the server is using.
		 * 
		 * @param socket
		 *            Socket used for the streams.
		 */
		public ClientHandler(Socket socket) {
			this.socket = socket;
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
				for (int i = 0; i < sizeUsers(); i++) {
					if (username.equals(getUsers(i).getUsername())) {
						userInReg = true;
					}
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
			System.out.println("Server: Added " + name() + " to chList");
			
			chList.add(this);
			for(ClientHandler ch : chList){
				System.out.println("name: " + ch.name());
			}
			System.out.println("2");
			updateCH();
			System.out.println("3");

			while (true) {
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
						for (int i = 0; i < sizeUsers(); i++) {
							if (msg.getReceivers().contains(getUsers(i).getUsername())) {
								getUsers(i).addMessage(msg);
							} else {
								addMsgBuffer(msg);
							}
						}
						System.out.println("Server: Message added to buffer");
						updateCH();
					}
					if (obj instanceof String) {
						if (obj.equals("getUserReg")) {
							oos.writeUnshared(userOnline());
							oos.flush();
							System.out.println("Server: User list sent to client");
						}
						if (obj.equals("getMsgBuffer")) {
							int nbrOfMessages = 0;
							for (int i = 0; i < sizeUsers(); i++) {
								if (username.equals(getUsers(i).getUsername())) {
									nbrOfMessages = getUsers(i).getMessages().size();
								}
							}
							Message[] messages = new Message[nbrOfMessages];
							for (int i = 0; i < sizeUsers(); i++) {
								if (getUsers(i).getUsername().equals(username)) {
									for (int j = 0; j < getUsers(i).getMessages().size(); j++) {
										messages[j] = getUsers(i).getMessages().remove(j);
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
							for (int i = 0; i < sizeUserOnline(); i++) {
								if (getUserOnline(i).equals(removeUser)) {
									removeUserOnline(i);
									logger.info("User " + username + " is no longer online.");
									System.out.println("User " + removeUser + " removed");
									interrupt();
								}
							}
							for(int i = 0; i < chList.size(); i++){
								if(chList.get(i).name().equals(removeUser)){
									chList.remove(i);
									System.out.println("Server: Removed " + removeUser + " from chlist");
								}
							}
						}
					}
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		
		/**
		 * Returns the username of the clientHandeler  
		 * @return	String
		 */
		public String name(){
			return username;
		}
		
		/**
		 * Tells the client to update itself
		 */
		public void update(){
		try {
			oos.writeObject("changes");
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
	}
}