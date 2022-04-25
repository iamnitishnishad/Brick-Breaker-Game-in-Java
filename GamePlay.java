package demogame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePlay extends JPanel implements ActionListener, KeyListener {
	private boolean play= false;
	private int score =0;
	private int totalBrick=21;
	private Timer timer;
	private int delay=8;
	private int ballpoaX=120;
	private int ballpoaY=350;
	private int ballXdir=-1;
	private int ballYdir=-2;
	private int playerX=350;
	private MapGenerator map;
	
	
	public GamePlay() {
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(true);
		
		timer = new Timer(delay,this);
		timer.start();
		
		map = new MapGenerator(3, 7);
		
	}
	public void paint(Graphics g) {
		//black canvas
		g.setColor(Color.black);
		g.fillRect(1,  1, 692, 592);
		
		//border color
		g.setColor(Color.green);
		g.fillRect(0, 0, 692, 2);
		g.fillRect(0, 3, 3, 592);
		g.fillRect(691, 3, 3, 592);
		
		//paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 550, 100, 8);
		
		//Bricks
		map.draw((Graphics2D)g);
		
		//ball
		g.setColor(Color.red);
		g.fillOval(ballpoaX, ballpoaY, 20, 20);
		
		//score
		g.setColor(Color.yellow);
		g.setFont(new Font("serif",Font.BOLD,20));
		g.drawString("Score:"+score, 550, 30);
		
		//gameover
		if(ballpoaY>=570) {
			play=false;
			ballXdir=0;
			ballYdir=0;
			
			g.setColor(Color.yellow);
			g.setFont(new Font("serif",Font.BOLD,30));
			g.drawString("Game Over! Score:"+score, 200, 300);
			
			g.setFont(new Font("serif",Font.BOLD,25));
			g.drawString("Press Enter to Restart!" , 230, 350);
			
			
		}
		
		//Win Game
		if(totalBrick<=0) {
			play=false;
			ballXdir=0;
			ballYdir=0;
			
			g.setColor(Color.yellow);
			g.setFont(new Font("serif",Font.BOLD,30));
			g.drawString("You Won! Score:"+score, 200, 300);
			
			g.setFont(new Font("serif",Font.BOLD,25));
			g.drawString("Press Enter to Restart!" , 230, 350);
			
			
		}
	}
	
	private void moveLeft() {
		play = true;
		playerX-=20;
	}
	
	private void moveRight() {
		play = true;
		playerX+=20;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(playerX <= 0)
				playerX = 0;
			else
				moveLeft();
		}
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(playerX >= 600)
				playerX = 600;
			else
				moveRight();
		}
		if(e.getKeyCode()==KeyEvent.VK_ENTER) {
			if(!play) {
				score =0;
				totalBrick=21;
				ballpoaX=120;
				ballpoaY=350;
				ballXdir=-1;
				ballYdir=-2;
				playerX=320;
				
				map=new MapGenerator(3,7);
				
			}
		}
		
		repaint();
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(play) {
			
			if(ballpoaX<=0) {
				ballXdir=-ballXdir; //left boundry
			}
			if(ballpoaX>=670) {
				ballXdir=-ballXdir; //right boundry
			}
			if(ballpoaY<=0) {
				ballYdir=-ballYdir; //upper boundry
			}
			
			Rectangle ballRect = new Rectangle(ballpoaX, ballpoaY, 20, 20);
			Rectangle paddleRect = new Rectangle(playerX, 550, 100, 8);
			
			if(ballRect.intersects(paddleRect)) {
				ballYdir=-ballYdir;
			}
			
			A:for(int i=0; i<map.map.length; i++) {
				for(int j=0; j<map.map[i].length;j++) {
					
					if(map.map[i][j]>0) {
						
						int width=map.brickWidth;
						int height=map.brickHeight;
						int brickXpos=80+j*width;
						int brickYpos=50+i*height;
						
						Rectangle brickRect= new Rectangle(brickXpos,brickYpos,width,height);
						
						if(ballRect.intersects(brickRect)) {
							
							map.setBrick(0, i, j);
							totalBrick--;
							score+=5;
							
							if(ballpoaX+19<=brickXpos || ballpoaX+1>=brickXpos+width) {
								ballXdir=-ballXdir;
							}
							else {
								ballYdir=-ballYdir;
							}
							
							break A;
							
			
						
						}
						
					}
				}
			}
			
			ballpoaX += ballXdir;
			ballpoaY += ballYdir;
			
		}
		
		repaint();
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyReleased(KeyEvent e) {}
	
	

}
