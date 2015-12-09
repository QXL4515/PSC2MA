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
 * File: CreateLineConnectionCommand.java                                                   
 * Program: CreateLineConnectionCommand                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-22                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.commands;

import java.awt.MouseInfo;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;



import cn.cstv.wspscm.model.Lifeline;
import cn.cstv.wspscm.model.LineConnection;




/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class CreateLineConnectionCommand extends Command {

	private Lifeline source, target;
	private LineConnection connection;
	

	public boolean canExecute(){
		if(source==null||target==null){
			return false;
		}
//		if(source.equals(target))
//			return false;
		return true;
	}
	
	public void execute(){
		connection.attachSource();
		connection.attachTarget();
		
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(Object source) {
		this.source = (Lifeline)source;
		connection.setSource(source);
		connection.setLocation(new Point(MouseInfo.getPointerInfo().getLocation().x, 
				MouseInfo.getPointerInfo().getLocation().y));
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(Object target) {
		this.target = (Lifeline)target;
		connection.setTarget(target);
	}
	
	/**
	 * @param connection the connection to set
	 */
	public void setConnection(Object connection) {
		this.connection = (LineConnection)connection;
	}
	
	public void undo(){
		connection.detachSource();
		connection.detachTarget();
	}
}
