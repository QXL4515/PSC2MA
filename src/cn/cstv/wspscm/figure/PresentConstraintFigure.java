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
 * Package: cn.cstv.wspscm.figure                                            
 * File: PresentConstraintFigure.java                                                   
 * Program: PresentConstraintFigure                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-28                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.figure;

import org.eclipse.draw2d.Label;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class PresentConstraintFigure extends Label {

	public PresentConstraintFigure(String s){
		int size = s.length();
		String left = new String("");
		for(int i = 0; i<(size/2-1); i++){
			left+=" ";
		}
		setText(left+"¨w\n"+s);
	}
}
