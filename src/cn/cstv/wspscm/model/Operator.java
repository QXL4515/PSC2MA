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
 * File: Operator.java                                                   
 * Program: Operator                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-26                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.model;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
@SuppressWarnings("unchecked")
public class Operator extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String type;
	
	private Integer num = new Integer(2);//ԭ����3����֪��������ʲô��ͼ����ʱ�ĳ�2
	
	private Lifeline initLifeline, endLifeline;
	
	private List<Lifeline> lifelines = new ArrayList<Lifeline>();
	
//	private List<EnvironmentLifeline> environmentLifelines = new ArrayList<EnvironmentLifeline>();
	
	private List<LineConnection> connections = new ArrayList<LineConnection>();

	/** Location of this Lifeline. */
	private Point location = new Point(0, 0);
	/** Size of this Lifeline. */
	private Dimension size = new Dimension(90, 30);
	

	public static final String P_LOCATION = "_location";
	public static final String P_SIZE = "_size";
	public static final String P_CONNECTIONS = "_connections";
	public static final String P_CHILDREN = "_children";
	
	private List children = new ArrayList();
	private Lifeline source;
	
	
	
	
	private boolean isConnected;
	
	
	public Operator(){
		
	}
	
	public Operator(Lifeline lifeline){
		this.source = lifeline;
		source.addOperator(this);
		source.addSourceOfOperator(this);
	}
	
	
	
	public void attachSource() {
//		if (!source.getSources().contains(this)) {
			source.addOperator(this);
//		}
	}
	
	


	public void detachSource() {
		source.removeOperator(this);
	}
	
	
	
	public void disconnect() {
		if (isConnected) {
			source.removeOperator(this);
			isConnected = false;
		}
	}
	
	
	
	public void reconnect() {
		if (!isConnected) {
			source.addOperator(this);
			isConnected = true;
		}
	}
	
	
	
	public void reconnect(Lifeline newSource) {
		if (newSource == null) {
			throw new IllegalArgumentException();
		}
		disconnect();
		this.source = newSource;
		reconnect();
	}
	
	

	/**
	 * @return the children
	 */
	public List getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void addChild(Object child) {
		if(child != null){
			
				children.add(child);
				fireStructureChange(P_CHILDREN, child);
			
			
		}
	}

	public void removeChild(Object child) {
		children.remove(child);
		fireStructureChange(P_CHILDREN, child);
	}

	/**
	 * @return the location
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * @param location 
	 *         the location to set
	 */
	public void setLocation(Point newLocation) {
		//this.location = location;
		//firePropertyChange(P_LOCATION, null, this.location);
		if (newLocation == null) {
			throw new IllegalArgumentException();
		}
		this.location = newLocation;
		firePropertyChange(P_LOCATION, null, this.location);
	}

	/**
	 * @return the size
	 */
	public Dimension getSize() {
		return size;
	}

	/**
	 * @param size 
	 *         the size to set
	 */
	public void setSize(Dimension size) {
		this.size = size;
		firePropertyChange(P_SIZE, null, size);
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type 
	 *         the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the num
	 */
	public Integer getNum() {
		return num;
	}

	/**
	 * @param num 
	 *         the num to set
	 */
	public void setNum(int num) {
		this.num = num;
	}

	/**
	 * @return the initLifeline
	 */
	public Lifeline getInitLifeline() {
		return initLifeline;
	}

	/**
	 * @param initLifeline 
	 *         the initLifeline to set
	 */
	public void setInitLifeline(Lifeline initLifeline) {
		this.initLifeline = initLifeline;
	}

	/**
	 * @return the lifelines
	 */
	public List<Lifeline> getLifelines() {
		return lifelines;
	}

	/**
	 * @param lifelines 
	 *         the lifelines to set
	 */
	public void addLifelines(Lifeline lifeline) {
		this.lifelines.add(lifeline);
	}
	
	/**
	 * 
	 * @param lifeline
	 */
	public void removeLifelines(Lifeline lifeline) {
		this.lifelines.remove(lifeline);
	}

	/**
	 * @return the connections
	 */
	public List<LineConnection> getConnections() {
		return connections;
	}

	/**
	 * @param connections 
	 *         the connections to set
	 */
	public void addConnections(LineConnection connection) {
		this.connections.add(connection);
		fireStructureChange(P_CONNECTIONS, connection);
	}

	/**
	 * @param connections 
	 *         the connections to set
	 */
	public void removeConnections(LineConnection connection) {
		this.connections.remove(connection);
		fireStructureChange(P_CONNECTIONS, connection);
	}

	/**
	 * @param endLifeline 
	 *         the endLifeline to set
	 */
	public void setEndLifeline(Lifeline endLifeline) {
		this.endLifeline = endLifeline;
	}

	/**
	 * @return the endLifeline
	 */
	public Lifeline getEndLifeline() {
		return endLifeline;
	}

}