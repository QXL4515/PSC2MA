package cn.cstv.wspscm.actions;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import cn.cstv.wspscm.views.MessageView;
import cn.cstv.wspscm.views.SWTResourceManager;


public class InsertTriggerAction extends Action implements ISelectionListener,
		IWorkbenchAction {

	private Text timeText;
	public static final String ID = "cn.cstv.wspscm.actions.InsertTriggerAction";

	private IWorkbenchWindow window;
	private Shell shell;
	private Button finishButton;
	private int timeInterval;

	public InsertTriggerAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("Trigger");
		setToolTipText("Insert trigger message");
		setImageDescriptor(IImageKeys.getImageDescriptor(IImageKeys.TRIGGER));
		window.getSelectionService().addSelectionListener(this);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		window.getSelectionService().removeSelectionListener(this);
	}

	@Override
	public void run() {
		if (IImageKeys.messageLogFileName.isEmpty()) {
			MessageDialog.openWarning(null, "No Message File Warning!",
					"Message File don't exist!");
		} else {

		}
		shell = new Shell(window.getShell(), SWT.MIN | SWT.MAX | SWT.CLOSE);
		shell.setSize(503, 191);
		shell.setText("Insert trigger Message");
		shell.setImage(IImageKeys.getImage(IImageKeys.TRIGGER));
		shell.open();

		final Composite composite = new Composite(shell, SWT.NONE);
		composite.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_WHITE));
		composite.setBounds(0, 0, 494, 64);

		final Label insertTriggerMessageLabel = new Label(composite, SWT.NONE);
		insertTriggerMessageLabel.setFont(SWTResourceManager.getFont("", 12,
				SWT.NONE));
		insertTriggerMessageLabel.setBackground(Display.getCurrent()
				.getSystemColor(SWT.COLOR_WHITE));
		insertTriggerMessageLabel.setText("Insert Trigger Message");
		insertTriggerMessageLabel.setBounds(10, 10, 186, 24);

		final Label insertTriggerMessageLabel_1 = new Label(composite, SWT.NONE);
		insertTriggerMessageLabel_1.setBackground(Display.getCurrent()
				.getSystemColor(SWT.COLOR_WHITE));
		insertTriggerMessageLabel_1
				.setText("Insert trigger message on certain time as defined below:");
		insertTriggerMessageLabel_1.setBounds(20, 40, 342, 12);

		final Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setBounds(0, 70, 494, 64);

		final Label triggerMessageLabel = new Label(composite_1, SWT.NONE);
		triggerMessageLabel.setText("Trigger Message:");
		triggerMessageLabel.setBounds(10, 10, 96, 12);

		final Label monitorLogexitinginvoketiggerLabel = new Label(composite_1,
				SWT.NONE);
		monitorLogexitinginvoketiggerLabel
				.setText("[Monitor LOG]Exiting:[invoke]tigger(begin)");
		monitorLogexitinginvoketiggerLabel.setBounds(114, 10, 318, 12);

		final Label timeIntervalLabel = new Label(composite_1, SWT.NONE);
		timeIntervalLabel.setText("Time interval:");
		timeIntervalLabel.setBounds(10, 39, 96, 12);

		timeText = new Text(composite_1, SWT.BORDER | SWT.RIGHT);
		timeText.setText("0");
		timeText.setBounds(109, 35, 32, 18);
		addNumberValidateToolTip(timeText);

		final Label hourLabel = new Label(composite_1, SWT.NONE);
		hourLabel.setText("h(hour)");
		hourLabel.setBounds(147, 39, 42, 12);

		final Button cannelButton = new Button(shell, SWT.NONE);
		cannelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				shell.dispose();
			}
		});
		cannelButton.setText("Cannel");
		cannelButton.setBounds(436, 140, 48, 22);

		finishButton = new Button(shell, SWT.NONE);
		finishButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String file = IImageKeys.messageLogFileName;
				String newTriggerMessageFile = file.substring(0,file.lastIndexOf("."))+"_trigger"+file.substring(file.lastIndexOf("."));
				IImageKeys.messageLogFileName = newTriggerMessageFile;
				String triggerMessageString = "[Monitor LOG]Exiting:[invoke]tigger(begin)";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				try {
					BufferedReader in = new BufferedReader(
							new FileReader(file));
					PrintWriter out = new PrintWriter(new FileWriter(newTriggerMessageFile));
					String s = "";
					boolean first = true;
					Date firstDate = new Date();
					int NumberOfTimeInterval = 1;
					while ((s = in.readLine()) != null) {
						String messageTime = s.substring(0, s
								.indexOf(" ["));
						if(first){
							first=false;
							out.println(messageTime+" "+triggerMessageString);
							firstDate = sdf.parse(messageTime);
						}
						Date date = sdf.parse(messageTime);
						if((timeInterval!=0)&&(date.getTime()-firstDate.getTime())>NumberOfTimeInterval*timeInterval*60*60*1000){
							out.println(sdf.format(firstDate.getTime()+NumberOfTimeInterval*timeInterval*60*60*1000)+" "+triggerMessageString);
							NumberOfTimeInterval++;
						}
						out.println(s);
					}		
					in.close();
					out.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (ParseException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				
				InputStream is;
				try {
					is = new FileInputStream(newTriggerMessageFile);

					// TODO Auto-generated catch block

					byte[] inputByte = new byte[is.available()];

					is.read(inputByte);
					String contentString = new String(inputByte);
					IViewPart view = window.getActivePage().showView(MessageView.ID);
					((MessageView) view).getText().setText(contentString);
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
				
				shell.close();
			}
		});
		finishButton.setText("Finish");
		finishButton.setBounds(382, 140, 48, 22);
		shell.layout();
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
				try {

					timeInterval = Integer.parseInt(text.getText());
					if (!IImageKeys.LOGFILE.isEmpty()) {
						finishButton.setEnabled(true);
					}
				} catch (java.lang.NumberFormatException e1) {
					// return "Error: Please input numerical value";
					errorInfoTip
							.setMessage("Error: Please input Integer value");
					errorInfoTip.setVisible(true);
					finishButton.setEnabled(false);

				}

			}

		});
	}

}
