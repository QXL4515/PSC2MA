package cn.cstv.wspscm.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import cn.cstv.wspscm.IImageKeys;

/**
 * @author hp
 *
 */
public class GetResultAction extends Action implements ISelectionListener,
		IWorkbenchAction {
	public static final String ID = "cn.cstv.wspscm.actions.GetResultAction";

	private IWorkbenchWindow window;

	public GetResultAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("Result");
		setToolTipText("Get Result");
		setImageDescriptor(IImageKeys.getImageDescriptor(IImageKeys.GETRESULT));
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
	
	@Override
	public void run() {
		
	}
}
