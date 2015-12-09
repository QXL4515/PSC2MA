package cn.cstv.wspscm.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import cn.cstv.wspscm.IImageKeys;
import cn.cstv.wspscm.monitor.AnalyzeMessageSequence;
import cn.cstv.wspscm.views.SWTResourceManager;

public class ExamineMessageSequence extends Action implements
		ISelectionListener,IWorkbenchAction {
	public static final String ID = "cn.cstv.wspscm.actions.ExamineMessageSequence";

	private IWorkbenchWindow window;
	private Shell shell;
	private Text kValueText;
	private Button finishButton;
	private Integer kValue;
	private Button cancelButton;


	public ExamineMessageSequence(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("Find Message Sequence");
		setToolTipText("Find Message Sequence in all automachine");
		setImageDescriptor(IImageKeys.getImageDescriptor(IImageKeys.LOOKAHEAD));
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
		shell = new Shell(window.getShell(),SWT.MIN | SWT.MAX | SWT.CLOSE);
		shell.setSize(503, 199);
		shell.setText("Find message sequence");
		shell.setImage(IImageKeys.getImage(IImageKeys.LOOKAHEAD));
		
		final Composite composite = new Composite(shell, SWT.NONE);
		composite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		composite.setBounds(0, 0, 494, 64);

//		final Label inputMessageRuleLabel = new Label(composite, SWT.NONE);
//		inputMessageRuleLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
//		inputMessageRuleLabel.setText("Look ahead k steps from current message and current state.");
//		inputMessageRuleLabel.setBounds(20, 36, 313, 18);
//
//		final Label generateMessageSequenceLabel = new Label(composite, SWT.NONE);
//		generateMessageSequenceLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
//		generateMessageSequenceLabel.setFont(SWTResourceManager.getFont("", 12, SWT.NONE));
//		generateMessageSequenceLabel.setText("Predict possible errors.");
//		generateMessageSequenceLabel.setBounds(10, 10, 250, 20);
//
//		final Composite composite_1 = new Composite(shell, SWT.NONE);
//		composite_1.setBounds(0, 70, 100, 50);
//
//		final Label label = new Label(composite_1, SWT.NONE);
//		label.setAlignment(SWT.RIGHT);
//		label.setText("k steps:");
//		label.setBounds(5, 10, 45, 15);
//
//		kValueText = new Text(composite_1, SWT.BORDER);
//		kValueText.setBounds(55, 7, 47, 18);
//		addErrorNumberValidateToolTip(kValueText);
//
//		final Group group1 = new Group(shell, SWT.NONE);
//		group1.setText("Controllable");
//		group1.setBounds(110, 70, 200, 70);

//		  //声明一个单选按钮,注意，这里是增加到开始创建的第一个分组框种
//		  final Button button_1 = new Button(group1, SWT.RADIO);
//		  //设置单选按钮的名称
//		  button_1.setSelection(true);
//		  button_1.setText("Controllability/Uncotrollability");
//		  //设置单选按钮的位置和大小
//		  button_1.setBounds(10, 16, 163, 16);
//
//		  final Button button_2 = new Button(group1, SWT.RADIO);
//		  button_2.setText("Accept/Error");
//		  button_2.setBounds(10, 43, 163, 16);
//
//			final Group group2 = new Group(shell, SWT.NONE);
//			group2.setText("Search");
//			group2.setBounds(320, 70, 160, 70);
//
//			  //声明一个单选按钮,注意，这里是增加到开始创建的第一个分组框种
//			  final Button button_3 = new Button(group2, SWT.RADIO);
//			  //设置单选按钮的名称
//			  button_3.setSelection(true);
//			  button_3.setText("Breadth Search First");
//			  //设置单选按钮的位置和大小
//			  button_3.setBounds(10, 16, 123, 16);
//
//			  final Button button_4 = new Button(group2, SWT.RADIO);
//			  button_4.setText("Depth Search First");
//			  button_4.setBounds(10, 43, 123, 16);

		  
		cancelButton = new Button(shell, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				shell.dispose();
			}
		});
		cancelButton.setText("Cancel");
		cancelButton.setBounds(446, 146, 48, 22);

		finishButton = new Button(shell, SWT.NONE);
//		finishButton.setEnabled(false);
		finishButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {

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
//					System.out.println(IImageKeys.messageLogFileName+" "+IImageKeys.automataFileName);
//					AnalyzeMessageSequence.executeVerificationByKSteps(
//							IImageKeys.messageLogFileName, IImageKeys.automataFileName, kValue);
//					AnalyzeMessageSequence.withControllability = button_1.getSelection()?1:0;
//					AnalyzeMessageSequence.isDepthSearchFirst = button_3.getSelection()?1:0;
					AnalyzeMessageSequence.examineMessageSeq();
				}
			
				shell.close();
			}
		});
		finishButton.setText("Finish");
		finishButton.setBounds(380, 146, 48, 22);
		
		shell.open();
		shell.layout();
	}
	
	private void addErrorNumberValidateToolTip(final Text text) {
		// TODO Auto-generated method stub
		text.addKeyListener(new KeyListener() {
			/**
			* Display a tip about detail info of this picture when mouse hover
			* on.
			*/
			final ToolTip errorInfoTip = new ToolTip(text.getShell(),
			SWT.BALLOON | SWT.ICON_ERROR);
			{
			errorInfoTip.setText("Invalid Integer Number");
			errorInfoTip.setAutoHide(true);
			}

			public void keyPressed(KeyEvent e) {
			}

			/**
			* When key released, will check for filename.
			*/
			public void keyReleased(KeyEvent e) {
				try{
				kValue = Integer.parseInt(text.getText());
				if(kValue<0||kValue>100){
					errorInfoTip
					.setMessage("Error: Please input Integer value 1~100");
					errorInfoTip.setVisible(true);
					finishButton.setEnabled(false);
				}else{
					finishButton.setEnabled(true);
				}

				}catch(java.lang.NumberFormatException e1){
//					return "Error: Please input numerical value";
					errorInfoTip
					.setMessage("Error: Please input Integer value 0~100");
					errorInfoTip.setVisible(true);
					finishButton.setEnabled(false);
				}
			}

			});
	}



}
