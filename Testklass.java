package guMeddelandesystem;

import javax.swing.SwingUtilities;

public class Testklass {
	public static void main(String[] args) {
		MsgServer server = new MsgServer(3500);
		server.start();
		
		UI ui = new UI();
		UI ui2 = new UI();
		UI ui3 = new UI();
		Client client = new Client(ui);
		Client client2 = new Client(ui2);
		Client client3 = new Client(ui3);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ui.startFrame(client);
				ui2.startFrame(client2);
				ui3.startFrame(client3);
			}
		});
		}	
}
