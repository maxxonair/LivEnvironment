package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;



public class Sideboard {

	
	private JPanel mainPanel;
	private Color backgroundColor = Color.gray;
	@SuppressWarnings("unused")
	private Color labelColor = Color.gray;
	
	public Sideboard(Thread simulationMain) {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(backgroundColor);
		mainPanel.setPreferredSize(new Dimension(420,450));

	    ControlPane controlPane = new ControlPane(simulationMain);
	    mainPanel.add(controlPane.getMainPanel(), BorderLayout.CENTER);
	    
	    //ConsolePane consolePane = new ConsolePane();
	    //mainPanel.add(consolePane.getMainPanel(), BorderLayout.SOUTH);

	}
	
	public JPanel getMainPanel() {
		return mainPanel;
	}
}
