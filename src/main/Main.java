package main;

import gui.MainFrame;
import simPlatform.PandemicSettings;
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
		int timeIncrement = 100;		 // Simulated Time increment [milli simulation time unit]
		
		// Playground 
	    int[] fieldSize= new int[2];
	    fieldSize[0] = 1150;				  // Environment field size x direction [pixel/unit]
	    fieldSize[1] = 450;				  // Environment field size x direction [pixel/unit]
	    // GUI Playground settings 
	    int citizenSize = 4;			 	  // Citizen box length/width [pixel] (Does not influence to infection radius)
	    // Population Settings 
	    int populationSize = 1000;		  // Total population size per environment
	    int citizenStepSize = 10; 		  // Maximum citizen mobility step per time step [pixel/unit]
	    
	    PandemicSettings pandemicSetting = new PandemicSettings();
	    
	    // Pandemic Settings 
	    pandemicSetting.setInfectionRadius(10);	    	   // Infection box half length [pixel/unit]
	    pandemicSetting.setRiskOfInfection(70);		   // Risk of infection during single encounter [%]
	    pandemicSetting.setSickLeave(14); 	     	   // Time period of sickness (from infection to removed) [pixel/unit]
	    pandemicSetting.setMortalityRate(0.08);        // Mortality rate after incubation period
	    
	    pandemicSetting.setSymptomRate(0.6);	   			// Percentage of sick individuals entering quarantine [%]
	    pandemicSetting.setTimeToSymptoms(4);	        // Time from infection to showing symptoms
	    pandemicSetting.setBoEnterQuarantine(true);  	// Include Quarantine
	    pandemicSetting.setRiskOfMisdiagnose(0.1);	   	// Risk of misdiagnosis, missing tests, no symptoms 
										   				 // -> sick infectious citizens are not quarantined
	    
	    // Init health setting 
	    pandemicSetting.setInitInfectionRate(0.01);
	    
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
		SimEnvironment simEnvironment = new SimEnvironment(fieldSize, citizenStepSize, timeIncrement, dataOut, 
														   strFileName, pandemicSetting);
		// Create population 
		simEnvironment.createPopulation(populationSize);
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