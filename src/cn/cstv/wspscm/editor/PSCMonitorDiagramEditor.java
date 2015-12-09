/*************************************************************************
 * Copyright (c) 2006, 2008. All rights reserved. This program and the   
 * accompanying materials are made available under the terms of the      
 * Eclipse Public License v1.0 which accompanies this distribution,       
 * and is available at http://www.eclipse.org/legal/epl-v10.html         
 * 
 * Contributors:                                                         
 * Author: Su Zhiyong                                                    
 * Group: CSTV (Chair of Software Testing & Verification) Group           
 * E-mail: zhiyongsu@gmail.com                                          
 ***********************************************************************/

/***********************************************************************
 * Project: cn.cstv.wspscm                                          
 * Package: cn.cstv.wspscm.editor                                            
 * File: PSCMonitorDiagramEditor.java                                                   
 * Program: PSCMonitorDiagramEditor                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-21                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.EventObject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.properties.UndoablePropertySheetEntry;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;

import cn.cstv.wspscm.IImageKeys;
import cn.cstv.wspscm.commands.SetConstraintToolEntry;
import cn.cstv.wspscm.model.Diagram;
import cn.cstv.wspscm.model.EnvironmentLifeline;
import cn.cstv.wspscm.model.EnvironmentLineConnection;
import cn.cstv.wspscm.model.Lifeline;
import cn.cstv.wspscm.model.LineConnection;
import cn.cstv.wspscm.model.Operator;
import cn.cstv.wspscm.parts.PartsFactory;

/**
 * @author Su Zhiyong
 * 
 */
public class PSCMonitorDiagramEditor extends GraphicalEditorWithFlyoutPalette {

	public static final String ID = "cn.cstv.wspscm.editor.PSCMonitorDiagramEditor";

	private PropertySheetPage propertySheetPage;

	private Diagram diagram;	//����													

	/**
	 * @return the diagram
	 */
	public Diagram getDiagram() {
		return diagram;
	}

	/**
	 * @param diagram
	 *            the diagram to set
	 */
	public void setDiagram(Diagram diagram) {
		this.diagram = diagram;
	}

