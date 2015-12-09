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
 * Package: cn.cstv.wspscm.views                                            
 * File: FileManagerView.java                                                   
 * Program: FileManagerView                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-8-27                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.views;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import cn.cstv.wspscm.actions.ExecuteBPELAction;
import cn.cstv.wspscm.actions.OpenBPELFileAction;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class BPELView extends ViewPart {

	public static final String ID = "cn.cstv.wspscm.views.BPELView";


	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	private Text text;
	private ExecuteBPELAction executeBPELAction;
	private String contentString = "";
	private OpenBPELFileAction openBPELFileAction;
	@Override
	public void createPartControl(Composite parent) {

		System.currentTimeMillis();
		text = new Text(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
//		text.setBackground(ColorConstants.lightBlue);
//		text.setText("(s0,[invoke]alarm(high),(x:=0),s1)\n(s1,![invoke]alarm(high),(x<7),s1)\n(s1,[invoke]alarm(high),(x<7),s2)\n(s1,emtpy,(x>7),s6(Accept))\n(s2,![invoke]alarm(high),(x<7),s2)\n(s2,[invoke]alarm(high),(x<7),s3)\n(s2,emtpy,(x>7),s6(Accept))\n(s3,![invoke]changeDiagosis(diagosis),(x<7),s3)\n(s3,[invoke]changeDiagosis(diagosis),(x<7;y:=0),s4)\n(s3,emtpy,(x>7),s7(Error))\n(s4,![invoke]changeDiagosis(responseDiagnosis),(y<1),s4)\n(s4,[invoke]changeDiagosis(responseDiagnosis),(y<1),s5(Accept))\n(s4,emtpy,(y>1),s7(Error))");
		text.setText(contentString);
		createActions(); 
		initializeToolBar();
		initializeMenu();
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	private void createActions() {
		// TODO Auto-generated method stub
		executeBPELAction = new ExecuteBPELAction(getSite().getWorkbenchWindow());
		openBPELFileAction = new OpenBPELFileAction(getSite().getWorkbenchWindow());
	}
	
	/**
	 * @return the text
	 */
	public Text getText() {
		return text;
	}

	/**
	 * @param text 
	 *         the text to set
	 */
	public void setText(Text text) {
		this.text = text;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	private void initializeToolBar() {
		IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
		toolBarManager.add(openBPELFileAction);
		toolBarManager.add(executeBPELAction);
	}
	
    private void initializeMenu() {
		IMenuManager manager =
           getViewSite().getActionBars().getMenuManager();
        manager.add(openBPELFileAction);
        manager.add(executeBPELAction);
    } 
    

}
