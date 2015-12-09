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
 * File: OpenBooleanConstraintFigure.java                                                   
 * Program: OpenBooleanConstraintFigure                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-27                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class UnwantedMessageConstraintFigure extends Figure {

	private String s = new String();
	/**
	 * @return the s
	 */
	public String getS() {
		return s;
	}


	public UnwantedMessageConstraintFigure(String s){
		this.setBounds(new Rectangle(0,0,68,28));
		this.s=s;
//		int size = s.length();
//		String left = new String("");
//		for(int i = 0; i<(size/2-1); i++){
//			left+=" ";
//		}
//		setText(left+"��\n"+s);
	}
	
	
	protected void paintFigure(Graphics g) {
		super.paintFigure(g);
		Rectangle r = this.getBounds();
		g.setBackgroundColor(ColorConstants.black);
		g.fillOval(new Rectangle(r.x,r.y,16,16));
		g.drawText(getS(), new Point(r.x,r.y+16));
	}
}
