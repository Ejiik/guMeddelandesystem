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
		private JTextField txtFieldIP = new JTextField("IP-nummer");
		private JTextField txtFieldPort = new JTextField("Portnummer");
		private JLabel lblImageViewer = new JLabel();
		
		private JTextArea txtAreaWriteMessage = new JTextArea();
//		private JTextField txtFieldWriteMessage = new JTextField();
		private JButton btnSendMessage = new JButton("Skicka meddelande");
		private JButton btnChooseFile = new JButton("Välj Bild");
		private JFileChooser fileChooser = new JFileChooser();
		private JButton btnRemoveImage = new JButton("Ångra bildval");
		private JScrollPane messageScroll = new JScrollPane(txtAreaWriteMessage);
		
		private JList listUsers = new JList(); //???
		private JList listMessages = new JList();
		
		private JScrollPane scrollMessages = new JScrollPane(listMessages);
		private JScrollPane listScroll = new JScrollPane(listUsers);
		private JTabbedPane tabbedPane = new JTabbedPane();
		
		private JTextPane textPane = new JTextPane();
		private JScrollPane scrollChat = new JScrollPane(textPane);
		private StyledDocument doc;
		private Client client;
		
		public UI(Client client) {
			this.client = client;
			doc = (StyledDocument) textPane.getDocument();
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
		
		private JPanel login() {
			JPanel panel = new JPanel();
//			panel.setLayout(new FlowLayout(FlowLayout.LEFT));
//			panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//			panel.setPreferredSize(new Dimension(1000,40));
//			txtFieldUsername.setPreferredSize(new Dimension(200,30));
//			txtFieldIP.setPreferredSize(new Dimension(200,30));
//			txtFieldPort.setPreferredSize(new Dimension(200,30));
//			btnLogin.setPreferredSize(new Dimension(120,30));
//			txtFieldIP.setFont(font1);
//			txtFieldPort.setFont(font1);
//			txtFieldUsername.setFont(font1);
//			btnLogin.setFont(font1);
//			panel.add(txtFieldUsername);
//			panel.add(txtFieldIP);
//			panel.add(txtFieldPort);
//			panel.add(btnLogin);
			return panel;
		}
		
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
//			panel.add(scrollChat);
			panel.add(messageScroll);
			return panel;
		}
		
		private JPanel inputPanel() {
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.LEFT));
			panel.setPreferredSize(new Dimension(1000,190));
			panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			txtAreaWriteMessage.setPreferredSize(new Dimension(800,180));
			txtAreaWriteMessage.setFont(font1);
			txtAreaWriteMessage.setLineWrap(true);
			txtAreaWriteMessage.setWrapStyleWord(true);
//			messageScroll.setPreferredSize(new Dimension(800,180));
			messageScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			panel.add(messageScroll);
			return panel;
		}
		
		private JPanel serverPanel() {
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.LEADING));
			panel.setPreferredSize(new Dimension(290,720));
			panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			listUsers.setPreferredSize(new Dimension(250,200));
			listUsers.setLayoutOrientation(JList.VERTICAL);
			listUsers.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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
//			panel.add(listUsers);
			panel.add(lblImageViewer);
			panel.add(btnChooseFile);
			panel.add(btnRemoveImage);
			panel.add(btnSendMessage);
			return panel;
		}
		
		private JPanel leftPanel() {
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout());
			panel.setPreferredSize(new Dimension(10,720));
			panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			return panel;
		}
		
		public void startFrame(Client client) {
			UI ui = new UI(client);
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
		/**
		 * Add the string input at the last line of the JTextPane chat area.
		 * @param str String to be added to the chat.
		 */
		public void append(String str) {
			try {
			doc.insertString(doc.getLength(), str, null);
			} catch (BadLocationException e) {
				System.err.println(e);
			}
		}
		
		public void displayPreview(ImageIcon imageIcon) {
			//Skulle egentligen vilja att bilden skulle skalas till labeln
			//men kan inte riktigt lista ut hur man gör.
			lblImageViewer.setIcon(imageIcon);
		}
		
		public void openMessage(int index) {
			
		}
		
		public void updateUserList() {
			
		}
		
		public void updateMessageList() {
			
		}
	
		//Den här main-metoden ska givetvis inte vara här sen.
	public static void main (String[] args) {
		Client client = new Client();
		UI ui = new UI(client);
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(ui);
		frame.pack();
		frame.setVisible(true);
	}
	
	private class MouseAdapter implements MouseListener {
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
					//controller-metod för att nollställa variabler?
				}
			} else if (e.getSource() == btnSendMessage) {
				if(!txtAreaWriteMessage.getText().equals("") && lblImageViewer.getIcon() == null) {
//					append(txtAreaWriteMessage.getText() + "\n");
					client.sendMessage(txtAreaWriteMessage.getText());
					txtAreaWriteMessage.setText("");
					System.out.println("Send message");
				} else if (!txtAreaWriteMessage.getText().equals("") && lblImageViewer.getIcon() != null) {
//					append(txtAreaWriteMessage.getText() + "\n");
					client.sendMessage(txtAreaWriteMessage.getText());
					txtAreaWriteMessage.setText("");
					client.sendImage(filepath);
					lblImageViewer.setIcon(null);
					System.out.println("Send message and or image");
				} else if (txtAreaWriteMessage.getText().equals("") && lblImageViewer.getIcon() != null) {
					if(filepath != null) {
					client.sendImage(filepath);
					lblImageViewer.setIcon(null);
					System.out.println("filepath is not null");
					}
				}
			} else if (e.getSource() == btnChooseFile) {
				returnval = fileChooser.showOpenDialog(null);
				if(returnval == JFileChooser.APPROVE_OPTION) {
					filepath = fileChooser.getSelectedFile().getPath();
					displayPreview(new ImageIcon(fileChooser.getSelectedFile().getPath()));
//					lblImageViewer.setIcon(new ImageIcon(fileChooser.getSelectedFile().getPath()));
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
