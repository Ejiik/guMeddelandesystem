package guMeddelandesystem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.ImageIcon;
/**
 * Client class for communicating with the messages server. Can connect to server and receiever
 * list of online users. Can send and receive messages.
 * @author Viktor Ekström, Erik Johansson, Simon Börjesson
 *
 */
public class Client {
	private UI ui;
	private int port;
	private String ip;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private String username;
	private ArrayList<Message> messages = new ArrayList<Message>();
	private ArrayList<String> users = new ArrayList<String>();
	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	private LocalDateTime dateAndTime;
/**
 * Constructor requiring a passed UI used for interacting with the system.
 * @param ui The UI model to be used.
 */
	public Client(UI ui) {
		this.ui = ui;
	}

/**
 * Will send a request to server asking for the list of messages belonging to this user.
 */
	public void getMessages() {
		try {
			oos.writeObject("getMsgBuffer");
			oos.flush();
			System.out.println("Client: Wrote getMsgBuffer");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
/**
 * Will send a request to server asking for the list of online users.
 */
	public void getUsers(){
		try {
			oos.writeObject("getUserReg");
			oos.flush();
			System.out.println("Client: Wrote getUserReg");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Used to send a message to the server, meant for some other user. The message is of Message type and 
	 * created elsewhere but is passed as a parameter.  
	 * @param msg Message to be sent.
	 */
	public void sendMsg(Message msg){
		try{
			oos.writeObject(msg);
			oos.flush();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * Sends the server a message that the user has logged out of the system.
	 */
	public void logOut() {
		try {
			oos.writeObject("logOut");
			oos.flush();
			System.out.println("Client: Wrote logOut");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Sets the port the server is listening on. Used when connecting to the server.
	 * @param port Port server is listening on.
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * Sets the ip for the server the client wants to connect to.
	 * @param ip The servers ip.
	 */
	public void setIP(String ip) {
		this.ip = ip;
	}
	/**
	 * The choosen username of the client, displayed for all other users on the system
	 * and used as a receiver and sender name when sending messages.
	 * @param text
	 */
	public void setUsername(String text) {
		this.username = text;
	}
	/**
	 * Connects to the server that was choosen in the UI, depending on input. Called when logging in
	 * on the UI. 
	 * Will tell the server what username is choosen and starts the thread that will listen for
	 * messages from the server. Asks for users and messages to be displayed immediately.
	 */
	public void connectToServer() {
		try {
			socket = new Socket(this.ip, this.port);
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(username);
			oos.flush();
			new Listener().start();
			getUsers();
			getMessages();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Will create a new message with the information added from the UI. The message will be
	 * sent as long as it has any receivers added.
	 */
	public void createMessage() {
		Message msg = new Message();
		ArrayList<String> receiverslist = ui.getSelectedUsers(); 
		msg.setMessage(ui.getMessageText());
		msg.setImageIcon((ImageIcon)ui.getImageIcon());
		msg.setSender(username);
		msg.setReceivers(receiverslist);
		if(!receiverslist.isEmpty()) {
			sendMsg(msg);
		}
	}
	/**
	 * Gets the current date and time of day. Used for noting when a message was received by the client.
	 * @return date The date and time of day by the format defined.
	 */
	public String dateAndTime() {
		dateAndTime = LocalDateTime.now();
		String date = dateTimeFormatter.format(dateAndTime);
		return date;
	}
	/**
	 * The thread that handles all communciation with the server. Will listen for new objects
	 * and handle them appropriately. Usually meaning updating the UI with the new information.
	 * @author Viktor Ekström, Simon Börjesson, Erik Johansson
	 *
	 */
	private class Listener extends Thread {
		private Object obj;
		/**
		 * The run method of the thread.
		 */
		public void run() {
			try {
				while (true) {
					obj = ois.readObject();
					if (obj instanceof ArrayList<?>) {
						users = (ArrayList<String>) obj;
						System.out.println("Client: ArrayList<?> for users received, size: " + users.size());
						String[] tempUser = new String[users.size()];
						for(int i = 0; i < users.size(); i++) {
							if(!users.get(i).equals(username))
							tempUser[i] = users.get(i);
						}
						ui.updateUserList(tempUser);
					} else if (obj instanceof Message[]) {
						Message[] tempMessagesArray = (Message[]) obj;
						for(int i = 0; i < tempMessagesArray.length; i++) {		
							if(tempMessagesArray[i].getTimeReceivedClient() == null) {
								tempMessagesArray[i].setTimeReceivedClient(dateAndTime());
							}
							messages.add(tempMessagesArray[i]);
						}
						System.out.println("Client: ArrayList for message buffer received, size: " + messages.size());
						Message[] listMessages = new Message[messages.size()];
						for(int i = 0; i < messages.size(); i++) {
							listMessages[i] = messages.get(i);
						}
						ui.updateMessageList(listMessages);
					}
					if(obj instanceof String) {
						if(obj.equals("requestUsername")) {
							System.out.println("Client: Server asked username");
							oos.writeObject(username);
							oos.flush();
							System.out.println("Client: Wrote username: " + username);
						}
						if(obj.equals("changes")) {
							System.out.println("Client " + username +": Notified of user changed");
							getUsers();
							getMessages();
						}
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}