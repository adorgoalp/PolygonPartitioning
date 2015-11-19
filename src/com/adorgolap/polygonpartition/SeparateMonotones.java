package com.adorgolap.polygonpartition;

import java.util.ArrayList;

import com.adorgolap.helper.Edge;
import com.adorgolap.helper.Vertex;

public class SeparateMonotones {
	ArrayList<Vertex> polygon = new ArrayList<Vertex>();
	ArrayList<Edge> diagonals = new ArrayList<Edge>();
	ArrayList<Edge> edges = new ArrayList<Edge>();
	ArrayList<Vertex> temp = new ArrayList<Vertex>();
	public SeparateMonotones(ArrayList<Vertex> allPoints, ArrayList<Edge> diagonals) {
		this.polygon = allPoints;
		this.diagonals = diagonals;
		System.out.println("===== Separation ======");
		makeAdjacencyList();
		for(Vertex v :polygon)
		{
			System.out.println(v + " "+v.adjacent);
		}
		doIt(0);
	}
	Vertex startV;
	private void doIt(int start) {
		
		startV = polygon.get(start);
		f(startV);
		System.out.println("bal " + temp);
	}
	private void f(Vertex startVertex) {
		if(temp.size() > 0 && startVertex.equals(startV))
		{
			return;
		}
		temp.add(startVertex);
		if(startVertex.adjacent.size() == 1)
		{
			f(startVertex.adjacent.get(0));
		}else
		{
			for(int i = 0 ; i < startVertex.adjacent.size() ;i++)
			{
				Edge e = new Edge(startVertex, startVertex.adjacent.get(i));
				for(int j = 0 ; j < diagonals.size() ;j++)
				{
					if(diagonals.get(j).equals(e))
					{
						Vertex tempz= startVertex.adjacent.get(i);
						startVertex.adjacent.remove(tempz);
						f(tempz);
						break;
					}
				}
			}
		}
	}

	private boolean isEndOfDiagonal() {
		// TODO Auto-generated method stub
		return false;
	}
	private void makeAdjacencyList() {
		for(int i = 0 ; i < polygon.size() ;i++)
		{
			Vertex currentVertex = polygon.get(i);
			Vertex nextVertex = polygon.get((i + 1) % polygon.size());
			currentVertex.adjacent.add(nextVertex);
			for(Edge d : diagonals)
			{
				if(currentVertex.equals(d.start))
				{
					currentVertex.adjacent.add(d.end);
				}else if(currentVertex.equals(d.end))
				{
					currentVertex.adjacent.add(d.start);
				}
			}
		}
	}
	

}
