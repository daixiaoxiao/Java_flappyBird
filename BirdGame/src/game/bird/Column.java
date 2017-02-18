package game.bird;

import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;

/**type of column, x,y is the center position of column */
class Column{
	BufferedImage image;
	int x,y;
	int width,height;
	/**the gap of column*/
	int gap;
	int distance; /**the distance between columns*/
	
	Random random = new Random();
	/**constructor: initialize, n represent the number of columns*/
	public Column(int n) throws Exception{
		image = ImageIO.read(getClass().getResource("column.png"));
		width = image.getWidth();
		height = image.getHeight();
		gap = 144;
		distance = 245;
		x = 550 + (n-1)* distance;
		y = random.nextInt(218) + 132;
	}
	//column step
	public void step(){
		x--;
		if (x == -width / 2){
			x = distance *2 - width / 2;
			y = random.nextInt(218) + 132;
		}
	}
}