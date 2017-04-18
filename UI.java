package guMeddelandesystem;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
 * 
 * @author Viktor Ekström
 *
 */
public class UI extends JPanel {
	private Font font1 = new Font("Arial", Font.PLAIN, 22);
	private ButtonListener listener = new ButtonListener();
	private MouseAdapter mouseListener = new MouseAdapter();
	private JTextField txtFieldUsername = new JTextField("Användarnamn");
	private JButton btnLogin = new JButton("O");
	private JTextField txtFieldIP = new JTextField("localhost");
	private JTextField txtFieldPort = new JTextField("3500");
	private JLabel lblImageViewer = new JLabel();
	private JTextArea txtAreaWriteMessage = new JTextArea();
	private JButton btnSendMessage = new JButton("Skicka meddelande");
	private JButton btnChooseFile = new JButton("Välj Bild");
	private JFileChooser fileChooser = new JFileChooser();
	private JButton btnRemoveImage = new JButton("Ångra bildval");
	private JScrollPane messageScroll = new JScrollPane(txtAreaWriteMessage);
	private JButton btnUpdateMessages = new JButton("Hämta meddelande");
	private JButton btnUpdateUsers = new JButton("Hämta användare");
	private JList<String> listUsers = new JList();
	private JList<Message> listMessages = new JList();
	private JScrollPane scrollMessages = new JScrollPane(listMessages);
	private JScrollPane listScroll = new JScrollPane(listUsers);
	private JTextPane textPane = new JTextPane();
	private JScrollPane scrollChat = new JScrollPane(textPane);
	private StyledDocument doc;
	private Style style;
	private Client client;
	/**
	 * Constructor for the UI. Does nothing special.
	 */
	public UI() {
		setPreferredSize(new Dimension(1100, 720));
		setLayout(new BorderLayout());
		add(chatPanel(), BorderLayout.CENTER);
		add(serverPanel(), BorderLayout.EAST);
		addListeners();
	}
	/**
	 * Add listeners for all buttons at once.
	 */
	public void addListeners() {
		btnLogin.addActionListener(listener);
		btnChooseFile.addActionListener(listener);
		btnSendMessage.addActionListener(listener);
		btnRemoveImage.addActionListener(listener);
		btnUpdateMessages.addActionListener(listener);
		btnUpdateUsers.addActionListener(listener);
	}

