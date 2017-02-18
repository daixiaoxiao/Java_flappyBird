package game.bird;


import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;

class Cloud {
	BufferedImage image;
	Image scaled_Image;
	int x,y;
	int width,height;
	
	Random random = new Random();
	public Cloud() throws Exception{
		image = ImageIO.read(getClass().getResource("cloud.png"));
		width = image.getWidth();
		height = image.getHeight();

		x = setX();
		y = setY(); 
		width = setWidth();
		
	}

	private int setX(){
		x = random.nextInt(440 - image.getWidth()) + image.getWidth() / 2 ;
		return x;
	}
	
	private int setY(){
		y = random.nextInt(500 - image.getHeight()) + image.getHeight() / 2;
		return y;
	}
	
	private int setWidth(){
	    width = (int) ((Math.random() + 0.5) * width);
	    return width;
	}
	
	//column step
	public void step(){
		x--;
		if (x == -width / 2){
			x = 450 - width / 2;
			y = setY();
		}
	}
}
