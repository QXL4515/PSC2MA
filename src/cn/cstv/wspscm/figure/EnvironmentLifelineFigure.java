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
 * File: LifelineFigure.java                                                   
 * Program: LifelineFigure                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-23                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.figure;

import java.awt.BasicStroke;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import cn.cstv.wspscm.model.EnvironmentLifeline;


/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class EnvironmentLifelineFigure extends Label {

	public EnvironmentLifelineFigure(EnvironmentLifeline lifeline){
		setText(lifeline.getName());
		this.setTextAlignment(TOP);
		setBorder(new MarginBorder(10,10,10,10));
	}
	
	protected void paintFigure(Graphics g) {
		super.paintFigure(g);
		Rectangle r = getTextBounds();

		r.expand(9, 9);
		for(int i=1; i<=15; i++) {
			int perDst = (r.right() - r.x)/15;
			if(i%2 != 0) {
				g.drawLine(r.x+perDst*i, r.y, r.x+perDst*(i+1), r.y);
			}
		}
		for(int i=1; i<=15; i++) {
			int perDst = (r.right() - r.x)/15;
			if(i%2 != 0) {
				g.drawLine(r.x+perDst*i, r.bottom(), r.x+perDst*(i+1), r.bottom());
			}
		}
		for(int i=1; i<=8; i++) {
			int perDst = (r.bottom() - r.y)/8;
			if(i%2 != 0) {
				g.drawLine(r.x, r.y+perDst*i, r.x, r.y+perDst*(i+1));		
			}
		}
		for(int i=1; i<=8; i++) {
			int perDst = (r.bottom() - r.y)/8;
			if(i%2 != 0) {
				g.drawLine(r.right(), r.y+perDst*i, r.right(), r.y+perDst*(i+1));		
			}
		}
		//g.drawLine(r.x, r.y, r.right(), r.y); //Top line
		//g.drawLine(r.x, r.bottom(), r.right(), r.bottom()); //Bottom line
		//g.drawLine(r.x, r.bottom(), r.x, r.y); //left line
		//g.drawLine(r.right(), r.y + r.height, r.right(), r.y);//right line
		
		//lifeline
		int x = r.x+r.width/2;
		int y = r.y+r.height;
		for(int i = 0; i< 100;i++){
			g.drawLine(new Point(x, y+i*10), new Point(
					x, y+i*10+6));

		}

	}

}
