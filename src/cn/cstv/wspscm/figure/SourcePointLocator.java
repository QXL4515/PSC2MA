package cn.cstv.wspscm.figure;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.geometry.Point;

/**
 * @author hp
 *
 */
public class SourcePointLocator extends ConnectionLocator {

	private int index;
	/**
	 * @param connection
	 */
	public SourcePointLocator(Connection c, int i) {
		super(c);
		index = i;
		// TODO Auto-generated constructor stub
	}
	
	protected int getIndex() {
		return index;
	}
	
	protected Point getReferencePoint() {
		Connection conn = getConnection();
		Point p = Point.SINGLETON;
		Point p1 = conn.getPoints().getPoint(getIndex());
		Point p2 = conn.getPoints().getPoint(getIndex() + 1);
		conn.translateToAbsolute(p1);
		conn.translateToAbsolute(p2);
		p.x = (p2.x - p1.x) / 4 + p1.x;
		p.y = (p2.y - p1.y) / 4 + p1.y;
		return p;
	}

}
