package cn.cstv.wspscm.actions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import cn.cstv.wspscm.IImageKeys;
import cn.cstv.wspscm.views.MessageView;

/**
 * @author hp
 *
 */
public class ExecuteBPELAction extends Action implements ISelectionListener,
		IWorkbenchAction {

	public static final String ID = "cn.cstv.wspscm.actions.ExecuteBPELAction";

	private IWorkbenchWindow window;

	public ExecuteBPELAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("Execute PSC");
		setToolTipText("Execute PSC Process");
		setImageDescriptor(IImageKeys.getImageDescriptor(IImageKeys.RUNTOOL));
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
		if(IImageKeys.bpelFileName.isEmpty()){
			MessageDialog.openWarning(null, "No File Warning!",
			"PSC File don't exist!");
		}else{
			try {
				IViewPart view = window.getActivePage().showView(MessageView.ID);
				InputStream is;
				String path = "C:\\OutputMessageFile.message";
				IImageKeys.messageLogFileName = "C:\\OutputMessageFile.message";
				try {
					is = new FileInputStream(path);
				
					// TODO Auto-generated catch block

				byte[] inputByte = new byte[is.available()];

				is.read(inputByte);
				String contentString = new String(inputByte);
				((MessageView)view).getText().setText(contentString);
				is.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
