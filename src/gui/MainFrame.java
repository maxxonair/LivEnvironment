package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import realtimeChart.RealTimePlotElement;
import simPlatform.SimEnvironment;

public class MainFrame {

	
	public MainFrame(int[] fieldSize, SimEnvironment simEnvironment, Thread simulationMain, 
			         int simTime , int timeIncrement, double[] dataOut, int citizenSize) {
		JFrame frame = new JFrame("The Living Sim - Mk1 | Population Size "+simEnvironment.getPopulationsSize());
		frame.setSize(400,400);
		frame.setLayout(new BorderLayout());
		
		MenuBar menuBar = new MenuBar(frame, simulationMain);
		frame.add(menuBar.getMenuBar(), BorderLayout.NORTH);
		
		SimulationCanvas simulationCanvas = new SimulationCanvas(fieldSize, simEnvironment, citizenSize);
		//frame.add(simulationCanvas.getMainPanel(), BorderLayout.CENTER);
		
		//Sideboard sideboard = new Sideboard( simulationMain);
		//frame.add(sideboard.getMainPanel(), BorderLayout.WEST);
		
		double plotFrequency = 1000/(double) (timeIncrement);
		RealTimePlotElement realTimePlotElement = new RealTimePlotElement(simTime, plotFrequency, 
																		dataOut, simEnvironment) ;
		frame.add(realTimePlotElement.getChartPanel(), BorderLayout.SOUTH);
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		//topPanel.add(sideboard.getMainPanel(), BorderLayout.WEST);
		topPanel.add(simulationCanvas.getMainPanel(), BorderLayout.CENTER);
		
		//JSplitPane horizontalSplitUp = SplitPane.getSplitPane("horizontal");
		JSplitPane verticalSplitpane = SplitPane.getSplitPane("vertical");
		verticalSplitpane.add(realTimePlotElement.getChartPanel(), JSplitPane.BOTTOM);
		verticalSplitpane.add(topPanel, JSplitPane.TOP);
		
		frame.add(verticalSplitpane, BorderLayout.CENTER);
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		frame.pack();
		frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
	}
}
