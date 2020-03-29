package simPlatform;

import java.io.File;
import java.io.PrintWriter;
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
	private double mortalityRate;
	private int populationsSize;
	//----------------------------------------------------------------
	// GUI Variables
	EnvironmentPane guiEnvironment ;	
	RealTimePlotElement realTimePlotElement;
	//----------------------------------------------------------------
	// Script Control Variables 
	boolean boPosOut=false;
	boolean boHealthOut=false;
	boolean boCreateOutFile=false;
	

	//----------------------------------------------------------------
	// Output variables 
	private double[] dataOut;
	
	private String strFileName;
	private String strOutputFileFormat=".pandaOut";
	
	public SimEnvironment(int[] fieldSize, int citizenStepSize, int timeIncrement, double[] dataOut, String strFileName) {
		this.fieldSize=fieldSize;
		this.citizenStepSize=citizenStepSize;
		this.timeIncrement=timeIncrement; 
		this.dataOut=dataOut;
		this.strFileName=strFileName;
		
	}
	
	public void createPopulation(int populationSize, int infectionRadius, int riskOfInfection, 
								double initInfectionRate, double mortalityRate, double sickLeave) {
		int nrOfSick = (int) (initInfectionRate*populationSize);
		for(int i=0;i<populationSize;i++) {
			int healthstatus = 0;
			if(i< (nrOfSick)) {
				healthstatus = 1;
			}
			int[] initPos = new int[2];
			initPos[0] = rand.nextInt(fieldSize[0]);
			initPos[1] = rand.nextInt(fieldSize[1]);
			
			this.mortalityRate=mortalityRate;
			this.populationsSize=populationSize;
			
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
					int inMortRate = (int) (mortalityRate * 1000);
					int nerosCall = rand.nextInt(1001);
					if(nerosCall > inMortRate) {
						citizen.setHealthStatus(2);  // set to removed and alive
					} else {
						citizen.setHealthStatus(3);  // set to removed and dead
					}
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
		ArrayList<String> steps = new ArrayList<String>();
		int tstep=0;
		while(tstep<timeSteps) {
			//-------------------------
			// Add Simulation Actions here 
			populationMove();
			updateContamination();
			// Update GUI
			updateGUI();
			// Output
			if(boPosOut)    		{outPositions(tstep);}
			if(boHealthOut) 		{outHealthStats();}
			if(boCreateOutFile) {addOutputTimestepData(steps, tstep ,timestep, populationField);}
			//-------------------------
			try {
				TimeUnit.MILLISECONDS.sleep(timestep);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tstep++;
		}	
		if(boCreateOutFile) {createWriteOut(steps);}
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
		int nrDead=0;
		DecimalFormat numberFormat =  new DecimalFormat("##.##");
		for(int i=0;i<populationField.size();i++) {
			RandomDude citizen = (RandomDude) populationField.get(i);
			if(citizen.getHealthStatus()==0) {
				nrFine++;
			} else if (citizen.getHealthStatus()==1) {
				nrInfected++;
			} else if (citizen.getHealthStatus()==2) {
				nrRemoved++;
			} else if (citizen.getHealthStatus()==3) {
				nrDead++;
			}
		}
		double infRate    = (double) (nrInfected) / populationField.size()  * 100;
		double immRate    = (double) (nrRemoved)  / populationField.size()  * 100;
		double receptRate = (double) (nrFine)     / populationField.size()  * 100;
		double deathRate  = (double) (nrDead)     / populationField.size()  * 100;
		System.out.println("Infection Rate: "+numberFormat.format(infRate) );
		// System.out.println("Immunity Rate: "+immRate);

		dataOut[0] = infRate;
		dataOut[1] = receptRate;
		dataOut[2] = immRate;
		dataOut[3] = deathRate;
		realTimePlotElement.updateChart(dataOut);
	}
	
	private static ArrayList<String> addOutputTimestepData(ArrayList<String> steps, int timeIs, int timesteps, List<Citizen> populationField) {
	double time = (double) (timeIs) / ( 1000 / (double) (timesteps)  ); 
	String strLine ="";
	strLine += time +" ";
	steps.add(strLine);
	return steps;	
	}

	private void createWriteOut(ArrayList<String> steps) {
        try{
            String resultpath="";
            	String dir = System.getProperty("user.dir");
            	resultpath = dir + "/" + strFileName+ ""+ strOutputFileFormat;
            PrintWriter writer = new PrintWriter(new File(resultpath), "UTF-8");
            for(String step: steps) {
                writer.println(step);
            }
            System.out.println("WRITE: Result file >> Complete"); 
            System.out.println("------------------------------------------");
            writer.close();
        } catch(Exception e) {System.out.println("ERROR: Writing result file failed");System.out.println(e);};
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

	public int getPopulationsSize() {
		return populationsSize;
	}

	public void setStrFileName(String strFileName) {
		this.strFileName = strFileName;
	}

	public void setStrOutputFileFormat(String strOutputFileFormat) {
		this.strOutputFileFormat = strOutputFileFormat;
	}
	
	public void setBoCreateOutFile(boolean boCreateOutFile) {
		this.boCreateOutFile = boCreateOutFile;
	}
}
