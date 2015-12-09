package cn.cstv.wspscm.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Platform;
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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import cn.cstv.wspscm.IImageKeys;
import cn.cstv.wspscm.monitor.OutputRandomMessages;
import cn.cstv.wspscm.views.SWTResourceManager;


/**
 * @author hp
 *
 */
public class GenerateRandomMessagesAction extends Action implements
		ISelectionListener, IWorkbenchAction {

	private Text ratioText;
	private Text errorRuleText;
	private Shell shell;
	private Text numberText;
	private Text ruleText;
	private String path;
	private String errorPath;
	private Button generateButton;
	private Button cancelButton;
	private Integer number;
	private Integer ratio;
	public static final String ID = "cn.cstv.wspscm.actions.GenerateRandomMessagesAction";

	private IWorkbenchWindow window;

	public GenerateRandomMessagesAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("Generate message");
		setToolTipText("Generate Random message sequences");
		setImageDescriptor(IImageKeys.getImageDescriptor(IImageKeys.GENERATE));
		this.window.getSelectionService().addSelectionListener(this);
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
		



		shell = new Shell(window.getShell(),SWT.MIN | SWT.MAX | SWT.CLOSE);
		shell.setSize(503, 259);
		shell.setText("Generate Message");
		shell.setImage(IImageKeys.getImage(IImageKeys.GENERATE));

		final Composite composite = new Composite(shell, SWT.NONE);
		composite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		composite.setBounds(0, 0, 494, 64);

		final Label inputMessageRuleLabel = new Label(composite, SWT.NONE);
		inputMessageRuleLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		inputMessageRuleLabel.setText("Input Message Rule for generating message sequence");
		inputMessageRuleLabel.setBounds(20, 36, 313, 18);

		final Label generateMessageSequenceLabel = new Label(composite, SWT.NONE);
		generateMessageSequenceLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		generateMessageSequenceLabel.setFont(SWTResourceManager.getFont("", 12, SWT.NONE));
		generateMessageSequenceLabel.setText("Generate Message Sequence");
		generateMessageSequenceLabel.setBounds(10, 10, 250, 20);

		final Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setBounds(0, 70, 494, 130);

		final Label nameLabel = new Label(composite_1, SWT.NONE);
		nameLabel.setAlignment(SWT.RIGHT);
		nameLabel.setText("Rule:");
		nameLabel.setBounds(10, 10, 107, 15);

		ruleText = new Text(composite_1, SWT.BORDER);
		ruleText.setBounds(123, 7, 284, 18);

		final Button browseButton = new Button(composite_1, SWT.NONE);
		browseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				path = OpenFileDialoge();
				if ( path != null )
					ruleText.setText(path);	
				if(number>0){
					generateButton.setEnabled(true);
				}
			}
		});
		browseButton.setText("Browse...");
		browseButton.setBounds(413, 5, 71, 22);

		final Label errorRuleFileLabel = new Label(composite_1, SWT.NONE);
		errorRuleFileLabel.setAlignment(SWT.RIGHT);
		errorRuleFileLabel.setText("Error Rule:");
		errorRuleFileLabel.setBounds(10, 42, 107, 12);

		errorRuleText = new Text(composite_1, SWT.BORDER);
		errorRuleText.setBounds(123, 38, 284, 18);

		final Button browseButton1 = new Button(composite_1, SWT.NONE);
		browseButton1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				errorPath = OpenFileDialoge();
				if ( errorPath != null)
					errorRuleText.setText(errorPath);	
				if(ratio>=0&&ratio<=100){
					generateButton.setEnabled(true);
				}
			}
		});
		browseButton1.setText("Browse...");
		browseButton1.setBounds(413, 37, 71, 22);

		final Label messageNumberLabel = new Label(composite_1, SWT.NONE);
		messageNumberLabel.setAlignment(SWT.RIGHT);
		messageNumberLabel.setText("Message Number:");
		messageNumberLabel.setBounds(10, 73, 107, 15);

		numberText = new Text(composite_1, SWT.BORDER);
		numberText.setBounds(123, 70, 107, 18);
		addNumberValidateToolTip(numberText);

		final Label pleaseInputIntegerLabel = new Label(composite_1, SWT.NONE);
		pleaseInputIntegerLabel.setText("(Please input Integer Number)");
		pleaseInputIntegerLabel.setBounds(310, 73, 174, 12);

		final Label label = new Label(composite_1, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		label.setText("Error Ratio:");
		label.setBounds(22, 106, 95, 12);

		ratioText = new Text(composite_1, SWT.BORDER);
		ratioText.setBounds(123, 101, 107, 18);
		addErrorNumberValidateToolTip(ratioText);

		final Label label_1 = new Label(composite_1, SWT.NONE);
		label_1.setText("%");
		label_1.setBounds(234, 106, 13, 12);

		final Label integerNumber0100Label = new Label(composite_1, SWT.NONE);
		integerNumber0100Label.setText("(Integer Number 0~100)");
		integerNumber0100Label.setBounds(310, 106, 174, 12);

		cancelButton = new Button(shell, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				shell.dispose();
			}
		});
		cancelButton.setText("Cancel");
		cancelButton.setBounds(446, 206, 48, 22);

		generateButton = new Button(shell, SWT.NONE);
		generateButton.setEnabled(false);
		generateButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				IFile messageFile = ((IFileEditorInput) window.getActivePage()
						.getActiveEditor().getEditorInput()).getFile();
				String messageLogFileName = messageFile.getLocation().toString();
				messageLogFileName= messageLogFileName.substring(0, messageLogFileName.lastIndexOf("."))+ ".message";
				(new OutputRandomMessages()).GenerateRandomMessages(messageLogFileName,path,errorPath,window,number,ratio);
				IImageKeys.messageLogFileName = messageLogFileName;
				shell.close();
			}
		});
		generateButton.setText("Finish");
		generateButton.setBounds(380, 206, 48, 22);

