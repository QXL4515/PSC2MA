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
 * File: LineConnectionContainerEditPolicy.java                                                   
 * Program: LineConnectionContainerEditPolicy                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-30                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class LineConnectionContainerEditPolicy extends ContainerEditPolicy {

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.ContainerEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
	 */
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
