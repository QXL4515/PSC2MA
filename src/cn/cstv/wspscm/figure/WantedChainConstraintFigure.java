/*************************************************************************
 * Copyright (c) 2006, 2008. All rights reserved. This program and the   
 * accompanying materials are made available under the terms of the      
 * Eclipse Public License v1.0 which accompanies this distribution,       
 * and is available at http://www.eclipse.org/legal/epl-v10.html         
 * 
 * Contributors:                                                         
 * Author: Su Zhiyong & Zhang Pengcheng                                 
 * Group: CSTV (Chair of Software Testing & Verification) Group          
 * E-mail: zhiyongsu@gmail.com, pchzhang@seu.edu.cn                     
 ***********************************************************************/

/***********************************************************************
 * Project: cn.cstv.wspscm                                          
 * Package: cn.cstv.wspscm.figure                                            
 * File: OpenChainConstraintFigure.java                                                   
 * Program: OpenChainConstraintFigure                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-27                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.figure;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 * 
 */
public class WantedChainConstraintFigure extends Figure {

	private boolean LeftToRight;
	
	String s = "";

	public WantedChainConstraintFigure(boolean LeftToRight,String s) {
		this.setBounds(new Rectangle(0, 0, 68, 34));
		this.LeftToRight = LeftToRight;
		this.s = s;
//		int size = s.length();
//		String left = new String("");
//		for(int i = 0; i<(size/2-2); i++){
//			left+=" ";
//		}
//		setText(left+"==>\n"+s);
	}

	/**
	 * @return the s
	 */
	public String getS() {
		return s;
	}

	protected void paintFigure(Graphics g) {
		super.paintFigure(g);
		Rectangle r = this.getBounds();
		// g.setBackgroundColor(ColorConstants.black);
		// g.fillPolygon(new
		// int[]{r.x,r.y+4,r.x+20,r.y+4,r.x+20,r.y,r.x+28,r.y+6
		// ,r.x+20,r.y+12,r.x+20,r.y+8,r.x,r.y+8,r.x,r.y+4});

		if (LeftToRight) {
			g.drawPolyline(new int[] { r.x + 1, r.y + 6, r.x + 36, r.y + 6,
					r.x + 36, r.y + 1, r.x + 51, r.y + 11, r.x + 36, r.y + 21,
					r.x + 36, r.y + 16, r.x + 1, r.y + 16, r.x + 1, r.y + 6 });

		} else {
			g.drawPolyline(new int[] { r.x + 1, r.y + 11, r.x + 16,
							r.y + 1, r.x + 16, r.y + 6, r.x + 51, r.y + 6,
							r.x + 51, r.y + 16, r.x + 16, r.y + 16, r.x + 16,
							r.y + 21, r.x + 1, r.y + 11 });

		}
		g.drawText(getS(), new Point(r.x,r.y+22));

	}
}
