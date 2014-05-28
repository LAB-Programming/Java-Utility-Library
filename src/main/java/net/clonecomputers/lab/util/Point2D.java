package net.clonecomputers.lab.util;

import static java.lang.Math.*;

public class Point2D implements Comparable<Point2D> {
	public final double x;
	public final double y;
	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public String toString() {
		return String.format("(%.2f,%.2f)", x,y);
	}
	
	public static double dot(Point2D a, Point2D b) {
		return (a.x*b.x) + (a.y*b.y);
	}
	
	public static double dot(Point2D... points) {
		double x=1,y=1;
		for(Point2D p: points) {
			x *= p.x;
			y *= p.y;
		}
		return x+y;
	}
	
	public static double cross(Point2D a, Point2D b) {
		return a.x*b.y - b.x*a.y;
	}
	
	public static Point2D sum(Point2D a, Point2D b) {
		return new Point2D(a.x + b.x, a.y + b.y);
	}
	
	public static Point2D sum(Point2D... points) {
		double x=0,y=0;
		for(Point2D p: points) {
			x += p.x;
			y += p.y;
		}
		return new Point2D(x,y);
	}
	
	public static double abs(Point2D a) {
		return sqrt((a.x*a.x) + (a.y*a.y));
	}
	
	public static Point2D norm(Point2D a) {
		return prod(a,1/abs(a));
	}
	
	public static Point2D diff(Point2D a, Point2D b) {
		return new Point2D(a.x - b.x, a.y - b.y);
	}
	
	public static Point2D prod(Point2D a, double b) {
		return new Point2D(
			a.x * b,
			a.y * b
		);
	}
	
	public static double dist(Point2D a, Point2D b) {
		return sqrt((a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y));
	}
	
	public boolean equals(Object o) {
		return o instanceof Point2D && o != null && ((Point2D)o).x == x && ((Point2D)o).y == y;
	}
	
	public int hashCode() {
		return new Double(x).hashCode() ^ new Double(y).hashCode();
	}
	
	@Override
	public int compareTo(Point2D p) {
		return this.equals(p)?0:this.hashCode() > p.hashCode()?1:-1;
	}
}