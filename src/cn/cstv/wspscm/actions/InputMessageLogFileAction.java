package cn.cstv.wspscm.actions;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import cn.cstv.wspscm.IImageKeys;

/**
 * @author hp
 *
 */
public class InputMessageLogFileAction extends Action implements
		ISelectionListener, IWorkbenchAction {

	public static final String ID = "cn.cstv.wspscm.actions.InputMessageLogFileAction";

	private IWorkbenchWindow window;

	public InputMessageLogFileAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("MessageLog");
		setToolTipText("Input Message Log File");
		setImageDescriptor(IImageKeys.getImageDescriptor(IImageKeys.OPENFILE));
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

		dlg.setFilterNames(new String[] { "*.*","Message Log File(*.message)"});
		dlg.setFilterExtensions(new String[]{"*.*","*.message" });
		String fileName = dlg.open();
		if(!(fileName.endsWith(".message"))){
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
//			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
//			IEditorInput input = new FileEditorInput(root.getFile(new Path(path)));
//			IWorkbenchPage page = window.getActivePage();
//			try {
//				window.getActivePage().get
//			} catch (PartInitException e) {
//				e.printStackTrace();
//			}
		}
	}

}
