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
 * File: LifelineEditPart.java                                                   
 * Program: LifelineEditPart                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-21                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.jface.viewers.TextCellEditor;

import cn.cstv.wspscm.ConstantsSet;
import cn.cstv.wspscm.figure.LifelineConnectionAnchor;
import cn.cstv.wspscm.figure.LifelineFigure;
import cn.cstv.wspscm.model.Lifeline;
import cn.cstv.wspscm.model.Operator;
import cn.cstv.wspscm.model.LineConnection;
import cn.cstv.wspscm.policy.DiagramComponentEditPolicy;
import cn.cstv.wspscm.policy.DiagramXYLayoutEditPolicy;
import cn.cstv.wspscm.policy.LifelineConnectionEditPolicy;
import cn.cstv.wspscm.policy.LifelineDirectEditPolicy;
import cn.cstv.wspscm.policy.LifelineSourceEditPolicy;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 * 
 */
public class LifelineEditPart extends EditPartWithListener implements
		NodeEditPart {

	private LifelineFigure figure;

	private LifelineConnectionAnchor sourceAnchor;

	private LifelineConnectionAnchor targetAnchor;

	/**
	 * @return the sourceAnchor
	 */
	public LifelineConnectionAnchor getSourceAnchor() {
		return sourceAnchor;
	}

	/**
	 * @param sourceAnchor
	 *            the sourceAnchor to set
	 */
	public void setSourceAnchor(LifelineConnectionAnchor sourceAnchor) {
		this.sourceAnchor = sourceAnchor;
	}

	/**
	 * @return the targetAnchor
	 */
	public LifelineConnectionAnchor getTargetAnchor() {
		return targetAnchor;
	}

	/**
	 * @param targetAnchor
	 *            the targetAnchor to set
	 */
	public void setTargetAnchor(LifelineConnectionAnchor targetAnchor) {
		this.targetAnchor = targetAnchor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {					//确定外观
		// TODO Auto-generated method stub
		Lifeline lifeline = (Lifeline) getModel();		//取得模型
		figure = new LifelineFigure(lifeline);
		return figure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new DiagramComponentEditPolicy());
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new LifelineDirectEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new LifelineConnectionEditPolicy());
		installEditPolicy(EditPolicy.CONTAINER_ROLE,//大多数具有children的editpart应该具有的一个基本role
				new LifelineSourceEditPolicy());
	}

	@Override
	protected void refreshVisuals() {
		Rectangle bounds = figure.getTextBounds();
		Dimension dimension = new Dimension(bounds.width + 20, 30 + 800);
		if (dimension.width < 60) {
			dimension = new Dimension(60, 30 + 800);
		}
		Rectangle rectangle = new Rectangle(((Lifeline) getModel())
				.getLocation(), dimension);
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), rectangle);
	}

	protected void refreshSizeVisuals() {
		Rectangle rectangle = new Rectangle(((Lifeline) getModel())
				.getLocation(), ((Lifeline) getModel()).getSize());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), rectangle);
	}

	public void propertyChange(PropertyChangeEvent event) {
		
		if (event.getPropertyName().equals(Lifeline.P_LOCATION)) {
			refreshSizeVisuals();
		} else if (event.getPropertyName().equals(Lifeline.P_SIZE)) {
			refreshSizeVisuals();
		} else if (event.getPropertyName().equals(Lifeline.P_NAME)) {
			((Label) getFigure()).setText((String) event.getNewValue());
			refreshVisuals();
		} else if (event.getPropertyName().equals(Lifeline.P_SOURCE)) {
			refreshSourceConnections();
		} else if (event.getPropertyName().equals(Lifeline.P_TARGET)) {
			refreshTargetConnections();
		}else if (event.getPropertyName().equals(Lifeline.P_OPERATOR)) {
			refreshOperators();
		}
		
	}

	protected void refreshOperators() {
		// TODO Auto-generated method stub
//		Rectangle rect = figure.getTextBounds();
		
	}

	@Override
	protected void performDirectEdit() {
		// TODO Auto-generated method stub
		if (manager == null) {
			manager = new LifelineDirectEditManager(this, TextCellEditor.class,
					new LifelineCellEditorLocator(getFigure()));
		}
		manager.show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef
	 * .ConnectionEditPart)
	 */
	@Override
	
	
	
	
	
	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection) {
		// TODO Auto-generated method stub
		LineConnectionEditPart con = (LineConnectionEditPart) connection;
		LifelineConnectionAnchor anchor = con.getSourceAnchor();
		if (anchor == null || anchor.getOwner() != getFigure()) {
			anchor = new LifelineConnectionAnchor(getFigure());
			LineConnection conModel = (LineConnection) con.getModel();

			anchor.setOffset(conModel.getSourceOffset());
			con.setSourceAnchor(anchor);
			conModel.setSourceOffset(anchor.getOffset());
			
		}
		return anchor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef
	 * .Request)
	 */
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		// TODO Auto-generated method stub
		if (request instanceof ReconnectRequest) {
			ReconnectRequest r = (ReconnectRequest) request;
			LineConnectionEditPart con = (LineConnectionEditPart) r
					.getConnectionEditPart();
			LineConnection conModel = (LineConnection) con.getModel();
			LifelineConnectionAnchor anchor = con.getSourceAnchor();
			GraphicalEditPart part = (GraphicalEditPart) r.getTarget();
			if (anchor == null || anchor.getOwner() != part.getFigure()) {
				anchor = new LifelineConnectionAnchor(getFigure());
				anchor.setOffset(conModel.getSourceOffset());
				con.setSourceAnchor(anchor);
			}
			Point loc = r.getLocation();
			anchor.setOffset(loc.y - ConstantsSet.LifelineOffset);
			conModel.setSourceOffset(anchor.getOffset());
			con.getTargetAnchor().setOffset(anchor.getOffset());
			return anchor;
		} else {
			CreateConnectionRequest r = (CreateConnectionRequest) request;
			Point loc = r.getLocation();
			LifelineConnectionAnchor anchor = new LifelineConnectionAnchor(
					getFigure());
			anchor.setOffset(loc.y - ConstantsSet.LifelineOffset);
			return anchor;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef
	 * .ConnectionEditPart)
	 */
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connection) {
		// TODO Auto-generated method stub
		LineConnectionEditPart con = (LineConnectionEditPart) connection;
		LifelineConnectionAnchor anchor = con.getTargetAnchor();
		if (anchor == null || anchor.getOwner() != getFigure()) {
			
			anchor = new LifelineConnectionAnchor(getFigure());
			LineConnection conModel = (LineConnection) con.getModel();
			anchor.setOffset(conModel.getTargetOffset());
			con.setTargetAnchor(anchor);
			conModel.setTargetOffset(anchor.getOffset());
		}
		return anchor;
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef
	 * .Request)
	 */
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		// TODO Auto-generated method stub
		if (request instanceof ReconnectRequest) {
			ReconnectRequest r = (ReconnectRequest) request;
			LineConnectionEditPart con = (LineConnectionEditPart) r
					.getConnectionEditPart();
			LineConnection conModel = (LineConnection) con.getModel();
			LifelineConnectionAnchor anchor = con.getTargetAnchor();
			GraphicalEditPart part = (GraphicalEditPart) r.getTarget();
			if (anchor == null || anchor.getOwner() != part.getFigure()) {
				anchor = new LifelineConnectionAnchor(getFigure());
				anchor.setOffset(conModel.getTargetOffset());
				con.setTargetAnchor(anchor);
			}
			Point loc = r.getLocation();
			anchor.setOffset(loc.y - ConstantsSet.LifelineOffset);
			conModel.setTargetOffset(anchor.getOffset());
			con.getSourceAnchor().setOffset(anchor.getOffset());
			return anchor;
		} else {
			// return new LifelineConnectionAnchor(getFigure());
			CreateConnectionRequest r = (CreateConnectionRequest) request;
			Point loc = r.getLocation();
			LifelineConnectionAnchor anchor = new LifelineConnectionAnchor(
					getFigure());
			anchor.setOffset(loc.y - ConstantsSet.LifelineOffset);
			return anchor;
		}
	}
	

	@SuppressWarnings("unchecked")
	public List getModelSourceConnections() {
		return ((Lifeline) getModel()).getSourceConnections();
	}

	@SuppressWarnings("unchecked")
	public List getModelTargetConnections() {
		return ((Lifeline) getModel()).getTargetConnections();
	}
}
