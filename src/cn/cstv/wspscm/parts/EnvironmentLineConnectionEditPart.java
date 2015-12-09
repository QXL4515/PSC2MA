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
 * File: LineConnectionEditPart.java                                                   
 * Program: LineConnectionEditPart                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-22                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPolicy;

import cn.cstv.wspscm.figure.BooleanConstraintFigure;
import cn.cstv.wspscm.figure.LifelineConnectionAnchor;


import cn.cstv.wspscm.figure.MessageFigure;
import cn.cstv.wspscm.figure.MidpointOffsetLocator;
import cn.cstv.wspscm.figure.PresentConstraintFigure;
import cn.cstv.wspscm.figure.PresentFigure;
import cn.cstv.wspscm.figure.SourcePointOffsetLocator;
import cn.cstv.wspscm.figure.TargetPointOffsetLocator;
import cn.cstv.wspscm.figure.UnwantedChainConstraintFigure;
import cn.cstv.wspscm.figure.WantedChainConstraintFigure;


import cn.cstv.wspscm.model.EnvironmentLineConnection;
import cn.cstv.wspscm.model.LineConnection;
import cn.cstv.wspscm.policy.AttributeEditPolicy;
import cn.cstv.wspscm.policy.EnvironmentLineConnectionEditPolicy;
import cn.cstv.wspscm.policy.LineConnectionEditPolicy;
import cn.cstv.wspscm.policy.LineConnectionEndPointEditPolicy;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 * 
 */
public class EnvironmentLineConnectionEditPart extends LineConnectionEditPart {
	protected IFigure createFigure() {
		PolylineConnection connection = new PolylineConnection();
		connection.setTargetDecoration(new PolygonDecoration());
		connection.setLineStyle( org.eclipse.swt.SWT.LINE_DASH);
		return connection;
	}

}
