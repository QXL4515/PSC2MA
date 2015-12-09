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
 * File: OperatorFigure.java                                                   
 * Program: OperatorFigure                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-26                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.figure;

import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.swt.widgets.Shell;

import cn.cstv.wspscm.model.Operator;


/**
 * @author Su Zhiyong & Zhang Pengcheng
 * 
 */
public class OperatorFigure extends Label {

	private String type;
	
	private  int num;
	private Shell shell;

	public OperatorFigure(Operator operator) {
		
		type = operator.getType();
		num = operator.getNum();
		try{
			if(type.equals("Loop")){
				setText(operator.getType()+" "+operator.getNum());
			}else{
			   setText(operator.getType());
			}

			setBorder(new CompoundBorder(new LineBorder(), new MarginBorder(0)));

			this.setLabelAlignment(PositionConstants.LEFT);
			this.setTextAlignment(PositionConstants.TOP);
			
		}catch(Exception e){
		}
	}

	protected void paintFigure(Graphics g) {
		super.paintFigure(g);
		Rectangle rr = this.getBounds();


		Rectangle textRectangle = getTextBounds();
		// g.drawLine(r.x, r.y, r.right()+5, r.y); //Top line
		g.drawLine(textRectangle.x, textRectangle.bottom(), textRectangle
				.right(), textRectangle.bottom()); // Bottom line
		// g.drawLine(r.x, r.bottom(), r.x, r.y); //left line
		g.drawLine(textRectangle.right(), textRectangle.y
				+ textRectangle.height, textRectangle.right() + 5,
				textRectangle.y);// right line
		try{
			if (type.equals("Alt") || type.equals("Par")) {
				g.setLineStyle(Graphics.LINE_DOT);
				Rectangle operatorRectangle = getBounds();
				for(int i = 0; i < num - 1; i++){
					int yCurrent = operatorRectangle.y+(i+1)*operatorRectangle.height/num;
					g.drawLine(operatorRectangle.x, yCurrent, operatorRectangle.right(),yCurrent);
				}
			}
		}catch(Exception e){
		
		}
	}
}