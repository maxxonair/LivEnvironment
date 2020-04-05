package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBar {
	
	//-------------------------------------------------------------------------------------------------------------
	
	private JMenuBar menuBar;
	//-------------------------------------------------------------------------------------------------------------
	// Formatting valules (Fonts, Borders, decimalLayouts etc):	
	
    private Color labelColor;
    
	Font smallFont			  = new Font("Verdana", Font.LAYOUT_LEFT_TO_RIGHT, 10);
    //-------------------------------------------------------------------------------------------------------------
    // Global GUI components:
	
	//-------------------------------------------------------------------------------------------------------------
    // Content Lists 
   	private int simState=0;
	// 0 - Not started 
   	// 1 - Running 
   	// 2 - Paused 
    //--------------
	
	public MenuBar(JFrame mainFrame, Thread simulationMain) {
	       menuBar = new JMenuBar();
		   menuBar.setOpaque(true);
		   menuBar.setPreferredSize(new Dimension(1200, 25));
	       
	       //Build the first menu.
	       JMenu mainMenu = new JMenu("Main");
	       mainMenu.setOpaque(true);
	    		mainMenu.setBackground(labelColor);
	       mainMenu.setFont(smallFont);
	       mainMenu.setMnemonic(KeyEvent.VK_A);
	       // mainMenu.setIcon(icon_BlueBook);
	       menuBar.add(mainMenu);
	       
	       JMenuItem menuItem_01 = new JMenuItem("Exit                  "); 
	       menuItem_01.setForeground(Color.black);
	       menuItem_01.setFont(smallFont);
	       mainMenu.add(menuItem_01);       
	       
	       menuItem_01.addActionListener(new ActionListener() {
	                  public void actionPerformed(ActionEvent e) {
	                	  mainFrame.dispose();
	                 		
	                   } });
	       mainMenu.addSeparator();
	       
	       JMenuItem menuItem_02 = new JMenuItem("Start/Stop                 "); 
	       menuBar.add(menuItem_02);
	       menuItem_02.setFont(smallFont);
	       menuItem_02.addActionListener(new ActionListener() {
	                  @SuppressWarnings("deprecation")
					public void actionPerformed(ActionEvent e) {
	      				if (simState == 0) {
	    					simulationMain.start();
	    					simState=1;
	    				} else if (simState == 1) {

	    						simulationMain.stop();
	    						simState=2;

	    				} else if (simState == 2) {
	    					simulationMain.start();
	    					simState=1;
	    				}
	                 		
	                   } });
	}
	
	public JMenuBar getMenuBar() {
		return menuBar;
	}
	
}
