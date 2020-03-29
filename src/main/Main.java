package main;

import gui.MainFrame;
import simPlatform.SimEnvironment;
/*
//-------------------------------------------------------------------------------------------------------
//
//							PandaSim Mk1 - Micro pandemic simulation library
//
// Author MBraun
// Date 03/2020 
//
//-------------------------------------------------------------------------------------------------------
 */
public class Main {

	public static void main(String[] args) {		
		//-------------------------------------------------------------------------------------------------------
		//
		//									Simulation Setup 
		//
		//-------------------------------------------------------------------------------------------------------
		// Simulation Settings:
		int simulationTime = 30;     // Simulated Time frame [simulation time unit] , e.g. days
		int timeIncrement = 100;		// Simulated Time increment [milli simulation time unit]
		
		// Playground 
	    int[] fieldSize= new int[2];
	    fieldSize[0] = 1150;				  // Environment field size x direction [pixel/unit]
	    fieldSize[1] = 450;				  // Environment field size x direction [pixel/unit]
	    // GUI Playground settings 
	    int citizenSize = 4;			 	  // Citizen box length/width [pixel]
	    // Population Settings 
	    int populationSize = 1000;		  // Total population size per environment
	    int citizenStepSize = 10; 		  // Maximum citizen mobility step per time step [pixel/unit]
	    
	    // Pandemic Settings 
	    int infectionRadius = 5;	    	      // Infection box half length [pixel/unit]
	    int riskOfInfection = 70;		  // Risk of infection during single encounter [%]
	    double sickLeave = 1;  	     	  // Time period of sickness (from infection to removed) [pixel/unit]
	    double mortalityRate = 0.85;		  // Mortality rate after incubation period
	    
	    // Init health setting 
	    double initInfectionRate= 0.015;   // Initial amount of infected citizens
	    
	    // Console settings
	    boolean consPos=false;				// Select to show citizen position as console output
	    boolean healthOut=true; 	   			// Select to show health status on console and plot
	    boolean boCreateOutFile=true;		// Select to create simulation result file 
	    
	    String strFileName="result";			// Output file name
	    double[] dataOut = new double[4];	// Four channels opened to plot data (Set channels in SimEnvironment)
	    for(int i=0;i<dataOut.length;i++) {
	    dataOut[i]=0;
	    }
	    //-------------------------------------------------------------------------------------------------------
	    // 							Create Simulation Environment
	    //-------------------------------------------------------------------------------------------------------
		SimEnvironment simEnvironment = new SimEnvironment(fieldSize, citizenStepSize, 
														timeIncrement, dataOut, strFileName);
		// Create population 
		simEnvironment.createPopulation(populationSize, infectionRadius, riskOfInfection, 
										initInfectionRate, mortalityRate, sickLeave);
		// Simulation Output Settings
		simEnvironment.setBoPosOut(consPos);
		simEnvironment.setBoHealthOut(healthOut);
		simEnvironment.setBoCreateOutFile(boCreateOutFile);
	    //-------------------------------------------------------------------------------------------------------
		// 							Prepare Thread 
	    //-------------------------------------------------------------------------------------------------------
		Thread simulationMain = new Thread(new Runnable() {
		    public void run() {
		    		int timesteps= (int) (simulationTime/ ((double) (timeIncrement)/1000));
		    		simEnvironment.runSimulation(timesteps, timeIncrement);
		    }
		});
	    //-------------------------------------------------------------------------------------------------------
		// 							Create GUI
	    //-------------------------------------------------------------------------------------------------------
	    @SuppressWarnings("unused")
		MainFrame guiFrame = new MainFrame(fieldSize, simEnvironment, simulationMain, 
				simulationTime, timeIncrement, dataOut, citizenSize);
	    //-------------------------------------------------------------------------------------------------------
		// Launch Simulation:  
		//simulationMain.start();  // Launch simulation directly from script 	
	    //-------------------------------------------------------------------------------------------------------
	}
}