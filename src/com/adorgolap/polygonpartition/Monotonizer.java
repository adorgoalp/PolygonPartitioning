package com.adorgolap.polygonpartition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.adorgolap.helper.Edge;
import com.adorgolap.helper.Helper;
import com.adorgolap.helper.Vertex;
import com.adorgolap.helper.VertexType;

public class Monotonizer {
	ArrayList<Vertex> polygon;
	ArrayList<Vertex> sortedVertex;
	ArrayList<Edge> T = new ArrayList<Edge>();
	ArrayList<Edge> diagonals = new ArrayList<Edge>();
	ArrayList<Edge> edges = new ArrayList<>();

	public Monotonizer(ArrayList<Vertex> allPoints) {
		polygon = allPoints;
		classifyPoints();
		makeEdgeList();
		sortedVertex = sortOverYaxis();
		System.out.println(polygon);
		System.out.println(sortedVertex);
		for (int i = 0; i < sortedVertex.size(); i++) {
			Vertex currentPoint = sortedVertex.get(i);
			if (currentPoint.type == VertexType.START) {
				handleStartVertex(i);
			} else if (currentPoint.type == VertexType.END) {
				handleEndVertex(i);
			} else if (currentPoint.type == VertexType.SPLIT) {
				handleSplitVertex(i);
			} else if (currentPoint.type == VertexType.MERGE) {
				handleMergeVertex(i);
			} else if (currentPoint.type == VertexType.REGULAR_LEFT) {
				handleRegularLeftVertex(i);

			} else if (currentPoint.type == VertexType.REGULAR_RIGHT) {
				handleReularRightVertex(i);
			}
		}
	}

	

	private void handleStartVertex(int indexofCurrentVertex) {
//		System.out.println("in start handler");
		Edge nextEdge = getNextEdge(indexofCurrentVertex);
		nextEdge.helper = sortedVertex.get(indexofCurrentVertex);
		T.add(nextEdge);
//		System.out.println(T);
//		System.out.println("Exiting start handler");
	}

	private void handleEndVertex(int index) {
		Vertex v = sortedVertex.get(index);
		Edge previousEdge = getPreviousEdge(index);
		//System.out.println(">>>> " + previousEdge);
		if (previousEdge.helper!=null && previousEdge.helper.type == VertexType.MERGE)
		{
			Edge diagonal = new Edge(v,previousEdge.helper);
			diagonals.add(diagonal);
		}
		T.remove(previousEdge);
	}
	private void handleSplitVertex(int index) {
//		System.out.println("in split vertex handle ");
		// search T and find left edge
		Vertex v = sortedVertex.get(index);
		double minimumDistance = Double.MAX_VALUE;
		Edge minEdge = null;
		for (Edge edge : T) {
			double xCord = (v.y - edge.start.y) * (edge.start.x - edge.end.x) / (edge.start.y - edge.end.y)
					+ edge.start.x;
			if (xCord < v.x) // left to v
			{
				if ((v.x - xCord) < minimumDistance) {
					minEdge = edge;
				}
			}
		}
//		System.out.println("Split vertex is " + v);
//		System.out.println("Min edge is " + minEdge);
		if(minEdge != null)
		{
			Edge diagonal = new Edge(minEdge.helper, v);
			diagonals.add(diagonal);
			minEdge.helper = v;
//			System.out.println("diagonal is " + diagonal);
		}
		Edge nextEdge = getNextEdge(index);
		nextEdge.helper = v;
		T.add(nextEdge);
		
//		System.out.println("Exiting split vertex handler");
	}
	private void handleMergeVertex(int index) {
//		System.out.println("in mearge vertex");
		Vertex v = sortedVertex.get(index);
//		System.out.println("Vertex " + v);
		Edge previousEdge = getPreviousEdge(index);
//		System.out.println("Previous Edge " + previousEdge);
		if(previousEdge.helper.type == VertexType.MERGE)
		{
			Edge diagonal = new Edge(previousEdge.helper, v);
			diagonals.add(diagonal);
		}
		T.remove(previousEdge);
		double minimumDistance = Double.MAX_VALUE;
		Edge minEdge = null;
		for (Edge edge : T) {
			double xCord = (v.y - edge.start.y) * (edge.start.x - edge.end.x) / (edge.start.y - edge.end.y)
					+ edge.start.x;
			if (xCord < v.x) // left to v
			{
				if ((v.x - xCord) < minimumDistance) {
					minEdge = edge;
				}
			}
		}
//		System.out.println("Min edge is " + minEdge);
		if( minEdge != null && minEdge.helper.type == VertexType.MERGE)
		{
			Edge diagonal = new Edge(minEdge.helper, v);
			diagonals.add(diagonal);
		}
		if(minEdge != null)
		{
			minEdge.helper = v;
		}
	}
	private void handleReularRightVertex(int index) {
//		System.out.println("in right vertex");
		Vertex v = sortedVertex.get(index);
		double minimumDistance = Double.MAX_VALUE;
		Edge minEdge = null;
		for (Edge edge : T) {
			double xCord = (v.y - edge.start.y) * (edge.start.x - edge.end.x) / (edge.start.y - edge.end.y)
					+ edge.start.x;
			if (xCord < v.x) // left to v
			{
				if ((v.x - xCord) < minimumDistance) {
					minEdge = edge;
				}
			}
		}
//		System.out.println("Min edge is " + minEdge);
		if(minEdge != null && minEdge.helper.type == VertexType.MERGE)
		{
			Edge diagonal = new Edge(minEdge.helper, v);
			diagonals.add(diagonal);
		}
		if(minEdge!=null)
		{
			minEdge.helper = v;
		}
	}

