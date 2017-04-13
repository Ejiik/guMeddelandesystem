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

	public void setIP(String text) {
		this.ip = text;

	}

	public void createMessage() {
		Message msg = new Message();
		ArrayList<String> receiverslist = ui.getSelectedUsers(); 
		msg.setMessage(ui.getMessageText());
		msg.setImageIcon((ImageIcon)ui.getImageIcon());
		msg.setSender(username);
		msg.setReceivers(receiverslist);
		msg.setTimeSent(dateAndTime());
		sendMsg(msg); //if (resevers != 0)
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
//					getUsers();
					obj = ois.readObject();
					if (obj instanceof ArrayList<?>) {
						users = (ArrayList<String>) obj;
						System.out.println("Client: ArrayList<?> for users received, size: " + users.size());
						ui.updateUserList(users);
					} else if (obj instanceof Message[]) {
						Message[] tempMessagesArray = (Message[]) obj;
						for(int i = 0; i < tempMessagesArray.length; i++) {
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
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

//	public static void main(String[] args) {
//		UI ui = new UI();
//		Client client = new Client(ui);
//		ui.startFrame(client);
//	}

}