package guMeddelandesystem;

public class Testklass {
	public static void main(String[] args) {
		MsgServer server = new MsgServer(3500);
		
		UI ui = new UI();
		UI ui2 = new UI();
		UI ui3 = new UI();
		Client client = new Client(ui);
		Client client2 = new Client(ui2);
		Client client3 = new Client(ui3);
		
		ui.startFrame(client);
		ui2.startFrame(client2);
		ui3.startFrame(client3);
		
		}	
}
