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
 * File: PresentConstraintCommand.java                                                   
 * Program: PresentConstraintCommand                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-28                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import cn.cstv.wspscm.model.LineConnection;
import cn.cstv.wspscm.wizards.CreateConstraintOfLineConnectionWizard;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 * 
 */
public class SetConstraintOfLineConnectionCommand extends Command {

	private LineConnection lineConnection;
	private String pastOrFuture="";

	private String value = "";
	private String constraint = "";
	private String reset = "";
	private String oldValue = "";
	private String oldConstraint = "";
	private String oldReset = "";

	private Shell shell;
	// private Combo combo;
	// private Text resetText;
	// private Text constraintText;
	// private Text valueText;

	private String typeOfConstraint;

	public SetConstraintOfLineConnectionCommand(LineConnection lineConnection,
			String typeOfConstraint, Shell shell) {
		super();
		this.lineConnection = lineConnection;
		this.typeOfConstraint = typeOfConstraint;
		this.shell = shell;
		// this.value = PresentConstraintValue;
		// this.constraint = PresentConstraintConstraint;
		// this.reset = PresentConstraintReset;
	}

	public boolean canExecute() {
		if (lineConnection == null) {
			return false;
		}
		// if(lineConnection.getPresentConstraint().equals("true"))
		// return false;
		return true;
	}

	public void execute() {
//	    InputDialog dlg = new InputDialog(shell, "Gef Practice", "New node's name:", "Node", null);
//	    if (Window.OK == dlg.open()) {
//	        value = dlg.getValue();
//	    }
		if(typeOfConstraint.equals("Strict")){
			lineConnection.setIsStrict_int(1);
		}else{
			CreateConstraintOfLineConnectionWizard wizard = new CreateConstraintOfLineConnectionWizard(
					typeOfConstraint);
			WizardDialog dialog = new WizardDialog(shell, wizard);
			dialog.create();
			dialog.getShell().setSize(280, 325);

			dialog.setTitle("Set Constraint Wizard");
			dialog.setMessage("Set "+typeOfConstraint);
			if (dialog.open() == WizardDialog.OK) {
				pastOrFuture=wizard.getPastOrFuture();
				value = wizard.getValue();
				constraint = wizard.getConstraint();
				reset = wizard.getReset();
			}
			
			if(typeOfConstraint.equals("PresentConstraint")){
				oldValue = lineConnection.getPresentConstraintValue();
				oldConstraint = lineConnection.getPresentConstraintConstraint();
				oldReset = lineConnection.getPresentConstraintReset();
				lineConnection.setPresentConstraintValue(value);
				lineConnection.setPresentConstraintConstraint(constraint);
				lineConnection.setPresentConstraintReset(reset);
			}else if(typeOfConstraint.equals("BooleanConstraint")){
				if(pastOrFuture.equals("Past")){
					oldValue = lineConnection.getPastUnwantedMessageConstraintValue();
					oldConstraint = lineConnection.getPastUnwantedMessageConstraintConstraint();
					oldReset = lineConnection.getPastUnwantedMessageConstraintReset();
					lineConnection.setPastUnwantedMessageConstraintValue(value);
					lineConnection.setPastUnwantedMessageConstraintConstraint(constraint);
					lineConnection.setPastUnwantedMessageConstraintReset(reset);
				}else if(pastOrFuture.equals("Future")){
					oldValue = lineConnection.getFutureUnwantedMessageConstraintValue();
					oldConstraint = lineConnection.getFutureUnwantedMessageConstraintConstraint();
					oldReset = lineConnection.getFutureUnwantedMessageConstraintReset();
					lineConnection.setFutureUnwantedMessageConstraintValue(value);
					lineConnection.setFutureUnwantedMessageConstraintConstraint(constraint);
					lineConnection.setFutureUnwantedMessageConstraintReset(reset);
				}
			}else if(typeOfConstraint.equals("UnwantedMessageConstraint")){
				if(pastOrFuture.equals("Past")){
					oldValue = lineConnection.getPastUnwantedMessageConstraintValue();
					oldConstraint = lineConnection.getPastUnwantedMessageConstraintConstraint();
					oldReset = lineConnection.getPastUnwantedMessageConstraintReset();
					lineConnection.setPastUnwantedMessageConstraintValue(value);
					lineConnection.setPastUnwantedMessageConstraintConstraint(constraint);
					lineConnection.setPastUnwantedMessageConstraintReset(reset);
				}else if(pastOrFuture.equals("Future")){
					oldValue = lineConnection.getFutureUnwantedMessageConstraintValue();
					oldConstraint = lineConnection.getFutureUnwantedMessageConstraintConstraint();
					oldReset = lineConnection.getFutureUnwantedMessageConstraintReset();
					lineConnection.setFutureUnwantedMessageConstraintValue(value);
					lineConnection.setFutureUnwantedMessageConstraintConstraint(constraint);
					lineConnection.setFutureUnwantedMessageConstraintReset(reset);
				}
			}else if(typeOfConstraint.equals("Present")){
				if(pastOrFuture.equals("Past")){
					oldValue = lineConnection.getPresentPastValue();
					oldConstraint = lineConnection.getPresentPastConstraint();
					oldReset = lineConnection.getPresentPastReset();
					lineConnection.setPresentPastValue(value);
					lineConnection.setPresentPastConstraint(constraint);
					lineConnection.setPresentPastReset(reset);
				}else if(pastOrFuture.equals("Future")){
					oldValue = lineConnection.getPresentFutureValue();
					oldConstraint = lineConnection.getPresentFutureConstraint();
					oldReset = lineConnection.getPresentFutureReset();
					lineConnection.setPresentFutureValue(value);
					lineConnection.setPresentFutureConstraint(constraint);
					lineConnection.setPresentFutureReset(reset);
				}
			}else if(typeOfConstraint.equals("WantedChainConstraint")){
				if(pastOrFuture.equals("Past")){
					oldValue = lineConnection.getPastWantedChainConstraintValue();
					oldConstraint = lineConnection.getPastWantedChainConstraintConstraint();
					oldReset = lineConnection.getPastWantedChainConstraintReset();
					lineConnection.setPastWantedChainConstraintValue(value);
					lineConnection.setPastWantedChainConstraintConstraint(constraint);
					lineConnection.setPastWantedChainConstraintReset(reset);
				}else if(pastOrFuture.equals("Future")){
					oldValue = lineConnection.getFutureWantedChainConstraintValue();
					oldConstraint = lineConnection.getFutureWantedChainConstraintConstraint();
					oldReset = lineConnection.getFutureWantedChainConstraintReset();
					lineConnection.setFutureWantedChainConstraintValue(value);
					lineConnection.setFutureWantedChainConstraintConstraint(constraint);
					lineConnection.setFutureWantedChainConstraintReset(reset);
				}
			}else if(typeOfConstraint.equals("UnwantedChainConstraint")){
				if(pastOrFuture.equals("Past")){
					oldValue = lineConnection.getPastUnwantedChainConstraintValue();
					oldConstraint = lineConnection.getPastUnwantedChainConstraintConstraint();
					oldReset = lineConnection.getPastUnwantedChainConstraintReset();
					lineConnection.setPastUnwantedChainConstraintValue(value);
					lineConnection.setPastUnwantedChainConstraintConstraint(constraint);
					lineConnection.setPastUnwantedChainConstraintReset(reset);
				}else if(pastOrFuture.equals("Future")){
					oldValue = lineConnection.getFutureUnwantedChainConstraintValue();
					oldConstraint = lineConnection.getFutureUnwantedChainConstraintConstraint();
					oldReset = lineConnection.getFutureUnwantedChainConstraintReset();
					lineConnection.setFutureUnwantedChainConstraintValue(value);
					lineConnection.setFutureUnwantedChainConstraintConstraint(constraint);
					lineConnection.setFutureUnwantedChainConstraintReset(reset);
				}
			}
			

		}

		
	}
	
