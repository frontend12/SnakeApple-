import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameField extends JPanel implements ActionListener{
    private final int SIZE = 320;
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = 400;
    private Image dot;
    private Image apple;
    private int appleX;
    private int appleY;
    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];
    private int dots;
    private Timer timer;
    private Direction direction = Direction.RIGHT;
    private boolean inGame = true;

    private final Snake snake=new Snake(List.of());


    public GameField(){
        setBackground(Color.black);
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);

    }

    public void initGame(){
        dots = 3;
        List<Coordinate> initialCoordinates=new ArrayList<>();
        for (int i = 0; i < dots; i++) {
            x[i] = 48 - i*DOT_SIZE;
            y[i] = 48;
            Coordinate coordinate = new Coordinate(x[i], y[i]);
            initialCoordinates.add(coordinate);
        }
        snake.setCoordinates(initialCoordinates);

        timer = new Timer(250,this);
        timer.start();
        createApple();
    }

    public void createApple(){

        int valueOfX = new Random().nextInt(20)*DOT_SIZE;
        int valueOfY = new Random().nextInt(20)*DOT_SIZE;
        Coordinate result=new Coordinate(valueOfX,valueOfY);

        while (snake.getCoordinates().contains(result)){
            result.setX(new Random().nextInt(20)*DOT_SIZE);
            result.setY(new Random().nextInt(20)*DOT_SIZE);
        }

        System.out.println(appleX+" "+appleY);
        appleX=valueOfX;
        appleY=valueOfY;

    }

    public void loadImages(){
        ImageIcon iia = new ImageIcon("apple.png");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon("dot.png");
        dot = iid.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(inGame){
            g.drawImage(apple,appleX,appleY,this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot,x[i],y[i],this);
            }
        } else{
            String str = "Game Over";
            //Font f = new Font("Arial",14,Font.BOLD);
            g.setColor(Color.white);
            // g.setFont(f);
            g.drawString(str,125,SIZE/2);
        }
    }

    public void move(){
        List<Coordinate> coordinates=new ArrayList<>();
        for (int i = dots; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
            coordinates.add(new Coordinate(x[i],y[i]));
        }
        switch (direction) {
            case LEFT:
                x[0] -= DOT_SIZE;
                break;
            case RIGHT:
                x[0] += DOT_SIZE;
                break;
            case UP:
                y[0] -= DOT_SIZE;
                break;
            case DOWN:
                y[0] += DOT_SIZE;
                break;
        }

        coordinates.add(new Coordinate(x[0],y[0]));

        snake.setCoordinates(coordinates);

    }



    public void checkApple(){
        if(x[0] == appleX && y[0] == appleY){
            dots++;
            createApple();
        }
    }

    public void checkCollisions(){
        for (int i = dots; i >0 ; i--) {
            if(i>4 && x[0] == x[i] && y[0] == y[i]){
                inGame = false;
            }
        }

        if(x[0]>SIZE){
            inGame = false;
        }
        if(x[0]<0){
            inGame = false;
        }
        if(y[0]>SIZE){
            inGame = false;
        }
        if(y[0]<0){
            inGame = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(inGame){
            checkApple();
            checkCollisions();
            move();

        }
        repaint();
    }

    class FieldKeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_LEFT:
                    if (direction.canChangeDirectionTo(Direction.LEFT)) {
                        direction = Direction.LEFT;
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction.canChangeDirectionTo(Direction.UP)) {
                        direction = Direction.UP;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction.canChangeDirectionTo(Direction.RIGHT)) {
                        direction = Direction.RIGHT;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction.canChangeDirectionTo(Direction.DOWN)) {
                        direction = Direction.DOWN;
                    }
                    break;
            }
        }
    }


}
