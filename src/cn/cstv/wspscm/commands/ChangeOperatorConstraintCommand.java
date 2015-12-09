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
 * File: ChangeOperatorConstraintCommand.java                                                   
 * Program: ChangeOperatorConstraintCommand                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-26                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import cn.cstv.wspscm.model.Operator;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class ChangeOperatorConstraintCommand extends Command {

	
	private Operator operator;

	private Rectangle rectangle;
	
	private Rectangle oldRectangle;
	
	public void execute(){
		oldRectangle = new Rectangle(operator.getLocation(), operator.getSize());
		redo();
	}
	
	public void redo() {
		operator.setSize(rectangle.getSize());
		operator.setLocation(rectangle.getLocation());
	}

	/**
	 * @param lifeline the lifeline to set
	 */
	public void setOperator(Object operator) {
		this.operator = (Operator)operator;
		oldRectangle =new Rectangle(this.operator.getLocation(),this.operator.getSize());
	}

	/**
	 * @param rectangle the rectangle to set
	 */
	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}
	
	
	public void undo(){
		operator.setSize(oldRectangle.getSize());
		operator.setLocation(oldRectangle.getLocation());
	}
	
}
