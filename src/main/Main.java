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
	    fieldSize[0] = 800;
	    fieldSize[1] = 500;
	    
	    // Population Settings 
	    int populationSize = 180;
	    int citizenStepSize = 10; 	
	    
	    // Pandemic Settings 
	    int infectionRadius = 30;	    
	    int riskOfInfection = 50;
	    double sickLeave = 3;  //[s]
	    
	    // Init health setting 
	    double initInfectionRate= 0.05;  // Initial amount of infected citizens
	    
	    // Console settings
	    boolean consPos=false;
	    boolean healthOut=true; 	   
	    
	    
	    double[] dataOut = new double[4];
	    for(int i=0;i<dataOut.length;i++) {
	    dataOut[i]=0;
	    }
	    //-----------------------------------------------------------------------------
	    // Create Simulation Environment 
		SimEnvironment simEnvironment = new SimEnvironment(fieldSize, citizenStepSize, timeIncrement);
		// Create population 
		simEnvironment.createPopulation(populationSize, infectionRadius, riskOfInfection, initInfectionRate, sickLeave);
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
		MainFrame guiFrame = new MainFrame(fieldSize, simEnvironment, simulationMain, simulationTime, timeIncrement, dataOut);
		//-----------------------------------------------------------------------------
		// Launch Simulation:
		//simulationMain.start();		
	}
}