package cn.cstv.wspscm.editor;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

public class AppContextMenuProvider extends ContextMenuProvider {

	private ActionRegistry actionRegistry;
	
	public AppContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
		super(viewer);
		// TODO Auto-generated constructor stub
		actionRegistry = registry;
	}

	@Override
	public void buildContextMenu(IMenuManager menu) {
		// TODO Auto-generated method stub
		IAction action;
		GEFActionConstants.addStandardActionGroups(menu);
		
		action = getActionRegistry().getAction(ActionFactory.UNDO.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

		action = getActionRegistry().getAction(ActionFactory.REDO.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

		action = getActionRegistry().getAction(ActionFactory.DELETE.getId());
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

	}

	public ActionRegistry getActionRegistry() {
		return actionRegistry;
	}

}
