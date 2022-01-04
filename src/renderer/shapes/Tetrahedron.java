package src.renderer.shapes;

import java.awt.Color;
import java.awt.Graphics;

public class Tetrahedron {
  
  private MyPolygon[] polygons;
  private Color colour;

  public Tetrahedron(Color colour, MyPolygon... polygons) {
    this.colour = colour;
    this.polygons = polygons;
    this.setPolygonColour();
  }

  public Tetrahedron(MyPolygon... polygons) {
    this.colour = Color.YELLOW;
    this.polygons = polygons;
  }

  public void render(Graphics g) {
    for(MyPolygon poly : this.polygons) {
      poly.render(g);
    }    
  }

  public void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees) {
    for(MyPolygon p : this.polygons) {
      p.rotate(CW, xDegrees, yDegrees, zDegrees);
    }
    this.sortPolygons();
  }

  private void sortPolygons() {
    MyPolygon.sortPolygons(this.polygons);
  }

  private void setPolygonColour() {
    for(MyPolygon poly: this.polygons) {
      poly.setColour(this.colour);
    }
  }

}
