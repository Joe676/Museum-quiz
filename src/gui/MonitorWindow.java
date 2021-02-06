package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import museum.Monitor;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class MonitorWindow extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField portTxt;
	
	private List<ScorePanel> scores = new ArrayList<>();
	private JPanel scoresPanel;
	
	private Monitor monitor; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MonitorWindow frame = new MonitorWindow();
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
	public MonitorWindow() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(monitor!=null)monitor.disconnect();
				System.exit(0);
			}
		});
		setTitle("Monitor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		JPanel create = new JPanel();
		contentPane.add(create, "Create");
		create.setLayout(null);
		
		JLabel portLbl = new JLabel("registry port:");
		portLbl.setBounds(101, 119, 90, 14);
		create.add(portLbl);
		
		portTxt = new JTextField();
		portTxt.setBounds(201, 116, 96, 20);
		create.add(portTxt);
		portTxt.setColumns(10);
		
		JButton startBtn = new JButton("Start");
		startBtn.addActionListener(this);
		startBtn.setBounds(277, 198, 89, 23);
		create.add(startBtn);
		
		JPanel monitorPanel = new JPanel();
		contentPane.add(monitorPanel, "Monitor");
		monitorPanel.setLayout(new BorderLayout(0, 0));
		
		scoresPanel = new JPanel(new BorderLayout(0, 0));
		monitorPanel.add(scoresPanel, BorderLayout.CENTER);
		
		JPanel misc = new JPanel();
		monitorPanel.add(misc, BorderLayout.SOUTH);
		misc.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel idLbl = new JLabel("ID: ");
		misc.add(idLbl);
		
		JButton disconnectBtn = new JButton("Disconnect");
		disconnectBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				monitor.disconnect();
				System.exit(0);
			}
		});
		misc.add(disconnectBtn);
	}

	public void addScore(String userName, int percentageScore) {
		scores.add(new ScorePanel(userName, percentageScore));
		showScores();
	}

	private void showScores() {
		scoresPanel.removeAll();

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setPreferredSize(new Dimension(scoresPanel.getWidth()-30, ScorePanel.HEIGHT*scores.size()) );
		for(ScorePanel s:scores) {
			p.add(s);
		}
		p.validate();
		p.repaint();
		
		JScrollPane scroll = new JScrollPane(p);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scoresPanel.add(scroll, BorderLayout.CENTER);
		scroll.validate();
		scroll.repaint();
		scoresPanel.validate();
		scoresPanel.repaint();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		monitor = new Monitor(Integer.parseInt(portTxt.getText()), this);
		CardLayout cl = (CardLayout) contentPane.getLayout();
		cl.show(contentPane, "Monitor");
	}

}
