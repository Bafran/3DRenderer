package src.renderer.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import src.renderer.point.MyPoint;
import src.renderer.point.PointConverter;

public class Line {
  private Color colour;
  private MyPoint[] points;

  public Line() {
    this.points = new MyPoint[2];
    this.colour = null;

    this.points[0] = new MyPoint(0, 0, 0);
    this.points[1] = new MyPoint(0, 0, 0);
  }

  public Line(Color colour, MyPoint p1, MyPoint p2) {
    this.points = new MyPoint[2];
    this.colour = colour;

    this.points[0] = new MyPoint(p1.x, p1.y, p1.z);
    this.points[1] = new MyPoint(p2.x, p2.y, p2.z);
  }

  public void render(Graphics g) {
    Point p1 = PointConverter.convertPoint(this.points[0]);
    Point p2 = PointConverter.convertPoint(this.points[1]);
    
    g.setColor(this.colour);
    g.drawLine(p1.x, p1.y, p2.x, p2.y);
  }

  public void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees) {
    for(MyPoint p : this.points) {
      PointConverter.rotateAxisX(p, CW, xDegrees);
      PointConverter.rotateAxisY(p, CW, yDegrees);
      PointConverter.rotateAxisZ(p, CW, zDegrees);
    }
  }

  public Line scale(double scale) {
    this.points[1].x *= scale;
    this.points[1].y *= scale;
    this.points[1].z *= scale;

    return this;
  }

  // Assuming the origin (p1) is zero, return the vector in point form
  public MyPoint vectorPoint() {
    return new MyPoint(
      this.points[1].x,
      this.points[1].y,
      this.points[1].z
    );
  }

  public static Line crossProduct(int[] v0, int[] v1) {
    MyPoint output = new MyPoint(
      v0[1]*v1[2] - v0[2]*v1[1],
      v0[2]*v1[0] - v0[0]*v1[2],
      v0[0]*v1[1] - v0[1]*v1[0]
    );

    return new Line(Color.RED, new MyPoint(0, 0, 0), output);
  }
}