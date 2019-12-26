package pirates;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public abstract class Ship {
	
	private Point location;
	private int angle = 0;
	
	private int sails;
	
	private int bowCannons;
	private int sideCannons;
	private int maxHull;
	private int cargoCapacity;
	
	private int crewCapacity;
	private int sailors;
	private int soldiers;
	
	private int frontCannons = 0;
	private int rightCannons = 0;
	private int backCannons = 0;
	private int leftCannons = 0;
	
	private final int RELOAD_TIME = 10000;
	private int frontReload = RELOAD_TIME;
	private int rightReload = RELOAD_TIME;
	private int backReload = RELOAD_TIME;
	private int leftReload = RELOAD_TIME;
	
	private BufferedImage image;
	private File imageFile;
	
	private final Point initialCenter;
	private final int WIDTH;
	private final int HEIGHT;
	
	private ArrayList<String> goods = new ArrayList<String>();

	public Ship(Point location, int crew, int sails, int front, int side, int hull, int cargo, String address){
		this.location = location;
		crewCapacity = crew;
		this.sails = sails;
		bowCannons = front;
		sideCannons = side;
		maxHull = hull;
		this.cargoCapacity = cargo;
		imageFile = new File(address);
		try {
			image = ImageIO.read(imageFile);
		} catch (IOException e) {
			System.out.println("problem with ship image");
		}
		WIDTH = image.getWidth();
		HEIGHT = image.getHeight();
		initialCenter = new Point(location.x + WIDTH/2, location.y + HEIGHT/2);

		
	}
	public ArrayList<String> getCargo(){
		return goods;
	}
	public int getCargoCapacity(){
		return cargoCapacity;
	}
	public void addCargo(String s){
		if (getCargo().size() < cargoCapacity){
			goods.add(s);
		}
	}
	public void removeCargo(String s){
		if (getCargo().contains(s)){
			goods.remove(s);
		}
	}
	public Point getLocation(){
		return location;
	}
	public void setLocation(Point p){
		location = p;
	}
	public Point getCenter(){
		int x = getLocation().x + WIDTH/2;
		int y = getLocation().y + HEIGHT/2;
		return new Point(x,y);
		
	}
	public Point getInitialCenter(){
		return initialCenter;
	}
	public int getWidth(){
		return WIDTH;
	}
	public int getHeight(){
		return HEIGHT;
	}
	
	public int getCrewCapacity(){
		return crewCapacity;
	}
	public int getSailors(){
		return sailors;
	}
	public int getSoldiers(){
		return soldiers;
	}
	public void addSoldiers(int amount){
		if (getSoldiers() + amount < getCrewCapacity()){
			soldiers += amount;
		}
	}
	public void addSailors(int amount){
		if (getSailors() + amount < getCrewCapacity()){
			sailors += amount;
		}
	}
	
	public int getSails(){
		return sails;
	}
	
	public int sailorsNeeded(){
		return getSails()*20;
	}
	public int frontCannonCapacity(){
		return bowCannons;
	}
	public int sideCannonCapacity(){
		return sideCannons;
	}
	public int getMaxHull(){
		return maxHull;
	}
	public int getAngle(){
		return angle;
	}
	public void setAngle(int a){
		angle = a;
	}
	public int getFrontCannons(){
		return frontCannons;
	}
	public int getRightCannons(){
		return rightCannons;
	}
	public int getBackCannons(){
		return backCannons;
	}
	public int getLeftCannons(){
		return leftCannons;
	}
	public double frontReloadPercent(){
		return (double)(frontReload)/(double)(RELOAD_TIME);
	}
	public double rightReloadPercent(){
		return (double)rightReload/(double)RELOAD_TIME;
	}
	public double backReloadPercent(){
		return (double)backReload/(double)RELOAD_TIME;
	}
	public double leftReloadPercent(){
		return (double)leftReload/(double)RELOAD_TIME;
	}
	
	public void addFrontCannon(){
		if (getFrontCannons() != frontCannonCapacity()){
			frontCannons ++;
		}
	}
	public void addRightCannon(){
		if (getRightCannons() != sideCannonCapacity()){
			rightCannons++;
		}
	}
	public void addBackCannon(){
		if (getBackCannons() != frontCannonCapacity()){
			backCannons++;
		}
	}
	public void addLeftCannon(){
		if (getLeftCannons() != sideCannonCapacity()){
			leftCannons++;
		}
	}
	public ArrayList<Artillery> fireFrontCannons(Main1.artilleryType t){
		
		ArrayList<Artillery> list = new ArrayList<Artillery>();
		int frontx = (int)(getCenter().x + Math.cos(Math.toRadians(getAngle()))* WIDTH/2);
		int fronty = (int)(getCenter().y - Math.sin(Math.toRadians(getAngle())) * HEIGHT/2);
		
		int eachSide = getFrontCannons()/2;
		
		
		for (int i = 0;i < eachSide; i ++){
			int xOffset = (int)(Math.sin(Math.toRadians(getAngle())) * (i+1) * 6);
			int yOffset = (int)(Math.cos(Math.toRadians(getAngle())) * (i+1) * 6);
			if (t.equals(Main1.artilleryType.CANNON_BALL)){
				CannonBall a = new CannonBall(new Point(frontx + xOffset, fronty + yOffset), getAngle());
				CannonBall b = new CannonBall(new Point(frontx - xOffset, fronty - yOffset), getAngle());
				list.add(a);
				list.add(b);
			}	
		}
		if (getFrontCannons()%2 == 1){
			CannonBall c = new CannonBall(new Point(frontx, fronty), getAngle());
			list.add(c);
		}

		if (frontReload < RELOAD_TIME){
			list = new ArrayList<Artillery>();
		}
		else{
			frontReload = 0;
		}
		return list; 
		
	}
	
	public ArrayList<Artillery> fireBackCannons(Main1.artilleryType t){
		
		ArrayList<Artillery> list = new ArrayList<Artillery>();
		int backx = (int)(getCenter().x - Math.cos(Math.toRadians(getAngle()))* WIDTH/2);
		int backy = (int)(getCenter().y + Math.sin(Math.toRadians(getAngle())) * HEIGHT/2);
		
		int eachSide = getBackCannons()/2;
		
		
		for (int i = 0;i < eachSide; i ++){
			int xOffset = (int)(Math.sin(Math.toRadians(getAngle())) * (i+1) * 6);
			int yOffset = (int)(Math.cos(Math.toRadians(getAngle())) * (i+1) * 6);
			if (t.equals(Main1.artilleryType.CANNON_BALL)){
				CannonBall a = new CannonBall(new Point(backx + xOffset, backy + yOffset), getAngle() + 180);
				CannonBall b = new CannonBall(new Point(backx - xOffset, backy - yOffset), getAngle() + 180);
				list.add(a);
				list.add(b);
			}	
		}
		if (getBackCannons()%2 == 1){
			CannonBall c = new CannonBall(new Point(backx, backy), getAngle() + 180);
			list.add(c);
		}

		if (backReload < RELOAD_TIME){
			list = new ArrayList<Artillery>();
		}
		else{
			backReload = 0;
		}
		return list; 
		
	}
	public ArrayList<Artillery> fireRightCannons(Main1.artilleryType t){
		
		ArrayList<Artillery> list = new ArrayList<Artillery>();
		int rightx = (int)(getCenter().x + Math.cos(Math.toRadians(getAngle()-90))* WIDTH/2);
		int righty = (int)(getCenter().y - Math.sin(Math.toRadians(getAngle()-90)) * HEIGHT/2);
		
		int eachSide = getRightCannons()/2;
		
		
		for (int i = 0;i < eachSide; i ++){
			int xOffset = (int)(Math.cos(Math.toRadians(getAngle())) * (i+1) * 6);
			int yOffset = (int)(Math.sin(Math.toRadians(getAngle())) * (i+1) * -6);
			if (t.equals(Main1.artilleryType.CANNON_BALL)){
				CannonBall a = new CannonBall(new Point(rightx + xOffset, righty + yOffset), getAngle() - 90);
				CannonBall b = new CannonBall(new Point(rightx - xOffset, righty - yOffset), getAngle() - 90);
				list.add(a);
				list.add(b);
			}	
		}
		if (getRightCannons()%2 == 1){
			CannonBall c = new CannonBall(new Point(rightx, righty), getAngle() - 90);
			list.add(c);
		}

		if (rightReload < RELOAD_TIME){
			list = new ArrayList<Artillery>();
		}
		else{
			rightReload = 0;
		}
		return list; 
		
	}
	public ArrayList<Artillery> fireLeftCannons(Main1.artilleryType t){
		
		ArrayList<Artillery> list = new ArrayList<Artillery>();
		int leftx = (int)(getCenter().x + Math.cos(Math.toRadians(getAngle()+90))* WIDTH/2);
		int lefty = (int)(getCenter().y - Math.sin(Math.toRadians(getAngle()+90)) * HEIGHT/2);
		
		int eachSide = getLeftCannons()/2;
		
		
		for (int i = 0;i < eachSide; i ++){
			int xOffset = (int)(Math.cos(Math.toRadians(getAngle())) * (i+1) * 6);
			int yOffset = (int)(Math.sin(Math.toRadians(getAngle())) * (i+1) * -6);
			if (t.equals(Main1.artilleryType.CANNON_BALL)){
				CannonBall a = new CannonBall(new Point(leftx + xOffset, lefty + yOffset), getAngle() + 90);
				CannonBall b = new CannonBall(new Point(leftx - xOffset, lefty - yOffset), getAngle() + 90);
				list.add(a);
				list.add(b);
			}	
		}
		if (getLeftCannons()%2 == 1){
			CannonBall c = new CannonBall(new Point(leftx, lefty), getAngle() + 90);
			list.add(c);
		}

		if (leftReload < RELOAD_TIME){
			list = new ArrayList<Artillery>();
		}
		else{
			leftReload = 0;
		}
		return list; 
		
	}
	
	
	public void reload(int time){
		if (frontReload < RELOAD_TIME){
			frontReload += time;
		}
		if (frontReload > RELOAD_TIME){
			frontReload = RELOAD_TIME;
		}
		if (rightReload < RELOAD_TIME){
			rightReload += time;
		}
		if (rightReload > RELOAD_TIME){
			rightReload = RELOAD_TIME;
		}
		if (backReload < RELOAD_TIME){
			backReload += time;
		}
		if (backReload > RELOAD_TIME){
			backReload = RELOAD_TIME;
		}
		if (leftReload < RELOAD_TIME){
			leftReload += time;
		}
		if (leftReload > RELOAD_TIME){
			leftReload = RELOAD_TIME;
		}
	}
	
	public BufferedImage getImage(){
		try {
			image = ImageIO.read(imageFile);
		} catch (IOException e) {
			System.out.println("problem reading image");
		}
		AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(-angle + 90), WIDTH/2 , HEIGHT/2);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        return image = op.filter(image,null);
	}
	
	public int getSpeed(){
		double sailSpeed = 2*getSails();
		double hullPercent = getHull()/getMaxHull();
		double sailorPercent = getSailors()/sailorsNeeded();
		return (int)(sailSpeed * hullPercent * sailorPercent);
	}
	public void sailForward(){
		int deltax = (int)(Math.cos(Math.toRadians(angle))*getSpeed());
		int deltay = (int)(Math.sin(Math.toRadians(angle))*-getSpeed());
		location.x = getLocation().x + deltax;
		location.y = getLocation().y + deltay;	
	}
	public void sailFrontLeft(){
		sailForward();
		angle += getSpeed()/4;
	}
	public void sailFrontRight(){
		sailForward();
		angle -= getSpeed()/4;
	}
	public void sailBackLeft(){
		sailBack();
		angle += getSpeed()/4;
	}
	public void sailBackRight(){
		sailBack();
		angle -= getSpeed()/4;
	}
	public void sailBack(){
		int deltax = (int)(Math.cos(Math.toRadians(angle))*getSpeed());
		int deltay = (int)(Math.sin(Math.toRadians(angle))*-getSpeed());
		location.x = getLocation().x - deltax;
		location.y = getLocation().y - deltay;	
	}
	
	public abstract int getHull();
	
	
	
	
}
