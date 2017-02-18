package game.bird;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

/**type of bird, x,y is the center position of bird */
class Bird{
	BufferedImage image;
	int x,y;
	int width,height;
	int size; /**use for collision detecting*/
	//add attribution to calculate the position of bird
	double g;  //gravity
	double t;  //interval 
	double v0;  // original speed
	double speed; //currend upcast speed
	double s;
	double alpha;  
	
	//define an array pictures
	BufferedImage[] images;
	int index;
	
	public Bird() throws Exception {
		image = ImageIO.read(getClass().getResource("0.png"));
		width = image.getWidth();
		height = image.getHeight();
		x = 132;
		y = 280;
		size = 40;
		
		g = 4;
		v0 = 20;
		t = 0.25;
		speed = v0;
		s = 0;
		alpha = 0;
		
		images = new BufferedImage[8];
		for (int i = 0; i < 8; i++){
			//i = 0 1 2 
			images[i] = ImageIO.read(getClass().getResource(i + ".png"));
		}
		index = 0;
	}
	
	//code of flying
	public void fly(){
		index++;
		image = images[(index/12) % 8];  // /12 is to slow down the frequency of switching pictures
	}
	
	//code of moving
	public void step(){
		double v0 = speed;
		s = v0 * t + g * t * t /2;
		y = y - (int)s;
		double v = v0 - g * t;
		speed = v;
		
		//invoke math API
		alpha = Math.atan(s / 8);
	}
	
	public void flappy(){
		//reset the speed of bird 
		speed = v0;
	}
	
	public boolean hit(Ground ground){
		if ((y + size / 2) > ground.y){
			//put the bird on the ground
			y = ground.y - size / 2;
			//has the dynamic falling result
			alpha = -3.14159265358979323 / 2;
			return true;
		}
		else {
	        return false;		
		}
	}
	
	//detect the collision with column
	public boolean hit(Column column){
		//detect whether it's in range or not
		if (x > (column.x - column.width / 2 - size / 2) && x < (column.x + column.width / 2 + size / 2)){
			//whether it's in the gap
			if (y > column.y - column.gap / 2 + size / 2 && y < column.y + column.gap / 2 - size / 2){
				return false;
			}
			return true;
		}
		return false;
	}
}
