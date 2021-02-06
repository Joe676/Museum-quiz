package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import museum.Visitor;
import java.awt.Font;

@SuppressWarnings("serial")
public class VisitorWindow extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField nameTxt;
	private JTextField portTxt;
	
	private Visitor visitor;
	private JPanel qaPanel;
	private String[] questions;
	private List<QPanel> qpanels = new ArrayList<>();
	private JLabel scoreLbl;
	private JLabel idLbl;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VisitorWindow frame = new VisitorWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VisitorWindow() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(visitor!=null)visitor.signOut();
				System.exit(0);
			}
		});
		setTitle("Visitor Terminal");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		JPanel create = new JPanel();
		contentPane.add(create, "Create");
		create.setLayout(null);
		
		JLabel nameLbl = new JLabel("Name:");
		nameLbl.setBounds(100, 91, 96, 14);
		create.add(nameLbl);
		
		nameTxt = new JTextField();
		nameTxt.setBounds(222, 88, 96, 20);
		create.add(nameTxt);
		nameTxt.setColumns(10);
		
		JLabel portLbl = new JLabel("registry port:");
		portLbl.setBounds(100, 150, 96, 14);
		create.add(portLbl);
		
		portTxt = new JTextField();
		portTxt.setBounds(222, 147, 96, 20);
		create.add(portTxt);
		portTxt.setColumns(10);
		
		JButton startBtn = new JButton("Start");
		startBtn.addActionListener(this);
		startBtn.setBounds(276, 197, 89, 23);
		create.add(startBtn);
		
		JPanel quizPanel = new JPanel();
		contentPane.add(quizPanel, "Quiz");
		quizPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel miscQA = new JPanel();
		quizPanel.add(miscQA, BorderLayout.SOUTH);
		
		scoreLbl = new JLabel("");
		scoreLbl.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 14));
		miscQA.add(scoreLbl);
		
		idLbl = new JLabel("");
		miscQA.add(idLbl);
		
		JButton answerBtn = new JButton("Answer");
		answerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] a = new String[questions.length];
				for(int i = 0; i<a.length; i++) {
					a[i] = qpanels.get(i).getAnswer();
				}
				boolean[] b = visitor.checkAnswers(a);
				int n = 0;
				for(int i = 0; i<b.length; i++) {
					qpanels.get(i).finished(b[i]);
					n+=b[i]?1:0;
				}
				n*=100;
				int score = n/b.length;
				scoreLbl.setText("Your Score: "+ score+"%");
				answerBtn.setEnabled(false);
			}
		});
		miscQA.add(answerBtn);
		
		qaPanel = new JPanel(new BorderLayout(0, 0));
		quizPanel.add(qaPanel, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		visitor = new Visitor(Integer.parseInt(portTxt.getText()), nameTxt.getText(), this);
		CardLayout cl = (CardLayout)contentPane.getLayout();
		cl.show(contentPane, "Quiz");
		idLbl.setText("Name: "+visitor.getName());
		showQuiz();
	}

	private void showQuiz() {
		qaPanel.removeAll();

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setPreferredSize(new Dimension(qaPanel.getWidth()-30, QPanel.HEIGHT*questions.length) );
		for(int i = 0; i<questions.length; i++) {
			QPanel qp = new QPanel(i, questions[i]);
			p.add(qp);
			qpanels.add(qp);
		}
		p.validate();
		p.repaint();
		
		JScrollPane scroll = new JScrollPane(p);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		qaPanel.add(scroll, BorderLayout.CENTER);
		scroll.validate();
		scroll.repaint();
		qaPanel.validate();
		qaPanel.repaint();
		
	}

	public void setQuestions(String[] questions) {
		this.questions = questions;
	}
}
