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
 * File: DeleteOperatorCommand.java                                                   
 * Program: DeleteOperatorCommand                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-29                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.commands;

import org.eclipse.gef.commands.Command;

import cn.cstv.wspscm.model.Diagram;
import cn.cstv.wspscm.model.Operator;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class DeleteOperatorCommand extends Command {

	private Operator operator;
	private Diagram parent;
	
	public void execute(){
		parent.removeChild(operator);
	}


	/**
	 * @param operator 
	 *         the operator to set
	 */
	public void setOperator(Object operator) {
		this.operator = (Operator)operator;
	}
	

	public void setParent(Object parent) {
		this.parent = (Diagram)parent;
	}


	public void undo(){
		parent.addChild(operator);
	}
}
