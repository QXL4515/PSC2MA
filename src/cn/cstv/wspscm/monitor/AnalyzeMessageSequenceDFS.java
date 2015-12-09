package cn.cstv.wspscm.monitor;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;

import cn.cstv.wspscm.IImageKeys;

public class AnalyzeMessageSequenceDFS {
	private static TimedAutomataSet timedAutomataSet = AnalyzeMessageSequence.timedAutomataSet;
	private static List<State> timedAutomataState = AnalyzeMessageSequence.timedAutomataState;
	private static List<TimedAutomataSet> timedAutomataSetList = AnalyzeMessageSequence.timedAutomataSetList;
	private static List<List<State>> timedAutomataStateList =AnalyzeMessageSequence.timedAutomataStateList;//billy：好东西 泛型.列表中的列表
	public static Integer kSteps = -1;
	private static ConcurrentLinkedQueue<Mission> queue = new ConcurrentLinkedQueue<Mission>();// 任务队列
	private static ConcurrentLinkedQueue<Mission> kqueue = new ConcurrentLinkedQueue<Mission>();// 任务队列
	private static Stack<Mission>Stack = new Stack<Mission>();
	private static Stack<Mission>reStack = new Stack<Mission>();
	private static HashMap<String,String> messageMap=AnalyzeMessageSequence.messageMap;
	private static ArrayList<MissionForRefactor> foundList = new ArrayList<MissionForRefactor>();// 存放了满足K步的所有可能组合的信息，为重构用
	private static ArrayList<MissionForRefactor> tmpFoundList = new ArrayList<MissionForRefactor>();
   
	public static final int INNER_MESSAGE=0,SEND_MESSAGE=1,RECEIVE_MESSAGE=2,NON_INNER_MESSAGE=10;//内部消息、发送消息、接收消息、
	private static final String PlaceArray = null;
	private static long currentTime1;
	private static List<Message> messageLog = new ArrayList<Message>();
	
	
	
	private static boolean isDebug=true,isForever=true;//isForever控制是否不停的找K步（true为不停）,isDebug控制是否硬编码消息序列供重构用
	private static LinkedList<Mission> errorSequenceList=new LinkedList<Mission>();//存放错误的序列的链表
	private static ArrayList<Long> timeList=new ArrayList<Long>();//存放每步的计算时间
    
