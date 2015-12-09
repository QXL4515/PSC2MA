/*************************************************************************
 * Copyright (c) 2006, 2008. All rights reserved. This program and the   
 * accompanying materials are made available under the terms of the      
 * Eclipse Public License v1.0 which accompanies this distribution,       
 * and is available at http://www.eclipse.org/legal/epl-v10.html         
 * 
 * Contributors:                                                         
 * Author: Su Zhiyong & Zhang Pengcheng                                 
 * Group: CSTV (Chair of Software Testing & Verification) Group          
 * E-mail: zhiyongsu@gmail.com, pchzhang@seu.edu.cn                     
 ***********************************************************************/

/***********************************************************************
 * Project: cn.cstv.wspscm                                          
 * Package: cn.cstv.wspscm.views                                            
 * File: MessageView.java                                                   
 * Program: MessageView                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-8-27                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.views;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.internal.console.IOConsoleViewer;
import org.eclipse.ui.part.ViewPart;

import cn.cstv.wspscm.IImageKeys;
import cn.cstv.wspscm.actions.GenerateRandomMessagesAction;
import cn.cstv.wspscm.actions.InsertTriggerAction;
import cn.cstv.wspscm.actions.OpenMessageLogFileAction;
import cn.cstv.wspscm.monitor.AnalyzeMessageSequence;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 * 
 */
public class MessageView extends ViewPart {

	public static final String ID = "cn.cstv.wspscm.views.MessageView";

	private long lastTimeFileSize = 0; // 上次文件大小
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	private Text text;
	private OpenMessageLogFileAction openMessageLogFileAction;
	// private String contentString = "";
	private GenerateRandomMessagesAction generateRandomMessagesAction;
	private InsertTriggerAction insertTriggerAction;
	private IOConsoleViewer iov;
	private MessageConsoleStream stream;
	private List<String> messageString = new ArrayList<String>();

