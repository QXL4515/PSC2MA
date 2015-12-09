package cn.cstv.wspscm.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import cn.cstv.wspscm.IImageKeys;
import cn.cstv.wspscm.views.AutomataView;
import cn.cstv.wspscm.views.SWTResourceManager;

public class GenerateCertainAutomataAction extends Action implements
		ISelectionListener, IWorkbenchAction {

	private Shell shell;
	private Text messageText;
	private String path;
	private Button generateButton;
	private Button cancelButton;
	private Integer numberOfState = 0;
	private Integer numberOfTransfer = 0;
	private Text stateText;
	private Text transferText;

	private Integer number;

	public static final String ID = "cn.cstv.wspscm.actions.GenerateCertainAutomataAction";

	private IWorkbenchWindow window;

	public GenerateCertainAutomataAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("Generate automata");
		setToolTipText("Generate certain automata");
		setImageDescriptor(IImageKeys.getImageDescriptor(IImageKeys.GENEAUTO));
		this.window.getSelectionService().addSelectionListener(this);
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

		shell = new Shell(window.getShell(), SWT.MIN | SWT.MAX | SWT.CLOSE);
		shell.setSize(503, 259);
		shell.setText("Generate Automata");
		shell.setImage(IImageKeys.getImage(IImageKeys.GENEAUTO));

		final Composite composite = new Composite(shell, SWT.NONE);
		composite.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_WHITE));
		composite.setBounds(0, 0, 494, 64);

		final Label inputMessageRuleLabel = new Label(composite, SWT.NONE);
		inputMessageRuleLabel.setBackground(Display.getCurrent()
				.getSystemColor(SWT.COLOR_WHITE));
		inputMessageRuleLabel.setText("Input Messages for generating automata");
		inputMessageRuleLabel.setBounds(20, 36, 313, 18);

		final Label generateMessageSequenceLabel = new Label(composite,
				SWT.NONE);
		generateMessageSequenceLabel.setBackground(Display.getCurrent()
				.getSystemColor(SWT.COLOR_WHITE));
		generateMessageSequenceLabel.setFont(SWTResourceManager.getFont("", 12,
				SWT.NONE));
		generateMessageSequenceLabel.setText("Generate Automata");
		generateMessageSequenceLabel.setBounds(10, 10, 250, 20);

		final Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setBounds(0, 70, 494, 130);

		final Label nameLabel = new Label(composite_1, SWT.NONE);
		nameLabel.setAlignment(SWT.RIGHT);
		nameLabel.setText("Messages:");
		nameLabel.setBounds(10, 10, 127, 15);

		messageText = new Text(composite_1, SWT.BORDER);
		messageText.setBounds(143, 7, 284, 18);

		final Button browseButton = new Button(composite_1, SWT.NONE);
		browseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				path = OpenFileDialoge();
				if (path != null)
					messageText.setText(path);