	public static Integer getkSteps() {
		return kSteps;
	}
	public static void setkSteps(Integer kSteps) {
		
		if (kSteps > 0) {}
		else {System.out.println("Lookahead is closed.");}
		AnalyzeMessageSequenceDFS.kSteps = kSteps;	
		
		if(isForever){			// billy :不停的找K步
			new Thread(){   //为什么要重新开启一个线程？？
				ArrayList<String> messageSeq=new ArrayList<String>();				
				public void run() {
					AnalyzeMessageSequence.setMyMessageSeq(messageSeq);//设置messageSeq，如果是debug的时候，序列是硬编码的
					for(int j=0;j<1;j++){//循环100次    ??billy
						synchronizedFindDFS();//tjf 20100925 使点了lookahead之后就自动进行寻找，不再经由每隔10秒的messageview线程调用
			    	for(int i=0;i<messageSeq.size();i++){							
							try {
							Thread.currentThread().sleep(200);//等待2秒重新开始下一次循环
						} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}			
						   AyalyzingPreLookAheadForRefactorDFS(messageSeq.get(i));
						}     
			    	   
						System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
						System.out.println("Look ahead " + getkSteps() + " steps （billy DFS） " );
						System.out.println("The time list: ");
						System.out.println("the first: "+timeList.get(0));
						for(int i=0;i<messageSeq.size();i++){
							System.out.print(messageSeq.get(i)+": "+timeList.get(i+1)+"  ");
						}
						System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
					}
				}
			}.start();
			
		}else
			synchronizedFindDFS();
		
			}

	static void synchronizedFindDFS(){
		if (kSteps == -1) {
			return;
		}
		
		foundList.clear();//把用于重构的两个链表首先清空
		tmpFoundList.clear();
		errorSequenceList.clear();//把用于打印错误组合的链表清空
		timeList.clear();//清空时间链表
		
		if (timedAutomataStateList.size() == 0) {
			if (IImageKeys.automataFilesName.size() > 0) {
				long currentTime1 = System.nanoTime();

				AnalyzeMessageSequence.InitilizeAutomataStateWithMultiAutomata(IImageKeys.automataFilesName);

				System.out.println();


			}			
		}

		currentTime1 = System.nanoTime();

		if (kSteps > 0) {
			System.out.println();
			System.out.println("Look ahead " + kSteps + " steps （billy DFS） " );
			System.out.println();

		}

		
		AyalyzingPreLookAheadWithMultiAutomataDFS();		
		
	}
	private static void AyalyzingPreLookAheadWithMultiAutomataDFS() {
		if (queue.isEmpty()) {
		
			for (int i = 0; i < timedAutomataStateList.size(); i++) {// 从每个自动机的起始状态开始找（billy：这边的size 表示有几个自动机）
				ArrayList<String> placeArray = new ArrayList<String>(timedAutomataStateList.size());
				AnalyzeMessageSequence.initPlaceArray(placeArray);
				List<State> timedAutomataState = timedAutomataStateList.get(i);//billy：得到第i个自动机 里面的所有状态
				String stateName =placeArray.get(i); //billy：第i个自动机的 名字
				for (int j = 0; j < timedAutomataState.size(); j++) {// 根据起始状态的名字查找timedAutomataState （billy：指第i个自动机的每个状态）
					State currentState = timedAutomataState.get(j);
					if (currentState.getStateName().equals(stateName)) {// 找到了对应名字的state 
						List<String> innerList = currentState.getInnerMessageList();
						for (int k = 0; k < innerList.size(); k++) {// 内部消息都是可能的
							ArrayList<String> tmpPlaceArray=(ArrayList<String>)placeArray.clone();
							tmpPlaceArray.set(i, currentState.getEndStateName(innerList.get(k)));//billy：把内部消息对应的状态  staname放入到tmp
							queue.add(new Mission(innerList.get(k), (ArrayList<String>) tmpPlaceArray.clone(), 1));	
						}
						//System.out.println("queue whether empty");
						List<String> sendList = currentState.getSendMessageList();				
						for (int k = 0; k < sendList.size(); k++) {// 发送必须和接收配对，才是可能的
							ArrayList<String> tmpPlaceArray=(ArrayList<String>)placeArray.clone();
							int temp = AnalyzeMessageSequence.findInAllAuto(sendList.get(k),tmpPlaceArray);
							if (temp != -1) {
								tmpPlaceArray.set(i, currentState.getEndStateName(sendList.get(k)));
//								printPlaceArray(tmpPlaceArray);
								queue.add(new Mission(sendList
										.get(k),
										(ArrayList<String>) tmpPlaceArray.clone(),
										1));
								
							}
						}
				//		System.out.println("55 " + queue.size());
						break;//找到了第一个起始状态的描述，不再进行内循环
					}
				}
			}
		}
		System.out.println("\n Possible right message sequences:\n");
		while (!queue.isEmpty()) {
				Mission m = queue.poll();// 从队列取出，删
				
				
				if(m.getSteps()<kSteps){
					Stack.push(m);
		       
		        	 while(!Stack.isEmpty()){
		        		  Mission sm=Stack.pop();
		        		  String buffer = sm.getPreviousMessage();		  
		        		  ArrayList<String> placeArray = (ArrayList<String>) sm.getPlaceList().clone();
		        		  boolean isFind=false;
		        	 for (int i = 0; i < timedAutomataStateList.size(); i++) {
		    		List<State> timedAutomataState = timedAutomataStateList.get(i);
		    		
					for (int j = 0; j < timedAutomataState.size(); j++) {// 根据下一状态的名字查找timedAutomataState 
						State currentState = timedAutomataState.get(j);
					//	System.out.println("88 "+currentState.getStateName());
					//	AnalyzeMessageSequence.printPlaceArray(placeArray);	
						if (currentState.getStateName().equals(placeArray.get(i))) {// 在自动机i中找到了对应名字的state
							List<String> innerList = currentState.getInnerMessageList();
						//	System.out.println("99 " + innerList.size());
							for (int k = 0; k < innerList.size(); k++){// 内部消息都是可能的
								ArrayList<String> tmpPlaceArray=(ArrayList<String>)placeArray.clone();
								tmpPlaceArray.set(i, currentState.getEndStateName(innerList.get(k)));
//								printPlaceArray(tmpPlaceArray);
						       if(sm.getSteps()+1==kSteps)
						       {
						    	   kqueue.add(new Mission(buffer+" , "+innerList.get(k), (ArrayList<String>)tmpPlaceArray.clone(), sm.getSteps() + 1));
						    	  Mission km=kqueue.poll();
						    	  foundList.add(new MissionForRefactor(km.getPreviousMessage(),km.getPlaceList()));
						    	  System.out.println("---" + km.getPreviousMessage() + "---");
						    	  isFind=true;
						    	   
						       }else {
								//tmpPlaceArray.add("visited");
								Stack.push(new Mission(buffer+" , "+innerList.get(k), (ArrayList<String>)tmpPlaceArray.clone(), sm.getSteps() + 1));
								isFind=true;}
								}
							
							List<String> sendList = currentState.getSendMessageList();
//						
					for (int k = 0; k < sendList.size(); k++){// 发送必须和接收配对，才是可能的
								ArrayList<String> tmpPlaceArray=(ArrayList<String>)placeArray.clone();
								if (AnalyzeMessageSequence.findInAllAuto(sendList.get(k),tmpPlaceArray)!=-1){
									tmpPlaceArray.set(i, currentState.getEndStateName(sendList.get(k)));
							     if(sm.getSteps()+1==kSteps){
							    	 kqueue.add(new Mission(buffer+" , "+sendList.get(k), (ArrayList<String>)tmpPlaceArray.clone(), sm.getSteps() + 1));
							    	 Mission km =kqueue.poll();
							    	 foundList.add(new MissionForRefactor(km.getPreviousMessage(),km.getPlaceList()));
							    	 System.out.println("---" + km.getPreviousMessage() + "---");
							    	 isFind=true;
			
							     }else{Stack.push(new Mission(buffer+" , "+sendList.get(k), (ArrayList<String>)tmpPlaceArray.clone(), sm.getSteps() + 1));
							       	isFind=true;}
										}
								} 
								} //end current if	
						  
						}
		        	 }   //end out for //end inner for	
					 if(!isFind){  //
						boolean isMissionExist=false;
						for(Mission mission:errorSequenceList ){//判断一下errorSequenceList中原来是不是已经加了这个任务
							if(mission.getPreviousMessage().equals(sm.getPreviousMessage())) {
								isMissionExist=true;
								break;								
							}
						}
						if((!isMissionExist)
//								&&(!isMissionLeave)&&(!flag)
								) errorSequenceList.add(sm);//如果mission没有下文了，则存入错误组合链表，供最后集中打印
			//			for(int j=0;j<errorSequenceList.size();j++){
			//				Mission mission=errorSequenceList.get(j);
			//				System.out.println("errorsequecne"+mission.getPreviousMessage());}
				}
					
					   
						}//end empty
					
		        } //end if k
				else{ System.out.println("---" + m.getPreviousMessage() + "---（billy）");}
				}//end queue 
		
		for(MissionForRefactor mr:foundList){
			for(int i=0;i<errorSequenceList.size();i++){
				Mission mission=errorSequenceList.get(i);
				if(mr.getMessageSequence().contains(mission.getPreviousMessage())) 
					errorSequenceList.remove(mission); //首次出现的mission
			}
		}
	
		//打印所有错误的组合
		System.out.println("\n Possible error message sequences（billy）:\n");
		for(int i=0;i<errorSequenceList.size();i++){
			Mission m=errorSequenceList.get(i);
			System.out.print("Warning: "+m.getPreviousMessage()+"\t");
			ArrayList<String> list=m.getPlaceList();
			 
               System.out.print("( ");
			for(int k=0;k<list.size();k++){
				System.out.print(list.get(k)+"("+IImageKeys.automataFilesName.get(k).
						substring(IImageKeys.automataFilesName.get(k).indexOf('/')+1,IImageKeys.automataFilesName.get(k).lastIndexOf('t'))+") ");
			}
			System.out.println(")");
		}
		if (kSteps > 0) {
			// System.out.println("Look ahead " + kSteps + " steps " +
			// (withControllability!=0?"with":"without") +
			// " controllability by " +
			// (isDepthSearchFirst!=0?"breadth search first.":"depth search first."));
			long current2=System.nanoTime();
			timeList.add((current2 - currentTime1) / 1000);
			System.out
					.println("\nThe execution time of initial Lookahead is:  "
							+ (current2 - currentTime1) / 1000 + " us");
		} else {
			System.out
					.println("The execution time of analysis without Pre-Lookahead is: "
							+ (System.nanoTime() - currentTime1) / 1000 + " us");

		}
		System.out
				.println("--------------深度---------------多自动机主动监控-------------------------------------------");	
	
	}	//end ayalyzingpre dfs
	



      // 重构思想
