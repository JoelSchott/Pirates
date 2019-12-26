package pirates;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Main1 {
	
	public Main1(){
		
		CARGO.put("Wool", 10);
		CARGO.put("Furs", 15);
		CARGO.put("Tea", 18);
		COUNTRIES.put("Iberia", Color.YELLOW);
		COUNTRIES.put("Gallica", Color.RED);
		
		player = new Pirate(new Point(600,500));
		player.addSailors(40);
		
		for (int i = 0; i < 17; i ++){
			player.addCargo(Main1.getCargoName(0));
		}
		player.addCargo(Main1.getCargoName(1));

		
		
		player.addFrontCannon();
		
		for (int i = 0; i < 5; i ++){
			player.addRightCannon();
		}
		for (int i = 0; i < 2; i++){
			player.addBackCannon();
		}
		for (int i = 0; i < 7; i++){
			player.addLeftCannon();
		}
		
		horizontalCoast[0] = 20;
		for (int i = coastWidth; i < seaWidth; i += coastWidth){
			horizontalCoast[i/coastWidth] = horizontalCoast[(i/coastWidth)-1] + (int)((Math.random() - 0.5)*10);	
			if (horizontalCoast[i/coastWidth] < 10){
				horizontalCoast[i/coastWidth] = 10;
			}
		}
		verticalCoast[0] = 20;
		for (int i = coastWidth; i < seaHeight; i += coastWidth){
			verticalCoast[i/coastWidth] = verticalCoast[i/coastWidth - 1] + (int)((Math.random() - 0.5)*10);
			if (verticalCoast[i/coastWidth] < 10){
				verticalCoast[i/coastWidth] = 10;
			}
		}
		
		File portFile = new File("C:\\Eclipse\\workspace\\Java Udemy\\src\\Port.png");
		try {
			PORT_IMAGE = ImageIO.read(portFile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Point p = new Point((int)(seaWidth/3), horizontalCoast[(int)(seaWidth/3/coastWidth)]);
		Port murcia = new Port(p , "Murica", Main1.getCountryName(0), 0, Port.Coast.NORTH);
		ports.add(murcia);
		
		p = new Point(verticalCoast[seaHeight/6/coastWidth], seaHeight/6);
		Port nantes = new Port(p, "Nantes", Main1.getCountryName(1), 0, Port.Coast.WEST);
		ports.add(nantes);
		
		displayPort = ports.get(0);
		
		frame = new JFrame();
		frame.setVisible(true);
		frame.setSize(1600, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("Pirates");
		File frameFile = new File("C:\\Eclipse\\workspace\\Java Udemy\\src\\piratecover.jpg");
		Image image = null;
		try {
			image = ImageIO.read(frameFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.setIconImage(image);
		ocean = new OceanPanel();
		//frame.add(ocean, BorderLayout.EAST);
		frame.getContentPane().add(ocean, BorderLayout.EAST);
		info = new InfoPanel();
		frame.getContentPane().add(info, BorderLayout.WEST);
		frame.pack();
		frame.setSize(1600, 1000);
		input = new Input();
		frame.addKeyListener(input);
		
		
		
	}
	
	private JFrame frame;
	private OceanPanel ocean;
	private InfoPanel info;
	private Input input;
	
	private Pirate player;
	
	private final int LOOP_PAUSE = 50;
	
	private ArrayList<Artillery> artList = new ArrayList<Artillery>();
	private ArrayList<Port> ports = new ArrayList<Port>();
	
	private Port displayPort = null;
	
	private int seaWidth = 3000;
	private int seaHeight = 5000;
	private final int coastWidth = 2;
	
	private Integer[] verticalCoast = new Integer[seaHeight/coastWidth];
	private Integer[] horizontalCoast = new Integer[seaWidth/coastWidth];
	
	public static BufferedImage PORT_IMAGE = null;
	
	public enum artilleryType{
		CANNON_BALL
	}
	
	public static final HashMap<String, Integer> CARGO = new HashMap<String, Integer>();
	public static final HashMap<String, Color> COUNTRIES = new HashMap<String, Color>();
	
	public static String getCargoName(int index){
		return (String)CARGO.keySet().toArray()[index];
	}
	public static String getCountryName(int index){
		return (String)COUNTRIES.keySet().toArray()[index];
	}
	
	public static void main(String[] args){
		Main1 m = new Main1();
		m.loop();
	}
	public void loop(){
		boolean running = true;
		while (running){
			frame.repaint();
			sleep(LOOP_PAUSE);

			sail();
			moveArtillery();
			player.reload(LOOP_PAUSE);

			
		}
	}
	
	private void sail(){
		int left = 37;
		int forward = 38;
		int right = 39;
		int back = 40;
		
		int w  = 87;
		int d = 68;
		int s = 83;
		int a = 65;
		
		int one = 49;
		
		if (input.contains(left) && input.contains(forward)){
			Point tempLoc = new Point(player.getLocation());
			int tempAngle = player.getAngle();
			player.sailFrontLeft();
			if (!inBounds(player)){
				player.setLocation(tempLoc);
				player.setAngle(tempAngle);
			}
		}
		else if (input.contains(right) && input.contains(forward)){
			Point tempLoc = new Point(player.getLocation());
			int tempAngle = player.getAngle();
			player.sailFrontRight();
			if (!inBounds(player)){
				player.setLocation(tempLoc);
				player.setAngle(tempAngle);
			}
		}
		else if (input.contains(left) && input.contains(back)){
			Point tempLoc = new Point(player.getLocation());
			int tempAngle = player.getAngle();
			player.sailBackLeft();
			if (!inBounds(player)){
				player.setLocation(tempLoc);
				player.setAngle(tempAngle);
			}
		}
		else if (input.contains(right) && input.contains(back)){
			Point tempLoc = new Point(player.getLocation());
			int tempAngle = player.getAngle();
			player.sailBackRight();
			if (!inBounds(player)){
				player.setLocation(tempLoc);
				player.setAngle(tempAngle);
			}
		}
		else if (input.contains(forward)){
			Point tempLoc = new Point(player.getLocation());
			int tempAngle = player.getAngle();
			player.sailForward();
			if (!inBounds(player)){
				player.setLocation(tempLoc);
				player.setAngle(tempAngle);
			}
		}
		else if (input.contains(back)){
			Point tempLoc = new Point(player.getLocation());
			int tempAngle = player.getAngle();
			player.sailBack();
			if (!inBounds(player)){
				player.setLocation(tempLoc);
				player.setAngle(tempAngle);
			}
		}
		if (input.contains(w)){
			artList.addAll(player.fireFrontCannons(artilleryType.CANNON_BALL));
		}
		if (input.contains(d)){
			artList.addAll(player.fireRightCannons(artilleryType.CANNON_BALL));
		}
		if (input.contains(s)){
			artList.addAll(player.fireBackCannons(artilleryType.CANNON_BALL));
		}
		if (input.contains(a)){
			artList.addAll(player.fireLeftCannons(artilleryType.CANNON_BALL));
		}

	}
	private void moveArtillery(){
		int speed = 5;
		for (Artillery a : artList){
			a.move(speed);
		}
		Iterator<Artillery> i = artList.iterator();
		while (i.hasNext()){
			Artillery a = i.next();
			if (a.outOfRange()){
				i.remove();
			}
		}
	}
	
	
	private class OceanPanel extends JPanel{
		
		private int width = 1200;
		private int height = 1000;
		private int waveLife = 0;
		
		
		
		private ArrayList<Point> waveLocations = new ArrayList<Point>();
		
		public OceanPanel(){
			this.setBackground(new Color(64, 195, 221));
			this.setPreferredSize(new Dimension(this.width,this.height));
			for (int i = 0; i < 10; i ++){
				int x = (int)(Math.random() * this.width);
				int y = (int) (Math.random() * this.height);
				waveLocations.add(new Point(x,y));
			}
		}
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			int xOffset = this.width/2 - player.getLocation().x;
			int yOffset = this.height/2 - player.getLocation().y;

			for (Point p : waveLocations){
				drawWave(g, p.x + xOffset, p.y + yOffset);
			}
			waveLife += LOOP_PAUSE;
			if (waveLife > 1000){
				waveLocations.remove(0);
				int x = (int)(Math.random() * this.width) + player.getLocation().x - this.width/2;
				int y = (int)(Math.random() * this.height) + player.getLocation().y - this.height/2;
				
				waveLocations.add(new Point(x,y));
				waveLife = 0;
			}
			
			
			g.setColor(Color.ORANGE);
			
			g.drawImage(player.getImage(), this.width/2, this.height/2, this);

//			int degree = 15;
//			double scale = 5.0/6.0;
//			for (int i = 0; i < 90; i += degree){
//				int x = (int)(Math.cos(Math.toRadians(i)) * player.getWidth()/2 * scale);
//				int y = -(int)(Math.sin(Math.toRadians(i)) * player.getHeight()/2 * scale);
//				g.drawRect(player.getCenter().x + x + xOffset, player.getCenter().y + y + yOffset, 3,3);
//				g.drawRect(player.getCenter().x + x + xOffset, player.getCenter().y - y + yOffset, 3,3);
//				g.drawRect(player.getCenter().x - x + xOffset, player.getCenter().y + y + yOffset, 3,3);
//				g.drawRect(player.getCenter().x - x + xOffset, player.getCenter().y - y + yOffset, 3,3);
//				System.out.println("degree is " + i);
//				
//			}
			
			
			g.setColor(new Color(40,140,5));
			for (int i = 0;i < seaWidth/coastWidth; i ++){
				g.fillRect(i*coastWidth + xOffset , 0 + yOffset, coastWidth, horizontalCoast[i]);
				g.fillRect(i*coastWidth + xOffset, seaHeight - horizontalCoast[i] + yOffset, coastWidth, horizontalCoast[i]);
			}
			for (int i = 0; i < seaHeight/coastWidth; i++){
				g.fillRect(0 + xOffset, i * coastWidth + yOffset, verticalCoast[i], coastWidth);
				g.fillRect(seaWidth - verticalCoast[i] + xOffset, i * coastWidth + yOffset, verticalCoast[i], coastWidth);
			}
			
			g.fillRect(0 + xOffset, -1200 + yOffset, seaWidth, 1200);
			g.fillRect(0 + xOffset, seaHeight + yOffset, seaWidth, 1200);
			g.fillRect(-1200 + xOffset, -1200 + yOffset, 1200, seaHeight + 2400);
			g.fillRect(seaWidth + xOffset, -1200 + yOffset, 1200, seaHeight + 2400);
			
			
			for (Port p : ports){
//				int radius = 10;
//				g.setColor(Color.BLUE);
//				g.fillOval(p.getLocation().x - radius + xOffset, p.getLocation().y - radius + yOffset, radius*2, radius*2);
//				radius *= 2;
//				g.setColor(Color.RED);
//				g.drawOval(p.getLocation().x - radius + xOffset, p.getLocation().y - radius + yOffset, radius*2, radius*2);
				
				g.drawImage(PORT_IMAGE, p.getLocation().x + xOffset, p.getLocation().y + yOffset, this);
				
				g.setColor(Color.WHITE);
				g.drawString(p.getName() + " (" + p.getCountry() + ")", p.getLocation().x + xOffset, p.getLocation().y + PORT_IMAGE.getHeight() + yOffset + 20);
				g.setColor(Color.BLACK);
				g.fillRect(p.getLocation().x + PORT_IMAGE.getWidth()/4 + xOffset-3, p.getLocation().y - PORT_IMAGE.getHeight()/4 + yOffset, 3, PORT_IMAGE.getHeight() + 10);
				
				g.setColor(Main1.COUNTRIES.get(p.getCountry()));
				g.fillRect(p.getLocation().x + PORT_IMAGE.getWidth()/4 + xOffset, p.getLocation().y - PORT_IMAGE.getHeight()/4 + yOffset, PORT_IMAGE.getWidth()/2, PORT_IMAGE.getHeight()/4);
				
			}
			
			for (Artillery a : artList){
				if (a.getClass() == CannonBall.class){
					g.setColor(Color.BLACK);
					g.fillOval(a.getLocation().x + xOffset, a.getLocation().y + yOffset, 5, 5);
				}
			}

		}
		
		
		private void drawWave(Graphics g, int x, int y){
			g.setColor(Color.WHITE);
			g.drawArc(x, y, 20, 20, 270, 90);
			
			for (int i = 20 ; i < 60; i+= 20){
				g.drawArc(x + i, y, 20, 20, 180, 180);
				
			}
			g.drawArc(x + 60, y, 20, 20, 180, 90);
			
		}
	}
	
	private class InfoPanel extends JPanel{
		
		private JLabel crewLabel;
		private JLabel sailorLabel;
		private JLabel soldierLabel;
		private JLabel shipLabel;
		private JLabel hullLabel;
		private JLabel sailLabel;
		private JLabel frontLabel;
		private JLabel rightLabel;
		private JLabel backLabel;
		private JLabel leftLabel;
		private JLabel cargoLabel;
		private JLabel portLabel;
		private JLabel displayName;

		private JLabel[] cargoLabels = new JLabel[Main1.CARGO.size()];
		private JLabel[] portLabels = new JLabel[ports.size()];
		private JLabel[] displayLabels = new JLabel[Main1.CARGO.size()];
		
		
		public InfoPanel(){
			this.setPreferredSize(new Dimension(400,1000));
			this.setBackground(Color.LIGHT_GRAY);
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			crewLabel = new JLabel("CREW");	
			this.add(crewLabel);
			this.add(new JLabel(" "));
			sailorLabel = new JLabel();
			this.add(sailorLabel);
			soldierLabel = new JLabel();
			this.add(soldierLabel);
			this.add(new JLabel(" "));
			shipLabel = new JLabel("SHIP");
			this.add(shipLabel);
			this.add(new JLabel(" "));
			hullLabel = new JLabel();
			this.add(hullLabel);
			sailLabel = new JLabel();
			this.add(sailLabel);
			frontLabel = new JLabel();
			this.add(frontLabel);
			rightLabel = new JLabel();
			this.add(rightLabel);
			backLabel = new JLabel();
			this.add(backLabel);
			leftLabel = new JLabel();
			this.add(leftLabel);
			this.add(new JLabel(" "));
			cargoLabel = new JLabel();
			this.add(cargoLabel);
			this.add(new JLabel(" "));
			
				
			for (int i = 0; i < Main1.CARGO.size(); i ++){
				String s = Main1.getCargoName(i);
				JLabel cargoType = new JLabel(s);
				this.add(cargoType);
				
				JLabel valueLabel = new JLabel();
				valueLabel.setLocation(cargoLabel.getX() + 60, cargoLabel.getY());
				valueLabel.setBounds(cargoLabel.getX() + 60, cargoLabel.getY(), valueLabel.getPreferredSize().width, valueLabel.getPreferredSize().height);
				this.add(valueLabel);	
				cargoLabels[i] = valueLabel;
			}
			
			this.add(new JLabel(" "));
			portLabel = new JLabel("PORTS");
			this.add(portLabel);
			this.add(new JLabel(" "));
			
			for (int i = 0; i < ports.size(); i++){
				JLabel portLabel = new JLabel();
				this.add(portLabel);
				portLabels[i] = portLabel;
				JButton detailsButton = new JButton("Details");
				detailsButton.addActionListener(new PortDetails(ports.get(i)));
				detailsButton.setFocusable(false);
				this.add(detailsButton);
			}
			
			this.add(new JLabel(" "));
			displayName = new JLabel();
			this.add(displayName);
			
			for (int i = 0; i < Main1.CARGO.size(); i ++){
				String type = Main1.getCargoName(i);
				JLabel priceLabel = new JLabel(type + " " + displayPort.getPrices()[i]);
				this.add(priceLabel);
				displayLabels[i] = priceLabel;
			}
			
			
		}
		
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			sailorLabel.setText("Sailors " + player.getSailors() + " / " + player.sailorsNeeded());
			soldierLabel.setText("Soldiers " + player.getSoldiers());
			hullLabel.setText("Hull Strength " + player.getHull() + " / " + player.getMaxHull());
			sailLabel.setText("Sails " + player.getSails());
			frontLabel.setText("Front Cannons " + player.getFrontCannons() + " / " + player.frontCannonCapacity() + "           FRONT");
			rightLabel.setText("Right Cannons " + player.getRightCannons() + " / " + player.sideCannonCapacity() + "           RIGHT");
			backLabel.setText("Back Cannons " + player.getBackCannons() + " / " + player.frontCannonCapacity() + "           BACK");
			leftLabel.setText("Left Cannons " + player.getLeftCannons() + " / " + player.sideCannonCapacity() + "             LEFT");
			
			cargoLabel.setText("CARGO    " + player.getCargo().size() + " / " + player.getCargoCapacity());
			for (int i = 0; i < cargoLabels.length; i++){
				int cargo = 0;
				for (String playerCargo: player.getCargo()){
					if (Main1.getCargoName(i).equals(playerCargo)){
						cargo ++;
					}
				}
				cargoLabels[i].setText("            $" + Main1.CARGO.get(Main1.getCargoName(i)) + "  -  " + cargo);
			}
			for (int i = 0; i < ports.size(); i++){
				String direction = "";
				double deltax = ports.get(i).getLocation().x + PORT_IMAGE.getWidth()/2 - player.getCenter().x;
				double deltay = player.getCenter().y - (ports.get(i).getLocation().y + PORT_IMAGE.getHeight()/2);
				double angle;
				if (deltay == 0){
					angle = 270;
				}
				else{
					angle = Math.toDegrees(Math.atan(deltay/deltax));
					if (player.getCenter().x > ports.get(i).getLocation().x + PORT_IMAGE.getWidth()/2){
						angle += 180;
					}
					
					angle += 360;
					
					angle = angle%360;
					
					//System.out.println("angle is " + angle);
				}
				
				
				
				if (angle > 23 && angle < 68){
					direction = "NE";
				}
				else if (angle >= 68 && angle <= 113){
					direction = "N";
				}
				else if (angle > 113 && angle < 158){
					direction = "NW";
				}
				else if (angle >= 158 && angle < 203){
					direction = "W";
				}
				else if (angle > 203 && angle < 248){
					direction = "SW";
				}
				else if (angle >= 248 && angle <= 293){
					direction = "S";
				}
				else if (angle > 293 && angle < 338){
					direction = "SW";
				}
				else{
					direction = "E";
				}
				portLabels[i].setText(ports.get(i).getName() + "  -  " + ports.get(i).getCountry() + "  -  " + direction);
			}
			
			displayName.setText(displayPort.getName());
			for (int i = 0; i < displayLabels.length; i++){
				String type = Main1.getCargoName(i);
				displayLabels[i].setText(type + " " + displayPort.getPrices()[i]);
			}
			
			
			int displayWidth = 100;
			int displayHeight = 10;
			int gap = 200;
			g.setColor(Color.BLACK);
			g.drawRect(frontLabel.getX() + gap, frontLabel.getY(), displayWidth, displayHeight);
			g.drawRect(rightLabel.getX() + gap, rightLabel.getY(), displayWidth, displayHeight);
			g.drawRect(backLabel.getX() + gap, backLabel.getY(), displayWidth, displayHeight);
			g.drawRect(leftLabel.getX() + gap, leftLabel.getY(), displayWidth, displayHeight);

			
			g.setColor(Color.RED);
			g.fillRect(frontLabel.getX() + gap+1, frontLabel.getY() + 1, (int)(100 * player.frontReloadPercent()) - 1, displayHeight-1);
			g.fillRect(rightLabel.getX() + gap+1, rightLabel.getY() + 1, (int)(100 * player.rightReloadPercent()) -1 , displayHeight-1);
			g.fillRect(backLabel.getX() + gap+1, backLabel.getY() + 1, (int)(100 * player.backReloadPercent()) -1 , displayHeight-1);
			g.fillRect(leftLabel.getX() + gap+1, leftLabel.getY() + 1, (int)(100 * player.leftReloadPercent()) -1 , displayHeight-1);
			
			

		}
			
	}
	
	private class PortDetails implements ActionListener{

		public PortDetails(Port p){
			port = p;
		}
		private Port port;
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			displayPort = port;
			
		}
			
	}
	
	private class Input implements KeyListener{
		
		private ArrayList<Integer> held = new ArrayList<Integer>();

		@Override
		public void keyPressed(KeyEvent e) {
			int pressed = e.getKeyCode();
			if (!held.contains(pressed)){
				held.add(pressed);
			}
			System.out.println("key code " + e.getKeyCode());
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			int released = e.getKeyCode();
			held.remove(Integer.valueOf(released));
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		public boolean contains(int i){
			return held.contains(i);
		}
		
	}
	
	private boolean inBounds(Point p){
		
		if (p.x < 0 || p.x > seaWidth || p.y < 0 || p.y > seaHeight){
			return false;
		}
		int y = horizontalCoast[p.x/coastWidth];
		if (p.y < y || p.y > seaHeight-y){
			return false;
		}
		int x = verticalCoast[p.y/coastWidth];
		if (p.x < x || p.x > seaWidth - x){
			return false;
		}
		return true;
		
	}
	
	public boolean inBounds (Ship s){
		int degree = 15;
		double scale = 5.0/6.0;
		for (int i = 0; i < 90; i += degree){
			int x = (int)(Math.cos(Math.toRadians(i)) * s.getWidth()/2 * scale);
			int y = -(int)(Math.sin(Math.toRadians(i)) * s.getHeight()/2 * scale);
			Point a = new Point(s.getCenter().x + x, s.getCenter().y + y);
			Point b = new Point(s.getCenter().x + x, s.getCenter().y - y);
			Point c = new Point(s.getCenter().x - x, s.getCenter().y + y);
			Point d = new Point(s.getCenter().x -x, s.getCenter().y - y);
			if (!inBounds(a) || !inBounds(b) || !inBounds(c) || !inBounds(d)){
				return false;
			}
			
		}
		return true;
	}

	
	private void sleep(int m){
		try {
			Thread.sleep(m);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