	private void handleRegularLeftVertex(int index) {
//		System.out.println("in left vertex");
		Vertex v = sortedVertex.get(index);
//		System.out.println("Vertex " + v);
		Edge previousEdge = getPreviousEdge(index);
//		System.out.println("Previous Edge " + previousEdge);
		if(previousEdge.helper != null && previousEdge.helper.type == VertexType.MERGE)
		{
			Edge diagonal = new Edge(previousEdge.helper, v);
			diagonals.add(diagonal);
		}
		T.remove(previousEdge);
		Edge nextEdge = getNextEdge(index);
		nextEdge.helper = v;
		T.add(nextEdge);
	}
	

	private void classifyPoints() {
		for (int i = 0; i < polygon.size(); i++) {
			Vertex previousPoint = polygon.get((i + polygon.size() - 1) % polygon.size());
			Vertex currentPoint = polygon.get(i);
			Vertex nextPoint = polygon.get((i + 1) % polygon.size());

			// start and split
			if (currentPoint.y < previousPoint.y && currentPoint.y < nextPoint.y) {
				double product = getCrossProduct(previousPoint, currentPoint, nextPoint);
				if (product > 0)// start vertex
				{
					currentPoint.type = VertexType.START;
				} else// split vertex
				{
					currentPoint.type = VertexType.SPLIT;
				}
			} // merge and end
			else if (currentPoint.y > previousPoint.y && currentPoint.y > nextPoint.y) {
				double product = getCrossProduct(previousPoint, currentPoint, nextPoint);
				if (product > 0)// end vertex
				{
					currentPoint.type = VertexType.END;
				} else// merge vertex
				{
					currentPoint.type = VertexType.MERGE;
				}
			} // regular
			else {
				if (previousPoint.y > currentPoint.y && nextPoint.y < currentPoint.y) {
					currentPoint.type = VertexType.REGULAR_RIGHT;
				} else {
					currentPoint.type = VertexType.REGULAR_LEFT;
				}
			}
		}
	}

	private double getCrossProduct(Vertex previousPoint, Vertex currentPoint, Vertex nextPoint) {
		Vertex v1 = new Vertex(previousPoint.x - currentPoint.x, previousPoint.y - currentPoint.y);
		Vertex v2 = new Vertex(nextPoint.x - currentPoint.x, nextPoint.y - currentPoint.y);
		return (v1.x * v2.y - v1.y - v2.x);
	}

	private ArrayList<Vertex> sortOverYaxis() {
		ArrayList<Vertex> sortedList = new ArrayList<Vertex>(polygon);
		Comparator<Vertex> comparator = new Comparator<Vertex>() {

			@Override
			public int compare(Vertex o1, Vertex o2) {
				return o1.y - o2.y;
			}
		};
		Collections.sort(sortedList, comparator);
		return sortedList;
	}

	private void makeEdgeList() {
		for (int i = 0; i < polygon.size(); i++) {
			Vertex currentPoint = polygon.get(i);
			Vertex nextPoint = polygon.get((i + 1) % polygon.size());
			Edge egde = new Edge(currentPoint, nextPoint);
			edges.add(egde);
		}
	}

	public ArrayList<Edge> getDiagonals() {

		return diagonals;
	}

	private Edge getNextEdge(int indexofCurrentVertex) {
		int indexInPolygon = polygon.indexOf(sortedVertex.get(indexofCurrentVertex));
		return edges.get(indexInPolygon);
	}

	private Edge getPreviousEdge(int indexofCurrentVertex) {
		int indexInPolygon = polygon.indexOf(sortedVertex.get(indexofCurrentVertex));
		return edges.get((indexInPolygon + polygon.size() - 1) % polygon.size());
	}
}
