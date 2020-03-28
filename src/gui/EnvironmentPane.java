package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import population.Citizen;
import simPlatform.SimEnvironment;

public class EnvironmentPane {
	
	private JPanel mainPanel;
	
	public static Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    public static JTextArea textArea 	  = new JTextArea();

    List<Citizen> population;
    
    List<JPanel> populationElements;
	
	public EnvironmentPane(int[] fieldSize, SimEnvironment simEnvironment) {
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setBackground(Color.GRAY);
		mainPanel.setSize(new Dimension(fieldSize[0],fieldSize[1]));
		
		// Link GUI to Population 
		this.population = simEnvironment.getPopulationField();
		// Link GUI to Simulation environment 
		simEnvironment.setGuiEnvironment(this);
		// Initialize GUI Population>
		initPopulation();
	}
	
	public void initPopulation() {
		populationElements = new ArrayList<>();
		for(int i=0;i<population.size();i++) {
			JPanel citizen = new JPanel();
			citizen.setSize(3,3);
			citizen.setBackground(Color.RED);
			//citizen.setLayout(null);
			citizen.setLocation(population.get(i).getPosition()[0], 
					 			population.get(i).getPosition()[1]);
			populationElements.add(citizen);
			mainPanel.add(citizen);
		}
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	public void updatePopulation() {
		try {
		for(int i=0;i<population.size();i++) {
			populationElements.get(i).setLocation(population.get(i).getPosition()[0], 
					 							  population.get(i).getPosition()[1]);
			populationElements.get(i).setBackground(population.get(i).getColor());
		}
		mainPanel.revalidate();
		mainPanel.repaint();
		} catch(NullPointerException exp) {
			System.err.println(""+exp.getStackTrace());
		}
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}
	
}
