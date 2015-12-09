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
 * Package: cn.cstv.wspscm.commands                                            
 * File: CreateLifelineCommand.java                                                   
 * Program: CreateLifelineCommand                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-22                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.commands;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import cn.cstv.wspscm.ConstantsSet;
import cn.cstv.wspscm.model.Diagram;
import cn.cstv.wspscm.model.Lifeline;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class CreateLifelineCommand extends Command {

	private Diagram parent;
	private Lifeline child;
	private Rectangle bounds;
	
	private String name;
	private Shell shell;
	
	public CreateLifelineCommand(Shell shell){
		super();
		this.shell=shell;
	}
//	
//	public boolean canExecute() {
//		return child != null && parent != null && bounds != null;
//	}
	@Override
	public void execute() {
		Point p = bounds.getLocation();
		p.setLocation(p.x,ConstantsSet.LifelineOffset);
		child.setLocation(p);
		Dimension size = bounds.getSize();
		child.setSize(size);
		redo();
	}
	
	public void redo(){
	    InputDialog dlg = new InputDialog(shell, "Set the name of Lifeline", "New Lifeline's name:", "Lifeline", null);
	    if (Window.OK == dlg.open()) {
	        name = dlg.getValue();
	    }
	    child.setName(name);
		parent.addChild(child);
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Object parent) {
		this.parent = (Diagram)parent;
	}
	/**
	 * @param child the child to set
	 */
	public void setChild(Object child) {
		this.child = (Lifeline)child;
	}	

	/**
	 * @param bounds the bounds to set
	 */
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	@Override
	public void undo() {
		parent.removeChild(child);
	}

}
