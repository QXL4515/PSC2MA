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
 * Package: cn.cstv.wspscm.parts                                            
 * File: ConnectionEditPartWithListener.java                                                   
 * Program: ConnectionEditPartWithListener                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-28                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.AbstractConnectionEditPart;

import cn.cstv.wspscm.model.AbstractModel;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class ConnectionEditPartWithListener extends AbstractConnectionEditPart
		implements PropertyChangeListener {

	@Override
	public void activate() {
		super.activate();
		((AbstractModel)getModel()).addPropertyChangeListener(this);
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		((AbstractModel)getModel()).removePropertyChangeListener(this);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}


}
