package gui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class StandPanel extends JPanel {
	private JTextField textField;
	public static final int HEIGHT = 70;
	private int ID;
	

	public StandPanel(int ID) {
		this.ID = ID;
		setLayout(new BorderLayout(0, 0));
		
		textField = new JTextField();
		add(textField, BorderLayout.CENTER);
		textField.setColumns(10);
		
		JLabel idLbl = new JLabel("ID: "+ID);
		add(idLbl, BorderLayout.WEST);

	}


	public int getID() {
		return ID;
	}
	
	public String getDescription() {
		return textField.getText();
	}
	
	

}
