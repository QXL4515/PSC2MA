package cn.cstv.wspscm.actions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import cn.cstv.wspscm.IImageKeys;
import cn.cstv.wspscm.editor.PSCMonitorDiagramEditor;
import cn.cstv.wspscm.model.Diagram;
import cn.cstv.wspscm.model.Lifeline;
import cn.cstv.wspscm.model.LineConnection;
import cn.cstv.wspscm.views.AutomataView;

/**
 * @author hp
 * 
 */
public class TransferPscToAutomateAction extends Action implements
		ISelectionListener, IWorkbenchAction {

	public static final String ID = "cn.cstv.wspscm.actions.TransferPscToAutomateAction";

	private IWorkbenchWindow window;

	public TransferPscToAutomateAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("PSC to Automate");
		setToolTipText("Transfer PSC to Automate");
		setImageDescriptor(IImageKeys
				.getImageDescriptor(IImageKeys.TRANSFERPSCTOAUTOMATE));
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
		
		/*
		
		
		//计算执行时间 
		long startTime = System.currentTimeMillis();   //获取开始时间 
		
		// (new AnalyzeMessageSequence()).executeVerification(IImageKeys.
		// messageLogFileName, IImageKeys.automataFileName);
		IViewPart view;
		IFile automataFile = ((IFileEditorInput) window.getActivePage()
				.getActiveEditor().getEditorInput()).getFile();
		String automataFileName = automataFile.getLocation().toString();
				
	//	if(automataFileName.substring(automataFileName.lastIndexOf("."))=="auto"){
		automataFileName = automataFileName.substring(0, automataFileName
				.lastIndexOf(".")) + ".auto";
		
		IImageKeys.automataFilesName.clear();
		IImageKeys.automataFilesName.add(automataFileName);
		PrintWriter out = null;
		Text text = null;
		List<String> automataList = new ArrayList<String>();
		try {
			view = window.getActivePage().showView(AutomataView.ID);
			text = ((AutomataView) view).getTextArray().get(0);
			text.setText("");
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out = new PrintWriter(new FileWriter(automataFileName));

			TreeSet<LineConnection> connections = new TreeSet<LineConnection>(
					new LineConnectionComparator<LineConnection>());
			Diagram diagram = ((PSCMonitorDiagramEditor) window.getActivePage()
					.getActiveEditor()).getDiagram();
			for (int i = 0; i < diagram.getChildren().size(); i++) {
				Lifeline lifeline1 = (Lifeline) diagram.getChildren().get(i);
				for (int j = 0; j < lifeline1.getSourceConnections().size(); j++) {
					connections.add((LineConnection) lifeline1
							.getSourceConnections().get(j));
				}
				for (int j = 0; j < lifeline1.getTargetConnections().size(); j++) {
					connections.add((LineConnection) lifeline1
							.getTargetConnections().get(j));
				}
			}

			Iterator<LineConnection> lineIterator = connections.iterator();
			int stateIndex = 0;
			while (lineIterator.hasNext()) {
				int startStateIndex = stateIndex;
				LineConnection line = lineIterator.next();
				if (line.getPresentConstraintValue().isEmpty()
				&& line.getPresentPastValue().isEmpty()
				&& line.getPresentFutureValue().isEmpty()
						&& line.getPastUnwantedMessageConstraintValue().isEmpty()
						&& line.getFutureUnwantedMessageConstraintValue().isEmpty()
						&& line.getPastWantedChainConstraintValue().isEmpty()
						&& line.getFutureWantedChainConstraintValue().isEmpty()
						&& line.getPastUnwantedChainConstraintValue().isEmpty()
						&& line.getFutureUnwantedChainConstraintValue()
								.isEmpty()) {
					// //////////////////////////////////////////////////////////
					// ////
					// //
					// /////////// == No Constraint
					// ==///////////////////////////////////
					// //////////////////////////////////////////////////////////
					// ////
					// //
					// 局部临时变量，易于处理问题
					String value1 = line.getMessageValue();
					String constraint1 = line.getMessagePara();
					Integer type1 = line.getMessageType();
					String value1Right = value1
							.substring(value1.indexOf(":") + 1);

					// 这里假设value2是只有1个元素的集合,因此valueArraySize=1
					int valueArraySize = 0;
					// 比如b={m1,m2,...,mn},假设n=1，预先这样处理，此为一bug

					if (value1.substring(0, value1.indexOf(":")).equals("e")) {
						// 前缀为e
						// 首先判断PastWantedChainConstraint的constraint是否存在<或<=，
						// 然后才可以进行下一步
						if (constraint1.contains("<")
								|| constraint1.contains("<=")) {
							if (constraint1.contains("<")
									&& !constraint1.contains("<=")) {
								String constraint1Left = constraint1.substring(
										0, constraint1.indexOf("<"));
								String constraint1Right = constraint1
										.substring(constraint1.indexOf("<") + 1);
								
								if (!reset1.isEmpty()) {
									automataList.add("(s" + stateIndex + ",!"
											+ value1Right + ",(" + constraint1
											+ "),s" + stateIndex + ")");

									automataList.add("(s" + stateIndex + ","
											+ value1Right + ",(" + constraint1
											+ ";" + reset1 + "),s"
											+ (stateIndex + 1) + "(Glue))");
									automataList.add("(s" + stateIndex
											+ ",empty,(" + constraint1Left
											+ ">=" + constraint1Right + "),s"
											+ startStateIndex + "(Accept))");
									stateIndex++;

								} else if (reset1.isEmpty()) {
									automataList.add("(s" + stateIndex + ",!"
											+ value1Right + ",(" + constraint1
											+ "),s" + stateIndex + ")");
									automataList.add("(s" + stateIndex + ","
											+ value1Right + ",(" + constraint1
											+ "),s" + (stateIndex + 1)
											+ "(Glue))");
									automataList.add("(s" + stateIndex
											+ ",empty,(" + constraint1Left
											+ ">=" + constraint1Right + "),s"
											+ startStateIndex + "(Accept))");
									stateIndex++;
								}
							} else if (constraint1.contains("<=")) {
								String constraint1Left = constraint1.substring(
										0, constraint1.indexOf("<="));
								String constraint1Right = constraint1
										.substring(constraint1.indexOf("<=") + 1);
								if (!reset1.isEmpty()) {
									automataList.add("(s" + stateIndex + ",!"
											+ value1Right + ",(" + constraint1
											+ "),s" + stateIndex + ")");
									automataList.add("(s" + stateIndex + ","
											+ value1Right + ",(" + constraint1
											+ ";" + reset1 + "),s"
											+ (stateIndex + 1) + "(Glue))");
									automataList.add("(s" + stateIndex
											+ ",empty,(" + constraint1Left
											+ ">" + constraint1Right + "),s"
											+ startStateIndex + "(Accept))");
									stateIndex++;
								} else if (reset1.isEmpty()) {
									automataList.add("(s" + stateIndex + ",!"
											+ value1Right + ",(" + constraint1
											+ "),s" + stateIndex + ")");
									automataList.add("(s" + stateIndex + ","
											+ value1Right + ",(" + constraint1
											+ "),s" + (stateIndex + 1)
											+ "(Glue))");
									automataList.add("(s" + stateIndex
											+ ",empty,(" + constraint1Left
											+ ">" + constraint1Right + "),s"
											+ startStateIndex + "(Accept))");
									stateIndex++;
								}
							}
						} else if (constraint1.isEmpty()) {
							automataList.add("(s" + stateIndex + ","
									+ value1Right + ",(" + reset1 + "),s"
									+ (stateIndex + 1) + "(Glue))");
							stateIndex++;
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}
					} else if (value1.substring(0, value1.indexOf(":")).equals(
							"r")) {
						// 前缀为r
						// 首先判断PastWantedChainConstraint的constraint是否存在<或<=，
						// 然后才可以进行下一步
						if (constraint1.contains("<")
								|| constraint1.contains("<=")) {
							if (constraint1.contains("<")
									&& !constraint1.contains("<=")) {
								String constraint1Left = constraint1.substring(
										0, constraint1.indexOf("<"));
								String constraint1Right = constraint1
										.substring(constraint1.indexOf("<") + 1);
								if (!reset1.isEmpty()) {
									automataList.add("(s" + stateIndex + ",!"
											+ value1Right + ",(" + constraint1
											+ "),s" + stateIndex + ")");

									automataList.add("(s" + stateIndex + ","
											+ value1Right + ",(" + constraint1
											+ ";" + reset1 + "),s"
											+ (stateIndex + 1) + "(Glue))");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">="
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;

								} else if (reset1.isEmpty()) {
									automataList.add("(s" + stateIndex + ",!"
											+ value1Right + ",(" + constraint1
											+ "),s" + stateIndex + ")");
									automataList.add("(s" + stateIndex + ","
											+ value1Right + ",(" + constraint1
											+ "),s" + (stateIndex + 1)
											+ "(Glue))");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">="
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								}
							} else if (constraint1.contains("<=")) {
								String constraint1Left = constraint1.substring(
										0, constraint1.indexOf("<="));
								String constraint1Right = constraint1
										.substring(constraint1.indexOf("<=") + 1);
								if (!reset1.isEmpty()) {
									automataList.add("(s" + stateIndex + ",!"
											+ value1Right + ",(" + constraint1
											+ "),s" + stateIndex + ")");
									automataList.add("(s" + stateIndex + ","
											+ value1Right + ",(" + constraint1
											+ ";" + reset1 + "),s"
											+ (stateIndex + 1) + "(Glue))");

									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">"
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								} else if (reset1.isEmpty()) {
									automataList.add("(s" + stateIndex + ",!"
											+ value1Right + ",(" + constraint1
											+ "),s" + stateIndex + ")");
									automataList.add("(s" + stateIndex + ","
											+ value1Right + ",(" + constraint1
											+ "),s" + (stateIndex + 1)
											+ "(Glue))");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">"
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								}
							}
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}
					} else if (value1.substring(0, value1.indexOf(":")).equals(
							"f")) {
						// 前缀为f
						if (constraint1.contains("<")
								|| constraint1.contains("<=")) {
							if (constraint1.contains("<")
									&& !constraint1.contains("<=")) {
								String constraint1Left = constraint1.substring(
										0, constraint1.indexOf("<"));
								String constraint1Right = constraint1
										.substring(constraint1.indexOf("<") + 1);
								if (!reset1.isEmpty()) {
									automataList.add("(s" + stateIndex + ",!"
											+ value1Right + ",(" + constraint1
											+ "),s" + stateIndex + ")");

									automataList.add("(s" + stateIndex + ","
											+ value1Right + ",(" + constraint1
											+ ";" + reset1 + "),s"
											+ (stateIndex + 1) + "(Error))");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">="
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Glue))");
									stateIndex++;

								} else if (reset1.isEmpty()) {
									automataList.add("(s" + stateIndex + ",!"
											+ value1Right + ",(" + constraint1
											+ "),s" + stateIndex + ")");
									automataList.add("(s" + stateIndex + ","
											+ value1Right + ",(" + constraint1
											+ "),s" + (stateIndex + 1)
											+ "(Error))");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">="
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Glue))");
									stateIndex++;
								}
							} else if (constraint1.contains("<=")) {
								String constraint1Left = constraint1.substring(
										0, constraint1.indexOf("<="));
								String constraint1Right = constraint1
										.substring(constraint1.indexOf("<=") + 1);
								if (!reset1.isEmpty()) {
									automataList.add("(s" + stateIndex + ",!"
											+ value1Right + ",(" + constraint1
											+ "),s" + stateIndex + ")");
									automataList.add("(s" + stateIndex + ","
											+ value1Right + ",(" + constraint1
											+ ";" + reset1 + "),s"
											+ (stateIndex + 1) + "(Error))");

									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">"
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Glue))");
									stateIndex++;
								} else if (reset1.isEmpty()) {
									automataList.add("(s" + stateIndex + ",!"
											+ value1Right + ",(" + constraint1
											+ "),s" + stateIndex + ")");
									automataList.add("(s" + stateIndex + ","
											+ value1Right + ",(" + constraint1
											+ "),s" + (stateIndex + 1)
											+ "(Error))");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">"
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Glue))");
									stateIndex++;
								}
							}
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}
					} else {
						MessageDialog.openError(window.getShell(), "Error",
								"timed constraint error!");
					}

					// //////////////////////////////////////////////////////////
					// ////
					// //
					// /////////// == End:No Constraint ==/////////////////////
					// //////////////////////////////////////////////////////////
					// ////
					// //
				} else if (!line.getPresentConstraintValue().isEmpty()
						&& line.getPastUnwantedMessageConstraintValue().isEmpty()
						&& line.getFutureUnwantedMessageConstraintValue().isEmpty()
						&& line.getPresentPastValue().isEmpty()
						&& line.getPresentFutureValue().isEmpty()
						&& line.getPastWantedChainConstraintValue().isEmpty()
						&& line.getFutureWantedChainConstraintValue().isEmpty()
						&& line.getPastUnwantedChainConstraintValue().isEmpty()
						&& line.getFutureUnwantedChainConstraintValue()
								.isEmpty()) {
					// //////////////////////////////////////////////////////////
					// ////
					// //
					// /////////// == PresentConstraint
					// ==///////////////////////////////////
					// //////////////////////////////////////////////////////////
					// ////
					// //
					// 局部临时变量，易于处理问题
					String value1 = line.getMessageValue();
					String value2 = line.getPresentConstraintValue();
					String constraint1 = line.getMessageConstraint();
					String constraint2 = line.getPresentConstraintConstraint();
					String reset1 = line.getMessageReset();
					String reset2 = line.getPresentConstraintReset();
					String value1Right = value1
							.substring(value1.indexOf(":") + 1);
					String value2Contain = value2.substring(
							value2.indexOf("{") + 1, value2.lastIndexOf("}"));

					// 这里假设value2是只有1个元素的集合,因此valueArraySize=1
					int valueArraySize = 0;
					// 比如b={m1,m2,...,mn},假设n=1，预先这样处理，此为一bug

					if (!value2Contain.contains(",")) {
						// 表明{}内所包含的只有1个元素。多个元素以,隔开
						if ((constraint1.equals(constraint2) && reset1
								.equals(reset2))
								|| (constraint1.isEmpty()
										&& constraint2.isEmpty() && reset1
										.equals(reset2))
								|| (constraint1.equals(constraint2)
										&& reset1.isEmpty() && reset2.isEmpty())
								|| (constraint1.isEmpty()
										&& constraint2.isEmpty()
										&& reset1.isEmpty() && reset2.isEmpty())) {
							if (value1.substring(0, value1.indexOf(":"))
									.equals("e")) {
								// 前缀为e
								// 首先判断PastWantedChainConstraint的constraint是否存在<或
								// <=，
								// 然后才可以进行下一步
								if (constraint2.contains("<")
										|| constraint2.contains("<=")) {
									if (constraint2.contains("<")
											&& !constraint2.contains("<=")) {
										String constraint2Left = constraint2
												.substring(0, constraint2
														.indexOf("<"));
										String constraint2Right = constraint2
												.substring(constraint2
														.indexOf("<") + 1);
										if (!reset2.isEmpty()) {
											automataList.add("(s" + stateIndex
													+ ",!" + value1Right + ",("
													+ constraint2 + "),s"
													+ stateIndex + ")");

											automataList.add("(s" + stateIndex
													+ "," + value2Contain + "&"
													+ value1Right + ",("
													+ constraint2 + ";"
													+ reset2 + "),s"
													+ (stateIndex + 1)
													+ "(Glue))");
											automataList.add("(s" + stateIndex
													+ ",empty,("
													+ constraint2Left + ">="
													+ constraint2Right + "),s"
													+ startStateIndex
													+ "(Accept))");
											stateIndex++;

										} else if (reset2.isEmpty()) {
											automataList.add("(s" + stateIndex
													+ ",!" + value1Right + ",("
													+ constraint2 + "),s"
													+ stateIndex + ")");
											automataList.add("(s" + stateIndex
													+ "," + value2Contain + "&"
													+ value1Right + ",("
													+ constraint2 + "),s"
													+ (stateIndex + 1)
													+ "(Glue))");
											automataList.add("(s" + stateIndex
													+ ",empty,("
													+ constraint2Left + ">="
													+ constraint2Right + "),s"
													+ startStateIndex
													+ "(Accept))");
											stateIndex++;
										}
									} else if (constraint2.contains("<=")) {
										String constraint2Left = constraint2
												.substring(0, constraint2
														.indexOf("<="));
										String constraint2Right = constraint2
												.substring(constraint2
														.indexOf("<=") + 2);
										if (!reset2.isEmpty()) {
											automataList.add("(s" + stateIndex
													+ ",!" + value1Right + ",("
													+ constraint2 + "),s"
													+ stateIndex + ")");
											automataList.add("(s" + stateIndex
													+ "," + value2Contain + "&"
													+ value1Right + ",("
													+ constraint2 + ";"
													+ reset2 + "),s"
													+ (stateIndex + 1)
													+ "(Glue))");

											automataList.add("(s" + stateIndex
													+ ",empty,("
													+ constraint2Left + ">"
													+ constraint2Right + "),s"
													+ startStateIndex
													+ "(Accept))");
											stateIndex++;
										} else if (reset2.isEmpty()) {
											automataList.add("(s" + stateIndex
													+ ",!" + value1Right + ",("
													+ constraint2 + "),s"
													+ stateIndex + ")");
											automataList.add("(s" + stateIndex
													+ "," + value2Contain + "&"
													+ value1Right + ",("
													+ constraint2 + "),s"
													+ (stateIndex + 1)
													+ "(Glue))");
											automataList.add("(s" + stateIndex
													+ ",empty,("
													+ constraint2Left + ">"
													+ constraint2Right + "),s"
													+ startStateIndex
													+ "(Accept))");
											stateIndex++;
										}
									}
								} else if (constraint2.isEmpty()) {

									automataList.add("(s" + stateIndex + ","
											+ value2Contain + "&" + value1Right
											+ ",(" + reset2 + "),s"
											+ (stateIndex + 1) + "(Glue))");
									stateIndex++;
								} else {
									MessageDialog.openError(window.getShell(),
											"Error", "timed constraint error!");
								}
							} else if (value1.substring(0, value1.indexOf(":"))
									.equals("r")) {
								// 前缀为r
								// 首先判断PastWantedChainConstraint的constraint是否存在<或
								// <=，
								// 然后才可以进行下一步
								if (constraint2.contains("<")
										|| constraint2.contains("<=")) {
									if (constraint2.contains("<")
											&& !constraint2.contains("<=")) {
										String constraint2Left = constraint2
												.substring(0, constraint2
														.indexOf("<"));
										String constraint2Right = constraint2
												.substring(constraint2
														.indexOf("<") + 1);
										if (!reset2.isEmpty()) {
											automataList.add("(s" + stateIndex
													+ ",!" + value1Right + ",("
													+ constraint2 + "),s"
													+ stateIndex + ")");

											automataList.add("(s" + stateIndex
													+ "," + value2Contain + "&"
													+ value1Right + ",("
													+ constraint2 + ";"
													+ reset2 + "),s"
													+ (stateIndex + 1)
													+ "(Glue))");
											automataList
													.add("(s"
															+ stateIndex
															+ ",empty,("
															+ constraint2Left
															+ ">="
															+ constraint2Right
															+ "),s"
															+ (startStateIndex
																	+ valueArraySize + 2)
															+ "(Error))");
											stateIndex++;

										} else if (reset2.isEmpty()) {
											automataList.add("(s" + stateIndex
													+ ",!" + value1Right + ",("
													+ constraint2 + "),s"
													+ stateIndex + ")");
											automataList.add("(s" + stateIndex
													+ "," + value2Contain + "&"
													+ value1Right + ",("
													+ constraint2 + "),s"
													+ (stateIndex + 1)
													+ "(Glue))");
											automataList
													.add("(s"
															+ stateIndex
															+ ",empty,("
															+ constraint2Left
															+ ">="
															+ constraint2Right
															+ "),s"
															+ (startStateIndex
																	+ valueArraySize + 2)
															+ "(Error))");
											stateIndex++;
										}
									} else if (constraint2.contains("<=")) {
										String constraint2Left = constraint2
												.substring(0, constraint2
														.indexOf("<="));
										String constraint2Right = constraint2
												.substring(constraint2
														.indexOf("<=") + 2);
										if (!reset2.isEmpty()) {
											automataList.add("(s" + stateIndex
													+ ",!" + value1Right + ",("
													+ constraint2 + "),s"
													+ stateIndex + ")");
											automataList.add("(s" + stateIndex
													+ "," + value2Contain + "&"
													+ value1Right + ",("
													+ constraint2 + ";"
													+ reset2 + "),s"
													+ (stateIndex + 1)
													+ "(Glue))");

											automataList
													.add("(s"
															+ stateIndex
															+ ",empty,("
															+ constraint2Left
															+ ">"
															+ constraint2Right
															+ "),s"
															+ (startStateIndex
																	+ valueArraySize + 2)
															+ "(Error))");
											stateIndex++;
										} else if (reset2.isEmpty()) {
											automataList.add("(s" + stateIndex
													+ ",!" + value1Right + ",("
													+ constraint2 + "),s"
													+ stateIndex + ")");
											automataList.add("(s" + stateIndex
													+ "," + value2Contain + "&"
													+ value1Right + ",("
													+ constraint2 + "),s"
													+ (stateIndex + 1)
													+ "(Glue))");
											automataList
													.add("(s"
															+ stateIndex
															+ ",empty,("
															+ constraint2Left
															+ ">"
															+ constraint2Right
															+ "),s"
															+ (startStateIndex
																	+ valueArraySize + 2)
															+ "(Error))");
											stateIndex++;
										}
									}
								} else {
									MessageDialog.openError(window.getShell(),
											"Error", "timed constraint error!");
								}
							} else if (value1.substring(0, value1.indexOf(":"))
									.equals("f")) {
								// 前缀为f
								if (constraint2.contains("<")
										|| constraint2.contains("<=")) {
									if (constraint2.contains("<")
											&& !constraint2.contains("<=")) {
										String constraint2Left = constraint2
												.substring(0, constraint2
														.indexOf("<"));
										String constraint2Right = constraint2
												.substring(constraint2
														.indexOf("<") + 1);
										if (!reset2.isEmpty()) {
											automataList.add("(s" + stateIndex
													+ ",!" + value1Right + ",("
													+ constraint2 + "),s"
													+ stateIndex + ")");

											automataList.add("(s" + stateIndex
													+ "," + value2Contain + "&"
													+ value1Right + ",("
													+ constraint2 + ";"
													+ reset2 + "),s"
													+ (stateIndex + 1)
													+ "(Error))");
											automataList
													.add("(s"
															+ stateIndex
															+ ",empty,("
															+ constraint2Left
															+ ">="
															+ constraint2Right
															+ "),s"
															+ (startStateIndex
																	+ valueArraySize + 2)
															+ "(Glue))");
											stateIndex++;

										} else if (reset2.isEmpty()) {
											automataList.add("(s" + stateIndex
													+ ",!" + value1Right + ",("
													+ constraint2 + "),s"
													+ stateIndex + ")");
											automataList.add("(s" + stateIndex
													+ "," + value2Contain + "&"
													+ value1Right + ",("
													+ constraint2 + "),s"
													+ (stateIndex + 1)
													+ "(Error))");
											automataList
													.add("(s"
															+ stateIndex
															+ ",empty,("
															+ constraint2Left
															+ ">="
															+ constraint2Right
															+ "),s"
															+ (startStateIndex
																	+ valueArraySize + 2)
															+ "(Glue))");
											stateIndex++;
										}
									} else if (constraint2.contains("<=")) {
										String constraint2Left = constraint2
												.substring(0, constraint2
														.indexOf("<="));
										String constraint2Right = constraint2
												.substring(constraint2
														.indexOf("<=") + 2);
										if (!reset2.isEmpty()) {
											automataList.add("(s" + stateIndex
													+ ",!" + value1Right + ",("
													+ constraint2 + "),s"
													+ stateIndex + ")");
											automataList.add("(s" + stateIndex
													+ "," + value2Contain + "&"
													+ value1Right + ",("
													+ constraint2 + ";"
													+ reset2 + "),s"
													+ (stateIndex + 1)
													+ "(Error))");

											automataList
													.add("(s"
															+ stateIndex
															+ ",empty,("
															+ constraint2Left
															+ ">"
															+ constraint2Right
															+ "),s"
															+ (startStateIndex
																	+ valueArraySize + 2)
															+ "(Glue))");
											stateIndex++;
										} else if (reset2.isEmpty()) {
											automataList.add("(s" + stateIndex
													+ ",!" + value1Right + ",("
													+ constraint2 + "),s"
													+ stateIndex + ")");
											automataList.add("(s" + stateIndex
													+ "," + value2Contain + "&"
													+ value1Right + ",("
													+ constraint2 + "),s"
													+ (stateIndex + 1)
													+ "(Error))");
											automataList
													.add("(s"
															+ stateIndex
															+ ",empty,("
															+ constraint2Left
															+ ">"
															+ constraint2Right
															+ "),s"
															+ (startStateIndex
																	+ valueArraySize + 2)
															+ "(Glue))");
											stateIndex++;
										}
									}
								} else {
									MessageDialog.openError(window.getShell(),
											"Error", "timed constraint error!");
								}
							} else {
								MessageDialog.openError(window.getShell(),
										"Error", "timed constraint error!");
							}
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}

					} else {
						MessageDialog.openError(window.getShell(), "Error",
								"timed constraint error!");
					}

					// //////////////////////////////////////////////////////////
					// ////
					// //
					// /////////// == End:PresentConstraint
					// ==/////////////////////
					// //////////////////////////////////////////////////////////
					// ////
					// //
				} else if (line.getPresentConstraintValue().isEmpty()
						&& !line.getPastUnwantedMessageConstraintValue().isEmpty()
						&& line.getFutureUnwantedMessageConstraintValue().isEmpty()
						&& line.getPresentPastValue().isEmpty()
						&& line.getPresentFutureValue().isEmpty()
						&& line.getPastWantedChainConstraintValue().isEmpty()
						&& line.getFutureWantedChainConstraintValue().isEmpty()
						&& line.getPastUnwantedChainConstraintValue().isEmpty()
						&& line.getFutureUnwantedChainConstraintValue()
								.isEmpty()) {
					// //////////////////////////////////////////////////////////
					// ////
					// //
					// /////////// == PastBooleanConstraint
					// ==/////////////////////////
					// //////////////////////////////////////////////////////////
					// ////
					// //
					// 局部临时变量，易于处理问题
					String value1 = line.getMessageValue();
					String constraint1 = line.getMessageConstraint();
					String reset1 = line.getMessageReset();
					String value2 = line.getPastUnwantedMessageConstraintValue();
					String constraint2 = line
							.getPastUnwantedMessageConstraintConstraint();
					String reset2 = line.getPastUnwantedMessageConstraintReset();
					String value1Right = value1
							.substring(value1.indexOf(":") + 1);
					String value2Contain = value2.substring(
							value2.indexOf("{") + 1, value2.lastIndexOf("}"));

					// 这里假设value2是只有1个元素的集合,因此valueArraySize=1
					int valueArraySize = 1;
					// 比如b={m1,m2,...,mn},假设n=1，预先这样处理，此为一bug

					if (!value2Contain.contains(",")) {
						// 表明{}内所包含的只有1个元素。多个元素以,隔开
						if (value1.substring(0, value1.indexOf(":"))
								.equals("e")) {
							// 前缀为e
							// 首先判断PastWantedChainConstraint的constraint是否存在<或<=，
							// 然后才可以进行下一步
							if (constraint2.contains("<")
									|| constraint2.contains("<=")) {
								if (constraint2.contains("<")
										&& !constraint2.contains("<=")) {
									String constraint2Left = constraint2
											.substring(0, constraint2
													.indexOf("<"));
									String constraint2Right = constraint2
											.substring(constraint2.indexOf("<") + 1);
									if (!reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");

										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + ";" + reset2
												+ "),s" + (stateIndex + 1)
												+ ")");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">=" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;

									} else if (reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + "),s"
												+ (stateIndex + 1) + ")");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">=" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									}
								} else if (constraint2.contains("<=")) {
									String constraint2Left = constraint2
											.substring(0, constraint2
													.indexOf("<="));
									String constraint2Right = constraint2
											.substring(constraint2
													.indexOf("<=") + 2);
									if (!reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + ";" + reset2
												+ "),s" + (stateIndex + 1)
												+ ")");

										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									} else if (reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + "),s"
												+ (stateIndex + 1) + ")");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									}
								}
							} else if (constraint2.isEmpty()) {
								automataList.add("(s" + stateIndex + ","
										+ value2Contain + ",(" + reset2 + "),s"
										+ (stateIndex + 1) + ")");
								stateIndex++;
							} else {
								MessageDialog.openError(window.getShell(),
										"Error", "timed constraint error!");
							}
							// 然后message
							if (constraint1.contains("<")
									|| constraint1.contains("<=")) {
								if (constraint1.contains("<")
										&& !constraint1.contains("<=")) {
									String constraint1Left = constraint1
											.substring(0, constraint1
													.indexOf("<"));
									String constraint1Right = constraint1
											.substring(constraint1.indexOf("<") + 1);
									if (!reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + ",("
												+ constraint1 + ";" + reset1
												+ "),s" + (stateIndex + 1)
												+ "(Glue))");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint1Left
												+ ">=" + constraint1Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									} else if (reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + ",("
												+ constraint1 + "),s"
												+ (stateIndex + 1) + "(Glue))");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint1Left
												+ ">=" + constraint1Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									}
								} else if (constraint1.contains("<=")) {
									String constraint1Left = constraint1
											.substring(0, constraint1
													.indexOf("<="));
									String constraint1Right = constraint1
											.substring(constraint1
													.indexOf("<=") + 2);
									if (!reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + ",("
												+ constraint1 + ";" + reset1
												+ "),s" + (stateIndex + 1)
												+ "(Glue))");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint1Left
												+ ">" + constraint1Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									} else if (reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + ",("
												+ constraint1 + "),s"
												+ (stateIndex + 1) + "(Glue))");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint1Left
												+ ">" + constraint1Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									}
								}
							} else {
								MessageDialog.openError(window.getShell(),
										"Error", "timed constraint error!");
							}
						} else if (value1.substring(0, value1.indexOf(":"))
								.equals("r")) {
							// 前缀为r
							// 首先判断PastWantedChainConstraint的constraint是否存在<或<=，
							// 然后才可以进行下一步
							if (constraint2.contains("<")
									|| constraint2.contains("<=")) {
								if (constraint2.contains("<")
										&& !constraint2.contains("<=")) {
									String constraint2Left = constraint2
											.substring(0, constraint2
													.indexOf("<"));
									String constraint2Right = constraint2
											.substring(constraint2.indexOf("<") + 1);
									if (!reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");

										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + ";" + reset2
												+ "),s" + (stateIndex + 1)
												+ ")");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">="
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;

									} else if (reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + "),s"
												+ (stateIndex + 1) + ")");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">="
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								} else if (constraint2.contains("<=")) {
									String constraint2Left = constraint2
											.substring(0, constraint2
													.indexOf("<="));
									String constraint2Right = constraint2
											.substring(constraint2
													.indexOf("<=") + 2);
									if (!reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + ";" + reset2
												+ "),s" + (stateIndex + 1)
												+ ")");

										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">"
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									} else if (reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + "),s"
												+ (stateIndex + 1) + ")");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">"
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								}
							} else {
								MessageDialog.openError(window.getShell(),
										"Error", "timed constraint error!");
							}
							// 然后message
							if (constraint1.contains("<")
									|| constraint1.contains("<=")) {
								if (constraint1.contains("<")
										&& !constraint1.contains("<=")) {
									String constraint1Left = constraint1
											.substring(0, constraint1
													.indexOf("<"));
									String constraint1Right = constraint1
											.substring(constraint1.indexOf("<") + 1);
									if (!reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + ",("
												+ constraint1 + ";" + reset1
												+ "),s" + (stateIndex + 1)
												+ "(Glue))");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">="
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									} else if (reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + ",("
												+ constraint1 + "),s"
												+ (stateIndex + 1) + "(Glue))");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">="
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								} else if (constraint1.contains("<=")) {
									String constraint1Left = constraint1
											.substring(0, constraint1
													.indexOf("<="));
									String constraint1Right = constraint1
											.substring(constraint1
													.indexOf("<=") + 2);
									if (!reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + ",("
												+ constraint1 + ";" + reset1
												+ "),s" + (stateIndex + 1)
												+ "(Glue))");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">"
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									} else if (reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + ",("
												+ constraint1 + "),s"
												+ (stateIndex + 1) + "(Glue))");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">"
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								}
							} else {
								MessageDialog.openError(window.getShell(),
										"Error", "timed constraint error!");
							}
							// // 最后是Error状态的一个自转换(暂时未添加，还未成熟)
							// automataList.add("(s"
							// + (startStateIndex + valueArraySize + 2)
							// + ",1,empty," + "s"
							// + (startStateIndex + valueArraySize + 2)
							// + "(Error))");
						} else if (value1.substring(0, value1.indexOf(":"))
								.equals("f")) {
							// 前缀为f
							// 首先判断PastWantedChainConstraint和message的constraint
							// 综合考虑是否存在<或<=，然后才可以进行下一步
							if ((constraint2.contains("<") || constraint2
									.contains("<="))
									&& (constraint1.contains("<") || constraint1
											.contains("<="))) {

								if (!reset2.isEmpty()) {
									automataList.add("(s" + stateIndex + ",!"
											+ value2Contain + ",("
											+ constraint2 + "),s" + stateIndex
											+ ")");
									automataList.add("(s" + stateIndex + ","
											+ value2Contain + ",("
											+ constraint2 + ";" + reset2
											+ "),s" + (stateIndex + 1) + ")");
									stateIndex++;
								} else if (reset2.isEmpty()) {
									if (constraint2.equals(constraint1)) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + "),s"
												+ (stateIndex + 1) + ")");
										stateIndex++;

									} else {
										MessageDialog.openError(window
												.getShell(), "Error",
												"timed constraint error!");
									}

								}
							} else {
								MessageDialog.openError(window.getShell(),
										"Error", "timed constraint error!");
							}
							// 然后message
							if (constraint1.contains("<")
									|| constraint1.contains("<=")) {
								if (constraint1.contains("<")
										&& !constraint1.contains("<=")) {
									String constraint1Left = constraint1
											.substring(0, constraint1
													.indexOf("<"));
									String constraint1Right = constraint1
											.substring(constraint1.indexOf("<") + 1);
									if (!reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s"
												+ stateIndex
												+ ","
												+ value1Right
												+ ",("
												+ constraint1
												+ ";"
												+ reset1
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">="
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 1)
												+ "(Glue))");
										stateIndex++;
									} else if (reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s"
												+ stateIndex
												+ ","
												+ value1Right
												+ ",("
												+ constraint1
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">="
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 1)
												+ "(Glue))");
										stateIndex++;
									}
								} else if (constraint1.contains("<=")) {
									String constraint1Left = constraint1
											.substring(0, constraint1
													.indexOf("<="));
									String constraint1Right = constraint1
											.substring(constraint1
													.indexOf("<=") + 2);
									if (!reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s"
												+ stateIndex
												+ ","
												+ value1Right
												+ ",("
												+ constraint1
												+ ";"
												+ reset1
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">"
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 1)
												+ "(Glue))");
										stateIndex++;
									} else if (reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s"
												+ stateIndex
												+ ","
												+ value1Right
												+ ",("
												+ constraint1
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">"
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 1)
												+ "(Glue))");
										stateIndex++;
									}
								}
							} else {
								MessageDialog.openError(window.getShell(),
										"Error", "timed constraint error!");
							}
							// // 最后是Error状态的一个自转换(暂时未添加，还未成熟)
							// automataList.add("(s"
							// + (startStateIndex + valueArraySize + 2)
							// + ",1,empty," + "s"
							// + (startStateIndex + valueArraySize + 2)
							// + "(Error))");
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}

					} else {
						MessageDialog.openError(window.getShell(), "Error",
								"timed constraint error!");
					}

					// //////////////////////////////////////////////////////////
					// ////
					// //
					// /////////// == End:PastBooleanConstraint
					// ==/////////////////////
					// //////////////////////////////////////////////////////////
					// ////
					// //
				} else if (line.getPresentConstraintValue().isEmpty()
						&& line.getPastUnwantedMessageConstraintValue().isEmpty()
						&& !line.getFutureUnwantedMessageConstraintValue().isEmpty()
						&& line.getPresentPastValue().isEmpty()
						&& line.getPresentFutureValue().isEmpty()
						&& line.getPastWantedChainConstraintValue().isEmpty()
						&& line.getFutureWantedChainConstraintValue().isEmpty()
						&& line.getPastUnwantedChainConstraintValue().isEmpty()
						&& line.getFutureUnwantedChainConstraintValue()
								.isEmpty()) {
					// //////////////////////////////////////////////////////////
					// ////
					// //
					// /////////// == FutureBooleanConstraint
					// ==/////////////////////////
					// //////////////////////////////////////////////////////////
					// ////
					// //
					// 局部临时变量，易于处理问题
					String value1 = line.getMessageValue();
					String constraint1 = line.getMessageConstraint();
					String reset1 = line.getMessageReset();
					String value2 = line.getFutureUnwantedMessageConstraintValue();
					String constraint2 = line
							.getFutureUnwantedMessageConstraintConstraint();
					String reset2 = line.getFutureUnwantedMessageConstraintReset();
					String value1Right = value1
							.substring(value1.indexOf(":") + 1);
					String value2Contain = value2.substring(
							value2.indexOf("{") + 1, value2.lastIndexOf("}"));

					// 这里假设value2是只有1个元素的集合,因此valueArraySize=1
					int valueArraySize = 1;
					// 比如b={m1,m2,...,mn},假设n=1，预先这样处理，此为一bug

					if (!value2Contain.contains(",")) {
						// 表明{}内所包含的只有1个元素。多个元素以,隔开
						if (value1.substring(0, value1.indexOf(":"))
								.equals("e")) {
							// 前缀为e
							// 先处理message部分
							if (constraint1.contains("<")
									|| constraint1.contains("<=")) {
								if (constraint1.contains("<")
										&& !constraint1.contains("<=")) {
									String constraint1Left = constraint1
											.substring(0, constraint1
													.indexOf("<"));
									String constraint1Right = constraint1
											.substring(constraint1.indexOf("<") + 1);
									if (!reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + ",("
												+ constraint1 + ";" + reset1
												+ "),s" + (stateIndex + 1)
												+ ")");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint1Left
												+ ">=" + constraint1Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									} else if (reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + ",("
												+ constraint1 + "),s"
												+ (stateIndex + 1) + ")");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint1Left
												+ ">=" + constraint1Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									}
								} else if (constraint1.contains("<=")) {
									String constraint1Left = constraint1
											.substring(0, constraint1
													.indexOf("<="));
									String constraint1Right = constraint1
											.substring(constraint1
													.indexOf("<=") + 2);
									if (!reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + ",("
												+ constraint1 + ";" + reset1
												+ "),s" + (stateIndex + 1)
												+ ")");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint1Left
												+ ">" + constraint1Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									} else if (reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + ",("
												+ constraint1 + "),s"
												+ (stateIndex + 1) + ")");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint1Left
												+ ">" + constraint1Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									}
								}
							} else if (constraint1.isEmpty()) {
								automataList.add("(s" + stateIndex + ","
										+ value1Right + ",(" + reset1 + "),s"
										+ (stateIndex + 1) + ")");
								stateIndex++;
							} else {
								MessageDialog.openError(window.getShell(),
										"Error", "timed constraint error!");
							}
							// 然后判断PastWantedChainConstraint的constraint是否存在<或<=，
							// 然后才可以进行下一步
							if (constraint2.contains("<")
									|| constraint2.contains("<=")) {
								if (constraint2.contains("<")
										&& !constraint2.contains("<=")) {
									String constraint2Left = constraint2
											.substring(0, constraint2
													.indexOf("<"));
									String constraint2Right = constraint2
											.substring(constraint2.indexOf("<") + 1);
									if (!reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");

										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + ";" + reset2
												+ "),s" + (stateIndex + 1)
												+ "(Glue))");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">=" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;

									} else if (reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + "),s"
												+ (stateIndex + 1) + "(Glue))");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">=" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									}
								} else if (constraint2.contains("<=")) {
									String constraint2Left = constraint2
											.substring(0, constraint2
													.indexOf("<="));
									String constraint2Right = constraint2
											.substring(constraint2
													.indexOf("<=") + 2);
									if (!reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + ";" + reset2
												+ "),s" + (stateIndex + 1)
												+ "(Glue))");

										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									} else if (reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + "),s"
												+ (stateIndex + 1) + "(Glue))");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									}
								}
							} else {
								MessageDialog.openError(window.getShell(),
										"Error", "timed constraint error!");
							}
						} else if (value1.substring(0, value1.indexOf(":"))
								.equals("r")) {
							// 前缀为r
							// 先处理message部分
							if (constraint1.contains("<")
									|| constraint1.contains("<=")) {
								if (constraint1.contains("<")
										&& !constraint1.contains("<=")) {
									String constraint1Left = constraint1
											.substring(0, constraint1
													.indexOf("<"));
									String constraint1Right = constraint1
											.substring(constraint1.indexOf("<") + 1);
									if (!reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + ",("
												+ constraint1 + ";" + reset1
												+ "),s" + (stateIndex + 1)
												+ ")");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">="
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									} else if (reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + ",("
												+ constraint1 + "),s"
												+ (stateIndex + 1) + ")");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">="
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								} else if (constraint1.contains("<=")) {
									String constraint1Left = constraint1
											.substring(0, constraint1
													.indexOf("<="));
									String constraint1Right = constraint1
											.substring(constraint1
													.indexOf("<=") + 2);
									if (!reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + ",("
												+ constraint1 + ";" + reset1
												+ "),s" + (stateIndex + 1)
												+ ")");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">"
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									} else if (reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + ",("
												+ constraint1 + "),s"
												+ (stateIndex + 1) + ")");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">"
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								}
							} else {
								MessageDialog.openError(window.getShell(),
										"Error", "timed constraint error!");
							}
							// 然后判断PastWantedChainConstraint的constraint是否存在<或<=，
							// 然后才可以进行下一步
							if (constraint2.contains("<")
									|| constraint2.contains("<=")) {
								if (constraint2.contains("<")
										&& !constraint2.contains("<=")) {
									String constraint2Left = constraint2
											.substring(0, constraint2
													.indexOf("<"));
									String constraint2Right = constraint2
											.substring(constraint2.indexOf("<") + 1);
									if (!reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");

										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + ";" + reset2
												+ "),s" + (stateIndex + 1)
												+ "(Glue))");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">="
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;

									} else if (reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + "),s"
												+ (stateIndex + 1) + "(Glue))");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">="
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								} else if (constraint2.contains("<=")) {
									String constraint2Left = constraint2
											.substring(0, constraint2
													.indexOf("<="));
									String constraint2Right = constraint2
											.substring(constraint2
													.indexOf("<=") + 2);
									if (!reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + ";" + reset2
												+ "),s" + (stateIndex + 1)
												+ "(Glue))");

										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">"
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									} else if (reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + "),s"
												+ (stateIndex + 1) + "(Glue))");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">"
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								}
							} else {
								MessageDialog.openError(window.getShell(),
										"Error", "timed constraint error!");
							}

							// // 最后是Error状态的一个自转换(暂时未添加，还未成熟)
							// automataList.add("(s"
							// + (startStateIndex + valueArraySize + 2)
							// + ",1,empty," + "s"
							// + (startStateIndex + valueArraySize + 2)
							// + "(Error))");
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}

					} else {
						MessageDialog.openError(window.getShell(), "Error",
								"timed constraint error!");
					}

					// //////////////////////////////////////////////////////////
					// ////
					// //
					// /////////// == End:FutureBooleanConstraint
					// ==/////////////////////
					// //////////////////////////////////////////////////////////
					// ////
					// //
				} else if (line.getPresentConstraintValue().isEmpty()
						&& line.getPastUnwantedMessageConstraintValue().isEmpty()
						&& line.getFutureUnwantedMessageConstraintValue().isEmpty()
						&& !line.getPresentPastValue().isEmpty()
						&& line.getPresentFutureValue().isEmpty()
						&& line.getPastWantedChainConstraintValue().isEmpty()
						&& line.getFutureWantedChainConstraintValue().isEmpty()
						&& line.getPastUnwantedChainConstraintValue().isEmpty()
						&& line.getFutureUnwantedChainConstraintValue()
								.isEmpty()) {
					// //////////////////////////////////////////////////////////
					// ////
					// //
					// /////////// == PresentPast
					// ==///////////////////////////////////
					// //////////////////////////////////////////////////////////
					// ////
					// //
					// 局部临时变量，易于处理问题
					String value1 = line.getMessageValue();
					String constraint1 = line.getMessageConstraint();
					String reset1 = line.getMessageReset();
					String value2 = line.getPresentPastValue();
					String constraint2 = line.getPresentPastConstraint();
					String reset2 = line.getPresentPastReset();
					String value1Right = value1
							.substring(value1.indexOf(":") + 1);
					String value2Contain = value2.substring(
							value2.indexOf("{") + 1, value2.lastIndexOf("}"));

					// 这里假设value2是只有1个元素的集合,因此valueArraySize=1
					int valueArraySize = 1;
					// 比如b={m1,m2,...,mn},假设n=1，预先这样处理，此为一bug

					if (!value2Contain.contains(",")) {
						// 表明{}内所包含的只有1个元素。多个元素以,隔开
						if (value1.substring(0, value1.indexOf(":"))
								.equals("e")) {
							// 前缀为e
							// 首先判断PastWantedChainConstraint的constraint是否存在<或<=，
							// 然后才可以进行下一步
							if (constraint2.contains("<")
									|| constraint2.contains("<=")) {
								if (constraint2.contains("<")
										&& !constraint2.contains("<=")) {
									String constraint2Left = constraint2
											.substring(0, constraint2
													.indexOf("<"));
									String constraint2Right = constraint2
											.substring(constraint2.indexOf("<") + 1);
									if (!reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");

										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + ";" + reset2
												+ "),s" + (stateIndex + 1)
												+ ")");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">=" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;

									} else if (reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + "),s"
												+ (stateIndex + 1) + ")");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">=" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									}
								} else if (constraint2.contains("<=")) {
									String constraint2Left = constraint2
											.substring(0, constraint2
													.indexOf("<="));
									String constraint2Right = constraint2
											.substring(constraint2
													.indexOf("<=") + 2);
									if (!reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + ";" + reset2
												+ "),s" + (stateIndex + 1)
												+ ")");

										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									} else if (reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + "),s"
												+ (stateIndex + 1) + ")");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									}
								}
							} else if (constraint2.isEmpty()) {
								automataList.add("(s" + stateIndex + ","
										+ value2Contain + ",(" + reset2 + "),s"
										+ (stateIndex + 1) + ")");
								stateIndex++;
							} else {
								MessageDialog.openError(window.getShell(),
										"Error", "timed constraint error!");
							}
							// 然后message
							if (constraint1.contains("<")
									|| constraint1.contains("<=")) {
								if (constraint1.contains("<")
										&& !constraint1.contains("<=")) {
									String constraint1Left = constraint1
											.substring(0, constraint1
													.indexOf("<"));
									String constraint1Right = constraint1
											.substring(constraint1.indexOf("<") + 1);
									if (!reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + "&"
												+ value2Contain + ",("
												+ constraint1 + ";" + reset1
												+ "),s" + (stateIndex + 1)
												+ "(Glue))");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint1Left
												+ ">=" + constraint1Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									} else if (reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + "&"
												+ value2Contain + ",("
												+ constraint1 + "),s"
												+ (stateIndex + 1) + "(Glue))");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint1Left
												+ ">=" + constraint1Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									}
								} else if (constraint1.contains("<=")) {
									String constraint1Left = constraint1
											.substring(0, constraint1
													.indexOf("<="));
									String constraint1Right = constraint1
											.substring(constraint1
													.indexOf("<=") + 2);
									if (!reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + "&"
												+ value2Contain + ",("
												+ constraint1 + ";" + reset1
												+ "),s" + (stateIndex + 1)
												+ "(Glue))");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint1Left
												+ ">" + constraint1Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									} else if (reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + "&"
												+ value2Contain + ",("
												+ constraint1 + "),s"
												+ (stateIndex + 1) + "(Glue))");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint1Left
												+ ">" + constraint1Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									}
								}
							} else {
								MessageDialog.openError(window.getShell(),
										"Error", "timed constraint error!");
							}
						} else if (value1.substring(0, value1.indexOf(":"))
								.equals("r")) {
							// 前缀为r
							// 首先判断PastWantedChainConstraint的constraint是否存在<或<=，
							// 然后才可以进行下一步
							if (constraint2.contains("<")
									|| constraint2.contains("<=")) {
								if (constraint2.contains("<")
										&& !constraint2.contains("<=")) {
									String constraint2Left = constraint2
											.substring(0, constraint2
													.indexOf("<"));
									String constraint2Right = constraint2
											.substring(constraint2.indexOf("<") + 1);
									if (!reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");

										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + ";" + reset2
												+ "),s" + (stateIndex + 1)
												+ ")");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">="
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;

									} else if (reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + "),s"
												+ (stateIndex + 1) + ")");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">="
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								} else if (constraint2.contains("<=")) {
									String constraint2Left = constraint2
											.substring(0, constraint2
													.indexOf("<="));
									String constraint2Right = constraint2
											.substring(constraint2
													.indexOf("<=") + 2);
									if (!reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + ";" + reset2
												+ "),s" + (stateIndex + 1)
												+ ")");

										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">"
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									} else if (reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + "),s"
												+ (stateIndex + 1) + ")");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">"
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								}
							} else {
								MessageDialog.openError(window.getShell(),
										"Error", "timed constraint error!");
							}
							// 然后message
							if (constraint1.contains("<")
									|| constraint1.contains("<=")) {
								if (constraint1.contains("<")
										&& !constraint1.contains("<=")) {
									String constraint1Left = constraint1
											.substring(0, constraint1
													.indexOf("<"));
									String constraint1Right = constraint1
											.substring(constraint1.indexOf("<") + 1);
									if (!reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + "&"
												+ value2Contain + ",("
												+ constraint1 + ";" + reset1
												+ "),s" + (stateIndex + 1)
												+ "(Glue))");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">="
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									} else if (reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + "&"
												+ value2Contain + ",("
												+ constraint1 + "),s"
												+ (stateIndex + 1) + "(Glue))");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">="
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								} else if (constraint1.contains("<=")) {
									String constraint1Left = constraint1
											.substring(0, constraint1
													.indexOf("<="));
									String constraint1Right = constraint1
											.substring(constraint1
													.indexOf("<=") + 2);
									if (!reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + "&"
												+ value2Contain + ",("
												+ constraint1 + ";" + reset1
												+ "),s" + (stateIndex + 1)
												+ "(Glue))");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">"
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									} else if (reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + "&"
												+ value2Contain + ",("
												+ constraint1 + "),s"
												+ (stateIndex + 1) + "(Glue))");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">"
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								}
							} else {
								MessageDialog.openError(window.getShell(),
										"Error", "timed constraint error!");
							}
							// // 最后是Error状态的一个自转换(暂时未添加，还未成熟)
							// automataList.add("(s"
							// + (startStateIndex + valueArraySize + 2)
							// + ",1,empty," + "s"
							// + (startStateIndex + valueArraySize + 2)
							// + "(Error))");
						} else if (value1.substring(0, value1.indexOf(":"))
								.equals("f")) {
							// 前缀为f
							// 首先判断PastWantedChainConstraint和message的constraint
							// 综合考虑是否存在<或<=，然后才可以进行下一步
							if ((constraint2.contains("<") || constraint2
									.contains("<="))
									&& (constraint1.contains("<") || constraint1
											.contains("<="))) {

								if (!reset2.isEmpty()) {
									automataList.add("(s" + stateIndex + ",!"
											+ value2Contain + ",("
											+ constraint2 + "),s" + stateIndex
											+ ")");
									automataList.add("(s" + stateIndex + ","
											+ value2Contain + ",("
											+ constraint2 + ";" + reset2
											+ "),s" + (stateIndex + 1) + ")");
									stateIndex++;
								} else if (reset2.isEmpty()) {
									if (constraint2.equals(constraint1)) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + "),s"
												+ (stateIndex + 1) + ")");
										stateIndex++;

									} else {
										MessageDialog.openError(window
												.getShell(), "Error",
												"timed constraint error!");
									}

								}
							} else {
								MessageDialog.openError(window.getShell(),
										"Error", "timed constraint error!");
							}
							// 然后message
							if (constraint1.contains("<")
									|| constraint1.contains("<=")) {
								if (constraint1.contains("<")
										&& !constraint1.contains("<=")) {
									String constraint1Left = constraint1
											.substring(0, constraint1
													.indexOf("<"));
									String constraint1Right = constraint1
											.substring(constraint1.indexOf("<") + 1);
									if (!reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s"
												+ stateIndex
												+ ","
												+ value1Right
												+ "&"
												+ value2Contain
												+ ",("
												+ constraint1
												+ ";"
												+ reset1
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">="
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 1)
												+ "(Glue))");
										stateIndex++;
									} else if (reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s"
												+ stateIndex
												+ ","
												+ value1Right
												+ "&"
												+ value2Contain
												+ ",("
												+ constraint1
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">="
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 1)
												+ "(Glue))");
										stateIndex++;
									}
								} else if (constraint1.contains("<=")) {
									String constraint1Left = constraint1
											.substring(0, constraint1
													.indexOf("<="));
									String constraint1Right = constraint1
											.substring(constraint1
													.indexOf("<=") + 2);
									if (!reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s"
												+ stateIndex
												+ ","
												+ value1Right
												+ "&"
												+ value2Contain
												+ ",("
												+ constraint1
												+ ";"
												+ reset1
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">"
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 1)
												+ "(Glue))");
										stateIndex++;
									} else if (reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s"
												+ stateIndex
												+ ","
												+ value1Right
												+ "&"
												+ value2Contain
												+ ",("
												+ constraint1
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">"
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 1)
												+ "(Glue))");
										stateIndex++;
									}
								}
							} else {
								MessageDialog.openError(window.getShell(),
										"Error", "timed constraint error!");
							}
							// // 最后是Error状态的一个自转换(暂时未添加，还未成熟)
							// automataList.add("(s"
							// + (startStateIndex + valueArraySize + 2)
							// + ",1,empty," + "s"
							// + (startStateIndex + valueArraySize + 2)
							// + "(Error))");
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}

					} else {
						MessageDialog.openError(window.getShell(), "Error",
								"timed constraint error!");
					}

					// //////////////////////////////////////////////////////////
					// ////
					// //
					// /////////// == End:PresentPast ==/////////////////////
					// //////////////////////////////////////////////////////////
					// ////
					// //
				} else if (line.getPresentConstraintValue().isEmpty()
						&& line.getPastUnwantedMessageConstraintValue().isEmpty()
						&& line.getFutureUnwantedMessageConstraintValue().isEmpty()
						&& line.getPresentPastValue().isEmpty()
						&& !line.getPresentFutureValue().isEmpty()
						&& line.getPastWantedChainConstraintValue().isEmpty()
						&& line.getFutureWantedChainConstraintValue().isEmpty()
						&& line.getPastUnwantedChainConstraintValue().isEmpty()
						&& line.getFutureUnwantedChainConstraintValue()
								.isEmpty()) {
					// //////////////////////////////////////////////////////////
					// ////
					// //
					// /////////// == PresentFuture ==/////////////////////////
					// //////////////////////////////////////////////////////////
					// ////
					// //
					// 局部临时变量，易于处理问题
					String value1 = line.getMessageValue();
					String constraint1 = line.getMessageConstraint();
					String reset1 = line.getMessageReset();
					String value2 = line.getPresentFutureValue();
					String constraint2 = line.getPresentFutureConstraint();
					String reset2 = line.getPresentFutureReset();
					String value1Right = value1
							.substring(value1.indexOf(":") + 1);
					String value2Contain = value2.substring(
							value2.indexOf("{") + 1, value2.lastIndexOf("}"));

					// 这里假设value2是只有1个元素的集合,因此valueArraySize=1
					int valueArraySize = 1;
					// 比如b={m1,m2,...,mn},假设n=1，预先这样处理，此为一bug

					if (!value2Contain.contains(",")) {
						// 表明{}内所包含的只有1个元素。多个元素以,隔开
						if (value1.substring(0, value1.indexOf(":"))
								.equals("e")) {
							// 前缀为r
							// 先处理message部分
							if (constraint1.contains("<")
									|| constraint1.contains("<=")) {
								if (constraint1.contains("<")
										&& !constraint1.contains("<=")) {
									String constraint1Left = constraint1
											.substring(0, constraint1
													.indexOf("<"));
									String constraint1Right = constraint1
											.substring(constraint1.indexOf("<") + 1);
									if (!reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + "&"
												+ value2Contain + ",("
												+ constraint1 + ";" + reset1
												+ "),s" + (stateIndex + 1)
												+ ")");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint1Left
												+ ">=" + constraint1Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									} else if (reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + "&"
												+ value2Contain + ",("
												+ constraint1 + "),s"
												+ (stateIndex + 1) + ")");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint1Left
												+ ">=" + constraint1Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									}
								} else if (constraint1.contains("<=")) {
									String constraint1Left = constraint1
											.substring(0, constraint1
													.indexOf("<="));
									String constraint1Right = constraint1
											.substring(constraint1
													.indexOf("<=") + 2);
									if (!reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + "&"
												+ value2Contain + ",("
												+ constraint1 + ";" + reset1
												+ "),s" + (stateIndex + 1)
												+ ")");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint1Left
												+ ">" + constraint1Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									} else if (reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + "&"
												+ value2Contain + ",("
												+ constraint1 + "),s"
												+ (stateIndex + 1) + ")");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint1Left
												+ ">" + constraint1Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									}
								}
							} else if (constraint1.isEmpty()) {
								automataList.add("(s" + stateIndex + ","
										+ value1Right + "&" + value2Contain
										+ ",(" + reset1 + "),s"
										+ (stateIndex + 1) + ")");
								stateIndex++;
							} else {
								MessageDialog.openError(window.getShell(),
										"Error", "timed constraint error!");
							}
							// 然后判断PastWantedChainConstraint的constraint是否存在<或<=，
							// 然后才可以进行下一步
							if (constraint2.contains("<")
									|| constraint2.contains("<=")) {
								if (constraint2.contains("<")
										&& !constraint2.contains("<=")) {
									String constraint2Left = constraint2
											.substring(0, constraint2
													.indexOf("<"));
									String constraint2Right = constraint2
											.substring(constraint2.indexOf("<") + 1);
									if (!reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");

										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + ";" + reset2
												+ "),s" + (stateIndex + 1)
												+ "(Glue))");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">=" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;

									} else if (reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + "),s"
												+ (stateIndex + 1) + "(Glue))");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">=" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									}
								} else if (constraint2.contains("<=")) {
									String constraint2Left = constraint2
											.substring(0, constraint2
													.indexOf("<="));
									String constraint2Right = constraint2
											.substring(constraint2
													.indexOf("<=") + 2);
									if (!reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + ";" + reset2
												+ "),s" + (stateIndex + 1)
												+ "(Glue))");

										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									} else if (reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + "),s"
												+ (stateIndex + 1) + "(Glue))");
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									}
								}
							} else {
								MessageDialog.openError(window.getShell(),
										"Error", "timed constraint error!");
							}
						} else if (value1.substring(0, value1.indexOf(":"))
								.equals("r")) {
							// 前缀为r
							// 先处理message部分
							if (constraint1.contains("<")
									|| constraint1.contains("<=")) {
								if (constraint1.contains("<")
										&& !constraint1.contains("<=")) {
									String constraint1Left = constraint1
											.substring(0, constraint1
													.indexOf("<"));
									String constraint1Right = constraint1
											.substring(constraint1.indexOf("<") + 1);
									if (!reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + ",("
												+ constraint1 + ";" + reset1
												+ "),s" + (stateIndex + 1)
												+ ")");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">="
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									} else if (reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + ",("
												+ constraint1 + "),s"
												+ (stateIndex + 1) + ")");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">="
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								} else if (constraint1.contains("<=")) {
									String constraint1Left = constraint1
											.substring(0, constraint1
													.indexOf("<="));
									String constraint1Right = constraint1
											.substring(constraint1
													.indexOf("<=") + 2);
									if (!reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + ",("
												+ constraint1 + ";" + reset1
												+ "),s" + (stateIndex + 1)
												+ ")");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">"
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									} else if (reset1.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value1Right + ",("
												+ constraint1 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value1Right + ",("
												+ constraint1 + "),s"
												+ (stateIndex + 1) + ")");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint1Left
												+ ">"
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								}
							} else {
								MessageDialog.openError(window.getShell(),
										"Error", "timed constraint error!");
							}
							// 然后判断PastWantedChainConstraint的constraint是否存在<或<=，
							// 然后才可以进行下一步
							if (constraint2.contains("<")
									|| constraint2.contains("<=")) {
								if (constraint2.contains("<")
										&& !constraint2.contains("<=")) {
									String constraint2Left = constraint2
											.substring(0, constraint2
													.indexOf("<"));
									String constraint2Right = constraint2
											.substring(constraint2.indexOf("<") + 1);
									if (!reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");

										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + ";" + reset2
												+ "),s" + (stateIndex + 1)
												+ "(Glue))");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">="
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;

									} else if (reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + "),s"
												+ (stateIndex + 1) + "(Glue))");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">="
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								} else if (constraint2.contains("<=")) {
									String constraint2Left = constraint2
											.substring(0, constraint2
													.indexOf("<="));
									String constraint2Right = constraint2
											.substring(constraint2
													.indexOf("<=") + 2);
									if (!reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + ";" + reset2
												+ "),s" + (stateIndex + 1)
												+ "(Glue))");

										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">"
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									} else if (reset2.isEmpty()) {
										automataList.add("(s" + stateIndex
												+ ",!" + value2Contain + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										automataList.add("(s" + stateIndex
												+ "," + value2Contain + ",("
												+ constraint2 + "),s"
												+ (stateIndex + 1) + "(Glue))");
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">"
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								}
							} else {
								MessageDialog.openError(window.getShell(),
										"Error", "timed constraint error!");
							}

							// // 最后是Error状态的一个自转换(暂时未添加，还未成熟)
							// automataList.add("(s"
							// + (startStateIndex + valueArraySize + 2)
							// + ",1,empty," + "s"
							// + (startStateIndex + valueArraySize + 2)
							// + "(Error))");
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}

					} else {
						MessageDialog.openError(window.getShell(), "Error",
								"timed constraint error!");
					}

					// //////////////////////////////////////////////////////////
					// ////
					// //
					// /////////// == End:PresentFuture ==/////////////////////
					// //////////////////////////////////////////////////////////
					// ////
					// //
				} else if (line.getPresentConstraintValue().isEmpty()
						&& line.getPastUnwantedMessageConstraintValue().isEmpty()
						&& line.getFutureUnwantedMessageConstraintValue().isEmpty()
						&& line.getPresentPastValue().isEmpty()
						&& line.getPresentFutureValue().isEmpty()
						&& !line.getPastWantedChainConstraintValue().isEmpty()
						&& line.getFutureWantedChainConstraintValue().isEmpty()
						&& line.getPastUnwantedChainConstraintValue().isEmpty()
						&& line.getFutureUnwantedChainConstraintValue()
								.isEmpty()) {
					// //////////////////////////////////////////////////////////
					// ////
					// //
					// /////////// == PastWantedChainConstraint
					// ==/////////////////////
					// //////////////////////////////////////////////////////////
					// ////
					// //
					// 局部临时变量，易于处理问题
					String value1 = line.getMessageValue();
					String constraint1 = line.getMessageConstraint();
					String reset1 = line.getMessageReset();
					String value2 = line.getPastWantedChainConstraintValue();
					String constraint2 = line
							.getPastWantedChainConstraintConstraint();
					String reset2 = line.getPastWantedChainConstraintReset();

					// 下面处理PastWantedChainConstraint部分，先分解其value部分
					List<String> valueArray = new ArrayList<String>();
					value2 = value2.substring(value2.indexOf("(") + 1, value2
							.lastIndexOf(")"));
					while (value2.indexOf(",") > 0) {
						valueArray
								.add(value2.substring(0, value2.indexOf(",")));
						value2 = value2.substring(value2.indexOf(",") + 1);
					}
					valueArray.add(value2);
					int valueArraySize = valueArray.size();

					if (value1.substring(0, value1.indexOf(":")).equals("e")) {
						// 前缀为e
						// 首先判断PastWantedChainConstraint的constraint是否存在<或<=，
						// 然后才可以进行下一步
						if (constraint2.contains("<")
								|| constraint2.contains("<=")) {
							if (constraint2.contains("<")
									&& !constraint2.contains("<=")) {
								String constraint2Left = constraint2.substring(
										0, constraint2.indexOf("<"));
								String constraint2Right = constraint2
										.substring(constraint2.indexOf("<") + 1);
								if (!reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2 + ";"
													+ reset2 + "),s"
													+ (stateIndex + 1) + ")");
										}
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">=" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									}
								} else if (reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										}
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">=" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									}
								}
							} else if (constraint2.contains("<=")) {
								String constraint2Left = constraint2.substring(
										0, constraint2.indexOf("<="));
								String constraint2Right = constraint2
										.substring(constraint2.indexOf("<=") + 2);
								if (!reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2 + ";"
													+ reset2 + "),s"
													+ (stateIndex + 1) + ")");
										}
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									}
								} else if (reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										}
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									}
								}
							}
						} else if (constraint2.isEmpty()) {
							for (int j = 0; j < valueArraySize; j++) {
								automataList.add("(s" + stateIndex + ",!"
										+ valueArray.get(j) + ",(),s"
										+ stateIndex + ")");
								if (j != valueArraySize - 1) {
									automataList.add("(s" + stateIndex + ","
											+ valueArray.get(j) + ",(),s"
											+ (stateIndex + 1) + ")");
								} else {
									automataList.add("(s" + stateIndex + ","
											+ valueArray.get(j) + ",(" + reset2
											+ "),s" + (stateIndex + 1) + ")");
								}
								stateIndex++;
							}
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}
						// 然后message
						if (constraint1.contains("<")
								|| constraint1.contains("<=")) {
							if (constraint1.contains("<")
									&& !constraint1.contains("<=")) {
								String constraint1Left = constraint1.substring(
										0, constraint1.indexOf("<"));
								String constraint1Right = constraint1
										.substring(constraint1.indexOf("<") + 1);
								if (!reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + ";" + reset1
											+ "),s" + (stateIndex + 1)
											+ "(Glue))");
									automataList.add("(s" + stateIndex
											+ ",empty,(" + constraint1Left
											+ ">=" + constraint1Right + "),s"
											+ startStateIndex + "(Accept))");
									stateIndex++;
								} else if (reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s"
											+ (stateIndex + 1) + "(Glue))");
									automataList.add("(s" + stateIndex
											+ ",empty,(" + constraint1Left
											+ ">=" + constraint1Right + "),s"
											+ startStateIndex + "(Accept))");
									stateIndex++;
								}
							} else if (constraint1.contains("<=")) {
								String constraint1Left = constraint1.substring(
										0, constraint1.indexOf("<="));
								String constraint1Right = constraint1
										.substring(constraint1.indexOf("<=") + 2);
								if (!reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + ";" + reset1
											+ "),s" + (stateIndex + 1)
											+ "(Glue))");
									automataList.add("(s" + stateIndex
											+ ",empty,(" + constraint1Left
											+ ">" + constraint1Right + "),s"
											+ startStateIndex + "(Accept))");
									stateIndex++;
								} else if (reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s"
											+ (stateIndex + 1) + "(Glue))");
									automataList.add("(s" + stateIndex
											+ ",empty,(" + constraint1Left
											+ ">" + constraint1Right + "),s"
											+ startStateIndex + "(Accept))");
									stateIndex++;
								}
							}
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}
					} else if (value1.substring(0, value1.indexOf(":")).equals(
							"r")) {
						// 前缀为r
						// 首先判断PastWantedChainConstraint的constraint是否存在<或<=，
						// 然后才可以进行下一步
						if (constraint2.contains("<")
								|| constraint2.contains("<=")) {
							if (constraint2.contains("<")
									&& !constraint2.contains("<=")) {
								String constraint2Left = constraint2.substring(
										0, constraint2.indexOf("<"));
								String constraint2Right = constraint2
										.substring(constraint2.indexOf("<") + 1);
								if (!reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2 + ";"
													+ reset2 + "),s"
													+ (stateIndex + 1) + ")");
										}
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">="
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								} else if (reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										}
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">="
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								}
							} else if (constraint2.contains("<=")) {
								String constraint2Left = constraint2.substring(
										0, constraint2.indexOf("<="));
								String constraint2Right = constraint2
										.substring(constraint2.indexOf("<=") + 2);
								if (!reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2 + ";"
													+ reset2 + "),s"
													+ (stateIndex + 1) + ")");
										}
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">"
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								} else if (reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										}
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">"
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								}
							}
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}
						// 然后message
						if (constraint1.contains("<")
								|| constraint1.contains("<=")) {
							if (constraint1.contains("<")
									&& !constraint1.contains("<=")) {
								String constraint1Left = constraint1.substring(
										0, constraint1.indexOf("<"));
								String constraint1Right = constraint1
										.substring(constraint1.indexOf("<") + 1);
								if (!reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + ";" + reset1
											+ "),s" + (stateIndex + 1)
											+ "(Glue))");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">="
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								} else if (reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s"
											+ (stateIndex + 1) + "(Glue))");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">="
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								}
							} else if (constraint1.contains("<=")) {
								String constraint1Left = constraint1.substring(
										0, constraint1.indexOf("<="));
								String constraint1Right = constraint1
										.substring(constraint1.indexOf("<=") + 2);
								if (!reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + ";" + reset1
											+ "),s" + (stateIndex + 1)
											+ "(Glue))");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">"
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								} else if (reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s"
											+ (stateIndex + 1) + "(Glue))");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">"
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								}
							}
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}
						// // 最后是Error状态的一个自转换(暂时未添加，还未成熟)
						// automataList.add("(s" + (startStateIndex +
						// valueArraySize
						// + 2)
						// + "(Error),1,empty," + "s"
						// + (startStateIndex + valueArraySize + 2)
						// + "(Error))");
					} else if (value1.substring(0, value1.indexOf(":")).equals(
							"f")) {
						// 前缀为f
						// 首先判断PastWantedChainConstraint和message的constraint
						// 综合考虑是否存在<或<=，然后才可以进行下一步
						if ((constraint2.contains("<") || constraint2
								.contains("<="))
								&& (constraint1.contains("<") || constraint1
										.contains("<="))) {
							String value1Right = value1.substring(value1
									.indexOf(":") + 1);
							String constraint1Right = "";
							if (constraint1.contains("<=")) {
								constraint1Right = constraint1
										.substring(constraint1.indexOf("<=") + 2);
							} else {
								constraint1Right = constraint1
										.substring(constraint1.indexOf("<") + 1);
							}

							if (!reset2.isEmpty()) {
								for (int j = 0; j < valueArraySize; j++) {
									automataList.add("(s" + stateIndex + ",!"
											+ valueArray.get(j) + ",("
											+ constraint2 + "),s" + stateIndex
											+ ")");
									if (j != valueArraySize - 1) {
										automataList.add("(s" + stateIndex
												+ "," + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ (stateIndex + 1) + ")");
									} else {
										automataList.add("(s" + stateIndex
												+ "," + valueArray.get(j)
												+ ",(" + constraint2 + ";"
												+ reset2 + "),s"
												+ (stateIndex + 1) + ")");
									}
									automataList
											.add("(s"
													+ stateIndex
													+ ","
													+ value1Right
													+ ",("
													+ constraint2
													+ "+"
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 1)
													+ "(Glue))");
									stateIndex++;
								}
							} else if (reset2.isEmpty()) {
								if (constraint2.equals(constraint1)) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										}
										automataList.add("(s"
												+ stateIndex
												+ ","
												+ value1Right
												+ ",("
												+ constraint2
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 1)
												+ "(Glue))");
										stateIndex++;
									}
								} else {
									MessageDialog.openError(window.getShell(),
											"Error", "timed constraint error!");
								}

							}
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}
						// 然后message
						if (constraint1.contains("<")
								|| constraint1.contains("<=")) {
							if (constraint1.contains("<")
									&& !constraint1.contains("<=")) {
								String constraint1Left = constraint1.substring(
										0, constraint1.indexOf("<"));
								String constraint1Right = constraint1
										.substring(constraint1.indexOf("<") + 1);
								if (!reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList
											.add("(s"
													+ stateIndex
													+ ","
													+ value1.substring(value1
															.indexOf(":") + 1)
													+ ",("
													+ constraint1
													+ ";"
													+ reset1
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">="
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 1)
													+ "(Glue))");
									stateIndex++;
								} else if (reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList
											.add("(s"
													+ stateIndex
													+ ","
													+ value1.substring(value1
															.indexOf(":") + 1)
													+ ",("
													+ constraint1
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">="
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 1)
													+ "(Glue))");
									stateIndex++;
								}
							} else if (constraint1.contains("<=")) {
								String constraint1Left = constraint1.substring(
										0, constraint1.indexOf("<="));
								String constraint1Right = constraint1
										.substring(constraint1.indexOf("<=") + 2);
								if (!reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList
											.add("(s"
													+ stateIndex
													+ ","
													+ value1.substring(value1
															.indexOf(":") + 1)
													+ ",("
													+ constraint1
													+ ";"
													+ reset1
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">"
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 1)
													+ "(Glue))");
									stateIndex++;
								} else if (reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList
											.add("(s"
													+ stateIndex
													+ ","
													+ value1.substring(value1
															.indexOf(":") + 1)
													+ ",("
													+ constraint1
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">"
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 1)
													+ "(Glue))");
									stateIndex++;
								}
							}
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}
						// // 最后是Error状态的一个自转换(暂时未添加，还未成熟)
						// automataList.add("(s" + (startStateIndex +
						// valueArraySize
						// + 2)
						// + "(Error),1,empty," + "s"
						// + (startStateIndex + valueArraySize + 2)
						// + "(Error))");
					} else {
						// 其它情况
						MessageDialog.openError(window.getShell(), "Error",
								"timed constraint error!");
					}

					// //////////////////////////////////////////////////////////
					// ////
					// //
					// /////////// == End:PastWantedChainConstraint
					// ==/////////////////
					// //////////////////////////////////////////////////////////
					// ////
					// //
				} else if (line.getPresentConstraintValue().isEmpty()
						&& line.getPastUnwantedMessageConstraintValue().isEmpty()
						&& line.getFutureUnwantedMessageConstraintValue().isEmpty()
						&& line.getPresentPastValue().isEmpty()
						&& line.getPresentFutureValue().isEmpty()
						&& line.getPastWantedChainConstraintValue().isEmpty()
						&& !line.getFutureWantedChainConstraintValue()
								.isEmpty()
						&& line.getPastUnwantedChainConstraintValue().isEmpty()
						&& line.getFutureUnwantedChainConstraintValue()
								.isEmpty()) {

					// //////////////////////////////////////////////////////////
					// ////
					// //
					// /////////// == FutureWantedChainConstraint
					// ==///////////////////
					// //////////////////////////////////////////////////////////
					// ////
					// //
					// 局部临时变量，易于处理问题
					String value1 = line.getMessageValue();
					String constraint1 = line.getMessageConstraint();
					String reset1 = line.getMessageReset();
					String value2 = line.getFutureWantedChainConstraintValue();
					String constraint2 = line
							.getFutureWantedChainConstraintConstraint();
					String reset2 = line.getFutureWantedChainConstraintReset();

					// 下面处理FutureWantedChainConstraint部分，先分解其value部分
					List<String> valueArray = new ArrayList<String>();
					value2 = value2.substring(value2.indexOf("(") + 1, value2
							.lastIndexOf(")"));
					while (value2.indexOf(",") > 0) {
						valueArray
								.add(value2.substring(0, value2.indexOf(",")));
						value2 = value2.substring(value2.indexOf(",") + 1);
					}
					valueArray.add(value2);
					int valueArraySize = valueArray.size();

					if (value1.substring(0, value1.indexOf(":")).equals("e")) {
						// 前缀为e
						// 首先判断message的constraint是否存在<或<=，然后才可以进行下一步
						if (constraint1.contains("<")
								|| constraint1.contains("<=")) {
							if (constraint1.contains("<")
									&& !constraint1.contains("<=")) {
								String constraint1Left = constraint1.substring(
										0, constraint1.indexOf("<"));
								String constraint1Right = constraint1
										.substring(constraint1.indexOf("<") + 1);
								if (!reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + ";" + reset1
											+ "),s" + (stateIndex + 1) + ")");
									automataList.add("(s" + stateIndex
											+ ",empty,(" + constraint1Left
											+ ">=" + constraint1Right + "),s"
											+ startStateIndex + "(Accept))");
									stateIndex++;
								} else if (reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s"
											+ (stateIndex + 1) + ")");
									automataList.add("(s" + stateIndex
											+ ",empty,(" + constraint1Left
											+ ">=" + constraint1Right + "),s"
											+ startStateIndex + "(Accept))");
									stateIndex++;
								}
							} else if (constraint1.contains("<=")) {
								String constraint1Left = constraint1.substring(
										0, constraint1.indexOf("<="));
								String constraint1Right = constraint1
										.substring(constraint1.indexOf("<=") + 2);
								if (!reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + ";" + reset1
											+ "),s" + (stateIndex + 1) + ")");
									automataList.add("(s" + stateIndex
											+ ",empty,(" + constraint1Left
											+ ">" + constraint1Right + "),s"
											+ startStateIndex + "(Accept))");
									stateIndex++;
								} else if (reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s"
											+ (stateIndex + 1) + ")");
									automataList.add("(s" + stateIndex
											+ ",empty,(" + constraint1Left
											+ ">" + constraint1Right + "),s"
											+ startStateIndex + "(Accept))");
									stateIndex++;
								}
							}
						} else if (constraint1.isEmpty()) {
							automataList.add("(s" + stateIndex + ","
									+ value1.substring(value1.indexOf(":") + 1)
									+ ",(" + reset1 + "),s" + (stateIndex + 1)
									+ ")");
							stateIndex++;
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}

						if (constraint2.contains("<")
								|| constraint2.contains("<=")) {
							if (constraint2.contains("<")
									&& !constraint2.contains("<=")) {
								String constraint2Left = constraint2.substring(
										0, constraint2.indexOf("<"));
								String constraint2Right = constraint2
										.substring(constraint2.indexOf("<") + 1);
								if (!reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2 + ";"
													+ reset2 + "),s"
													+ (stateIndex + 1)
													+ "(Glue))");
										}
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">=" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept)))");
										stateIndex++;
									}
								} else if (reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ "(Glue))");
										}
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">=" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									}
								}
							} else if (constraint2.contains("<=")) {
								String constraint2Left = constraint2.substring(
										0, constraint2.indexOf("<="));
								String constraint2Right = constraint2
										.substring(constraint2.indexOf("<=") + 2);
								if (!reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2 + ";"
													+ reset2 + "),s"
													+ (stateIndex + 1)
													+ "(Glue))");
										}
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									}
								} else if (reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ "(Glue))");
										}
										automataList.add("(s" + stateIndex
												+ ",empty,(" + constraint2Left
												+ ">" + constraint2Right
												+ "),s" + startStateIndex
												+ "(Accept))");
										stateIndex++;
									}
								}
							}
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}
					} else if (value1.substring(0, value1.indexOf(":")).equals(
							"r")) {
						// 前缀为r
						// 首先判断message的constraint是否存在<或<=，然后才可以进行下一步
						if (constraint1.contains("<")
								|| constraint1.contains("<=")) {
							if (constraint1.contains("<")
									&& !constraint1.contains("<=")) {
								String constraint1Left = constraint1.substring(
										0, constraint1.indexOf("<"));
								String constraint1Right = constraint1
										.substring(constraint1.indexOf("<") + 1);
								if (!reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + ";" + reset1
											+ "),s" + (stateIndex + 1) + ")");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">="
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								} else if (reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s"
											+ (stateIndex + 1) + ")");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">="
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								}
							} else if (constraint1.contains("<=")) {
								String constraint1Left = constraint1.substring(
										0, constraint1.indexOf("<="));
								String constraint1Right = constraint1
										.substring(constraint1.indexOf("<=") + 2);
								if (!reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + ";" + reset1
											+ "),s" + (stateIndex + 1) + ")");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">"
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								} else if (reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s"
											+ (stateIndex + 1) + ")");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">"
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								}
							}
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}

						if (constraint2.contains("<")
								|| constraint2.contains("<=")) {
							if (constraint2.contains("<")
									&& !constraint2.contains("<=")) {
								String constraint2Left = constraint2.substring(
										0, constraint2.indexOf("<"));
								String constraint2Right = constraint2
										.substring(constraint2.indexOf("<") + 1);
								if (!reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2 + ";"
													+ reset2 + "),s"
													+ (stateIndex + 1)
													+ "(Glue))");
										}
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">="
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								} else if (reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ "(Glue))");
										}
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">="
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								}
							} else if (constraint2.contains("<=")) {
								String constraint2Left = constraint2.substring(
										0, constraint2.indexOf("<="));
								String constraint2Right = constraint2
										.substring(constraint2.indexOf("<=") + 2);
								if (!reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2 + ";"
													+ reset2 + "),s"
													+ (stateIndex + 1)
													+ "(Glue))");
										}
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">"
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								} else if (reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ "(Glue))");
										}
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">"
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								}
							}
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}
					} else {
						// 其它情况
						MessageDialog.openError(window.getShell(), "Error",
								"timed constraint error!");
					}

					// //////////////////////////////////////////////////////////
					// ////
					// //
					// /////////// == End: FutureWantedChainConstraint
					// ==//////////////
					// //////////////////////////////////////////////////////////
					// ////
					// //
				} else if (line.getPresentConstraintValue().isEmpty()
						&& line.getPastUnwantedMessageConstraintValue().isEmpty()
						&& line.getFutureUnwantedMessageConstraintValue().isEmpty()
						&& line.getPresentPastValue().isEmpty()
						&& line.getPresentFutureValue().isEmpty()
						&& line.getPastWantedChainConstraintValue().isEmpty()
						&& line.getFutureWantedChainConstraintValue().isEmpty()
						&& !line.getPastUnwantedChainConstraintValue()
								.isEmpty()
						&& line.getFutureUnwantedChainConstraintValue()
								.isEmpty()) {
					// //////////////////////////////////////////////////////////
					// ////
					// //
					// /////////// == PastUnwantedChainConstraint
					// ==/////////////////////
					// //////////////////////////////////////////////////////////
					// ////
					// //
					// 局部临时变量，易于处理问题
					String value1 = line.getMessageValue();
					String constraint1 = line.getMessageConstraint();
					String reset1 = line.getMessageReset();
					String value2 = line.getPastUnwantedChainConstraintValue();
					String constraint2 = line
							.getPastUnwantedChainConstraintConstraint();
					String reset2 = line.getPastUnwantedChainConstraintReset();

					// 下面处理PastUnwantedChainConstraint部分，先分解其value部分
					List<String> valueArray = new ArrayList<String>();
					value2 = value2.substring(value2.indexOf("(") + 1, value2
							.lastIndexOf(")"));
					while (value2.indexOf(",") > 0) {
						valueArray
								.add(value2.substring(0, value2.indexOf(",")));
						value2 = value2.substring(value2.indexOf(",") + 1);
					}
					valueArray.add(value2);
					int valueArraySize = valueArray.size();

					if (value1.substring(0, value1.indexOf(":")).equals("e")) {
						// 前缀为e
						// 首先判断PastUnwantedChainConstraint和message的constraint
						// 综合考虑是否存在<或<=，然后才可以进行下一步
						String value1Right = value1.substring(value1
								.indexOf(":") + 1);

						if (constraint2.contains("<")
								|| constraint2.contains("<=")) {

							String constraint1Right = "";
							if (constraint1.contains("<=")) {
								constraint1Right = constraint1
										.substring(constraint1.indexOf("<=") + 2);
							} else {
								constraint1Right = constraint1
										.substring(constraint1.indexOf("<") + 1);
							}
							String constraint2Left = constraint2.substring(0,
									constraint2.indexOf("<"));
							String constraint2Right = "";
							if (constraint2.contains("<=")) {
								constraint2Right = constraint2
										.substring(constraint2.indexOf("<=") + 2);
							} else {
								constraint2Right = constraint2
										.substring(constraint2.indexOf("<") + 1);
							}

							if ((constraint1.contains("<") || constraint1
									.contains("<="))) {
								if (!reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + startStateIndex
													+ "(Accept))");
										}
										automataList.add("(s"
												+ stateIndex
												+ ","
												+ value1Right
												+ ",("
												+ constraint2
												+ "+"
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 1)
												+ "(Glue))");
										if (constraint2.contains("<=")) {
											automataList.add("(s" + stateIndex
													+ ",empty,("
													+ constraint2Left + ">"
													+ constraint2Right + "+"
													+ constraint1Right + "),s"
													+ startStateIndex
													+ "(Accept))");
										} else {
											automataList.add("(s" + stateIndex
													+ ",empty,("
													+ constraint2Left + ">="
													+ constraint2Right + "+"
													+ constraint1Right + "),s"
													+ startStateIndex
													+ "(Accept))");
										}
										stateIndex++;
									}
								} else if (reset2.isEmpty()) {
									if (constraint2.equals(constraint1)) {
										for (int j = 0; j < valueArraySize; j++) {
											automataList.add("(s" + stateIndex
													+ ",!" + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + stateIndex + ")");
											if (j != valueArraySize - 1) {
												automataList.add("(s"
														+ stateIndex + ","
														+ valueArray.get(j)
														+ ",(" + constraint2
														+ "),s"
														+ (stateIndex + 1)
														+ ")");
											} else {
												automataList.add("(s"
														+ stateIndex + ","
														+ valueArray.get(j)
														+ ",(" + constraint2
														+ "),s"
														+ startStateIndex
														+ "(Accept))");
											}
											automataList
													.add("(s"
															+ stateIndex
															+ ","
															+ value1Right
															+ ",("
															+ constraint2
															+ "),s"
															+ (startStateIndex
																	+ valueArraySize + 1)
															+ "(Glue))");
											if (constraint2.contains("<=")) {
												automataList.add("(s"
														+ stateIndex
														+ ",empty,("
														+ constraint2Left + ">"
														+ constraint2Right
														+ "),s"
														+ startStateIndex
														+ "(Accept))");
											} else {
												automataList.add("(s"
														+ stateIndex
														+ ",empty,("
														+ constraint2Left
														+ ">="
														+ constraint2Right
														+ "),s"
														+ startStateIndex
														+ "(Accept))");
											}
											stateIndex++;
										}
									} else {
										MessageDialog.openError(window
												.getShell(), "Error",
												"timed constraint error!");
									}

								}
							} else {
								MessageDialog.openError(window.getShell(),
										"Error", "timed constraint error!");
							}
						} else if (constraint2.isEmpty()) {
							for (int j = 0; j < valueArraySize; j++) {
								automataList.add("(s" + stateIndex + ",!"
										+ valueArray.get(j) + ",(),s"
										+ stateIndex + ")");
								if (j != valueArraySize - 1) {
									automataList.add("(s" + stateIndex + ","
											+ valueArray.get(j) + ",(),s"
											+ (stateIndex + 1) + ")");
								} else {
									automataList.add("(s" + stateIndex + ","
											+ valueArray.get(j) + ",(),s"
											+ startStateIndex + "(Accept))");
								}
								stateIndex++;
							}
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}
						// // 然后message
						// if (constraint1.contains("<")
						// || constraint1.contains("<=")) {
						// String constraint1Left = constraint1.substring(0,
						// constraint1.indexOf("<"));
						// String constraint1Right = "";
						// if (constraint1.contains("<=")) {
						// constraint1Right = constraint1
						// .substring(constraint1.indexOf("<=") + 2);
						// } else {
						// constraint1Right = constraint1
						// .substring(constraint1.indexOf("<") + 1);
						// }
						// if (constraint1.contains("<")
						// && !constraint1.contains("<=")) {
						// if (!reset1.isEmpty()) {
						// automataList.add("(s" + stateIndex + ",!"
						// + value1Right + ",(" + constraint1
						// + "),s" + stateIndex + ")");
						// automataList.add("(s"
						// + stateIndex
						// + ","
						// + value1Right
						// + ",("
						// + constraint1
						// + ";"
						// + reset1
						// + "),s"
						// + (startStateIndex
						// + valueArraySize + 2)
						// + "(Error))");
						// automataList.add("(s" + stateIndex + ",empty,("
						// + constraint1Left + ">="
						// + constraint1Right + "),s"
						// + startStateIndex + "(Accept))");
						// stateIndex++;
						// } else if (reset1.isEmpty()) {
						// automataList.add("(s" + stateIndex + ",!"
						// + value1Right + ",(" + constraint1
						// + "),s" + stateIndex + ")");
						// automataList.add("(s"
						// + stateIndex
						// + ","
						// + value1Right
						// + ",("
						// + constraint1
						// + "),s"
						// + (startStateIndex
						// + valueArraySize + 2)
						// + "(Error))");
						// automataList.add("(s" + stateIndex + ",empty,("
						// + constraint1Left + ">="
						// + constraint1Right + "),s"
						// + startStateIndex + "(Accept))");
						// stateIndex++;
						// }
						// } else if (constraint1.contains("<=")) {
						// if (!reset1.isEmpty()) {
						// automataList.add("(s" + stateIndex + ",!"
						// + value1Right + ",(" + constraint1
						// + "),s" + stateIndex + ")");
						// automataList.add("(s"
						// + stateIndex
						// + ","
						// + value1Right
						// + ",("
						// + constraint1
						// + ";"
						// + reset1
						// + "),s"
						// + (startStateIndex
						// + valueArraySize + 2)
						// + "(Error))");
						// automataList.add("(s" + stateIndex + ",empty,("
						// + constraint1Left + ">="
						// + constraint1Right + "),s"
						// + startStateIndex + "(Accept))");
						// stateIndex++;
						// } else if (reset1.isEmpty()) {
						// automataList.add("(s" + stateIndex + ",!"
						// + value1Right + ",(" + constraint1
						// + "),s" + stateIndex + ")");
						// automataList.add("(s"
						// + stateIndex
						// + ","
						// + value1Right
						// + ",("
						// + constraint1
						// + "),s"
						// + (startStateIndex
						// + valueArraySize + 2)
						// + "(Error))");
						// automataList.add("(s" + stateIndex + ",empty,("
						// + constraint1Left + ">="
						// + constraint1Right + "),s"
						// + startStateIndex + "(Accept))");
						// stateIndex++;
						// }
						// }
						// } else {
						// MessageDialog.openError(window.getShell(), "Error",
						// "timed constraint error!");
						// }
					} else if (value1.substring(0, value1.indexOf(":")).equals(
							"r")) {
						// 前缀为r
						// 首先判断PastUnwantedChainConstraint和message的constraint
						// 综合考虑是否存在<或<=，然后才可以进行下一步
						String value1Right = value1.substring(value1
								.indexOf(":") + 1);

						if ((constraint2.contains("<") || constraint2
								.contains("<="))
								&& (constraint1.contains("<") || constraint1
										.contains("<="))) {
							String constraint1Right = "";
							if (constraint1.contains("<=")) {
								constraint1Right = constraint1
										.substring(constraint1.indexOf("<=") + 2);
							} else {
								constraint1Right = constraint1
										.substring(constraint1.indexOf("<") + 1);
							}
							String constraint2Left = constraint2.substring(0,
									constraint2.indexOf("<"));
							String constraint2Right = "";
							if (constraint2.contains("<=")) {
								constraint2Right = constraint2
										.substring(constraint2.indexOf("<=") + 2);
							} else {
								constraint2Right = constraint2
										.substring(constraint2.indexOf("<") + 1);
							}

							if (!reset2.isEmpty()) {
								for (int j = 0; j < valueArraySize; j++) {
									automataList.add("(s" + stateIndex + ",!"
											+ valueArray.get(j) + "&!"
											+ value1Right + ",(" + constraint2
											+ "),s" + stateIndex + ")");
									if (j != valueArraySize - 1) {
										automataList.add("(s" + stateIndex
												+ "," + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ (stateIndex + 1) + ")");
									} else {
										automataList.add("(s" + stateIndex
												+ "," + valueArray.get(j)
												+ ",(" + constraint2 + ";"
												+ reset2 + "),s"
												+ (stateIndex + 1) + ")");
									}
									automataList
											.add("(s"
													+ stateIndex
													+ ","
													+ value1Right
													+ ",("
													+ constraint2
													+ "+"
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 1)
													+ "(Glue))");
									if (constraint2.contains("<=")) {
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">"
												+ constraint2Right
												+ "+"
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
									} else {
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">="
												+ constraint2Right
												+ "+"
												+ constraint1Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
									}
									stateIndex++;
								}
							} else if (reset2.isEmpty()) {
								if (constraint2.equals(constraint1)) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ "&!" + value1Right + ",("
												+ constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										}
										automataList.add("(s"
												+ stateIndex
												+ ","
												+ value1Right
												+ ",("
												+ constraint2
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 1)
												+ "(Glue))");
										if (constraint2.contains("<=")) {
											automataList
													.add("(s"
															+ stateIndex
															+ ",empty,("
															+ constraint2Left
															+ ">"
															+ constraint2Right
															+ "),s"
															+ (startStateIndex
																	+ valueArraySize + 2)
															+ "(Error))");
										} else {
											automataList
													.add("(s"
															+ stateIndex
															+ ",empty,("
															+ constraint2Left
															+ ">="
															+ constraint2Right
															+ "),s"
															+ (startStateIndex
																	+ valueArraySize + 2)
															+ "(Error))");
										}
										stateIndex++;
									}
								} else {
									MessageDialog.openError(window.getShell(),
											"Error", "timed constraint error!");
								}

							}
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}
						// 然后message
						if (constraint1.contains("<")
								|| constraint1.contains("<=")) {
							if (constraint1.contains("<")
									&& !constraint1.contains("<=")) {
								if (!reset1.isEmpty()) {
									automataList.add("(s" + stateIndex + ",!"
											+ value1Right + ",(" + constraint1
											+ "),s" + stateIndex + ")");
									automataList
											.add("(s"
													+ stateIndex
													+ ","
													+ value1Right
													+ ",("
													+ constraint1
													+ ";"
													+ reset1
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								} else if (reset1.isEmpty()) {
									automataList.add("(s" + stateIndex + ",!"
											+ value1Right + ",(" + constraint1
											+ "),s" + stateIndex + ")");
									automataList
											.add("(s"
													+ stateIndex
													+ ","
													+ value1Right
													+ ",("
													+ constraint1
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								}
							} else if (constraint1.contains("<=")) {
								if (!reset1.isEmpty()) {
									automataList.add("(s" + stateIndex + ",!"
											+ value1Right + ",(" + constraint1
											+ "),s" + stateIndex + ")");
									automataList
											.add("(s"
													+ stateIndex
													+ ","
													+ value1Right
													+ ",("
													+ constraint1
													+ ";"
													+ reset1
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								} else if (reset1.isEmpty()) {
									automataList.add("(s" + stateIndex + ",!"
											+ value1Right + ",(" + constraint1
											+ "),s" + stateIndex + ")");
									automataList
											.add("(s"
													+ stateIndex
													+ ","
													+ value1Right
													+ ",("
													+ constraint1
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								}
							}
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}
						// // 最后是Error状态的一个自转换(暂时未添加，还未成熟)
						// automataList.add("(s" + (startStateIndex +
						// valueArraySize
						// + 2)
						// + "(Error),1,empty," + "s"
						// + (startStateIndex + valueArraySize + 2)
						// + "(Error))");
					} else if (value1.substring(0, value1.indexOf(":")).equals(
							"f")) {
						// 前缀为f
						// 首先判断PastUnwantedChainConstraint和message的constraint
						// 综合考虑是否存在<或<=，然后才可以进行下一步
						String value1Right = value1.substring(value1
								.indexOf(":") + 1);

						if ((constraint2.contains("<") || constraint2
								.contains("<="))
								&& (constraint1.contains("<") || constraint1
										.contains("<="))) {
							String constraint1Right = "";
							if (constraint1.contains("<=")) {
								constraint1Right = constraint1
										.substring(constraint1.indexOf("<=") + 2);
							} else {
								constraint1Right = constraint1
										.substring(constraint1.indexOf("<") + 1);
							}

							if (!reset2.isEmpty()) {
								for (int j = 0; j < valueArraySize; j++) {
									automataList.add("(s" + stateIndex + ",!"
											+ valueArray.get(j) + ",("
											+ constraint2 + "),s" + stateIndex
											+ ")");
									if (j != valueArraySize - 1) {
										automataList.add("(s" + stateIndex
												+ "," + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ (stateIndex + 1) + ")");
									} else {
										automataList.add("(s" + stateIndex
												+ "," + valueArray.get(j)
												+ ",(" + constraint2 + ";"
												+ reset2 + "),s"
												+ (stateIndex + 1) + ")");
									}
									automataList
											.add("(s"
													+ stateIndex
													+ ","
													+ value1Right
													+ ",("
													+ constraint2
													+ "+"
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								}
							} else if (reset2.isEmpty()) {
								if (constraint2.equals(constraint1)) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										}
										automataList.add("(s"
												+ stateIndex
												+ ","
												+ value1Right
												+ ",("
												+ constraint2
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 2)
												+ "(Error))");
										stateIndex++;
									}
								} else {
									MessageDialog.openError(window.getShell(),
											"Error", "timed constraint error!");
								}

							}
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}
						// 然后message
						if (constraint1.contains("<")
								|| constraint1.contains("<=")) {
							String constraint1Left = constraint1.substring(0,
									constraint1.indexOf("<"));
							String constraint1Right = "";
							if (constraint1.contains("<=")) {
								constraint1Right = constraint1
										.substring(constraint1.indexOf("<=") + 2);
							} else {
								constraint1Right = constraint1
										.substring(constraint1.indexOf("<") + 1);
							}

							if (constraint1.contains("<")
									&& !constraint1.contains("<=")) {
								if (!reset1.isEmpty()) {
									automataList.add("(s" + stateIndex + ",!"
											+ value1Right + ",(" + constraint1
											+ "),s" + stateIndex + ")");
									automataList
											.add("(s"
													+ stateIndex
													+ ","
													+ value1Right
													+ ",("
													+ constraint1
													+ ";"
													+ reset1
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 1)
													+ "(Glue))");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">="
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								} else if (reset1.isEmpty()) {
									automataList.add("(s" + stateIndex + ",!"
											+ value1Right + ",(" + constraint1
											+ "),s" + stateIndex + ")");
									automataList
											.add("(s"
													+ stateIndex
													+ ","
													+ value1Right
													+ ",("
													+ constraint1
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 1)
													+ "(Glue))");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">="
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								}
							} else if (constraint1.contains("<=")) {
								if (!reset1.isEmpty()) {
									automataList.add("(s" + stateIndex + ",!"
											+ value1Right + ",(" + constraint1
											+ "),s" + stateIndex + ")");
									automataList
											.add("(s"
													+ stateIndex
													+ ","
													+ value1Right
													+ ",("
													+ constraint1
													+ ";"
													+ reset1
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 1)
													+ "(Glue))");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">"
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								} else if (reset1.isEmpty()) {
									automataList.add("(s" + stateIndex + ",!"
											+ value1Right + ",(" + constraint1
											+ "),s" + stateIndex + ")");
									automataList
											.add("(s"
													+ stateIndex
													+ ","
													+ value1Right
													+ ",("
													+ constraint1
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 1)
													+ "(Glue))");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">"
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								}
							}
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}
						// // 最后是Error状态的一个自转换(暂时未添加，还未成熟)
						// automataList.add("(s" + (startStateIndex +
						// valueArraySize
						// + 2)
						// + "(Error),1,empty," + "s"
						// + (startStateIndex + valueArraySize + 2)
						// + "(Error))");
					} else {
						// 其它情况
						MessageDialog.openError(window.getShell(), "Error",
								"timed constraint error!");
					}

					// //////////////////////////////////////////////////////////
					// ////
					// //
					// /////////// == End:PastUnwantedChainConstraint
					// ==/////////////////
					// //////////////////////////////////////////////////////////
					// ////
					// //
				} else if (line.getPresentConstraintValue().isEmpty()
						&& line.getPastUnwantedMessageConstraintValue().isEmpty()
						&& line.getFutureUnwantedMessageConstraintValue().isEmpty()
						&& line.getPresentPastValue().isEmpty()
						&& line.getPresentFutureValue().isEmpty()
						&& line.getPastWantedChainConstraintValue().isEmpty()
						&& line.getFutureWantedChainConstraintValue().isEmpty()
						&& line.getPastUnwantedChainConstraintValue().isEmpty()
						&& !line.getFutureUnwantedChainConstraintValue()
								.isEmpty()) {

					// //////////////////////////////////////////////////////////
					// ////
					// //
					// /////////// == FutureUnwantedChainConstraint
					// ==/////////////////
					// //////////////////////////////////////////////////////////
					// ////
					// //
					// 局部临时变量，易于处理问题
					String value1 = line.getMessageValue();
					String constraint1 = line.getMessageConstraint();
					String reset1 = line.getMessageReset();
					String value2 = line
							.getFutureUnwantedChainConstraintValue();
					String constraint2 = line
							.getFutureUnwantedChainConstraintConstraint();
					String reset2 = line
							.getFutureUnwantedChainConstraintReset();
					String value1Right = value1
							.substring(value1.indexOf(":") + 1);

					// 下面处理FutureUnwantedChainConstraint部分，先分解其value部分
					List<String> valueArray = new ArrayList<String>();
					value2 = value2.substring(value2.indexOf("(") + 1, value2
							.lastIndexOf(")"));
					while (value2.indexOf(",") > 0) {
						valueArray
								.add(value2.substring(0, value2.indexOf(",")));
						value2 = value2.substring(value2.indexOf(",") + 1);
					}
					valueArray.add(value2);
					int valueArraySize = valueArray.size();

					if (value1.substring(0, value1.indexOf(":")).equals("e")) {
						// 前缀为e
						// 首先判断message的constraint是否存在<或<=，然后才可以进行下一步
						if (constraint1.contains("<")
								|| constraint1.contains("<=")) {
							if (constraint1.contains("<")
									&& !constraint1.contains("<=")) {
								String constraint1Left = constraint1.substring(
										0, constraint1.indexOf("<"));
								String constraint1Right = constraint1
										.substring(constraint1.indexOf("<") + 1);
								if (!reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + ";" + reset1
											+ "),s" + (stateIndex + 1) + ")");
									automataList.add("(s" + stateIndex
											+ ",empty,(" + constraint1Left
											+ ">=" + constraint1Right + "),s"
											+ startStateIndex + "(Accept))");
									stateIndex++;
								} else if (reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s"
											+ (stateIndex + 1) + ")");
									automataList.add("(s" + stateIndex
											+ ",empty,(" + constraint1Left
											+ ">=" + constraint1Right + "),s"
											+ startStateIndex + "(Accept))");
									stateIndex++;
								}
							} else if (constraint1.contains("<=")) {
								String constraint1Left = constraint1.substring(
										0, constraint1.indexOf("<="));
								String constraint1Right = constraint1
										.substring(constraint1.indexOf("<=") + 2);
								if (!reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + ";" + reset1
											+ "),s" + (stateIndex + 1) + ")");
									automataList.add("(s" + stateIndex
											+ ",empty,(" + constraint1Left
											+ ">" + constraint1Right + "),s"
											+ startStateIndex + "(Accept))");
									stateIndex++;
								} else if (reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s"
											+ (stateIndex + 1) + ")");
									automataList.add("(s" + stateIndex
											+ ",empty,(" + constraint1Left
											+ ">" + constraint1Right + "),s"
											+ startStateIndex + "(Accept))");
									stateIndex++;
								}
							}
						} else if (constraint1.isEmpty()) {
							automataList.add("(s" + stateIndex + ","
									+ value1Right + ",(" + reset1 + "),s"
									+ (stateIndex + 1) + ")");
							stateIndex++;
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}

						if (constraint2.contains("<")
								|| constraint2.contains("<=")) {
							if (constraint2.contains("<")
									&& !constraint2.contains("<=")) {
								String constraint2Left = constraint2.substring(
										0, constraint2.indexOf("<"));
								String constraint2Right = constraint2
										.substring(constraint2.indexOf("<") + 1);
								if (!reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2 + ";"
													+ reset2 + "),s"
													+ startStateIndex
													+ "(Accept))");
										}
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">="
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 1)
												+ "(Glue))");
										stateIndex++;
									}
								} else if (reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + startStateIndex
													+ "(Accept))");
										}
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">="
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 1)
												+ "(Glue))");
										stateIndex++;
									}
								}
							} else if (constraint2.contains("<=")) {
								String constraint2Left = constraint2.substring(
										0, constraint2.indexOf("<="));
								String constraint2Right = constraint2
										.substring(constraint2.indexOf("<=") + 2);
								if (!reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2 + ";"
													+ reset2 + "),s"
													+ startStateIndex
													+ "(Accept))");
										}
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">"
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 1)
												+ "(Glue))");
										stateIndex++;
									}
								} else if (reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + startStateIndex
													+ "(Accept))");
										}
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">"
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 1)
												+ "(Glue))");
										stateIndex++;
									}
								}
							}
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}
					} else if (value1.substring(0, value1.indexOf(":")).equals(
							"r")) {
						// 前缀为r
						// 首先判断message的constraint是否存在<或<=，然后才可以进行下一步
						if (constraint1.contains("<")
								|| constraint1.contains("<=")) {
							if (constraint1.contains("<")
									&& !constraint1.contains("<=")) {
								String constraint1Left = constraint1.substring(
										0, constraint1.indexOf("<"));
								String constraint1Right = constraint1
										.substring(constraint1.indexOf("<") + 1);
								if (!reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + ";" + reset1
											+ "),s" + (stateIndex + 1) + ")");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">="
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								} else if (reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s"
											+ (stateIndex + 1) + ")");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">="
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								}
							} else if (constraint1.contains("<=")) {
								String constraint1Left = constraint1.substring(
										0, constraint1.indexOf("<="));
								String constraint1Right = constraint1
										.substring(constraint1.indexOf("<=") + 2);
								if (!reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + ";" + reset1
											+ "),s" + (stateIndex + 1) + ")");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">"
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								} else if (reset1.isEmpty()) {
									automataList.add("(s"
											+ stateIndex
											+ ",!"
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s" + stateIndex
											+ ")");
									automataList.add("(s"
											+ stateIndex
											+ ","
											+ value1.substring(value1
													.indexOf(":") + 1) + ",("
											+ constraint1 + "),s"
											+ (stateIndex + 1) + ")");
									automataList
											.add("(s"
													+ stateIndex
													+ ",empty,("
													+ constraint1Left
													+ ">"
													+ constraint1Right
													+ "),s"
													+ (startStateIndex
															+ valueArraySize + 2)
													+ "(Error))");
									stateIndex++;
								}
							}
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}

						if (constraint2.contains("<")
								|| constraint2.contains("<=")) {
							if (constraint2.contains("<")
									&& !constraint2.contains("<=")) {
								String constraint2Left = constraint2.substring(
										0, constraint2.indexOf("<"));
								String constraint2Right = constraint2
										.substring(constraint2.indexOf("<") + 1);
								if (!reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2 + ";"
													+ reset2 + "),s"
													+ (stateIndex + 2)
													+ "(Error))");
										}
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">="
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 1)
												+ "(Glue))");
										stateIndex++;
									}
								} else if (reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 2)
													+ "(Error))");
										}
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">="
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 1)
												+ "(Glue))");
										stateIndex++;
									}
								}
							} else if (constraint2.contains("<=")) {
								String constraint2Left = constraint2.substring(
										0, constraint2.indexOf("<="));
								String constraint2Right = constraint2
										.substring(constraint2.indexOf("<=") + 2);
								if (!reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2 + ";"
													+ reset2 + "),s"
													+ +(stateIndex + 2)
													+ "(Error))");
										}
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">"
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 1)
												+ "(Glue))");
										stateIndex++;
									}
								} else if (reset2.isEmpty()) {
									for (int j = 0; j < valueArraySize; j++) {
										automataList.add("(s" + stateIndex
												+ ",!" + valueArray.get(j)
												+ ",(" + constraint2 + "),s"
												+ stateIndex + ")");
										if (j != valueArraySize - 1) {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 1)
													+ ")");
										} else {
											automataList.add("(s" + stateIndex
													+ "," + valueArray.get(j)
													+ ",(" + constraint2
													+ "),s" + (stateIndex + 2)
													+ "(Error))");
										}
										automataList.add("(s"
												+ stateIndex
												+ ",empty,("
												+ constraint2Left
												+ ">"
												+ constraint2Right
												+ "),s"
												+ (startStateIndex
														+ valueArraySize + 1)
												+ "(Glue))");
										stateIndex++;
									}
								}
							}
						} else {
							MessageDialog.openError(window.getShell(), "Error",
									"timed constraint error!");
						}
					} else {
						// 其它情况
						MessageDialog.openError(window.getShell(), "Error",
								"timed constraint error!");
					}

					// //////////////////////////////////////////////////////////
					// ////
					// //
					// /////////// == End: FutureUnwantedChainConstraint
					// ==////////////
					// //////////////////////////////////////////////////////////
					// ////
					// //
				}
			}
			for (int i = 0; i < automataList.size(); i++) {
				out.println(automataList.get(i));
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		InputStream in;
		try {
			in = new FileInputStream(automataFileName);
			byte[] inputByte = new byte[in.available()];

			in.read(inputByte);
			String contentString = new String(inputByte);

			text.setText(contentString);
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long endTime=System.currentTimeMillis(); //获取结束时间  
		System.out.println("Time for transfering PSC to Automate is "+( endTime - startTime )+"ms"); 
		
		
		*/
	}
	
}
