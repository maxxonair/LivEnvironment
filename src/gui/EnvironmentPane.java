package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import population.Citizen;
import population.RandomDude;
import simPlatform.SimEnvironment;

public class EnvironmentPane {
	
	private JPanel mainPanel;
	
	public static Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    public static JTextArea textArea 	  = new JTextArea();


   	//public static Color backgroundColor = new Color(251,251,251);				    // Background Color
   	public static Color backgroundColor = new Color(41,41,41);				    // Background Color
   	// List of citizens
    private List<Citizen> population;
    // List of GUI citizens
    private List<JPanel> populationElements;
    // Link to simulation backbone
    SimEnvironment simEnvironment;
    
    private int citizenSize;
	
	public EnvironmentPane(int[] fieldSize, SimEnvironment simEnvironment, int citizenSize) {
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setBackground(backgroundColor);
		mainPanel.setSize(new Dimension(fieldSize[0],fieldSize[1]));
		
		this.simEnvironment=simEnvironment;
		this.citizenSize=citizenSize;
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
			citizen.setSize(citizenSize,citizenSize);
			RandomDude dude = (RandomDude) (population.get(i));
			if(dude.getHealthStatus()==0) {
			citizen.setBackground(Color.BLUE);
			} else if (dude.getHealthStatus()==1) {
				citizen.setBackground(Color.RED);
			} else if (dude.getHealthStatus()==2) {
				citizen.setBackground(Color.GRAY);
			} else if (dude.getHealthStatus()==3) {
				citizen.setBackground(Color.BLACK);
			} else if (dude.getHealthStatus()==4) {
				citizen.setBackground(Color.ORANGE);
			}
			//citizen.setLayout(null);
			citizen.setLocation(population.get(i).getPosition()[0], 
					 			population.get(i).getPosition()[1]);
			populationElements.add(citizen);
			mainPanel.add(citizen);
		}
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	public void resetPopulation() {
		populationElements.clear();
		this.population = simEnvironment.getPopulationField();
		for(int i=0;i<population.size();i++) {
			JPanel citizen = new JPanel();
			citizen.setSize(citizenSize,citizenSize);
			RandomDude dude = (RandomDude) (population.get(i));
			if(dude.getHealthStatus()==0) {
			citizen.setBackground(Color.BLUE);
			} else if (dude.getHealthStatus()==1) {
				citizen.setBackground(Color.RED);
			} else if (dude.getHealthStatus()==2) {
				citizen.setBackground(Color.GRAY);
			} else if (dude.getHealthStatus()==3) {
				citizen.setBackground(Color.BLACK);
			}
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
