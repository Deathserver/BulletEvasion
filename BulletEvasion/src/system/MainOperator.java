package system;



import java.awt.Rectangle;

import javax.swing.JFrame;

import java.awt.event.*;
import java.awt.geom.Line2D;

import javax.swing.Timer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("serial")
public class MainOperator extends JFrame implements ActionListener, KeyListener{
    
	
	boolean active;
	
	ArrayList <Integer> scores = new ArrayList();
	
    int x;
    int y;
	
    int vx;
    int vy;
    
    int score;
    
    Timer timer;
    
    int bulletCounter;
    
    Random rand = new Random();
    
    ArrayList <Bullet> bullets = new ArrayList();
    ArrayList <Bullet> powerups = new ArrayList();
    
    String ability = null;
    
    int abilityTick;
    
    
    public MainOperator() {
    	
    	reset();
    	
    	scores.add(0);
    	scores.add(0);
    	scores.add(0);
    	scores.add(0);
    	scores.add(0);
    	
    	active = true;
    	
        timer = new Timer(25,this);
        
        
        timer.start();
        
        addKeyListener(this);
    }
    
    public static void main (String[] args){
        
        
        JFrame window = new MainOperator();
        
        window.setSize(Config.WINDOW_WIDTH,Config.WINDOW_HEIGHT);
    	window.setResizable(false);
    	window.setTitle("Bullet Evasion V: " + Config.VERSION);
    	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	window.setLocationRelativeTo(null);
        
    	window.setVisible(true);
        
        
    }
    
    
    public void paint(Graphics g){
        
        Image img = createImage(getWidth(), getHeight());
        
        Graphics2D g2 = (Graphics2D) img.getGraphics();
        
        
        g2.setColor(Color.WHITE);
        g2.fill(new Rectangle(0,0,Config.WINDOW_WIDTH,Config.WINDOW_HEIGHT));
        
        
        if(ability.equalsIgnoreCase("Scanner")){
	        g2.setColor(Color.RED);
	        for(Bullet bullet : bullets){
		        if(!bullet.checkOutOfBoundary()){
		        	if(bullet.getVx() != 0){
		        		g2.draw(new Line2D.Double(0,bullet.getBox().getCenterY(), Config.WINDOW_WIDTH, bullet.getBox().getCenterY()));
		        	}
		        	else{
		        		g2.draw(new Line2D.Double(bullet.getBox().getCenterX(),0, bullet.getBox().getCenterX(), Config.WINDOW_HEIGHT));
		        	}
	        	}
	        }
        }
        
        g2.setColor(Color.CYAN);
        for(Bullet powerup : powerups){
	        if(!powerup.checkOutOfBoundary()){
	        	if(powerup.getVx() != 0){
	        		g2.draw(new Line2D.Double(0,powerup.getBox().getCenterY(), Config.WINDOW_WIDTH, powerup.getBox().getCenterY()));
	        	}
	        	else{
	        		g2.draw(new Line2D.Double(powerup.getBox().getCenterX(),0, powerup.getBox().getCenterX(), Config.WINDOW_HEIGHT));
	        	}
        	}
        }
        
        
        g2.setColor(Color.RED);
        for(Bullet bullet : bullets){
        	g2.fill(bullet.getBox());
        }
        
        g2.setColor(Color.CYAN);
        for(Bullet powerup : powerups){
        	g2.fill(powerup.getBox());
        }
        
        g2.setColor(Color.GREEN);
        g2.fill(new Rectangle(x-Config.PLAYER_SIZE/2,y-Config.PLAYER_SIZE/2,Config.PLAYER_SIZE,Config.PLAYER_SIZE));
        
        g2.setColor(Color.BLUE);
        g2.drawString( "Score: " + score, 50,50);
        
        if(!active){
        	g2.setColor(Color.BLUE);
            g2.drawString("Rank 1: " + scores.get(0), 200, 100);
            g2.drawString("Rank 2: " + scores.get(1), 200, 120);
            g2.drawString("Rank 3: " + scores.get(2), 200, 140);
            g2.drawString("Rank 4: " + scores.get(3), 200, 160);
            g2.drawString("Rank 5: " + scores.get(4), 200, 180);
            
            int n = 0;
            
            while(scores.get(n) != score){
            	n++;
            }
            
            
            g2.drawString("Your rank: " + (n+1), 200, 220);
        }
        
        
        g.drawImage(img, 0, 0, this);
        
        
    }
    
