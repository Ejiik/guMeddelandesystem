package guMeddelandesystem;

import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 * Message class for storing data with simple get- and set-methods
 * @author Isak Hartman, Viktor Ekström, Erik Johansson, Simon Börjesson
 *
 */
public class Message implements Serializable {
	private String message;
	private String timeServer, timeClient;
	private ImageIcon imageIcon;
	private String sender;
	private ArrayList<String> receivers; //annan list-typ kanske? 
	/**
	 * Returns the body text that is the message text written by the sender.
	 * @return message A string containing the message text.
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * Sets the text that is the message sent.
	 * @param message The text to be sent as a message.
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * Returns the time and date when the server received the message.
	 * @return String containing time and date.
	 */
	public String getTimeRecievedServer() {
		return timeServer;
	}
	/**
	 * Set the time and date of when the server receieved the message.
	 * @param timeServer String indicating the time and date.
	 */
	public void setTimeRecievedServer(String timeServer) {
		this.timeServer = timeServer;
	}
	/**
	 * Returns the time and date when the client receieved the message.
	 * @return String containing the time and date.
	 */
	public String getTimeReceivedClient() {
		return timeClient;
	}
	/**
	 * Set the time and date when the client recieved the message.
	 * @param timeReceived String indicating time and date.
	 */
	public void setTimeReceivedClient(String timeReceived) {
		this.timeClient = timeReceived;
	}
	/**
	 * Returns the ImageIcon choosen as the image sent along with the message.
	 * @return The ImageIcon of the message.
	 */
	public ImageIcon getImageIcon() {
		return imageIcon;
	}
	/**
	 * Set the ImageIcon that the message is containing.
	 * @param imageIcon The image of the message.
	 */
	public void setImageIcon(ImageIcon imageIcon) {
		this.imageIcon = imageIcon;
	}
	/**
	 * Returns the sender of the message, that is the username of the sender.
	 * @return String with the username.
	 */
	public String getSender() {
		return sender;
	}
	/**
	 * Set the sender of the message, that is the username of the sender. 
	 * @param sender String representing the sender.
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
	/**
	 * Returns the ArrayList containing all the recipients of this particular message.
	 * Can contain 1 or more receivers.
	 * @return The entire list of receivers.
	 */
	public ArrayList<String> getReceivers() {
		return receivers;
	}
	/**
	 * Sets the recipients of the message by passing an entire list.
	 * @param receivers The list containing the receivers.
	 */
	public void setReceivers(ArrayList<String> receivers) {
		this.receivers = receivers;
	}
	/**
	 * Used to display the desired information in the list of messages so that
	 * users can distinguish one message from another.
	 */
	public String toString() {
		return timeClient + " | " + sender;
	}

}