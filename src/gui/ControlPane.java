package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ControlPane {
	private JPanel mainPanel;
	
	public static Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    public static JTextArea textArea 	  = new JTextArea();
	
    public static Color labelColor = new Color(220,220,220);    					// Label Color
   	public static Color backgroundColor = new Color(41,41,41);				    // Background Color
   	
   	private int simState=0;
	// 0 - Not started 
   	// 1 - Running 
   	// 2 - Paused 
	public ControlPane(Thread simulationMain) {
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setBackground(backgroundColor);
		mainPanel.setSize(new Dimension(420,450));
		
		JButton startButton = new JButton("Start/Stop");
		startButton.setSize(150,50);
		startButton.setLocation(5, 30);
		startButton.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (simState == 0) {
					simulationMain.start();
					simState=1;
				} else if (simState == 1) {

						simulationMain.stop();
						simState=2;

				} else if (simState == 2) {
					simulationMain.start();
					simState=1;
				}
				
			}
			
		});
		mainPanel.add(startButton);

	}

	public JPanel getMainPanel() {
		return mainPanel;
	}
	
	
}
