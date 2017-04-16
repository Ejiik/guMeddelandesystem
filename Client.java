package guMeddelandesystem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

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

	public Client(UI ui) {
		this.ui = ui;
	}

	// -------------------------------------- Metoder som komunuserar med servern

	public void getMessages() {
		try {
			oos.writeObject("getMsgBuffer");
			oos.flush();
			System.out.println("Client: Wrote getMsgBuffer");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getUsers(){
		try {
			oos.writeObject("getUserReg");
			oos.flush();
			System.out.println("Client: Wrote getUserReg");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMsg(Message msg){
		try{
			oos.writeObject(msg);
			oos.flush();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	//Skickar meddelande till servern om att användaren har loggat ut
	public void logOut() {
		try {
			oos.writeObject("logOut");
			oos.flush();
			System.out.println("Client: Wrote logOut");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// -------------------------------------

	// --------------------------------------- Metoder som UI använder
	public void setPort(int parseInt) {
		this.port = parseInt;
	}
	
	public void setIP(String ip) {
		this.ip = ip;
	}

	public void setUsername(String text) {
		this.username = text;
	}

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
	
	public String dateAndTime() {
		dateAndTime = LocalDateTime.now();
		String date = dateTimeFormatter.format(dateAndTime);
		return date;
	}

	// --------------------------------------

	private class Listener extends Thread {
		private Object obj;

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
						ui.updateMessageList(tempMessagesArray);
					}
					//Om servern ber om användarnamn så skickar klienten ut detta.
					if(obj instanceof String) {
						if(obj.equals("requestUsername")) {
							System.out.println("Client: Server asked username");
							oos.writeObject(username);
							oos.flush();
							System.out.println("Client: Wrote username: " + username);
						}
						if(obj.equals("userListChange")) {
							getUsers();
							System.out.println("Client: Asked for users because notified of change");
						}
						if(obj.equals("msgBufferChange")) {
							getMessages();
							System.out.println("Client: Asked for messages because notified of change");
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