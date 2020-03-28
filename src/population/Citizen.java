package population;

import java.awt.Color;

public class Citizen implements Cloneable {

	private int[] position  = new int[2];
	private int[] fieldSize = new int[2];
	
	private Color color;
	
	public Citizen(int[] position, int[] fieldSize) {
		this.position=position;
		this.fieldSize=fieldSize;
		
		this.color = Color.BLUE;
	}

	public int[] getPosition() {
		return position;
	}

	public void setPosition(int[] position) {
		this.position = position;
	}

	public int[] getFieldSize() {
		return fieldSize;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
		
}
