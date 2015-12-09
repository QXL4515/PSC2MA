package cn.cstv.automata.model;

import org.eclipse.draw2d.geometry.Dimension;

public class ConnectionBendpoint{

	private float weight = 0.5f;
	private Dimension d1, d2;

	public ConnectionBendpoint() {}

	public Dimension getFirstRelativeDimension() {
		return d1;
	}

	public Dimension getSecondRelativeDimension() {
		return d2;
	}

	public float getWeight() {
		return weight;
	}

	public void setRelativeDimensions(Dimension dim1, Dimension dim2) {
		d1 = dim1;
		d2 = dim2;
	}

	public void setWeight(float w) {
		weight = w;
	}

}
