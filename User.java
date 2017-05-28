package guMeddelandesystem;

import java.util.ArrayList;
/**
 * Represents a user, containing a username and a list of messages meant for this user.
 * Used by the server to register users.
 * @author Viktor Ekström
 *
 */
public class User {
	private String username;
	private ArrayList<Message> messages = new ArrayList<Message>();
	/**
	 * Initiates an object with a username only. The username is entered in the UI but the object
	 * is created in the server.
	 * @param username Username representing the user.
	 */
	public User(String username) {
		this.username = username;
	}
	/**
	 * Sets the username.
	 * @param username Username replacing the other username.
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * Returns the username of this User object.
	 * @return String containing the username.
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * Adds a message to the list of messages belonging to this user.
	 * @param message Message to be added to the list.
	 */
	public void addMessage(Message message) {
		messages.add(message);
	}
	/**
	 * Returns the entire list of messages belonging to this user.
	 * @return The list of messages.
	 */
	public ArrayList<Message> getMessages() {
		return messages;
	}
}
