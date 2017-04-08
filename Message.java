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
	private String timeSent, timeReceived;
	private ImageIcon imageIcon;
	private String sender;
	private ArrayList<String> receivers; //annan list-typ kanske? 

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTimeSent() {
		return timeSent;
	}

	public void setTimeSent(String timeSent) {
		this.timeSent = timeSent;
	}

	public String getTimeReceived() {
		return timeReceived;
	}

	public void setTimeReceived(String timeReceived) {
		this.timeReceived = timeReceived;
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

}