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
 * File: DiagramEditPart.java                                                   
 * Program: DiagramEditPart                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-21                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.gef.EditPolicy;

import cn.cstv.wspscm.model.Diagram;
import cn.cstv.wspscm.policy.DiagramXYLayoutEditPolicy;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class DiagramEditPart extends EditPartWithListener {

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		Layer l = new Layer();
		l.setLayoutManager(new XYLayout());
		return l;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		installEditPolicy(EditPolicy.LAYOUT_ROLE,new DiagramXYLayoutEditPolicy());
			}
	
	@SuppressWarnings("unchecked")
	@Override
	protected List getModelChildren(){
		return ((Diagram)getModel()).getChildren();
	}
	
	public void propertyChange(PropertyChangeEvent event){
		if(event.getPropertyName().equals(Diagram.P_CHILDREN)){
			refreshChildren();
		}
	}

}
