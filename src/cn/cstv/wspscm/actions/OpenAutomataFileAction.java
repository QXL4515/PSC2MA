package cn.cstv.wspscm.actions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import cn.cstv.wspscm.IImageKeys;
import cn.cstv.wspscm.views.AutomataView;

/**
 * @author hp
 * 
 */
public class OpenAutomataFileAction extends Action implements
		ISelectionListener, IWorkbenchAction {

	public static final String ID = "cn.cstv.wspscm.actions.OpenAutomataFileAction";

	private IWorkbenchWindow window;

	public OpenAutomataFileAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("Automata File");
		setToolTipText("Open Automata File");
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

//	@Override
//	public void run() {
//		String path = OpenFileDialoge();
//		IImageKeys.automataFileName = path;
//		if (path != null) {
//			// IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
//			// IEditorInput input = new FileEditorInput(root.getFile(new
//			// Path(path)));
//			// try {
//			// window.getActivePage().get
//			// } catch (PartInitException e) {
//			// e.printStackTrace();
//			// }
//			InputStream is;
//			try {
//				is = new FileInputStream(path);
//
//				// TODO Auto-generated catch block
//
//				byte[] inputByte = new byte[is.available()];
//
//				is.read(inputByte);
//				String contentString = new String(inputByte);
//				IViewPart view = window.getActivePage().showView(
//						AutomataView.ID);
//				Text text = ((AutomataView) view).getTextArray().get(0);
//				text.setText(contentString);
//
//				is.close();
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (PartInitException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}

	public void run() {
		List<String> path = OpenMultiFileDialoge();
		// IImageKeys.automataFileName = path[0];
		IImageKeys.automataFilesName.clear();
		IImageKeys.automataFilesName.addAll(path);
		if (path.size() > 0) {
			try {
				IViewPart view = window.getActivePage().showView(
						AutomataView.ID);
				((AutomataView) view).resetAutomataView(path.size(),path);

				for (int i = 0; i < path.size(); i++) {
					InputStream is;
					is = new FileInputStream(path.get(i));

					// TODO Auto-generated catch block

					byte[] inputByte = new byte[is.available()];

					is.read(inputByte);
					String contentString = new String(inputByte);
					Text text = ((AutomataView) view).getTextArray().get(i);
					text.setText(contentString);
					is.close();
				}
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

		dlg.setFilterNames(new String[] { "Automata File(*.auto)", "*.*" });
		dlg.setFilterExtensions(new String[] { "*.auto", "*.*" });
		String fileName = dlg.open();
		if (!(fileName.endsWith(".auto"))) {
			MessageDialog.openWarning(null, "Warning!",
					"you have choose wrong file");
			return null;
		} else {
			return fileName;
		}
	}

	public List<String> OpenMultiFileDialoge() {
		final Shell fileDialogShell = new Shell(window.getShell(),
				SWT.DIALOG_TRIM);
		FileDialog dlg = new FileDialog(fileDialogShell, SWT.OPEN | SWT.MULTI);
		String path = Platform.getInstanceLocation().getURL().getPath()
				.toString();
		if (path.length() >= 2) {
			path = path.substring(1);
		} else {
			path = "c:";
		}
		dlg.setFilterPath(path);

		dlg.setFilterNames(new String[] { "Automata File(*.auto)", "*.*" });
		dlg.setFilterExtensions(new String[] { "*.auto", "*.*" });
		dlg.open();
		String[] fileNames = dlg.getFileNames();// 返回所有选择的文件名，不包括路径
		String multiPath = dlg.getFilterPath();// 返回选择的路径，这个和fileNames配合可以得到所有的文件的全路径
		multiPath = multiPath.endsWith("/") ? multiPath : (multiPath + "/");
		List<String> multiFilesPath = new ArrayList<String>();
		for (int i = 0; i < fileNames.length; i++) {
			multiFilesPath.add(multiPath + fileNames[i]);
		}
		return multiFilesPath;
	}

}
