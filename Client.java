package guMeddelandesystem;

import javax.swing.*;

public class Client {
	private String ip, name;
	private int port;
//	private Client client;
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
	
	public void sendImage(String filepath) {
		ImageIcon image = new ImageIcon(filepath);
		ui.displayImage(image);
	}
	
	public void connectToServer() {
		//client.connect(this.ip, this.port, this.name);
	}

}
