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
 * File: AbstractModelFactory.java                                                   
 * Program: AbstractModelFactory                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-21                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.model;

import org.eclipse.gef.requests.CreationFactory;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class AbstractModelFactory implements CreationFactory {

	private Object template;

	public AbstractModelFactory(Object o) {
		template = o;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.requests.CreationFactory#getNewObject()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getNewObject() {
		// TODO Auto-generated method stub
		try {
			return ((Class) template).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.requests.CreationFactory#getObjectType()
	 */
	@Override
	public Object getObjectType() {
		// TODO Auto-generated method stub
		return template;
	}

}