    public void actionPerformed(ActionEvent e){
	    
   		
    	if(active){
	    	
    		if(!ability.equals("")){
	    		if(abilityTick-- == 0){
	    			abilityTick = Config.INITIAL_ABILITY_TICK;
	    			ability = "";
	    		}
    		}
    		
    		if(ability.equalsIgnoreCase("TimeFreezer") &&
    				Bullet.bulletSpeed > Config.FREEZER_BULLET_SPEED){
    			Bullet.bulletSpeed--;
    		}
    		else if(Bullet.bulletSpeed < Config.INITIAL_BULLET_SPEED){
    			Bullet.bulletSpeed++;
    		}
    		
	    	move();
	    	
	    	score++;
	    	
	    	for(Bullet bullet : bullets){
	    		bullet.move();
	    	}
	    	
	    	for(Bullet powerup : powerups){
	    		powerup.movePowerup();
	    	}
	    	
	    	if( ( 15 <= bulletCounter && ability.equalsIgnoreCase("TimeFreezer") ) ||
	    			( 3 <= bulletCounter && !ability.equalsIgnoreCase("TimeFreezer")  )){
	    		addBullet();
	    	}
	    	
	    	bulletCounter++;
	    	
	    	if(checkCollision(bullets)){
	    		active = false;
	    	}
	    	
	    	if(checkCollision(powerups)){
	    		if(ability.equalsIgnoreCase("")){
	    			newAbility();
	    		}
	    	}
	    	
	    	if(0>x || Config.WINDOW_WIDTH<x || 0>y || Config.WINDOW_HEIGHT<y){
	    		active = false;
	    	}
	    	
	    	
	    	if(!active){
	    		int n = 0;
	    		
	    		while(score <= scores.get(n)){
	    			n++;
	    		}
	    		scores.add(n, score);
	    	}
	    	
	    	
	    	repaint();
	    	
	    	
    	}
    	
    }
    
    public void keyReleased(KeyEvent e){
    	
    	if(e.getKeyCode() == KeyEvent.VK_UP){
			vy = 0;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN){
			vy = 0;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			vx = 0;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			vx = 0;
		}
    	
    }
    
	public void keyPressed(KeyEvent e){
	    
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			reset();
		}
		
		
		if(e.getKeyCode() == KeyEvent.VK_UP){
			vy = -1;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN){
			vy = 1;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			vx = -1;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			vx = 1;
		}
		
    }
    
    
    
    public void keyTyped(KeyEvent e){}
    
    public void move(){
    	
    	x += vx * 10;
    	y += vy * 10;
    	
    	
    }
    
    public boolean checkCollision(ArrayList <Bullet> bullets){
    	
    	for(Bullet bullet : bullets){
    		if(bullet.getBox().intersects(new Rectangle(x-Config.PLAYER_SIZE/2,y-Config.PLAYER_SIZE/2,Config.PLAYER_SIZE,Config.PLAYER_SIZE))){
    			return true;
    		}
    	}
    	return false;
    }
    
    public void newAbility(){
    	//int type = rand.nextInt(3) + 1;
    	int type = rand.nextInt(2) + 1;
    	
    	if(type == 1){
    		ability = "Scanner";
    	}
    	if(type == 2){
    		ability = "TimeFreezer";
    	}
    	if(type == 3){
    		ability = "Shield";
    	}
    	
    }
    
    public void addBullet(){
	   	bulletCounter = 0;
		if(rand.nextInt(50) != 0){
			bullets.add(new Bullet());
		}
		else{
			powerups.add(new Bullet());
		}
    }
    
    public void reset(){
    	
        bullets = new ArrayList();
        powerups = new ArrayList();
    	bulletCounter = 0;
    	x = Config.WINDOW_WIDTH/2;
        y = Config.WINDOW_HEIGHT/2;
    	
        vx = 0;
        vy = 0;
    	
        ability = "";
        abilityTick = Config.INITIAL_ABILITY_TICK;
        
        Bullet.bulletSpeed = Config.INITIAL_BULLET_SPEED;
        
        score = 0;
        
    	active = true;
    	
    }
    
    
    
    
}