	public void undo() {
		if(typeOfConstraint.equals("PresentConstraint")){
			lineConnection.setPresentConstraintValue(oldValue);
			lineConnection.setPresentConstraintConstraint(oldConstraint);
			lineConnection.setPresentConstraintReset(oldReset);
		}else if(typeOfConstraint.equals("UnwantedMessageConstraint")){
			if(pastOrFuture.equals("Past")){
				lineConnection.setPastUnwantedMessageConstraintValue(oldValue);
				lineConnection.setPastUnwantedMessageConstraintConstraint(oldConstraint);
				lineConnection.setPastUnwantedMessageConstraintReset(oldReset);
			}else if(pastOrFuture.equals("Future")){
				lineConnection.setFutureUnwantedMessageConstraintValue(oldValue);
				lineConnection.setFutureUnwantedMessageConstraintConstraint(oldConstraint);
				lineConnection.setFutureUnwantedMessageConstraintReset(oldReset);
			}
		}else if(typeOfConstraint.equals("Present")){
			if(pastOrFuture.equals("Past")){
				lineConnection.setPresentPastValue(oldValue);
				lineConnection.setPresentPastConstraint(oldConstraint);
				lineConnection.setPresentPastReset(oldReset);
			}else if(pastOrFuture.equals("Future")){
				lineConnection.setPresentFutureValue(oldValue);
				lineConnection.setPresentFutureConstraint(oldConstraint);
				lineConnection.setPresentFutureReset(oldReset);
			}
		}else if(typeOfConstraint.equals("WantedChainConstraint")){
			if(pastOrFuture.equals("Past")){
				lineConnection.setPastWantedChainConstraintValue(oldValue);
				lineConnection.setPastWantedChainConstraintConstraint(oldConstraint);
				lineConnection.setPastWantedChainConstraintReset(oldReset);
			}else if(pastOrFuture.equals("Future")){
				lineConnection.setFutureWantedChainConstraintValue(oldValue);
				lineConnection.setFutureWantedChainConstraintConstraint(oldConstraint);
				lineConnection.setFutureWantedChainConstraintReset(oldReset);
			}
		}else if(typeOfConstraint.equals("UnwantedChainConstraint")){
			if(pastOrFuture.equals("Past")){
				lineConnection.setPastUnwantedChainConstraintValue(oldValue);
				lineConnection.setPastUnwantedChainConstraintConstraint(oldConstraint);
				lineConnection.setPastUnwantedChainConstraintReset(oldReset);
			}else if(pastOrFuture.equals("Future")){
				lineConnection.setFutureUnwantedChainConstraintValue(oldValue);
				lineConnection.setFutureUnwantedChainConstraintConstraint(oldConstraint);
				lineConnection.setFutureUnwantedChainConstraintReset(oldReset);
			}
		}

	}

	public void setTypeOfConstraint(String typeOfConstraint) {
		this.typeOfConstraint = typeOfConstraint;
	}

	public String getTypeOfConstraint() {
		return typeOfConstraint;
	}


}
