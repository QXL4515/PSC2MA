package cn.cstv.wspscm.actions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
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
import cn.cstv.wspscm.model.EnvironmentLifeline;
import cn.cstv.wspscm.model.Lifeline;
import cn.cstv.wspscm.model.LineConnection;
import cn.cstv.wspscm.model.Operator;
import cn.cstv.wspscm.views.GameStructureView;

public class PSC2GameStructureAction extends Action implements ISelectionListener, 
										IWorkbenchAction{
	public static final String ID = "cn.cstv.wspscm.actions.PSC2GameStructureAction";
	private static final String EICS = "(EnvironmentInfiniteControlableState)";
	private static final String ICS = "(SystemInfiniteControlableState)";
	private static final String SFCS = "(SystemFiniteControlableState)";
	private static final String EFCS = "(EnvironmentFiniteControlableState)";
	private static final String ERR = "(Violation)";
	private static final String SECS = "(SystemUrgentControlableState)";
	private static final String EECS = "(EnvironmentUrgentControlableState)";
	private static final String UNKNOWN = "";
	private String L = "(step <= Max)";
	private String UL = "(step > Max)";
	private static final String TO =" --> ";

	private IWorkbenchWindow window;
	private HashMap <Integer, String> state = new HashMap<Integer, String>();

	public PSC2GameStructureAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("PSC to GameStructure");
		setToolTipText("Transfer PSC to GameStructure");
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
		long startTime = System.currentTimeMillis();   //锟斤拷取锟斤拷始时锟斤拷 
		
		IViewPart view;
		IFile gameStructureFile = ((IFileEditorInput) window.getActivePage()
				.getActiveEditor().getEditorInput()).getFile();// store the gs file
		String gameStructureFileName = gameStructureFile.getLocation().toString();
		
		gameStructureFileName = gameStructureFileName.substring(0, gameStructureFileName
				.lastIndexOf(".")) + ".gs";				
		
		IImageKeys.gameStructureFilesName.clear();
		IImageKeys.gameStructureFilesName.add(gameStructureFileName);
		PrintWriter out = null;
		Text text = null;
		int stateIndex = 0;
		List<String> gameStructureList = new ArrayList<String>();
		try {
			view = window.getActivePage().showView(GameStructureView.ID);
			text = ((GameStructureView) view).getTextArray().get(0);
			text.setText("");
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out = new PrintWriter(new FileWriter(gameStructureFileName));

			TreeSet<LineConnection> connections = new TreeSet<LineConnection>(
					new LineConnectionComparator<LineConnection>());
			Diagram diagram = ((PSCMonitorDiagramEditor) window.getActivePage()
					.getActiveEditor()).getDiagram();											//get the diagram
			Lifeline lifeline1 = null;
			Operator operator = null;
			for (int i = 0; i < diagram.getChildren().size(); i++) {
				Object temp = diagram.getChildren().get(i);
				if(temp instanceof Lifeline){
					lifeline1 = (Lifeline) diagram.getChildren().get(i);
				}else if(temp instanceof Operator){
					operator =(Operator) diagram.getChildren().get(i);
				}
				
				
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
			
			//鏈塷perator鐨勬儏鍐�
			if(operator != null){
				int operatorStart =  operator.getLocation().y;
				int operatorMiddleline = operator.getLocation().y + (operator.getSize().height / 2);
				int operatorEnd = operator.getLocation().y + operator.getSize().height;
				
				//灏嗘墍鏈夌殑lineconnection杩涜閬嶅巻骞跺垎鎴愪笁缁勶紝鍦╫perator锛堝鏋滄湁鐨勮瘽锛変箣鍓嶇殑锛屼箣涓殑锛屼箣鍚庣殑
				TreeSet<LineConnection> connectionBefore = new TreeSet<LineConnection>(
						new LineConnectionComparator<LineConnection>());
				TreeSet<LineConnection> connectionIn = new TreeSet<LineConnection>(
						new LineConnectionComparator<LineConnection>());
				TreeSet<LineConnection> connectionAfter = new TreeSet<LineConnection>(
						new LineConnectionComparator<LineConnection>());
				
				while(lineIterator.hasNext()){
					LineConnection line = lineIterator.next();
					
					int connectionPosition = line.getLocation().y - 85;//鍑忓幓85鏄洜涓簂ineconnection鍜宱perator鐨刲ocation鐨勫彇鍊艰儗鏅笉涓�牱锛屼竴涓槸鍦ㄧ敾甯冧笂鐨刲ocation鑰屽彟涓�釜鍒欐槸鍦ㄥ睆骞曚笂鐨刲ocation锛�5鏄敾甯冧妇渚嬪睆骞曢《绔殑璺濈
					if(connectionPosition < operatorStart){
						connectionBefore.add(line);
//						System.out.println(line.getMessageValue());
					}
					if(connectionPosition > operatorStart && connectionPosition < operatorEnd){
						connectionIn.add(line);
					}
					if(connectionPosition > operatorEnd){
						connectionAfter.add(line);
					}
				}
				//澶勭悊operator涔嬪墠鐨勭殑messages
				/*
				System.out.print(connectionBefore.size());
				System.out.print(connectionIn.size());
				System.out.print(connectionAfter.size());
				*/
				stateIndex = psc2gs(connectionBefore.iterator(), stateIndex, gameStructureList);  
				
				//澶勭悊operator涓殑messages
				if(operator.getType().equals("Alt")){	//operator涓篴lt								 
					//灏哻onnectionIn涓殑connection鍦ㄥ垎涓烘槸鍦╫peratormiddle涔嬩笂鍜屼箣涓嬬殑涓ゅ潡
					TreeSet<LineConnection> connectionInAbove = new TreeSet<LineConnection>(
							new LineConnectionComparator<LineConnection>());
					TreeSet<LineConnection> connectionInBelow = new TreeSet<LineConnection>(
							new LineConnectionComparator<LineConnection>());
					
					Iterator<LineConnection> iterator = connectionIn.iterator();
					while(iterator.hasNext()){
						LineConnection line = iterator.next();
						if((line.getLocation().y - 85) < operatorStart + operator.getSize().height / 2){
							connectionInAbove.add(line);
						}else{
							connectionInBelow.add(line);
						}
					}
//					System.out.print(connectionInAbove.size());
//					System.out.print(connectionInBelow.size());
					int stateTemp = state.size();
//					System.out.print(stateTemp);
					
					stateIndex = psc2gs(connectionInAbove.iterator(), stateIndex, gameStructureList);
					stateIndex = psc2gs(connectionInBelow.iterator(), stateIndex, gameStructureList);
					String temp = state.get((0==stateTemp) ? stateTemp : stateTemp - 1);
					String stringTemp = gameStructureList.get(gameStructureList.size() - connectionInBelow.size());
					stringTemp = stringTemp.replace(stringTemp.substring(stringTemp.indexOf('s') + 1,stringTemp.indexOf(')') + 1), 
							((0==stateTemp) ? stateTemp : stateTemp - 1) + temp);//consider the situation of the state size is 0
//					System.out.println(((0==stateTemp) ? stateTemp : stateTemp - 1) + temp);
					//int绫诲瀷鍙互鐩存帴杈撳嚭锛屼絾鏄嵈涓嶈兘鐩存帴瑙ｆ瀽涓篊HAR绫诲瀷 
					gameStructureList.set(gameStructureList.size() - connectionInBelow.size(), stringTemp);
				}
				
				if(operator.getType().equals("Loop")){		//operator涓簂oop							 
					//灏哻onnectionIn涓殑connection鍦ㄥ垎涓烘槸鍦╫peratormiddle涔嬩笂鍜屼箣涓嬬殑涓ゅ潡
					Iterator<LineConnection> iterator = connectionIn.iterator();
					for(int i = 0; i < operator.getNum(); i++){
						stateIndex = psc2gs(iterator, stateIndex, gameStructureList);
						iterator = connectionIn.iterator();
					}
				}
				
				if(operator.getType().equals("Par")){	//operator涓簆ar									 
					//灏哻onnectionIn涓殑connection鍦ㄥ垎涓烘槸鍦╫peratormiddle涔嬩笂鍜屼箣涓嬬殑涓ゅ潡
					TreeSet<LineConnection> connectionInAbove = new TreeSet<LineConnection>(
							new LineConnectionComparator<LineConnection>());
					TreeSet<LineConnection> connectionInBelow = new TreeSet<LineConnection>(
							new LineConnectionComparator<LineConnection>());
					
					Iterator<LineConnection> iterator = connectionIn.iterator();
					while(iterator.hasNext()){
						LineConnection line = iterator.next();
						if((line.getLocation().y - 90) < operatorStart + operator.getSize().height / 2){
							connectionInAbove.add(line);
						}else{
							connectionInBelow.add(line);
						}
					}
					int stateTemp = stateIndex;
					stateIndex = psc2gs(connectionInAbove.iterator(), stateIndex, gameStructureList);
					stateIndex = psc2gs(connectionInBelow.iterator(), stateIndex, gameStructureList);
					String stringTemp = gameStructureList.get(gameStructureList.size() - connectionInBelow.size());
					stringTemp = stringTemp.replace(stringTemp.charAt(stringTemp.indexOf('s') + 1), 
							String.valueOf(stateTemp).charAt(0));//int绫诲瀷鍙互鐩存帴杈撳嚭锛屼絾鏄嵈涓嶈兘鐩存帴瑙ｆ瀽涓篊HAR绫诲瀷
					gameStructureList.set(gameStructureList.size() - connectionInBelow.size(), stringTemp);
				}
				 //澶勭悊operator涔嬪悗鐨刴essages
				stateIndex = psc2gs(connectionAfter.iterator(), stateIndex, gameStructureList); 
				state.clear();
				//鑰冭檻鍒版湁澶氬壇psc瑕佽浆鍖栫殑鎯呭喌锛岄渶瑕佹瘡娆℃妧鏈箣鍚庢竻绌簊tate鐨刪ashmap
				
			}else{//娌℃湁operator鐨勬儏鍐�
				psc2gs(lineIterator, stateIndex, gameStructureList);
				state.clear();
			}
			
			for (int i = 0; i < gameStructureList.size(); i++) {
				out.println(gameStructureList.get(i));
			}
			out.close();
			
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStream in;
		try {
			in = new FileInputStream(gameStructureFileName);
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
		
		long endTime=System.currentTimeMillis(); 
		System.out.println("Time for transfering PSC to GameStructure is "+( endTime - startTime )+"ms"); 
	}
	
	public void addState2HashMap(HashMap <Integer, String> state, int stateIndex, String stateOfState){
//		add the state and stateOfState to the hashmap;
		if(state.get(stateIndex) == null)
			state.put(stateIndex, stateOfState);
		else if(state.get(stateIndex + 1) == null)
			state.put(stateIndex + 1, stateOfState);
		else if(state.get(stateIndex + 2) == null)
			state.put(stateIndex + 2, stateOfState);
		else if(state.get(stateIndex + 3) == null)
			state.put(stateIndex + 3, stateOfState);
	}
	
	public int psc2gs(Iterator<LineConnection> lineIterator, int stateIndex, List<String> gameStructureList){
		
		while (lineIterator.hasNext()){
			//int startStateIndex = stateIndex;
			LineConnection line = lineIterator.next();		
			
			if(line.getPastUnwantedMessageConstraintValue().isEmpty()
					&& line.getFutureUnwantedMessageConstraintValue().isEmpty()
					&& line.getPastWantedChainConstraintValue().isEmpty()
					&& line.getFutureWantedChainConstraintValue().isEmpty()
					&& line.getPastUnwantedChainConstraintValue().isEmpty()
					&& line.getFutureUnwantedChainConstraintValue()
							.isEmpty()&&(!line.isStrict())){
				String value1 = line.getMessageValue();
				String para1 = line.getMessagePara();
				Integer type1 = line.getMessageType();
				String message = line.getSource().getName() + ":" + value1 + ":" + line.getTarget().getName() 
					+ (para1.equals("")?  "(;)" : "(" + para1 +")");
				if(type1.intValue()==0){
					if(stateIndex == 0){
						addState2HashMap(state, stateIndex, (line.getSource() instanceof EnvironmentLifeline ? EICS : ICS));
						addState2HashMap(state, stateIndex, (line.getTarget() instanceof EnvironmentLifeline ? EFCS : SFCS));
					}else{
						addState2HashMap(state, stateIndex, (line.getTarget() instanceof EnvironmentLifeline ? EFCS : SFCS));
					}
					gameStructureList.add("s" + stateIndex + state.get(stateIndex) + "[" + "\n" 
							+ "!" + message + TO + "s" + stateIndex + state.get(stateIndex) + "\n" 
							+ message + TO + "s" + (++stateIndex) + state.get(stateIndex) + "\n" + "]" + "\n");
				}else if(type1.intValue()==1){
					addState2HashMap(state, stateIndex, ERR);
					addState2HashMap(state, stateIndex, (line.getTarget() instanceof EnvironmentLifeline ? EFCS : SFCS));
					gameStructureList.add("s" + stateIndex + state.get(stateIndex) + "[" + "\n" 
							+ "!" + message + L + TO + "s" + stateIndex + state.get(stateIndex) + "\n"
							+ "!" + message + UL + TO + "s" + (++stateIndex) + state.get(stateIndex) + "\n"
							+ message + TO + "s" + (++stateIndex) + state.get(stateIndex) + "\n" + "]" + "\n");
				}else if(type1.intValue()==2){
					addState2HashMap(state, stateIndex, (line.getTarget() instanceof EnvironmentLifeline ? EFCS : SFCS));
					addState2HashMap(state, stateIndex, ERR);
					gameStructureList.add("s" + stateIndex + state.get(stateIndex) + "[" + "\n" 
							+ "!" + message + L + TO + "s" + stateIndex + state.get(stateIndex) + "\n"  
							+ "!" + message + UL + TO + "s" + (stateIndex + 2) + state.get(stateIndex) + "\n" 
							+ message + TO + "s" + (++stateIndex) + state.get(stateIndex) + "\n" + "]" + "\n");
				}
			}else if(!(line.getPastUnwantedMessageConstraintValue().isEmpty())//锟斤拷锟斤拷谋锟绞撅拷锟斤拷曰锟斤拷写锟斤拷锟斤拷锟绞︼拷锟饺�
					&& line.getFutureUnwantedMessageConstraintValue().isEmpty()
					&& line.getPastWantedChainConstraintValue().isEmpty()
					&& line.getFutureWantedChainConstraintValue().isEmpty()
					&& line.getPastUnwantedChainConstraintValue().isEmpty()
					&& line.getFutureUnwantedChainConstraintValue()
							.isEmpty()&&(!line.isStrict())){
				String value1 = line.getMessageValue();
				String para1 = line.getMessagePara();
				Integer type1 = line.getMessageType();
				String pastUnwantedMessageConstraintValue = line.getPastUnwantedMessageConstraintValue();
				String message = line.getSource().getName() + ":" + value1 + ":" + line.getTarget().getName() 
					+ (para1.equals("")?  "(;)" : ("(" + para1 +")"));
			
				if(type1.intValue()==0){
					if(stateIndex == 0){
						addState2HashMap(state, stateIndex, (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS));
						addState2HashMap(state, stateIndex, (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS));
					}else{
						addState2HashMap(state, stateIndex, (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS));
					}
					gameStructureList.add("s" + stateIndex + state.get(stateIndex) + "[" + "\n" 
							+ "!" + message + "&" + pastUnwantedMessageConstraintValue + TO + "s" + stateIndex + state.get(stateIndex) + "\n" 
							+ message  + TO + "s" + (++stateIndex) + state.get(stateIndex) + "\n" + "]" + "\n");
				}else if(type1.intValue()==1){
					addState2HashMap(state, stateIndex, ERR);
					addState2HashMap(state, stateIndex, (line.getTarget() instanceof EnvironmentLifeline ? EFCS : SFCS));
					gameStructureList.add("s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EFCS : SFCS) + "or" + EFCS + "[" + "\n"
							 + "!" + message + "&" + pastUnwantedMessageConstraintValue + TO + "s" + stateIndex + state.get(stateIndex) + "\n"
							 + "!" + pastUnwantedMessageConstraintValue + "||!" + message + TO + "s" + (++stateIndex) + state.get(stateIndex) + "\n"
							 + message + TO + "s" + (++stateIndex) + state.get(stateIndex) + "\n" + "]" + "\n");
				}else if(type1.intValue()==2){
					addState2HashMap(state, stateIndex, (line.getTarget() instanceof EnvironmentLifeline ? EFCS : SFCS));
					addState2HashMap(state, stateIndex, ERR);
					gameStructureList.add("s" + stateIndex + SFCS + "or" + EFCS + "[" + "\n" 
							 + "!" + message + "&" + pastUnwantedMessageConstraintValue + TO + "s" + stateIndex + state.get(stateIndex) + "or" + EFCS + "\n" 
							 + "!" + pastUnwantedMessageConstraintValue + TO + "s" + (++stateIndex) + state.get(stateIndex) + "\n" 
							 + message + TO + "s" + (++stateIndex) + state.get(stateIndex) + "\n" + "]" + "\n");
				}
			}else if(line.getPastUnwantedMessageConstraintValue().isEmpty()//锟斤拷锟斤拷谋锟绞撅拷锟斤拷曰锟斤拷写锟斤拷锟斤拷锟绞︼拷锟饺�
					&& !line.getFutureUnwantedMessageConstraintValue().isEmpty()
					&& line.getPastWantedChainConstraintValue().isEmpty()
					&& line.getFutureWantedChainConstraintValue().isEmpty()
					&& line.getPastUnwantedChainConstraintValue().isEmpty()
					&& line.getFutureUnwantedChainConstraintValue()
							.isEmpty()&&(!line.isStrict())){
				String value1 = line.getMessageValue();
				String para1 = line.getMessagePara();
				Integer type1 = line.getMessageType();
				String futureUnwantedMessageConstraintValue = line.getFutureUnwantedMessageConstraintValue();
				String message = line.getSource().getName() + ":" + value1 + ":" + line.getTarget().getName() 
						+ (para1.equals("")?  "(;)" : ("(" + para1 +")"));
				
				if(type1.intValue()==0){
					gameStructureList.add("s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "[" + "\n" 
							+ "!" + message + TO + "s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "\n" 
							+ message  + TO + "s" + (++stateIndex) + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "\n" + "]" + "\n" + "\n"
							+ "s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "[" + "\n"
							+ "!" + futureUnwantedMessageConstraintValue + TO + "s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "\n"
							+ futureUnwantedMessageConstraintValue + TO + "s" + (++stateIndex) + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "\n" + "]" + "\n"
							);
					//stateIndex += 2;
				}else if(type1.intValue()==1){
					gameStructureList.add("s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "[" + "\n" 
							+ "!" + message + UL + TO + "s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "\n" 
							+ "!" + message + L + TO + "s" + (++stateIndex) + ERR +"\n"//L 锟斤拷锟斤拷锟斤拷锟阶刺�
							+ message + TO + "s" + (++stateIndex) + (line.getTarget() instanceof EnvironmentLifeline ? EFCS : SFCS) + "or" + EFCS + "\n" + "]" + "\n" + "\n"
							+ "s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EFCS : SFCS) + "or" + EFCS + "[" + "\n" 
							
							+ "!" + futureUnwantedMessageConstraintValue + TO + "s" + (++stateIndex) + ERR + "\n"
							+ futureUnwantedMessageConstraintValue + TO + "s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EFCS : SFCS) + "or" + EFCS + "\n" + "]" + "\n");
				}else if(type1.intValue()==2){
					gameStructureList.add("ERROR : failmessage never has a futureMessageConstraint!");
				}	
			}else if(line.getPastUnwantedMessageConstraintValue().isEmpty()//锟斤拷锟斤拷谋锟绞撅拷锟斤拷曰锟斤拷写锟斤拷锟斤拷锟绞︼拷锟饺�
					&& line.getFutureUnwantedMessageConstraintValue().isEmpty()
					&& !line.getPastWantedChainConstraintValue().isEmpty()
					&& line.getFutureWantedChainConstraintValue().isEmpty()
					&& line.getPastUnwantedChainConstraintValue().isEmpty()
					&& line.getFutureUnwantedChainConstraintValue()
							.isEmpty()&&(!line.isStrict())){
				String value1 = line.getMessageValue();
				String para1 = line.getMessagePara();
				Integer type1 = line.getMessageType();
				String pastWantedChainConstraintValue = line.getPastWantedChainConstraintValue();
				pastWantedChainConstraintValue = pastWantedChainConstraintValue.substring
											(pastWantedChainConstraintValue.indexOf("("), pastWantedChainConstraintValue.indexOf(")")) + ",";
				String message = line.getSource().getName() + ":" + value1 + ":" + line.getTarget().getName() 
						+ (para1.equals("")?  "(;)" : ("(" + para1 +")"));
				
				ArrayList <String> messageList = new ArrayList <String>();
				while(pastWantedChainConstraintValue.indexOf(",")>0){
					messageList.add(pastWantedChainConstraintValue.substring(0, pastWantedChainConstraintValue.indexOf(",")));
					pastWantedChainConstraintValue = pastWantedChainConstraintValue.substring(pastWantedChainConstraintValue.indexOf(",")+1);
				}
				//m1,m2,m3为锟斤拷
				int messageListSize = messageList.size();
				if(type1.intValue()==0){
					for(int i=0; i<messageListSize; i++){
						gameStructureList.add("s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "[" + "\n" 
								+ "!" + messageList.get(i) + TO + "s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "\n"
								+ messageList.get(i) + TO + "s" + (++stateIndex) + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "\n" + "]" + "\n"
						);
						
					}gameStructureList.add("s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "[" + "\n"
							+ "!" + message + TO + "s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "\n"
							+ message + TO + "s" + (++stateIndex) + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "\n" + "]" + "\n"
							);
					
				}else if(type1.intValue()==1){
					for(int i=0; i<messageListSize; i++){
						gameStructureList.add("s" + stateIndex + UNKNOWN + "[" + "\n" 
								+ "!" + messageList.get(i) + UL + TO + "s" + stateIndex + UNKNOWN + "\n"
								+ "!" + messageList.get(i) + L + TO + "s" + (++stateIndex) + ERR + "\n"
								+ messageList.get(i) + TO + "s" + (++stateIndex) + UNKNOWN + "\n" + "]" + "\n"//UNKNOWN 锟斤拷?
						);
						
					}gameStructureList.add("s" + stateIndex + UNKNOWN + "[" + "\n"
							+ "!" + message + TO + "s" + stateIndex + UNKNOWN + "\n"
							+ message + TO + "s" + (++stateIndex) + (line.getTarget() instanceof EnvironmentLifeline ? EFCS : SFCS) + "\n" + "]" + "\n"
							);
					
				}else if(type1.intValue()==2){
					for(int i=0; i<messageListSize; i++){
						gameStructureList.add("s" + stateIndex + UNKNOWN + "[" + "\n" 
								+ "!" + messageList.get(i) + UL + TO + "s" + stateIndex + UNKNOWN + "\n"
								+ "!" + messageList.get(i) + L + TO + "s" + (messageListSize + 1) + "\n"
								+ messageList.get(i) + TO + "s" + (++stateIndex) + UNKNOWN + "\n" + "]" + "\n"//UNKNOWN 锟斤拷?
						);
						
					}gameStructureList.add("s" + stateIndex + UNKNOWN + "[" + "\n"
							+ "!" + message + UL + TO + "s" + stateIndex + UNKNOWN + "\n"
							+ "!" + message + L + TO + "s" + stateIndex + "s" + (++stateIndex) + "\n"
							+ message + TO + "s" + (++stateIndex) + ERR + "\n" + "]" + "\n"
							);
				}	
				
				
				
				
				
				
			}else if(line.getPastUnwantedMessageConstraintValue().isEmpty()
					&& line.getFutureUnwantedMessageConstraintValue().isEmpty()
					&& line.getPastWantedChainConstraintValue().isEmpty()
					&&!line.getFutureWantedChainConstraintValue().isEmpty()
					&& line.getPastUnwantedChainConstraintValue().isEmpty()
					&& line.getFutureUnwantedChainConstraintValue()
							.isEmpty()&&(!line.isStrict())){
				String value1 = line.getMessageValue();
				String para1 = line.getMessagePara();
				Integer type1 = line.getMessageType();
				String futureWantedChainConstraintValue = line.getFutureWantedChainConstraintValue();
				futureWantedChainConstraintValue = futureWantedChainConstraintValue.substring
								(futureWantedChainConstraintValue.indexOf("("), futureWantedChainConstraintValue.indexOf(")")) + ",";//锟斤拷锟斤拷一锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟絯hile循锟斤拷锟叫关ｏ拷为锟剿凤拷止锟劫硷拷一锟斤拷m3
				String message = line.getSource().getName() + ":" + value1 + ":" + line.getTarget().getName() 
						+ (para1.equals("")?  "(;)" : ("(" + para1 +")"));
				
				ArrayList <String> messageList = new ArrayList <String>();
				while(futureWantedChainConstraintValue.indexOf(",")>0){
					messageList.add(futureWantedChainConstraintValue.substring(0, futureWantedChainConstraintValue.indexOf(",")));
					futureWantedChainConstraintValue = futureWantedChainConstraintValue.substring(futureWantedChainConstraintValue.indexOf(",")+1);
				}
				//m1,m2,m3为锟斤拷
				int messageListSize = messageList.size();
				if(type1.intValue()==0){
					gameStructureList.add("s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "[" + "\n"
							+ "!" + message + TO + "s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "\n"
							+ message + TO + "s" + (++stateIndex) + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "\n" + "]" + "\n"
							);
					for(int i=0; i<messageListSize; i++){
						gameStructureList.add("s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "[" + "\n" 
								+ "!" + messageList.get(i) + TO + "s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "\n"
								+ messageList.get(i) + TO + "s" + (++stateIndex) + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "\n" + "]" + "\n"
						);
						
					}
					
				}else if(type1.intValue()==1){
					gameStructureList.add("s" + stateIndex + UNKNOWN + "[" + "\n"
							+ "!" + message + UL + TO + "s" + stateIndex + UNKNOWN + "\n"
							+ "!" + message + L + TO + "s" + (++stateIndex) + ERR + "\n"
							+ message + TO + "s" + (++stateIndex) + UNKNOWN + "\n" + "]" + "\n"
							);
					for(int i=0; i<messageListSize-1; i++){
						gameStructureList.add("s" + stateIndex + UNKNOWN + "[" + "\n" 
								+ "!" + messageList.get(i) + UL + TO + "s" + stateIndex + UNKNOWN + "\n"
								+ "!" + messageList.get(i) + L + TO + "s" + (++stateIndex) + ERR + "\n"
								+ messageList.get(i) + TO + "s" + (++stateIndex) + UNKNOWN + "\n" + "]" + "\n"//UNKNOWN 锟斤拷?
						);	
					}
					gameStructureList.add("s" + stateIndex + UNKNOWN + "[" + "\n" 
							+ "!" + messageList.get(messageListSize-1) + UL + TO + "s" + stateIndex + UNKNOWN + "\n"
							+ "!" + messageList.get(messageListSize-1) + L + TO + "s" + (++stateIndex) + ERR + "\n"
							+ messageList.get(messageListSize-1) + TO + "s" + (++stateIndex) + (line.getTarget() instanceof EnvironmentLifeline ? EFCS : SFCS) + "\n" + "]" + "\n"//UNKNOWN 锟斤拷?
							);
					
				}else if(type1.intValue()==2){
					gameStructureList.add("Failmessage never has a future constraint!"
							);
				
				}
				
				
				
				
				
			}else if(line.getPastUnwantedMessageConstraintValue().isEmpty()//锟斤拷锟斤拷谋锟绞撅拷锟斤拷曰锟斤拷写锟斤拷锟斤拷锟绞︼拷锟饺�
					&& line.getFutureUnwantedMessageConstraintValue().isEmpty()
					&& line.getPastWantedChainConstraintValue().isEmpty()
					&& line.getFutureWantedChainConstraintValue().isEmpty()
					&& !line.getPastUnwantedChainConstraintValue().isEmpty()
					&& line.getFutureUnwantedChainConstraintValue()
							.isEmpty()&&(!line.isStrict())){
				String value1 = line.getMessageValue();
				String para1 = line.getMessagePara();
				Integer type1 = line.getMessageType();
				String pastUnwantedChainConstraintValue = line.getPastUnwantedChainConstraintValue();
				pastUnwantedChainConstraintValue = pastUnwantedChainConstraintValue.substring
								(pastUnwantedChainConstraintValue.indexOf("("), pastUnwantedChainConstraintValue.indexOf(")")) + ",";//锟斤拷锟斤拷一锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟絯hile循锟斤拷锟叫关ｏ拷为锟剿凤拷止锟劫硷拷一锟斤拷m3
				String message = line.getSource().getName() + ":" + value1 + ":" + line.getTarget().getName() 
						+ (para1.equals("")?  "(;)" : ("(" + para1 +")"));
				
				ArrayList <String> messageList = new ArrayList <String>();
				while(pastUnwantedChainConstraintValue.indexOf(",")>0){
					messageList.add(pastUnwantedChainConstraintValue.substring(0, pastUnwantedChainConstraintValue.indexOf(",")));
					pastUnwantedChainConstraintValue = pastUnwantedChainConstraintValue.substring(pastUnwantedChainConstraintValue.indexOf(",")+1);
				}
				//m1,m2,m3为锟斤拷
				int messageListSize = messageList.size();
				if(type1.intValue()==0){
					for(int i=0; i<messageListSize; i++){
						gameStructureList.add("s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "[" + "\n" 
								+ "!" + messageList.get(i) + TO + "s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "\n"
								+ message + TO + "s" + (messageListSize + 1) + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "\n"
								+ messageList.get(i) + TO + "s" + (++stateIndex) + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "\n" + "]" + "\n"
						);
						
					}gameStructureList.add("s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "[" + "\n"
							+ "!" + message + TO + "s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "\n" + "]" + "\n"
							);
					
				}else if(type1.intValue()==1){
					for(int i=0; i<messageListSize-1; i++){
						gameStructureList.add("s" + stateIndex + UNKNOWN + "[" + "\n" 
								+ "!" + messageList.get(i) + UL + TO + "s" + stateIndex + UNKNOWN + "\n"
								+ "!" + messageList.get(i) + L + TO + "s" + messageListSize + UNKNOWN + "\n"
								+ messageList.get(i) + TO + "s" + (++stateIndex) + UNKNOWN + "\n" + "]" + "\n"//UNKNOWN 锟斤拷?
						);
						
					}
					gameStructureList.add("s" + stateIndex + UNKNOWN + "[" + "\n"
							+ "!" + messageList.get(messageListSize-1) + UL + TO + "s" + stateIndex + UNKNOWN + "\n"
							+ "!" + messageList.get(messageListSize-1) + L + TO + "s" + (++stateIndex) + UNKNOWN + "\n"
							+ messageList.get(messageListSize-1) + TO + "s" + (++stateIndex) + ERR + "\n" + "]" + "\n"
							);
					gameStructureList.add("s" + stateIndex + UNKNOWN + "[" + "\n"
							+ "!" + message + UL + TO + "s" + stateIndex + UNKNOWN + "\n"
							+ "!" + message + L + TO + "s" + (++stateIndex) + ERR + "\n"
							+ message + TO + "s" + (++stateIndex) + (line.getTarget() instanceof EnvironmentLifeline ? EFCS : SFCS) + "\n" + "]" + "\n"
							);
					
				}else if(type1.intValue()==2){
					for(int i=0; i<messageListSize-1; i++){
						gameStructureList.add("s" + stateIndex + UNKNOWN + "[" + "\n" 
								+ "!" + messageList.get(i) + UL + TO + "s" + stateIndex + UNKNOWN + "\n"
								+ "!" + messageList.get(i) + L + TO + "s" + messageListSize + UNKNOWN + "\n"
								+ messageList.get(i) + TO + "s" + (++stateIndex) + UNKNOWN + "\n" + "]" + "\n"//UNKNOWN 锟斤拷?
						);
						
					}
					gameStructureList.add("s" + stateIndex + UNKNOWN + "[" + "\n"
							+ "!" + messageList.get(messageListSize-1) + UL + TO + "s" + stateIndex + UNKNOWN + "\n"
							+ "!" + messageList.get(messageListSize-1) + L + TO + "s" + (++stateIndex) + UNKNOWN + "\n"
							+ messageList.get(messageListSize-1) + TO + "s" + (messageListSize + 1) + (line.getTarget() instanceof EnvironmentLifeline ? EFCS : SFCS) + "\n" + "]" + "\n"
							);
					gameStructureList.add("s" + stateIndex + UNKNOWN + "[" + "\n"
							+ "!" + message + TO + "s" + stateIndex + UNKNOWN + "\n"
							+ message + TO + "s" + (++stateIndex) + ERR + "\n" + "]" + "\n"
							);
				
				}
				
				
			}else if(line.getPastUnwantedMessageConstraintValue().isEmpty()//锟斤拷锟斤拷谋锟绞撅拷锟斤拷曰锟斤拷写锟斤拷锟斤拷锟绞︼拷锟饺�
					&& line.getFutureUnwantedMessageConstraintValue().isEmpty()
					&& line.getPastWantedChainConstraintValue().isEmpty()
					&& line.getFutureWantedChainConstraintValue().isEmpty()
					&& line.getPastUnwantedChainConstraintValue().isEmpty()
					&& !line.getFutureUnwantedChainConstraintValue()
							.isEmpty()&&(!line.isStrict())){
				String value1 = line.getMessageValue();
				String para1 = line.getMessagePara();
				Integer type1 = line.getMessageType();
				String futureUnwantedChainConstraintValue = line.getFutureUnwantedChainConstraintValue() ;
				futureUnwantedChainConstraintValue = futureUnwantedChainConstraintValue.substring
											(futureUnwantedChainConstraintValue.indexOf("("), futureUnwantedChainConstraintValue.indexOf(")"))+ ",";//锟斤拷锟斤拷一锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟絯hile循锟斤拷锟叫关ｏ拷为锟剿凤拷止锟劫硷拷一锟斤拷m3
				String message = line.getSource().getName() + ":" + value1 + ":" + line.getTarget().getName() 
						+ (para1.equals("")?  "(;)" : ("(" + para1 +")"));
				
				ArrayList <String> messageList = new ArrayList <String>();
				while(futureUnwantedChainConstraintValue.indexOf(",")>0){
					messageList.add(futureUnwantedChainConstraintValue.substring(0, futureUnwantedChainConstraintValue.indexOf(",")));
					futureUnwantedChainConstraintValue = futureUnwantedChainConstraintValue.substring(futureUnwantedChainConstraintValue.indexOf(",")+1);
				}
				//m1,m2,m3为锟斤拷
				int messageListSize = messageList.size();
				if(type1.intValue()==0){
					gameStructureList.add("s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "[" + "\n"
							+ "!" + message + TO + "s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "\n" 
							+ message + TO + "s" + (++stateIndex) + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "\n" + "]" + "\n"
							);
					for(int i=0; i<messageListSize; i++){
						gameStructureList.add("s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "[" + "\n" 
								+ "!" + messageList.get(i) + TO + "s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "\n"
								+ message + TO + "s" + (messageListSize + 1) + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "\n"
								+ messageList.get(i) + TO + "s" + (++stateIndex) + (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS) + "\n" + "]" + "\n"
						);
						
					}
					
				}else if(type1.intValue()==1){
					gameStructureList.add("s" + stateIndex + UNKNOWN + "[" + "\n"
							+ "!" + message + UL + TO + "s" + stateIndex + UNKNOWN + "\n"
							+ "!" + message + L + TO + "s" + (++stateIndex) + ERR + "\n"
							+ message + TO + "s" + (++stateIndex) + "\n" + "]" + "\n"
							);
					for(int i=0; i<messageListSize-1; i++){
						gameStructureList.add("s" + stateIndex + UNKNOWN + "[" + "\n" 
								+ "!" + messageList.get(i) + UL + TO + "s" + stateIndex + UNKNOWN + "\n"
								+ "!" + messageList.get(i) + L + TO + "s" + (messageListSize + 1) + (line.getTarget() instanceof EnvironmentLifeline ? EFCS : SFCS) + "\n"
								+ messageList.get(i) + TO + "s" + (++stateIndex) + UNKNOWN + "\n" + "]" + "\n"//UNKNOWN 锟斤拷?
						);
						
					}
					
					gameStructureList.add("s" + stateIndex + UNKNOWN + "[" + "\n"
							+ "!" + messageList.get(messageListSize-1) + UL + TO + "s" + stateIndex + UNKNOWN + "\n"
							+ "!" + messageList.get(messageListSize-1) + L + TO + "s" + (++stateIndex) + (line.getTarget() instanceof EnvironmentLifeline ? EFCS : SFCS)  + "\n"
							+ messageList.get(messageListSize-1) + TO + "s" + (++stateIndex) + ERR + "\n" + "]" + "\n"
							);
					
				}else if(type1.intValue()==2){
					
					gameStructureList.add("Failmessage never has a future constraint!"
							);
				
				}				
			}else if(line.getPastUnwantedMessageConstraintValue().isEmpty()//锟斤拷锟斤拷谋锟绞撅拷锟斤拷曰锟斤拷写锟斤拷锟斤拷锟绞︼拷锟饺�
					&& line.getFutureUnwantedMessageConstraintValue().isEmpty()
					&& line.getPastWantedChainConstraintValue().isEmpty()
					&& line.getFutureWantedChainConstraintValue().isEmpty()
					&& line.getPastUnwantedChainConstraintValue().isEmpty()
					&& line.getFutureUnwantedChainConstraintValue()
							.isEmpty()&&line.isStrict()){
				String value1 = line.getMessageValue();
				String para1 = line.getMessagePara();
				Integer type1 = line.getMessageType();
				String message = line.getSource().getName() + ":" + value1 + ":" + line.getTarget().getName() 
						+ (para1.equals("")?  "(;)" : ("(" + para1 +")"));
				L = L.replace("Max", "Min");
				UL = UL.replace("Max", "Min");
				if(type1.intValue()==0){
					if(stateIndex == 0){
						addState2HashMap(state, stateIndex, (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS));
						addState2HashMap(state, stateIndex, (line.getTarget() instanceof EnvironmentLifeline ? EFCS : SFCS));
						addState2HashMap(state, stateIndex, (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS));
					}else{
						addState2HashMap(state, stateIndex, (line.getTarget() instanceof EnvironmentLifeline ? EFCS : SFCS));
						addState2HashMap(state, stateIndex, (line.getTarget() instanceof EnvironmentLifeline ? EICS : ICS));
					}
					gameStructureList.add("s" + stateIndex + state.get(stateIndex) + "[" + "\n" 
							+ "!" + message + L + TO + "s" + stateIndex + state.get(stateIndex) + "\n" 
							+ message + TO + "s" + (++stateIndex) + state.get(stateIndex) + "\n" 
							+ "!" + message + UL + TO + "s" + (++stateIndex) + state.get(stateIndex) + "\n"
							+ "]" + "\n");
				}else if(type1.intValue()==1){
					addState2HashMap(state, stateIndex, ERR);
					addState2HashMap(state, stateIndex, (line.getTarget() instanceof EnvironmentLifeline ? EFCS : SFCS));
					gameStructureList.add("s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EFCS : SFCS) + "[" + "\n" 
							+ "!" + message + L + TO + "s" + stateIndex + (line.getTarget() instanceof EnvironmentLifeline ? EFCS : SFCS) + "\n"
							+ "!" + message + UL + TO + "s" + (++stateIndex) + ERR + "\n"
							+ message + TO + "s" + (++stateIndex) + (line.getTarget() instanceof EnvironmentLifeline ? EFCS : SFCS) + "\n" + "]" + "\n");
				}else if(type1.intValue()==2){
					gameStructureList.add("Failmessage never has a STRICT constraint!");
				}
				L = L.replace("Min", "Max");
				UL = UL.replace("Min", "Max");
			}
		}
		return stateIndex;
	}
}
