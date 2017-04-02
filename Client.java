package guMeddelandesystem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
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
	private ArrayList<Message> messages;
	private ArrayList<String> users;

	public Client(UI ui) {
		this.ui = ui;
	}

	// -------------------------------------- Metoder som komunuserar med servern

	public void getMessages() {
		try {
			oos.writeUTF("getMsgBuffer");
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void getUsers(){
		
		try {
			oos.writeUTF("getUserReg");
			oos.flush();
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
	
	public void logOut() {
		
	}

	// -------------------------------------

	// --------------------------------------- Metoder som UI använder
	public void setPort(int parseInt) {
		this.port = parseInt;
	}

	public void setUsername(String text) {
		username = text;

	}

	public void connectToServer() {
		try {
			socket = new Socket(this.ip, this.port);
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeUTF(username);
			oos.flush();
			
			new Listener().start();
			getUsers();
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
		 msg.setMessage(ui.getMessageText());
		 msg.setImageIcon((ImageIcon)ui.getImageIcon());
		// for(int i = 0; i < array.size(); i++){
		//
		// }
		 sendMsg(msg); //if (resevers != 0)
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
						System.out.println("ArrayList<?> detected, size: " + users.size());
						ui.updateUserList(users);
					} else if (obj instanceof Message[]) {

					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		UI ui = new UI();
		Client client = new Client(ui);
		ui.startFrame(client);
	}

}