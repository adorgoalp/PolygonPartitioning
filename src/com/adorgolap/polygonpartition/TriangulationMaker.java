package com.adorgolap.polygonpartition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;

import com.adorgolap.helper.Edge;
import com.adorgolap.helper.Vertex;
import com.adorgolap.helper.VertexType;

public class TriangulationMaker {
	ArrayList<Vertex> monotone;
	ArrayList<Vertex> sortedList;
	ArrayList<Edge> diagonals = new ArrayList<Edge>();

	public TriangulationMaker(ArrayList<Vertex> allPoints) {
		monotone = new ArrayList<Vertex>(allPoints);
		classifyVertex();
		sort();
//		System.out.println(sortedList);
		Stack<Vertex> stack = new Stack<Vertex>();
		stack.push(sortedList.get(0));
		stack.push(sortedList.get(1));
		for(int j = 2 ; j < sortedList.size()-1 ;j++)
		{
//			System.out.println("stack " + stack);
			Vertex uj = sortedList.get(j);
//			System.out.println("uj " + uj);
			if(!stack.isEmpty())
			{
				if(stack.peek().type != uj.type)
				{
//					System.out.println("??? " + stack + " size " + stack.size());
					ArrayList<Vertex> poppedList = new ArrayList<Vertex>(stack);
					stack.clear();
					poppedList.remove(0);
//					System.out.println("popped list" + poppedList);
					for(int i = 0 ; i < poppedList.size(); i++)
					{
						Edge diagonal = new Edge(uj, poppedList.get(i));
						diagonals.add(diagonal);
					}
					stack.push(sortedList.get(j-1));
					stack.push(uj);
				}else
				{
					stack.pop();
					Vertex temp = null;
					while(true && !stack.isEmpty())
					{
						Vertex checkVertex = stack.peek();
						
						boolean isDiagonalPossible = getDiagonalPossibility(checkVertex,uj);
						if(isDiagonalPossible)
						{
							temp = stack.pop();
							Edge diagonal = new Edge(temp, uj);
							diagonals.add(diagonal);
						}else
						{
							break;
						}
						
					}
					if(temp != null)
					{
						stack.push(temp);
					}
					stack.push(uj);
				}
			}
		}
		while(stack.size() > 2)
		{
			stack.pop();
			Vertex temp = stack.pop();
			if(stack.isEmpty())
			{
				break;
			}
			Edge diagonal = new Edge(temp, sortedList.get(sortedList.size()-1));
			diagonals.add(diagonal);
		}
		
	}

	private boolean getDiagonalPossibility(Vertex checkVertex, Vertex uj) {
		int prevVertexIndexOfUj = (monotone.indexOf(uj) -1 + monotone.size())%monotone.size();
		Vertex prevVertex = monotone.get(prevVertexIndexOfUj);
		Vertex vector1 = new Vertex(checkVertex.x-uj.x, checkVertex.y-uj.y);
		Vertex vector2  = new Vertex(prevVertex.x-uj.x, prevVertex.y-uj.y);
		double crossResult = vector1.x*vector2.y - vector1.y*vector2.x;
//		System.out.println("Uj " + uj + " check v " + checkVertex + " prev " + prevVertex);
//		System.out.println("Cross " + crossResult);
		if(crossResult > 0)
			return false;
		else 
			return true;
	}

	private void sort() {
		Comparator<Vertex> comparator = new Comparator<Vertex>() {

			@Override
			public int compare(Vertex o1, Vertex o2) {
				return o1.y - o2.y;
			}
		};
		sortedList = new ArrayList<>(monotone);
		Collections.sort(sortedList, comparator);
	}

	private void classifyVertex() {
		Vertex top = monotone.get(0);
		Vertex bottom = monotone.get(0);
		for (int i = 0; i < monotone.size(); i++) {
			Vertex v = monotone.get(i);
			if (v.y > top.y) {
				top = v;
			}
			if (v.y < bottom.y) {
				bottom = v;
			}
		}
//		System.out.println(top + " " + bottom);

		Vertex v;
		int topIndex = monotone.indexOf(top);
		int bottomIndex = monotone.indexOf(bottom);
		while(true)
		{
			monotone.get(topIndex).type = VertexType.CHAIN_RIGHT;
			topIndex = (topIndex+1)%monotone.size();
			if(topIndex == bottomIndex)
			{
				break;
			}
		}
		for(int i = 0 ; i < monotone.size();i++)
		{
			if(monotone.get(i).type == null)
			{
				monotone.get(i).type = VertexType.CHAIN_LEFT;
			}
		}
		
	}

	public ArrayList<Edge> getDiagonals() {
		return diagonals;
	}

	private double getCrossProduct(Vertex previousPoint, Vertex currentPoint, Vertex nextPoint) {
		Vertex v1 = new Vertex(previousPoint.x - currentPoint.x, previousPoint.y - currentPoint.y);
		Vertex v2 = new Vertex(nextPoint.x - currentPoint.x, nextPoint.y - currentPoint.y);
		return (v1.x * v2.y - v1.y - v2.x);
	}

}
