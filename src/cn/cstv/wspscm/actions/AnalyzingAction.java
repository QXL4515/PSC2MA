package cn.cstv.wspscm.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import cn.cstv.wspscm.IImageKeys;
import cn.cstv.wspscm.monitor.AnalyzeMessageSequence;

/**
 * @author hp
 * 
 */
public class AnalyzingAction extends Action implements ISelectionListener,
		IWorkbenchAction {

	public static final String ID = "cn.cstv.wspscm.actions.AnalyzingAction";

	private IWorkbenchWindow window;

	public AnalyzingAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("Analyzing");
		setToolTipText("Analyze messages");
		setImageDescriptor(IImageKeys.getImageDescriptor(IImageKeys.ANALYZE));
		window.getSelectionService().addSelectionListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.
	 * IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.actions.ActionFactory.IWorkbenchAction#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		window.getSelectionService().removeSelectionListener(this);
	}

	@Override
	public void run() {
		if (IImageKeys.messageLogFileName.isEmpty()
				&& IImageKeys.automataFilesName.isEmpty()) {
			MessageDialog.openWarning(null, "No File Warning!",
					"Message File and Automata File don't exist!");
		} else if (IImageKeys.messageLogFileName.isEmpty()
				&& !IImageKeys.automataFilesName.isEmpty()) {
			MessageDialog.openWarning(null, "No File Warning!",
					"Message File don't exist!");
		} else if (!IImageKeys.messageLogFileName.isEmpty()
				&& IImageKeys.automataFilesName.isEmpty()) {
			MessageDialog.openWarning(null, "No File Warning!",
					"Automata File don't exist!");
		} else if (!IImageKeys.messageLogFileName.isEmpty()
				&& !IImageKeys.automataFilesName.isEmpty()) {
//			System.out.println(IImageKeys.messageLogFileName+" "+IImageKeys.automataFileName);
			AnalyzeMessageSequence.executeVerification(
					IImageKeys.messageLogFileName, IImageKeys.automataFilesName);
		}
	}
}
