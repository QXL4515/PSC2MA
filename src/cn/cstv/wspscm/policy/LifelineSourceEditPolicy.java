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
 * Package: cn.cstv.wspscm.policy                                            
 * File: LifelineSourceEditPolicy.java                                                   
 * Program: LifelineSourceEditPolicy                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-9-3                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.policy;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

import cn.cstv.wspscm.commands.AddAndAssignSourceCommand;
import cn.cstv.wspscm.commands.CreateAndAssignSourceCommand;


/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class LifelineSourceEditPolicy extends ContainerEditPolicy {

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.ContainerEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
	 */
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		// TODO Auto-generated method stub
		CreateAndAssignSourceCommand cmd = new CreateAndAssignSourceCommand();
		cmd.setOperator(getHost().getParent().getModel());
		cmd.setChild(request.getNewObject());  //可能会有由此引起的bug
		cmd.setSource(getHost().getModel());
		return cmd;
	}
	
	@Override
	protected Command getAddCommand(GroupRequest request) {
		CompoundCommand cmd = new CompoundCommand();
		for (int i = 0; i < request.getEditParts().size(); i++) {
			AddAndAssignSourceCommand add = new AddAndAssignSourceCommand();
			add.setOperator(getHost().getParent().getModel());
			add.setSource(getHost().getModel());
			add.setChild((((EditPart) request.getEditParts().get(i))
							.getModel()));
			cmd.add(add);
		}
		return cmd;
	}
	

	@Override
	public EditPart getTargetEditPart(Request request) {
		if (REQ_CREATE.equals(request.getType()))
			return getHost();
		if (REQ_ADD.equals(request.getType()))
			return getHost();
		if (REQ_MOVE.equals(request.getType()))
			return getHost();
		return super.getTargetEditPart(request);
	}

}
