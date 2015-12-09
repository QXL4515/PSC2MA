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
 * File: PresentConstraint.java                                                   
 * Program: PresentConstraint                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-27                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.model;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class PresentConstraint extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LineConnection lineConnection;

	/**
	 * @return the lineConnection
	 */
	public LineConnection getLineConnection() {
		return lineConnection;
	}

	/**
	 * @param lineConnection the lineConnection to set
	 */
	public void setLineConnection(LineConnection lineConnection) {
		this.lineConnection = lineConnection;
	}

}
