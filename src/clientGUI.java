import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

// client GUI
class clientGUI extends JFrame{
	// player status
	private final int regularS = 1;
	private final int readyS = 2;
	private final int defeatedS = 0;
	private final int immuneS = 3;
	private final String[] stalist = {"Defeated", "Regular", "Ready", "Immune"};
	// panals used to store user information.
	private player[] clients;
	private JPanel[] Ppanes;
	private Label[][] pLabel;
	private int self;
	// containers
	private Container container;
	private JPanel userCon;
	// Buttons
	public JButton statuSBu;
	public JButton quitBu;
	// target score label
	public Label tarLabel;
	// turn display label
	public Label turnLabel;
	// cards button
	public JButton[] cards;
	public JPanel cardP;
	// card use confirm
	public JButton conBu;
	//discard card buttion
	public JButton disBu;
	// target 1 
	public JPanel tar1p;
	public JButton[] tar1Bu = new JButton[4];
	// target 2
	public JPanel tar2p;
	public JButton[] tar2Bu = new JButton[4];
	// guess
	public JPanel gusp;
	public JButton[] gusb = new JButton[8];
	// effect textbox
	public JTextArea effectField = new JTextArea(250, 300);
	public JScrollPane boxSc;
	// description textbox
	public JTextArea desField = new JTextArea();

	public clientGUI(player[] clients){
		super("Love Letter");
		setLayout(null);
		setResizable(false);
		Ppanes = new JPanel[4];
		pLabel = new Label[4][4];
		this.clients = clients;
		this.setLayout(null);
        setDefaultCloseOperation(0); 
        container = getContentPane();
        this.setSize(800, 600); 
        this.setLocation(300,200);
        userCon = new JPanel();
        userCon.setLayout(new FlowLayout());
        userCon.setBounds(0,0,800,100);
        userCon.setBackground(Color.lightGray);
        // add button
        statuSBu = new JButton("Ready");
        statuSBu.setBounds(350,500,80,80);
     	quitBu = new JButton("Quit");
     	quitBu.setBounds(450,500,80,80);
     	// taget label
     	tarLabel = new Label();
     	tarLabel.setText("Target Score: TBD");
     	tarLabel.setBounds(0,getHeight() - 160,130, 20);
     	// turn label
     	turnLabel = new Label();
     	turnLabel.setVisible(false);
     	turnLabel.setBounds(0,400,150,20);
     	// card button
     	cards = new JButton[2];
     	cardP = new JPanel();
     	cardP.setLayout(new FlowLayout());
     	// cardP.setBackground(Color.RED);
     	cardP.setBounds(250,375,320,200);
     	cardP.setVisible(false);
     	// card use confirm
     	conBu = new JButton("Confirm");
     	conBu.setBounds(350,340,80,20);
     	conBu.setVisible(false);
     	// discard card button
     	disBu = new JButton("Discard");
     	disBu.setBounds(440,340,80,20);
     	disBu.setVisible(false);
     	// target 1
     	tar1p = new JPanel();
     	tar1p.setLayout(new FlowLayout());
     	tar1p.setBounds(400,160,105,150);
     	// target2
     	tar2p = new JPanel();
     	tar2p.setLayout(new FlowLayout());
     	tar2p.setBounds(510,160,105,150);
     	// guess
     	gusp = new JPanel();
      	gusp.setLayout(new FlowLayout());
     	gusp.setBounds(625,160,105,200);
     	gusb[0] = new JButton("guard");
     	gusb[0].setVisible(false);
     	gusb[1] = new JButton("priest");
     	gusb[2] = new JButton("baron");
     	gusb[3] = new JButton("handmaiden");
     	gusb[4] = new JButton("prince");
     	gusb[5] = new JButton("king");
     	gusb[6] = new JButton("countess");
     	gusb[7] = new JButton("princess");
     	for(int i = 0; i < 8; i++){
     		gusb[i].setPreferredSize(new Dimension(105, 20));
     		gusb[i].setName(gusb[i].getText());
     		gusp.add(gusb[i]);
     	}
     	tar1p.setVisible(false);
     	tar2p.setVisible(false);
     	gusp.setVisible(false);
     	// effect field
     	effectField.setEditable(false);
     	effectField.setLineWrap(true);
     	effectField.setWrapStyleWord(true);
     	boxSc = new JScrollPane(effectField);
     	boxSc.setBounds(0, 100, 250, 300);
     	boxSc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
     	boxSc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
     	boxSc.setWheelScrollingEnabled(true);
     	boxSc.setVisible(false);
     	// description text box
     	desField.setEditable(false);
     	desField.setLineWrap(true);
     	desField.setWrapStyleWord(true);
     	desField.setBounds(650,375,150,200);
     	desField.setVisible(false);
     	// adding
     	container.add(disBu);
     	container.add(desField);
     	container.add(boxSc);
     	container.add(gusp);
     	container.add(tar2p);
     	container.add(tar1p);
     	container.add(conBu);
     	container.add(cardP);
     	container.add(turnLabel);
     	container.add(tarLabel);
     	container.add(quitBu);
        container.add(statuSBu);
        container.add(userCon);
	}

