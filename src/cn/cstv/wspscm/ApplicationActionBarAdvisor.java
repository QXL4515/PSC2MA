package cn.cstv.wspscm;

import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import cn.cstv.wspscm.actions.AnalyzingAction;
import cn.cstv.wspscm.actions.ExamineMessageSequence;
import cn.cstv.wspscm.actions.ExecuteBPELAction;
import cn.cstv.wspscm.actions.GS2AOPAction;
import cn.cstv.wspscm.actions.GenerateCertainAutomataAction;
import cn.cstv.wspscm.actions.GenerateRandomMessagesAction;
import cn.cstv.wspscm.actions.GetResultAction;
import cn.cstv.wspscm.actions.InsertTriggerAction;
import cn.cstv.wspscm.actions.LogFileAction;
import cn.cstv.wspscm.actions.LookAheadAction;
import cn.cstv.wspscm.actions.OpenAutomataFileAction;
import cn.cstv.wspscm.actions.OpenBPELFileAction;
import cn.cstv.wspscm.actions.OpenJConsoleAction;
import cn.cstv.wspscm.actions.OpenMessageLogFileAction;
import cn.cstv.wspscm.actions.OpenPSCMonitorDiagramEditorAction;
import cn.cstv.wspscm.actions.PSC2GameStructureAction;
import cn.cstv.wspscm.actions.RefinementAutomataAction;
import cn.cstv.wspscm.actions.TestAction;
import cn.cstv.wspscm.actions.TransferPscToAutomateAction;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	// Open BPEL File Action with the open dialog
	private OpenPSCMonitorDiagramEditorAction openPSCMonitorDiagramEditorAction;
	// the actions from Workbench
	private IWorkbenchAction newWizardAction;
	private IWorkbenchAction saveAction;
	private IWorkbenchAction saveAsAction;
	private IWorkbenchAction saveAllAction;
	private IWorkbenchAction quitAction;
	private IWorkbenchAction aboutAction;
	private IWorkbenchAction perferencesAction;
	private IWorkbenchAction helpContentAction;
	private IWorkbenchAction dynamicHelpAction;
	private IWorkbenchAction selectAllAction;
	private IWorkbenchAction undoAction;
	private IWorkbenchAction redoAction;
	private TransferPscToAutomateAction transferPscToAutomateAction;
	private PSC2GameStructureAction psc2GameStructureAction;
	private GS2AOPAction gs2aopAction;
	private LogFileAction logFileAction;
	private GetResultAction getResultAction;
	private AnalyzingAction analyzingAction;
	private GenerateRandomMessagesAction generateRandomMessagesAction;
	private InsertTriggerAction insertTriggerAction;
	private ExecuteBPELAction executeBPELAction;
	private OpenBPELFileAction openBPELFileAction;
	private OpenAutomataFileAction openAutomataFileAction;
	private OpenMessageLogFileAction openMessageLogFileAction;
	private RefinementAutomataAction refinementAutomataAction;
	private TestAction testAction;
	private IWorkbenchAction preferencesAction;
	private LookAheadAction lookAheadAction;
	private ExamineMessageSequence examineMessageSequence;
	private GenerateCertainAutomataAction generateCertainAutomataAction;
	private OpenJConsoleAction openJConsoleAction;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(IWorkbenchWindow window) {
		newWizardAction = ActionFactory.NEW.create(window);
		newWizardAction.setText("New");		
		saveAction = ActionFactory.SAVE.create(window);
		saveAsAction = ActionFactory.SAVE_AS.create(window);
		saveAllAction = ActionFactory.SAVE_ALL.create(window);
		openPSCMonitorDiagramEditorAction = new OpenPSCMonitorDiagramEditorAction(
				window);
		quitAction = ActionFactory.QUIT.create(window);
		aboutAction = ActionFactory.ABOUT.create(window);
		perferencesAction = ActionFactory.PREFERENCES.create(window);
		helpContentAction = ActionFactory.HELP_CONTENTS.create(window);
		dynamicHelpAction = ActionFactory.DYNAMIC_HELP.create(window);
		selectAllAction = ActionFactory.SELECT_ALL.create(window);
		selectAllAction.setImageDescriptor(IImageKeys.getImageDescriptor(IImageKeys.SELECT_ALL));
		undoAction = ActionFactory.UNDO.create(window);
		redoAction = ActionFactory.REDO.create(window);
		transferPscToAutomateAction = new TransferPscToAutomateAction(window);
		psc2GameStructureAction = new PSC2GameStructureAction(window);
		gs2aopAction = new GS2AOPAction(window);
		logFileAction = new LogFileAction(window);	
		getResultAction = new GetResultAction(window);
		analyzingAction = new AnalyzingAction(window);
		generateRandomMessagesAction = new GenerateRandomMessagesAction(window);
		insertTriggerAction = new InsertTriggerAction(window);
		executeBPELAction = new ExecuteBPELAction(window);
		openBPELFileAction = new OpenBPELFileAction(window);
		openAutomataFileAction = new OpenAutomataFileAction(window);
		openMessageLogFileAction = new OpenMessageLogFileAction(window);
		refinementAutomataAction = new RefinementAutomataAction(window);
		testAction = new TestAction(window);
		preferencesAction = ActionFactory.PREFERENCES.create(window);
		lookAheadAction = new LookAheadAction(window);
		examineMessageSequence=new ExamineMessageSequence(window);
		generateCertainAutomataAction = new GenerateCertainAutomataAction(window);
		openJConsoleAction = new OpenJConsoleAction(window);

		register(newWizardAction);
		register(saveAction);
		register(saveAsAction);
		register(saveAllAction);
		register(openPSCMonitorDiagramEditorAction);
		register(quitAction);
		register(aboutAction);
		register(perferencesAction);
		register(helpContentAction);
		register(dynamicHelpAction);
		register(selectAllAction);
		register(undoAction);
		register(redoAction);
		register(transferPscToAutomateAction);
		register(psc2GameStructureAction);
		register(gs2aopAction);
		register(logFileAction);
		register(getResultAction);
		register(analyzingAction);
		register(generateRandomMessagesAction);
		register(insertTriggerAction);
		register(executeBPELAction);
		register(openBPELFileAction);
		register(openAutomataFileAction);
		register(openMessageLogFileAction);
		register(refinementAutomataAction);
		register(testAction);
		register(preferencesAction);
		register(lookAheadAction);
		register(examineMessageSequence);
		register(generateCertainAutomataAction);
		register(openJConsoleAction);
	}

	protected void fillMenuBar(IMenuManager menuBar) {
		IMenuManager fileMenu = new MenuManager("File(&F)",
				IWorkbenchActionConstants.M_FILE);
		IMenuManager editMenu = new MenuManager("Edit(&E)",
				IWorkbenchActionConstants.M_HELP);
		IMenuManager transferMenu = new MenuManager("Transfer(&T)",
				IWorkbenchActionConstants.M_HELP);
		IMenuManager monitorMenu = new MenuManager("Monitor(&M)",
				IWorkbenchActionConstants.M_HELP);
		IMenuManager testMenu = new MenuManager("Test(&T)",
				IWorkbenchActionConstants.M_HELP);
		IMenuManager viewMenu = new MenuManager("View(&V)",
				IWorkbenchActionConstants.WINDOW_EXT);
		IMenuManager windowsMenu = new MenuManager("Windows(&W)",
				IWorkbenchActionConstants.M_HELP);
		IMenuManager helpMenu = new MenuManager("Help(&H)",
				IWorkbenchActionConstants.M_HELP);
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(transferMenu);
		menuBar.add(monitorMenu);
		menuBar.add(testMenu);
		menuBar.add(viewMenu);
		menuBar.add(windowsMenu);
		menuBar.add(helpMenu);

		fileMenu.add(newWizardAction);
		fileMenu.add(openPSCMonitorDiagramEditorAction);
		fileMenu.add(openBPELFileAction);
		fileMenu.add(openAutomataFileAction);
		fileMenu.add(openMessageLogFileAction);
		fileMenu.add(new Separator());
		fileMenu.add(saveAction);
		fileMenu.add(saveAsAction);
		fileMenu.add(saveAllAction);
		fileMenu.add(new Separator());
		fileMenu.add(perferencesAction);
		fileMenu.add(new Separator());
		fileMenu.add(quitAction);
		editMenu.add(undoAction);
		editMenu.add(redoAction);
		editMenu.add(new Separator());
		editMenu.add(selectAllAction);
		transferMenu.add(transferPscToAutomateAction);
		transferMenu.add(psc2GameStructureAction);
		transferMenu.add(gs2aopAction);
		transferMenu.add(refinementAutomataAction);
		transferMenu.add(generateCertainAutomataAction);
		monitorMenu.add(logFileAction);
		monitorMenu.add(new Separator());	
		monitorMenu.add(analyzingAction);
		monitorMenu.add(new Separator());	
		monitorMenu.add(getResultAction);
		monitorMenu.add(generateRandomMessagesAction);
		monitorMenu.add(executeBPELAction);
		monitorMenu.add(insertTriggerAction);
		monitorMenu.add(new Separator());
		monitorMenu.add(lookAheadAction);
		monitorMenu.add(examineMessageSequence);
		testMenu.add(testAction);
		windowsMenu.add(preferencesAction);
		windowsMenu.add(new Separator());
		windowsMenu.add(openJConsoleAction);
		helpMenu.add(new Separator());
		helpMenu.add(helpContentAction);
		helpMenu.add(dynamicHelpAction);
		helpMenu.add(aboutAction);
	}
	
	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);

		coolBar.add(new ToolBarContributionItem(toolbar, "main"));
		toolbar.add(newWizardAction);
		toolbar.add(openPSCMonitorDiagramEditorAction);
		toolbar.add(saveAction);
		toolbar.add(new Separator());
		toolbar.add(selectAllAction);
		toolbar.add(new Separator());
		toolbar.add(transferPscToAutomateAction);
		toolbar.add(psc2GameStructureAction);
		toolbar.add(gs2aopAction);
		toolbar.add(refinementAutomataAction);
		toolbar.add(generateCertainAutomataAction);
		toolbar.add(new Separator());
		toolbar.add(logFileAction);
		toolbar.add(lookAheadAction);
		toolbar.add(examineMessageSequence);
		toolbar.add(getResultAction);
		toolbar.add(generateRandomMessagesAction);
		toolbar.add(executeBPELAction);
		toolbar.add(analyzingAction);
		toolbar.add(new Separator());
		toolbar.add(testAction);
		toolbar.add(openJConsoleAction);
	}
}