//		try {
//			IViewPart view = window.getActivePage().showView(MessageView.ID);
//			InputStream is;
//			String path = "C:\\OutputMessageFile.rule";
//			String outputFileName = "C:\\OutputMessageFile.rule";
//			try {
//				(new OutputRandomMessages()).GenerateRandomMessages(path, outputFileName);
//				is = new FileInputStream(outputFileName);
//			
//				// TODO Auto-generated catch block
//
//			byte[] inputByte = new byte[is.available()];
//
//			is.read(inputByte);
//			String contentString = new String(inputByte);
//			((MessageView)view).getText().setText(contentString);
//			is.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		} catch (PartInitException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}


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
				ratio = Integer.parseInt(text.getText());
				if(ratio<0||ratio>100){
					errorInfoTip
					.setMessage("Error: Please input Integer value 0~100");
					errorInfoTip.setVisible(true);
					generateButton.setEnabled(false);
				}else{
					if(!errorPath.isEmpty()){
						generateButton.setEnabled(true);
					}
				}

				}catch(java.lang.NumberFormatException e1){
//					return "Error: Please input numerical value";
					errorInfoTip
					.setMessage("Error: Please input Integer value 0~100");
					errorInfoTip.setVisible(true);
					generateButton.setEnabled(false);
				}
			}

			});
	}

	/**
	 * @param numberText2
	 */
	private void addNumberValidateToolTip(final Text text) {
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

				number = Integer.parseInt(text.getText());
				if(!path.isEmpty()){
					generateButton.setEnabled(true);
				}
				}catch(java.lang.NumberFormatException e1){
//					return "Error: Please input numerical value";
					errorInfoTip
					.setMessage("Error: Please input Integer value");
					errorInfoTip.setVisible(true);
					generateButton.setEnabled(false);

				}

			}

			});
	}

	public String OpenFileDialoge() {
		final Shell fileDialogShell = new Shell(window.getShell(),SWT.DIALOG_TRIM);
		FileDialog dlg = new FileDialog(fileDialogShell, SWT.OPEN);
		String path = Platform.getInstanceLocation().getURL().getPath().toString();
		if(path.length()>=2){
			path = path.substring(1);
		}else{
			path = "c:";
		}
		dlg.setFilterPath(path);

		dlg.setFilterNames(new String[] { "Time Message Rule(*.rule)","*.*"});
		dlg.setFilterExtensions(new String[]{"*.rule", "*.*"});
		String fileName = dlg.open();
		if(!(fileName.endsWith(".rule"))){
			MessageDialog.openWarning(null, "Warning!", "you have choose wrong file");
			return null;
		}else{
			return fileName;
		}
	}
}
