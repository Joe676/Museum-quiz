package gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class ScorePanel extends JPanel {

	public static final int HEIGHT = 40;
	
	public ScorePanel(String name, int score) {
		setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel nameLbl = new JLabel(name + ":");
		nameLbl.setHorizontalAlignment(SwingConstants.CENTER);
		nameLbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
		add(nameLbl);
		
		JLabel scoreLbl = new JLabel(score+"%");
		scoreLbl.setHorizontalAlignment(SwingConstants.CENTER);
		scoreLbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
		add(scoreLbl);

	}

}