public static void AyalyzingPreLookAheadForRefactorDFS(String messageName){
	if(messageName==null){
		System.out.println("No input message to drive the automachine!");
		return;	
	}
	
	LinkedList<Mission> errorSequenceList2=new LinkedList<Mission>();
	
	if (kSteps > 0) {
		System.out.println();
		System.out.println("Look ahead "
				+ kSteps
				+ " steps with message: "+messageMap.get(messageName)+"\nwhich is mapped into "+messageName+"billy refactor"
				);
		System.out.println();

	}
	
	currentTime1 = System.nanoTime();
	
	if(tmpFoundList.size()!=0){
		foundList=(ArrayList<MissionForRefactor>)tmpFoundList.clone();
		tmpFoundList.clear();
	}
	
	System.out.println("\n Possible right message sequences(billy refactor):\n");
	
	for (int i = 0; i < timedAutomataStateList.size(); i++) {// 从每个自动机的起始状态开始找
		ArrayList<String> placeArray ;		
		
		MissionForRefactor mfr;
		for(int index=0;index<foundList.size();index++)
		{
			mfr=foundList.get(index);				
			
			if(!mfr.getFirstMessageName().equals(messageName)) continue;//给定的消息在原来查找到的序列里不合适，结束本次循环
			
			placeArray=mfr.getPlaceArray();
			String tmpMessageSequence=mfr.getMessageSequenceWithoutFirstMessageName();
			boolean isFind=false;
		List<State> timedAutomataState = timedAutomataStateList.get(i);
//		String stateName = timedAutomataState.get(0).getStateName();
		String stateName =placeArray.get(i);
//		System.out.println("11 " + stateName);
		for (int j = 0; j < timedAutomataState.size(); j++) {// 根据起始状态的名字查找timedAutomataState
			State currentState = timedAutomataState.get(j);
//			System.out.println("22 ");
			if (currentState.getStateName().equals(stateName)) {// 找到了对应名字的state
				List<String> innerList = currentState.getInnerMessageList();
//				System.out.println("33 " + innerList.size());
				for (int k = 0; k < innerList.size(); k++) {// 内部消息都是可能的
					ArrayList<String> tmpPlaceArray=(ArrayList<String>)placeArray.clone();
					tmpPlaceArray.set(i, currentState.getEndStateName(innerList.get(k)));
//					StringBuffer buffer=new StringBuffer(innerList.get(k));
//					synchronizedMoveInFindingMessageSeq(buffer,1,innerList.get(k),INNER_MESSAGE,tmpPlaceArray,i);
//					printPlaceArray(tmpPlaceArray);							
//					queue.add(new Mission(innerList.get(k), (ArrayList<String>) tmpPlaceArray.clone(), 1));	
					if(tmpMessageSequence.equals(""))
						tmpFoundList.add(new MissionForRefactor(innerList.get(k),tmpPlaceArray));
					else
						tmpFoundList.add(new MissionForRefactor(tmpMessageSequence+" , "+innerList.get(k),tmpPlaceArray));
					System.out.println("*** "+tmpMessageSequence+" , "+innerList.get(k)+" ***");
					isFind=true;
				}
				
				List<String> sendList = currentState.getSendMessageList();
				// List<String>
				// receiveList=currentState.getReceiveMessageList();
//				System.out.println("44 " + sendList.size());
				for (int k = 0; k < sendList.size(); k++) {// 发送必须和接收配对，才是可能的
					ArrayList<String> tmpPlaceArray=(ArrayList<String>)placeArray.clone();
					int temp =AnalyzeMessageSequence.findInAllAuto(sendList.get(k),tmpPlaceArray);
					if (temp != -1) {
						tmpPlaceArray.set(i, currentState.getEndStateName(sendList.get(k)));
//						printPlaceArray(tmpPlaceArray);
//						queue.add(new Mission(sendList.get(k),
//								(ArrayList<String>) tmpPlaceArray.clone(),
//								1));
						if(tmpMessageSequence.equals(""))
							tmpFoundList.add(new MissionForRefactor(sendList.get(k),tmpPlaceArray));
						else
							tmpFoundList.add(new MissionForRefactor(tmpMessageSequence+" , "+sendList.get(k),tmpPlaceArray));
						System.out.println("*** "+tmpMessageSequence+" , "+sendList.get(k)+" ***");
						isFind=true;
					}
				}
//				System.out.println("55 " + queue.size());
				break;//找到了第一个起始状态的描述，不再进行内循环
			}
		}
		if(!isFind) {
				boolean isMissionExist=false;
				for(Mission mission:errorSequenceList2 ){//判断一下errorSequenceList中原来是不是已经加了这个任务
					if(mission.getPreviousMessage().equals(mfr.getMessageSequence())) {
						isMissionExist=true;
						break;								
					}
				}
				
				if(!isMissionExist)
					errorSequenceList2.add(new Mission(mfr.getMessageSequence(),mfr.getPlaceArray(),kSteps));//如果mission没有下文了，则存入错误组合链表，供最后集中打印			
			
		}
	}
	}
	
	//再次判断是否错误组合中有正确组合的部分，否则去掉
	for(MissionForRefactor mr:foundList){
		for(int i=0;i<errorSequenceList2.size();i++){
			Mission mission=errorSequenceList2.get(i);
			if(mr.getMessageSequence().contains(mission.getPreviousMessage())) 
				errorSequenceList2.remove(mission);
		}
	}
	
	System.out.println("\n Possible error message sequences:\n");
	for(int index=0;index<errorSequenceList2.size();index++){
		MissionForRefactor mfr=errorSequenceList2.get(index).toReconstrcut();
		if(!mfr.getFirstMessageName().equals(messageName)) continue;//给定的消息在原来查找到的序列里不合适，结束本次循环
		ArrayList<String> placeArray =mfr.getPlaceArray();
		String tmpMessageSequence=mfr.getMessageSequenceWithoutFirstMessageName();
		System.out.print("Warning: "+tmpMessageSequence+"\t");
		
		System.out.print("( ");
		for(int k=0;k<placeArray.size();k++){
			System.out.print(placeArray.get(k)+"("+IImageKeys.automataFilesName.get(k).
					substring(IImageKeys.automataFilesName.get(k).indexOf('/')+1,IImageKeys.automataFilesName.get(k).lastIndexOf('.'))+") ");				
		}
		System.out.println(")");
	}
	
	
	boolean isFind=true;
	while(isFind){
		isFind=false;
		for(int i=0;i<errorSequenceList.size();i++){
			Mission mission=errorSequenceList.get(i);
			if(!mission.getFirstMessageName().equals(messageName)) {					
				continue;
			}
			isFind=true;
			System.out.println("Warning: "+mission.getMessageSequenceWithoutFirstMessageName()+"\t");
			
			mission.setPreviousMessage(mission.getMessageSequenceWithoutFirstMessageName());
		
			ArrayList<String> placeArray =mission.getPlaceList();
			System.out.print("( ");
			for(int k=0;k<placeArray.size();k++){
				System.out.print(placeArray.get(k)+"("+IImageKeys.automataFilesName.get(k).
					substring(IImageKeys.automataFilesName.get(k).indexOf('/')+1,IImageKeys.automataFilesName.get(k).lastIndexOf('.'))+") ");				
			}
			System.out.println(")");
			}
	}
	
	long current2=System.nanoTime();
	timeList.add((current2 - currentTime1) / 1000);
	System.out.println("\n The execution time of analysis with reconstruct is: "
			+ (current2 - currentTime1) / 1000 + " us");
	System.out.println("*****************************************************************************");
}

 
} 

