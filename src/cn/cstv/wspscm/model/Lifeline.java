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
 * Package: cn.cstv.wspscm.model                                            
 * File: Liftline.java                                                   
 * Program: Liftline                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-21                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 * 
 */
@SuppressWarnings("unchecked")
public class Lifeline extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name = new String();

	/** Location of this Lifeline. */
	protected Point location = new Point(0, 0);
	/** Size of this Lifeline. */
	protected Dimension size = new Dimension(90, 30);

	public static final String P_NAME = "_name";

	public static final String P_SOURCE = "_source";
	public static final String P_TARGET = "_target";

	public static final String P_LOCATION = "_location";
	public static final String P_SIZE = "_size";

	private List sourceConnections = new ArrayList();

	private List targetConnections = new ArrayList();
	
	private List<AbstractConnection> sourceOfConnection = new ArrayList<AbstractConnection>();

	private List<AbstractConnection> targetOfConnection = new ArrayList<AbstractConnection>();

	/////////////////////////////////Operator的操作////////////////////////////////////////////////
	
	private List operators = new ArrayList();
	
	private List<Operator> sourceOfOperator = new ArrayList<Operator>();
	
	public static final String P_OPERATOR = "_operator";


	public void setOperators(List operators) {
		this.operators = operators;
	}

	public List getOperators() {
		return operators;
	}

	public void setSourceOfOperator(List<Operator> sourceOfOperator) {
		this.sourceOfOperator = sourceOfOperator;
	}

	public List<Operator> getSourceOfOperator() {
		return sourceOfOperator;
	}
	
	public void addSourceOfOperator(Operator operator) {
		this.sourceOfOperator.add(operator);
	}
	
	public void addOperator(Object operator) {
		// TODO Auto-generated method stub
		operators.add(operator);
		fireStructureChange(P_OPERATOR, operator);
	}

	public void removeOperator(Object operator) {
		// TODO Auto-generated method stub
		operators.remove(operator);
		fireStructureChange(P_OPERATOR, operator);
	}

	/////////////////////////////////Operator的操作-END////////////////////////////////////////////////
	
	/**
	 * @return the sourceOfConnection
	 */
	public List<AbstractConnection> getSourceOfConnection() {
		return sourceOfConnection;
	}

	/**
	 * @param sourceOfConnection the sourceOfConnection to set
	 */
	public void setSourceOfConnection(List<AbstractConnection> sourceOfConnection) {
		this.sourceOfConnection = sourceOfConnection;
	}
	
	/**
	 * @param sourceOfConnection the sourceOfConnection to set
	 */
	public void addSourceOfConnection(AbstractConnection sourceOfConnection) {
		this.sourceOfConnection.add(sourceOfConnection);
	}

	/**
	 * @return the targetOfConnection
	 */
	public List<AbstractConnection> getTargetOfConnection() {
		return targetOfConnection;
	}

	/**
	 * @param targetOfConnection the targetOfConnection to set
	 */
	public void setTargetOfConnection(List<AbstractConnection> targetOfConnection) {
		this.targetOfConnection = targetOfConnection;
	}
	
	public void addTargetOfConnection(AbstractConnection targetOfConnection) {
		this.targetOfConnection.add(targetOfConnection);
	}

	public Lifeline() {
	}

	public Lifeline(String name) {
		this.name = name;
	}
	
	/**
	 * @return the rectangle
	 */
	public void addSourceConnection(Object source) {
		// TODO Auto-generated method stub
		sourceConnections.add(source);
		fireStructureChange(P_SOURCE, source);
	}

	public void addTargetConnection(Object target) {
		targetConnections.add(target);
		fireStructureChange(P_TARGET, target);
	}

	
	public void removeSourceConnection(Object source) {
		// TODO Auto-generated method stub
		sourceConnections.remove(source);
		fireStructureChange(P_SOURCE, source);
	}

	public void removeTargetConnection(Object target) {
		targetConnections.remove(target);
		fireStructureChange(P_TARGET, target);
	}



	/**
	 * @return the sources
	 */
	public List getSourceConnections() {
		return sourceConnections;
	}

	/**
	 * @return the targets
	 */
	public List getTargetConnections() {
		return targetConnections;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
		firePropertyChange(P_NAME, null, this.name);
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] { new TextPropertyDescriptor(
				P_NAME, "name"), };
		return descriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {
		// TODO Auto-generated method stub
		if (id.equals(P_NAME)) {
			return getName();
		}
		return super.getPropertyValue(id);
	}
	
	@Override
	public boolean isPropertySet(Object id) {
		// TODO Auto-generated method stub
		if (id.equals(P_NAME)) {
			return true;
		} else
			return false;
	}
	
	@Override
	public void setPropertyValue(Object id, Object value) {
		// TODO Auto-generated method stub
		if (id.equals(P_NAME)) {
			setName((String) value);
		}
	}

	/**
	 * @param newLocation the location to set
	 */
	public void setLocation(Point newLocation) {
		if (newLocation == null) {
			throw new IllegalArgumentException();
		}
		this.location.setLocation(newLocation);
		firePropertyChange(P_LOCATION, null, this.location);

	}

	/**
	 * @return the location
	 */
	public Point getLocation() {
		return location.getCopy();
	}

	/**
	 * @param newSize the size to set
	 */
	public void setSize(Dimension newSize) {
		if (newSize != null) {
			size.setSize(newSize);
			firePropertyChange(P_SIZE, null, size);
		}
	}

	/**
	 * @return the size
	 */
	public Dimension getSize() {
		return size.getCopy();
	}

}
