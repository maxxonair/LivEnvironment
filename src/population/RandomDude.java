package population;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import simPlatform.PandemicSettings;

public class RandomDude extends Citizen implements Cloneable {
	
	// Environment Settings
	private int[] fieldSize = new int[2];
	// Random Unit 
	private Random rand = new Random(); 
	private Color color;
	//-------------------------------------------------------------------------------------
	// Class properties
	private int[] position= new int[2];  // Current position on the field 
	private int stepSize; 				 // Maximum step on the field per timestep 
	//-------------------------------------------------------------------------------------
	// Health Status
	// 0 - Healthy/Receptive
	// 1 - Sick/Contagious
	// 2 - Removed/Alive 
	// 3 - Removed/Dead
	// 4 - Sick/ Non Contagious
	private int healthStatus=0;
	//-------------------------------------------------------------------------------------
	// Containment Status
	// 0 - Out and about
	// 1 - Quarantined 
	private int containmentStatus=0;
	//-------------------------------------------------------------------------------------
	private double infectionTime=0;  	// Time since infection 
	PandemicSettings pandemicSetting;
	//-------------------------------------------------------------------------------------
	public RandomDude(int[] position, int[] fieldSize, int stepSize, int healthStatus, 
			            PandemicSettings pandemicSetting) {
		super(position, fieldSize);
		
		this.position=position;
		this.fieldSize=fieldSize;
		this.stepSize=stepSize;
		this.pandemicSetting=pandemicSetting;
		
		this.healthStatus=healthStatus;
		
		if(healthStatus==0) {
			this.color = Color.BLUE;
		} else if (healthStatus==1) {
			this.color = Color.RED;
		} else if (healthStatus==2) {
			this.color = new Color(210,210,210);
		} else if (healthStatus==4) {
			this.color = Color.ORANGE;
		}
	}

	public void move(int[] position, List<Citizen> populationField) {
		if(containmentStatus == 0) { // Dude is out and about 
			// Perform step randomly eather to north, east, south or west 
			// If the new position is occupied by another citizen -> stay 
			// If the new position is outside the environment     -> stay 
			//------------------------------------------------------------
			// Get Stepsize 
			int stepReal = rand.nextInt(stepSize+1);
			// Take a random step 
			int walkDecis = rand.nextInt(4);
			int[] newPosition = position;
			if(healthStatus != 3) {
				if(walkDecis==0) { 		   // north
					newPosition[1] -= stepReal;
				} else if (walkDecis==1) { // west 
					newPosition[0] += stepReal;
				} else if (walkDecis==2) { // south 
					newPosition[1] += stepReal;
				} else if (walkDecis==3) { // east 
					newPosition[0] -= stepReal;
				}
			}
			//------------------------------------------------------------
			// Check if the new position is occupied 
			for (int i=0;i<populationField.size();i++) {
				if(populationField.get(i).getPosition() == newPosition) {
					newPosition = position;
				}
			}
			//------------------------------------------------------------
			// Check if the new position is outside the environment frame 
			if(newPosition[0]>fieldSize[0]) {
				newPosition[0] = fieldSize[0]-1;
			} else if (newPosition[0]<0) {
				newPosition[0] = 0;
			}
			if(newPosition[1]>fieldSize[1] ) {
				newPosition[1] = fieldSize[1]-1;
			} else if (newPosition[1]<0) {
				newPosition[1] = 0;
			}
			//------------------------------------------------------------
			//------------------------------------------------------------
			// Set new citizen position
			this.position = newPosition;
		}
	}
	
	public void updateSickLeave(int timestep) {
		infectionTime+= ((double) (timestep)/1000);
		infectionTime+= ((double) (timestep)/1000);
	}
	
	
	@Override
	public Object clone() throws CloneNotSupportedException {

	    return super.clone();
	}


	public int[] getFieldSize() {
		return fieldSize;
	}


	public int[] getPosition() {
		return position;
	}


	public int getHealthStatus() {
		return healthStatus;
	}


	public double getInfectionTime() {
		return infectionTime;
	}


	public void setHealthStatus(int healthStatus) {
		this.healthStatus = healthStatus;
		if(healthStatus==0) {
			this.color = Color.BLUE;
		} else if (healthStatus==1) {
			this.color = Color.RED;
		} else if (healthStatus==2) {
			this.color = new Color(210,210,210);
		} else if (healthStatus==3) {
			this.color = Color.BLACK;
		} else if (healthStatus==4) {
			this.color = Color.ORANGE;
		}
	}
	
	@Override
	public Color getColor() {
		return color;
	}


	public int getContainmentStatus() {
		return containmentStatus;
	}


	public void setContainmentStatus(int containmentStatus) {
		this.containmentStatus = containmentStatus;
		int[] newPosition = new int[2];
		newPosition[0]=-1;
		newPosition[1]=-1;
		this.position=newPosition; // Remove from field
	}

	public PandemicSettings getPandemicSetting() {
		return pandemicSetting;
	}
	
	
}
