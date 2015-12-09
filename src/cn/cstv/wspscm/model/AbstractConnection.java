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
 * File: AbstractConnection.java                                                   
 * Program: AbstractConnection                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-22                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.model;


/**
 * @author Su Zhiyong & Zhang Pengcheng
 * 
 */
public abstract class AbstractConnection extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Lifeline source, target;
	
	private int sourceOffset=60; 
	private int targetOffset=60; 

	private boolean isConnected;

	public AbstractConnection(){
	}
	
	public AbstractConnection(Lifeline source,Lifeline target){
		this.source=source;
		this.target=target;
		source.addSourceConnection(this);
		target.addTargetConnection(this);
		source.addSourceOfConnection(this);
		target.addTargetConnection(this);
	}

	/**
	 * @return the sourceOffset
	 */
	public int getSourceOffset() {
		return sourceOffset;
	}

	/**
	 * @param sourceOffset 
	 *         the sourceOffset to set
	 */
	public void setSourceOffset(int sourceOffset) {
		this.sourceOffset = sourceOffset;
	}

	/**
	 * @return the targetOffset
	 */
	public int getTargetOffset() {
		return targetOffset;
	}

	/**
	 * @param targetOffset 
	 *         the targetOffset to set
	 */
	public void setTargetOffset(int targetOffset) {
		this.targetOffset = targetOffset;
	}
	
	/**
	 * @return the source
	 */
	public Lifeline getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(Object source) {
		this.source = (Lifeline) source;
	}

	/**
	 * @return the target
	 */
	public Lifeline getTarget() {
		return target;
	}

	/**
	 * @param target
	 *            the target to set
	 */
	public void setTarget(Object target) {
		this.target = (Lifeline) target;
	}

	public void attachSource() {
//		if (!source.getSources().contains(this)) {
			source.addSourceConnection(this);
//		}
	}

	public void attachTarget() {
//		if (!target.getTargets().contains(this)) {
			target.addTargetConnection(this);
//		}
	}

	public void detachSource() {
		source.removeSourceConnection(this);
	}

	public void detachTarget() {
		target.removeTargetConnection(this);
	}
	
	public void disconnect() {
		if (isConnected) {
			source.removeSourceConnection(this);
			target.removeTargetConnection(this);
			isConnected = false;
		}
	}
	
	public void reconnect() {
		if (!isConnected) {
			source.addSourceConnection(this);
			target.addSourceConnection(this);
			isConnected = true;
		}
	}
	
	public void reconnect(Lifeline newSource, Lifeline newTarget) {
		if (newSource == null || newTarget == null || newSource == newTarget) {
			throw new IllegalArgumentException();
		}
		disconnect();
		this.source = newSource;
		this.target = newTarget;
		reconnect();
	}

}
