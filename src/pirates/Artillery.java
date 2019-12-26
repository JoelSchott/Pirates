package pirates;

import java.awt.*;

public abstract class Artillery {
	
	private Point location;
	
	private double crewChance;
	private int hullDamage;
	private int range;
	private int angle;
	
	private final Point initialLocation;
	
	public Artillery(Point location, int angle, double crewDamage, int hull, int range){
		this.location = location;
		this.crewChance = crewDamage;
		this.hullDamage = hull;
		this.range = range;
		this.angle = angle;
		this.initialLocation = new Point(location);
	}
	
	public Point getLocation(){
		return location;
	}
	public double getCrewChance(){
		return crewChance;
	}
	public int getHullDamage(){
		return hullDamage;
	}
	public int getRange(){
		return range;
	}
	public int getAngle(){
		return angle;
	}
	public void move(int speed){
		int deltax = (int)(Math.cos(Math.toRadians(getAngle())) * speed);
		int deltay = (int)(Math.sin(Math.toRadians(getAngle())) * -speed);
		location.x += deltax;
		location.y += deltay;
	}
	public boolean outOfRange(){
		boolean out = false;
		
		double x = Math.abs(getLocation().x - initialLocation.x);
		double y = Math.abs(getLocation().y - initialLocation.y);
		
		System.out.println("x is " + x);
		System.out.println("y is " + y);
		
		double distance = Math.sqrt((x*x) + (y*y));
		if (distance > getRange()){
			out = true;
		}
		System.out.println("distance is " + distance);
		System.out.println("range is " + range);
		return out;
	}

}
