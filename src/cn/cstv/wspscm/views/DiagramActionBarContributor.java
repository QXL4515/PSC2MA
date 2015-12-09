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
 * File: DiagramActionBarContributor.java                                                   
 * Program: DiagramActionBarContributor                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-22                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentRetargetAction;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.part.EditorActionBarContributor;

import cn.cstv.wspscm.editor.PSCMonitorDiagramEditor;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 * 
 */
@SuppressWarnings("unchecked")
public class DiagramActionBarContributor extends ActionBarContributor {

	private ActionRegistry registry = new ActionRegistry();

	private List retargetActions = new ArrayList();
	private List globalActionKeys = new ArrayList();

	protected void addAction(IAction action) {
		getActionRegistry().registerAction(action);
	}

	protected void addGlobalActionKey(String key) {
		globalActionKeys.add(key);
	}

	protected void addRetargetAction(RetargetAction action) {
		addAction(action);
		retargetActions.add(action);
		getPage().addPartListener(action);
		addGlobalActionKey(action.getId());
	}

	public void dispose() {
		for (int i = 0; i < retargetActions.size(); i++) {
			RetargetAction action = (RetargetAction) retargetActions.get(i);
			getPage().removePartListener(action);
			action.dispose();
		}
		registry.dispose();
		retargetActions = null;
		registry = null;
	}

	protected IAction getAction(String id) {
		return getActionRegistry().getAction(id);
	}

	/**
	 * returns this contributor's ActionRegsitry.
	 * 
	 * @return the ActionRegistry
	 */
	protected ActionRegistry getActionRegistry() {
		return registry;
	}

	/**
	 * @see EditorActionBarContributor#init(IActionBars)
	 */
	public void init(IActionBars bars) {
		buildActions();
		declareGlobalActionKeys();
		super.init(bars);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#buildActions()
	 */
	@Override
	protected void buildActions() {
		// TODO Auto-generated method stub
		addRetargetAction(new UndoRetargetAction());

		addRetargetAction(new RedoRetargetAction());

		addRetargetAction(new DeleteRetargetAction());

		addRetargetAction(new ZoomInRetargetAction());

		addRetargetAction(new ZoomOutRetargetAction());
		
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.LEFT));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.CENTER));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.RIGHT));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.TOP));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.MIDDLE));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.BOTTOM));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
	 */

	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
		toolBarManager.add(getAction(ActionFactory.REDO.getId()));
		toolBarManager.add(getAction(ActionFactory.DELETE.getId()));
		toolBarManager.add(new Separator());
		toolBarManager.add(getActionRegistry().getAction(
				GEFActionConstants.ZOOM_IN));
		toolBarManager.add(getActionRegistry().getAction(
				GEFActionConstants.ZOOM_OUT));
		toolBarManager.add(new ZoomComboContributionItem(getPage()));
		toolBarManager.add(new Separator());
		toolBarManager.add(getActionRegistry().getAction(GEFActionConstants.ALIGN_LEFT));
		toolBarManager.add(getActionRegistry().getAction(GEFActionConstants.ALIGN_CENTER));
		toolBarManager.add(getActionRegistry().getAction(GEFActionConstants.ALIGN_RIGHT));
		toolBarManager.add(new Separator());
		toolBarManager.add(getActionRegistry().getAction(GEFActionConstants.ALIGN_TOP));
		toolBarManager.add(getActionRegistry().getAction(GEFActionConstants.ALIGN_MIDDLE));
		toolBarManager.add(getActionRegistry().getAction(GEFActionConstants.ALIGN_BOTTOM));
	}

	protected IAction getAction(IEditorPart editor, String actionID) {
		if (editor instanceof PSCMonitorDiagramEditor) {
			return ((PSCMonitorDiagramEditor) editor).getAction(actionID);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
	 */
	@Override
	protected void declareGlobalActionKeys() {
		// TODO Auto-generated method stub

	}

	public void setActiveEditor(IEditorPart editor) {
		ActionRegistry registry = (ActionRegistry) editor
				.getAdapter(ActionRegistry.class);
		IActionBars bars = getActionBars();
		for (int i = 0; i < globalActionKeys.size(); i++) {
			String id = (String) globalActionKeys.get(i);
			bars.setGlobalActionHandler(id, registry.getAction(id));
		}
		bars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(),
				getAction(editor, ActionFactory.SELECT_ALL.getId()));
		bars.updateActionBars();
	}

}
