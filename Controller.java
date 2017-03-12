package guMeddelandesystem;

import javax.swing.*;

public class Controller {
	private String ip, name;
	private int port;
//	private Client client;
	private UI ui;
	
	public Controller() {
		ui = new UI(this);
	}
	
	public void readIp(String ip) {
		this.ip = ip;
	}
	
	public void readPort(int port) {
		this.port = port;
	}
	
	public void readUsername(String name) {
		this.name = name;
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
		//client.connect(this.ip, this.port, this.name);
	}

}
