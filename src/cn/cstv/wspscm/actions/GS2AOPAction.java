package cn.cstv.wspscm.actions;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import cn.cstv.wspscm.IImageKeys;
import cn.cstv.wspscm.views.GameStructureView;

public class GS2AOPAction extends Action implements ISelectionListener, 
										IWorkbenchAction{
	public static final String ID = "cn.cstv.wspscm.actions.GS2AOPAction";

	private IWorkbenchWindow window;

	public GS2AOPAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("GS to AOP");
		setToolTipText("Transfer GS to AOP");
		setImageDescriptor(IImageKeys
				.getImageDescriptor(IImageKeys.TRANSFERPSCTOGAMESTRUCTURE));
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

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	public void run(){
		final String SYSTEMERRBEGIN = "System.out.println(";
		final String SYSTEMERREND = ");";
		final String BUNDLE = "Bundle ";
		final String MESSAGE = "Message ";
		final String TO = " ---> ";
		IViewPart view;
		Text text = null;
		StringBuilder sb = new StringBuilder();
		try {
			view = window.getActivePage().showView(GameStructureView.ID);
			text = ((GameStructureView) view).getTextArray().get(0);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String gs = text.getText();
		ArrayList<String> messages = new ArrayList<String>();
		ArrayList<String> bundles = new ArrayList<String>();
		HashMap<String, String> messageToState = new HashMap<String, String>();
		HashMap<String, String> stateToStateContent = new HashMap<String, String>();
		
		while(gs.contains("State")){
			/*traverse the gs ,find all the states, messages and bundles
			 *map the states and messages
			 * */
			
			String state1 = gs.substring(0, gs.indexOf('['));
			String stateContent = gs.substring(gs.indexOf('[') + 1, gs.indexOf(']'));
			stateToStateContent.put(state1.trim(), stateContent.trim());
			
			while(stateContent.contains("-->")){
				String message = stateContent.substring(0, stateContent.indexOf("-->"));
//				the message might be sent
				message = message.trim();
				stateContent = stateContent.substring(stateContent.indexOf("-->") + 1);
				String state = stateContent.substring(stateContent.indexOf('s'), stateContent.indexOf(')') + 1);
//				the possible state
				stateContent = stateContent.substring(stateContent.indexOf(')') + 1);
				state = state.trim();
				messages.add(message.trim());
				messageToState.put(message, state);
//				map the messages(including !message) and the states
				String bundle = message.substring(0, message.indexOf(':'));
				
				if(bundle.contains("!"))
//					if the message is a !message remove the '!'
					bundle = bundle.substring(bundle.indexOf('!') + 1);
				if(!bundles.contains(bundle))
//					if the bundles does not contain the bundle add it  
					bundles.add(bundle);
				
				bundle = message.substring(message.lastIndexOf(':') + 1, message.indexOf('('));
//				another bundle in the message
				if(!bundles.contains(bundle))
					bundles.add(bundle);
			}
			gs = gs.substring(gs.indexOf(']') + 1);
//			trim the string, remove the handled state
			
			/*
			 * a state likes:
			 * s0(InfiniteControlableState)[
			 * !User:inputAccount:LoginWeb(input;) --> s0(InfiniteControlableState)
			 * User:inputAccount:LoginWeb(input;) --> s1(SystemFiniteControlableState)
			 * ]
			 * */
		}
		
		Iterator<String> bundleIterator = bundles.iterator();
		String method = null;
		
		while(bundleIterator.hasNext()){
//			each bundle has a monitor
			String temp = text.getText();
			Iterator<String> messageIterator = messages.iterator();
			String tempBundle = bundleIterator.next().toString();
			sb.append("**********************************************" + "Monitor of " + tempBundle + "**********************************************" + "\n");
			sb.append("public aspect " + tempBundle + "Aspect" + " {" + "\n");
			sb.append("\t" + "String time = null;" + "\n");
			if(tempBundle.equals(temp.substring(temp.indexOf('!') + 1, temp.indexOf(':')))){
				sb.append("\t" + "after() :execution(" + ")" + "{" + "\n");
				sb.append("\t" + "\t" + "time = Calendar.getInstance().getTime().toString()" + "+" + '"' + ": " + '"' + ";"+ "\n");
				sb.append("\t" + "\t" + SYSTEMERRBEGIN + "time" + " + " + '"' + BUNDLE 
						+ tempBundle + " is in the state of " + temp.subSequence(0, temp.indexOf(')') + 1) + '"' + SYSTEMERREND + "\n");
				sb.append("\t" + "}" + "\n");
			}
			while(messageIterator.hasNext()){
				String tempMessage = messageIterator.next().toString();
				if(tempMessage.contains(tempBundle)&&!tempMessage.contains("!")){
//					message contain bundle(maybe sender or receiver) and does not contain '!'
					if(tempBundle.equals(tempMessage.substring(0, tempMessage.indexOf(':'))))
//						if bundle is the sender of message, the method is before the ';'
						method = tempMessage.substring(tempMessage.indexOf('(') + 1, tempMessage.indexOf(';'));
					else
//						if bundle is the receiver of message, the method is before the ';'
						method = tempMessage.substring(tempMessage.indexOf(';') + 1, tempMessage.indexOf(')'));
					if(method.length()>0)
//						assume the method return a void data
						method = "void " + method + "()";
					
					String anotherBundle = null;
					if(tempMessage.substring(0, tempMessage.indexOf(':')).equals(tempBundle)){
//						bundle is the sender of message
						anotherBundle = tempMessage.substring(tempMessage.lastIndexOf(':') + 1, tempMessage.indexOf('('));
						String message = tempMessage.substring(tempMessage.indexOf(':') + 1, tempMessage.lastIndexOf(':'));
//						after the method is executed
						sb.append("\t" + "after() :execution(" + method + ")" + "{" + "\n");
						sb.append("\t" + "\t" + "time = Calendar.getInstance().getTime().toString()" + "+" + '"' + ": " + '"' + ";" + "\n" +
								"\t" + "\t" + SYSTEMERRBEGIN + "time" + " + " + '"' + message + ":" + tempBundle + TO + anotherBundle + '"' 
								+ SYSTEMERREND + "\n");
						if(null != messageToState.get("!" + tempMessage + "(step > Max)")){
							sb.append("\t" + "\t" + "Monitor.setMessageSended(true);" + "\n");
						}
						sb.append("\t" + "}" + "\n");
					}else{
//						if bundle is the receiver of message
						anotherBundle = tempMessage.substring(0, tempMessage.indexOf(':'));
						
//						after the method is executed
						sb.append("\t" + "after() :execution(" + method + ")" + "{" + "\n");
						sb.append("\t" + "\t" + "time = Calendar.getInstance().getTime().toString()" + "+" + '"' + ": " + '"' + ';' + "\n" );
						sb.append("\t" + "\t" + SYSTEMERRBEGIN + "time" + " + " + '"' + BUNDLE 
								+ tempBundle + " got the Message " + '"' + SYSTEMERREND + "\n");
						sb.append("\t" + "\t" + SYSTEMERRBEGIN + "time" + " + " + '"' + BUNDLE 
								+ tempBundle + " is in the state of " + messageToState.get(tempMessage) + '"' + SYSTEMERREND + "\n");
						if(null != stateToStateContent.get(messageToState.get(tempMessage))){
							if(stateToStateContent.get(messageToState.get(tempMessage)).contains("(step > Max)")){
								sb.append("\t" + "\t" + "Monitor.setStartTime(System.currentTimeMillis());" + "\n");
								sb.append("\t" + "\t" + "Monitor.setViolationState(" + '"' + BUNDLE + tempBundle + "is in the state of "  + 
										messageToState.get(stateToStateContent.get(messageToState.get(tempMessage)).substring(
												stateToStateContent.get(messageToState.get(tempMessage)).lastIndexOf('!'), 
												stateToStateContent.get(messageToState.get(tempMessage)).indexOf("(step > Max)") + 12)) + '"' + SYSTEMERREND + "\n");
								//son do not even try to read this code, u will never understand it and neither do i 
								sb.append("\t" + "\t" + "new Monitor();" + "\n");
							}
						}
						sb.append("\t" + "}" + "\n");
						/*sb.append("\t" + "\t" + "System.out.println(" + '"' + tempBundle + " is in the state of " 
								 + messageToState.get(tempMessage) + '"' + ")" + "\n");*/
					}
//					sb.append("\t" + "}" + "\n");
				}
//				else if (tempMessage.contains(tempBundle)&&tempMessage.contains("!")){
////					message contain bundle(maybe sender or receiver) and does contain '!'
//					if(messageToState.get(tempMessage).contains("Violation")){
//						String anotherBundle = null;
//						if(tempMessage.substring(1, tempMessage.indexOf(':')).equals(tempBundle)){
////							bundle is the sender of message
//							anotherBundle = tempMessage.substring(tempMessage.lastIndexOf(':') + 1, tempMessage.indexOf('('));
//							String message = tempMessage.substring(tempMessage.indexOf(':') + 1, tempMessage.lastIndexOf(':'));
////							after the method is executed
//							sb.append("\t" + "after() :execution(" + method + ")" + "{" + "\n");
//							sb.append("\t" + "\t" + "time = Calendar.getInstance().getTime().toString()" + "+" + '"' + ": " + '"' + ";" + "\n" +
//									"\t" + "\t" + SYSTEMERRBEGIN + "time" + " + " + '"' + message + ":" + tempBundle + TO + anotherBundle + '"' 
//									+ SYSTEMERREND + "\n");
//							sb.append("\t" + "\t" + "Monitor.setMessageSended(true);" + "\n");
//							sb.append("\t" + "}" + "\n");
//						}else{
////							if bundle is the receiver of message
//							anotherBundle = tempMessage.substring(1, tempMessage.indexOf(':'));
//							
////							after the method is executed
//							sb.append("\t" + "after() :execution(" + method + ")" + "{" + "\n");
//							sb.append("\t" + "\t" + "time = Calendar.getInstance().getTime().toString()" + "+" + '"' + ": " + '"' + ';' + "\n" );
//							sb.append("\t" + "\t" + SYSTEMERRBEGIN + "time" + " + " + '"' + BUNDLE 
//									+ tempBundle + " got the Message " + '"' + SYSTEMERREND + "\n");
//							sb.append("\t" + "\t" + SYSTEMERRBEGIN + "time" + " + " + '"' + BUNDLE 
//									+ tempBundle + " is in the state of " + messageToState.get(tempMessage) + '"' + SYSTEMERREND + "\n");
//							sb.append("\t" + "\t" + "Monitor.setStartTime(System.currentTimeMillis());" + "\n");
//							sb.append("\t" + "\t" + "Monitor.setValiationState(" + '"' + BUNDLE + tempBundle + "is in the state of "  + 
//									messageToState.get(tempMessage) + '"' + SYSTEMERREND + "\n");
//							sb.append("\t" + "\t" + "new Monitor();" + "\n");
//							sb.append("\t" + "}" + "\n");
//							/*sb.append("\t" + "\t" + "System.out.println(" + '"' + tempBundle + " is in the state of " 
//									 + messageToState.get(tempMessage) + '"' + ")" + "\n");*/
//						}
//					}
//				}
			}
			
			sb.append("}" + "\n");
		}
		sb.append("**********************************************" + "The Monitor of All Bundles" + "**********************************************" + "\n");
		System.out.print(sb.toString());
		
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(new File("D:/Monitor.java"));
			br = new BufferedReader(fr);
			String line = null;
			while(null != (line = br.readLine())){
				System.out.println(line);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("The Monitor is not in the d:/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				fr.close();
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
