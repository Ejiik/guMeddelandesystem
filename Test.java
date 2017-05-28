package guMeddelandesystem;

public class Test {

	public static void main(String[] args) {
		UI ui = new UI();
		Client c = new Client(ui);
		ui.startFrame(c);
	}
}
