package guMeddelandesystem;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
/**
 * User interface for chat application
 * @author Viktor Ekström
 *
 */

public class UI extends JPanel{
		private Font font1 = new Font("Arial", Font.PLAIN, 22);
		private ButtonListener listener = new ButtonListener();
		private MouseAdapter mouseListener = new MouseAdapter();
		private JTextField txtFieldUsername = new JTextField("Användarnamn");
		private JButton btnLogin = new JButton("Logga in");
		private JTextField txtFieldIP = new JTextField("localhost");
		private JTextField txtFieldPort = new JTextField("3500");
		private JLabel lblImageViewer = new JLabel();
		
		private JTextArea txtAreaWriteMessage = new JTextArea();
		private JButton btnSendMessage = new JButton("Skicka meddelande");
		private JButton btnChooseFile = new JButton("Välj Bild");
		private JFileChooser fileChooser = new JFileChooser();
		private JButton btnRemoveImage = new JButton("Ångra bildval");
		private JScrollPane messageScroll = new JScrollPane(txtAreaWriteMessage);
		
		private JList<String> listUsers = new JList();
		private JList<Message> listMessages = new JList();
		private JLabel lblUserList = new JLabel();
		
		private JScrollPane scrollMessages = new JScrollPane(listMessages);
		private JScrollPane listScroll = new JScrollPane(lblUserList);
		
		private JTextField txtFieldReceiver = new JTextField();
		private JTextPane textPaneMessage = new JTextPane();
		private JScrollPane scrollMessage = new JScrollPane(textPaneMessage);
		
		private JTextPane textPane = new JTextPane();
		private JScrollPane scrollChat = new JScrollPane(textPane);
		private StyledDocument doc;
		private Client client;
		
		public UI() {
			doc = (StyledDocument) textPaneMessage.getDocument();
			setPreferredSize(new Dimension(1100,720));
			setLayout(new BorderLayout());
			add(chatPanel(), BorderLayout.CENTER);
			add(serverPanel(), BorderLayout.EAST);
//			add(leftPanel(), BorderLayout.WEST);
//			add(login(), BorderLayout.NORTH);
//			add(inputPanel(), BorderLayout.SOUTH);
			addListeners();
		}

		public void addListeners() {
			btnLogin.addActionListener(listener); 
			btnChooseFile.addActionListener(listener);
			btnSendMessage.addActionListener(listener);
			btnRemoveImage.addActionListener(listener);
		}
		/**
		 * JPanel containing a list of messages and the box where one writes a message.
		 * @return
		 */
		private JPanel chatPanel() {
//			JPanel panel = new JPanel();
//			panel.setLayout(new FlowLayout());
//			panel.setPreferredSize(new Dimension(900,800));
//			panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//			doc = (StyledDocument) textPane.getDocument();
//			textPane.setFont(font1);
//			scrollChat.setPreferredSize(new Dimension(900,800));
//			scrollChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//			textPane.setPreferredSize(new Dimension(900,800));
//			textPane.setEditable(false);
//			panel.add(scrollChat);
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.LEFT));
			panel.setPreferredSize(new Dimension(800,190));
			panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//			txtAreaWriteMessage.setPreferredSize(new Dimension(800,180));
			txtFieldReceiver.setPreferredSize(new Dimension(800,30));
			txtAreaWriteMessage.setFont(font1);
			txtAreaWriteMessage.setLineWrap(true);
			txtAreaWriteMessage.setWrapStyleWord(true);
			messageScroll.setPreferredSize(new Dimension(800,180));
			messageScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//			textPane.setPreferredSize(new Dimension(800,180));
			textPane.setFont(font1);
//			scrollChat.setPreferredSize(new Dimension(800,180));
//			scrollChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//			scrollChat.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//			panel.add(tabbedPane);
			scrollMessages.setPreferredSize(new Dimension(800,500));
			listMessages.addMouseListener(mouseListener);
			panel.add(scrollMessages);
			panel.add(txtFieldReceiver);
//			panel.add(scrollChat);
			panel.add(messageScroll);
			return panel;
		}
		/**
		 * JPanel containing the input fields for username, ip, and port. Also a list of users
		 * and the preview window for images that should be sent. Also contains buttons for choosing an iamge,
		 * cancelling this choice, and sending the message.
		 * @return
		 */
		private JPanel serverPanel() {
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.LEADING));
			panel.setPreferredSize(new Dimension(290,720));
			panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			lblUserList.setPreferredSize(new Dimension(250,200));