	/**
	 * JPanel containing a list of messages and the box where a message is written.
	 * @return panel The panel that is to be added to the frame.
	 */
	private JPanel chatPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.setPreferredSize(new Dimension(800, 190));
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		txtAreaWriteMessage.setFont(font1);
		txtAreaWriteMessage.setLineWrap(true);
		txtAreaWriteMessage.setWrapStyleWord(true);
		messageScroll.setPreferredSize(new Dimension(800, 180));
		messageScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		textPane.setFont(font1);
		scrollMessages.setPreferredSize(new Dimension(800, 500));
		listMessages.addMouseListener(mouseListener);
		panel.add(scrollMessages);
		panel.add(messageScroll);
		return panel;
	}

	/**
	 * JPanel containing the input fields for username, ip, and port. Also a
	 * list of users and the preview window for images that should be sent. Also
	 * contains buttons for choosing an image, cancelling this choice, and
	 * sending the message.
	 * 
	 * @return panel The panel to be added to a frame.
	 */
	private JPanel serverPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEADING));
		panel.setPreferredSize(new Dimension(290, 720));
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		listUsers.setLayoutOrientation(JList.VERTICAL);
		listUsers.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		listUsers.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Användare"));
		listScroll.setPreferredSize(new Dimension(250, 150));
		listScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		listScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		lblImageViewer.setPreferredSize(new Dimension(250, 250));
		lblImageViewer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		btnRemoveImage.setPreferredSize(new Dimension(220, 30));
		btnRemoveImage.setFont(font1);
		txtFieldUsername.setPreferredSize(new Dimension(200, 30));
		txtFieldIP.setPreferredSize(new Dimension(200, 30));
		txtFieldPort.setPreferredSize(new Dimension(200, 30));
		btnLogin.setPreferredSize(new Dimension(50, 30));
		txtFieldIP.setFont(font1);
		txtFieldPort.setFont(font1);
		txtFieldUsername.setFont(font1);
		btnUpdateMessages.setPreferredSize(new Dimension(250, 30));
		btnUpdateUsers.setPreferredSize(new Dimension(250, 30));
		btnUpdateMessages.setFont(font1);
		btnUpdateUsers.setFont(font1);
		btnSendMessage.setPreferredSize(new Dimension(220, 30));
		btnChooseFile.setPreferredSize(new Dimension(220, 30));
		btnSendMessage.setFont(font1);
		btnChooseFile.setFont(font1);
		btnUpdateUsers.setEnabled(false);
		btnUpdateMessages.setEnabled(false);
		btnSendMessage.setEnabled(false);
		btnChooseFile.setEnabled(false);
		btnRemoveImage.setEnabled(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG, JPEG, GIF, PNG","jpg","gif","png","jpeg");
		fileChooser.setFileFilter(filter);
		panel.add(txtFieldUsername);
		panel.add(btnLogin);
		panel.add(txtFieldIP);
		panel.add(txtFieldPort);
		panel.add(btnUpdateMessages);
		panel.add(btnUpdateUsers);
		panel.add(listScroll);
		panel.add(lblImageViewer);
		panel.add(btnChooseFile);
		panel.add(btnRemoveImage);
		panel.add(btnSendMessage);
		return panel;
	}

	/**
	 * A JPanel defining the appearance of the entire message frame. 
	 * @return panel The panel that is to be added to the frame.
	 */
	private JPanel messagePanel() {
		JPanel panel = new JPanel();
		JTextPane textPaneMessage = new JTextPane();
		JScrollPane scrollMessage = new JScrollPane(textPaneMessage);
		doc = (StyledDocument) textPaneMessage.getDocument();
		style = doc.addStyle("AddImage", null);
		textPaneMessage.setFont(font1);
		textPaneMessage.setEditable(false);
		panel.setPreferredSize(new Dimension(900, 720));
		scrollMessage.setPreferredSize(new Dimension(900, 720));
		panel.add(scrollMessage);
		return panel;
	}

	/**
	 * Creates and starts the frame, showing the UI.
	 * @param client A client to communicate with.
	 */
	public void startFrame(Client client) {
		this.client = client;
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.pack();
		frame.setVisible(true);
	}
	/**
	 * Displays the image in a message in the message window. Will be added at the bottom, after any text.
	 * @param imageIcon The image to be displayed.
	 */
	public void displayImage(ImageIcon imageIcon) {
		StyleConstants.setIcon(style, imageIcon);
		try {
			doc.insertString(doc.getLength(), "blabla", style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
//		textPane.insertIcon(imageIcon);
	}

	/**
	 * Returns the text written for a message. Used by the client when creating a message object.
	 * @return Returns any text found in the JTextArea.
	 */
	public String getMessageText() {
		return txtAreaWriteMessage.getText();
	}
	/**
	 * Return the image choosen for a message. Used by the client when creating a message ojbect.
	 * @return Any Icon found in the preview JLabel.
	 */
	public Icon getImageIcon() {
		return lblImageViewer.getIcon();
	}
	/**
	 * Returns a list of those users selected as reciepients of a written message. They are chosen
	 * from the JList containing online users. Used by the client when creating a message object.
	 * @return Returns a list cointaining the selected items in the userlist. In this case usernames as String.
	 */
	public ArrayList<String> getSelectedUsers() {
		return (ArrayList<String>) listUsers.getSelectedValuesList();
	}

	/**
	 * Adds the passed string as text in the message window. This includes message text and times, sender, etc.
	 * @param str String to be added to the message JTextPane.
	 */
	public void append(String str) {
		try {
			doc.insertString(doc.getLength(), str, null);
		} catch (BadLocationException e) {
			System.err.println(e);
		}
	}

	/**
	 * The image that is to be sent is displayed in a small preview JLabel.
	 * @param imageIcon The ImageIcon that is to be displayed.
	 */
	public void displayPreview(ImageIcon imageIcon) {
		lblImageViewer.setIcon(imageIcon);
	}

	/**
	 * Opens the message selected from the list of messages in a new frame.
	 * @param index Index in the list of messages, indicating the specific messages to be viewed.
	 */
	public void openMessage(int index) {
		JFrame frame = new JFrame("Meddelande");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(messagePanel());
		frame.pack();
		frame.setVisible(true);
		append("Meddelande mottaget av server: " + listMessages.getModel().getElementAt(index).getTimeRecievedServer()
				+ "\n");
		append("Meddelande mottaget av klient: " + listMessages.getModel().getElementAt(index).getTimeReceivedClient()
				+ "\n");
		append("Från: " + listMessages.getModel().getElementAt(index).getSender() + "\n");
		append("\n" + listMessages.getModel().getElementAt(index).getMessage() + "\n");
		if (listMessages.getModel().getElementAt(index).getImageIcon() != null) {
			displayImage(listMessages.getModel().getElementAt(index).getImageIcon());
		}
	}

	/**
	 * Updates the list of online users with the passed array. From this list the user can
	 * choose who a message should be sent to.
	 * @param users The users that will be displayed in the list.
	 */
	public void updateUserList(String[] users) {
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					listUsers.setListData(users);
				}
			});
		}
		System.out.println("UI: Updated userlist");
	}

	/**
	 * Updates the list of messages that the current logged in user has been sent.
	 * Each messages in the list can be clicked to be viewed in full.
	 * @param messages The array of messages to be displayed.
	 */
	public void updateMessageList(Message[] messages) {
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					listMessages.setListData(messages);
				}
			});
		}
		System.out.println("UI: Messages updated");
	}

	/**
	 * Defines the events of mouse clicks on the JList containing messages.
	 * @author Viktor Ekström
	 */
	private class MouseAdapter implements MouseListener {
		/**
		 * Enables a mouse click to register for a particular index in the list
		 * of messages.
		 */
		public void mouseClicked(MouseEvent e) {
			listMessages = (JList) e.getSource();
			if (e.getClickCount() == 2) {
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
	/**
	 * Handles the different events associated with all buttons in the UI.
	 * @author Viktor Ekström
	 */
	private class ButtonListener implements ActionListener {
		int returnval = 0;
		String filepath;

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnLogin) {
				if (btnLogin.getText().equals("O")) {
					btnLogin.setText("X");
					txtFieldUsername.setEditable(false);
					txtFieldIP.setEditable(false);
					txtFieldPort.setEditable(false);
					btnSendMessage.setEnabled(true);
					btnChooseFile.setEnabled(true);
					btnRemoveImage.setEnabled(true);
					btnUpdateUsers.setEnabled(true);
					btnUpdateMessages.setEnabled(true);
					System.out.println("UI: Log in");
					client.setUsername(txtFieldUsername.getText());
					client.setIP(txtFieldIP.getText());
					client.setPort(Integer.parseInt(txtFieldPort.getText()));
					client.connectToServer();
				} else if (btnLogin.getText().equals("X")) {
					btnLogin.setText("O");
					txtFieldUsername.setEditable(true);
					txtFieldIP.setEditable(true);
					txtFieldPort.setEditable(true);
					btnSendMessage.setEnabled(false);
					btnChooseFile.setEnabled(false);
					btnRemoveImage.setEnabled(false);
					btnUpdateUsers.setEnabled(false);
					btnUpdateMessages.setEnabled(false);
					System.out.println("UI: Log out");
					client.logOut();
				}
			} else if (e.getSource() == btnSendMessage) {
				if (!txtAreaWriteMessage.getText().equals("") && lblImageViewer.getIcon() == null) {
					client.createMessage();
					txtAreaWriteMessage.setText("");
					System.out.println("UI: Send message");
				} else if (!txtAreaWriteMessage.getText().equals("") && lblImageViewer.getIcon() != null) {
					client.createMessage();
					txtAreaWriteMessage.setText("");
					lblImageViewer.setIcon(null);
					System.out.println("UI: Send message and or image");
				} else if (txtAreaWriteMessage.getText().equals("") && lblImageViewer.getIcon() != null) {
					if (filepath != null) {
						client.createMessage();
						lblImageViewer.setIcon(null);
						System.out.println("UI: filepath is not null");
					}
				}
			} else if (e.getSource() == btnChooseFile) {
				returnval = fileChooser.showOpenDialog(null);
				if (returnval == JFileChooser.APPROVE_OPTION) {
					filepath = fileChooser.getSelectedFile().getPath();
					displayPreview(new ImageIcon(fileChooser.getSelectedFile().getPath()));
					System.out.println("UI: Chose image");
				}
			} else if (e.getSource() == btnRemoveImage) {
				lblImageViewer.setIcon(null);
				filepath = null;
				System.out.println("UI: Removed image");
			} else if (e.getSource() == btnUpdateMessages) {
				client.getMessages();
				System.out.println("UI: Get messages pressed");
			} else if (e.getSource() == btnUpdateUsers) {
				client.getUsers();
				System.out.println("UI: Get users pressed");
			}
		}
	}
}