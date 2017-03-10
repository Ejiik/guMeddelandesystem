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
		private JLabel lblImageViewer = new JLabel("TESTEST");
		
//		private JList listChat = new JList();
//		private JTextArea txtAreaChat = new JTextArea();
//		private JScrollPane scrollChat = new JScrollPane(txtAreaChat);
		private JTextField txtFieldWriteMessage = new JTextField();
		private JButton btnSendMessage = new JButton("Skicka");
		private JButton btnChooseFile = new JButton("Bild");
		private JFileChooser fileChooser = new JFileChooser();
		
		private JList listUsers = new JList(); //???
		
		private JTextPane textPane = new JTextPane();
		private JScrollPane scrollChat = new JScrollPane(textPane);
		private StyledDocument doc;
		private Controller controller;
		
		public UI(Controller controller) {
			this.controller = controller;
			doc = (StyledDocument) textPane.getDocument();
			setPreferredSize(new Dimension(1200,900));
			setLayout(new BorderLayout());
			add(Login(), BorderLayout.NORTH);
			add(chatPanel(), BorderLayout.CENTER);
			add(serverPanel(), BorderLayout.EAST);
			addListeners();
		}
		
		public void addListeners() {
			btnLogin.addActionListener(listener); 
			btnChooseFile.addActionListener(listener);
			btnSendMessage.addActionListener(listener);
		}
		
		private JPanel Login() {
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout());
//			setPreferredSize(new Dimension(500,150));
//			lblGreeting.setPreferredSize(new Dimension(300, 30));
//			lblUsername.setPreferredSize(new Dimension(200,30));
			txtFieldUsername.setPreferredSize(new Dimension(200,30));
			txtFieldIP.setPreferredSize(new Dimension(200,30));
			txtFieldPort.setPreferredSize(new Dimension(200,30));
			btnLogin.setPreferredSize(new Dimension(120,30));
//			lblGreeting.setFont(font1);
//			lblUsername.setFont(font1);
			txtFieldIP.setFont(font1);
			txtFieldPort.setFont(font1);
			txtFieldUsername.setFont(font1);
			btnLogin.setFont(font1);
			txtFieldUsername.setFocusable(true);
//			panel.setPreferredSize(new Dimension(500,150));
//			panel.add(lblGreeting);
			
			panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
			panel.add(txtFieldUsername);
			panel.add(txtFieldIP);
			panel.add(txtFieldPort);
			panel.add(btnLogin);
		//Lägg till grejer för servern och port.
			return panel;
		}
		
		private JPanel chatPanel() {
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout());
//			listChat.setPreferredSize(new Dimension(900,500));
			scrollChat.setPreferredSize(new Dimension(900,800));
			scrollChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//			txtAreaChat.setEditable(false);
			textPane.setEditable(false);
			txtFieldWriteMessage.setPreferredSize(new Dimension(550,30));
			txtFieldWriteMessage.setFont(font1);
			txtFieldWriteMessage.addActionListener(listener);
			btnSendMessage.setPreferredSize(new Dimension(100,30));
			btnChooseFile.setPreferredSize(new Dimension(80,30));
			btnSendMessage.setFont(font1);
			btnChooseFile.setFont(font1);
//			panel.add(listChat);
//			fileChooser.setFileFilter(new ImageFilter());
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG, JPEG, GIF, PNG", "jpg", "gif", "png", "jpeg");
			fileChooser.setFileFilter(filter);
			
			panel.add(scrollChat);
			panel.add(txtFieldWriteMessage);
			panel.add(btnSendMessage);
			panel.add(btnChooseFile);
			return panel;
		}
		
		private JPanel serverPanel() {
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout());
			panel.setPreferredSize(new Dimension(300,850));
			listUsers.setPreferredSize(new Dimension(250,400));
			listUsers.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Användare"));
			lblImageViewer.setPreferredSize(new Dimension(200,200));
			lblImageViewer.setBorder(BorderFactory.createLineBorder(Color.BLACK));

			panel.add(listUsers);
			panel.add(lblImageViewer);
			return panel;
		}
		
		public void startFrame(Controller controller) {
			UI ui = new UI(controller);
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
		
		public void append(String str) {
			try {
			doc.insertString(doc.getLength(), str, null);
			} catch (BadLocationException e) {
				System.err.println(e);
			}
		}
	
	
	public static void main (String[] args) {
//		try {
//		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {}
		Controller controller = new Controller();
		UI ui = new UI(controller);
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
					controller.readUsername(txtFieldUsername.getText());
					controller.readIp(txtFieldIP.getText());
					controller.readPort(Integer.parseInt(txtFieldPort.getText()));
					controller.connectToServer();
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
				} else if (!txtFieldWriteMessage.getText().equals("") && lblImageViewer.getIcon() != null) {
					append(txtFieldWriteMessage.getText() + "\n");
					txtFieldWriteMessage.setText("");
					controller.sendImage(filepath);
					lblImageViewer.setIcon(null);
				} else if (txtFieldWriteMessage.getText().equals("") && lblImageViewer.getIcon() != null) {
					controller.sendImage(filepath);
					lblImageViewer.setIcon(null);
				}
			} else if (e.getSource() == btnChooseFile) {
				returnval = fileChooser.showOpenDialog(null);
				if(returnval == JFileChooser.APPROVE_OPTION) {
					filepath = fileChooser.getSelectedFile().getPath();
					lblImageViewer.setIcon(new ImageIcon(fileChooser.getSelectedFile().getPath()));
				}
			}
		}
		
	}

}