//			listUsers.setPreferredSize(new Dimension(250,200));
//			listUsers.setLayoutOrientation(JList.VERTICAL);
//			listUsers.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//			listUsers.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Användare"));
//			listUsers.addListSelectionListener(listListener);
			listScroll.setPreferredSize(new Dimension(250,200));
			listScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			listScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			lblImageViewer.setPreferredSize(new Dimension(250,250));
			lblImageViewer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			btnRemoveImage.setPreferredSize(new Dimension(220,30));
			btnRemoveImage.setFont(font1);
			txtFieldUsername.setPreferredSize(new Dimension(200,30));
			txtFieldIP.setPreferredSize(new Dimension(200,30));
			txtFieldPort.setPreferredSize(new Dimension(200,30));
			btnLogin.setPreferredSize(new Dimension(120,30));
			txtFieldIP.setFont(font1);
			txtFieldPort.setFont(font1);
			txtFieldUsername.setFont(font1);
			btnLogin.setFont(font1);
			btnSendMessage.setPreferredSize(new Dimension(220,30));
			btnChooseFile.setPreferredSize(new Dimension(220,30));
			btnSendMessage.setFont(font1);
			btnChooseFile.setFont(font1);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG, JPEG, GIF, PNG", "jpg", "gif", "png", "jpeg");
			fileChooser.setFileFilter(filter);
			panel.add(txtFieldUsername);
			panel.add(txtFieldIP);
			panel.add(txtFieldPort);
			panel.add(btnLogin);
			panel.add(listScroll);
			panel.add(lblImageViewer);
			panel.add(btnChooseFile);
			panel.add(btnRemoveImage);
			panel.add(btnSendMessage);
			return panel;
		}
		/**
		 * A JPanel used for filling out the west side of the main frame.
		 * @return panel
		 */
		private JPanel leftPanel() {
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout());
			panel.setPreferredSize(new Dimension(10,720));
			panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			return panel;
		}
		/**
		 * A JPanel defining the appearance of the entire message frame.
		 * @return
		 */
		private JPanel messagePanel() {
			JPanel panel = new JPanel();
			panel.setPreferredSize(new Dimension(900,720));
			scrollMessage.setPreferredSize(new Dimension(900,720));
			panel.add(scrollMessage);
			return panel;
		}
		/**
		 * Displays the UI.
		 * @param client
		 */
		public void startFrame(Client client) {
			this.client = client;
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(this);
			frame.pack();
			frame.setVisible(true);
		}
		//Funkar inte just nu?
		public void displayImage(ImageIcon imageIcon) {
			JOptionPane.showMessageDialog(null, imageIcon);
			textPane.insertIcon(imageIcon);
			append("\n");
		}
		
		public String getMessageText() {
			return txtAreaWriteMessage.getText(); 
		}
		
		public Icon getImageIcon() {
			return lblImageViewer.getIcon();
		}
		
		public int[] getUsersSelected() {
			return listUsers.getSelectedIndices();
		}
		
		/**
		 * Add the string input at the last line of the JTextPane that the StyledDocument is assigned to.
		 * @param str String to be added to the chat.
		 */
		public void append(String str) {
			try {
			doc.insertString(doc.getLength(), str, null);
			} catch (BadLocationException e) {
				System.err.println(e);
			}
		}
		/**
		 * The image that is to be sent is displayed in a small preview window.
		 * @param imageIcon The ImageIcon that is to be displayed.
		 */
		public void displayPreview(ImageIcon imageIcon) {
			//Skulle egentligen vilja att bilden skulle skalas till labeln
			//men kan inte riktigt lista ut hur man gör.
			lblImageViewer.setIcon(imageIcon);
		}
		/**
		 * Opens the message selected from the list of messages in a new frame.
		 * @param index Index in the list of messages, indicating the specific messages to be viewed.
		 */
		public void openMessage(int index) {
			JFrame frame = new JFrame("Meddelande");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(messagePanel());
			frame.pack();
			frame.setVisible(true);
			append(listMessages.getModel().getElementAt(index).getTimeSent());
			append(listMessages.getModel().getElementAt(index).getTimeReceived());
			append(listMessages.getModel().getElementAt(index).getMessage());
			append(listMessages.getModel().getElementAt(index).getSender());
			if(listMessages.getModel().getElementAt(index).getImageIcon() != null) {
				displayImage(listMessages.getModel().getElementAt(index).getImageIcon());
			}
		}
		//Metod för att uppdatera listan med användare? Vet inte om den behövs eller ska vara här.
		public void updateUserList(ArrayList<String> usernames) {
			String tempUser = "";
			for(int i = 0; i < usernames.size(); i++) {
				if(!usernames.get(i).equals(txtFieldUsername.getText()))
				tempUser += usernames.get(i) + "\n";
			}
			lblUserList.setText(tempUser);
		}
		//Metod för att uppdatera listan med meddelande. Vet inte om den behövs eller ska vara här.
		public void updateMessageList(Message[] messages) {
			listMessages.setListData(messages);
		}
	
		//Den här main-metoden ska givetvis inte vara här sen.
