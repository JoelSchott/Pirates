package pirates;

import java.awt.*;

public class Port {
	
	public static enum Coast{
		NORTH,SOUTH,EAST,WEST;
	}
	
	public Port(Point l, String n, String c, int cannon, Port.Coast co){
		location = l;
		name = n;
		country = c;
		cannons = cannon;
		coast = co;
		if (coast.equals(Port.Coast.NORTH)){
			location.y -= Main1.PORT_IMAGE.getHeight() * 3.0/2.0;
		}
		else if (coast.equals(Port.Coast.SOUTH)){
			location.y += Main1.PORT_IMAGE.getHeight() * 3.0/2.0;
		}
		else if (coast.equals(Port.Coast.WEST)){
			location.x -= Main1.PORT_IMAGE.getWidth() * 3.0/2.0;
		}
		else{
			location.x += Main1.PORT_IMAGE.getWidth() * 3.0/2.0;
		}
		generatePrices();
	}
	
	private Point location;
	private String name;
	private String country;
	private int cannons;
	private Coast coast;
	
	private int[] prices = new int[Main1.CARGO.size()];
	
	public Point getLocation(){
		return location;
	}
	public String getName(){
		return name;
	}
	public String getCountry(){
		return country;
	}
	public int[] getPrices(){
		return prices;
	}
	public int getCannons(){
		return cannons;
	}
	public Coast getCoast(){
		return coast;
	}
	
	public void generatePrices(){
		for (int i = 0; i < Main1.CARGO.size(); i ++){
			int base = Main1.CARGO.get(Main1.getCargoName(i));
			int price = base + (int)((Math.random() - 0.5) * (double)base);
			if (price < 1){
				price = 1;
			}
			prices[i] = price;
		}
	}
	
	public void adjustPrices(){
		for (int i = 0; i < prices.length; i++){
			if (Math.random() > 0.5){
				prices[i] = prices[i] + 1;
			}
			else{
				prices[i] = prices[i] - 1;
			}
			if (prices[i] < 1){
				prices[i] = 1;
			}
		}
	}
	
	
	
}