	public void setSelf(int self){
		this.self =self;
	}
	// update informatio of players
	public void updatePlayer(){
		for(int i = 0; i < 4; i++){
			if(clients[i] != null){
				String username = clients[i].getUsername();
				String userstatus = clients[i].getStatus() + "";
				int usercode = clients[i].getUserid();
				String userscore = clients[i].getScore() + "";
				if(Ppanes[usercode] == null){
					Ppanes[usercode] = new JPanel(new FlowLayout());
					if(usercode == self){
						Ppanes[usercode].setBorder(BorderFactory.createTitledBorder("You"));
					}else{
						// Ppanes[usercode].setBorder(BorderFactory.createEtchedBorder());
					}
					if(usercode == self){
						Ppanes[usercode].setBounds(0,getHeight() - 130,150, 110);
						container.add(Ppanes[usercode]);
					}else{
						Ppanes[usercode].setLayout(new FlowLayout());
						Ppanes[usercode].setPreferredSize(new Dimension(150, 90));
						userCon.add(Ppanes[usercode]);
					}
					for(int index = 0; index < 4; index++){
						pLabel[usercode][index] = new Label("");
						Ppanes[usercode].add(pLabel[usercode][index]);
					}
				}
				pLabel[usercode][0].setText("Username: " + username);
				pLabel[usercode][1].setText("Usercode: " + usercode + "");
				pLabel[usercode][1].setVisible(false);
				pLabel[usercode][2].setText("Status: " + stalist[Integer.parseInt(userstatus)]);
				pLabel[usercode][3].setText("Socre: " + userscore);
				// create target button
				if(usercode != self && tar1Bu[usercode] == null){
					tar1Bu[usercode] = new JButton(clients[usercode].getUsername());
					tar1Bu[usercode].setName(usercode + "");
					tar1Bu[usercode].setPreferredSize(new Dimension(105,20));
					tar1p.add(tar1Bu[usercode]);
					
					tar2Bu[usercode] = new JButton(clients[usercode].getUsername());
					tar2Bu[usercode].setName(usercode + "");
					tar2Bu[usercode].setPreferredSize(new Dimension(105,20));
					tar2p.add(tar2Bu[usercode]);
				}else if(tar1Bu[usercode] != null){
					tar1Bu[usercode].setText(clients[usercode].getUsername());
					tar2Bu[usercode].setText(clients[usercode].getUsername());
				}
				if(tar1Bu[usercode] != null){
					if(clients[usercode].getStatus() == defeatedS || clients[usercode].getStatus() == immuneS){
						tar1Bu[usercode].setVisible(false);
						tar2Bu[usercode].setVisible(false);
					}else if(clients[usercode].getStatus() == regularS){
						tar1Bu[usercode].setVisible(true);
						tar2Bu[usercode].setVisible(true);
					}
				}
			}else if(Ppanes[i] != null){
				Ppanes[i].setVisible(false);
				userCon.remove(Ppanes[i]);
				Ppanes[i] = null;
			}
		}
		setVisible(true);
	}


}