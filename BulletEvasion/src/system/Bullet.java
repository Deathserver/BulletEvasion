package system;

import java.awt.Rectangle;
import java.util.Random;

public class Bullet {
	
	public static int bulletSpeed;
	
	private int x;
	private int y;
	private int vx;
	private int vy;
	
	Random rand = new Random();
	
	public Bullet(){
		
		vx = 0;
		vy = 0;
		
		if(rand.nextBoolean()){
			vx = rand.nextInt(2) * 2 - 1;
		}
		else{
			vy = rand.nextInt(2) * 2 - 1;
		}
		
		x = rand.nextInt(Config.WINDOW_WIDTH/10) * 10;
		y = rand.nextInt(Config.WINDOW_HEIGHT/10) * 10;
		
		if(vx == 1){
			x = 20;
		}
		if(vx == -1){
			x = Config.WINDOW_WIDTH - 20;
		}
		
		if(vy == -1){
			y = Config.WINDOW_HEIGHT - 20;
		}
		if(vy == 1){
			y = 20;
		}
		
		
	}
	public Bullet(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public boolean checkOutOfBoundary(){
		return x < 0 || Config.WINDOW_WIDTH < x || y < 0 || Config.WINDOW_HEIGHT < y;
	}
	
	public void move(){
		x += vx * bulletSpeed;
		y += vy * bulletSpeed;
	}
	
	public void movePowerup(){
		x += vx * bulletSpeed / 2;
		y += vy * bulletSpeed / 2;
	}
	
	public Rectangle getBox(){
		return new Rectangle(x-Config.BULLET_SIZE/2, y-Config.BULLET_SIZE/3, Config.BULLET_SIZE, Config.BULLET_SIZE);
	}
	
	public int getVx(){
		return vx;
	}
	public int getVy(){
		return vy;
	}
	
	
	
}







