package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import museum.Stand;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.CardLayout;
import javax.swing.JTextField;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class StandWindow extends JFrame implements ActionListener {

	protected StandWindow me;
	
	private JPanel contentPane;
	private Stand stand;
	private JTextField portTxt;
	private JLabel idLbl;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StandWindow frame = new StandWindow();
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
	public StandWindow() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				stand.disconnect();
				System.exit(0);
			}
		});
		me = this;
		setTitle("Stand");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		JPanel create = new JPanel();
		contentPane.add(create, "Create");
		create.setLayout(null);
		
		portTxt = new JTextField();
		portTxt.setBounds(194, 116, 96, 20);
		create.add(portTxt);
		portTxt.setColumns(10);
		
		JLabel portLbl = new JLabel("registry port:");
		portLbl.setBounds(104, 119, 112, 14);
		create.add(portLbl);
		
		JButton startBtn = new JButton("Start");
		startBtn.addActionListener(this);
		startBtn.setBounds(279, 191, 89, 23);
		create.add(startBtn);
		
		JPanel standPanel = new JPanel();
		contentPane.add(standPanel, "Stand");
		standPanel.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setBounds(70, 49, 286, 154);
		standPanel.add(textArea);
		
		idLbl = new JLabel("ID: ");
		idLbl.setBounds(70, 214, 49, 14);
		standPanel.add(idLbl);
		
		JButton disconnectBtn = new JButton("Disconnect");
		disconnectBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(stand!=null)stand.disconnect();
				me.setVisible(false);
				me.dispose();
				System.exit(0);
			}
		});
		disconnectBtn.setBounds(286, 219, 130, 23);
		standPanel.add(disconnectBtn);
	}
	
	public void updateDescription(String d) {
		textArea.setText(d);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		stand = new Stand(Integer.parseInt(portTxt.getText()), this);
		CardLayout cl = (CardLayout) contentPane.getLayout();
		cl.show(contentPane, "Stand");
		try {
			idLbl.setText("ID: "+stand.getId());
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
	}
}
