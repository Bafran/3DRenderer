package src.renderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JTextField;

import src.renderer.input.ClickType;
import src.renderer.input.Mouse;
import src.renderer.point.MyPoint;
import src.renderer.point.PointConverter;
import src.renderer.shapes.Axis;
import src.renderer.shapes.Line;
import src.renderer.shapes.MyPolygon;
import src.renderer.shapes.Tetrahedron;

public class Display extends Canvas implements Runnable{

  private Thread thread;
  private JFrame frame;
  private static String title = "3D Renderer";
  public static final int WIDTH = 800;
  public static final int HEIGHT = 600;
  private static boolean running = false;

  private Tetrahedron[] tetras;
  private MyPolygon[] polygons;
  private Line[] lines;
  private Axis axis;

  private static JTextField input1;
  private static JTextField input2;
  private int[] vector1;
  private int[] vector2;
  
  private Mouse mouse;
  
  public Display() {
    this.frame = new JFrame();
    input1 = new JTextField();
    input1.setBounds(0, 0, 100, 25);
    input1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String input = input1.getText();
        String[] split = input.split(" ");

        vector1 = new int[3];
        for(int i = 0; i < 3; i++) {
          vector1[i] = Integer.parseInt(split[i]);
        }

        MyPoint pointOrigin = new MyPoint(0, 0, 0);
        lines[0] = new Line(
          Color.ORANGE,
          pointOrigin,
          new MyPoint(
            vector1[0],
            vector1[1],
            vector1[2]
          )
        );

        if(!(vector1 == null || vector2 == null)) {
          lines[2] = Line.crossProduct(vector1, vector2);
          polygons[0] = MyPolygon.vectorPlane(lines[0], lines[1]);
        }
      }
    });

    input2 = new JTextField();
    input2.setBounds(0, 25, 100, 25);
    input2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String input = input2.getText();
        String[] split = input.split(" ");

        vector2 = new int[3];
        for(int i = 0; i < 3; i++) {
          vector2[i] = Integer.parseInt(split[i]);
        }

        MyPoint pointOrigin = new MyPoint(0, 0, 0);
        lines[1] = new Line(
          Color.ORANGE,
          pointOrigin,
          new MyPoint(
            vector2[0],
            vector2[1],
            vector2[2]
          )
        );

        if(!(vector1 == null || vector2 == null)) {
          lines[2] = Line.crossProduct(vector1, vector2);
          polygons[0] = MyPolygon.vectorPlane(lines[0], lines[1]);
        }
      }
    });
    
    Dimension size = new Dimension(WIDTH, HEIGHT);
    this.setPreferredSize(size);

    // Mouse and Listeners
    this.mouse = new Mouse();
    this.addMouseListener(this.mouse);
    this.addMouseMotionListener(this.mouse);
    this.addMouseWheelListener(this.mouse);

    // Keyboard Listeners
  }

  public synchronized void start() {
    running = true;
    this.thread = new Thread(this, "Display");
    this.thread.start();
  }

  public static void main(String[] args) {
    Display display = new Display();
    display.frame.setTitle(title);
    display.frame.add(input1);
    display.frame.add(input2);
    display.frame.add(display);
    display.frame.pack();
    display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    display.frame.setLocationRelativeTo(null);
    display.frame.setResizable(false);
    display.frame.setVisible(true);

    display.start();
  }

  public synchronized void stop() {
    running = false;

    try {
      this.thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    long prevTick = System.nanoTime();
    long timer = System.currentTimeMillis();
    // Number of nanoseconds between frames
    final double ns = 1000000000.00 / 60;
    double delta = 0;
    int frames = 0;

    init();

    while(running) {
      long thisTick = System.nanoTime();
      delta += (thisTick - prevTick) / ns;
      prevTick = thisTick;

      // Explanation for why this is a while loop:
      // If your computer skips an update, and delta approaches 2 (should be updating when delta >= 1)
      // Then, we can compensate by running two updates to catch up. If this was an if statement, this
      // wouldn't happen
      while(delta >= 1) {
        update();
        delta--;
        render();
        frames++;
      }

      if(System.currentTimeMillis() - timer > 1000) {
        timer += 1000;
        this.frame.setTitle(title + " | " + frames + " fps");
        frames = 0;
      }
    }

    stop();
  }

  private void init() {
    this.tetras = new Tetrahedron[1];
    this.polygons = new MyPolygon[1];
    this.lines = new Line[4];
    this.axis = new Axis(Color.CYAN, 250);
    
    int s =  100;
    
    MyPoint p1 = new MyPoint(s/2, -s/2, -s/2);
    MyPoint p2 = new MyPoint(s/2, s/2, -s/2);
    MyPoint p3 = new MyPoint(s/2, s/2, s/2);
    MyPoint p4 = new MyPoint(s/2, -s/2, s/2);
    MyPoint p5 = new MyPoint(-s/2, -s/2, -s/2);
    MyPoint p6 = new MyPoint(-s/2, s/2, -s/2);
    MyPoint p7 = new MyPoint(-s/2, s/2, s/2);
    MyPoint p8 = new MyPoint(-s/2, -s/2, s/2);

    Tetrahedron tetra = new Tetrahedron(
      new MyPolygon(Color.BLUE, p1, p2, p3, p4),
      new MyPolygon(Color.RED, p5, p6, p7, p8),
      new MyPolygon(Color.GREEN, p1, p2, p6, p5),
      new MyPolygon(Color.CYAN, p1, p5, p8, p4),
      new MyPolygon(Color.WHITE, p2, p6, p7, p3),
      new MyPolygon(Color.YELLOW, p4, p3, p7, p8)
    );

    this.tetras[0] = tetra;

    for(int i = 0; i < lines.length; i++) {
      lines[i] = new Line();
    }

    for(int i = 0; i < polygons.length; i++) {
      polygons[i] = new MyPolygon();
    }
  }

  private void render() {
    BufferStrategy bs = this.getBufferStrategy();
    if(bs == null) {
      this.createBufferStrategy(3);
      return;
    }

    Graphics g = bs.getDrawGraphics();
    
    // Background
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, WIDTH, HEIGHT);

    // Axis
    axis.render(g);

    // Renders
    for(Tetrahedron t : this.tetras) {
      t.render(g);
    }

    for(MyPolygon p : polygons) {
      p.render(g);
    }

    for(Line l: lines) {
      l.render(g);
    }

    // Overlays

    g.dispose();
    bs.show();
  }

  int initialX, initialY;
  private void update() {
    // Mouse movements and rotations
    int x = this.mouse.getX();
    int y = this.mouse.getY();
    if(this.mouse.getMouseB() == ClickType.LeftClick) {
      int xDif = x - initialX;
      int yDif = y - initialY;
      rotate(0, xDif, yDif);
    } else if(this.mouse.getMouseB() == ClickType.RightClick) {
      int xDif = x - initialX;
      rotate(0, xDif, 0);
    }

    if(this.mouse.isScrollingUp()) {
      PointConverter.zoomIn();
    } else if (this.mouse.isScrollingDown()) {
      PointConverter.zoomOut();
    }

    this.mouse.resetScroll();

    initialX = x;
    initialY = y;
  }

  private void rotate(int x, int y, int z) {
    for(Tetrahedron t : this.tetras) {
      t.rotate(true, x, z, -y);
    }
  
    for(MyPolygon p : polygons) {
      p.rotate(true, x, z, -y);
    }
  
    for(Line l: lines) {
      l.rotate(true, x, z, -y);
    }
  
    axis.rotate(true, x, -z, y);
  }
}
