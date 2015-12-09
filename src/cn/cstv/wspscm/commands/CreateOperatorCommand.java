/*************************************************************************
 * Copyright (c 2006, 2008. All rights reserved. This program and the   
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
 * File: CreateOperatorCommand.java                                                   
 * Program: CreateOperatorCommand                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-26                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;




import cn.cstv.wspscm.model.Diagram;
import cn.cstv.wspscm.model.Lifeline;
import cn.cstv.wspscm.model.LineConnection;
import cn.cstv.wspscm.model.Operator;
import cn.cstv.wspscm.wizards.CreateConstraintOfLineConnectionWizard;
import cn.cstv.wspscm.wizards.CreateOperatorWizard;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 * 
 */
public class CreateOperatorCommand extends Command {

	private Diagram parent;
//	private Lifeline initLifeline, endLifeline;
	private Operator operator;
	private String typeOfOperator;
	private Rectangle bounds;
	private Display display;
	
	private String type = "";
	private String num = "";
	
	private Shell shell;
	
	public CreateOperatorCommand(Diagram diagram,
			String typeOfOperator, Shell shell) {
		super();
		this.parent = diagram;
		this.typeOfOperator = typeOfOperator;
		this.shell = shell;
		// this.value = PresentConstraintValue;
		// this.constraint = PresentConstraintConstraint;
		// this.reset = PresentConstraintReset;
	}

	public boolean canExecute() {
		if(parent==null){
			return false;
		}
		return true;
	}

	@Override
	public void execute() {
		/*
		shell = new Shell(display, SWT.NONE);
		shell.setSize(503, 242);
		shell.setText("Select Type and Number");
		
		final Composite composite = new Composite(shell, SWT.NONE);
		composite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		composite.setBounds(0, 0, 494, 64);

		shell.open();
		shell.layout();
		operator.setLocation(bounds.getLocation());
		operator.setSize(bounds.getSize());
		redo();
		*/
		CreateOperatorWizard wizard = new CreateOperatorWizard(
				"TypeOfOperator");
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		dialog.getShell().setSize(280, 325);

		dialog.setTitle("Set Operator Wizard");
		if (dialog.open() == WizardDialog.OK) {
			type = wizard.getType();
			num = wizard.getNum();
		}
		operator.setType(type);
		if(!num.equals("")){
			operator.setNum(Integer.parseInt(num));
		}
		parent.addChild(operator);
		operator.setLocation(bounds.getLocation());//这一行以及下一行代码解决了，operator创建的point的问题
		operator.setSize(bounds.getSize());
	}

	public void redo() {
		operator.attachSource();
		//parent.addChild(operator);
		//operator.addLifelines(initLifeline);
		//operator.addLifelines(endLifeline);
		//operator.attachInitLifeline();
		//operator.attachEndLifeline();
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(Object parent) {
		this.parent = (Diagram) parent;
	}

	/**
	 * @param initLifeline
	 *            the initLifeline to set
	 */
	// public void setInitLifeline(Object initLifeline) {
	// this.initLifeline = (Lifeline)initLifeline;
	// }

	// /**
	// * @param initLifeline
	// * the initLifeline to set
	// */
	// public void setInitLifeline(Object initLifeline) {
	// this.initLifeline = (Lifeline)initLifeline;
	// }
	//
	// /**
	// * @param endLifeline
	// * the endLifeline to set
	// */
	// public void setEndLifeline(Object endLifeline) {
	// this.endLifeline = (Lifeline)endLifeline;
	// }
	/**
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator(Object operator) {
		this.operator = (Operator) operator;
	}

	/**
	 * @param bounds
	 *            the bounds to set
	 */
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	@Override
	public void undo() {
		operator.detachSource();
		// operator.removeLifelines(initLifeline);
		// operator.removeLifelines(endLifeline);
		// operator.detachEndLifeline();
		// operator.detachInitLifeline();
	}
}
