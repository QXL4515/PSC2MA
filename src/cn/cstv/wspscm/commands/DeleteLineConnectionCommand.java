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
 * File: DeleteLineConnectionCommand.java                                                   
 * Program: DeleteLineConnectionCommand                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-22                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.commands;

import org.eclipse.gef.commands.Command;

import cn.cstv.wspscm.model.AbstractConnection;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class DeleteLineConnectionCommand extends Command {

	private AbstractConnection connection;
	
	public void execute(){
		connection.detachSource();
		connection.detachTarget();
	}

	/**
	 * @param connection the connection to set
	 */
	public void setConnection(Object connection) {
		this.connection = (AbstractConnection)connection;
	}
	
	public void undo(){
		connection.attachSource();
		connection.attachTarget();
	}
}
