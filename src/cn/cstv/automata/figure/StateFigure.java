package cn.cstv.automata.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class StateFigure extends Label {

	private int type;
	private String name;
	private Point loc;

	// /////Just For Test////////////
	private Label label;

	/**
	 * Rectangle for the node.
	 */
	private Ellipse ellipse;

	/**
	 * Constructor for a Digraph1NodeFigure.
	 * 
	 * @param number
	 *            the node number in the directed graph.
	 */
	public StateFigure(int type, String name, Point loc) {
		setLayoutManager(new XYLayout());
		this.ellipse = new Ellipse();
		this.ellipse.setLocation(loc);
		this.ellipse.setSize(new Dimension(30, 30));
		if (type == -1 || type == -2) {
			this.ellipse.setBackgroundColor(ColorConstants.gray);
		}
		if (type == 0 || type == 3 || type == -2) {
			this.ellipse.setBackgroundColor(ColorConstants.yellow);
		}
		if (type == 2 || type == 3) {
			Ellipse e = new Ellipse();
			e.setLocation(new Point(loc.x + 2, loc.y + 2));
			e.setSize(new Dimension(26, 26));
			this.ellipse.add(e);
		}
		add(this.ellipse);
		this.label = new Label();
		this.label.setText(name); //$NON-NLS-1$
		add(this.label);

		// getRectangleFigure().setBackgroundColor(ColorConstants.lightGreen);
	}

	public Label getLabel() {
		return this.label;
	}

	public Ellipse getEllipse() {
		return ellipse;
	}

	/*
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	@Override
	public void paintFigure(Graphics g) {

		Rectangle r = getBounds().getCopy();
		setConstraint(getEllipse(), new Rectangle(0, 0, r.width, r.height));
		setConstraint(getLabel(), new Rectangle(0, 0, r.width, r.height));
		getEllipse().invalidate();
		getLabel().invalidate();
	}

	// /////////////////////////////
	// public StateFigure() {
	// }
	//
	// public StateFigure(int type, String name) {
	// super();
	// this.type = type;
	// this.name = name;
	// setText(name);
	// }
	//
	// public StateFigure(int type, String name, Point loc) {
	// super();
	// this.type = type;
	// this.name = name;
	// this.loc = loc;
	// this.setText(name);
	// System.out.println("20");
	// }
	//
	// protected void paintFigure(Graphics g) {
	// super.paintFigure(g);
	// this.setLocation(loc);
	// Dimension r = this.getTextSize();
	// int radius = r.height < r.width ? r.width : r.height;
	// int xOffset = r.height < radius ? (radius - r.height) / 2 : 0;
	// int yOffset = r.width < radius ? (radius - r.width) / 2 : 0;
	// // 将圆形的中心定位于Label的中心
	// g.drawOval(loc.x - xOffset, loc.y - yOffset, radius, radius);
	// if (type == -1) {
	// // Error状态，设置背景颜色为灰
	// g.setBackgroundColor(ColorConstants.gray);
	// } else if (type == 0) {
	// // 开始状态，加导入线
	// g.drawLine(new Point(loc.x - xOffset - 10, loc.y - yOffset), new Point(
	// loc.x - xOffset, loc.y - yOffset));
	// } else if (type == 1) {
	// // 不做任何效果
	// } else if (type == 2) {
	// // Accept状态，设置两个圆的图形
	// g.drawOval(loc.x - xOffset + 1, loc.y - yOffset + 1, radius - 2,
	// radius - 2);
	// }
	// }
	//
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param loc
	 *            the loc to set
	 */
	public void setLoc(Point loc) {
		this.loc = loc;
	}

	/**
	 * @return the loc
	 */
	public Point getLoc() {
		return loc;
	}

}
