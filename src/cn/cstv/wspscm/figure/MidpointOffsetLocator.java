package cn.cstv.wspscm.figure;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.geometry.Point;

/**
 * @author hp
 * 
 */
public class MidpointOffsetLocator extends MidpointLocator {

	private Point offset;

	/**
	 * @param c
	 * @param i
	 */
	public MidpointOffsetLocator(Connection c, int i, Point p) {
		super(c, i);
		offset = p;
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
