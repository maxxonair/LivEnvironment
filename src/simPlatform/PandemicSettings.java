package simPlatform;

public class PandemicSettings extends Object implements Cloneable {
	
    // Pandemic Settings 
    private int infectionRadius ;	    	   // Infection box half length [pixel/unit]
    private int riskOfInfection ;		   // Risk of infection during single encounter [%]
    private double sickLeave ;  	     	   // Time period of sickness (from infection to removed) [pixel/unit]
    private double mortalityRate ;		   // Mortality rate after incubation period
    
    private double symptomRate;			   // Percentage of sick individuals entering quarantine [%]
    private double timeToSymptoms;		       // Time from infection to showing symptoms
    private boolean boEnterQuarantine;    // Include Quarantine
    private double riskOfMisdiagnose;	   // Risk of misdiagnosis, missing tests, no symptoms 
									   // -> sick infectious citizens are not quarantined
    
    // Init health setting 
    private double initInfectionRate;   // Initial amount of infected citizens
	
	public PandemicSettings(){
		
		
	}

	public int getInfectionRadius() {
		return infectionRadius;
	}

	public void setInfectionRadius(int infectionRadius) {
		this.infectionRadius = infectionRadius;
	}

	public int getRiskOfInfection() {
		return riskOfInfection;
	}

	public void setRiskOfInfection(int riskOfInfection) {
		this.riskOfInfection = riskOfInfection;
	}

	public double getSickLeave() {
		return sickLeave;
	}

	public void setSickLeave(double sickLeave) {
		this.sickLeave = sickLeave;
	}

	public double getMortalityRate() {
		return mortalityRate;
	}

	public void setMortalityRate(double mortalityRate) {
		this.mortalityRate = mortalityRate;
	}

	public double getSymptomRate() {
		return symptomRate;
	}

	public void setSymptomRate(double symptomRate) {
		this.symptomRate = symptomRate;
	}

	public double getTimeToSymptoms() {
		return timeToSymptoms;
	}

	public void setTimeToSymptoms(double timeToSymptoms) {
		this.timeToSymptoms = timeToSymptoms;
	}

	public boolean isBoEnterQuarantine() {
		return boEnterQuarantine;
	}

	public void setBoEnterQuarantine(boolean boEnterQuarantine) {
		this.boEnterQuarantine = boEnterQuarantine;
	}

	public double getRiskOfMisdiagnose() {
		return riskOfMisdiagnose;
	}

	public void setRiskOfMisdiagnose(double riskOfMisdiagnose) {
		this.riskOfMisdiagnose = riskOfMisdiagnose;
	}

	public double getInitInfectionRate() {
		return initInfectionRate;
	}

	public void setInitInfectionRate(double initInfectionRate) {
		this.initInfectionRate = initInfectionRate;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {

	    return super.clone();
	}
}
