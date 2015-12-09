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
 * Package: cn.cstv.wspscm.actions                                            
 * File: OpenPSCMonitorDiagramEditorAction.java                                                   
 * Program: OpenPSCMonitorDiagramEditorAction                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-21                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.actions;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.part.FileEditorInput;

import cn.cstv.wspscm.IImageKeys;
import cn.cstv.wspscm.editor.PSCMonitorDiagramEditor;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class OpenPSCMonitorDiagramEditorAction extends Action implements
		ISelectionListener, IWorkbenchAction {
	
	public static final String ID = "cn.cstv.wspscm.actions.OpenPSCMonitorDiagramEditorAction";

	private IWorkbenchWindow window;

	public OpenPSCMonitorDiagramEditorAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("Open(*.PSC)");
		setToolTipText("Open PSCMonitor");
		setImageDescriptor(IImageKeys.getImageDescriptor(IImageKeys.FILE));
		window.getSelectionService().addSelectionListener(this);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.actions.ActionFactory.IWorkbenchAction#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		window.getSelectionService().removeSelectionListener(this);
	}
	
	public String OpenFileDialoge() {
		final Shell fileDialogShell = new Shell(window.getShell(),
				SWT.DIALOG_TRIM);
		FileDialog dlg = new FileDialog(fileDialogShell, SWT.OPEN);
		String path = Platform.getInstanceLocation().getURL().getPath().toString();
		if(path.length()>=2){
			path = path.substring(1);
		}else{
			path = "c:";
		}
		dlg.setFilterPath(path);

		dlg.setFilterNames(new String[] { "*.*","extended sequence diagram(*.esd)","extended state diagram(*.smd)","smv file(*.smv)","property(*.prop)","process(*.bpel)","choreography(*.cdl)" ,"PSC Monitor(*.psc)"});
		dlg.setFilterExtensions(new String[]{"*.*","*.esd" ,"*.smd","*.smv","*.prop","*.bpel","*.cdl","*.psc"});
		String fileName = dlg.open();
		if(!(fileName.endsWith(".esd")||fileName.endsWith(".smd")||fileName.endsWith(".smv")||fileName.endsWith(".prop")||fileName.endsWith(".bpel")||fileName.endsWith(".cdl")||fileName.endsWith(".psc"))){
			MessageDialog.openWarning(null, "Warning!", "you have choose wrong file");
			return null;
		}else{
			return fileName;
		}
	}

	@Override
	public void run() {
		String path = OpenFileDialoge();
		if (path != null) {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IEditorInput input = new FileEditorInput(root.getFile(new Path(path)));
			IWorkbenchPage page = window.getActivePage();
			try {
				page.openEditor(input, PSCMonitorDiagramEditor.ID, true);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
	}

}
