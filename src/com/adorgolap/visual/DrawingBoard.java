package com.adorgolap.visual;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.adorgolap.helper.Edge;
import com.adorgolap.helper.Vertex;
import com.adorgolap.polygonpartition.Main;

@SuppressWarnings("serial")
public class DrawingBoard extends JPanel{
	ArrayList<Vertex> polygon = new ArrayList<Vertex>();
	ArrayList<Edge> diagonals = new ArrayList<Edge>();
	Vertex p1,p2;
	public DrawingBoard(ArrayList<Vertex> allPoints) {
		polygon = allPoints;
	}

	public DrawingBoard(ArrayList<Vertex> allPoints, ArrayList<Edge> diagonals) {
		polygon = allPoints;
		this.diagonals = diagonals;
	}

	@Override
	protected void paintComponent(Graphics g) {
		drawGrid(g);
		g.setColor(Color.ORANGE);
		for(Vertex p : polygon)
		{
			g.fillOval(p.x-3, p.y-3, 6, 6);
			g.drawString(p.toString()+" "+p.type, p.x, p.y);
		}
		g.setColor(Color.RED);
		for(int i = 0 ; i < polygon.size()  ;i++)
		{
			p1 = polygon.get(i);
			p2 = polygon.get((i+1)%polygon.size());
			g.drawLine(p1.x, p1.y,p2.x,p2.y);
		}
		g.setColor(Color.YELLOW);
		for(Edge e : diagonals)
		{
			g.drawLine(e.start.x, e.start.y, e.end.x, e.end.y);
		}
	}

	private void drawGrid(Graphics g) {
		
		for(int i = 0 ; i < Main.HEIGHT;i = i + 50)
		{
			g.setColor(Color.DARK_GRAY);
			g.drawLine( i, 50,i, Main.HEIGHT);
			g.drawLine( Main.WIDTH, i,50, i);
			g.setColor(Color.WHITE);
			g.drawString(i+"", i-10, 30);
			g.drawString(i+"", 20, i);
		}
	}
}