//				if (numberOfState > 0 && numberOfTransfer > 0) {
//					generateButton.setEnabled(true);
//				}
			}
		});
		browseButton.setText("Browse...");
		browseButton.setBounds(433, 5, 71, 22);

		final Label messageNumberLabel = new Label(composite_1, SWT.NONE);
		messageNumberLabel.setAlignment(SWT.RIGHT);
		messageNumberLabel.setText("Number Of State:");
		messageNumberLabel.setBounds(10, 43, 127, 15);

		stateText = new Text(composite_1, SWT.BORDER);
		stateText.setBounds(143, 40, 107, 18);
		addLengthValidateToolTip(stateText);

		final Label label = new Label(composite_1, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		label.setText("Number Of Transfer:");
		label.setBounds(10, 76, 127, 12);

		transferText = new Text(composite_1, SWT.BORDER);
		transferText.setBounds(143, 71, 107, 18);
		addWidthValidateToolTip(transferText);

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
				//计算执行时间 
				long startTime = System.currentTimeMillis();   //获取开始时间 

				List<String> messageList = new ArrayList<String>();
				List<String> automataList = new ArrayList<String>();

				File tmpFile = new File(path);
				try {
					final RandomAccessFile randomFile = new RandomAccessFile(
							tmpFile, "r");
					String tmp = "";
					messageList.clear();
					while ((tmp = randomFile.readLine()) != null) {
						messageList.add(tmp);
					}
					GenerateAutomataFile(messageList, numberOfState,
							numberOfTransfer, automataList);

				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				String automataFileName = path.substring(0, path
						.lastIndexOf("."))
						+ ".auto";
				IImageKeys.automataFilesName.clear();
				IImageKeys.automataFilesName.add(automataFileName);
				List<String> tmpList=new ArrayList<String>();
				tmpList.add(automataFileName);
				try {
					((AutomataView)window.getActivePage().showView(AutomataView.ID)).resetAutomataView(1,tmpList);
				} catch (PartInitException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				PrintWriter out = null;
				try {
					out = new PrintWriter(new FileWriter(
							IImageKeys.automataFilesName.get(0)));
					for (int i = 0; i < automataList.size(); i++) {
						out.println(automataList.get(i));
					}
					out.close();
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}

				if (IImageKeys.automataFilesName.size() == 1) {
					InputStream is;
					try {
						is = new FileInputStream(IImageKeys.automataFilesName.get(0));

						// TODO Auto-generated catch block

						byte[] inputByte = new byte[is.available()];

						is.read(inputByte);
						String contentString = new String(inputByte);
						IViewPart view = window.getActivePage().showView(
								AutomataView.ID);
						Text text = ((AutomataView) view).getTextArray().get(0);
						text.setText(contentString);

						is.close();
					} catch (FileNotFoundException e4) {
						e4.printStackTrace();
					} catch (IOException e5) {
						// TODO Auto-generated catch block
						e5.printStackTrace();
					} catch (PartInitException e6) {
						// TODO Auto-generated catch block
						e6.printStackTrace();
					}
				}
				long endTime=System.currentTimeMillis(); //获取结束时间  
				System.out.println("Time for generating +"+ automataList.size() + " automata is "+( endTime - startTime )+"ms"); 


				shell.close();
			}

		});
		generateButton.setText("Finish");
		generateButton.setBounds(380, 206, 48, 22);

		shell.open();
		shell.layout();
	}

	private void GenerateAutomataFile(List<String> messageList,
			Integer numberOfState, Integer numberOfTransfer,
			List<String> automataList) {
		automataList.clear();

		int messageLength = messageList.size();

		String startStateName = "";

		String endStateName = "";

		String messageString = "";

		String firstCondition = "(x:=0)";

		String restCondition = "(x<4)";

		int last = 0, current = 0;

		if (messageList.size() > numberOfState) {
			return;
		}

		if (numberOfState > numberOfTransfer) {
			return;
		}

		int step = numberOfState / messageLength;
		int span = numberOfTransfer / numberOfState;

		last = 0;
		startStateName = "s" + last;
		int randomInteger;

		int totalMessageNumber = 0;
		int messageIndex = 0;
		String message = "";
		
		List<Integer> rightPath = new ArrayList<Integer>();
		List<Integer> acceptIndex = new ArrayList<Integer>();
		List<Integer> errorIndex = new ArrayList<Integer>();
		
		rightPath.add(0);
		for( int i = 0 ; i < messageLength ; i++)
		{
			randomInteger = new Random().nextInt(step);
			current = i * step + (randomInteger > 0 ? randomInteger : 1);
			rightPath.add(current);
			
			if( i == messageLength - 1)
			{
				acceptIndex.add(current);
				
				for( int j = current + 1; j < numberOfState; j++ )
				{
					errorIndex.add(j);
				}
				
				if(current+1 == numberOfState)
				{
					errorIndex.add(current - 1);
				}
			}
			
		}

		for (int i = 0; i < messageLength; i++) {
			current = rightPath.get(i+1);
			messageIndex = 0;
			message = messageList.get(i);
			
			startStateName = "s" + last;

			if (i == messageLength - 1) {
				endStateName = "s" + current + "(Accept)";
			} else {
				endStateName = "s" + current;

			}
			if (last == 0) {
				messageString = "(" + startStateName + "," + message + ","
						+ firstCondition + "," + endStateName + ")";
			} else {
				messageString = "(" + startStateName + "," + message + ","
						+ restCondition + "," + endStateName + ")";
			}
			if (totalMessageNumber < numberOfTransfer) {
				automataList.add(messageString);
				totalMessageNumber++;
			}

			for (int j = last; j < current; j++) {
				Boolean isErrorOrTrue = false;
				for( int k = 0; k < acceptIndex.size();k++)
				{
					if( j == acceptIndex.get(k))
					{
						isErrorOrTrue = true;
					}
				}
				for( int k = 0; k < errorIndex.size();k++)
				{
					if( j == errorIndex.get(k))
					{
						isErrorOrTrue = true;
					}
				}
				
				if(isErrorOrTrue)
				{
					continue;
				}
				
				startStateName = "s" + j;
				for (int k = 0; k < span; k++) {
					randomInteger = j + new Random().nextInt(numberOfState - j);
					Boolean isAccept = false;
					for(int s= 0; s < acceptIndex.size();s++)
					{
						if(randomInteger == acceptIndex.get(s))
						{
							isAccept = true;
						}
					}
					
					if(isAccept)
					{
						continue;
					}
					
					Boolean isError = false;
					for(int s= 0; s < errorIndex.size();s++)
					{
						if(randomInteger == errorIndex.get(s))
						{
							isError = true;
						}
					}
					if( isError )
					{
						endStateName = "s" + randomInteger+"(Error)";							
					}
					else
					{
						endStateName = "s" + randomInteger;												
					}
					String insertMessage = message.substring(0, message
							.lastIndexOf('('))
							+ messageIndex
							+ message.substring(message.lastIndexOf('('));
					messageIndex += 1;

					if (last == 0) {
						messageString = "(" + startStateName + ","
								+ insertMessage + "," + firstCondition + ","
								+ endStateName + ")";
					} else {
						messageString = "(" + startStateName + ","
								+ insertMessage + "," + restCondition + ","
								+ endStateName + ")";
					}
					if (totalMessageNumber < numberOfTransfer) {
						automataList.add(messageString);
						totalMessageNumber++;
					}
				}
			}
			last = current;

		}

//		for (int j = 0; j < automataList.size(); j++) {
//			System.out.println(automataList.get(j));
//		}

		// System.out.println(statename);

	}

	private void addWidthValidateToolTip(final Text text) {
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
				try {
					numberOfTransfer = Integer.parseInt(text.getText());
					if (numberOfTransfer < 0) {
						errorInfoTip
								.setMessage("Error: Please input Integer value >0");
						errorInfoTip.setVisible(true);
						generateButton.setEnabled(false);
					} else {
						if (!path.isEmpty()) {
							generateButton.setEnabled(true);
						}
					}

				} catch (java.lang.NumberFormatException e1) {
					// return "Error: Please input numerical value";
					errorInfoTip
							.setMessage("Error: Please input Integer value >0");
					errorInfoTip.setVisible(true);
					generateButton.setEnabled(false);
				}
			}

		});
	}

	/**
	 * @param numberText2
	 */
	private void addLengthValidateToolTip(final Text text) {
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
				try {

					numberOfState = Integer.parseInt(text.getText());
					if (numberOfState < 0) {
						errorInfoTip
								.setMessage("Error: Please input Integer value >0");
						errorInfoTip.setVisible(true);
						generateButton.setEnabled(false);
					} else {

						if (!path.isEmpty()) {
							generateButton.setEnabled(true);
						}
					}
				} catch (java.lang.NumberFormatException e1) {
					// return "Error: Please input numerical value";
					errorInfoTip
							.setMessage("Error: Please input Integer value");
					errorInfoTip.setVisible(true);
					generateButton.setEnabled(false);

				}

			}

		});
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

		dlg.setFilterNames(new String[] { "Messages(*.message)", "*.*" });
		dlg.setFilterExtensions(new String[] { "*.message", "*.*" });
		String fileName = dlg.open();
		if (!(fileName.endsWith(".message"))) {
			MessageDialog.openWarning(null, "Warning!",
					"you have choose wrong file");
			return null;
		} else {
			return fileName;
		}
	}

}
