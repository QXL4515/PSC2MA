package cn.cstv.wspscm.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;

import cn.cstv.wspscm.views.JFreeChartView;

public class OpenJFreeChartViewAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	public OpenJFreeChartViewAction() {

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
		this.window = window;
	}

	@Override
	public void run(IAction action) {
		// TODO Auto-generated method stub
		try {
			window.getActivePage().showView(JFreeChartView.ID);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
