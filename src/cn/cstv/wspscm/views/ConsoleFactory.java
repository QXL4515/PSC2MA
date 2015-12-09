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
 * File: ConsoleFactory.java                                                   
 * Program: ConsoleFactory                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-8-27                                                        
 ***********************************************************************/

/**
 * 
 */
package cn.cstv.wspscm.views;

import java.io.PrintStream;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 */
public class ConsoleFactory implements IConsoleFactory {

	static MessageConsole console = new MessageConsole("Result",
			null);

	public static void closeConsole() {
		IConsoleManager manager = ConsolePlugin.getDefault()
				.getConsoleManager();
		if (console != null) {
			manager.removeConsoles(new IConsole[] { console });
		}
	}

	public static MessageConsole getConsole() {
		return console;
	}

	public static void showConsole() {
		if (console != null) {
			IConsoleManager manager = ConsolePlugin.getDefault()
					.getConsoleManager();
			IConsole[] existing = manager.getConsoles();
			boolean exists = false;
			for (int i = 0; i < existing.length; i++) {
				if (console == existing[i])
					exists = true;
			}
			if (!exists) {
				manager.addConsoles(new IConsole[] { console });
			}
			manager.showConsoleView(console);

			MessageConsoleStream stream = console.newMessageStream();

			System.setOut(new PrintStream(stream));
			System.setErr(new PrintStream(stream));
		}
	}

	public void openConsole() {
		showConsole();
	}

}