//	public static void main (String[] args) {
//		Client client = new Client();
//		UI ui = new UI();
//		JFrame frame = new JFrame("Meddelandesystem");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.add(ui);
//		frame.pack();
//		frame.setVisible(true);
//	}
	/**
	 * Defines the events of mouse clicks on the JList containing messages.
	 * @author Viktor
	 *
	 */
	private class MouseAdapter implements MouseListener {
		/**
		 * Enables a mouse click to register for a particular index in the list of messages.
		 */
		public void mouseClicked(MouseEvent e) {
			listMessages = (JList)e.getSource();
			if(e.getClickCount() == 2) {
				openMessage(listMessages.locationToIndex(e.getPoint()));
			}
		}
		public void mouseEntered(MouseEvent arg0) {	
		}
		public void mouseExited(MouseEvent arg0) {	
		}
		public void mousePressed(MouseEvent arg0) {	
		}
		public void mouseReleased(MouseEvent arg0) {
		}
	}
	
	private class ButtonListener implements ActionListener {
		int returnval = 0;
		String filepath;
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == btnLogin) {
				if(btnLogin.getText().equals("Logga in")) {
					btnLogin.setText("Logga ut");
					txtFieldUsername.setEditable(false);
					txtFieldIP.setEditable(false);
					txtFieldPort.setEditable(false);
					System.out.println("Log in");
					client.setUsername(txtFieldUsername.getText());
					client.setIP(txtFieldIP.getText());
					client.setPort(Integer.parseInt(txtFieldPort.getText()));
					client.connectToServer();

				} else if(btnLogin.getText().equals("Logga ut")) {
					btnLogin.setText("Logga in");
					txtFieldUsername.setEditable(true);
					txtFieldIP.setEditable(true);
					txtFieldPort.setEditable(true);
					System.out.println("Log out");
					client.logOut();
				}
			} else if (e.getSource() == btnSendMessage) {
				if(!txtAreaWriteMessage.getText().equals("") && lblImageViewer.getIcon() == null) {
//					client.sendMessage(txtAreaWriteMessage.getText());
					client.createMessage();
					txtAreaWriteMessage.setText("");
					System.out.println("Send message");
				} else if (!txtAreaWriteMessage.getText().equals("") && lblImageViewer.getIcon() != null) {
//					client.sendMessage(txtAreaWriteMessage.getText());
					client.createMessage();
					txtAreaWriteMessage.setText("");
//					client.sendImage(filepath);
					lblImageViewer.setIcon(null);
					System.out.println("Send message and or image");
				} else if (txtAreaWriteMessage.getText().equals("") && lblImageViewer.getIcon() != null) {
					if(filepath != null) {
						client.createMessage();
//						client.sendImage(filepath);
						lblImageViewer.setIcon(null);
						System.out.println("filepath is not null");
					}
				}
			} else if (e.getSource() == btnChooseFile) {
				returnval = fileChooser.showOpenDialog(null);
				if(returnval == JFileChooser.APPROVE_OPTION) {
					filepath = fileChooser.getSelectedFile().getPath();
					displayPreview(new ImageIcon(fileChooser.getSelectedFile().getPath()));
					System.out.println("Chose image");
				}
			} else if (e.getSource() == btnRemoveImage) {
				lblImageViewer.setIcon(null);
				filepath = null;
				System.out.println("Removed image");
			}
		}
		
	}

}