package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import simPlatform.SimEnvironment;

public class SimulationCanvas {
	private JPanel mainPanel;
	
	public static Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    public static JTextArea textArea 	  = new JTextArea();
    public static Color labelColor = new Color(220,220,220);    					// Label Color
   	public static Color backgroundColor = new Color(41,41,41);				    // Background Color
	
	public SimulationCanvas(int[] fieldSize, SimEnvironment simEnvironment, int citizenSize) {
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setBackground(backgroundColor);
		mainPanel.setPreferredSize(new Dimension(420,450));
		
		EnvironmentPane environmentPane = new EnvironmentPane(fieldSize, simEnvironment, citizenSize);
		environmentPane.getMainPanel().setLocation(5, 5);
		mainPanel.add(environmentPane.getMainPanel());	
		
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}
	
	public void add(JPanel panel) {
		mainPanel.add(panel);
	}
}
