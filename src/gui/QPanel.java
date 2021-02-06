package gui;

import javax.swing.JPanel;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class QPanel extends JPanel {
	private JTextField answerTxt;
	private int qNum;
	
	public static final int HEIGHT = 70;
	
	public QPanel(int n, String q) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel questionLbl = new JLabel("Question #"+n+": "+q);
		add(questionLbl);
		
		JLabel answerLbl = new JLabel("Answer:");
		add(answerLbl);
		
		answerTxt = new JTextField();
		add(answerTxt);
		answerTxt.setColumns(10);
		
	}

	public int getqNum() {
		return qNum;
	}

	public void setqNum(int qNum) {
		this.qNum = qNum;
	}
	
	public String getAnswer() {
		return this.answerTxt.getText();
	}
	
	public void finished(boolean isRight) {
		answerTxt.setEditable(false);
		if(isRight)answerTxt.setForeground(Color.GREEN);
		else answerTxt.setForeground(Color.RED);
	}

}
