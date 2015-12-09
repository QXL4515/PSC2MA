package cn.cstv.wspscm.actions;

import java.io.IOException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import cn.cstv.wspscm.IImageKeys;

public class OpenJConsoleAction extends Action implements ISelectionListener,
		IWorkbenchAction {

	public static final String ID = "cn.cstv.wspscm.actions.OpenJConsoleAction";

	private IWorkbenchWindow window;
	
	public OpenJConsoleAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("JConsole");
		setToolTipText("Open JConsole");
		setImageDescriptor(IImageKeys.getImageDescriptor(IImageKeys.JCONSOLE));
		window.getSelectionService().addSelectionListener(this);
	}

	@Override
	public void selectionChanged(IWorkbenchPart arg0, ISelection arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		window.getSelectionService().removeSelectionListener(this);
	}

	@Override
	public void run() {
		try {
			Runtime.getRuntime().exec("cmd /c jconsole");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
