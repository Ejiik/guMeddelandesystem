package guMeddelandesystem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class MsgServer {
	private int port;
	private LinkedList<String> userReg = new LinkedList<String>();
	private LinkedList<Message> msgBuffer = new LinkedList<Message>();

	public MsgServer(int port) {
		this.port = port;

		try {
			new StartServer(port).start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private class StartServer extends Thread {
		private int port;

		public StartServer(int port) throws InterruptedException {
			this.port = port;
		}

		public void run() {
			Socket socket = null;

			System.out.println("Server startad");
			try {
				ServerSocket serverSocket = new ServerSocket(port);

				while (true) {
					try {
						socket = serverSocket.accept();

						new ClientHandler(socket);

					} catch (IOException e) {
						e.printStackTrace();
						socket.close();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Server stoppad");
		}

	}

	private class ClientHandler extends Thread {
		private Socket socket;
		private ObjectOutputStream oos;
		private ObjectInputStream ois;
		private InetAddress clientAddress;

		public ClientHandler(Socket socket) throws InterruptedException {
			this.socket = socket;
			try {

				this.clientAddress = socket.getInetAddress();
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());

				start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			Message msg = null;
			String username = null;
			String[] tempUserReg = (String[])userReg.toArray();
			
			try{
				for(int i=0; i<tempUserReg.length; i++){
					if(ois.readObject().equals(tempUserReg[i])){
						username = (String)ois.readObject();
						break;
					}else{
						if(i == tempUserReg.length-1){
							username = (String)ois.readObject();
							userReg.add(username);
							break;
						}
					}
						
				}
			} catch(Exception e){
				e.printStackTrace();
			}
			
			while (true) {
				try {
					
					
					if (ois.readObject() instanceof Message) {
						msg = (Message)ois.readObject();
					
						msgBuffer.add(msg);
						
					}

					if (ois.readObject() instanceof String) {
						if (ois.readObject().equals("getMsgBuffer")) {
							Message[] messagesTemp = new Message[msgBuffer.size()];
		
							messagesTemp = (Message[]) msgBuffer.toArray();

							for (int i = 0; i < messagesTemp.length; i++) {
								if (!messagesTemp[i].getReceivers().contains(username)) {
									messagesTemp[i] = null;

								}
							}
							
							int nbrOfMessages = 0;
							
							for (int i = 0; i < messagesTemp.length; i++) {

								if (messagesTemp[i] == null) {
									if (!(i + 1 > messagesTemp.length)) {
										messagesTemp[i] = messagesTemp[i + 1];
										messagesTemp[i+1] = null;
									}
								} else{
									nbrOfMessages++;
								}
							}
							Message[] messages = new Message[nbrOfMessages];
							
							for(int i = 0; i<nbrOfMessages; i++){
								messages[i] = messagesTemp[i];
							}
							oos.writeObject(messages);
							
						} 
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new MsgServer(3500);
	}

}
