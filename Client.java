package guMeddelandesystem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import javax.swing.*;

public class Client {
	private String ip, name;
	private int port;
	private LinkedList<Object> messages = new LinkedList<Object>();
//	private Client client;
//	private UI ui;
//	
//	public Client() {
//		ui = new UI(this);
//	}
	
	public Client(String ip, int port){
		this.ip = ip;
		this.port = port;
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
//		ui.displayImage(image);
	}
	
	public void connectToServer() {
		//client.connect(this.ip, this.port, this.name);
		
		try{
			Socket socket = new Socket(ip, port);
			
			new GetMessages(socket);
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private class SendMessage extends Thread{
		private Socket socket;
		private ObjectOutputStream oos;
		private ObjectInputStream ois;
		
		
		public SendMessage(Socket socket){
			this.socket = socket;
		}
		public void run(){
			
			
			
		}
	}
	
	private class GetMessages extends Thread{
		private Socket socket;
		private ObjectOutputStream oos;
		private ObjectInputStream ois;
		
		public GetMessages(Socket socket){
			this.socket = socket;
		}
		
		public void run(){
			try{
				while(true){
					oos.writeUTF("getMsgBuffer");
					oos.flush();
					
					if(ois.readObject() instanceof Object[]){
						Object[] newMsgBuffer = (Object[])ois.readObject();
						for(int i = 0; i<newMsgBuffer.length;i++){
							messages.add(newMsgBuffer[i]);
						}
					}
				}
			} catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}
	

}