package cn.cstv.wspscm;

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
 * Project: cn.cstv.wsrt                                          
 * Package: cn.cstv.wsrt                                            
 * File: IImageKeys.java                                                   
 * Program: IImageKeys                                                
 * Version: J2SE-1.6.0                                                  
 * Date: 2008-7-19                                                        
 ***********************************************************************/

/**
 * 
 */

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * @author Su Zhiyong & Zhang Pengcheng
 *
 * Set the Image Keys or ImageDescriptor, then convenience to use.
 */
public class IImageKeys {

	private static ImageRegistry imageRegistry;
	private final static String ICONS_PATH = "icons/";

	public static final String FILE = "FILE";
	public final static String LIFE_LINE = "LIFE_LINE";
//	public final static String DIAGRAM = "DIAGRAM";
	public static final String ARROW_CONNECTION = "ARROW_CONNECTION";
	public static final String SELECT_ALL = "SELECT_ALL";
	public static final String OPERATOR = "OPERATOR";
	public static final String PRESENTCONSTRAINT = "PRESENTCONSTRAINT";
	public static final String OPENBOOLEANCON = "OPENBOOLEANCON";
	public static final String STRICT = "STRICT";
	public static final String UNWANTEDMESSAGE = "UNWANTEDMESSAGE";
	public static final String OPENCHAINCON = "OPENCHAINCON";
	public static final String CHAINCON = "CHAINCON";
	public static final String TRANSFERPSCTOAUTOMATE = "TRANSFERPSCTOAUTOMATE";
	public static final String TRANSFERPSCTOGAMESTRUCTURE = "TRANSFERPSCTOGAMESTRUCTURE";
	public static final String LOGFILE = "LOGFILE";
	public static final String GETRESULT = "GETRESULT";
	public static final String PROJECT = "PROJECT";
	public static final String OPENFILE = "OPENFILE";
	public static final String ANALYZE = "ANALYZE";
	public static final String GENERATE = "GENERATE";
	public static final String RUNTOOL = "RUNTOOL";
	public static final String PSCMONITOR = "PSCMONITOR";
	public static final String NAVIGATOR = "NAVIGATOR";
	public static final String REFINEMENT = "REFINEMENT";
	public static final String TEST = "TEST";
	public static final String TRIGGER = "TRIGGER";
	public static final String LOOKAHEAD = "LOOKAHEAD";
	public static final String GENEAUTO = "GENEAUTO";
	public static final String JCONSOLE = "JCONSOLE";

	
	//public static String automataFileName="";
	public static String messageLogFileName="";
	public static String bpelFileName="";
	public static List<String> automataFilesName = new ArrayList<String>();
	public static List<String> gameStructureFilesName = new ArrayList<String>();
	public static List<String> PSCFilesName=new ArrayList<String>();


	/**
	 * 
	 */
	private final static void declareImages() {
		declareRegistryImage(FILE, ICONS_PATH + "openFile.gif");
		
		declareRegistryImage(LIFE_LINE, ICONS_PATH + "life16.gif");

		declareRegistryImage(TEST, ICONS_PATH + "test.gif");

		declareRegistryImage(ARROW_CONNECTION, ICONS_PATH + "line116.gif");

		declareRegistryImage(SELECT_ALL, ICONS_PATH + "allteamstrms_rep.gif");

		declareRegistryImage(OPERATOR, ICONS_PATH + "com16.gif");

		declareRegistryImage(PRESENTCONSTRAINT, ICONS_PATH + "cross16.gif");

		declareRegistryImage(OPENBOOLEANCON, ICONS_PATH + "circle16.gif");
		
		declareRegistryImage(STRICT, ICONS_PATH + "strict.gif");

		declareRegistryImage(UNWANTEDMESSAGE, ICONS_PATH + "circle416.gif");

		declareRegistryImage(OPENCHAINCON, ICONS_PATH + "arrow316.gif");

		declareRegistryImage(CHAINCON, ICONS_PATH + "arrow416.gif");
		
		declareRegistryImage(TRANSFERPSCTOAUTOMATE, ICONS_PATH + "transferPSCtoAutomate.gif");
		
		declareRegistryImage(TRANSFERPSCTOGAMESTRUCTURE, ICONS_PATH + "transferPSCtoGameStructure.gif");
		
		declareRegistryImage(LOGFILE, ICONS_PATH + "logFile.gif");
		
		declareRegistryImage(GETRESULT, ICONS_PATH + "getResult.gif");		
		
		declareRegistryImage(PROJECT, ICONS_PATH + "folder_package.gif");
		
		declareRegistryImage(OPENFILE, ICONS_PATH + "folder_open.gif");
		
		declareRegistryImage(ANALYZE, ICONS_PATH + "analyze.gif");
		
		declareRegistryImage(GENERATE, ICONS_PATH + "generate.gif");

		declareRegistryImage(RUNTOOL, ICONS_PATH + "run.gif");
		
		declareRegistryImage(PSCMONITOR, ICONS_PATH + "psc-monitor.gif");
		
		declareRegistryImage(NAVIGATOR, ICONS_PATH + "nav.gif");
		
		declareRegistryImage(REFINEMENT, ICONS_PATH + "refinement.gif");
				
		declareRegistryImage(TRIGGER, ICONS_PATH + "trigger.gif");

		declareRegistryImage(LOOKAHEAD, ICONS_PATH + "ari.gif");
		
		declareRegistryImage(GENEAUTO,ICONS_PATH + "gene_auto.gif");
		
		declareRegistryImage(JCONSOLE,ICONS_PATH + "find.gif");

}

	/**
	 * 
	 * @param key
	 * @param path
	 */
	private final static void declareRegistryImage(String key, String path) {
		try{
			URL url = FileLocator.find(Activator.getDefault().getBundle(), new Path(path), null);
			if(url != null){
				ImageDescriptor desc = ImageDescriptor.createFromURL(url);
				getImageRegistry().put(key, desc);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public static Image getImage(String key) {
		return getImageRegistry().get(key);
	}

	/**
	 * 
	 * @param key
	 * @return ImageDescriptor
	 */
	public static ImageDescriptor getImageDescriptor(String key) {
		return getImageRegistry().getDescriptor(key);
	}

	/**
	 * 
	 * @return ImageRegistry
	 */
	public static ImageRegistry getImageRegistry() {
		if (imageRegistry == null) {
			imageRegistry = new ImageRegistry();
			declareImages();
		}
		return imageRegistry;
	}
}
