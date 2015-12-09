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
 * File: OperatorEditPolicy.java                                                   
 * Program: OperatorEditPolicy                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-29                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.policy;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import cn.cstv.wspscm.commands.ChangeOperatorConstraintCommand;
import cn.cstv.wspscm.commands.CreateOperatorCommand;
import cn.cstv.wspscm.model.Diagram;
import cn.cstv.wspscm.model.Lifeline;
import cn.cstv.wspscm.model.Operator;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 * 
 */
public class OperatorEditPolicy extends XYLayoutEditPolicy {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#
	 * createChangeConstraintCommand(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	@Override
	protected Command createChangeConstraintCommand(EditPart child,
			Object constraint) {
		// TODO Auto-generated method stub
		if (child.getModel() instanceof Operator) {
			ChangeOperatorConstraintCommand command = new ChangeOperatorConstraintCommand();
			command.setOperator(child.getModel());
			command.setRectangle((Rectangle) constraint);
			return command;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.LayoutEditPolicy#getCreateCommand(org.eclipse
	 * .gef.requests.CreateRequest)
	 */
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		// TODO Auto-generated method stub
		CreateOperatorCommand command = null;
		if (request.getNewObject() instanceof Operator) {
			try{
				command = new CreateOperatorCommand((Diagram)getHost().getModel(),"TypeOfOperator",getHost().getViewer().getControl().getShell());
				Rectangle rectangle = (Rectangle) getConstraintFor(request);
				Operator operator = (Operator) request.getNewObject();
				command.setBounds(rectangle);
				command.setOperator(operator);
				command.setParent((Lifeline)getHost().getModel());
				return command;
			}catch(Exception e){
				//do nothing
			}
		}
		return null;
	}

}
