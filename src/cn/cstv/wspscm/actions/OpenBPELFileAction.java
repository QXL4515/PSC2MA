package cn.cstv.wspscm.actions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import cn.cstv.wspscm.IImageKeys;
import cn.cstv.wspscm.views.BPELView;

/**
 * @author hp
 * 
 */
public class OpenBPELFileAction extends Action implements ISelectionListener,
		IWorkbenchAction {

	public static final String ID = "cn.cstv.wspscm.actions.OpenBPELFileAction";

	private IWorkbenchWindow window;

	public OpenBPELFileAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("PSC File");
		setToolTipText("Open PSC File");
		setImageDescriptor(IImageKeys.getImageDescriptor(IImageKeys.OPENFILE));
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
		String path = OpenFileDialoge();
		IImageKeys.bpelFileName = path;
		if (path != null) {
			// IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			// IEditorInput input = new FileEditorInput(root.getFile(new
			// Path(path)));
			// try {
			// window.getActivePage().get
			// } catch (PartInitException e) {
			// e.printStackTrace();
			// }
			InputStream is;
			try {
				is = new FileInputStream(path);

				// TODO Auto-generated catch block

				byte[] inputByte = new byte[is.available()];

				is.read(inputByte);
				String contentString = new String(inputByte);
				IViewPart view = window.getActivePage().showView(BPELView.ID);
				Text text = ((BPELView) view).getText();
				text.setText(contentString);

				is.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String OpenFileDialoge() {
		final Shell fileDialogShell = new Shell(window.getShell(),
				SWT.DIALOG_TRIM);
		FileDialog dlg = new FileDialog(fileDialogShell, SWT.OPEN);
		String path = Platform.getInstanceLocation().getURL().getPath()
				.toString();
		if (path.length() >= 2) {
			path = path.substring(1);
		} else {
			path = "c:";
		}
		dlg.setFilterPath(path);

		dlg.setFilterNames(new String[] { "PSC File(*.pauto)", "*.*" });
		dlg.setFilterExtensions(new String[] { "*.pauto", "*.*" });
		String fileName = dlg.open();
		if (!(fileName.endsWith(".pauto"))) {
			MessageDialog.openWarning(null, "Warning!",
					"you have choose wrong file");
			return null;
		} else {
			return fileName;
		}
	}

}
