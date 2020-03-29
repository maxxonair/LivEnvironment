package main;

import gui.MainFrame;
import simPlatform.SimEnvironment;

public class Main {


	public static void main(String[] args) {
		
		//-----------------------------------------------------------------------------
		// Simulation Settings:
		int simulationTime = 20;    // Simulated Time frame [s]
		int timeIncrement = 100;		// Simulated Time increment [ms]
		
		// Playground 
	    int[] fieldSize= new int[2];
	    fieldSize[0] = 900;
	    fieldSize[1] = 500;
	    
	    int citizenSize = 5;
	    // Population Settings 
	    int populationSize = 700;
	    int citizenStepSize = 15; 	
	    
	    // Pandemic Settings 
	    int infectionRadius = 15;	    
	    int riskOfInfection = 60;
	    double sickLeave = 2;  //[s]
	    double mortalityRate = 0.09;
	    
	    // Init health setting 
	    double initInfectionRate= 0.025;  // Initial amount of infected citizens
	    
	    // Console settings
	    boolean consPos=false;
	    boolean healthOut=true; 	   
	    
	    
	    double[] dataOut = new double[4];
	    for(int i=0;i<dataOut.length;i++) {
	    dataOut[i]=0;
	    }
	    //-----------------------------------------------------------------------------
	    // Create Simulation Environment 
		SimEnvironment simEnvironment = new SimEnvironment(fieldSize, citizenStepSize, 
														timeIncrement);
		// Create population 
		simEnvironment.createPopulation(populationSize, infectionRadius, riskOfInfection, 
										initInfectionRate, mortalityRate, sickLeave);
		// Simulation Output Settings
		simEnvironment.setBoPosOut(consPos);
		simEnvironment.setBoHealthOut(healthOut);
		//-----------------------------------------------------------------------------
		// Prepare Thread 
		Thread simulationMain = new Thread(new Runnable() {
		    public void run() {
		    		int timesteps= (int) (simulationTime/ ((double) (timeIncrement)/1000));
		    		simEnvironment.runSimulation(timesteps, timeIncrement);
		    }
		});
		//-----------------------------------------------------------------------------
		// Create GUI
	    @SuppressWarnings("unused")
		MainFrame guiFrame = new MainFrame(fieldSize, simEnvironment, simulationMain, 
				simulationTime, timeIncrement, dataOut, citizenSize);
		//-----------------------------------------------------------------------------
		// Launch Simulation:
		//simulationMain.start();		
	}
}