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
 * Package: cn.cstv.wspscm.views                                            
 * File: DiagramComponentEditPolicy.java                                                   
 * Program: DiagramComponentEditPolicy                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-22                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import cn.cstv.wspscm.commands.DeleteEnvironmentLifelineCommand;
import cn.cstv.wspscm.commands.DeleteLifelineCommand;
import cn.cstv.wspscm.commands.DeleteOperatorCommand;
import cn.cstv.wspscm.model.EnvironmentLifeline;
import cn.cstv.wspscm.model.Lifeline;
import cn.cstv.wspscm.model.Operator;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 * 
 */
public class DiagramComponentEditPolicy extends ComponentEditPolicy {

	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		if (getHost().getModel() instanceof Lifeline) {
			DeleteLifelineCommand deleteCommand = new DeleteLifelineCommand();
			deleteCommand.setParent(getHost().getParent().getModel());
			deleteCommand.setChild(getHost().getModel());
			return deleteCommand;
		} else if (getHost().getModel() instanceof Operator) {
			DeleteOperatorCommand deleteCommand = new DeleteOperatorCommand();
			deleteCommand.setParent(getHost().getParent().getModel());
			deleteCommand.setOperator(getHost().getModel());
			return deleteCommand;
		} else if (getHost().getModel() instanceof EnvironmentLifeline) {
			DeleteEnvironmentLifelineCommand deleteCommand = new DeleteEnvironmentLifelineCommand();
			deleteCommand.setParent(getHost().getParent().getModel());
			deleteCommand.setChild(getHost().getModel());
			return deleteCommand;
		}
		return null;
	}
}
