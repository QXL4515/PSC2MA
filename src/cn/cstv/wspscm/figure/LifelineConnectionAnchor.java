/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package cn.cstv.wspscm.figure;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;

public class LifelineConnectionAnchor extends AbstractConnectionAnchor {

	protected int offset;

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
		fireAnchorMoved();
	}

	public LifelineConnectionAnchor(IFigure owner) {
		super(owner);
	}

    @Override
	public Point getLocation(Point reference) {
//		System.out.println("("+reference.x+", "+reference.y+")");

		Rectangle r = getOwner().getBounds().getCopy();
//		
//		offset=reference.y-r.y;
//		System.out.println(offset);

		int x, y;
		x = r.x + r.width / 2;
		y = r.y+offset;
//		System.out.println(offset);


		PrecisionPoint p = new PrecisionPoint(x, y);
		getOwner().translateToAbsolute(p);
		return p;

	}
    
    public Point getReferencePoint() {
    	if (getOwner() == null)
    		return null;
    	else {
    		Point ref = getOwner().getBounds().getCenter();
    		getOwner().translateToAbsolute(ref);
    		return ref;
    	}
    }


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (o instanceof LifelineConnectionAnchor) {
			LifelineConnectionAnchor fa = (LifelineConnectionAnchor) o;

			if (fa.offset == this.offset && fa.getOwner() == this.getOwner()) {
				return true;
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return (this.offset * 47) ^ this.getOwner().hashCode();
	}

}