	public PSCMonitorDiagramEditor() {
		setEditDomain(new DefaultEditDomain(this));//���ñ༭��
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getPaletteRoot
	 * ()
	 */
	@Override
	protected PaletteRoot getPaletteRoot() {
		// TODO Auto-generated method stub
		PaletteRoot root = new PaletteRoot();
		PaletteGroup toolGroup = new PaletteGroup("tools");

		ToolEntry toolSelect = new SelectionToolEntry();
		toolGroup.add(toolSelect);
		root.setDefaultEntry(toolSelect);

		ToolEntry toolMarquee = new MarqueeToolEntry();
		toolGroup.add(toolMarquee);

		PaletteDrawer drawer = new PaletteDrawer("draw");

		ImageDescriptor systemLifelineDescriptor = IImageKeys
				.getImageDescriptor(IImageKeys.LIFE_LINE);

		CombinedTemplateCreationEntry createLifelineToolEntry = new CombinedTemplateCreationEntry(
				"System Lifeline", "create systemlifeline", Lifeline.class,
				new SimpleFactory(Lifeline.class), systemLifelineDescriptor,
				systemLifelineDescriptor);

		drawer.add(createLifelineToolEntry);
		
		ImageDescriptor environmentLifelineDescriptor = IImageKeys
		.getImageDescriptor(IImageKeys.LIFE_LINE);

		CombinedTemplateCreationEntry createEnvironmentLifelineToolEntryE = new CombinedTemplateCreationEntry(
		"Environment Lifeline", "create environmentlifeline", EnvironmentLifeline.class,
		new SimpleFactory(EnvironmentLifeline.class), environmentLifelineDescriptor,
		environmentLifelineDescriptor);

		drawer.add(createEnvironmentLifelineToolEntryE);
		
		ImageDescriptor operatorDscriptor = IImageKeys
				.getImageDescriptor(IImageKeys.OPERATOR);

		CombinedTemplateCreationEntry createOperatorToolEntry = new CombinedTemplateCreationEntry(
				"Operator", "create operator", Operator.class,
				new SimpleFactory(Operator.class), operatorDscriptor,
				operatorDscriptor);

		drawer.add(createOperatorToolEntry);
		
		
		
		PaletteDrawer connectionDrawer = new PaletteDrawer("connection");

		ImageDescriptor newConnectionDescriptor = IImageKeys
				.getImageDescriptor(IImageKeys.ARROW_CONNECTION);

		ConnectionCreationToolEntry connxCreateEntry = new ConnectionCreationToolEntry(
				"System Message", "create system message", new SimpleFactory(
						LineConnection.class), newConnectionDescriptor,
				newConnectionDescriptor);

		connectionDrawer.add(connxCreateEntry);
		
		ImageDescriptor newEnvironmentConnectionDescriptor = IImageKeys
		.getImageDescriptor(IImageKeys.ARROW_CONNECTION);

		ConnectionCreationToolEntry connxCreateEntryE = new ConnectionCreationToolEntry(
		"Environment Message", "create environment message", new SimpleFactory(
				EnvironmentLineConnection.class), newEnvironmentConnectionDescriptor,
		newEnvironmentConnectionDescriptor);

		connectionDrawer.add(connxCreateEntryE);
		
		
		
		PaletteDrawer attributeDrawer = new PaletteDrawer("Constraint");
		
		/*
		ImageDescriptor presentConstraintDescriptor = IImageKeys
				.getImageDescriptor(IImageKeys.PRESENTCONSTRAINT);

		ToolEntry presentConstraintCreateEntry = new SetConstraintToolEntry(
				"PresentConstraint", "Set PresentContraint",
				presentConstraintDescriptor, presentConstraintDescriptor,
				"PresentConstraint");

		attributeDrawer.add(presentConstraintCreateEntry);
		*/
		
		
		
		ImageDescriptor unwantedMessageConDescriptor = IImageKeys
				.getImageDescriptor(IImageKeys.UNWANTEDMESSAGE);

		ToolEntry unwantedMessageConCreateEntry = new SetConstraintToolEntry(
				"UnwantedMessageConstraint", "Set UnwantedMessageConstraint(Past or Future)",
				unwantedMessageConDescriptor, unwantedMessageConDescriptor, "UnwantedMessageConstraint");

		attributeDrawer.add(unwantedMessageConCreateEntry);
		
		/*
		ImageDescriptor openBooleanConDescriptor = IImageKeys
				.getImageDescriptor(IImageKeys.OPENBOOLEANCON);

		ToolEntry openBooleanConCreateEntry = new SetConstraintToolEntry(
				"Present", "Set Present(Past or Future)",
				openBooleanConDescriptor, openBooleanConDescriptor, "Present");

		attributeDrawer.add(openBooleanConCreateEntry);
		*/
		
		ImageDescriptor openChainConDescriptor = IImageKeys
				.getImageDescriptor(IImageKeys.OPENCHAINCON);

		ToolEntry openChainConCreateEntry = new SetConstraintToolEntry(
				"WantedChainConstraint",
				"Set WantedChainConstraint(Past or Future)",
				openChainConDescriptor, openChainConDescriptor,
				"WantedChainConstraint");

		attributeDrawer.add(openChainConCreateEntry);

		ImageDescriptor chainConDescriptor = IImageKeys
				.getImageDescriptor(IImageKeys.CHAINCON);

		ToolEntry chainConCreateEntry = new SetConstraintToolEntry(
				"UnwantedChainConstraint", "Set UnwantedChainConstraint(Past or Future)",
				chainConDescriptor, chainConDescriptor,
				"UnwantedChainConstraint");

		attributeDrawer.add(chainConCreateEntry);
		
		ImageDescriptor strictDescriptor = IImageKeys
		.getImageDescriptor(IImageKeys.STRICT);

		ToolEntry strictCreateEntry = new SetConstraintToolEntry(
				"Strict", "Set Strict", strictDescriptor,
						strictDescriptor, "Strict"); 

		attributeDrawer.add(strictCreateEntry);
		
		root.add(toolGroup);
		root.add(drawer);
		root.add(connectionDrawer);
		root.add(attributeDrawer);
		return root;
	}

	@SuppressWarnings( { "deprecation", "unchecked" })
	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		ScalableRootEditPart rootEditPart = new ScalableRootEditPart();
		getGraphicalViewer().setRootEditPart(rootEditPart);
		getGraphicalViewer().setEditPartFactory(new PartsFactory());		//����EditPart�Ĺ�����
		// getGraphicalViewer().getControl().setBackground(ColorConstants.blue);
		// �ṩ���������Ź���
		ZoomManager manager = rootEditPart.getZoomManager();

		double[] zoomLevels = new double[] { 0.25, 0.5, 0.75, 1, 1.25, 1.5,
				2.0, 3.0, 5.0, 10.0, 20.0 };
		manager.setZoomLevels(zoomLevels);

		ArrayList zoomContributions = new ArrayList();
		zoomContributions.add(ZoomManager.FIT_ALL);
		zoomContributions.add(ZoomManager.FIT_HEIGHT);
		zoomContributions.add(ZoomManager.FIT_WIDTH);
		manager.setZoomLevelContributions(zoomContributions);

		IAction action = new ZoomInAction(manager);
		getActionRegistry().registerAction(action);

		action = new ZoomOutAction(manager);
		getActionRegistry().registerAction(action);

		// �������̾��
		KeyHandler keyHandler = new KeyHandler();

		keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0),
				getActionRegistry().getAction(GEFActionConstants.DELETE));

		keyHandler.put(KeyStroke.getPressed(SWT.F2, 0), getActionRegistry()
				.getAction(GEFActionConstants.DIRECT_EDIT));

		keyHandler.put(KeyStroke.getPressed(SWT.F3, 0), getActionRegistry()
				.getAction(GEFActionConstants.UNDO));

		getGraphicalViewer().setKeyHandler(
				new GraphicalViewerKeyHandler(getGraphicalViewer())
						.setParent(keyHandler));

		ContextMenuProvider provider = new AppContextMenuProvider(
				getGraphicalViewer(), getActionRegistry());
		getGraphicalViewer().setContextMenu(provider);
		getSite().registerContextMenu(provider, getGraphicalViewer());

	}

	@Override
	protected void initializeGraphicalViewer() {
		getGraphicalViewer().setContents(diagram);    //����contents
		getGraphicalViewer().addDropTargetListener(	  //�����Ϸ�֧��
				new TemplateTransferDropTargetListener(getGraphicalViewer()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		// getCommandStack().markSaveLocation();
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			createOutputStream(out);
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			file.setContents(new ByteArrayInputStream(out.toByteArray()), true,
					false, monitor);
			out.close();
			getCommandStack().markSaveLocation();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doSaveAs() {
		SaveAsDialog dialog = new SaveAsDialog(getSite().getWorkbenchWindow()
				.getShell());
		dialog.setOriginalFile(((IFileEditorInput) getEditorInput()).getFile());
		dialog.open();
		IPath path = dialog.getResult();

		if (path == null)
			return;

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IFile file = workspace.getRoot().getFile(path);

		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			public void execute(final IProgressMonitor monitor)
					throws CoreException {
				try {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					createOutputStream(out);
					file.create(new ByteArrayInputStream(out.toByteArray()),
							true, monitor);
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		try {
			new ProgressMonitorDialog(getSite().getWorkbenchWindow().getShell())
					.run(false, true, op);
			setInput(new FileEditorInput((IFile) file));
			getCommandStack().markSaveLocation();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		// IFile file = ((IFileEditorInput)input).getFile();
		// try {
		// InputStream is = file.getContents(false);
		// ObjectInputStream ois = new ObjectInputStream(is);
		// diagram = (Diagram)ois.readObject();
		// ois.close();
		// } catch (Exception e) {
		// //This is just an example. All exceptions caught here.
		// e.printStackTrace();
		// diagram = new Diagram();
		// }
		// if (input instanceof IFileEditorInput) {
		// setPartName(((IFileEditorInput) input).getName());
		// }
		IFile file = ((IFileEditorInput) input).getFile();
		try {
			InputStream is = file.getContents(false);
			ObjectInputStream ois = new ObjectInputStream(is);
			diagram = (Diagram) ois.readObject();
			ois.close();
			setPartName(file.getName());
		} catch (Exception e) {
			// This is just an example. All exceptions caught here.
			e.printStackTrace();
			diagram = new Diagram();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void createActions() {
		super.createActions();
		ActionRegistry registry = getActionRegistry();

		IAction action = new DirectEditAction((IWorkbenchPart) this);
		registry.registerAction(action);

		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart) this,
				PositionConstants.LEFT);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart) this,
				PositionConstants.CENTER);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart) this,
				PositionConstants.RIGHT);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart) this,
				PositionConstants.TOP);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart) this,
				PositionConstants.MIDDLE);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart) this,
				PositionConstants.BOTTOM);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
	}

	public IAction getAction(String key) {
		return getActionRegistry().getAction(key);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class type) {
		if (type == IPropertySheetPage.class) {
			propertySheetPage = new MyPropertySheetPage();
			// �±���仰�ǳ���Ҫ,�������,Properties View����ʱ��,��Դ�����Զ�����...
			propertySheetPage.setRootEntry(new UndoablePropertySheetEntry(this
					.getCommandStack()));
			return propertySheetPage;
		}

		if (type == ZoomManager.class)
			return ((ScalableRootEditPart) getGraphicalViewer()
					.getRootEditPart()).getZoomManager();
		return super.getAdapter(type);
	}

	public boolean isDirty() {
		return getCommandStack().isDirty();
	}

	public void commandStackChanged(EventObject event) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(event);
	}

	/**
	 * Creates an appropriate output stream and writes the activity diagram out
	 * to this stream.
	 * 
	 * @param os
	 *            the base output stream
	 * @throws IOException
	 */
	protected void createOutputStream(OutputStream os) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(os);
		out.writeObject(diagram);
		out.close();
	}
}
