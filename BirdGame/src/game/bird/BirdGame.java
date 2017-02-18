package game.bird;

import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BirdGame extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Bird bird;
	Column column1, column2;
	Ground ground;
	//ArrayList<Cloud> listOfClouds = new ArrayList<>();
	Cloud cloud1, cloud2, cloud3;
	BufferedImage background;
	
	//game state
	int state;
	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int GAME_OVER = 2;
	//score
	int score;
	String player_name;
	
	/**initialize BirdGame*/
	public BirdGame() throws Exception{
		
		state = START;
		
		bird = new Bird();
		column1 = new Column(1);
		column2 = new Column(2);
		cloud1 = new Cloud();
		cloud2 = new Cloud();
		cloud3 = new Cloud();
		ground = new Ground();
		background = ImageIO.read(getClass().getResource("bg.png"));
	}
	// action method
	public void action() throws Exception{
		addMouseListener ( new MouseAdapter(){
			//press the mouse
			public void mousePressed(MouseEvent e){
				
				try{
					switch (state){
					case GAME_OVER:
						
						bird = new Bird();
						column1 = new Column(1);
						column2 = new Column(2);
						cloud1 = new Cloud();
						cloud2 = new Cloud();
						cloud3 = new Cloud();
						score = 0;
						state = START;
						break;
					case START:
						state = RUNNING;
					case RUNNING:
						//flappy and cloud step
						bird.flappy();
						cloud1.step();
						cloud2.step();
						cloud3.step();
						
					}
				}
				catch(Exception ex){
					ex.printStackTrace();
				}
			}
		});
		while (true){
			switch (state){
			case START:
				bird.fly();
				ground.step();
				cloud1.step();
				cloud2.step();
				cloud3.step();
				break;
			case RUNNING:
				column1.step();
				column2.step();
				bird.step();
				bird.fly();
				ground.step();
				cloud1.step();
				cloud2.step();
				cloud3.step();
				
				//score logic
				if (bird.x == column1.x || bird.x == column2.x){
					score++;
				}
				
				if (bird.hit(ground) || bird.hit(column1) || bird.hit(column2)){
			    	state = GAME_OVER;
			    	doIt();
			    }
				break;
			}
			repaint();
			Thread.sleep(1000 / 30);
		  
		}
	}
	
    class OpenActionListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			display();
		}
	}
    
    public void display(){

         JTextField field1 = new JTextField();
         JPanel panel = new JPanel(new GridLayout(0, 1));
         panel.add(new JLabel("Player:"));
         panel.add(field1);
         int result = JOptionPane.showConfirmDialog(null, panel, "Test",
             JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
         if (result == JOptionPane.OK_OPTION) {
             System.out.println(" " + field1.getText());
             player_name = field1.getText();
             enterPlayer(field1.getText());
         } else {
             System.out.println("Cancelled");
         }
    }
    
	/**the way to start software */
	public static void main(String[] args) throws Exception{
		JFrame frame = new JFrame("small bird fly fly");
		BirdGame game = new BirdGame();
		JMenuBar menubar = new JMenuBar();
		frame.setJMenuBar(menubar);
		JMenu playerMenu = new JMenu("Player");
		menubar.add(playerMenu);
		JMenuItem openItem = new JMenuItem("Enter Player");
		openItem.addActionListener(game.new OpenActionListener());
		playerMenu.add(openItem);
		frame.add(game);
		frame.setSize(440, 730);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		game.action();
		
	}
	
	public void enterPlayer(String player_name){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		System.out.println("connect to database...");
		String url = "jdbc:mysql://studev.groept.be:3306/a15_sd60";
		String sql = "SELECT name FROM player where name = '"+player_name+"'";
		String sql_0 = "INSERT INTO player " + "VALUES ('"+player_name+"', NULL)";
		
		try (Connection con = DriverManager.getConnection(url, "a15_SD60", "a15_SD60");
				Statement statement = con.createStatement();){
			ResultSet resultset = statement.executeQuery(sql);
			if(resultset.absolute(1)){
				System.out.println("The player name already in the database...");
			}
			else{
				System.out.println("Inserted records into the table...");
				statement.executeUpdate(sql_0);
			}
	    }
		catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("finished");
	}
	public void doIt() {
		
		// load driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		System.out.println("class loaded...");

		String url = "jdbc:mysql://studev.groept.be:3306/a15_sd60";
		String sql_0 = "SELECT * FROM player where name = '"+player_name+"'";
		String sql = "INSERT INTO player " + "VALUES ('"+player_name+"', '"+score+"')";
		String sql_1 = "UPDATE player set score = ? where name = '"+player_name+"'";
		
		
		try (Connection con = DriverManager.getConnection(url, "a15_SD60", "a15_SD60");
			Statement statement = con.createStatement();){
		    ResultSet rs = statement.executeQuery(sql_0);
		    if(rs.absolute(1)){
				System.out.println("The player name already in the database, please update the score if nessecary...");
				
				int old_score = rs.getInt(2);
				System.out.println("**********************************");
				if(old_score < score){
					PreparedStatement preparedStmt = con.prepareStatement(sql_1);
		    	    preparedStmt.setInt(1, score);
		    	    //preparedStmt.setString(2, "Fred");
		    	    preparedStmt.executeUpdate();
		    	    System.out.println("updated the highest score");
				}
				else{
					System.out.println("The score isn't highest, no need to update!");
				}
								
			}
			else{
				System.out.println("Inserted records into the table...");
				statement.executeUpdate(sql);
			}
		}
			
			// ps.setInt(1, userId);
		 catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("finished");

	}
	/**revise the paint method to draw*/
	public void paint(Graphics g){
		g.drawImage(background, 0, 0, null);
		g.drawImage(column1.image, column1.x - column1.width / 2,column1.y - column1.height / 2, null);
		g.drawImage(column2.image, column2.x - column2.width / 2,column2.y - column2.height / 2, null);
		g.drawImage(ground.image, ground.x, ground.y, null);

		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(-bird.alpha, bird.x, bird.y);

		g.drawImage(bird.image, bird.x - bird.width / 2,bird.y - bird.height / 2, null);
		
		g2.rotate(bird.alpha, bird.x, bird.y);
		
		g.drawImage(cloud1.image, cloud1.x - cloud1.width / 2,cloud1.y - cloud1.height / 2, cloud1.width, cloud1.height, null);
        g.drawImage(cloud2.image, cloud2.x - cloud2.width / 2,cloud2.y - cloud2.height / 2, cloud2.width, cloud2.height, null);
		g.drawImage(cloud3.image, cloud3.x - cloud3.width / 2,cloud3.y - cloud3.height / 2, cloud3.width, cloud3.height, null);
		
		//draw the score
		Font f = new Font(Font.SANS_SERIF, Font.BOLD, 40);
		g.setFont(f);
		g.drawString(""+ score, 40, 60);
		g.setColor(Color.WHITE);
		g.drawString(""+ score, 40 - 3, 60 - 3);
		

		switch(state){
		case GAME_OVER:
			
			Font over = new Font(Font.SANS_SERIF, Font.BOLD, 50);
			g.setFont(over);
			g.drawString("GAME OVER!", 55, 330);
			g.setColor(Color.WHITE);
			
			Font Score = new Font(Font.MONOSPACED, Font.ITALIC, 30);
			g.setFont(Score);
			g.drawString("YOUR SCORE: "+ score, 90, 400);
			

			break;
		case START:
			
			Font ready = new Font(Font.SANS_SERIF, Font.BOLD, 50);
			g.setFont(ready);
			g.drawString("GET READY!", 55, 330);
			break;
		}
	}
}



