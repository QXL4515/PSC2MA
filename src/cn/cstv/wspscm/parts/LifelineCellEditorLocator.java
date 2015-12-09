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
 * Package: cn.cstv.wspscm.parts                                            
 * File: LifelineCellEditorLocator.java                                                   
 * Program: LifelineCellEditorLocator                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-22                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class LifelineCellEditorLocator implements CellEditorLocator {


	private IFigure figure;

	public LifelineCellEditorLocator(IFigure f) {
		figure = f;
	}


	public void relocate(CellEditor celleditor) {
		// TODO Auto-generated method stub
		Text text = (Text) celleditor.getControl();

		Point pref = text.computeSize(-1, -1);
		Rectangle rect = figure.getBounds().getCopy();
		figure.translateToAbsolute(rect);
		text.setBounds(rect.x+5, rect.y+5, pref.x+5, pref.y+5);
	}



}
