package cn.cstv.wspscm;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.ide.model.WorkbenchAdapterBuilder;

@SuppressWarnings("restriction")
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

//	  private SystemTrayMaster trayMaster;

	public ApplicationWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(800, 600));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(false);
		configurer.setTitle("PSC2MA");
		WorkbenchAdapterBuilder.registerAdapters();
		configurer.getWorkbenchConfigurer().declareImage(
				IDE.SharedImages.IMG_OBJ_PROJECT,
				IImageKeys.getImageDescriptor(IImageKeys.PROJECT), true);
	}

	@Override
	public void postWindowCreate() {
		super.postWindowCreate();
		
		// ���ô�ʱ��󻯴���
		getWindowConfigurer().getWindow().getShell().setMaximized(true);
	}
}
