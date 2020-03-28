package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import realtimeChart.RealTimePlotElement;
import simPlatform.SimEnvironment;

public class MainFrame {

	
	public MainFrame(int[] fieldSize, SimEnvironment simEnvironment, Thread simulationMain, 
			         int simTime , int timeIncrement, double[] dataOut) {
		JFrame frame = new JFrame("The Living Sim - Mk1");
		frame.setSize(400,400);
		frame.setLayout(new BorderLayout());
		
		MenuBar menuBar = new MenuBar();
		frame.add(menuBar.getMenuBar(), BorderLayout.NORTH);
		
		SimulationCanvas simulationCanvas = new SimulationCanvas(fieldSize, simEnvironment);
		frame.add(simulationCanvas.getMainPanel(), BorderLayout.CENTER);
		
		Sideboard sideboard = new Sideboard( simulationMain);
		frame.add(sideboard.getMainPanel(), BorderLayout.WEST);
		
		
		double plotFrequency = 1000/(double) (timeIncrement);
		RealTimePlotElement realTimePlotElement = new RealTimePlotElement(simTime, plotFrequency, 
																		dataOut, simEnvironment) ;
		frame.add(realTimePlotElement.getChartPanel(), BorderLayout.SOUTH);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		frame.pack();
		frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
	}
}
