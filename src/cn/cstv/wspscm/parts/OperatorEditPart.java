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
 * File: OperatorEditPart.java                                                   
 * Program: OperatorEditPart                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-26                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;

import cn.cstv.wspscm.figure.OperatorFigure;
import cn.cstv.wspscm.model.Diagram;
import cn.cstv.wspscm.model.Lifeline;
import cn.cstv.wspscm.model.Operator;
import cn.cstv.wspscm.policy.DiagramComponentEditPolicy;
import cn.cstv.wspscm.policy.LifelineDirectEditPolicy;
import cn.cstv.wspscm.policy.OperatorEditPolicy;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class OperatorEditPart extends EditPartWithListener {

	private OperatorFigure figure;
	
	private Lifeline source;
	
	public Lifeline getSource() {
		return source;
	}

	public void setSource(Lifeline source) {
		this.source = source;
	}

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		Operator operator = (Operator)getModel();
		figure = new OperatorFigure(operator);
		return figure;
	}
	
	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new DiagramComponentEditPolicy());
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new LifelineDirectEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,new OperatorEditPolicy());
	}

	@Override
	protected void refreshVisuals() {

		Dimension dimension = ((Operator) getModel()).getSize();
		if (dimension.height<100&&dimension.width < 100) {
			dimension = new Dimension(150, 150);
		}
		Rectangle rectangle = new Rectangle(((Operator) getModel())
				.getLocation(), dimension);
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), rectangle);

	}	

	public void propertyChange(PropertyChangeEvent event) {
		
		if (event.getPropertyName().equals(Operator.P_LOCATION)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(Operator.P_SIZE)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(Operator.P_CONNECTIONS)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(Operator.P_CHILDREN)) {
			refreshVisuals();
		} else if (event.getPropertyName().equals(Diagram.P_CHILDREN)) {
			refreshVisuals();
		}
		
	}
}
