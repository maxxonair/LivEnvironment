package realtimeChart;


public class InputFileSet {
	
	private int ID;
	private String inputDataFileName="";
	
	private boolean legend=true; 
	
	 public double[][] plotData ;
	
	public InputFileSet(int nPlot, double frequency) {
		plotData = initPlotData( nPlot,  frequency);
	}
	
	private double[][] initPlotData( int nPlot, double frequency) {
		plotData = new double[nPlot][2];
		double time=0;
		double timeStep = (double) (1/frequency);
		for(int i=0;i<nPlot;i++) {
			//DataPair line = new DataPair(time, 0);
			plotData[i][0] = time;
			plotData[i][1] = 0;
			time += timeStep;
		}
		return plotData; 
	}
	
	public double[][] updatePlotData(double newVal){
		double[][] interimData = new double[plotData.length][2];
		for(int i=0;i<plotData.length;i++) {
			interimData[i][0] = plotData[i][0];
			interimData[i][1] = plotData[i][1];
		}
		plotData[0][1] = newVal;
		for(int i=1;i<plotData.length;i++) {
			plotData[i][1] = interimData[i-1][1];
		}
		return plotData;
	}

	public boolean isLegend() {
		return legend;
	}


	public void setLegend(boolean legend) {
		this.legend = legend;
	}

	public String getInputDataFileName() {
		return inputDataFileName;
	}

	public void setInputDataFileName(String inputDataFileName) {
		this.inputDataFileName = inputDataFileName;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	

}
