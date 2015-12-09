package cn.cstv.wspscm.figure;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;

/**
 * @author hp
 *
 */
public class SourcePointOffsetLocator extends SourcePointLocator {

	private Point offset;
	/**
	 * @param c
	 * @param i
	 */
	public SourcePointOffsetLocator(Connection c, int i, Point p) {
		super(c, i);
		offset = p;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Point getReferencePoint() {
		Point point = super.getReferencePoint();
		return point.getTranslated(offset);
	}
	
	public Point getOffset() {
		return offset;
	}

	public void setOffset(Point offset) {
		this.offset = offset;
	}
}
