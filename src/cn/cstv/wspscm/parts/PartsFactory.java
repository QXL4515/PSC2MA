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
 * File: PartsFactory.java                                                   
 * Program: PartsFactory                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-21                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import cn.cstv.wspscm.model.Diagram;
import cn.cstv.wspscm.model.EnvironmentLifeline;
import cn.cstv.wspscm.model.EnvironmentLineConnection;
import cn.cstv.wspscm.model.Lifeline;
import cn.cstv.wspscm.model.LineConnection;
import cn.cstv.wspscm.model.Operator;
import cn.cstv.wspscm.model.PresentConstraint;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 * 
 */
public class PartsFactory implements EditPartFactory {						//工厂类将在创建模型的时候被调用

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart,
	 * java.lang.Object)
	 */
	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		// TODO Auto-generated method stub
		EditPart editPart = null;
																			//根据不同的model分别启动各自的EditPart也就是控制器，这样model和EditPart就联系起来了
		if (model instanceof EnvironmentLifeline) {
			editPart = new EnvironmentLifelineEditPart();
		} else if (model instanceof Diagram) {
			editPart = new DiagramEditPart();
		} else if (model instanceof EnvironmentLineConnection) {
			editPart = new EnvironmentLineConnectionEditPart();
		}else if (model instanceof LineConnection) {
			editPart = new LineConnectionEditPart();
		} else if (model instanceof Operator) {
			editPart = new OperatorEditPart();
		}else if (model instanceof PresentConstraint){
			editPart = new PresentConstraintEditPart();
		}else if (model instanceof Lifeline){
			editPart = new LifelineEditPart();
		}
		
		if (editPart != null) {
			editPart.setModel(model);						//模型设定，建立起model与editpart之间的关系
		}
		return editPart;
	}

}
