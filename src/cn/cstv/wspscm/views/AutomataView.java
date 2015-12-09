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
 * File: AutomateView.java                                                   
 * Program: AutomateView                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-8-27                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import cn.cstv.wspscm.actions.GenerateCertainAutomataAction;
import cn.cstv.wspscm.actions.OpenAutomataFileAction;
import cn.cstv.wspscm.actions.RefinementAutomataAction;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 * 
 */
public class AutomataView extends ViewPart {

	public static final String ID = "cn.cstv.wspscm.views.AutomataView";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	private List<Text> textArray = new ArrayList<Text>();
	private List<Group> zgraphGroupArray = new ArrayList<Group>();
	private List<TabItem> tabArray = new ArrayList<TabItem>();


	private OpenAutomataFileAction openAutomataFileAction;
	private RefinementAutomataAction refinementAutomataAction;
	private GenerateCertainAutomataAction generateCertainAutomataAction;
	private String contentString = "";
	private TabFolder tabFolder = null;

	public List<TabItem> getTabArray() {
		return tabArray;
	}
	
	@Override
	public void createPartControl(Composite parent) {

		final FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		parent.setLayout(fillLayout);

		tabFolder = new TabFolder(parent, SWT.NULL);

		for (int k = 0; k < 1; k++) {
			tabFolder.dispose();
			tabFolder = new TabFolder(parent, SWT.NULL);
			TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
			tabItem.setText(""+(k+1));
						
			SashForm sashForm = new SashForm(tabFolder, SWT.VERTICAL);
			Group ruleGroup = new Group(sashForm, SWT.NONE);
			ruleGroup.setText("Rule");
			ruleGroup.setLayout(new FillLayout(SWT.VERTICAL));

			Text text = new Text(ruleGroup, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
			// text.setBackground(ColorConstants.lightBlue);
			// text.setText("(s0,[invoke]alarm(high),(x:=0),s1)\n(s1,![invoke]alarm(high),(x<7),s1)\n(s1,[invoke]alarm(high),(x<7),s2)\n(s1,emtpy,(x>7),s6(Accept))\n(s2,![invoke]alarm(high),(x<7),s2)\n(s2,[invoke]alarm(high),(x<7),s3)\n(s2,emtpy,(x>7),s6(Accept))\n(s3,![invoke]changeDiagosis(diagosis),(x<7),s3)\n(s3,[invoke]changeDiagosis(diagosis),(x<7;y:=0),s4)\n(s3,emtpy,(x>7),s7(Error))\n(s4,![invoke]changeDiagosis(responseDiagnosis),(y<1),s4)\n(s4,[invoke]changeDiagosis(responseDiagnosis),(y<1),s5(Accept))\n(s4,emtpy,(y>1),s7(Error))");

			text.setText(contentString);
			
			textArray.add(text);

			Group zgraphGroup;
			zgraphGroup = new Group(sashForm, SWT.NONE);
			zgraphGroup.setText("Graph");
			zgraphGroup.setLayout(new FillLayout());
			zgraphGroupArray.add(zgraphGroup);

			sashForm.setWeights(new int[] { 1, 1 });
	
			tabItem.setControl(sashForm);
			tabArray.add(tabItem);
			
		}
		
		//tabFolder.setVisible(false);



		createActions();
		initializeToolBar();
		initializeMenu();
		// TODO Auto-generated method stub

	}
	
	public void resetAutomataView(int tabItemNumber,List<String> path)
	{
		textArray.clear();
		zgraphGroupArray.clear();
		for( int i = 0; i < tabArray.size();i++)
		{
			tabArray.get(i).dispose();
		}
		tabArray.clear();
		
		for (int k = 0; k < tabItemNumber; k++) {
			TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
//			tabItem.setText(""+(k+1));
			tabItem.setText(path.get(k).substring(path.get(k).indexOf('/')+1,path.get(k).lastIndexOf('.')));
						
			SashForm sashForm = new SashForm(tabFolder, SWT.VERTICAL);
			Group ruleGroup = new Group(sashForm, SWT.NONE);
			ruleGroup.setText("Rule");
			ruleGroup.setLayout(new FillLayout(SWT.VERTICAL));

			Text text = new Text(ruleGroup, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
			// text.setBackground(ColorConstants.lightBlue);
			// text.setText("(s0,[invoke]alarm(high),(x:=0),s1)\n(s1,![invoke]alarm(high),(x<7),s1)\n(s1,[invoke]alarm(high),(x<7),s2)\n(s1,emtpy,(x>7),s6(Accept))\n(s2,![invoke]alarm(high),(x<7),s2)\n(s2,[invoke]alarm(high),(x<7),s3)\n(s2,emtpy,(x>7),s6(Accept))\n(s3,![invoke]changeDiagosis(diagosis),(x<7),s3)\n(s3,[invoke]changeDiagosis(diagosis),(x<7;y:=0),s4)\n(s3,emtpy,(x>7),s7(Error))\n(s4,![invoke]changeDiagosis(responseDiagnosis),(y<1),s4)\n(s4,[invoke]changeDiagosis(responseDiagnosis),(y<1),s5(Accept))\n(s4,emtpy,(y>1),s7(Error))");

			text.setText("");
			
			textArray.add(text);

			Group zgraphGroup;
			zgraphGroup = new Group(sashForm, SWT.NONE);
			zgraphGroup.setText("Graph");
			zgraphGroup.setLayout(new FillLayout());
			zgraphGroupArray.add(zgraphGroup);

			sashForm.setWeights(new int[] { 1, 1 });
	
			tabItem.setControl(sashForm);
			tabArray.add(tabItem);
			
		}
		
	}

	public List<Group> getZgraphGroupArray() {
		return zgraphGroupArray;
	}


	/**
	 * 
	 */
	private void createActions() {
		// TODO Auto-generated method stub
		openAutomataFileAction = new OpenAutomataFileAction(getSite()
				.getWorkbenchWindow());
		refinementAutomataAction = new RefinementAutomataAction(getSite()
				.getWorkbenchWindow());
		generateCertainAutomataAction = new GenerateCertainAutomataAction(
				getSite().getWorkbenchWindow());
	}

	/**
	 * @return the text
	 */
	public List<Text> getTextArray() {
		return textArray;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private void initializeToolBar() {
		IToolBarManager toolBarManager = getViewSite().getActionBars()
				.getToolBarManager();
		toolBarManager.add(refinementAutomataAction);
		toolBarManager.add(openAutomataFileAction);
		toolBarManager.add(generateCertainAutomataAction);
	}

	private void initializeMenu() {
		IMenuManager manager = getViewSite().getActionBars().getMenuManager();
		manager.add(refinementAutomataAction);
		manager.add(openAutomataFileAction);
		manager.add(generateCertainAutomataAction);
	}

	public void setTabFolder(TabFolder tabFolder) {
		this.tabFolder = tabFolder;
	}

	public TabFolder getTabFolder() {
		return tabFolder;
	}

}
