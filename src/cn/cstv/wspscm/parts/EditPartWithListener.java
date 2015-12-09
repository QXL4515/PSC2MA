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
 * File: EditPartWithListener.java                                                   
 * Program: EditPartWithListener                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-21                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DirectEditManager;

import cn.cstv.wspscm.model.AbstractModel;
import cn.cstv.wspscm.model.Lifeline;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public abstract class EditPartWithListener extends AbstractGraphicalEditPart implements
		PropertyChangeListener {

	protected DirectEditManager manager = null;
	
	private ConnectionAnchor anchor;
	
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
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		return null;
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
	
	protected void performDirectEdit() {
	}

	@Override
	public void performRequest(Request req) {
		if (req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)) {
			performDirectEdit();
			return;
		}
		super.performRequest(req);
	}

	protected ConnectionAnchor getConnectionAnchor() {
		if (anchor == null) {
			if (getModel() instanceof Lifeline)
				anchor = new EllipseAnchor(getFigure());
			else
				// if Shapes gets extended the conditions above must be updated
				throw new IllegalArgumentException("unexpected model");
		}
		return anchor;
	}
}
