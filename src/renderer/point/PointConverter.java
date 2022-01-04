package src.renderer.point;

import java.awt.Point;

import src.renderer.Display;

public class PointConverter {
  
  private static double scale = 1;
  private static final double ZoomFactor = 1.2;

  private static int cameraPos = 15;
  private static int scaleVar = 1400;

  public static Point convertPoint(MyPoint point3D) {
    double x3d = point3D.y * scale;
    double y3d = point3D.z * scale;
    double depth = point3D.x * scale;
    double[] scaledVals = scale(x3d, y3d, depth);

    int x2d = (int) (Display.WIDTH / 2 + scaledVals[0]);
    int y2d = (int) (Display.HEIGHT / 2 - scaledVals[1]);

    Point point2D = new Point(x2d, y2d);
    return point2D;
  }

  private static double[] scale(double x3d, double y3d, double depth) {
    double dist = Math.sqrt(x3d*x3d + y3d*y3d);
    double theta = Math.atan2(y3d, x3d);
    double relativeDepth = cameraPos - depth;
    double localScale = Math.abs(scaleVar/(relativeDepth + scaleVar));

    dist *= localScale;

    double[] scaledVals = new double[2];
    scaledVals[0] = dist * Math.cos(theta);
    scaledVals[1] = dist * Math.sin(theta);
    return scaledVals;
  }

  public static void zoomIn() {
    scale *= ZoomFactor;
  }

  public static void zoomOut() {
    scale /= ZoomFactor;
  }

  public static void rotateAxisX(MyPoint p, boolean CW, double degrees) {
    // double radius = Math.sqrt(Math.pow(p.y, 2) + Math.pow(p.z, 2));
    // double theta = Math.atan2(p.z, p.y);
    // theta += 2*Math.PI/360*degrees*(CW ? -1 : 1);
    // p.y = radius * Math.cos(theta);
    // p.z = radius * Math.sin(theta);

    degrees = Math.toRadians(degrees*(CW ? -1 : 1));

    double y0 = p.y;
    double z0 = p.z;

    p.y = (y0*Math.cos(degrees)) - (z0*Math.sin(degrees));
    p.z = (y0*Math.sin(degrees)) + (z0*Math.cos(degrees));
  }

  public static void rotateAxisY(MyPoint p, boolean CW, double degrees) {
    // double radius = Math.sqrt(Math.pow(p.x, 2) + Math.pow(p.z, 2));
    // double theta = Math.atan2(p.z, -p.x);
    // theta += 2*Math.PI/360*degrees*(CW ? -1 : 1);
    // p.x = radius * Math.cos(theta);
    // p.z = radius * Math.sin(theta);

    degrees = Math.toRadians(degrees);

    double x0 = p.x;
    double z0 = p.z;

    p.x = (x0*Math.cos(degrees)) + (z0*Math.sin(degrees));
    p.z = -(x0*Math.sin(degrees)) + (z0*Math.cos(degrees));
    
  }

  public static void rotateAxisZ(MyPoint p, boolean CW, double degrees) {
    degrees = Math.toRadians(degrees*(CW ? -1 : 1));

    double x0 = p.x;
    double y0 = p.y;

    p.x = (x0*Math.cos(degrees)) - (y0*Math.sin(degrees));
    p.y = (x0*Math.sin(degrees)) + (y0*Math.cos(degrees));
  }
}
