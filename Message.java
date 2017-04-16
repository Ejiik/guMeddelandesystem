package guMeddelandesystem;

import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 * Message class for storing data with simple get- and set-methods
 * @author Isak Hartman
 *
 */
public class Message implements Serializable {
	private String message;
	private String timeServer, timeClient;
	private ImageIcon imageIcon;
	private String sender;
	private ArrayList<String> receivers; //annan list-typ kanske? 

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTimeRecievedServer() {
		return timeServer;
	}

	public void setTimeRecievedServer(String timeServer) {
		this.timeServer = timeServer;
	}

	public String getTimeReceivedClient() {
		return timeClient;
	}

	public void setTimeReceivedClient(String timeReceived) {
		this.timeClient = timeReceived;
	}

	public ImageIcon getImageIcon() {
		return imageIcon;
	}

	public void setImageIcon(ImageIcon imageIcon) {
		this.imageIcon = imageIcon;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public ArrayList<String> getReceivers() {
		return receivers;
	}

	public void setReceivers(ArrayList<String> receivers) {
		this.receivers = receivers;
	}
	
	public String toString() {
		return timeClient + " | " + sender;
	}

}