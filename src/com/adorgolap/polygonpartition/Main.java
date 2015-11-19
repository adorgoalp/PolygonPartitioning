package com.adorgolap.polygonpartition;

import java.awt.Color;
import java.security.AllPermission;
import java.security.Policy;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.adorgolap.helper.Edge;
import com.adorgolap.helper.Helper;
import com.adorgolap.helper.Vertex;
import com.adorgolap.visual.DrawingBoard;

public class Main {
	
	public static final int INPUT_SIZE = 20;
	public static final int HEIGHT = 600;
	public static final int WIDTH = 600;

	public static void main(String[] args) {
		ArrayList<Vertex> allPoints = new ArrayList<Vertex>();
		ArrayList<Edge> diagonals;
		allPoints = Helper.takeInput();
		Monotonizer m  = new Monotonizer(allPoints);
		diagonals = m.getDiagonals();
		System.out.println(">>>>>>>>>>>>>>"+ diagonals);
		SeparateMonotones sm = new SeparateMonotones(allPoints,diagonals);
//		TriangulationMaker tm = new TriangulationMaker(allPoints);
//		diagonals  = tm.getDiagonals();
		DrawingBoard drawingBoard = new DrawingBoard(allPoints,diagonals);
		JFrame frame = new JFrame("Polygon");
		frame.setBackground(Color.BLACK);
		frame.add(drawingBoard);
		frame.setSize(HEIGHT, WIDTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

}
