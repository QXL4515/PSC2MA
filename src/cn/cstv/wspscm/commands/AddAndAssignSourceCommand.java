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
 * File: AddAndAssignSourceCommand.java                                                   
 * Program: AddAndAssignSourceCommand                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-9-3                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.commands;

import org.eclipse.gef.commands.Command;

import cn.cstv.wspscm.model.Lifeline;
import cn.cstv.wspscm.model.LineConnection;
import cn.cstv.wspscm.model.Operator;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class AddAndAssignSourceCommand extends Command {


	private Lifeline source,child;
	private Operator operator;
	private LineConnection connection;
	/**
	 * @param source 
	 *         the source to set
	 */
	public void setSource(Object source) {
		this.source = (Lifeline)source;
	}
	/**
	 * @param target 
	 *         the target to set
	 */
	public void setChild(Object child) {
		try{
			this.child = (Lifeline)child;
		}catch(ClassCastException e){
			
		}
	}
	/**
	 * @param operator 
	 *         the operator to set
	 */
	public void setOperator(Object operator) {
		this.operator = (Operator)operator;
	}
	/**
	 * @param connection 
	 *         the connection to set
	 */
	public void setConnection(Object connection) {
		this.connection = (LineConnection)connection;
	}
	
	@Override
	public boolean canExecute() {
		for (int i = 0; i < source.getSourceConnections().size(); i++) {
			Lifeline target = ((LineConnection) source.getSourceConnections().get(i))
					.getTarget();
			if (target.equals(child))
				return false;
		}
		return true;
	}

	@Override
	public void execute() {
		operator.addChild(child);
		connection = new LineConnection(source, child);
	}

	@Override
	public void redo() {
		source.addSourceConnection(connection);
		child.addTargetConnection(connection);
		operator.addChild(child);
	}
	
	@Override
	public void undo() {
		source.removeSourceConnection(connection);
		child.removeTargetConnection(connection);
		operator.removeChild(child);
	}
}
