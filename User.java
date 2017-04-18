package guMeddelandesystem;

import java.util.ArrayList;

public class User {
	private String username;
	private ArrayList<Message> messages = new ArrayList<Message>();
	
	public User(String username) {
		this.username = username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void addMessage(Message message) {
		messages.add(message);
	}
	
	public ArrayList<Message> getMessages() {
		return messages;
	}
}
