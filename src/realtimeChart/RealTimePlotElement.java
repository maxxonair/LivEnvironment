package realtimeChart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import simPlatform.SimEnvironment;

public class RealTimePlotElement {

	
	 public static double PI = 3.14159265358979323846264;
		@SuppressWarnings("unused")
		private final static double deg2rad = PI/180.0; 		//Convert degrees to radians
		//private final static double rad2deg = 180/PI; 		//Convert radians to degrees
    
   private List<InputFileSet> resultFile = new ArrayList<InputFileSet>(); 
   
   private XYSeriesCollection resultSet = new XYSeriesCollection();
   
   private ChartPanel chartPanel;
   
   private Color[] seriesColor = new Color[4];
  // seriesColor[0] = Color.RED;
   
   private int ID;
   private Color backgroundColor;
   private Color labelColor;

   private int nPlot; 
   private double[] dataOut;
   
   public RealTimePlotElement(int plotHorizon, double plotFrequency, double[] dataOut, SimEnvironment simEnvironment) {
	   
	   	nPlot = (int) (plotFrequency * plotHorizon); 
	   	this.dataOut=dataOut;
	   	// Self Register to link real time chart output to simulation results 
	   	simEnvironment.setRealTimePlotElement(this);
	   	
	   	for(int j=0;j<dataOut.length;j++) {
			   InputFileSet val_01 = new InputFileSet(nPlot, plotFrequency);
			   val_01.setLegend(true);
			   if(j==0) {
				   val_01.setInputDataFileName("Infection Rate");
			   } else if (j==1) {
				   val_01.setInputDataFileName("Receptives Rate");   
			   } else if (j==2) {
				   val_01.setInputDataFileName("Removed Rate");   
			   } else {
				   val_01.setInputDataFileName("");  
			   }
			   resultFile.add(val_01);
	   	}
	   	
	   
	     labelColor = new Color(220,220,220);   
	   	 backgroundColor = new Color(41,41,41);
	   	 
	   	seriesColor[0] = Color.RED;
	   	seriesColor[1] = Color.BLUE;
	   	seriesColor[2] = Color.WHITE;
	   	seriesColor[3] = Color.GREEN;
	   	
	   	createPlotElement();

   }
   
	
	public JPanel createPlotElement() {
	   	 
       JPanel panel = new JPanel();
       panel.setLayout(new BorderLayout());
       panel.setBackground(backgroundColor);

	      try {
			chartPanel = createChartPanel();
		      panel.add(chartPanel, BorderLayout.CENTER);
			} catch (Exception e) {
				System.err.println("ERROR: Chart Panel could not be created.");
			}
       
		return panel;
	}
	
	public ChartPanel createChartPanel() throws IOException {
		try {
			resultSet = updateData(dataOut);
		} catch(Exception eFNF2) {
			System.out.println("Error: creating initial resultSet failed. "+eFNF2);
		}
	    //-----------------------------------------------------------------------------------
		JFreeChart chart = ChartFactory.createScatterPlot("", "", "", resultSet, PlotOrientation.VERTICAL, false, false, false); 
		XYPlot plot = (XYPlot)chart.getXYPlot(); 
		//try {
		    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
			Font font3 = new Font("Dialog", Font.PLAIN, 12);
		    double size = 2.0;
		    double delta = size / 2.0;
			Shape dot = new Ellipse2D.Double(-delta, -delta, size, size);
		
		    for(int i=0;i<resultFile.size();i++) {
			    plot.setRenderer(i, renderer);
				plot.getDomainAxis().setLabelFont(font3);
				plot.getRangeAxis().setLabelFont(font3);
				plot.getRangeAxis().setLabelPaint(labelColor);
				plot.getDomainAxis().setLabelPaint(labelColor);
				plot.setForegroundAlpha(0.5f);
				plot.setBackgroundPaint(backgroundColor);
				plot.setDomainGridlinePaint(labelColor);
				plot.setRangeGridlinePaint(labelColor); 
				plot.getRangeAxis().setLabel("[-]");
				plot.getDomainAxis().setLabel("time [-]");
			    renderer.setSeriesPaint( i , seriesColor[i]);
				renderer.setSeriesShape(i, dot);
		    }
		
			chart.setBackgroundPaint(backgroundColor); 	
			final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
			rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
	
			ChartPanel chartPanel = new ChartPanel(chart);
			chartPanel.setMaximumSize(new Dimension(1500,250));
			/*
			chartPanel.setMaximumDrawHeight(200);
			chartPanel.setMaximumDrawWidth(50000);
			chartPanel.setMinimumDrawHeight(0);
			chartPanel.setMinimumDrawWidth(0);
			*/
	   return chartPanel;
	}

	
	public void updateChart(double[] dataOut){
	   	try {
	   	   	resultSet.removeAllSeries();
	   		resultSet = updateData(dataOut);
	   	} catch(Exception eFNF2) {
	   		
	   	}
	}
	
	
	public XYSeriesCollection updateData(double[] dataOut) {
		try {
		   	resultSet.removeAllSeries();
			for(int i=0;i<resultFile.size();i++) {
				XYSeries xySeries = new XYSeries(""+i+""+resultFile.get(i).getInputDataFileName(), false, true); 

					resultFile.get(i).updatePlotData(dataOut[i]);
				

				for(int j=0;j<resultFile.get(i).plotData.length;j++) {
					xySeries.add(resultFile.get(i).plotData[j][0] , resultFile.get(i).plotData[j][1]);
				}
			resultSet.addSeries(xySeries); 
			}
		} catch (Exception excp ) {
			
		}
	return resultSet;
	}
		
	public int getID() {
		return ID;
	}

	public ChartPanel getChartPanel() {
		return chartPanel;
	}

}
