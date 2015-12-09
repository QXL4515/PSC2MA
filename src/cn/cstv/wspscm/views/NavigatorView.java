package cn.cstv.wspscm.views;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.actions.OpenResourceAction;
import org.eclipse.ui.views.navigator.ResourceNavigator;

/**
 * @author hp
 *
 */
public class NavigatorView extends ResourceNavigator {

	public static final String ID = "cn.cstv.wspscm.views.NavigatorView";
	
	
    public NavigatorView() {
    	super();
    }
    
    protected void handleDoubleClick(DoubleClickEvent event) {
        IStructuredSelection selection = (IStructuredSelection) event
                .getSelection();
        Object element = selection.getFirstElement();

        // 1GBZIA0: ITPUI:WIN2000 - Double-clicking in navigator should expand/collapse containers
        TreeViewer viewer = getTreeViewer();
        if (viewer.isExpandable(element)) {
            viewer.setExpandedState(element, !viewer.getExpandedState(element));
		} else if (selection.size() == 1 && (element instanceof IResource)
				&& ((IResource) element).getType() == IResource.PROJECT) {
			OpenResourceAction ora = new OpenResourceAction(getSite());
			ora.selectionChanged((IStructuredSelection) viewer.getSelection());
			if (ora.isEnabled()) {
				ora.run();
			}
		}

    }
}
