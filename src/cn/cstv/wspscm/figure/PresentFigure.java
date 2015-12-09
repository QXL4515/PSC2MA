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
 * File: BooleanConstraintFigure.java                                                   
 * Program: BooleanConstraintFigure                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-27                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.figure;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class PresentFigure extends Figure {
	
	IFigure footer;
	
	String text;
	
	public PresentFigure(String s){
		this.setBounds(new Rectangle(0,0,68,28));
		text = s;
//		int size = s.length();
//		String left = new String("");
//		for(int i = 0; i<(size/2-1); i++){
//			left+=" ";
//		}
//		setText(left+"¡ð\n"+s);
	}
	

	/**
	 * @return the footer
	 */
	public IFigure getFooter() {
		return footer;
	}
	
	

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}


	protected void paintFigure(Graphics g) {
		super.paintFigure(g);
		Rectangle r = getBounds();
		g.setLineWidth(2);
		g.drawOval(new Rectangle(r.x,r.y,16,16));
		g.drawText(getText(), new Point(r.x,r.y+16));
	}
	
}