	@SuppressWarnings("restriction")
	@Override
	public void createPartControl(Composite parent) {

		MessageConsole mainConsole = MConsoleFactory.CONSOLE_MESSAGE
				.getMessageConsole();
		mainConsole.setWaterMarks(5000, 80000);
		// tcv = new TextConsoleViewer(parent, mainConsole);

		stream = mainConsole.newMessageStream();

		MessageConsole mainConsole2 = MConsoleFactory.DEBUG_SYSTEM
				.getMessageConsole();

		MessageConsoleStream stream2 = mainConsole2.newMessageStream();

		System.setOut(new PrintStream(stream2));
		System.setErr(new PrintStream(stream2));

		/* 如果这个console不是在第一个lab，当切换到该lab时，如果要自动滚屏到最后一条输出，需要加下面这行 */
		// toTopIndex(tcv);
		// tcv.addTextListener(new ITextListener() {
		// public void textChanged(TextEvent event) {
		// toTopIndex(tcv);
		// }
		// });

		/**
		 * 下面2行注释的代码也是与上面的toTopIndex具有相同的功效， 只是看起来有点别扭，因为会延迟50毫秒，具体请参照Eclipse的代码
		 */
		iov = new IOConsoleViewer(parent, mainConsole);
		iov.setAutoScroll(true);

		// text = new Text(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		// text.setBackground(ColorConstants.lightGreen);
		// text.setText("2008-04-10 09:51 [Monitor LOG]Entering:[receive]startAssistance(Id60235)\n2008-04-10 09:52 [Monitor LOG]Entering:[on Message]detectVitalParameters(vitalParameters)\n2008-04-10 09:53 [Monitor LOG]Entering:[invoke]analyzeData(vitalParameters)\n2008-04-10 10:20 [Monitor LOG]Exiting:[invoke]analyzeData(low)\n2008-04-10 10:22 [Monitor LOG]Entering:[invoke]alarm(low)\n2008-04-10 12:00 [Monitor LOG]Exiting:[invoke]alarm(beginAssistence)\n2008-04-13 09:15 [Monitor LOG]Entering:[on Message]detectVitalParameters(vitalParameters)\n2008-04-13 09:20 [Monitor LOG]Entering:[invoke]analyzeData(vitalParameters)\n2008-04-13 09:51 [Monitor LOG]Exiting:[invoke]analyzeData(vitalParameters)\n2008-04-13 09:52 [Monitor LOG]Entering:[invoke]changeDiagosis(needDiagosis)\n2008-04-14 09:53 [Monitor LOG]Exiting:[invoke]changeDiagosis(beginDiagosis)\n2008-04-15 09:51 [Monitor LOG]Entering:[on Message]panicButtonAlarm(high)\n2008-04-15 09:54 [Monitor LOG]Entering:[invoke]alarm(high)\n2008-04-15 10:30 [Monitor LOG]Exiting:[invoke]alarm(beginAssistence)\n2008-04-17 11:51 [Monitor LOG]Entering:[on Message]panicButtonAlarm(high)\n2008-04-17 12:21 [Monitor LOG]Entering:[invoke]alarm(high)\n2008-04-17 13:00 [Monitor LOG]Exiting:[invoke]alarm(beginAssistence)\n2008-04-19 06:29 [Monitor LOG]Entering:[on Message]panicButtonAlarm(high)\n2008-04-19 06:31 [Monitor LOG]Entering:[invoke]alarm(high)\n2008-04-19 07:00 [Monitor LOG]Exiting:[invoke]alarm(beginAssistence)\n2008-04-21 05:00 [Monitor LOG]Entering:[on Message]panicButtonAlarm(high)\n2008-04-21 05:03 [Monitor LOG]Entering:[invoke]alarm(high)\n2008-04-21 05:41 [Monitor LOG]Exiting:[invoke]alarm(beginAssistence)\n2008-04-25 09:51 [Monitor LOG]Entering:[on Message]panicButtonAlarm(low)\n2008-04-25 09:55 [Monitor LOG]Entering:[invoke]alarm(low)\n2008-04-25 11:51 [Monitor LOG]Exiting:[invoke]alarm(beginAssistence)\n2008-05-03 15:21 [Monitor LOG]Entering:[on Message]panicButtonAlarm(low)\n2008-04-10 15:28 [Monitor LOG]Entering:[invoke]alarm(low)\n2008-04-10 17:30 [Monitor LOG]Exiting:[invoke]alarm(beginAssistence)\n2008-05-10 10:00 [Monitor LOG]Entering:[on Message]stopAssistance(stopping)");
		// text.setText(contentString);
		createActions();
		initializeToolBar();
		initializeMenu(); // TODO Auto-generated method stub
		try {
			inputMessageProcess();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// job = null;
		// StartJob();
	}

	private void inputMessageProcess() throws IOException {

		// 启动一个线程每10秒钟读取新增的日志信息
		ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
		exec.scheduleWithFixedDelay(new Runnable() {
			public void run() {
				try {
					String filename = "C:/";
					filename += (new SimpleDateFormat("yyyyMMddHH"))
							.format(new Date());
					filename += ".message";
					// IImageKeys.messageLogFileName = "C:/AspectJLog.message";
					IImageKeys.messageLogFileName = filename;

					File tmpLogFile = new File(filename);
					if (!tmpLogFile.exists()) {
						tmpLogFile.createNewFile();
						lastTimeFileSize = 0;
					}

					final RandomAccessFile randomFile = new RandomAccessFile(
							tmpLogFile, "rw");
					// contentString = "";
					// 获得变化部分的
					randomFile.seek(lastTimeFileSize);
					String tmp = "";
					messageString.clear();
					while ((tmp = randomFile.readLine()) != null) {
						// text.append(tmp);
						// contentString += tmp + "\n";
						stream.println(tmp);
						// System.out.println(tmp);
						messageString.add(tmp);
					}
					if (messageString.size() > 0) {
						AnalyzeMessageSequence
								.executeVerificationByKStepsRealTimeWithMultiAutomata(messageString);
					}
					lastTimeFileSize = randomFile.length();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}, 0, 3, TimeUnit.SECONDS);

		// System.setOut(stream);
		// String path = "C:\\AspectJLog.message";
		// IImageKeys.messageLogFileName = path;
		// if (path != null) {
		// InputStream is = null;
		// try {
		// is = new FileInputStream(path);
		//
		// // TODO Auto-generated catch block
		//
		// byte[] inputByte = new byte[is.available()];
		//
		// is.read(inputByte);
		// String contentString = new String(inputByte);
		// this.text.setText(contentString);
		// is.close();
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// job = null;
	}

	/**
	 * 
	 */

	private void createActions() {
		// TODO Auto-generated method stub
		generateRandomMessagesAction = new GenerateRandomMessagesAction(
				getSite().getWorkbenchWindow());
		openMessageLogFileAction = new OpenMessageLogFileAction(getSite()
				.getWorkbenchWindow());
		insertTriggerAction = new InsertTriggerAction(getSite()
				.getWorkbenchWindow());
	}

	/**
	 * @return the text
	 */
	public Text getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(Text text) {
		this.text = text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private void initializeToolBar() {
		IToolBarManager toolBarManager = getViewSite().getActionBars()
				.getToolBarManager();
		toolBarManager.add(openMessageLogFileAction);
		toolBarManager.add(generateRandomMessagesAction);
		toolBarManager.add(insertTriggerAction);
	}

	private void initializeMenu() {
		// TODO Auto-generated method stub
		IMenuManager manager = getViewSite().getActionBars().getMenuManager();
		manager.add(openMessageLogFileAction);
		manager.add(generateRandomMessagesAction);
		manager.add(insertTriggerAction);
	}

	/**
	 * 自动滚屏
	 * 
	 * @param tcv
	 */
	// private void toTopIndex(final TextConsoleViewer tcv) {
	// StyledText textWidget = tcv.getTextWidget();
	// if (textWidget != null && !textWidget.isDisposed()) {
	// int lineCount = textWidget.getLineCount();
	// tcv.setTopIndex(lineCount - 1);
	// }
	// }

	/**
	 * 这个才是控制台的名字
	 */
	public String getPartName() {
		return "Message";
	}

}
