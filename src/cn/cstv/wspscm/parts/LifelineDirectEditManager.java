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
 * File: LifelineDirectEditManager.java                                                   
 * Program: LifelineDirectEditManager                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-22                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.parts;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.widgets.Text;

import cn.cstv.wspscm.model.Lifeline;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class LifelineDirectEditManager extends DirectEditManager {

	private Lifeline lifeline;
	/**
	 * @param source
	 * @param editorType
	 * @param locator
	 */
	@SuppressWarnings("unchecked")
	public LifelineDirectEditManager(GraphicalEditPart source,
			Class editorType, CellEditorLocator locator) {
		super(source, editorType, locator);
		lifeline = (Lifeline)source.getModel();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.DirectEditManager#initCellEditor()
	 */
	@Override
	protected void initCellEditor() {
		// TODO Auto-generated method stub
		getCellEditor().setValue(lifeline.getName());
		
		Text text = (Text)getCellEditor().getControl();
		text.selectAll();
	}

}
