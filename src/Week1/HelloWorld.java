package Week1;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;

public class HelloWorld {

	public HelloWorld() {
		JFrame jf = new JFrame("Hello");
		jf.setSize(400, 400);
		jf.setVisible(true);
		jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE); //chương trình đóng khi cửa sổ jframe đóng theo
		
		JButton btnNewButton = new JButton("New button");
		jf.getContentPane().add(btnNewButton, BorderLayout.CENTER);
	}
	public static void main(String[] args) {
		new HelloWorld();
	}
}
