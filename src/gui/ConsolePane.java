package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.PrintStream;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import utils.TextAreaOutputStream;

public class ConsolePane {
	
	private JPanel mainPanel;
	
	public static Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    public static JTextArea textArea 	  = new JTextArea();
    private static TextAreaOutputStream  taOutputStream = new TextAreaOutputStream(textArea, ""); 
	
    public static Color labelColor = new Color(220,220,220);    					// Label Color
   	public static Color backgroundColor = new Color(41,41,41);				    // Background Color
	
	public ConsolePane() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(backgroundColor);
		mainPanel.setPreferredSize(new Dimension(420,450));
	      //-----------------------------------------------------------------------------------------------
	      //								Console Window        
	      //-----------------------------------------------------------------------------------------------
	        int console_size_x = 390;
	        int console_size_y = 270; 
	        int uy_p41 		   = 10;

	        
	        JPanel JP_EnginModel = new JPanel();
	        JP_EnginModel.setSize(console_size_x,console_size_y);
	        JP_EnginModel.setLocation(5, uy_p41 + 25 * 18);
	        JP_EnginModel.setBackground(backgroundColor);
	        JP_EnginModel.setForeground(labelColor);
	         JP_EnginModel.setBorder(BorderFactory.createEmptyBorder(1,1,1,1)); 
	        //JP_EnginModel.setBackground(Color.red);
	        taOutputStream = null; 
	        taOutputStream = new TextAreaOutputStream(textArea, ""); 
	        textArea.setForeground(labelColor);
	        textArea.setBackground(backgroundColor);
	        JScrollPane JSP_EnginModel = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
	        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	        JSP_EnginModel.setBackground(backgroundColor);
	        JSP_EnginModel.setForeground(labelColor);
	        JSP_EnginModel.getVerticalScrollBar().setForeground(labelColor);
	        JSP_EnginModel.getHorizontalScrollBar().setForeground(labelColor);
	        JSP_EnginModel.getHorizontalScrollBar().setBackground(backgroundColor);
	        JSP_EnginModel.getVerticalScrollBar().setBackground(backgroundColor);
	        JSP_EnginModel.setOpaque(true);
	        JSP_EnginModel.setPreferredSize(new Dimension(console_size_x-5,console_size_y-10));
	        JSP_EnginModel.setLocation(5, 5);
	        JP_EnginModel.add(JSP_EnginModel);
	        System.setOut(new PrintStream(taOutputStream));  
	        //-----------------------------------------------------------------------------------------------
	        mainPanel.add(JP_EnginModel, BorderLayout.CENTER);
	        //-----------------------------------------------------------------------------------------------
	}
	
	public JPanel getMainPanel() {
		return mainPanel;
	}

}
