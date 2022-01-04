package src.renderer.shapes;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.awt.Color;
import src.renderer.point.MyPoint;
import src.renderer.point.PointConverter;

public class MyPolygon {
  
  private Color colour;
  private MyPoint[] points;

  public MyPolygon(Color colour, MyPoint... points) {
    this.colour = colour;
    this.points = new MyPoint[points.length];

    for(int i = 0; i < points.length; i++) {
      MyPoint p = points[i];
      this.points[i] = new MyPoint(p.x, p.y, p.z);
    }
  }

  public MyPolygon(MyPoint... points) {
    this.colour = Color.WHITE;
    this.points = new MyPoint[points.length];

    for(int i = 0; i < points.length; i++) {
      MyPoint p = points[i];
      this.points[i] = new MyPoint(p.x, p.y, p.z);
    }
  }

  public void render(Graphics g) {
    Polygon poly = new Polygon();
    // add every point to a polygon
    for(int i = 0; i < this.points.length; i++) {
      Point p = PointConverter.convertPoint(this.points[i]);
      poly.addPoint(p.x, p.y);
    }

    g.setColor(this.colour);
    g.fillPolygon(poly);
  }

  public void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees) {
    for(MyPoint p : points) {
      PointConverter.rotateAxisX(p, CW, xDegrees);
      PointConverter.rotateAxisY(p, CW, yDegrees);
      PointConverter.rotateAxisZ(p, CW, zDegrees);
    }
  }

  public double getAverageX() {
    double sum = 0;
    for(MyPoint p: this.points) {
      sum += p.x;
    }

    return sum / this.points.length;
  }

  public static MyPolygon[] sortPolygons(MyPolygon[] polygons) {
    List<MyPolygon> polygonsList = new ArrayList<MyPolygon>();
    
    for(MyPolygon poly : polygons) {
      polygonsList.add(poly);
    }

    Collections.sort(polygonsList, new Comparator<MyPolygon>() {
      @Override
      public int compare(MyPolygon p1, MyPolygon p2) {
       return p2.getAverageX() - p1.getAverageX() < 0 ? 1 : -1; 
      }
    });

    for(int i = 0; i < polygons.length; i++) {
      polygons[i] = polygonsList.get(i);
    }

    return polygons;
  }

  public void setColour(Color colour) {
    this.colour = colour;
  }

  public static MyPolygon vectorPlane(Line l0, Line l1) {
    Line inversel0 = l0.scale(-1);
    Line inversel1 = l1.scale(-1);

    return new MyPolygon(
      Color.WHITE,
      l0.vectorPoint(),
      l1.vectorPoint(),
      inversel0.vectorPoint(),
      inversel1.vectorPoint()
    );
  }

}
