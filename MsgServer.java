package guMeddelandesystem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class MsgServer {
	private int port;
	private ArrayList<String> userReg = new ArrayList<String>();
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
				System.out.println("Server stoppad");
			}

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
			Message msg;
			String username = new String();
			Object obj;
			// String[] tempUserReg = userReg.toArray();

			try {
				username = (String) ois.readObject();
				userReg.add(username);
//				for (int i = 0; i < userReg.size(); i++) {
//					if (username.equals(userReg.get(i))) {
////						username = (String) obj;
//						System.out.println("Server: Did not add a user");
////						break;
//					} else {
////						if (i == userReg.size() - 1) {
////							username = (String) obj;
//							userReg.add(username);
//							System.out.println("Server: Added user " + username);
////							break;
////						}
//					}
//				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			while (true) {
				try {
					obj = ois.readObject();
					if (obj instanceof Message) {
						msg = (Message) obj;
						msgBuffer.add(msg);
						System.out.println("Server: Message added to buffer");
					}
					if (obj instanceof String) {
						if (obj.equals("getUserReg")) {
							oos.writeObject(userReg);
							System.out.println("Server: User list sent to client");
						}
						if (obj.equals("getMsgBuffer")) {
							Message[] messagesTemp = new Message[msgBuffer.size()];
							for(int i = 0; i < messagesTemp.length; i++) {
								messagesTemp[i] = msgBuffer.get(i);
							}
							
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
										messagesTemp[i + 1] = null;
									}
								} else {
									nbrOfMessages++;
								}
							}
							Message[] messages = new Message[nbrOfMessages];
							for (int i = 0; i < nbrOfMessages; i++) {
								messages[i] = messagesTemp[i];
							}
							oos.writeObject(messages);
							System.out.println("Server: List of messages sent");
						}
						// Tar bort en användare ur userReg som loggar ut i sin
						// klient.
						if (obj.equals("logOut")) {
							System.out.println("Server: Received logOut");
							oos.writeObject("requestUsername");
							System.out.println("Server: Requests username");
							String removeUser = (String) ois.readObject();
							System.out.println("Server: Received username: " + removeUser);
							for (int i = 0; i < userReg.size(); i++) {
								if (userReg.get(i).equals(removeUser)) {
									userReg.remove(i);
									System.out.println("User " + removeUser + " removed");
								}
							}
						}
					}
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// public static void main(String[] args) {
	// new MsgServer(3500);
	// }

}