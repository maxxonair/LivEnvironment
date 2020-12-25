package simPlatform;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	private int populationsSize;
	
	private PandemicSettings pandemicSetting;
	// -> sick infectious citizens are not quarantined
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
	//----------------------------------------------------------------
	// Statistics data 
	double infRate    = 0;
	double sickRate   = 0;
	double immRate    = 0;
	double receptRate = 0;
	double deathRate  = 0;
	double hospRate   = 0;
	//----------------------------------------------------------------
	// Population data 
	private double[][] distanceMatrix; 
	//----------------------------------------------------------------
	private String strFileName;
	private String strOutputFileFormat=".pandaOut";
	
	public SimEnvironment(int[] fieldSize, int citizenStepSize, int timeIncrement, 
			double[] dataOut, String strFileName, PandemicSettings pandemicSetting) {
		this.fieldSize=fieldSize;
		this.citizenStepSize=citizenStepSize;
		this.timeIncrement=timeIncrement; 
		this.dataOut=dataOut;
		this.strFileName=strFileName;
		this.pandemicSetting=pandemicSetting;
	}
	
	public void createPopulation(int populationSize) {
		this.populationsSize=populationSize;
		
		int nrOfSick = (int) (pandemicSetting.getInitInfectionRate()*populationSize);
		for(int i=0;i<populationSize;i++) {
			int healthstatus = 0;
			if(i< (nrOfSick)) {
				healthstatus = 4;
			}
			int[] initPos = new int[2];
			initPos[0] = rand.nextInt(fieldSize[0]);
			initPos[1] = rand.nextInt(fieldSize[1]);
			//----------------------------------------
			// Mobility type setting 
			int riskMobility = rand.nextInt(100);
			int mobilityType = 0;
			if(riskMobility > 65) {
				mobilityType = (rand.nextInt(2)) + 1;
			}
			
			RandomDude citizen = new RandomDude(initPos, fieldSize, citizenStepSize, healthstatus, 
												mobilityType,pandemicSetting);
			populationField.add(citizen);
		}
		
		distanceMatrix = createDistanceMatrix();
	}
	
	private double[][] createDistanceMatrix(){
		double[][] distanceMatrix = new double[populationsSize][populationsSize];
		for(int i=0; i< populationsSize ;i++) {
			for(int j = i;j< populationsSize ;j++) {
				double vecNorm=0;
				for(int k=0;k<2;k++) {
					double localDiff = (double) ( (double) populationField.get(i).getPosition()[k] - 
												  (double) populationField.get(j).getPosition()[k] );
					vecNorm += localDiff * localDiff;
				} 
				vecNorm = Math.sqrt(vecNorm);
				distanceMatrix[j][i]  =  vecNorm;
			}
		}
		
		for(int i = 0 ; i < populationsSize ; i++) {
			for(int j=0 ; j < i ; j++) {
				distanceMatrix[j][i] = distanceMatrix[i][j];
			}
		}
		
		return distanceMatrix;
	}
	
	public void populationMove() {
		for(int i=0; i<populationField.size();i++) {
			RandomDude citizen = (RandomDude) populationField.get(i);
			citizen.move(populationField);
		}		
	}
	
	public void updateContamination() {
		for(int k=0; k<populationField.size();k++) {
			RandomDude citizen = (RandomDude) populationField.get(k);
			// Detect Citizens in vicinity radius 
			// Loop through vicinity field
			int[] x0 = citizen.getPosition();
			int r = citizen.getPandemicSetting().getInfectionRadius();
			//-------------------------------------------------------------------------------------------
			if(citizen.getHealthStatus() == 0) {
				for(int i=(x0[0]-r); i<=(x0[0]+r);i++) {
					for(int j=(x0[1]-r); j<=(x0[1]+r);j++) {
						for (int l=0; l<populationField.size();l++) {
							int[] cis=new int[2];cis[0]=i; cis[1]=j;
							RandomDude intCit = (RandomDude) populationField.get(l);
							if(k != l && intCit.getPosition()[0] == cis[0] 
									&& intCit.getPosition()[1] == cis[1]  && (intCit.getHealthStatus() == 1 || intCit.getHealthStatus() == 4 )) {  // Sickness assessment 
								citizen.setHealthStatus( contaminationCaseAssessment(citizen.getPandemicSetting().getRiskOfInfection()) ); 
							}
						}
					}
				}

			//-------------------------------------------------------------------------------------------
			} else if (citizen.getHealthStatus() == 1) {
				citizen.updateSickLeave(timeIncrement);  
				if(citizen.getInfectionTime()>citizen.getPandemicSetting().getSickLeave()) {

					int inMortRate = (int) (citizen.getPandemicSetting().getMortalityRate() * 1000);
					int nerosCall = rand.nextInt(1001);
					if(nerosCall > inMortRate) {
						citizen.setHealthStatus(2);  // set to removed and alive and immune
						citizen.setContainmentStatus(0);
					} else {
						citizen.setHealthStatus(3);  // set to removed and dead
						citizen.setContainmentStatus(0);  
					}
				}
				
			//-------------------------------------------------------------------------------------------
			} else if (citizen.getHealthStatus() == 4) {
				citizen.updateSickLeave(timeIncrement); 
				if(citizen.getInfectionTime()>citizen.getPandemicSetting().getTimeToSymptoms() ) {
					citizen.setHealthStatus(1);  // set to sick and contagious
					
					if(citizen.getPandemicSetting().isBoEnterQuarantine()) {
						int quarantRate = (int) (citizen.getPandemicSetting().getSymptomRate() * 1000);
						int quarantineCall = rand.nextInt(1001);
						if(quarantineCall > quarantRate) {
							
							citizen.setContainmentStatus(1);  // Citizen is entering quarantine and is removed from the field
						}
					}
					
				}
				
			}
			
		}
	}
	
	public void updateContamination(boolean useAdvancedMethod) {
		
		distanceMatrix = createDistanceMatrix();
		
		for(int k=0; k<populationField.size();k++) {
			RandomDude citizen = (RandomDude) populationField.get(k);
			double r = (double) (citizen.getPandemicSetting().getInfectionRadius());
			if(citizen.getHealthStatus() == 0) {
				for(int j=0; j<populationField.size();j++) {
					if ( distanceMatrix[k][j] < r && k != j ) {
						RandomDude intCit = (RandomDude) populationField.get(j);
						if (intCit.getHealthStatus() == 1 || intCit.getHealthStatus() == 4 ) {
							citizen.setHealthStatus( contaminationCaseAssessment(citizen.getPandemicSetting().getRiskOfInfection()) ); 
						}
						break;
					}
				}					
				
			} else if (citizen.getHealthStatus() == 1) {
				citizen.updateSickLeave(timeIncrement);  
				if(citizen.getInfectionTime()>citizen.getPandemicSetting().getSickLeave()) {

					int inMortRate = (int) (citizen.getPandemicSetting().getMortalityRate() * 1000);
					int nerosCall = rand.nextInt(1001);
					if(nerosCall > inMortRate) {
						citizen.setHealthStatus(2);  // set to removed and alive and immune
						citizen.setContainmentStatus(0);
					} else {
						citizen.setHealthStatus(3);  // set to removed and dead
						citizen.setContainmentStatus(0);  
					}
				}
				
			//-------------------------------------------------------------------------------------------
			} else if (citizen.getHealthStatus() == 4) {
				citizen.updateSickLeave(timeIncrement); 
				if(citizen.getInfectionTime()>citizen.getPandemicSetting().getTimeToSymptoms() ) {
					citizen.setHealthStatus(1);  // set to sick and contagious
					
					if(citizen.getPandemicSetting().isBoEnterQuarantine()) {
						int quarantRate = (int) (citizen.getPandemicSetting().getSymptomRate() * 1000);
						int quarantineCall = rand.nextInt(1001);
						if(quarantineCall > quarantRate) {
							
							citizen.setContainmentStatus(1);  // Citizen is entering quarantine and is removed from the field
						}
					}
					
				}
				
			}
		}
	}
	
	private int contaminationCaseAssessment(int riskOfInfection) {
		int riskCase = rand.nextInt(100);
		if(riskCase < riskOfInfection ) {
			return 4;
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
			updateContamination(true);
			/* Create write out from distance matrix:
			if (tstep == 0) {
				try {
					writeDistanceMatrixToFile( "distanceMatrix.csv" , " ");
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			*/
			// Update GUI
			updateGUI();
			// Output
			if(boPosOut)    		{outPositions(tstep);}
			if(boHealthOut) 		{outHealthStats();}
			if(boCreateOutFile) {addOutputTimestepData(steps, tstep ,timestep, populationField);}
			//-------------------------
				//TimeUnit.MILLISECONDS.sleep(timestep);
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
		int nrSick=0;
		int nrFine=0;
		int nrRemoved=0;
		int nrDead=0;
		int nrHospital=0;
		DecimalFormat numberFormat =  new DecimalFormat("##.##");
		for(int i=0;i<populationField.size();i++) {
			RandomDude citizen = (RandomDude) populationField.get(i);
			if(citizen.getHealthStatus()==0) {
				nrFine++;
			} else if (citizen.getHealthStatus()==4) {
				nrInfected++;
			} else if (citizen.getHealthStatus()==2) {
				nrRemoved++;
			} else if (citizen.getHealthStatus()==3) {
				nrDead++;
			} else if (citizen.getHealthStatus()==1) {
				nrSick++;
			}
			
			if (citizen.getContainmentStatus()==1) {
				nrHospital++;
			}
		}
		 infRate    = (double) (nrInfected) / populationField.size()  * 100;
		 immRate    = (double) (nrRemoved)  / populationField.size()  * 100;
		 receptRate = (double) (nrFine)     / populationField.size()  * 100;
		 deathRate  = (double) (nrDead)     / populationField.size()  * 100;
		 sickRate   = (double) (nrSick)     / populationField.size()  * 100;
		 hospRate   = (double) (nrHospital) / populationField.size()  * 100;
		 
		System.out.println("Sick Rate: "+numberFormat.format(infRate) );
		// System.out.println("Immunity Rate: "+immRate);

		dataOut[0] = sickRate;
		dataOut[1] = receptRate;
		dataOut[2] = immRate;
		dataOut[3] = deathRate;
		dataOut[4] = infRate;
		dataOut[5] = hospRate;
		
		realTimePlotElement.updateChart(dataOut);
	}
	
	private ArrayList<String> addOutputTimestepData(ArrayList<String> steps, int timeIs, int timesteps, List<Citizen> populationField) {
	double time = (double) (timeIs) / ( 1000 / (double) (timesteps)  ); 
	String strLine ="";
	strLine += time +" ";
	strLine += infRate +" ";
	strLine += immRate +" ";
	strLine += receptRate +" ";
	strLine += deathRate +" ";
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
	
	public void writeDistanceMatrixToFile(String fileName, String delimiter) throws FileNotFoundException, UnsupportedEncodingException {
        String resultpath="";
    	String dir = System.getProperty("user.dir");
    	resultpath = dir + "/"+fileName;
    	PrintWriter writer = new PrintWriter(new File(resultpath), "UTF-8");
		for(int k=0; k<populationField.size();k++) {
			String line = "";
			for(int l=0; l<populationField.size();l++) {
				line += distanceMatrix[k][l]+delimiter;
			}
			writer.println(line);
		}
		writer.close();
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
