package pirates;

import java.awt.Point;
import java.util.ArrayList;

public class Pirate extends Ship{
	
	public Pirate(Point location){
		super(location, 100, 2, 3, 8, 100, 20, "src\\images\\Pirate.jpg");
	}
	
	private int hull = getMaxHull();
	

	
	@Override
	public int getHull(){
		return hull;
	}
	
	
	


	
	
	
}
