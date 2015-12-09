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
 * Package: cn.cstv.wspscm.commands                                            
 * File: ChangeConstraintCommand.java                                                   
 * Program: ChangeConstraintCommand                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-21                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.commands;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import cn.cstv.wspscm.ConstantsSet;
import cn.cstv.wspscm.model.Lifeline;


/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class ChangeLifelineConstraintCommand extends Command {
	
	private Lifeline lifeline;

	private Rectangle rectangle;
	
	private Rectangle oldRectangle;
	
//	public ChangeLifelineConstraintCommand(Lifeline lifeline, ChangeBoundsRequest req, 
//			Rectangle rectangle) {
//		if (lifeline == null || req == null || rectangle == null) {
//			throw new IllegalArgumentException();
//		}
//		this.lifeline = lifeline;
//		this.request = req;
//		this.rectangle = rectangle.getCopy();
//		setLabel("move / resize");
//	}

//	public ChangeLifelineConstraintCommand(Lifeline lifeline, 
//			Rectangle rectangle) {
//		if (lifeline == null || rectangle == null) {
//			throw new IllegalArgumentException();
//		}
//		this.lifeline = lifeline;
//		this.rectangle = rectangle.getCopy();
//		setLabel("move / resize");
//	}
	
	public void execute(){
		oldRectangle = new Rectangle(lifeline.getLocation(), lifeline.getSize());
		redo();
	}
	
	public void redo() {
		lifeline.setSize(rectangle.getSize());
		Point p = rectangle.getLocation();
		p.setLocation(p.x,ConstantsSet.LifelineOffset);
		lifeline.setLocation(p);
	}

	/**
	 * @param lifeline the lifeline to set
	 */
	public void setLifeline(Object lifeline) {
		this.lifeline = (Lifeline)lifeline;
		oldRectangle =new Rectangle(this.lifeline.getLocation(),this.lifeline.getSize());
	}

	/**
	 * @param rectangle the rectangle to set
	 */
	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}
	
	
	public void undo(){
		lifeline.setSize(oldRectangle.getSize());
		lifeline.setLocation(oldRectangle.getLocation());
	}
	
}
