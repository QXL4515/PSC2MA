package cn.cstv.wspscm;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

import cn.cstv.wspscm.views.ConsoleFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		final String properties = "org.eclipse.ui.views.PropertySheet";
//		final String automata = "cn.cstv.wspscm.views.AutomataView";
		final String gameStructure = "cn.cstv.wspscm.views.GameStructureView";
		final String message = "cn.cstv.wspscm.views.MessageView";
		final String fileManager = "cn.cstv.wspscm.views.NavigatorView";
		//final String BPEL = "cn.cstv.wspscm.views.BPELView";
		final String jfreechart = "cn.cstv.wspscm.views.JFreeChartView";

		final String editorArea = layout.getEditorArea();

		IFolderLayout leftTopFolder = layout.createFolder("LeftTop",
				IPageLayout.LEFT, 0.25f, editorArea);
		
		IFolderLayout BottomFolder = layout.createFolder("Bottom",
				IPageLayout.BOTTOM, 0.5f, editorArea);

		IFolderLayout BottomRightFolder = layout.createFolder("BottomRight",
				IPageLayout.RIGHT, 0.5f, "Bottom");

//		IFolderLayout BottomRightFolder2 = layout.createFolder("BottomRight2",
//				IPageLayout.RIGHT, 0.5f, "BottomRight");

		IFolderLayout rightTopFolder = layout.createFolder("RightTop",
				IPageLayout.RIGHT, 0.5f, editorArea);
		
		IFolderLayout rightBottomFolder = layout.createFolder("RightBottom",
				IPageLayout.BOTTOM, 0.5f, "LeftTop");
		
		ConsoleFactory cf = new ConsoleFactory();
		cf.openConsole();
		// BottomFolder.addPlaceholder(IConsoleConstants.ID_CONSOLE_VIEW);
		BottomRightFolder.addView(IConsoleConstants.ID_CONSOLE_VIEW);
		
		BottomRightFolder.addView(jfreechart);

		leftTopFolder.addPlaceholder("org.eclipse.ui.views.ResourceNavigator"+":*");
		leftTopFolder.addView(fileManager);
		
		rightBottomFolder.addView(properties);

		//rightTopFolder.addView(automata);
		rightTopFolder.addView(gameStructure);

		//BottomFolder.addView(BPEL);

		BottomFolder.addView(message);

		layout.setEditorAreaVisible(true);
	}
}
