import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener{
    //constants
    static final int WIDTH = 500;
    static final int HEIGHT = 500;
    static final int TILE_SIZE = 50; //the size of each tile
    static final int GAME_TILES = (WIDTH * HEIGHT)/(TILE_SIZE);
    static final int DELAY = 87;

    //hold the coordinates of the body parts of the snake
    final int x[] = new int[GAME_TILES];
    final int y[] = new int[GAME_TILES];

    //variables
    int bodyParts = 3; // keeps track of the length of snake
    int applesEaten;
    int appleX, appleY; //x, y coordinate of apple
    char direction = 'R';
    boolean run = false;
    Timer timer;
    Random random;
    //initialize the game panel
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.green);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter(this));
        startGame(); // initializes the game
    }

    public void startGame() {
        spawnApple();
        run = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(run) {
            body_move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public static void main(String[] args) {
        GameFrame frame = new GameFrame(); //instance of game frame
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void spawnApple(){
        int gridCols = WIDTH / TILE_SIZE;
        int gridRows = HEIGHT / TILE_SIZE;

        appleX = random.nextInt(gridCols) * TILE_SIZE;
        appleY = random.nextInt(gridRows) * TILE_SIZE;
    }

    //this method responsible for moving the snake
    public void body_move(){
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch (direction) {
            case 'U' -> y[0] = y[0] - TILE_SIZE;
            case 'D' -> y[0] = y[0] + TILE_SIZE;
            case 'L' -> x[0] = x[0] - TILE_SIZE;
            case 'R' -> x[0] = x[0] + TILE_SIZE;
        }

    }
    public void checkApple() {
        if((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            spawnApple();
        }
    }
    public void checkCollisions() {
        //checks if head collides with body
        for(int i = bodyParts; i > 0; i--) {

            if ((x[0] == x[i]) && (y[0] == y[i])) {
                run = false;
                break;
            }
        }
        //check if head touches left border
        if(x[0] < 0) {
            run = false;
        }
        //check if head touches right border
        if(x[0] > WIDTH) {
            run = false;
        }
        //check if head touches top border
        if(y[0] < 0) {
            run = false;
        }
        //check if head touches bottom border
        if(y[0] > HEIGHT) {
            run = false;
        }

        if(!run) {
            timer.stop();
        }
    }

    public void draw(Graphics g) {

        if (run) {
            g.setColor(Color.red); //object color
            g.fillOval(appleX, appleY, TILE_SIZE, TILE_SIZE); //width and height

            //draw the body of the snake
            for(int i = 0; i < bodyParts; i++) {
                if(i == 0) {
                    g.setColor(Color.blue);
                    g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE); //width and height
                }
                else {
                    g.setColor(new Color(45, 120, 180));
                    g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
                }
            }
            // draw score on the screen
            g.setFont( new Font("Helvetica",Font.BOLD, 30));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+applesEaten, (WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }

    }
    public void gameOver(Graphics g) {
        //Score
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten, (WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
        //Game Over text
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (WIDTH - metrics2.stringWidth("Game Over"))/2, HEIGHT /2);
    }

}
