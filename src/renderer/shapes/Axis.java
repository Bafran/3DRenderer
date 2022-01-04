package src.renderer.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import src.renderer.point.MyPoint;
import src.renderer.point.PointConverter;

public class Axis {

  private Color colour;
  private MyPoint[] points;

  public Axis(Color colour, int axisLength) {
    this.colour = colour;
    this.points = new MyPoint[3];

    this.points[0] = new MyPoint(0, 0, axisLength);
    this.points[1] = new MyPoint(0, axisLength, 0);
    this.points[2] = new MyPoint(axisLength, 0, 0);
  }

  public void render(Graphics g) {
    MyPoint point3D = new MyPoint(0, 0, 0);
    Point point2D = PointConverter.convertPoint(point3D);
    g.setColor(this.colour);
    
    for(int i = 0; i < 3; i++) {
      Point p = PointConverter.convertPoint(this.points[i]);
      g.drawLine(point2D.x, point2D.y, p.x, p.y);
    }
  }

  public void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees) {
    for(MyPoint p : this.points) {
      PointConverter.rotateAxisX(p, CW, xDegrees);
      PointConverter.rotateAxisY(p, CW, yDegrees);
      PointConverter.rotateAxisZ(p, CW, zDegrees);
    }
  }
}
