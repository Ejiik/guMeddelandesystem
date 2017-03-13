package guMeddelandesystem;

import javax.swing.*;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client {
	private String ip, name;
	private int port;
	private UI ui;

	public Client() {
		ui = new UI(this);
	}

	public void setIP(String ip) {
		this.ip = ip;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setUsername(String name) {
		this.name = name;
	}

	/**
	 * Returns time of the day using 24h local time.
	 * 
	 * @return a string on the form "HH:MM:SS"
	 */
	public String getTime() {
		return Calendar.HOUR_OF_DAY + ":" + Calendar.MINUTE + ":" + Calendar.SECOND;
	}

	public void createMessage() {
		Message message = new Message();
		message.setMessage(ui.getMessageText());
		message.setImageIcon((ImageIcon) ui.getImageIcon());
		message.setSender(this.name);
		message.setTimeSent(getTime());

		new SendMessage(message, this.ip, this.port).start();
	}

	public void sendMessage(String message) {
		ui.append(message);
	}

	public void sendImage(String filepath) {
		ImageIcon image = new ImageIcon(filepath);
		JOptionPane.showMessageDialog(null, image);
		ui.displayImage(image);
	}

	public void connectToServer() {

	}

	/**
	 * Inner class that sends a message to the server
	 * 
	 * @author Isak Hartman
	 *
	 */
	private class SendMessage extends Thread {
		private Message message;
		private ObjectOutputStream oos;
		private Socket socket;

		public SendMessage(Message message, String ip, int port) {
			this.message = message;
			try {
				socket = new Socket(ip, port);
				oos = new ObjectOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			try {
				oos.writeObject(message);
				oos.flush();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			try {
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
