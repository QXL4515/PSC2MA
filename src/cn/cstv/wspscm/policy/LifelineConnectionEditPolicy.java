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
 * File: LifelineConnectionEditPolicy.java                                                   
 * Program: LifelineConnectionEditPolicy                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-22                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.policy;

import org.eclipse.draw2d.Connection;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import cn.cstv.wspscm.ConstantsSet;
import cn.cstv.wspscm.commands.CreateEnvironmentLineConnectionCommand;
import cn.cstv.wspscm.commands.CreateLineConnectionCommand;
import cn.cstv.wspscm.commands.ReconnectSourceCommand;
import cn.cstv.wspscm.commands.ReconnectTargetCommand;
import cn.cstv.wspscm.model.EnvironmentLineConnection;
import cn.cstv.wspscm.model.Lifeline;
import cn.cstv.wspscm.model.LineConnection;


/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class LifelineConnectionEditPolicy extends GraphicalNodeEditPolicy {

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCompleteCommand(org.eclipse.gef.requests.CreateConnectionRequest)
	 */
	@Override
	protected Command getConnectionCompleteCommand(
			CreateConnectionRequest request) {
		// TODO Auto-generated method stub
		CreateLineConnectionCommand command = (CreateLineConnectionCommand)request.getStartCommand();
		command.setTarget(getHost().getModel());
		int yOffset = request.getLocation().y-ConstantsSet.LifelineOffset;
		((LineConnection)request.getNewObject()).setSourceOffset(yOffset);
		((LineConnection)request.getNewObject()).setTargetOffset(yOffset);
		return command;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCreateCommand(org.eclipse.gef.requests.CreateConnectionRequest)
	 */
	
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		CreateLineConnectionCommand command = new CreateLineConnectionCommand();
		command.setConnection(request.getNewObject());
		command.setSource(getHost().getModel());
		request.setStartCommand(command);
		return command;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectSourceCommand(org.eclipse.gef.requests.ReconnectRequest)
	 */
	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		// TODO Auto-generated method stub
		ReconnectSourceCommand command = new ReconnectSourceCommand();
		command.setEdge((LineConnection) request.getConnectionEditPart().getModel());
		command.setSource((Lifeline) getHost().getModel());
		return command;
	}

	@Override
	protected Connection createDummyConnection(Request req) {
		// TODO Auto-generated method stub
		return super.createDummyConnection(req);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectTargetCommand(org.eclipse.gef.requests.ReconnectRequest)
	 */
	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		// TODO Auto-generated method stub
		ReconnectTargetCommand command = new ReconnectTargetCommand();
		command.setEdge((LineConnection) request.getConnectionEditPart().getModel());
		command.setTarget((Lifeline) getHost().getModel());
		return command;
	}

	

}
