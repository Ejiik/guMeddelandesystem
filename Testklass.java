package guMeddelandesystem;

public class Testklass {
	public static void main(String[] args) {
		MsgServer server = new MsgServer(3500);
		
		UI ui = new UI();
		UI ui2 = new UI();
		Client client = new Client(ui);
		Client client2 = new Client(ui2);
		ui.startFrame(client);
		ui2.startFrame(client2);
		
		}	
}
