package gui;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class QAPanel extends JPanel {
	private JTextField questionTxt;
	private JTextField answerTxt;
	public static final int HEIGHT = 70;

	public QAPanel(int num) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel questionLbl = new JLabel("Question #"+num);
		add(questionLbl);
		
		questionTxt = new JTextField();
		add(questionTxt);
		questionTxt.setColumns(10);
		
		JLabel answerLbl = new JLabel("Answer");
		add(answerLbl);
		
		answerTxt = new JTextField();
		add(answerTxt);
		answerTxt.setColumns(10);

	}
	
	public String getQuestion() {
		return questionTxt.getText();
	}
	
	public String getAnswer() {
		return answerTxt.getText();
	}

}
