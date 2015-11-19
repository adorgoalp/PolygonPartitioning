package com.adorgolap.helper;

public class Edge {
	public Vertex start, end;
	public Vertex helper;
	public Edge(Vertex s, Vertex e) {
		start = s;
		end = e;
	}
	public Edge(Vertex s, Vertex e,Vertex h) {
		start = s;
		end = e;
		helper = h;
	}
	@Override
	public String toString() {
		return "S "+start+" E "+end + " H "+helper;
	}
	@Override
	public boolean equals(Object obj) {
		Edge e = (Edge)obj;
		if((e.start.equals(this.start) && e.end.equals(this.end)) || (e.start.equals(this.end) && e.end.equals(this.start)))
			return true;
		return false;
	}
}
