package game.bird;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

/**ground */
class Ground{
	BufferedImage image;
	int x,y;
	int width;
	int height;
	
	public Ground() throws Exception{
		image = ImageIO.read(getClass().getResource("gd.png"));
		width = image.getWidth();
		height = image.getHeight();
		x = 0; 
		y = 580;
	}
	//move the ground
	public void step(){
		x--;
		if(x == -109){
			x = 0;
		}
	}
}