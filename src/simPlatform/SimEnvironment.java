package simPlatform;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import gui.EnvironmentPane;
import population.Citizen;
import population.RandomDude;
import realtimeChart.RealTimePlotElement;


public class SimEnvironment {

	// Size of the field [0]-> x , [1]-> y
	private int[] fieldSize= new int[2];
	private int timeIncrement; 
    
    private List<Citizen> populationField = new ArrayList<>();

	private Random rand = new Random();
	//----------------------------------------------------------------
	// Population Variables
	private int citizenStepSize;
	//----------------------------------------------------------------
	// GUI Variables
	EnvironmentPane guiEnvironment ;	
	RealTimePlotElement realTimePlotElement;
	//----------------------------------------------------------------
	// Script Control Variables 
	boolean boPosOut=false;
	boolean boHealthOut=false;
	
	//----------------------------------------------------------------
	public SimEnvironment(int[] fieldSize, int citizenStepSize, int timeIncrement) {
		this.fieldSize=fieldSize;
		this.citizenStepSize=citizenStepSize;
		this.timeIncrement=timeIncrement; 		
	}
	
	public void createPopulation(int populationSize, int infectionRadius, int riskOfInfection, double initInfectionRate, double sickLeave) {
		int nrOfSick = (int) (initInfectionRate*populationSize);
		for(int i=0;i<populationSize;i++) {
			int healthstatus = 0;
			if(i< (nrOfSick)) {
				healthstatus = 1;
			}
			int[] initPos = new int[2];
			initPos[0] = rand.nextInt(fieldSize[0]);
			initPos[1] = rand.nextInt(fieldSize[1]);

			RandomDude citizen = new RandomDude(initPos, fieldSize, citizenStepSize, healthstatus, infectionRadius, riskOfInfection, sickLeave);
			populationField.add(citizen);
		}
	}
	
	public void populationMove() {
		for(int i=0; i<populationField.size();i++) {
			RandomDude citizen = (RandomDude) populationField.get(i);
			citizen.move(citizen.getPosition(), populationField);
		}		
	}
	
	public void updateContamination() {
		for(int k=0; k<populationField.size();k++) {
			RandomDude citizen = (RandomDude) populationField.get(k);
			// Detect Citizens in vicinity radius 
			// Loop through vicinity field
			int[] x0 = citizen.getPosition();
			int r = citizen.getInfectionRadius();
			//-------------------------------------------------------------------------------------------
			if(citizen.getHealthStatus() == 0 ) {			// Case: Healthy Person 
				for(int i=(x0[0]-r); i<=(x0[0]+r);i++) {
					for(int j=(x0[1]-r); j<=(x0[1]+r);j++) {
						for (int l=0; l<populationField.size();l++) {
							int[] cis=new int[2];cis[0]=i; cis[1]=j;
							RandomDude intCit = (RandomDude) populationField.get(l);
							if(k != l && intCit.getPosition()[0] == cis[0] 
									&& intCit.getPosition()[1] == cis[1]  && intCit.getHealthStatus() == 1) {
								citizen.setHealthStatus( contaminationCaseAssessment(citizen.getRiskOfInfection()) ); 
							}
						}
					}
				}
			}
			//-------------------------------------------------------------------------------------------
			if(citizen.getHealthStatus() == 1 ) { 			// Case: Sick Person 
				citizen.updateSickLeave(timeIncrement);     
				if(citizen.getInfectionTime()>citizen.getSickLeave()) {
					citizen.setHealthStatus(2);  // set to removed 
				}
			}
		}
	}
	
	private int contaminationCaseAssessment(int riskOfInfection) {
		int riskCase = rand.nextInt(100);
		if(riskCase < riskOfInfection ) {
			return 1;
		} else {
			return 0;
		}
	}
	
	public void runSimulation(int timeSteps, int timestep) {
		int tstep=0;
		while(tstep<timeSteps) {
			//-------------------------
			// Add Simulation Actions here 
			populationMove();
			updateContamination();
			// Update GUI
			updateGUI();
			// Output
			if(boPosOut)    {outPositions(tstep);}
			if(boHealthOut) {outHealthStats();}
			//-------------------------
			try {
				TimeUnit.MILLISECONDS.sleep(timestep);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tstep++;
		}		
	}
	
	public void outPositions(int currentTimestep) {
		System.out.println("Timestep: "+currentTimestep);
		for(int i=0;i<populationField.size();i++) {

			System.out.println("Citizen "+ (i+1) +
							   " : x="   + populationField.get(i).getPosition()[0] +
							   ", y="    + populationField.get(i).getPosition()[1] );
		}
		System.out.println("------------------------------------------------------");
	}
	
	public void outHealthStats() {
		int nrInfected=0;
		int nrFine=0;
		int nrRemoved=0;
		DecimalFormat numberFormat =  new DecimalFormat("##.##");
		for(int i=0;i<populationField.size();i++) {
			RandomDude citizen = (RandomDude) populationField.get(i);
			if(citizen.getHealthStatus()==0) {
				nrFine++;
			} else if (citizen.getHealthStatus()==1) {
				nrInfected++;
			} else if (citizen.getHealthStatus()==2) {
				nrRemoved++;
			}
		}
		double infRate = (double) (nrInfected) / populationField.size() * 100;
		double immRate = (double) (nrRemoved) / populationField.size()  * 100;
		double receptRate = (double) (nrFine) / populationField.size()  * 100;
		System.out.println("Infection Rate: "+numberFormat.format(infRate) );
		// System.out.println("Immunity Rate: "+immRate);
		// Remember to change array size in Main!!! 
		double[] dataOut = new double[4];
		dataOut[0] = infRate;
		dataOut[1] = receptRate;
		dataOut[2] = immRate;
		dataOut[3] = 0;
		realTimePlotElement.updateChart(dataOut);
	}
	
	
	
	public void updateGUI() {
		if(guiEnvironment!=null) {
			guiEnvironment.updatePopulation();
		}
	}

	public List<Citizen> getPopulationField() {
		return populationField;
	}

	public void setBoPosOut(boolean boPosOut) {
		this.boPosOut = boPosOut;
	}

	public EnvironmentPane getGuiEnvironment() {
		return guiEnvironment;
	}

	public void setGuiEnvironment(EnvironmentPane guiEnvironment) {
		this.guiEnvironment = guiEnvironment;
	}

	public void setBoHealthOut(boolean boHealthOut) {
		this.boHealthOut = boHealthOut;
	}

	public void setRealTimePlotElement(RealTimePlotElement realTimePlotElement) {
		this.realTimePlotElement = realTimePlotElement;
	}
	
}
