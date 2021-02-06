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

import museum.Designer;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.BoxLayout;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class DesignerWindow extends JFrame implements ActionListener {

	private JPanel contentPane;
	private Designer designer;
	private JTextField portTxt;
	private JPanel designerPanel;
	private JLabel idLbl1;
	private JLabel idLbl;
	
	private List<QAPanel> qas = new ArrayList<QAPanel>();
	private List<StandPanel> stands	= new ArrayList<StandPanel>();
	private JPanel qaPanel;
	private JPanel standsPanel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DesignerWindow frame = new DesignerWindow();
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
	public DesignerWindow() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(designer!=null)designer.disconnect();
				System.exit(0);
			}
		});
		setTitle("Designer");
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
		portLbl.setBounds(97, 119, 95, 14);
		create.add(portLbl);
		
		portTxt = new JTextField();
		portTxt.setBounds(202, 116, 96, 20);
		create.add(portTxt);
		portTxt.setColumns(10);
		
		JButton startBtn = new JButton("Start");
		startBtn.addActionListener(this);
		startBtn.setBounds(289, 199, 89, 23);
		create.add(startBtn);
		
		designerPanel = new JPanel();
		contentPane.add(designerPanel, "Designer");
		designerPanel.setLayout(new CardLayout(0, 0));
		
		JPanel qaCard = new JPanel();
		designerPanel.add(qaCard, "QA");
		qaCard.setLayout(new BorderLayout(0, 0));
		
		qaPanel = new JPanel();
		qaPanel.setLayout(new BorderLayout(0, 0));
		qaCard.add(qaPanel, BorderLayout.CENTER);
		
		JPanel qaMisc = new JPanel();
		qaCard.add(qaMisc, BorderLayout.SOUTH);
		
		idLbl1 = new JLabel("ID: ");
		qaMisc.add(idLbl1);
		
		JButton addQABtn = new JButton("+");
		addQABtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				qas.add(new QAPanel(qas.size()));
				showQAs();
			}
		});
		qaMisc.add(addQABtn);
		
		JButton submitQABtn = new JButton("Submit Quiz");
		submitQABtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<String> qs = new ArrayList<String>();
				List<String> as = new ArrayList<String>();
				
				for(QAPanel q: qas) {
					qs.add(q.getQuestion());
					as.add(q.getAnswer());
				}
				
				if(designer.addQA(qs, as)) {
					CardLayout cl = (CardLayout)designerPanel.getLayout();
					cl.show(designerPanel, "Stands");
					showStands();
				}
				
			}
		});
		qaMisc.add(submitQABtn);
		
		JButton disconnectBtn1 = new JButton("Disconnect");
		disconnectBtn1.setActionCommand("OFF");
		disconnectBtn1.addActionListener(this);
		qaMisc.add(disconnectBtn1);
		
		JPanel standsCard = new JPanel();
		designerPanel.add(standsCard, "Stands");
		standsCard.setLayout(new BorderLayout(0, 0));
		
		JPanel standsMisc = new JPanel();
		standsCard.add(standsMisc, BorderLayout.SOUTH);
		
		idLbl = new JLabel("ID: ");
		standsMisc.add(idLbl);
		
		JButton submitSBtn = new JButton("Submit Stands");
		submitSBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(StandPanel s: stands) {
					designer.setContent(s.getID(), s.getDescription());
				}
				CardLayout cl = (CardLayout)designerPanel.getLayout();
				cl.show(designerPanel, "QA");
				qas.clear();
				showQAs();
				
			}
		});
		standsMisc.add(submitSBtn);
		
		JButton disconnectBtn2 = new JButton("Disconnect");
		disconnectBtn2.setActionCommand("OFF");
		disconnectBtn2.addActionListener(this);
		standsMisc.add(disconnectBtn2);
		
		standsPanel = new JPanel();
		standsPanel.setLayout(new BorderLayout(0, 0));
		standsCard.add(standsPanel, BorderLayout.CENTER);
	}

	public void showStands() {
		standsPanel.removeAll();
		stands.clear();

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setPreferredSize(new Dimension(standsPanel.getWidth()-30, StandPanel.HEIGHT*designer.getStandIds().size()) );
		for(int standID: designer.getStandIds()) {
			StandPanel s = new StandPanel(standID);
			p.add(s);
			stands.add(s);
		}
		p.validate();
		p.repaint();
		
		JScrollPane scroll = new JScrollPane(p);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		standsPanel.add(scroll, BorderLayout.CENTER);
		scroll.validate();
		scroll.repaint();
		standsPanel.validate();
		standsPanel.repaint();
	}

	protected void showQAs() {
		qaPanel.removeAll();
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setPreferredSize(new Dimension(qaPanel.getWidth()-30, QAPanel.HEIGHT*qas.size()));
		for(QAPanel q: qas) {
			p.add(q);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("OFF")) {
			designer.disconnect();
			this.setVisible(false);
			this.dispose();
			System.exit(0);
			return;
		}
		designer = new Designer(Integer.parseInt(portTxt.getText()), this);
		CardLayout cl = (CardLayout)contentPane.getLayout();
		cl.show(contentPane, "Designer");
		cl = (CardLayout)designerPanel.getLayout();
		cl.show(designerPanel, "QA");
		idLbl.setText("ID: "+designer.getId());
		idLbl1.setText("ID: "+designer.getId());
	}

}
