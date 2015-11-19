package com.adorgolap.helper;

import java.util.ArrayList;

public class Vertex {
	
	public int x;
	public int y;
	public VertexType type;
	public ArrayList<Vertex> adjacent = new ArrayList<Vertex>();
	public Vertex(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Vertex(int x, int y, VertexType type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}
	public double getDistance(Vertex p)
	{
		return Math.sqrt((p.x-x)*(p.x-x) + (p.y-y)*(p.y-y));
	}
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	public double getAngle(Vertex A, Vertex B) {
		Vertex C = this;
		double a = B.getDistance(C);
		double b = A.getDistance(C);
		double c = A.getDistance(B);
		double cosC = (a*a+b*b-c*c)/(2*a*b);
		return Math.acos(cosC);
	}
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null) {
	        return false;
	    }
	    if (getClass() != obj.getClass()) {
	        return false;
	    }
	    final Vertex o = (Vertex) obj;
	    if(this.x == o.x && this.y == o.y)
	    {
	    	return true;
	    }
	    return false;
	}
}

