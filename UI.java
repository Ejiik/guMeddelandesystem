package guMeddelandesystem;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
//		private JLabel lblGreeting = new JLabel("Var god logga in.");
//		private JLabel lblUsername = new JLabel("Användarnamn");
		private JTextField txtFieldUsername = new JTextField("Användarnamn");
		private JButton btnLogin = new JButton("Logga in");
		private JTextField txtFieldIP = new JTextField("IP-nummer");
		private JTextField txtFieldPort = new JTextField("Portnummer");
		private JLabel lblImageViewer = new JLabel();
		
//		private JList listChat = new JList();
//		private JTextArea txtAreaChat = new JTextArea();
//		private JScrollPane scrollChat = new JScrollPane(txtAreaChat);
		private JTextField txtFieldWriteMessage = new JTextField();
		private JButton btnSendMessage = new JButton("Skicka");
		private JButton btnChooseFile = new JButton("Bild");
		private JFileChooser fileChooser = new JFileChooser();
		private JButton btnRemoveImage = new JButton("Ångra bildval");
		
		private JList listUsers = new JList(); //???
		
		private JTextPane textPane = new JTextPane();
		private JScrollPane scrollChat = new JScrollPane(textPane);
		private StyledDocument doc;
		private Client client;
		
		public UI(Client client) {
			this.client = client;
			doc = (StyledDocument) textPane.getDocument();
			setPreferredSize(new Dimension(1200,900));
			setLayout(new BorderLayout());
			add(chatPanel(), BorderLayout.CENTER);
			add(serverPanel(), BorderLayout.EAST);
			add(leftPanel(), BorderLayout.WEST);
			add(login(), BorderLayout.NORTH);
			add(inputPanel(), BorderLayout.SOUTH);
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
			panel.setLayout(new FlowLayout(FlowLayout.LEFT));
//			panel.setBounds(5, 0, panel.getWidth(), panel.getHeight());
			panel.setPreferredSize(new Dimension(1000,40));
			txtFieldUsername.setPreferredSize(new Dimension(200,30));
			txtFieldIP.setPreferredSize(new Dimension(200,30));
			txtFieldPort.setPreferredSize(new Dimension(200,30));
			btnLogin.setPreferredSize(new Dimension(120,30));
			txtFieldIP.setFont(font1);
			txtFieldPort.setFont(font1);
			txtFieldUsername.setFont(font1);
			btnLogin.setFont(font1);
			txtFieldUsername.setFocusable(true);
			panel.add(txtFieldUsername);
			panel.add(txtFieldIP);
			panel.add(txtFieldPort);
			panel.add(btnLogin);
			return panel;
		}
		
		private JPanel chatPanel() {
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout());
			panel.setPreferredSize(new Dimension(900,800));
			scrollChat.setPreferredSize(new Dimension(900,800));
			scrollChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			textPane.setEditable(false);
			
			panel.add(scrollChat);
			return panel;
		}
		
		private JPanel inputPanel() {
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.LEFT));
			panel.setPreferredSize(new Dimension(400,40));
			txtFieldWriteMessage.setPreferredSize(new Dimension(550,30));
			txtFieldWriteMessage.setFont(font1);
			txtFieldWriteMessage.addActionListener(listener);
			btnSendMessage.setPreferredSize(new Dimension(100,30));
			btnChooseFile.setPreferredSize(new Dimension(80,30));
			btnSendMessage.setFont(font1);
			btnChooseFile.setFont(font1);
//			fileChooser.setFileFilter(new ImageFilter());
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG, JPEG, GIF, PNG", "jpg", "gif", "png", "jpeg");
			fileChooser.setFileFilter(filter);
			panel.add(txtFieldWriteMessage);
			panel.add(btnSendMessage);
			panel.add(btnChooseFile);
			return panel;
		}
		
		private JPanel serverPanel() {
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout());
			panel.setPreferredSize(new Dimension(290,850));
			listUsers.setPreferredSize(new Dimension(250,400));
			listUsers.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Användare"));
			lblImageViewer.setPreferredSize(new Dimension(250,250));
			lblImageViewer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			btnRemoveImage.setPreferredSize(new Dimension(160,30));
			btnRemoveImage.setFont(font1);
			panel.add(listUsers);
			panel.add(lblImageViewer);
			panel.add(btnRemoveImage);
			return panel;
		}
		
		private JPanel leftPanel() {
			JPanel panel = new JPanel();
			panel.setPreferredSize(new Dimension(10,900));
			return panel;
		}
		
		public void startFrame(Client client) {
			UI ui = new UI(client);
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(ui);
			frame.pack();
			frame.setVisible(true);
		}
		//Funkar inte just nu?
		public void displayImage(ImageIcon imageIcon) {
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
			//Någon jävla kod som kan visa en tumnagel
		}
	
	
	public static void main (String[] args) {
//		try {
//		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {}
		Client client = new Client();
		UI ui = new UI(client);
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(ui);
		frame.pack();
		frame.setVisible(true);
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
					client.setUsername(txtFieldUsername.getText());
					client.setIP(txtFieldIP.getText());
					client.setPort(Integer.parseInt(txtFieldPort.getText()));
					client.connectToServer();
				} else if(btnLogin.getText().equals("Logga ut")) {
					btnLogin.setText("Logga in");
					txtFieldUsername.setEditable(true);
					txtFieldIP.setEditable(true);
					txtFieldPort.setEditable(true);
					//controller-metod för att nollställa variabler?
				}
			} else if (e.getSource() == btnSendMessage || e.getSource() == txtFieldWriteMessage) {
				if(!txtFieldWriteMessage.getText().equals("") && lblImageViewer.getIcon() == null) {
					append(txtFieldWriteMessage.getText() + "\n");
					txtFieldWriteMessage.setText("");
					System.out.println("Send message");
				} else if (!txtFieldWriteMessage.getText().equals("") && lblImageViewer.getIcon() != null) {
					append(txtFieldWriteMessage.getText() + "\n");
					txtFieldWriteMessage.setText("");
					client.sendImage(filepath);
					lblImageViewer.setIcon(null);
					System.out.println("Send message and or image");
				} else if (txtFieldWriteMessage.getText().equals("") && lblImageViewer.getIcon() != null) {
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
