package cn.cstv.wspscm.monitor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import cn.cstv.wspscm.IImageKeys;

public class AnalyzeMessageSequencePSC  {
	
	public static List<Boolean> sequentialObservation = new ArrayList<Boolean>();

	// private static List<Message> messageLog = new ArrayList<Message>();
	public static TimedAutomataSet timedAutomataSet = new TimedAutomataSet();
	static List<State> timedAutomataState = new ArrayList<State>();
	// public static Integer kSteps = -1;
	// public static Integer withControllability = 0; //0代表Error/Accept,
	// 1代表Controllability/Uncontrollability
	// public static Integer isDepthSearchFirst = 0; //0代表广度优先搜索，1代表深度优先搜索
	private static State currentProcessState = null;
	private static State nextProcessState = null;
    private static String stateNameplace;
	private static List<State> acceptState = new ArrayList<State>();
	private static List<State> errorState = new ArrayList<State>();

	private static List<String> errorMessage = new ArrayList<String>();
	private static List<Integer> acceptMessage = new ArrayList<Integer>();

	// public static List<List<Boolean>> sequentialObservationList = new
	// ArrayList<List<Boolean>>();

	private static List<Message> messageLog = new ArrayList<Message>();
	public static List<TimedAutomataSet> timedAutomataSetList = new ArrayList<TimedAutomataSet>();
	public static List<List<State>> timedAutomataStateList = new ArrayList<List<State>>();//billy：好东西 泛型.列表中的列表
	public static List<List<State>> timedAutomataStateListPSC = new ArrayList<List<State>>();
	public static Integer kSteps = -1;
	public static Integer withControllability = 0; // 0代表Error/Accept,
													// 1代表Controllability/Uncontrollability
	public static Integer isDepthSearchFirst = 0; // 0代表广度优先搜索，1代表深度优先搜索
	private static List<State> currentProcessStateList = new ArrayList<State>();
	private static List<State> nextProcessStateList = new ArrayList<State>();

	private static List<List<State>> acceptStateList = new ArrayList<List<State>>();
	private static List<List<State>> errorStateList = new ArrayList<List<State>>();
	private static  ArrayList  nofitpsc = new ArrayList();
	private static  List<ArrayList<String>> nofitpscList = new ArrayList<ArrayList<String>>();
	private static  List<ArrayList<String>> fitpsc = new ArrayList<ArrayList<String>> ();
	private static ConcurrentLinkedQueue<MissionForRefactorPSC> fitpscqueue = new  ConcurrentLinkedQueue<MissionForRefactorPSC>();
	private static ConcurrentLinkedQueue<MissionForRefactorPSC> tmpfitpscqueue = new  ConcurrentLinkedQueue<MissionForRefactorPSC>();
	private static  List<ArrayList<String>>  fitpscList = new ArrayList <ArrayList<String>> ();
	private static List<List<Integer>> acceptMessageList = new ArrayList<List<Integer>>();
	public static  List	<ArrayList<tmpMissionForRefactorPSC> > foundlist = new ArrayList<ArrayList<tmpMissionForRefactorPSC> >(); 
	public static List	<List<ArrayList<String>>> foundlistPSC = new ArrayList<List<ArrayList<String>>>(); 
	public static ArrayList<tmpMissionForRefactorPSC>   subfoundlist =new ArrayList<tmpMissionForRefactorPSC>();
	public static List<ArrayList<String>>   subfoundlistPSC =new ArrayList<ArrayList<String>>();
	public static  ArrayList<String> aa= new ArrayList<String> ();
	public static  ArrayList<String> bb= new ArrayList<String> ();
    
	private static ArrayList<MissionForRefactor> foundList = new ArrayList<MissionForRefactor>();// 存放了满足K步的所有可能组合的信息，为重构用
	private static ArrayList<MissionForRefactor> foundListPSC = new ArrayList<MissionForRefactor>();// 存放了满足K步的所有可能组合的信息，为重构用
	private static ArrayList<MissionForRefactor> tmpFoundList = new ArrayList<MissionForRefactor>();
	private static ConcurrentLinkedQueue<Mission> queue = new ConcurrentLinkedQueue<Mission>();// 任务队列
	private static ConcurrentLinkedQueue<Mission> queuePSC = new ConcurrentLinkedQueue<Mission>(); //暂时存放psc序列
	private static ConcurrentLinkedQueue<Mission> queuePSC1 = new ConcurrentLinkedQueue<Mission>();//最终放入psc序列
	public static HashMap<String,String> messageMap=new HashMap<String,String>();
//	private static String tempName = null;
//	private static boolean flag=false,flag2=false; 
	private static long currentTime1;
	
	public static final int INNER_MESSAGE=0,SEND_MESSAGE=1,RECEIVE_MESSAGE=2,NON_INNER_MESSAGE=10;//内部消息、发送消息、接收消息、
																	//非内部消息（即要么发送，要么接收。不是单独的消息类型，只是为了表明不是内部消息） 
	public static final int ERROR_MESSAGE=-1,ABNORMAL_MESSAGE=-2;
	
	private static boolean isDebug=true,isForever=true;//isForever控制是否不停的找K步（true为不停）,isDebug控制是否硬编码消息序列供重构用
	private static LinkedList<Mission> errorSequenceList=new LinkedList<Mission>();//存放错误的序列的链表
	private static ArrayList<Long> timeList=new ArrayList<Long>();//存放每步的计算时间

	public static Integer getkSteps() {
		return kSteps;
	}

	public static void setkSteps(Integer kSteps) {
		if (kSteps > 0) {
//			System.out.println("Look ahead "
//					+ kSteps
//					+ " steps "
//					+ (withControllability != 0 ? "with" : "without")
//					+ " controllability by "
//					+ (isDepthSearchFirst != 0 ? "breadth search first."
//							: "depth search first."));
		} else {
			System.out.println("Lookahead is closed.");
		}
		AnalyzeMessageSequencePSC.kSteps = kSteps;			
		
//		if(isDebug) return;
		
		if(isForever){			// billy :不停的找K步
			new Thread(){   //为什么要重新开启一个线程？？
				ArrayList<String> messageSeq=new ArrayList<String>();				
				public void run() {
					setMyMessageSeq(messageSeq);//设置messageSeq，如果是debug的时候，序列是硬编码的
					for(int j=0;j<1;j++){//循环100次    ??billy
						synchronizedFind();//tjf 20100925 使点了lookahead之后就自动进行寻找，不再经由每隔10秒的messageview线程调用
					for(int i=0;i<messageSeq.size();i++){							
							try {
								Thread.currentThread().sleep(200);//等待2秒重新开始下一次循环
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}			
							AyalyzingPreLookAheadForRefactor(messageSeq.get(i));
						}
						System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
						System.out.println("Look ahead " + getkSteps() + " steps （billy BFS） " );
						System.out.println("The time list: ");
						System.out.println("the first: "+timeList.get(0));
						for(int i=0;i<messageSeq.size();i++){
							System.out.print(messageSeq.get(i)+": "+timeList.get(i+1)+"  ");
						}
						System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
					}
				}
			}.start();
			
		}else
			synchronizedFind();
		
			
	}
	
	private static void synchronizedFind(){
		if (kSteps == -1) {
			return;
		}
		
		foundList.clear();//把用于重构的两个链表首先清空
		tmpFoundList.clear();
		errorSequenceList.clear();//把用于打印错误组合的链表清空
		timeList.clear();//清空时间链表
		
		// if (timedAutomataStateList.size() == 0 &&
		// currentProcessStateList.size() == 0) {//tjf
		if (timedAutomataStateList.size() == 0) {
			if (IImageKeys.automataFilesName.size() > 0) {
				long currentTime1 = System.nanoTime();

				InitilizeAutomataStateWithMultiAutomata(IImageKeys.automataFilesName);

				System.out.println();
//				System.out
//						.println("The execution time of InitilizeAutomataState is: "
//								+ (System.nanoTime() - currentTime1)
//								/ 1000
//								+ " us");
//				System.out
//						.println("------------------------------------------------------------------------");

			}			
		}

		currentTime1 = System.nanoTime();

		if (kSteps > 0) {
			System.out.println();
			System.out.println("Look ahead "
					+ kSteps
					+ " steps （billy BFS） "
					);
			System.out.println();

		}
//		GetMessageFromMessageSequence(messageString);
		// AnalyzingWithoutTimeRealTime();
		// AyalyzingWithoutTimePreLookAheadWithMultiAutomata();//tjf
		
		AyalyzingPreLookAheadWithMultiAutomata();		
		
	}
	
	
	private static void AyalyzingPreLookAheadWithMultiAutomata() {//我主写的一个方法，完成可能序列的寻找工作
//		ArrayList<String> messageSeq=new ArrayList<String>();
//		setMyMessageSeq(messageSeq);//设置messageSeq，如果是debug的时候，序列是硬编码的

		if (queue.isEmpty()) {// 第一次，队列是空的
//			System.out.println("queue is empty " + initStateNameList.size());
			// State s=timedAutomataState.get(0);
			for (int i = 0; i < timedAutomataStateList.size(); i++) {// 从每个自动机的起始状态开始找（billy：这边的size 表示有几个自动机）
				ArrayList<String> placeArray = new ArrayList<String>(timedAutomataStateList.size());
				
//				if(messageSeq.size()==0) 
//					initPlaceArray(placeArray);//在messageSeq长度为0的情况下，将位置链表初始化为各自动机的起始状态
//				else{
//					initPlaceArray(placeArray);
////					printPlaceArray(placeArray);
//					initPlaceArrayForKSteps(placeArray,messageSeq);
////					printPlaceArray(placeArray);	
//				}
				initPlaceArray(placeArray);//在messageSeq长度为0的情况下，将位置链表初始化为各自动机的起始状态    
				List<State> timedAutomataState = timedAutomataStateList.get(i);//billy：得到第i个自动机 里面的所有状态
//				String stateName = timedAutomataState.get(0).getStateName();
				String stateName =placeArray.get(i); //billy：第i个自动机的 名字 
			//	System.out.println("11 " + stateName);
				for (int j = 0; j < timedAutomataState.size(); j++) {// 根据起始状态的名字查找timedAutomataState （billy：指第i个自动机的每个状态）
					State currentState = timedAutomataState.get(j);
			//		System.out.println("22 ");
					if (currentState.getStateName().equals(stateName)) {// 找到了对应名字的state 
						List<String> innerList = currentState.getInnerMessageList();
				//		System.out.println("33 " + innerList.size());
						for (int k = 0; k < innerList.size(); k++) {// 内部消息都是可能的
							ArrayList<String> tmpPlaceArray=(ArrayList<String>)placeArray.clone();
							tmpPlaceArray.set(i, currentState.getEndStateName(innerList.get(k)));//billy：把内部消息对应的状态  staname放入到tmp
//							StringBuffer buffer=new StringBuffer(innerList.get(k));
//							synchronizedMoveInFindingMessageSeq(buffer,1,innerList.get(k),INNER_MESSAGE,tmpPlaceArray,i);
	//						printPlaceArray(tmpPlaceArray);						
							queue.add(new Mission(innerList.get(k), (ArrayList<String>) tmpPlaceArray.clone(), 1));
					//		System.out.println("内部消息中tmpPlaceArray的值"+tmpPlaceArray);
						}
						List<String> sendList = currentState.getSendMessageList();				
				//		List<String> receiveList=currentState.getReceiveMessageList();
					//	System.out.println("44 " + sendList.size());
						for (int k = 0; k < sendList.size(); k++) {// 发送必须和接收配对，才是可能的
							ArrayList<String> tmpPlaceArray=(ArrayList<String>)placeArray.clone();
							int temp = findInAllAuto(sendList.get(k),tmpPlaceArray);
							if (temp != -1) {
								tmpPlaceArray.set(i, currentState.getEndStateName(sendList.get(k)));
//								printPlaceArray(tmpPlaceArray);
								queue.add(new Mission(sendList
										.get(k),
										(ArrayList<String>) tmpPlaceArray.clone(),
										1));
								
							}
						}
				//		System.out.println("内部消息中tmpPlaceArray的值"+tmpPlaceArray);
				//		System.out.println("55 " + queue.size());
						break;//找到了第一个起始状态的描述，不再进行内循环
					}
				}
			}
		}
	//	System.out.println("初始化k步之前的代码"+queue.size());
		System.out.println("\n Possible right message sequences:\n");
		
			while (!queue.isEmpty()) {
			//	System.out.println("66 " + queue.size());
				Mission m = queue.poll();// 从队列取出，删
				boolean isFind=false;
				if (m.getSteps() < kSteps) {// 还没到K步，继续查找可能的组合
					String buffer = m.getPreviousMessage();
			//		System.out.println("--->>> " + m.getPreviousMessage() + " <<<---");
					// String nextName=m.getNextState1();
					// String nextName2=m.getNextState2();
					// int no1=m.getNo1();
					// int no2=m.getNo2();
					ArrayList<String> placeArray = m.getPlaceList();
			//		System.out.println("77 " + buffer+" steps: "+m.getSteps());
					for (int i = 0; i < timedAutomataStateList.size(); i++) {
						List<State> timedAutomataState = timedAutomataStateList.get(i);
						for (int j = 0; j < timedAutomataState.size(); j++) {// 根据下一状态的名字查找timedAutomataState 
							State currentState = timedAutomataState.get(j);
					//		System.out.println("88 "+currentState.getStateName());
							if (currentState.getStateName().equals(placeArray.get(i))) {// 在自动机i中找到了对应名字的state
								List<String> innerList = currentState.getInnerMessageList();
					//			System.out.println("99 " + innerList.size());
								for (int k = 0; k < innerList.size(); k++){// 内部消息都是可能的
									ArrayList<String> tmpPlaceArray=(ArrayList<String>)placeArray.clone();
									tmpPlaceArray.set(i, currentState.getEndStateName(innerList.get(k)));
//								 
									queue.add(new Mission(buffer+" , "+innerList.get(k), (ArrayList<String>)tmpPlaceArray.clone(), m.getSteps() + 1));
  						   
									isFind=true;
								}
								List<String> sendList = currentState
										.getSendMessageList();
//							 
								for (int k = 0; k < sendList.size(); k++){// 发送必须和接收配对，才是可能的
									ArrayList<String> tmpPlaceArray=(ArrayList<String>)placeArray.clone();
 
									if (findInAllAuto(sendList.get(k),tmpPlaceArray)!=-1){
										tmpPlaceArray.set(i, currentState.getEndStateName(sendList.get(k)));
 
										queue.add(new Mission(buffer+" , "+sendList.get(k),(ArrayList<String>)tmpPlaceArray.clone(), m.getSteps() + 1));
	 
										isFind=true;
									}}								
					 
								break;//找到了，就不再循环了，因为同一个状态没有第二个存储位置
							}
						}}
						if(!isFind){  //
							boolean isMissionExist=false;
							for(Mission mission:errorSequenceList ){//判断一下errorSequenceList中原来是不是已经加了这个任务
								if(mission.getPreviousMessage().equals(m.getPreviousMessage())) {
									isMissionExist=true;
									break;								
								}
							}

							if((!isMissionExist)
//									&&(!isMissionLeave)&&(!flag)
									) errorSequenceList.add(m);//如果mission没有下文了，则存入错误组合链表，供最后集中打印
			 
						}
					
					// queue.remove(m);//将m从队列中删掉
				} else if (m.getSteps() == kSteps) {// 已经到了指定的k，把队列中所有的mission内容打出来即可					
					System.out.println("---" + m.getPreviousMessage() + "---");
                    
					foundList.add(new MissionForRefactor(m.getPreviousMessage()+","+"end"+",",m.getPlaceList()));
				 
				}
			}
			

			//再次判断是否错误组合中有正确组合的部分，否则去掉   ？
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
			System.out.println("\n——————————————————————————————————————————————————————————————————————————————\n");
			System.out.println("\n checking k steps with proprety :\n");
			
			
			for(int i=0;i<foundList.size();i++){
       		 
  			  MissionForRefactor mr=foundList.get(i);
  		     ArrayList<String> mrPlaceArray= mr.getPlaceArray();
      		  String s ;
      		  s=mr.getMessageSequence();
         ArrayList<String> aaa=(ArrayList<String>)aa.clone();
      
  		  while(s.indexOf("end")>1){
  			 
  			
  			String   a = s.substring(0,s.indexOf(","));// 开始状态
  			  s=s.substring(s.indexOf(",")+1);
  			 
  			  aaa.add(a); 
  			 
  		  }
  		   subfoundlist.add(new tmpMissionForRefactorPSC(aaa,mrPlaceArray ));
  		   
  		  
  		 
  		   
			}
  	     	foundlist.add(subfoundlist);

 
				
				for (int i = 0; i < timedAutomataStateListPSC.size(); i++) {// 从每个自动机的起始状态开始找（billy：这边的size 表示有几个自动机）
					ArrayList<String> placeArray = new ArrayList<String>(timedAutomataStateListPSC.size());//没用
					List<State> timedAutomataStatePSC = timedAutomataStateListPSC.get(i);//billy：得到第i个自动机 里面的所有状态
			      	 initPlaceArrayPSC(placeArray);//在messageSeq长度为0的情况下，将位置链表初始化为各自动机的起始状态    
			    	
			  //   	 System.out.println("检测是否被执行1");
  	              
				  		  for(int m=0;m<foundlist.size();m++){
				         	 subfoundlist=foundlist.get(m);
				         	 
				  Label2:
				         	for(int mm=0;mm<subfoundlist.size();mm++){//每条正确序列
				         		tmpMissionForRefactorPSC mr=subfoundlist.get(mm);
				         		aa=mr.getMessageSequence();
				         		
				         		int p=0;
				         		 
					         	 int q=0;
					         	
				    	 String stateName =placeArray.get(i);     	 
				// 	         	System.out.println("检测是否被执行2");
				  Label3:
					    for(int mmm=q;mmm<aa.size();mmm++)  {     //for 1
						           	 
				
					for (int ii = p; ii < timedAutomataStatePSC.size(); ii++) {// for 2
						//	 System.out.println("检测是否被执行3"); 
						 		State currentState = timedAutomataStatePSC.get(ii);
						 		
						 		if(currentState.getStateName().equals(stateName)){  //if 2
						 							
							List<String> innerList = currentState.getInnerMessageList();
					//		System.out.println("innerList :::"+innerList);
							  int innerplace=-1;
								
								for(int x=0;x<innerList.size();x++){
									  String tmp1=innerList.get(x).replaceAll(" ","");
									  
									  String tmp2=aa.get(mmm).replaceAll(" ","");
									
									if(tmp1.equals(tmp2)){
										innerplace=x;
						//				System.out.println("xxxxxx"+tmp2);
                                     break;
									}
								
								}  
								
						//		System.out.println("aa.get(mmm) :::"+aa.get(mmm));
							if(innerplace>=0){
								for(int k=innerplace;k<innerList.size();k++){            //for 3
						 	//	  	  System.out.println("检测是否被执行4");  
						   	  boolean yesorno=currentState.getYesORnoList().get(k);
						 	//	  	  System.out.println("yesorno :::"+yesorno);					            
						            	  String tmp1=innerList.get(k).replaceAll(" ","");						
						            	  String tmp2=aa.get(mmm).replaceAll(" ","");						         
				                     if(yesorno){ //if1				        	           
				        	            stateName=currentState.getEndStateName(innerList.get(k));				        	           
				        	            if(stateName.contains("Error")){
				        	            	nofitpsc.add(aa);
				        	            	continue Label2;}
				        	              continue Label3;				                 
				          				             	}//if1
				                       else if(!yesorno){
				               //     	   System.out.println("检测"); 
				                    	  continue ;				                    	   
				                       }				                  
							}//for  3						 		      				                  
						      }
							else{
								           //for 3
						 		  	  
						   	  boolean yesorno=currentState.getYesORnoList().get(0);
								 if(!yesorno){
			                    	 
			                    		//  q=++mmm;
						        	         
					        	     //       stateName=currentState.getEndStateName(innerList.get(k+1));
					        	         
					        	              continue Label3;
			                    	} 
			                       else if(yesorno){
			                    	   stateName =placeArray.get(i);
			        	            	continue Label3;
			                       }
							}
						            
						}  //if 2
						      
						     
				                  
						} // for 2
						
						  
						}  // for 1  Label3
				 	         stateNameplace=stateName;
				 	        fitpsc.add(aa);
				 	         fitpscqueue.add(new MissionForRefactorPSC(aa,stateNameplace,mr.getPlaceArray()));
					         	 continue Label2;
					         	 }  //for Label2
				         	 
				  		  } // for Lablel1
			    	 } // for PSC文件数目
				System.out.println("\n满足PSC属性的序列：\n");
				for(int i=0;i<fitpsc.size();i++)
				{
					System.out.println(fitpsc.get(i));
				}
				
				System.out.println("\n 不满足PSC属性的序列: \n");
				for(int i=0;i<nofitpsc.size();i++)
				{
					System.out.println(nofitpsc.get(i));
				}  
							
			
			if (kSteps > 0) {
			
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
					.println("-----------------------------多自动机主动监控-------------------------------------------");	
		
	}
	
	/**专为重构设计的一个方法
	 * */
	public static void AyalyzingPreLookAheadForRefactor(String messageName){
		if(messageName==null){
			System.out.println("No input message to drive the automachine!");
			return;	
		}
		nofitpsc.clear();
		fitpsc.clear();
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
	//	System.out.println("初始化前"+tmpfitpscqueue.size());
		while(!tmpfitpscqueue.isEmpty()){		
			MissionForRefactorPSC mr=tmpfitpscqueue.poll();
			fitpscqueue.add(mr);
			
		}
		tmpfitpscqueue.clear();
	//	System.out.println("初始化后"+fitpscqueue.size());
	//	System.out.println("初始化后"+tmpfitpscqueue.size());
		System.out.println("\n Possible right message sequences(billy refactor):\n");
		Label1:
			while(!fitpscqueue.isEmpty())
			{  
				ArrayList<String> placeArray ;		
				
				MissionForRefactorPSC mfr;
				
				mfr=fitpscqueue.poll();
				if(!mfr.getFirstMessageName().equals(messageName)) continue ;
				placeArray=mfr.getPlaceArray();
				ArrayList<String> tmpMessageSequence=mfr.getMessageSequenceWithoutFirstMessageName();
		    	 String stateNamepsc =mfr.getstateNameplace();
		Label2:		
		for (int i = 0; i < timedAutomataStateList.size(); i++) {// 从每个自动机的起始状态开始找

				boolean isFind=false;
			List<State> timedAutomataState = timedAutomataStateList.get(i);
			 
			String stateName =placeArray.get(i);
         
			for (int j = 0; j < timedAutomataState.size(); j++) {// 根据起始状态的名字查找timedAutomataState
				State currentState = timedAutomataState.get(j);
		 
//				System.out.println("22 ");
				if (currentState.getStateName().equals(stateName)) {// 找到了对应名字的state
					List<String> innerList = currentState.getInnerMessageList();
//					System.out.println("33 " + innerList.size());
					Label3:
					for (int k = 0; k < innerList.size(); k++) {// 内部消息都是可能的
						isFind=true;
						
						ArrayList<String> tmpPlaceArray=(ArrayList<String>)placeArray.clone();
						tmpPlaceArray.set(i, currentState.getEndStateName(innerList.get(k)));
						ArrayList<String> tmpMessageSequence1=(ArrayList<String>)tmpMessageSequence.clone();
 				      tmpMessageSequence1.add(innerList.get(k));
 				//     System.out.println("内部消息条数");    
	     			
						System.out.println("*** "+tmpMessageSequence1 +"***");
					//	System.out.println("内部消息中得 2");
					//	--------------------------判断重构时候每增加一步 检测PSC属性 
						for (int ipsc = 0; ipsc < timedAutomataStateListPSC.size(); ipsc++) {// 从每个自动机的起始状态开始找（billy：这边的size 表示有几个自动机）
							ArrayList<String> placeArraypsc = new ArrayList<String>(timedAutomataStateListPSC.size());//没用
							List<State> timedAutomataStatePSC = timedAutomataStateListPSC.get(ipsc);//billy：得到第i个自动机 里面的所有状态
					      	 initPlaceArrayPSC(placeArraypsc);//在messageSeq长度为0的情况下，将位置链表初始化为各自动机的起始状态   
							
					       	 String s0=placeArraypsc.get(ipsc);
					   
						   
							for (int ii = 0; ii < timedAutomataStatePSC.size(); ii++) {// for 2
						//			 System.out.println("检测是否被执行3"); 
									
								 		State currentStatepsc = timedAutomataStatePSC.get(ii);
								 	
								 		if(currentStatepsc.getStateName().equals(stateNamepsc)){  //if 2
								 							
									List<String> innerListpsc = currentStatepsc.getInnerMessageList();
						//			System.out.println("innerList :::"+innerListpsc);
									  int innerplace=-1;
										
										for(int x=0;x<innerListpsc.size();x++){
											  String tmp1=innerListpsc.get(x).replaceAll(" ","");
											  
											  String tmp2=innerList.get(k).replaceAll(" ","");
											
											if(tmp1.equals(tmp2)){
												innerplace=x;
								//			System.out.println("xxxxxx"+tmp2);
		                                     break;
											}
										
										}  
										
						//				System.out.println("aa.get(mmm) :::"+innerList.get(k));
									if(innerplace>=0){
										for(int kpsc=innerplace;kpsc<innerListpsc.size();kpsc++){            //for 3
							//	 		  	  System.out.println("检测是否被执行4");  
								   	  boolean yesorno=currentStatepsc.getYesORnoList().get(kpsc);
								 		  	 					            
								            	  String tmp1=innerListpsc.get(kpsc).replaceAll(" ","");						
								            	  String tmp2=innerList.get(k).replaceAll(" ","");						         
						                     if(tmp1.equals(tmp2)&&yesorno){ //if1		
						                    	 String tmpstateNamepsc=stateNamepsc;
						        	            tmpstateNamepsc=currentStatepsc.getEndStateName(innerListpsc.get(kpsc));				        	           
						        	            if(tmpstateNamepsc.contains("Error")){
						        	            	nofitpsc.add(tmpMessageSequence1);
						        	           	continue Label3;
						        	            	}else{
						        	            		
												 	        fitpsc.add(tmpMessageSequence1);
												 	       tmpfitpscqueue.add(new MissionForRefactorPSC(tmpMessageSequence1,mfr.getstateNameplace(),tmpPlaceArray));
												 	  
												 	         continue Label3;
						        	            	}
						        	              				                 
						          				             	}//if1
						                       else if(tmp1.equals(tmp2)&&!yesorno){
						      //            	   System.out.println("检测"); 
						                    	  continue ;				                    	   
						                       }				                  
									}//for  3						 		      				                  
								      }
									else{
										           
								 		  	  
								   	  boolean yesorno=currentState.getYesORnoList().get(0);
										 if(!yesorno){
											 fitpsc.add(tmpMessageSequence1);
											 tmpfitpscqueue.add(new MissionForRefactorPSC(tmpMessageSequence1,mfr.getstateNameplace(),tmpPlaceArray));
											
								 	       continue Label3;
					                    	} 
					                       else if(yesorno){
					                        
					                    	   
					                    	   fitpsc.add(tmpMessageSequence1);
					                    	   tmpfitpscqueue.add(new MissionForRefactorPSC(tmpMessageSequence1,mfr.getstateNameplace(),tmpPlaceArray));
					                    	   
									 	         continue Label3;
					                       }
									}
									         
								}  //if 2
								       
								} // for 2
								
					    	 } // for PSC文件数目
						
					
							
						
						
					}
					
					List<String> sendList = currentState.getSendMessageList();
					// List<String>
					// receiveList=currentState.getReceiveMessageList();
//					System.out.println("44 " + sendList.size());]
					Label4:
					for (int k = 0; k < sendList.size(); k++) {// 发送必须和接收配对，才是可能的
						ArrayList<String> tmpPlaceArray=(ArrayList<String>)placeArray.clone();
						int temp = findInAllAuto(sendList.get(k),tmpPlaceArray);
						if (temp != -1) {
							tmpPlaceArray.set(i, currentState.getEndStateName(sendList.get(k)));
//							printPlaceArray(tmpPlaceArray);
//							queue.add(new Mission(sendList.get(k),
//									(ArrayList<String>) tmpPlaceArray.clone(),
//									1));
							isFind=true;
							ArrayList<String> tmpMessageSequence2= (ArrayList<String>)tmpMessageSequence.clone(); 
							 tmpMessageSequence2.add(sendList.get(k));
					//		 System.out.println("检测到4");    
						//		tmprefitpsc.add(new MissionForRefactorPSC(tmpMessageSequence2,mfr.getstateNameplace(),tmpPlaceArray));
							System.out.println(" *** "+tmpMessageSequence2+" *** ");
							
							//	--------------------------判断重构时候每增加一步 检测PSC属性 
							for (int ipsc = 0; ipsc < timedAutomataStateListPSC.size(); ipsc++) {// 从每个自动机的起始状态开始找（billy：这边的size 表示有几个自动机）
								ArrayList<String> placeArraypsc = new ArrayList<String>(timedAutomataStateListPSC.size());//没用
								List<State> timedAutomataStatePSC = timedAutomataStateListPSC.get(ipsc);//billy：得到第i个自动机 里面的所有状态
						      	 initPlaceArrayPSC(placeArraypsc);//在messageSeq长度为0的情况下，将位置链表初始化为各自动机的起始状态   
								
						       	 String s0=placeArraypsc.get(ipsc);
						   

							   
								for (int ii = 0; ii < timedAutomataStatePSC.size(); ii++) {// for 2
						//				 System.out.println("检测是否被执行3"); 
										
									 		State currentStatepsc = timedAutomataStatePSC.get(ii);
									 	
									 		if(currentStatepsc.getStateName().equals(stateNamepsc)){  //if 2
									 							
										List<String> innerListpsc = currentStatepsc.getInnerMessageList();
						//				System.out.println("innerList :::"+innerListpsc);
										  int innerplace=-1;
											
											for(int x=0;x<innerListpsc.size();x++){
												  String tmp1=innerListpsc.get(x).replaceAll(" ","");
												  
												  String tmp2=sendList.get(k).replaceAll(" ","");
												
												if(tmp1.equals(tmp2)){
													innerplace=x;
										//			System.out.println("xxxxxx"+tmp2);
			                                     break;
												}
											
											}  
											
							 		//System.out.println("innerplace的值1："+innerplace);
										if(innerplace>=0){
											for(int kpsc=innerplace;kpsc<innerListpsc.size();kpsc++){            //for 3
									 //    	System.out.println("innerplace的值1："+innerplace);
									   	  boolean yesorno=currentStatepsc.getYesORnoList().get(kpsc);	
									//   	  System.out.println("innerplace的值2："+innerplace);
									            	  String tmp1=innerListpsc.get(kpsc).replaceAll(" ","");						
									            	  String tmp2=sendList.get(k).replaceAll(" ","");						         
							                     if(yesorno){ //if1		
							                    	 String tmpstateNamepsc=stateNamepsc;
							        	            tmpstateNamepsc=currentStatepsc.getEndStateName(innerListpsc.get(kpsc));				        	           
							        	            if(tmpstateNamepsc.contains("Error")){
							        	            	nofitpsc.add(tmpMessageSequence2);
							        	            	continue Label4;
							        	            	}else{
							        	            		
													 	        fitpsc.add(tmpMessageSequence2);
													 	       tmpfitpscqueue.add(new MissionForRefactorPSC(tmpMessageSequence2,mfr.getstateNameplace(),tmpPlaceArray));
													 	       
													 	     
													 	        continue Label4;
							        	            	}
							        	              				                 
							          				             	}//if1
							                       else if(!yesorno){
							              //      	   System.out.println("检测"); 
							                  	  continue ;				                    	   
							                       }				                  
										}//for  3						 		      				                  
									      }
										else{
											           
									 		  	  
									   	  boolean yesorno=currentState.getYesORnoList().get(0);
											 if(!yesorno){
												 fitpsc.add(tmpMessageSequence2);
												 tmpfitpscqueue.add(new MissionForRefactorPSC(tmpMessageSequence2,mfr.getstateNameplace(),tmpPlaceArray));
												
									 	       continue Label4;
						                    	} 
						                       else if(yesorno){
						                        
						                    	   
						                    	   fitpsc.add(tmpMessageSequence2);
						                    	   tmpfitpscqueue.add(new MissionForRefactorPSC(tmpMessageSequence2,mfr.getstateNameplace(),tmpPlaceArray));
						                    	 
										        continue Label4;
						                       }
										}
										         
									}  //if 2
									       
									} // for 2
									
						    	 } // for PSC文件数目
							
									
							
							
							
							
						}
					}
//					System.out.println("55 " + queue.size());
				//	break;//找到了第一个起始状态的描述，不再进行内循环
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
		System.out.println("\n满足PSC属性的序列：\n");
		for(int iii=0;iii<fitpsc.size();iii++)
		{
			System.out.println(fitpsc.get(iii));
		}
		
		System.out.println("\n 不满足PSC属性的序列: \n");
		for(int iii=0;iii<nofitpsc.size();iii++)
		{
			System.out.println(nofitpsc.get(iii));
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
	
	/**a :位置链表
	 * */
	static void initPlaceArray(ArrayList<String> a) {//把位置连表初始化为各自动机的起始状态
		for (int j = 0; j < timedAutomataStateList.size(); j++) {
			a.add(timedAutomataStateList.get(j).get(0).getStateName());//billy:得到每个自动机的名字 
		}
	}
	
	static void initPlaceArrayPSC(ArrayList<String> a) {//把位置连表初始化为各自动机的起始状态
		for (int j = 0; j < timedAutomataStateListPSC.size(); j++) {
			a.add(timedAutomataStateListPSC.get(j).get(0).getStateName());//billy:得到每个自动机的名字 
		}
	}
	/**a :位置链表
	 * 在消息序列有内容时执行，做K步前的准备工作：移动位置链表
	 * */
	private static void initPlaceArrayForKSteps(ArrayList<String> a,ArrayList<String> messageSeq){ // billy：没调用到啊
		ConcurrentLinkedQueue<ExamineMission> examineQueue=new ConcurrentLinkedQueue<ExamineMission> ();
		String messageName=messageSeq.get(0);//第一个消息名先取出	
		int messageType=AnalyseMessageType(messageName);//判断下这个消息的类型，以及是否合法
			for (int i = 0; i < timedAutomataStateList.size(); i++) {
//				boolean isFind=false;
				ArrayList<String> placeArray = (ArrayList<String>)a.clone();
				List<State> timedAutomataState = timedAutomataStateList.get(i);
				String stateName = timedAutomataState.get(0).getStateName();
				for (int j = 0; j < timedAutomataState.size(); j++) {// 根据起始状态的名字查找timedAutomataState
					State currentState = timedAutomataState.get(j);

//					System.out.println("22 ");
					if (currentState.getStateName().equals(stateName)) {// 找到了对应起始状态的state
						switch(messageType){
						case INNER_MESSAGE:
							List<String> innerList = currentState.getInnerMessageList();
//							System.out.println("33 " + innerList.size());
							for (int k = 0; k < innerList.size(); k++) {// 
								if(innerList.get(k).equals(messageName)){
									ArrayList<String> leftMessage=(ArrayList<String>)messageSeq.clone();
									leftMessage.remove(messageName);

									placeArray.set(i, currentState.getEndStateName(innerList.get(k)));
									synchronizedMoveByMessageSeq(messageName,messageType,placeArray,i);//确保其他自动机也因这个消息而发生了移动
//									printPlaceArray(placeArray);
									examineQueue.add(new ExamineMission(innerList.get(k),(ArrayList<String>)leftMessage.clone(), (ArrayList<String>) placeArray.clone()));
									break;
								}
							}
							break;
						case NON_INNER_MESSAGE:
							List<String> sendList = currentState.getSendMessageList();
							// List<String>
							// receiveList=currentState.getReceiveMessageList();
//							System.out.println("44 " + sendList.size());
							for (int k = 0; k < sendList.size(); k++) {
								if(sendList.get(k).equals(messageName)){
									ArrayList<String> leftMessage=(ArrayList<String>)messageSeq.clone();
									leftMessage.remove(messageName);
									placeArray.set(i, currentState.getEndStateName(sendList.get(k)));
									synchronizedMoveByMessageSeq(messageName,messageType,placeArray,i);//确保其他自动机也因这个消息而发生了移动
//									printPlaceArray(placeArray);
									examineQueue.add(new ExamineMission(sendList.get(k),(ArrayList<String>)leftMessage.clone(), (ArrayList<String>) placeArray.clone()));
									break;
								}
							}
							List<String> receiveList=currentState.getReceiveMessageList();
							for (int k = 0; k < receiveList.size(); k++) {
								if(receiveList.get(k).equals(messageName)){
									ArrayList<String> leftMessage=(ArrayList<String>)messageSeq.clone();
									leftMessage.remove(messageName);
									placeArray.set(i, currentState.getEndStateName(receiveList.get(k)));
									synchronizedMoveByMessageSeq(messageName,messageType,placeArray,i);//确保其他自动机也因这个消息而发生了移动
//									printPlaceArray(placeArray);
									examineQueue.add(new ExamineMission(receiveList.get(k),(ArrayList<String>)leftMessage.clone(), (ArrayList<String>) placeArray.clone()));
									break;
								}
							}
							break;
						default:
								System.out.println("default");
						}
						
//						System.out.println("55 " + queue.size());
						break;//找到了第一个起始状态的描述，不再进行内循环
					}
				}
			}
			
		
	if(examineQueue.isEmpty()){//第一个消息都没有匹配，自然退出
		System.out.println("Don't find because of the first message name in message sequence! \n Therefore looking ahead k steps is cancelled!!!");		
		return;
	}
	
	while(!examineQueue.isEmpty()){
			ExamineMission m = examineQueue.poll();// 从队列取出，删
			messageSeq=m.getList();
//			System.out.println(examineQueue.size()+" ***");
			boolean isFind=false;
			if (messageSeq.size()!=0) {// 还没判断完毕给定的消息序列	
//				System.out.println(messageSeq.size());
				messageName=messageSeq.get(0);
				messageType=AnalyseMessageType(messageName);//判断下这个消息的类型，以及是否合法
				ArrayList<String> placeArray = m.getPlaceList();
				for (int i = 0; i < timedAutomataStateList.size(); i++) {
					List<State> timedAutomataState = timedAutomataStateList.get(i);
					for (int j = 0; j < timedAutomataState.size(); j++) {// 根据下一状态的名字查找timedAutomataState
						State currentState = timedAutomataState.get(j);
//						System.out.println("88 "+currentState.getStateName());
//						System.out.println("$$$ "+currentState.getStateName());
//						System.out.println("$$$$$ "+placeArray.get(i));
						if (currentState.getStateName().equals(placeArray.get(i))) {// 在自动机i中找到了对应名字的state
							switch(messageType){
							case INNER_MESSAGE:
								List<String> innerList = currentState.getInnerMessageList();
//								System.out.println("99 " + innerList.size());
								for (int k = 0; k < innerList.size(); k++){// 
//									System.out.println("&&& "+innerList.get(k));
									if(innerList.get(k).equals(messageName)){
										isFind=true;
										ArrayList<String> leftMessage=(ArrayList<String>)messageSeq.clone();
										leftMessage.remove(messageName);
										placeArray.set(i, currentState.getEndStateName(innerList.get(k)));
										synchronizedMoveByMessageSeq(messageName,messageType,placeArray,i);//确保其他自动机也因这个消息而发生了移动
//										printPlaceArray(placeArray);
										examineQueue.add(new ExamineMission(innerList.get(k),(ArrayList<String>)leftMessage.clone(), (ArrayList<String>) placeArray.clone()));
										break;
									}
								}
								break;
							case NON_INNER_MESSAGE:
								List<String> sendList = currentState.getSendMessageList();
								List<String> receiveList = currentState
										.getReceiveMessageList();
//								System.out.println("991 " + sendList.size());
								for (int k = 0; k < sendList.size(); k++){
//									System.out.println("%%% "+sendList.get(k));
									if(sendList.get(k).equals(messageName))
									{
										isFind=true;
										ArrayList<String> leftMessage=(ArrayList<String>)messageSeq.clone();
										leftMessage.remove(messageName);
										placeArray.set(i, currentState.getEndStateName(sendList.get(k)));
										synchronizedMoveByMessageSeq(messageName,messageType,placeArray,i);//确保其他自动机也因这个消息而发生了移动
//										printPlaceArray(placeArray);
										examineQueue.add(new ExamineMission(sendList.get(k),(ArrayList<String>)leftMessage.clone(), (ArrayList<String>) placeArray.clone()));
										break;
									}
								}
								for (int k = 0; k < receiveList.size(); k++){
//									System.out.println("%%%% "+receiveList.get(k));
									if(receiveList.get(k).equals(messageName))
									{
										isFind=true;
										ArrayList<String> leftMessage=(ArrayList<String>)messageSeq.clone();
										leftMessage.remove(messageName);
										placeArray.set(i, currentState.getEndStateName(receiveList.get(k)));
										synchronizedMoveByMessageSeq(messageName,messageType,placeArray,i);//确保其他自动机也因这个消息而发生了移动
//										printPlaceArray(placeArray);
										examineQueue.add(new ExamineMission(receiveList.get(k),(ArrayList<String>)leftMessage.clone(), (ArrayList<String>) placeArray.clone()));
										break;
									}
								}
								break;
							}																
//							System.out.println("00 " + queue.size());
							break;//找到了，就不再循环了，因为同一个状态没有第二个存储位置
						}
					}						
				}
				if(!isFind&&(examineQueue.size()==0)){//没有找到，而且队列中也没有任务了，匹配失败，退出
					System.out.println("Not Find!");
					System.out.println("Therefore looking ahead k steps is cancelled!!!");
					return;
				}
				
			}
			else  {// 该判断的消息序列都判断完了，成功
				System.out.println("--- The Message Sequence has been found in automachine! ---");
				ArrayList<String> tmp=m.getPlaceList();
				for (int i=0;i<tmp.size();i++){
					a.set(i,tmp.get(i));
				}				
				return;
			}
		}
	
	}
	
	public static void printPlaceArray(ArrayList<String> a){
		for (int j = 0; j < a.size(); j++) {
			System.out.print(a.get(j)+" ");
		}
		System.out.println();
	}



	public static int findInAllAuto(String name, ArrayList<String> a) {//找到与发送配对的接收消息所在的自动机
		// tempHashmap.clear();
//		String tempName = null;
		for (int i = 0; i < timedAutomataStateList.size(); i++) {
			List<State> timedAutomataState = timedAutomataStateList.get(i);
			for (int j = 0; j < timedAutomataState.size(); j++) {
				State s = timedAutomataState.get(j);
				if(s==null) System.out.println("s is null");
				if(a==null) System.out.println("a is null");
				if(a.get(i)==null) System.out.println("a.get i is null "+i+" "+a.size());
				if(s.getStateName()==null) System.out.println("s.getstatename is null");
				List<String> l;
				if (a.get(i).equals(s.getStateName())) { //billy：这里a.get(i)取出的是状态S0。 equal =s.getstatename  
					l = s.getReceiveMessageList();
					for(int k=0;k<l.size();k++){
					if (l.get(k).equals(name)) {						
						a.set(i, s.getEndStateName(name));//更新找到对应接收命令的自动机，并在位置链表中设置
						return i;
				         
					}
					}
				}
			}
		}
		return -1;
	}
	
	public static void setMyMessageSeq(ArrayList<String> messageSeq){
		if(isDebug){ //billy ： 这里表示使用硬编码 为了重构
			messageSeq.add("[receive]SubmitRequst");
			messageSeq.add("AgentBusy");
			messageSeq.add("AgentFree");
			messageSeq.add("[invoke]SendPatientCondition(patientCondition)");
			messageSeq.add("hospitalList");
			messageSeq.add("CompareHospital");
			messageSeq.add("EnsureHospital");
			messageSeq.add("[invoke]ChoosePrimaryHospital(hospitalList)");
			messageSeq.add("primaryHospital");
			messageSeq.add("makeAppointment(detailDate)");
			
			
			messageMap.put("[receive]SubmitRequst", "[MedicalServiceAgent]org.equinoxosgi.toast.internal.client.emergency.EmergencyMonitor.[receive]SubmitRequst");
			messageMap.put("AgentBusy","[MedicalServiceAgent]org.equinoxosgi.toast.internal.dev.gps.sim.GpsCompassSensor.AgentBusy");
			messageMap.put("AgentFree","[MedicalServiceAgent]org.equinoxosgi.toast.internal.dev.gps.sim.GpsCompassSensor.AgentFree");
			messageMap.put("[invoke]SendPatientCondition(patientCondition)","[MedicalServiceAgent]org.equinoxosgi.toast.internal.dev.gps.sim.GpsCompassSensor.[invoke]SendPatientCondition(patientCondition)");
			messageMap.put("hospitalList","[MedicalServiceAgent]org.equinoxosgi.toast.internal.dev.gps.sim.GpsCompassSensor.hospitalList");
			messageMap.put("CompareHospital","[MedicalServiceAgent]org.equinoxosgi.toast.internal.dev.gps.sim.GpsCompassSensor.CompareHospital");
			messageMap.put("EnsureHospital","[MedicalServiceAgent]org.equinoxosgi.toast.internal.dev.gps.sim.GpsCompassSensor.EnsureHospital");
			messageMap.put("[invoke]ChoosePrimaryHospital(hospitalList)","[MedicalServiceAgent]org.equinoxosgi.toast.internal.dev.gps.sim.GpsCompassSensor.[invoke]ChoosePrimaryHospital(hospitalList)");
			messageMap.put("primaryHospital","[MedicalServiceAgent]org.equinoxosgi.toast.internal.dev.gps.sim.GpsCompassSensor.primaryHospital");
			messageMap.put("makeAppointment(detailDate)","[MedicalServiceAgent]org.equinoxosgi.toast.core.channel.sender.ChannelMessage.makeAppointment(detailDate)");
			
		}
		else
			for(int i=0;i<messageLog.size();i++){
				Message m=messageLog.get(i);
				messageSeq.add(m.getMessageText());
			}
	}
	
	/**根据消息序列中某个消息，判定它是不是内部消息
	 * */
	private static int AnalyseMessageType(String messageName){
		boolean findInSend=false,findInReceive=false;
		for (int i = 0; i < timedAutomataStateList.size(); i++) {
			List<State> timedAutomataState = timedAutomataStateList.get(i);
			for (int j = 0; j < timedAutomataState.size(); j++) {
				State currentState = timedAutomataState.get(j);
				List<String> innerList = currentState.getInnerMessageList();
				if(innerList.contains(messageName)){//内部消息链表中包含该消息，返回内部消息的编号：0
					return INNER_MESSAGE;
				}
				List<String> sendList = currentState.getSendMessageList();
				if(sendList.contains(messageName)){//发送消息链表中包含该消息，需要区别处理
					if(!findInSend) findInSend=true;//原来没有发现过，设标志位
					if(findInReceive) return NON_INNER_MESSAGE;//原来发现过，返回非内部消息的编号
				}
				List<String> receiveList = currentState.getReceiveMessageList();
				if(receiveList.contains(messageName)){
					if(!findInReceive) findInReceive=true;
					if(findInSend) return NON_INNER_MESSAGE;
				}
			}
		}
		
		if(!(findInSend&&findInReceive))//存在不配对的情况，则自动机本身有误，提示给用户
			if(findInSend||findInReceive) {
				System.out.println("The message name: "+messageName+" isn't matching! \n  Please check the auto file!!!");
				return ERROR_MESSAGE;
			}else{
				System.out.println("The message name: "+messageName+" can't be found! \n  Please check the auto file!!!");
				return ERROR_MESSAGE;
			}
		
		return ABNORMAL_MESSAGE;
	}
	

	
	/**messageName：要寻找的那个消息名
	 * messageType：要寻找的那个消息类型
	 * list：表示寻找位置的链表
	 * except：同步时例外的那个自动机，因为在examineMessageSeq已经找到了
	 * */
	private static void synchronizedMoveByMessageSeq(String messageName,int messageType,ArrayList<String> list,int except){
		for (int i = 0; i < timedAutomataStateList.size(); i++) {
			if(i==except) continue;
			List<State> timedAutomataState = timedAutomataStateList.get(i);
			String stateName=list.get(i);
			for (int j = 0; j < timedAutomataState.size(); j++) {
				State s = timedAutomataState.get(j);
				List<String> l;
				switch(messageType){
				case INNER_MESSAGE:
					if (stateName.equals(s.getStateName())) {
						l = s.getInnerMessageList();
						for(int k=0;k<l.size();k++){
						if (l.get(k).equals(messageName)) {
							list.set(i, s.getEndStateName(messageName));							
						}
						}
					}
					break;
					default:
						if (stateName.equals(s.getStateName())) {
							l=s.getSendMessageList();
							for(int k=0;k<l.size();k++){
								if (l.get(k).equals(messageName)) {
									list.set(i, s.getEndStateName(messageName));							
								}
							}
							l=s.getReceiveMessageList();
							for(int k=0;k<l.size();k++){
								if (l.get(k).equals(messageName)) {
									list.set(i, s.getEndStateName(messageName));							
								}
							}
						}
				}				
			}
		}
	}
	
	public static void examineMessageSeq(){		//我主写的第二个方法，用于判断传入的消息序列是否为自动机所支持的序列
		ArrayList<String> messageSeq=new ArrayList<String>();
		setMyMessageSeq(messageSeq);//设置messageSeq，如果是debug的时候，序列是硬编码的
		
		ConcurrentLinkedQueue<ExamineMission> examineQueue=new ConcurrentLinkedQueue<ExamineMission> ();
		if(messageSeq.size()==0) return;		
		
		if (timedAutomataStateList.size() == 0) {//确保所有的自动机信息被正确的加载解析了
			if (IImageKeys.automataFilesName.size() > 0) {				
				InitilizeAutomataStateWithMultiAutomata(IImageKeys.automataFilesName);
			}			
		}
		
			long currentTime = System.nanoTime();//记录下开始执行的时间
			String messageName=messageSeq.get(0);//第一个消息名先取出	
			int messageType=AnalyseMessageType(messageName);//判断下这个消息的类型，以及是否合法
				for (int i = 0; i < timedAutomataStateList.size(); i++) {
//					boolean isFind=false;
					ArrayList<String> placeArray = new ArrayList<String>(timedAutomataStateList.size());
					initPlaceArray(placeArray);
					List<State> timedAutomataState = timedAutomataStateList.get(i);
					String stateName = timedAutomataState.get(0).getStateName();
					for (int j = 0; j < timedAutomataState.size(); j++) {// 根据起始状态的名字查找timedAutomataState
						State currentState = timedAutomataState.get(j);
//						System.out.println("22 ");
						if (currentState.getStateName().equals(stateName)) {// 找到了对应起始状态的state
							switch(messageType){
							case INNER_MESSAGE:
								List<String> innerList = currentState.getInnerMessageList();
//								System.out.println("33 " + innerList.size());
								for (int k = 0; k < innerList.size(); k++) {// 
									if(innerList.get(k).equals(messageName)){
										ArrayList<String> leftMessage=(ArrayList<String>)messageSeq.clone();
										leftMessage.remove(messageName);
										placeArray.set(i, currentState.getEndStateName(innerList.get(k)));
										synchronizedMoveByMessageSeq(messageName,messageType,placeArray,i);//确保其他自动机也因这个消息而发生了移动
//										printPlaceArray(placeArray);
										examineQueue.add(new ExamineMission(innerList.get(k),(ArrayList<String>)leftMessage.clone(), (ArrayList<String>) placeArray.clone()));
										break;
									}
								}
								break;
							case NON_INNER_MESSAGE:
								List<String> sendList = currentState.getSendMessageList();
								// List<String>
								// receiveList=currentState.getReceiveMessageList();
//								System.out.println("44 " + sendList.size());
								for (int k = 0; k < sendList.size(); k++) {
									if(sendList.get(k).equals(messageName)){
										ArrayList<String> leftMessage=(ArrayList<String>)messageSeq.clone();
										leftMessage.remove(messageName);
										placeArray.set(i, currentState.getEndStateName(sendList.get(k)));
										synchronizedMoveByMessageSeq(messageName,messageType,placeArray,i);//确保其他自动机也因这个消息而发生了移动
//										printPlaceArray(placeArray);
										examineQueue.add(new ExamineMission(sendList.get(k),(ArrayList<String>)leftMessage.clone(), (ArrayList<String>) placeArray.clone()));
										break;
									}
								}
								List<String> receiveList=currentState.getReceiveMessageList();
								for (int k = 0; k < receiveList.size(); k++) {
									if(receiveList.get(k).equals(messageName)){
										ArrayList<String> leftMessage=(ArrayList<String>)messageSeq.clone();
										leftMessage.remove(messageName);
										placeArray.set(i, currentState.getEndStateName(receiveList.get(k)));
										synchronizedMoveByMessageSeq(messageName,messageType,placeArray,i);//确保其他自动机也因这个消息而发生了移动
//										printPlaceArray(placeArray);
										examineQueue.add(new ExamineMission(receiveList.get(k),(ArrayList<String>)leftMessage.clone(), (ArrayList<String>) placeArray.clone()));
										break;
									}
								}
								break;
							default:
									System.out.println("default");
//									innerList = currentState.getInnerMessageList();
////									System.out.println("33 " + innerList.size());
//									for (int k = 0; k < innerList.size(); k++) {// 
//										if(innerList.get(k).equals(messageName)){
//											ArrayList<String> leftMessage=(ArrayList<String>)messageSeq.clone();
//											leftMessage.remove(messageName);
//											placeArray.set(i, currentState.getEndStateName(innerList.get(k)));
////											printPlaceArray(placeArray);
//											examineQueue.add(new ExamineMission(innerList.get(k),(ArrayList<String>)leftMessage.clone(), (ArrayList<String>) placeArray.clone()));
//										}
//									}
//									sendList = currentState.getSendMessageList();
//									// List<String>
//									// receiveList=currentState.getReceiveMessageList();
////									System.out.println("44 " + sendList.size());
//									for (int k = 0; k < sendList.size(); k++) {
//										if(sendList.get(k).equals(messageName)){
//											ArrayList<String> leftMessage=(ArrayList<String>)messageSeq.clone();
//											leftMessage.remove(messageName);
//											placeArray.set(i, currentState.getEndStateName(sendList.get(k)));
////											printPlaceArray(placeArray);
//											examineQueue.add(new ExamineMission(sendList.get(k),(ArrayList<String>)leftMessage.clone(), (ArrayList<String>) placeArray.clone()));									
//										}
//									}
							}
							
//							System.out.println("55 " + queue.size());
							break;//找到了第一个起始状态的描述，不再进行内循环
						}
					}
				}
				
			
		if(examineQueue.isEmpty()){//第一个消息都没有匹配，自然退出
			System.out.println("not find because of the first message name!");
			System.out.println("The execution time of examine the message sequence is: "+ (System.nanoTime() - currentTime) / 1000 + " us");
			return;
		}
		
		while(!examineQueue.isEmpty()){
				ExamineMission m = examineQueue.poll();// 从队列取出，删
				messageSeq=m.getList();
//				System.out.println(examineQueue.size()+" ***");
				boolean isFind=false;
				if (messageSeq.size()!=0) {// 还没判断完毕给定的消息序列	
//					System.out.println(messageSeq.size());
					messageName=messageSeq.get(0);
					messageType=AnalyseMessageType(messageName);//判断下这个消息的类型，以及是否合法
					ArrayList<String> placeArray = m.getPlaceList();
					for (int i = 0; i < timedAutomataStateList.size(); i++) {
						List<State> timedAutomataState = timedAutomataStateList.get(i);
						for (int j = 0; j < timedAutomataState.size(); j++) {// 根据下一状态的名字查找timedAutomataState
							State currentState = timedAutomataState.get(j);
//							System.out.println("88 "+currentState.getStateName());
//							System.out.println("$$$ "+currentState.getStateName());
//							System.out.println("$$$$$ "+placeArray.get(i));
							if (currentState.getStateName().equals(placeArray.get(i))) {// 在自动机i中找到了对应名字的state
								switch(messageType){
								case INNER_MESSAGE:
									List<String> innerList = currentState.getInnerMessageList();
//									System.out.println("99 " + innerList.size());
									for (int k = 0; k < innerList.size(); k++){// 
//										System.out.println("&&& "+innerList.get(k));
										if(innerList.get(k).equals(messageName)){
											isFind=true;
											ArrayList<String> leftMessage=(ArrayList<String>)messageSeq.clone();
											leftMessage.remove(messageName);
											placeArray.set(i, currentState.getEndStateName(innerList.get(k)));
											synchronizedMoveByMessageSeq(messageName,messageType,placeArray,i);//确保其他自动机也因这个消息而发生了移动
//											printPlaceArray(placeArray);
											examineQueue.add(new ExamineMission(innerList.get(k),(ArrayList<String>)leftMessage.clone(), (ArrayList<String>) placeArray.clone()));
											break;
										}
									}
									break;
								case NON_INNER_MESSAGE:
									List<String> sendList = currentState.getSendMessageList();
									List<String> receiveList = currentState
											.getReceiveMessageList();
//									System.out.println("991 " + sendList.size());
									for (int k = 0; k < sendList.size(); k++){
//										System.out.println("%%% "+sendList.get(k));
										if(sendList.get(k).equals(messageName))
										{
											isFind=true;
											ArrayList<String> leftMessage=(ArrayList<String>)messageSeq.clone();
											leftMessage.remove(messageName);
											placeArray.set(i, currentState.getEndStateName(sendList.get(k)));
											synchronizedMoveByMessageSeq(messageName,messageType,placeArray,i);//确保其他自动机也因这个消息而发生了移动
//											printPlaceArray(placeArray);
											examineQueue.add(new ExamineMission(sendList.get(k),(ArrayList<String>)leftMessage.clone(), (ArrayList<String>) placeArray.clone()));
											break;
										}
									}
									for (int k = 0; k < receiveList.size(); k++){
//										System.out.println("%%%% "+receiveList.get(k));
										if(receiveList.get(k).equals(messageName))
										{
											isFind=true;
											ArrayList<String> leftMessage=(ArrayList<String>)messageSeq.clone();
											leftMessage.remove(messageName);
											placeArray.set(i, currentState.getEndStateName(receiveList.get(k)));
											synchronizedMoveByMessageSeq(messageName,messageType,placeArray,i);//确保其他自动机也因这个消息而发生了移动
//											printPlaceArray(placeArray);
											examineQueue.add(new ExamineMission(receiveList.get(k),(ArrayList<String>)leftMessage.clone(), (ArrayList<String>) placeArray.clone()));
											break;
										}
									}
									break;
								}																
//								System.out.println("00 " + queue.size());
								break;//找到了，就不再循环了，因为同一个状态没有第二个存储位置
							}
						}						
					}
					if(!isFind&&(examineQueue.size()==0)){//没有找到，而且队列中也没有任务了，匹配失败，退出
						System.out.println("Not Find!");
						System.out.println("The execution time of examine the message sequence is: "+ (System.nanoTime() - currentTime) / 1000 + " us");
						return;
					}
					
				}
				else  {// 该判断的消息序列都判断完了，成功
					System.out.println("--- The Message Sequence has been found in automachine! ---");
					System.out.println("The execution time of examine the message sequence is: "+ (System.nanoTime() - currentTime) / 1000 + " us");
					return;
				}
			}
		
			System.out.println("*** The Message Sequence has been found in automachine! ***");	
			System.out.println("The execution time of examine the message sequence is: "+ (System.nanoTime() - currentTime) / 1000 + " us");
		
	}

	public static void executeVerificationByKStepsRealTimeWithMultiAutomata(// 被messageview的线程调用
			List<String> messageString) {
//		System.out.println("executeVerificationByKStepsRealTimeWithMultiAutomata 1");
//		if (kSteps == -1) {
//			return;
//		}
////		System.out.println("executeVerificationByKStepsRealTimeWithMultiAutomata 2");
//		//System.out.println("tjf tjf tjf " + timedAutomataState.size());
//		// if (timedAutomataStateList.size() == 0 &&
//		// currentProcessStateList.size() == 0) {//tjf
//		if (timedAutomataStateList.size() == 0) {
//			if (IImageKeys.automataFilesName.size() > 0) {
//				long currentTime1 = System.nanoTime();
//
//				InitilizeAutomataStateWithMultiAutomata(IImageKeys.automataFilesName);
//
//				System.out.println();
//				System.out
//						.println("The execution time of InitilizeAutomataState is: "
//								+ (System.nanoTime() - currentTime1)
//								/ 1000
//								+ " us");
//				System.out
//						.println("------------------------------------------------------------------------");
//
//			}
//			return;
//		}
//
//		long currentTime1 = System.nanoTime();
//
//		if (kSteps > 0) {
//			System.out.println();
//			System.out.println("Look ahead "
//					+ kSteps
//					+ " steps "
//					+ (withControllability != 0 ? "with" : "without")
//					+ " controllability by "
//					+ (isDepthSearchFirst != 0 ? "breadth search first."
//							: "depth search first."));
//			System.out.println();
//
//		}
//		GetMessageFromMessageSequence(messageString);
//		// AnalyzingWithoutTimeRealTime();
//		 AyalyzingWithoutTimePreLookAheadWithMultiAutomata();//tjf
//		
////		AyalyzingPreLookAheadWithMultiAutomata();
//
//		System.out.println();
//		if (kSteps > 0) {
//			// System.out.println("Look ahead " + kSteps + " steps " +
//			// (withControllability!=0?"with":"without") +
//			// " controllability by " +
//			// (isDepthSearchFirst!=0?"breadth search first.":"depth search first."));
//			System.out
//					.println("The execution time of analysis with Pre-Lookahead is: "
//							+ (System.nanoTime() - currentTime1) / 1000 + " us");
//		} else {
//			System.out
//					.println("The execution time of analysis without Pre-Lookahead is: "
//							+ (System.nanoTime() - currentTime1) / 1000 + " us");
//
//		}
//		System.out
//				.println("------------------------------------------------------------------------");
		
		

	}

	public static void executeVerificationByKStepsRealTime(
			List<String> messageString) {
		if (kSteps == -1) {
			return;
		}

		if (timedAutomataState.size() == 0 && currentProcessState == null) {
			if (IImageKeys.automataFilesName.size() > 0) {
				long currentTime1 = System.nanoTime();

				InitilizeAutomataState(IImageKeys.automataFilesName.get(0));

				System.out.println();
				System.out
						.println("The execution time of InitilizeAutomataState is: "
								+ (System.nanoTime() - currentTime1)
								/ 1000
								+ " us");
				System.out
						.println("------------------------------------------------------------------------");

			}
			return;
		}

		long currentTime1 = System.nanoTime();

		if (kSteps > 0) {
			System.out.println();
			System.out.println("Look ahead "
					+ kSteps
					+ " steps "
					+ (withControllability != 0 ? "with" : "without")
					+ " controllability by "
					+ (isDepthSearchFirst != 0 ? "breadth search first."
							: "depth search first."));
			System.out.println();

		}
		GetMessageFromMessageSequence(messageString);
		// AnalyzingWithoutTimeRealTime();
		AyalyzingWithoutTimePreLookAhead();

		System.out.println();
		if (kSteps > 0) {
			// System.out.println("Look ahead " + kSteps + " steps " +
			// (withControllability!=0?"with":"without") +
			// " controllability by " +
			// (isDepthSearchFirst!=0?"breadth search first.":"depth search first."));
			System.out
					.println("The execution time of analysis with Pre-Lookahead is: "
							+ (System.nanoTime() - currentTime1) / 1000 + " us");
		} else {
			System.out
					.println("The execution time of analysis without Pre-Lookahead is: "
							+ (System.nanoTime() - currentTime1) / 1000 + " us");

		}
		System.out
				.println("------------------------------------------------------------------------");

	}

	public static void InitilizeAutomataStateWithMultiAutomata(
			List<String> automataFilesName) {
		SetTimedAutomataSetWithMultiAutomata(automataFilesName);// 这个方法用于初始化TimedAutomataSet，它代表一个自动机文件的所有条目；timedAutomataSetList存放若干个TimedAutomataSet
		GetTimedAutomataStateWithMultiAutomata();
		//
		// System.out.println(timedAutomataState.size());
		//
		// for (int i = 0; i < timedAutomataState.size(); i++) {
		// System.out.print(timedAutomataState.get(i).getStateName() + " [");
		// for (int j = 0; j < timedAutomataState.get(i).getEndStateList()
		// .size(); j++) {
		// System.out.print(timedAutomataState.get(i).getEndStateList()
		// .get(j).getStateName()
		// + " ");
		// }
		// System.out.println("]");
		//
		// }
		//
		// for (int i = 0; i < acceptState.size(); i++) {
		// System.out.print(acceptState.get(i).getStateName() + " ");
		// }
		// System.out.println();
		//
		// for (int i = 0; i < errorState.size(); i++) {
		// System.out.print(errorState.get(i).getStateName() + " ");
		// }
		// System.out.println();

		// ComputeLookAhead();
		// ComputeControllabilityWithMultiAutomata();//tjf
	}

	public static void InitilizeAutomataState(String automataFileName) {// tiaobudao
		SetTimedAutomataSet(automataFileName);
		GetTimedAutomataState();
		//
		// System.out.println(timedAutomataState.size());
		//
		// for (int i = 0; i < timedAutomataState.size(); i++) {
		// System.out.print(timedAutomataState.get(i).getStateName() + " [");
		// for (int j = 0; j < timedAutomataState.get(i).getEndStateList()
		// .size(); j++) {
		// System.out.print(timedAutomataState.get(i).getEndStateList()
		// .get(j).getStateName()
		// + " ");
		// }
		// System.out.println("]");
		//
		// }
		//
		// for (int i = 0; i < acceptState.size(); i++) {
		// System.out.print(acceptState.get(i).getStateName() + " ");
		// }
		// System.out.println();
		//
		// for (int i = 0; i < errorState.size(); i++) {
		// System.out.print(errorState.get(i).getStateName() + " ");
		// }
		// System.out.println();

		// ComputeLookAhead();
		ComputeControllability();
	}

	public static void executeVerificationByKSteps(String messageFileName,
			String automataFileName, Integer steps) {// 调不到

		kSteps = steps;

		GetMessageFromMessageLog(messageFileName);

		// for ( int i = 0 ; i < messageLog.size(); i++)
		// {
		// System.out.println(i + " "+messageLog.get(i).getMessageFullText());
		// }

		SetTimedAutomataSet(automataFileName);

		// for ( int i = 0 ; i < timedAutomataSet.getTimedAutomata().size();i++)
		// {
		// System.out.println(timedAutomataSet.getTimedAutomata().get(i).getStartStatus()
		// + " " + timedAutomataSet.getTimedAutomata().get(i).getEndStatus());
		// }

		double propertyTime = 0;

		GetTimedAutomataState();

		// System.out.println(timedAutomataState.size());
		//
		// for ( int i = 0 ; i < timedAutomataState.size();i++)
		// {
		// System.out.print(timedAutomataState.get(i).getStateName()+ " [");
		// for( int j = 0; j <
		// timedAutomataState.get(i).getEndStateList().size(); j++)
		// {
		// System.out.print(timedAutomataState.get(i).getEndStateList().get(j).getStateName()+" ");
		// }
		// System.out.println("]");
		//
		// }

		// for( int i = 0 ; i < acceptState.size(); i++)
		// {
		// System.out.print(acceptState.get(i).getStateName() + " ");
		// }
		// System.out.println();
		//
		//
		// for( int i = 0 ; i < errorState.size(); i++)
		// {
		// System.out.print(errorState.get(i).getStateName() + " ");
		// }
		// System.out.println();

		System.out.println("Analyzing with " + steps
				+ " step Lookahead started!");
		System.out.println();
		long currentTime1 = System.nanoTime();

		ComputeLookAhead();

		// for( int i = 0 ; i < timedAutomataState.size(); i++)
		// {
		// State state = timedAutomataState.get(i);
		//
		// System.out.print(state.getStateName() + " ");
		//
		// for(int k = 0; k < state.getErrorStateInfo().size(); k++)
		// {
		// System.out.print(state.getErrorStateInfo().get(k).getStateName()+"("+state.getErrorStateSteps().get(k)+",(");
		// for( int l = state.getErrorStateRoute().get(k).size() -1; l >= 0;l--)
		// {
		// System.out.print(state.getErrorStateRoute().get(k).get(l).getStateName()+" ");
		// }
		// System.out.print("))  ");
		// }
		//
		// for(int k = 0; k < state.getAcceptStateInfo().size(); k++)
		// {
		// System.out.print(state.getAcceptStateInfo().get(k).getStateName()+"("+state.getAcceptStateSteps().get(k)+",(");
		// for( int l = state.getAcceptStateRoute().get(k).size() -1; l >=
		// 0;l--)
		// {
		// System.out.print(state.getAcceptStateRoute().get(k).get(l).getStateName()+" ");
		// }
		// System.out.print("))  ");
		// }
		// System.out.println();
		// }

		AnalyzingWithoutTime(steps);

		// Analyzing(messageLog, timedAutomataSet, timedAutomataState,
		// acceptState, errorState);

		long currentTime2 = System.nanoTime();
		propertyTime = currentTime2 - currentTime1;
		System.out.println();
		System.out
				.println("The execution time of analysis with Pre-Lookahead is: "
						+ propertyTime / 1000 + " us");
		System.out
				.println("---------------------------------------------------------------");
	}

	public static void executeVerification(String messageFileName,
			String automataFileName) {

		GetMessageFromMessageLog(messageFileName);

		// for ( int i = 0 ; i < messageLog.size(); i++)
		// {
		// System.out.println(i + " "+messageLog.get(i).getMessageFullText());
		// }

		SetTimedAutomataSet(automataFileName);

		// for ( int i = 0 ; i < timedAutomataSet.getTimedAutomata().size();i++)
		// {
		// System.out.println(timedAutomataSet.getTimedAutomata().get(i).getStartStatus()
		// + " " + timedAutomataSet.getTimedAutomata().get(i).getEndStatus());
		// }

		double propertyTime = 0;

		GetTimedAutomataState();

		// System.out.println(timedAutomataState.size());
		//
		// for ( int i = 0 ; i < timedAutomataState.size();i++)
		// {
		// System.out.print(timedAutomataState.get(i).getStateName()+ " [");
		// for( int j = 0; j <
		// timedAutomataState.get(i).getEndStateList().size(); j++)
		// {
		// System.out.print(timedAutomataState.get(i).getEndStateList().get(j).getStateName()+" ");
		// }
		// System.out.println("]");
		// }
		//
		// for( int i = 0 ; i < acceptState.size(); i++)
		// {
		// System.out.print(acceptState.get(i).getStateName() + " ");
		// }
		// System.out.println();
		//
		//
		// for( int i = 0 ; i < errorState.size(); i++)
		// {
		// System.out.print(errorState.get(i).getStateName() + " ");
		// }
		// System.out.println();
		//
		// ComputeLookAhead(timedAutomataState,acceptState,errorState);
		//
		// for( int i = 0 ; i < timedAutomataState.size(); i++)
		// {
		// State state = timedAutomataState.get(i);
		//
		// System.out.print(state.getStateName() + " ");
		//
		// for(int k = 0; k < state.getErrorStateInfo().size(); k++)
		// {
		// System.out.print(state.getErrorStateInfo().get(k).getStateName()+"("+state.getErrorStateSteps().get(k)+",(");
		// for( int l = state.getErrorStateRoute().get(k).size() -1; l >= 0;l--)
		// {
		// System.out.print(state.getErrorStateRoute().get(k).get(l).getStateName()+" ");
		// }
		// System.out.print("))  ");
		// }
		//
		// for(int k = 0; k < state.getAcceptStateInfo().size(); k++)
		// {
		// System.out.print(state.getAcceptStateInfo().get(k).getStateName()+"("+state.getAcceptStateSteps().get(k)+",(");
		// for( int l = state.getAcceptStateRoute().get(k).size() -1; l >=
		// 0;l--)
		// {
		// System.out.print(state.getAcceptStateRoute().get(k).get(l).getStateName()+" ");
		// }
		// System.out.print("))  ");
		// }
		// System.out.println();
		// }

		System.out.println("Analyze with time");
		long currentTime1 = System.nanoTime();

		AnalyzingWithTime();

		// Analyzing(messageLog, timedAutomataSet, timedAutomataState,
		// acceptState, errorState);

		long currentTime2 = System.nanoTime();
		propertyTime = currentTime2 - currentTime1;
		System.out.println();
		System.out
				.println("The execution time of analysis with time condition is: "
						+ propertyTime / 1000 + " us");
		System.out
				.println("---------------------------------------------------------------");
	}

	public static void executeVerification(String messageFileName,
			List<String> automataFilesName) {
		String automataFileName = automataFilesName.get(0);

		GetMessageFromMessageLog(messageFileName);

		// for ( int i = 0 ; i < messageLog.size(); i++)
		// {
		// System.out.println(i + " "+messageLog.get(i).getMessageFullText());
		// }

		SetTimedAutomataSet(automataFileName);

		// for ( int i = 0 ; i < timedAutomataSet.getTimedAutomata().size();i++)
		// {
		// System.out.println(timedAutomataSet.getTimedAutomata().get(i).getStartStatus()
		// + " " + timedAutomataSet.getTimedAutomata().get(i).getEndStatus());
		// }

		double propertyTime = 0;

		GetTimedAutomataState();

		// System.out.println(timedAutomataState.size());
		//
		// for ( int i = 0 ; i < timedAutomataState.size();i++)
		// {
		// System.out.print(timedAutomataState.get(i).getStateName()+ " [");
		// for( int j = 0; j <
		// timedAutomataState.get(i).getEndStateList().size(); j++)
		// {
		// System.out.print(timedAutomataState.get(i).getEndStateList().get(j).getStateName()+" ");
		// }
		// System.out.println("]");
		// }
		//
		// for( int i = 0 ; i < acceptState.size(); i++)
		// {
		// System.out.print(acceptState.get(i).getStateName() + " ");
		// }
		// System.out.println();
		//
		//
		// for( int i = 0 ; i < errorState.size(); i++)
		// {
		// System.out.print(errorState.get(i).getStateName() + " ");
		// }
		// System.out.println();
		//
		// ComputeLookAhead(timedAutomataState,acceptState,errorState);
		//
		// for( int i = 0 ; i < timedAutomataState.size(); i++)
		// {
		// State state = timedAutomataState.get(i);
		//
		// System.out.print(state.getStateName() + " ");
		//
		// for(int k = 0; k < state.getErrorStateInfo().size(); k++)
		// {
		// System.out.print(state.getErrorStateInfo().get(k).getStateName()+"("+state.getErrorStateSteps().get(k)+",(");
		// for( int l = state.getErrorStateRoute().get(k).size() -1; l >= 0;l--)
		// {
		// System.out.print(state.getErrorStateRoute().get(k).get(l).getStateName()+" ");
		// }
		// System.out.print("))  ");
		// }
		//
		// for(int k = 0; k < state.getAcceptStateInfo().size(); k++)
		// {
		// System.out.print(state.getAcceptStateInfo().get(k).getStateName()+"("+state.getAcceptStateSteps().get(k)+",(");
		// for( int l = state.getAcceptStateRoute().get(k).size() -1; l >=
		// 0;l--)
		// {
		// System.out.print(state.getAcceptStateRoute().get(k).get(l).getStateName()+" ");
		// }
		// System.out.print("))  ");
		// }
		// System.out.println();
		// }

		System.out.println("Analyze with time");
		long currentTime1 = System.nanoTime();

		AnalyzingWithTime();

		// Analyzing(messageLog, timedAutomataSet, timedAutomataState,
		// acceptState, errorState);

		long currentTime2 = System.nanoTime();
		propertyTime = currentTime2 - currentTime1;
		System.out.println();
		System.out
				.println("The execution time of analysis with time condition is: "
						+ propertyTime / 1000 + " us");
		System.out
				.println("---------------------------------------------------------------");
	}

	public static void ComputeLookAhead() {// 间接调不到
		// 首先，为error状态或accept状态添加相应的errorStateInfo或acceptStateInfo
		List<State> lastStates = new ArrayList<State>();
		List<State> currentStates = new ArrayList<State>();

		// 终止状态一般是error状态或accept状态
		for (int i = 0; i < acceptState.size(); i++) {
			acceptState.get(i).addAcceptStateInfo(acceptState.get(i));
			acceptState.get(i).addAcceptStateSteps(0);
			List<State> route = new ArrayList<State>();
			route.add(acceptState.get(i));
			acceptState.get(i).addAcceptStateRoute(route);
			lastStates.add(acceptState.get(i));
		}

		for (int i = 0; i < errorState.size(); i++) {
			errorState.get(i).addErrorStateInfo(errorState.get(i));
			errorState.get(i).addErrorStateSteps(0);
			List<State> route = new ArrayList<State>();
			route.add(errorState.get(i));
			errorState.get(i).addErrorStateRoute(route);
			lastStates.add(errorState.get(i));
		}

		while (!lastStates.isEmpty()) {
			for (int i = 0; i < timedAutomataState.size(); i++) {
				State state = timedAutomataState.get(i);
				if (!state.contain(errorState) && !state.contain(acceptState)) {
					for (int j = 0; j < state.getEndStateList().size(); j++) {
						State nextState = state.getEndStateList().get(j);
						// 第一个条件防止自循环
						if ((!state.getStateName().equals(
								nextState.getStateName()))
								&& nextState.contain(lastStates)) {
							for (int k = 0; k < nextState.getErrorStateInfo()
									.size(); k++) {
								List<State> route = new ArrayList<State>();
								List<State> lastRoute = nextState
										.getErrorStateRoute().get(k);
								for (int l = 0; l < lastRoute.size(); l++) {
									route.add(lastRoute.get(l));
								}
								route.add(state);

								if (!state.containErrorRoute(route)) {
									state.addErrorStateInfo(nextState
											.getErrorStateInfo().get(k));
									state.addErrorStateSteps(nextState
											.getErrorStateSteps().get(k) + 1);
									state.addErrorStateRoute(route);
								}
							}

							for (int k = 0; k < nextState.getAcceptStateInfo()
									.size(); k++) {
								List<State> route = new ArrayList<State>();
								List<State> lastRoute = nextState
										.getAcceptStateRoute().get(k);
								for (int l = 0; l < lastRoute.size(); l++) {
									route.add(lastRoute.get(l));
								}
								route.add(state);

								if (!state.containAcceptRoute(route)) {
									state.addAcceptStateInfo(nextState
											.getAcceptStateInfo().get(k));
									state.addAcceptStateSteps(nextState
											.getAcceptStateSteps().get(k) + 1);
									state.addAcceptStateRoute(route);
								}
							}

							currentStates.add(state);
						}

					}
				}
			}
			lastStates.clear();
			for (int i = 0; i < currentStates.size(); i++) {
				lastStates.add(currentStates.get(i));
			}
			currentStates.clear();
		}

		for (int i = 0; i < timedAutomataState.size(); i++) {
			State currentState = timedAutomataState.get(i);
			if (currentState.contain(acceptState)) {
				currentState.setControllableStatus(1);
				continue;
			}

			if (currentState.contain(errorState)) {
				currentState.setControllableStatus(0);
				continue;
			}

			for (int k = 0; k < currentState.getAcceptStateInfo().size(); k++) {
				if (currentState.getAcceptStateSteps().get(k) != 0) {
					currentState.setControllableStatus(1);
					break;
				}
			}

			if (currentState.getControllableStatus() != 1) {
				for (int k = 0; k < currentState.getErrorStateInfo().size(); k++) {
					if (currentState.getErrorStateSteps().get(k) != 0) {
						currentState.setControllableStatus(0);
						break;
					}
				}
			}
		}
	}

	public static void ComputeControllabilityWithMultiAutomata() {// 会被initial开头的那个方法调到

		for (int no = 0; no < IImageKeys.automataFilesName.size(); no++) {
			// TimedAutomataSet timedAutomataSet = timedAutomataSetList.get(no);
			List<State> timedAutomataState = timedAutomataStateList.get(no);
			List<State> acceptState = acceptStateList.get(no);
			List<State> errorState = errorStateList.get(no);

			// 首先，为error状态或accept状态添加相应的errorStateInfo或acceptStateInfo
			List<State> lastStates = new ArrayList<State>();
			List<State> currentStates = new ArrayList<State>();

			// 终止状态一般是error状态或accept状态，首先将将Accept状态作为当前状态
			// 并且Accept的ControllableStatus为1，遍历整个状态
			for (int i = 0; i < acceptState.size(); i++) {
				State state = acceptState.get(i);
				state.setControllableStatus(1);
				lastStates.add(state);
			}

			while (!lastStates.isEmpty()) {
				// 每次对所有状态进行遍历，如果下个状态是lastStates内的状态，则据此确定其ControllableStatus属性值
				for (int i = 0; i < timedAutomataState.size(); i++) {
					State state = timedAutomataState.get(i);

					// 首先排除Accept状态和Error状态，并且该状态未确定其ControllableStatus属性值
					if (!state.contain(errorState)
							&& !state.contain(acceptState)
							&& state.getControllableStatus() == -1) {

						for (int j = 0; j < state.getEndStateList().size(); j++) {
							State nextState = state.getEndStateList().get(j);

							// 第一个条件防止自循环，是可达的
							if ((!state.getStateName().equals(
									nextState.getStateName()))
									&& nextState.contain(lastStates)) {
								state.setControllableStatus(1);
								currentStates.add(state);
							}

						}
					}
				}
				lastStates.clear();
				for (int i = 0; i < currentStates.size(); i++) {
					lastStates.add(currentStates.get(i));
				}
				currentStates.clear();
			}

			// 重新初始化
			lastStates.clear();
			currentStates.clear();

			// Error的ControllableStatus为0
			for (int i = 0; i < errorState.size(); i++) {
				State state = errorState.get(i);
				state.setControllableStatus(0);
				lastStates.add(state);
			}

			while (!lastStates.isEmpty()) {
				// 每次对所有状态进行遍历，如果下个状态是lastStates内的状态，则据此确定其ControllableStatus属性值
				for (int i = 0; i < timedAutomataState.size(); i++) {
					State state = timedAutomataState.get(i);

					// 首先排除Accept状态和Error状态，并且该状态未确定其ControllableStatus属性值
					if (!state.contain(errorState)
							&& !state.contain(acceptState)
							&& state.getControllableStatus() == -1) {

						for (int j = 0; j < state.getEndStateList().size(); j++) {
							State nextState = state.getEndStateList().get(j);

							// 第一个条件防止自循环，阻止重复遍历，是可达的
							if ((!state.getStateName().equals(
									nextState.getStateName()))
									&& !(state.getControllableStatus() == 0)
									&& nextState.contain(lastStates)) {
								state.setControllableStatus(0);
								currentStates.add(state);
							}

						}
					}
				}
				lastStates.clear();
				for (int i = 0; i < currentStates.size(); i++) {
					lastStates.add(currentStates.get(i));
				}
				currentStates.clear();
			}

			for (int i = 0; i < timedAutomataState.size(); i++) {
				State state = timedAutomataState.get(i);
				if (state.getControllableStatus() == 1) {
					System.out.println(state.getStateName() + " C");
				} else if (state.getControllableStatus() == 0) {
					System.out.println(state.getStateName() + " U");
				} else {
					System.out.println(state.getStateName() + " -");
				}
			}

		}
	}

	public static void ComputeControllability() {
		// 首先，为error状态或accept状态添加相应的errorStateInfo或acceptStateInfo
		List<State> lastStates = new ArrayList<State>();
		List<State> currentStates = new ArrayList<State>();

		// 终止状态一般是error状态或accept状态，首先将将Accept状态作为当前状态
		// 并且Accept的ControllableStatus为1，遍历整个状态
		for (int i = 0; i < acceptState.size(); i++) {
			State state = acceptState.get(i);
			state.setControllableStatus(1);
			lastStates.add(state);
		}

		while (!lastStates.isEmpty()) {
			// 每次对所有状态进行遍历，如果下个状态是lastStates内的状态，则据此确定其ControllableStatus属性值
			for (int i = 0; i < timedAutomataState.size(); i++) {
				State state = timedAutomataState.get(i);

				// 首先排除Accept状态和Error状态，并且该状态未确定其ControllableStatus属性值
				if (!state.contain(errorState) && !state.contain(acceptState)
						&& state.getControllableStatus() == -1) {

					for (int j = 0; j < state.getEndStateList().size(); j++) {
						State nextState = state.getEndStateList().get(j);

						// 第一个条件防止自循环，是可达的
						if ((!state.getStateName().equals(
								nextState.getStateName()))
								&& nextState.contain(lastStates)) {
							state.setControllableStatus(1);
							currentStates.add(state);
						}

					}
				}
			}
			lastStates.clear();
			for (int i = 0; i < currentStates.size(); i++) {
				lastStates.add(currentStates.get(i));
			}
			currentStates.clear();
		}

		// 重新初始化
		lastStates.clear();
		currentStates.clear();

		// Error的ControllableStatus为0
		for (int i = 0; i < errorState.size(); i++) {
			State state = errorState.get(i);
			state.setControllableStatus(0);
			lastStates.add(state);
		}

		while (!lastStates.isEmpty()) {
			// 每次对所有状态进行遍历，如果下个状态是lastStates内的状态，则据此确定其ControllableStatus属性值
			for (int i = 0; i < timedAutomataState.size(); i++) {
				State state = timedAutomataState.get(i);

				// 首先排除Accept状态和Error状态，并且该状态未确定其ControllableStatus属性值
				if (!state.contain(errorState) && !state.contain(acceptState)
						&& state.getControllableStatus() == -1) {

					for (int j = 0; j < state.getEndStateList().size(); j++) {
						State nextState = state.getEndStateList().get(j);

						// 第一个条件防止自循环，阻止重复遍历，是可达的
						if ((!state.getStateName().equals(
								nextState.getStateName()))
								&& !(state.getControllableStatus() == 0)
								&& nextState.contain(lastStates)) {
							state.setControllableStatus(0);
							currentStates.add(state);
						}

					}
				}
			}
			lastStates.clear();
			for (int i = 0; i < currentStates.size(); i++) {
				lastStates.add(currentStates.get(i));
			}
			currentStates.clear();
		}

		for (int i = 0; i < timedAutomataState.size(); i++) {
			State state = timedAutomataState.get(i);
			if (state.getControllableStatus() == 1) {
				System.out.println(state.getStateName() + " C");
			} else if (state.getControllableStatus() == 0) {
				System.out.println(state.getStateName() + " U");
			} else {
				System.out.println(state.getStateName() + " -");
			}
		}
	}

	public static boolean LookAheadErrorState(State currentState, int steps) {
		for (int i = 0; i < currentState.getErrorStateSteps().size(); i++) {
			if (currentState.getErrorStateSteps().get(i) <= steps) {
				return false;
			}
		}
		return true;
	}

	public static boolean LookAheadAcceptState(State currentState, int steps) {
		for (int i = 0; i < currentState.getAcceptStateSteps().size(); i++) {
			if (currentState.getAcceptStateSteps().get(i) <= steps) {
				return false;
			}
		}
		return true;
	}



	public static boolean AyalyzingWithoutTimePreLookAheadWithMultiAutomata() {// 会被executeVerificationByKStepsRealTimeWithMultiAutomata调用

		List<Message> messageInstances = new ArrayList<Message>();

		for (int l = 0; l < messageLog.size(); l++) {
			Message message = messageLog.get(l);
			messageInstances.add(message);
		}

		for (int no = 0; no < IImageKeys.automataFilesName.size(); no++) {
			State currentProcessState = currentProcessStateList.get(no);
			State nextProcessState = nextProcessStateList.get(no);
			List<State> timedAutomataState = timedAutomataStateList.get(no);
			List<State> acceptState = acceptStateList.get(no);
			List<State> errorState = errorStateList.get(no);
			boolean TACreated = false;

			int indexI = 0;

			Message currentI = messageInstances.get(indexI);

			if (currentProcessState.getStateName().equals(
					timedAutomataState.get(0).getStateName())) {
				// if (kSteps > 0) {
				// System.out.println("Current Message : "
				// + currentI.getMessageOrigin());
				// if( withControllability == 0 && isDepthSearchFirst == 0)
				// {
				// LookaheadWithoutControllabilityByDepthSearch(currentProcessState);
				// }
				// else if( withControllability == 0 && isDepthSearchFirst != 0)
				// {
				// LookaheadWithoutControllabilityByBreadthSearch(currentProcessState);
				// }
				// else if( withControllability != 0 && isDepthSearchFirst == 0)
				// {
				// LookaheadWithControllabilityByDepthSearch(currentProcessState);
				// }
				// else if( withControllability != 0 && isDepthSearchFirst != 0)
				// {
				// LookaheadWithControllabilityByBreadthSearch(currentProcessState);
				// }
				// }
				outer: while (currentI != null) {
					for (int i = 0; i < currentProcessState
							.getStateTimedCondition().size(); i++) {
						if (currentI.getMessageFullText().equals(
								currentProcessState.getStateTimedCondition()
										.get(i))) {
							nextProcessState = currentProcessState
									.getEndStateList().get(i);
							if (!nextProcessState.getStateName().equals(
									currentProcessState.getStateName())) {
								// if (!nextProcessState.contain(errorState)) {
								// sequentialObservation.add(true);
								// } else {
								// sequentialObservation.add(false);
								// //errorMessage.add(currentI.getMessageOrigin());
								// }
								if (kSteps > 0) {
									System.out.println("Current Message : "
											+ currentI.getMessageOrigin());
									if (nextProcessState.contain(acceptState)) {
										System.out.println("Current state : "
												+ nextProcessState
														.getStateName()
												+ "(Accept)");
										System.out.println();
									} else if (nextProcessState
											.contain(errorState)) {
										System.out.println("Current state : "
												+ nextProcessState
														.getStateName()
												+ "(Error)");
										System.out.println();
									} else {
										if (withControllability == 0
												&& isDepthSearchFirst == 0) {
											LookaheadWithoutControllabilityByDepthSearch(nextProcessState);
										} else if (withControllability == 0
												&& isDepthSearchFirst != 0) {
											LookaheadWithoutControllabilityByBreadthSearch(nextProcessState);
										} else if (withControllability != 0
												&& isDepthSearchFirst == 0) {
											LookaheadWithControllabilityByDepthSearch(nextProcessState);
										} else if (withControllability != 0
												&& isDepthSearchFirst != 0) {
											LookaheadWithControllabilityByBreadthSearch(nextProcessState);
										}
									}
								}
							}
							break outer;
						}

					}

					if ((indexI + 1) < messageInstances.size())
						currentI = messageInstances.get(++indexI);
					else
						currentI = null;
				}
			}

			if (currentI != null && TACreated == false) {
				TACreated = true;
				currentI = messageInstances.get(++indexI);

				currentProcessState = nextProcessState;
				currentProcessStateList.set(no, currentProcessState);

				while (currentI != null) {
					outer2: for (int i = 0; i < currentProcessState
							.getStateTimedCondition().size(); i++) {

						if (currentI.getMessageFullText().equals(
								currentProcessState.getStateTimedCondition()
										.get(i))) {
							nextProcessState = currentProcessState
									.getEndStateList().get(i);
							if (!nextProcessState.getStateName().equals(
									currentProcessState.getStateName())) {
								// if (!nextProcessState.contain(errorState)) {
								// sequentialObservation.add(true);
								// } else {
								// sequentialObservation.add(false);
								// //errorMessage.add(currentI.getMessageOrigin());
								// }
								if (kSteps > 0) {
									System.out.println("Current Message : "
											+ currentI.getMessageOrigin());
									if (nextProcessState.contain(acceptState)) {
										System.out.println("Current state : "
												+ nextProcessState
														.getStateName()
												+ "(Accept)");
										System.out.println();
									} else if (nextProcessState
											.contain(errorState)) {
										System.out.println("Current state : "
												+ nextProcessState
														.getStateName()
												+ "(Error)");
										System.out.println();
									} else {
										if (withControllability == 0
												&& isDepthSearchFirst == 0) {
											LookaheadWithoutControllabilityByDepthSearch(nextProcessState);
										} else if (withControllability == 0
												&& isDepthSearchFirst != 0) {
											LookaheadWithoutControllabilityByBreadthSearch(nextProcessState);
										} else if (withControllability != 0
												&& isDepthSearchFirst == 0) {
											LookaheadWithControllabilityByDepthSearch(nextProcessState);
										} else if (withControllability != 0
												&& isDepthSearchFirst != 0) {
											LookaheadWithControllabilityByBreadthSearch(nextProcessState);
										}
									}
								}
							}
							break outer2;
						}
					}

					// if (!currentProcessState.getStateName().equals(
					// nextProcessState.getStateName())) {
					// if (kSteps > 0) {
					// LookAhead(currentProcessState);
					// }
					// }
					currentProcessState = nextProcessState;
					currentProcessStateList.set(no, currentProcessState);

					if (currentProcessState.contain(acceptState)) {
						// if (kSteps > 0) {
						// System.out.println("Current Message : "
						// + currentI.getMessageOrigin());
						// System.out.println("Current state : " +
						// currentProcessState.getStateName()
						// + "(Accept)");
						// }
						// acceptMessage.add(indexI);
						if ((indexI + 1) < messageInstances.size()) {

							currentProcessState = timedAutomataState.get(0);
							currentProcessStateList
									.set(no, currentProcessState);
							currentI = messageInstances.get(++indexI);

							if (kSteps > 0) {
								System.out.println("Current Message : "
										+ currentI.getMessageOrigin());
								if (withControllability == 0
										&& isDepthSearchFirst == 0) {
									LookaheadWithoutControllabilityByDepthSearch(currentProcessState);
								} else if (withControllability == 0
										&& isDepthSearchFirst != 0) {
									LookaheadWithoutControllabilityByBreadthSearch(currentProcessState);
								} else if (withControllability != 0
										&& isDepthSearchFirst == 0) {
									LookaheadWithControllabilityByDepthSearch(currentProcessState);
								} else if (withControllability != 0
										&& isDepthSearchFirst != 0) {
									LookaheadWithControllabilityByBreadthSearch(currentProcessState);
								}
							}

							outer3: while (currentI != null) {
								for (int k = 0; k < currentProcessState
										.getStateTimedCondition().size(); k++) {
									if (currentI.getMessageFullText().equals(
											currentProcessState
													.getStateTimedCondition()
													.get(k))) {
										nextProcessState = currentProcessState
												.getEndStateList().get(k);
										if (!nextProcessState.getStateName()
												.equals(currentProcessState
														.getStateName())) {
											// if
											// (!nextProcessState.contain(errorState))
											// {
											// sequentialObservation.add(true);
											// } else {
											// sequentialObservation.add(false);
											// errorMessage.add(currentI.getMessageOrigin());
											// }
											if (kSteps > 0) {
												System.out
														.println("Current Message : "
																+ currentI
																		.getMessageOrigin());
												if (nextProcessState
														.contain(acceptState)) {
													System.out
															.println("Current state : "
																	+ nextProcessState
																			.getStateName()
																	+ "(Accept)");
													System.out.println();
												} else if (nextProcessState
														.contain(errorState)) {
													System.out
															.println("Current state : "
																	+ nextProcessState
																			.getStateName()
																	+ "(Error)");
													System.out.println();
												} else {
													if (withControllability == 0
															&& isDepthSearchFirst == 0) {
														LookaheadWithoutControllabilityByDepthSearch(nextProcessState);
													} else if (withControllability == 0
															&& isDepthSearchFirst != 0) {
														LookaheadWithoutControllabilityByBreadthSearch(nextProcessState);
													} else if (withControllability != 0
															&& isDepthSearchFirst == 0) {
														LookaheadWithControllabilityByDepthSearch(nextProcessState);
													} else if (withControllability != 0
															&& isDepthSearchFirst != 0) {
														LookaheadWithControllabilityByBreadthSearch(nextProcessState);
													}
												}
											}
										}
										break outer3;
									}
								}
								if ((indexI + 1) < messageInstances.size())
									currentI = messageInstances.get(++indexI);
								else
									currentI = null;
							}

							if ((indexI + 1) < messageInstances.size())
								currentI = messageInstances.get(++indexI);
							else
								currentI = null;
							// currentProcessState = nextProcessState;

						} else
							currentI = null;
					} else if (currentProcessState.contain(errorState)) {
						currentProcessState = timedAutomataState.get(0);
						currentProcessStateList.set(no, currentProcessState);
						currentI = messageInstances.get(++indexI);
						if (kSteps > 0) {
							System.out.println("Current Message : "
									+ currentI.getMessageOrigin());
							if (withControllability == 0
									&& isDepthSearchFirst == 0) {
								LookaheadWithoutControllabilityByDepthSearch(currentProcessState);
							} else if (withControllability == 0
									&& isDepthSearchFirst != 0) {
								LookaheadWithoutControllabilityByBreadthSearch(currentProcessState);
							} else if (withControllability != 0
									&& isDepthSearchFirst == 0) {
								LookaheadWithControllabilityByDepthSearch(currentProcessState);
							} else if (withControllability != 0
									&& isDepthSearchFirst != 0) {
								LookaheadWithControllabilityByBreadthSearch(currentProcessState);
							}
						}

						outer3: while (currentI != null) {
							for (int k = 0; k < currentProcessState
									.getStateTimedCondition().size(); k++) {
								if (currentI.getMessageFullText().equals(
										currentProcessState
												.getStateTimedCondition()
												.get(k))) {

									nextProcessState = currentProcessState
											.getEndStateList().get(k);
									if (!nextProcessState.getStateName()
											.equals(currentProcessState
													.getStateName())) {
										// if
										// (!nextProcessState.contain(errorState))
										// {
										// sequentialObservation.add(true);
										// } else {
										// sequentialObservation.add(false);
										// errorMessage.add(currentI.getMessageOrigin());
										// }
										if (kSteps > 0) {
											System.out
													.println("Current Message : "
															+ currentI
																	.getMessageOrigin());
											if (nextProcessState
													.contain(acceptState)) {
												System.out
														.println("Current state : "
																+ nextProcessState
																		.getStateName()
																+ "(Accept)");
												System.out.println();
											} else if (nextProcessState
													.contain(errorState)) {
												System.out
														.println("Current state : "
																+ nextProcessState
																		.getStateName()
																+ "(Error)");
												System.out.println();
											} else {
												if (withControllability == 0
														&& isDepthSearchFirst == 0) {
													LookaheadWithoutControllabilityByDepthSearch(nextProcessState);
												} else if (withControllability == 0
														&& isDepthSearchFirst != 0) {
													LookaheadWithoutControllabilityByBreadthSearch(nextProcessState);
												} else if (withControllability != 0
														&& isDepthSearchFirst == 0) {
													LookaheadWithControllabilityByDepthSearch(nextProcessState);
												} else if (withControllability != 0
														&& isDepthSearchFirst != 0) {
													LookaheadWithControllabilityByBreadthSearch(nextProcessState);
												}
											}
										}
									}

									break outer3;
								}
							}
							if ((indexI + 1) < messageInstances.size())
								currentI = messageInstances.get(++indexI);
							else
								currentI = null;
						}

						if ((indexI + 1) < messageInstances.size())
							currentI = messageInstances.get(++indexI);
						else
							currentI = null;
						// currentProcessState = nextProcessState;

					} else {
						if ((indexI + 1) < messageInstances.size())
							currentI = messageInstances.get(++indexI);
						else
							currentI = null;
					}
				}
				TACreated = false;
			}

		}

		return true;
	}

	public static boolean AyalyzingWithoutTimePreLookAhead() {// 间接调不到

		List<Message> messageInstances = new ArrayList<Message>();

		for (int l = 0; l < messageLog.size(); l++) {
			Message message = messageLog.get(l);
			messageInstances.add(message);
		}

		boolean TACreated = false;

		int indexI = 0;

		Message currentI = messageInstances.get(indexI);

		if (currentProcessState.getStateName().equals(
				timedAutomataState.get(0).getStateName())) {
			// if (kSteps > 0) {
			// System.out.println("Current Message : "
			// + currentI.getMessageOrigin());
			// if( withControllability == 0 && isDepthSearchFirst == 0)
			// {
			// LookaheadWithoutControllabilityByDepthSearch(currentProcessState);
			// }
			// else if( withControllability == 0 && isDepthSearchFirst != 0)
			// {
			// LookaheadWithoutControllabilityByBreadthSearch(currentProcessState);
			// }
			// else if( withControllability != 0 && isDepthSearchFirst == 0)
			// {
			// LookaheadWithControllabilityByDepthSearch(currentProcessState);
			// }
			// else if( withControllability != 0 && isDepthSearchFirst != 0)
			// {
			// LookaheadWithControllabilityByBreadthSearch(currentProcessState);
			// }
			// }
			outer: while (currentI != null) {
				for (int i = 0; i < currentProcessState
						.getStateTimedCondition().size(); i++) {
					if (currentI.getMessageFullText()
							.equals(currentProcessState
									.getStateTimedCondition().get(i))) {
						nextProcessState = currentProcessState
								.getEndStateList().get(i);
						if (!nextProcessState.getStateName().equals(
								currentProcessState.getStateName())) {
							// if (!nextProcessState.contain(errorState)) {
							// sequentialObservation.add(true);
							// } else {
							// sequentialObservation.add(false);
							// //errorMessage.add(currentI.getMessageOrigin());
							// }
							if (kSteps > 0) {
								System.out.println("Current Message : "
										+ currentI.getMessageOrigin());
								if (nextProcessState.contain(acceptState)) {
									System.out.println("Current state : "
											+ nextProcessState.getStateName()
											+ "(Accept)");
									System.out.println();
								} else if (nextProcessState.contain(errorState)) {
									System.out.println("Current state : "
											+ nextProcessState.getStateName()
											+ "(Error)");
									System.out.println();
								} else {
									if (withControllability == 0
											&& isDepthSearchFirst == 0) {
										LookaheadWithoutControllabilityByDepthSearch(nextProcessState);
									} else if (withControllability == 0
											&& isDepthSearchFirst != 0) {
										LookaheadWithoutControllabilityByBreadthSearch(nextProcessState);
									} else if (withControllability != 0
											&& isDepthSearchFirst == 0) {
										LookaheadWithControllabilityByDepthSearch(nextProcessState);
									} else if (withControllability != 0
											&& isDepthSearchFirst != 0) {
										LookaheadWithControllabilityByBreadthSearch(nextProcessState);
									}
								}
							}
						}
						break outer;
					}

				}

				if ((indexI + 1) < messageInstances.size())
					currentI = messageInstances.get(++indexI);
				else
					currentI = null;
			}
		}

		if (currentI != null && TACreated == false) {
			TACreated = true;
			currentI = messageInstances.get(++indexI);

			currentProcessState = nextProcessState;

			while (currentI != null) {
				outer2: for (int i = 0; i < currentProcessState
						.getStateTimedCondition().size(); i++) {

					if (currentI.getMessageFullText()
							.equals(currentProcessState
									.getStateTimedCondition().get(i))) {
						nextProcessState = currentProcessState
								.getEndStateList().get(i);
						if (!nextProcessState.getStateName().equals(
								currentProcessState.getStateName())) {
							// if (!nextProcessState.contain(errorState)) {
							// sequentialObservation.add(true);
							// } else {
							// sequentialObservation.add(false);
							// //errorMessage.add(currentI.getMessageOrigin());
							// }
							if (kSteps > 0) {
								System.out.println("Current Message : "
										+ currentI.getMessageOrigin());
								if (nextProcessState.contain(acceptState)) {
									System.out.println("Current state : "
											+ nextProcessState.getStateName()
											+ "(Accept)");
									System.out.println();
								} else if (nextProcessState.contain(errorState)) {
									System.out.println("Current state : "
											+ nextProcessState.getStateName()
											+ "(Error)");
									System.out.println();
								} else {
									if (withControllability == 0
											&& isDepthSearchFirst == 0) {
										LookaheadWithoutControllabilityByDepthSearch(nextProcessState);
									} else if (withControllability == 0
											&& isDepthSearchFirst != 0) {
										LookaheadWithoutControllabilityByBreadthSearch(nextProcessState);
									} else if (withControllability != 0
											&& isDepthSearchFirst == 0) {
										LookaheadWithControllabilityByDepthSearch(nextProcessState);
									} else if (withControllability != 0
											&& isDepthSearchFirst != 0) {
										LookaheadWithControllabilityByBreadthSearch(nextProcessState);
									}
								}
							}
						}
						break outer2;
					}
				}

				// if (!currentProcessState.getStateName().equals(
				// nextProcessState.getStateName())) {
				// if (kSteps > 0) {
				// LookAhead(currentProcessState);
				// }
				// }
				currentProcessState = nextProcessState;

				if (currentProcessState.contain(acceptState)) {
					// if (kSteps > 0) {
					// System.out.println("Current Message : "
					// + currentI.getMessageOrigin());
					// System.out.println("Current state : " +
					// currentProcessState.getStateName()
					// + "(Accept)");
					// }
					// acceptMessage.add(indexI);
					if ((indexI + 1) < messageInstances.size()) {

						currentProcessState = timedAutomataState.get(0);
						currentI = messageInstances.get(++indexI);

						if (kSteps > 0) {
							System.out.println("Current Message : "
									+ currentI.getMessageOrigin());
							if (withControllability == 0
									&& isDepthSearchFirst == 0) {
								LookaheadWithoutControllabilityByDepthSearch(currentProcessState);
							} else if (withControllability == 0
									&& isDepthSearchFirst != 0) {
								LookaheadWithoutControllabilityByBreadthSearch(currentProcessState);
							} else if (withControllability != 0
									&& isDepthSearchFirst == 0) {
								LookaheadWithControllabilityByDepthSearch(currentProcessState);
							} else if (withControllability != 0
									&& isDepthSearchFirst != 0) {
								LookaheadWithControllabilityByBreadthSearch(currentProcessState);
							}
						}

						outer3: while (currentI != null) {
							for (int k = 0; k < currentProcessState
									.getStateTimedCondition().size(); k++) {
								if (currentI.getMessageFullText().equals(
										currentProcessState
												.getStateTimedCondition()
												.get(k))) {
									nextProcessState = currentProcessState
											.getEndStateList().get(k);
									if (!nextProcessState.getStateName()
											.equals(currentProcessState
													.getStateName())) {
										// if
										// (!nextProcessState.contain(errorState))
										// {
										// sequentialObservation.add(true);
										// } else {
										// sequentialObservation.add(false);
										// errorMessage.add(currentI.getMessageOrigin());
										// }
										if (kSteps > 0) {
											System.out
													.println("Current Message : "
															+ currentI
																	.getMessageOrigin());
											if (nextProcessState
													.contain(acceptState)) {
												System.out
														.println("Current state : "
																+ nextProcessState
																		.getStateName()
																+ "(Accept)");
												System.out.println();
											} else if (nextProcessState
													.contain(errorState)) {
												System.out
														.println("Current state : "
																+ nextProcessState
																		.getStateName()
																+ "(Error)");
												System.out.println();
											} else {
												if (withControllability == 0
														&& isDepthSearchFirst == 0) {
													LookaheadWithoutControllabilityByDepthSearch(nextProcessState);
												} else if (withControllability == 0
														&& isDepthSearchFirst != 0) {
													LookaheadWithoutControllabilityByBreadthSearch(nextProcessState);
												} else if (withControllability != 0
														&& isDepthSearchFirst == 0) {
													LookaheadWithControllabilityByDepthSearch(nextProcessState);
												} else if (withControllability != 0
														&& isDepthSearchFirst != 0) {
													LookaheadWithControllabilityByBreadthSearch(nextProcessState);
												}
											}
										}
									}
									break outer3;
								}
							}
							if ((indexI + 1) < messageInstances.size())
								currentI = messageInstances.get(++indexI);
							else
								currentI = null;
						}

						if ((indexI + 1) < messageInstances.size())
							currentI = messageInstances.get(++indexI);
						else
							currentI = null;
						// currentProcessState = nextProcessState;

					} else
						currentI = null;
				} else if (currentProcessState.contain(errorState)) {
					currentProcessState = timedAutomataState.get(0);
					currentI = messageInstances.get(++indexI);
					if (kSteps > 0) {
						System.out.println("Current Message : "
								+ currentI.getMessageOrigin());
						if (withControllability == 0 && isDepthSearchFirst == 0) {
							LookaheadWithoutControllabilityByDepthSearch(currentProcessState);
						} else if (withControllability == 0
								&& isDepthSearchFirst != 0) {
							LookaheadWithoutControllabilityByBreadthSearch(currentProcessState);
						} else if (withControllability != 0
								&& isDepthSearchFirst == 0) {
							LookaheadWithControllabilityByDepthSearch(currentProcessState);
						} else if (withControllability != 0
								&& isDepthSearchFirst != 0) {
							LookaheadWithControllabilityByBreadthSearch(currentProcessState);
						}
					}

					outer3: while (currentI != null) {
						for (int k = 0; k < currentProcessState
								.getStateTimedCondition().size(); k++) {
							if (currentI.getMessageFullText().equals(
									currentProcessState
											.getStateTimedCondition().get(k))) {

								nextProcessState = currentProcessState
										.getEndStateList().get(k);
								if (!nextProcessState.getStateName().equals(
										currentProcessState.getStateName())) {
									// if
									// (!nextProcessState.contain(errorState)) {
									// sequentialObservation.add(true);
									// } else {
									// sequentialObservation.add(false);
									// errorMessage.add(currentI.getMessageOrigin());
									// }
									if (kSteps > 0) {
										System.out.println("Current Message : "
												+ currentI.getMessageOrigin());
										if (nextProcessState
												.contain(acceptState)) {
											System.out
													.println("Current state : "
															+ nextProcessState
																	.getStateName()
															+ "(Accept)");
											System.out.println();
										} else if (nextProcessState
												.contain(errorState)) {
											System.out
													.println("Current state : "
															+ nextProcessState
																	.getStateName()
															+ "(Error)");
											System.out.println();
										} else {
											if (withControllability == 0
													&& isDepthSearchFirst == 0) {
												LookaheadWithoutControllabilityByDepthSearch(nextProcessState);
											} else if (withControllability == 0
													&& isDepthSearchFirst != 0) {
												LookaheadWithoutControllabilityByBreadthSearch(nextProcessState);
											} else if (withControllability != 0
													&& isDepthSearchFirst == 0) {
												LookaheadWithControllabilityByDepthSearch(nextProcessState);
											} else if (withControllability != 0
													&& isDepthSearchFirst != 0) {
												LookaheadWithControllabilityByBreadthSearch(nextProcessState);
											}
										}
									}
								}

								break outer3;
							}
						}
						if ((indexI + 1) < messageInstances.size())
							currentI = messageInstances.get(++indexI);
						else
							currentI = null;
					}

					if ((indexI + 1) < messageInstances.size())
						currentI = messageInstances.get(++indexI);
					else
						currentI = null;
					// currentProcessState = nextProcessState;

				} else {
					if ((indexI + 1) < messageInstances.size())
						currentI = messageInstances.get(++indexI);
					else
						currentI = null;
				}
			}
			TACreated = false;
		}
		return true;
	}

	private static void LookaheadWithControllabilityByBreadthSearch(State state) {
		int step = 1;
		List<State> lastState = new ArrayList<State>();
		List<State> currentState = new ArrayList<State>();
		// List<State> allState = new ArrayList<State>();
		List<List<State>> controllableRouteList = new ArrayList<List<State>>();
		List<List<State>> uncontrollableRouteList = new ArrayList<List<State>>();

		lastState.add(state);
		// allState.add(state);
		List<State> startState = new ArrayList<State>();
		startState.add(state);
		currentState.clear();
		if (state.getControllableStatus() == 0) {
			uncontrollableRouteList.add(startState);
		} else if (state.getControllableStatus() == 1) {
			controllableRouteList.add(startState);
		}

		while (step <= kSteps) {
			for (int i = 0; i < lastState.size(); i++) {
				State current = lastState.get(i);

				if (current.getControllableStatus() == 0) {// 不可控制
					// 根据定义，不可控制状态的后继状态均为不可控制
					for (int j = 0; j < current.getEndStateList().size(); j++) {
						State next = current.getEndStateList().get(j);
						// if (!next.contain(allState)) {
						// 不包含自循环和环路的可能，如果环路已经存在的，则不作删除处理
						for (int k = 0; k < uncontrollableRouteList.size(); k++) {
							List<State> stateListTemp = uncontrollableRouteList
									.get(k);
							// if( stateListTemp.size() == step)
							// {
							if (stateListTemp.size() == step
									&& stateListTemp
											.get(stateListTemp.size() - 1)
											.getStateName()
											.equals(current.getStateName())) {
								List<State> newStateList = new ArrayList<State>();
								for (int m = 0; m < stateListTemp.size(); m++) {
									newStateList.add(stateListTemp.get(m));
								}
								newStateList.add(next);
								if (!isExist(newStateList,
										uncontrollableRouteList)) {
									uncontrollableRouteList.add(newStateList);
								}
								currentState.add(next);
							}
							// }
						}
						// }
					}
				} else if (current.getControllableStatus() == 1) {// 可控制
					// 根据定义，可控制状态的后继状态可以是可控制，也可以是不可控制
					for (int j = 0; j < current.getEndStateList().size(); j++) {
						State next = current.getEndStateList().get(j);
						// 不包含自循环和环路的可能，如果环路已经存在的，则不作删除处理
						// if (!next.contain(allState)) {
						if (next.getControllableStatus() == 0) {
							// 两种情况，从不可控进入可控，从不可控进入不可控
							for (int k = 0; k < uncontrollableRouteList.size(); k++) {
								List<State> stateListTemp = uncontrollableRouteList
										.get(k);
								// if( stateListTemp.size() == step)
								// {
								if (stateListTemp.size() == step
										&& stateListTemp
												.get(stateListTemp.size() - 1)
												.getStateName()
												.equals(current.getStateName())) {
									List<State> newStateList = new ArrayList<State>();
									for (int m = 0; m < stateListTemp.size(); m++) {
										newStateList.add(stateListTemp.get(m));
									}
									newStateList.add(next);
									if (!isExist(newStateList,
											uncontrollableRouteList)) {
										uncontrollableRouteList
												.add(newStateList);
									}
									currentState.add(next);
									// allState.add(next);
								}
								// }
							}

							for (int k = 0; k < controllableRouteList.size(); k++) {
								List<State> stateListTemp = controllableRouteList
										.get(k);
								// if( stateListTemp.size() == step)
								// {
								if (stateListTemp.size() == step
										&& stateListTemp
												.get(stateListTemp.size() - 1)
												.getStateName()
												.equals(current.getStateName())) {
									List<State> newStateList = new ArrayList<State>();
									for (int m = 0; m < stateListTemp.size(); m++) {
										newStateList.add(stateListTemp.get(m));
									}
									newStateList.add(next);
									if (!isExist(newStateList,
											uncontrollableRouteList)) {
										uncontrollableRouteList
												.add(newStateList);
									}
									currentState.add(next);
									// allState.add(next);
								}
								// }
							}
						} else if (next.getControllableStatus() == 1) {
							for (int k = 0; k < controllableRouteList.size(); k++) {
								List<State> stateListTemp = controllableRouteList
										.get(k);
								// if( stateListTemp.size() == step)
								// {
								if (stateListTemp.size() == step
										&& stateListTemp
												.get(stateListTemp.size() - 1)
												.getStateName()
												.equals(current.getStateName())) {
									List<State> newStateList = new ArrayList<State>();
									for (int m = 0; m < stateListTemp.size(); m++) {
										newStateList.add(stateListTemp.get(m));
									}
									newStateList.add(next);
									if (!isExist(newStateList,
											controllableRouteList)) {
										controllableRouteList.add(newStateList);
									}
									currentState.add(next);
									// allState.add(next);
								}
								// }
							}
						}
						// }
					}
				}
			}
			step++;
			lastState.clear();
			for (int i = 0; i < currentState.size(); i++) {
				lastState.add(currentState.get(i));
			}
			currentState.clear();

			if (lastState.size() == 0) {
				break;
			}
		}

		System.out.println("Current state : " + state.getStateName());
		if (controllableRouteList.size() > 0) {
			System.out.println("Potential paths to controllability states: ");
		}
		for (int k = 0; k < controllableRouteList.size(); k++) {
			List<State> stateListTemp = controllableRouteList.get(k);
			if (stateListTemp.size() > 1) {
				System.out.print(stateListTemp.get(stateListTemp.size() - 1)
						.getStateName()
						+ "("
						+ (stateListTemp.size() - 1)
						+ ",");
				System.out.print("(");
				for (int i = 0; i < stateListTemp.size(); i++) {
					System.out.print(stateListTemp.get(i).getStateName() + " ");
				}
				System.out.println("))");
			}
		}

		if (uncontrollableRouteList.size() > 0) {
			System.out.println("Potential paths to uncontrollability states: ");
		}
		for (int k = 0; k < uncontrollableRouteList.size(); k++) {
			List<State> stateListTemp = uncontrollableRouteList.get(k);
			if (stateListTemp.size() > 1) {
				System.out.print(stateListTemp.get(stateListTemp.size() - 1)
						.getStateName()
						+ "("
						+ (stateListTemp.size() - 1)
						+ ",");
				System.out.print("(");
				for (int i = 0; i < stateListTemp.size(); i++) {
					System.out.print(stateListTemp.get(i).getStateName() + " ");
				}
				System.out.println("))");
			}
		}
		System.out.println();
	}

	private static void LookaheadWithControllabilityByDepthSearch(State state) {
		// 如果当前达状态为接受状态或错误状态，则直接输出返回
		if (state.contain(acceptState)) {
			System.out.println("Current state : " + state.getStateName()
					+ "(Accept)");
			return;
		}
		if (state.contain(errorState)) {
			System.out.println("Current state : " + state.getStateName()
					+ "(Error)");
			return;
		}

		int step = kSteps;

		List<List<State>> controllableRouteList = new ArrayList<List<State>>();
		List<List<State>> uncontrollableRouteList = new ArrayList<List<State>>();

		List<State> stateList = new ArrayList<State>();
		stateList.add(state);
		ControllabilityDepthSearch(step, state, stateList,
				controllableRouteList, uncontrollableRouteList);

		System.out.println("Current state : " + state.getStateName());
		if (controllableRouteList.size() > 0) {
			System.out.println("Potential paths to controllability states: ");
		}
		for (int k = 0; k < controllableRouteList.size(); k++) {
			List<State> stateListTemp = controllableRouteList.get(k);
			if (stateListTemp.size() > 1) {
				System.out.print(stateListTemp.get(stateListTemp.size() - 1)
						.getStateName()
						+ "("
						+ (stateListTemp.size() - 1)
						+ ",");
				System.out.print("(");
				for (int i = 0; i < stateListTemp.size(); i++) {
					System.out.print(stateListTemp.get(i).getStateName() + " ");
				}
				System.out.println("))");
			}
		}

		if (uncontrollableRouteList.size() > 0) {
			System.out.println("Potential paths to uncontrollability states: ");
		}
		for (int k = 0; k < uncontrollableRouteList.size(); k++) {
			List<State> stateListTemp = uncontrollableRouteList.get(k);
			if (stateListTemp.size() > 1) {
				System.out.print(stateListTemp.get(stateListTemp.size() - 1)
						.getStateName()
						+ "("
						+ (stateListTemp.size() - 1)
						+ ",");
				System.out.print("(");
				for (int i = 0; i < stateListTemp.size(); i++) {
					System.out.print(stateListTemp.get(i).getStateName() + " ");
				}
				System.out.println("))");
			}
		}
		System.out.println();
	}

	private static void ControllabilityDepthSearch(int step, State state,
			List<State> stateList, List<List<State>> controllableRouteList,
			List<List<State>> uncontrollableRouteList) {
		// 结束递归
		if (step == 0) {
			return;
		}

		for (int j = 0; j < state.getEndStateList().size(); j++) {
			State next = state.getEndStateList().get(j);

			// 只避免自身的循环
			if (next.getStateName().equals(state.getStateName())) {
				continue;
			}

			if (next.contain(acceptState)) {
				List<State> newStateList = new ArrayList<State>();
				for (int i = 0; i < stateList.size(); i++) {
					newStateList.add(stateList.get(i));
				}
				newStateList.add(next);

				if (!isExist(newStateList, controllableRouteList)) {
					controllableRouteList.add(newStateList);
				}
			} else if (next.contain(errorState)) {
				List<State> newStateList = new ArrayList<State>();
				for (int i = 0; i < stateList.size(); i++) {
					newStateList.add(stateList.get(i));
				}
				newStateList.add(next);

				if (!isExist(newStateList, uncontrollableRouteList)) {
					uncontrollableRouteList.add(newStateList);
				}
			} else {
				if (next.getControllableStatus() == 0) {
					List<State> newStateList = new ArrayList<State>();
					for (int i = 0; i < stateList.size(); i++) {
						newStateList.add(stateList.get(i));
					}
					newStateList.add(next);

					if (!isExist(newStateList, uncontrollableRouteList)) {
						uncontrollableRouteList.add(newStateList);
					}

					ControllabilityDepthSearch(step - 1, next, newStateList,
							controllableRouteList, uncontrollableRouteList);
				} else if (next.getControllableStatus() == 1) {
					List<State> newStateList = new ArrayList<State>();
					for (int i = 0; i < stateList.size(); i++) {
						newStateList.add(stateList.get(i));
					}
					newStateList.add(next);

					if (!isExist(newStateList, controllableRouteList)) {
						controllableRouteList.add(newStateList);
					}

					ControllabilityDepthSearch(step - 1, next, newStateList,
							controllableRouteList, uncontrollableRouteList);
				} else {
					List<State> newStateList = new ArrayList<State>();
					for (int i = 0; i < stateList.size(); i++) {
						newStateList.add(stateList.get(i));
					}
					newStateList.add(next);
					ControllabilityDepthSearch(step - 1, next, newStateList,
							controllableRouteList, uncontrollableRouteList);
				}
			}
		}

	}

	private static boolean isExist(List<State> newStateList,
			List<List<State>> routeList) {
		boolean isExist = false;
		for (int m = 0; m < routeList.size(); m++) {
			List<State> stateListTemp = routeList.get(m);
			if (stateListTemp.size() == newStateList.size()) {
				boolean isMatch = true;
				for (int n = 0; n < stateListTemp.size(); n++) {
					if (!newStateList.get(n).getStateName()
							.equals(stateListTemp.get(n).getStateName())) {
						isMatch = false;
						break;
					}
				}

				if (isMatch) {
					isExist = true;
					break;
				}
			}
		}
		return isExist;
	}

	private static void LookaheadWithoutControllabilityByBreadthSearch(
			State state) {
		// 如果当前达状态为接受状态或错误状态，则直接输出返回
		if (state.contain(acceptState)) {
			System.out.println("Current state : " + state.getStateName()
					+ "(Accept)");
			return;
		}
		if (state.contain(errorState)) {
			System.out.println("Current state : " + state.getStateName()
					+ "(Error)");
			return;
		}

		int step = 1;
		List<State> lastState = new ArrayList<State>();
		List<State> currentState = new ArrayList<State>();
		List<List<State>> lastRouteList = new ArrayList<List<State>>();
		List<List<State>> currentRouteList = new ArrayList<List<State>>();
		List<List<State>> acceptRouteList = new ArrayList<List<State>>();
		List<List<State>> errorRouteList = new ArrayList<List<State>>();

		lastState.add(state);
		List<State> startState = new ArrayList<State>();
		startState.add(state);
		currentState.clear();
		lastRouteList.add(startState);

		while (step <= kSteps) {
			for (int i = 0; i < lastState.size(); i++) {
				// 首先确保current不是终止Accept或者Error状态
				State current = lastState.get(i);

				for (int j = 0; j < current.getEndStateList().size(); j++) {
					State next = current.getEndStateList().get(j);
					// 包含自循环和环路的可能，如果环路已经存在的，则不作删除处理
					for (int k = 0; k < lastRouteList.size(); k++) {
						List<State> stateListTemp = lastRouteList.get(k);
						// if( stateListTemp.size() == step)
						// {
						if (stateListTemp.get(stateListTemp.size() - 1)
								.getStateName().equals(current.getStateName())) {
							if (next.contain(acceptState)) {
								List<State> newStateList = new ArrayList<State>();
								for (int m = 0; m < stateListTemp.size(); m++) {
									newStateList.add(stateListTemp.get(m));
								}
								newStateList.add(next);
								if (!isExist(newStateList, acceptRouteList)) {
									acceptRouteList.add(newStateList);
								}
							} else if (next.contain(errorState)) {
								List<State> newStateList = new ArrayList<State>();
								for (int m = 0; m < stateListTemp.size(); m++) {
									newStateList.add(stateListTemp.get(m));
								}
								newStateList.add(next);
								if (!isExist(newStateList, errorRouteList)) {
									errorRouteList.add(newStateList);
								}
							} else {
								List<State> newStateList = new ArrayList<State>();
								for (int m = 0; m < stateListTemp.size(); m++) {
									newStateList.add(stateListTemp.get(m));
								}
								newStateList.add(next);
								if (!isExist(newStateList, currentRouteList)) {
									currentRouteList.add(newStateList);
								}
								currentState.add(next);
							}
						}
					}
				}
			}
			step++;
			lastRouteList.clear();
			for (int i = 0; i < currentRouteList.size(); i++) {
				lastRouteList.add(currentRouteList.get(i));
			}
			currentRouteList.clear();

			lastState.clear();
			for (int i = 0; i < currentState.size(); i++) {
				lastState.add(currentState.get(i));
			}
			currentState.clear();

			if (lastState.size() == 0) {
				break;
			}
		}

		System.out.println("Current state : " + state.getStateName());
		if (acceptRouteList.size() > 0) {
			System.out.println("Potential Accepting paths: ");
		}
		for (int k = 0; k < acceptRouteList.size(); k++) {
			List<State> stateListTemp = acceptRouteList.get(k);
			if (stateListTemp.size() > 1) {
				System.out.print(stateListTemp.get(stateListTemp.size() - 1)
						.getStateName()
						+ "("
						+ (stateListTemp.size() - 1)
						+ ",");
				System.out.print("(");
				for (int i = 0; i < stateListTemp.size(); i++) {
					System.out.print(stateListTemp.get(i).getStateName() + " ");
				}
				System.out.println("))");
			}
		}

		if (errorRouteList.size() > 0) {
			System.out.println("Potential Error paths: ");
		}
		for (int k = 0; k < errorRouteList.size(); k++) {
			List<State> stateListTemp = errorRouteList.get(k);
			if (stateListTemp.size() > 1) {
				System.out.print(stateListTemp.get(stateListTemp.size() - 1)
						.getStateName()
						+ "("
						+ (stateListTemp.size() - 1)
						+ ",");
				System.out.print("(");
				for (int i = 0; i < stateListTemp.size(); i++) {
					System.out.print(stateListTemp.get(i).getStateName() + " ");
				}
				System.out.println("))");
			}
		}
		System.out.println();
	}

	private static void LookaheadWithoutControllabilityByDepthSearch(State state) {
		// 如果当前达状态为接受状态或错误状态，则直接输出返回
		if (state.contain(acceptState)) {
			System.out.println("Current state : " + state.getStateName()
					+ "(Accept)");
			return;
		}
		if (state.contain(errorState)) {
			System.out.println("Current state : " + state.getStateName()
					+ "(Error)");
			return;
		}

		int step = kSteps;

		List<List<State>> acceptRouteList = new ArrayList<List<State>>();
		List<List<State>> errorRouteList = new ArrayList<List<State>>();

		List<State> stateList = new ArrayList<State>();
		stateList.add(state);
		UncontrollabilityDepthSearch(step, state, stateList, acceptRouteList,
				errorRouteList);

		System.out.println("Current state : " + state.getStateName());
		if (acceptRouteList.size() > 0) {
			System.out.println("Potential Accepting paths: ");
		}
		for (int k = 0; k < acceptRouteList.size(); k++) {
			List<State> stateListTemp = acceptRouteList.get(k);
			if (stateListTemp.size() > 1) {
				System.out.print(stateListTemp.get(stateListTemp.size() - 1)
						.getStateName()
						+ "("
						+ (stateListTemp.size() - 1)
						+ ",");
				System.out.print("(");
				for (int i = 0; i < stateListTemp.size(); i++) {
					System.out.print(stateListTemp.get(i).getStateName() + " ");
				}
				System.out.println("))");
			}
		}

		if (errorRouteList.size() > 0) {
			System.out.println("Potential Error paths: ");
		}
		for (int k = 0; k < errorRouteList.size(); k++) {
			List<State> stateListTemp = errorRouteList.get(k);
			if (stateListTemp.size() > 1) {
				System.out.print(stateListTemp.get(stateListTemp.size() - 1)
						.getStateName()
						+ "("
						+ (stateListTemp.size() - 1)
						+ ",");
				System.out.print("(");
				for (int i = 0; i < stateListTemp.size(); i++) {
					System.out.print(stateListTemp.get(i).getStateName() + " ");
				}
				System.out.println("))");
			}
		}
		System.out.println();
	}

	private static void UncontrollabilityDepthSearch(int step, State state,
			List<State> stateList, List<List<State>> acceptRouteList,
			List<List<State>> errorRouteList) {
		// 结束递归
		if (step == 0) {
			return;
		}

		for (int j = 0; j < state.getEndStateList().size(); j++) {
			State next = state.getEndStateList().get(j);

			// 只避免自身的循环
			if (next.getStateName().equals(state.getStateName())) {
				continue;
			}

			if (next.contain(acceptState)) {
				List<State> newStateList = new ArrayList<State>();
				for (int i = 0; i < stateList.size(); i++) {
					newStateList.add(stateList.get(i));
				}
				newStateList.add(next);

				if (!isExist(newStateList, acceptRouteList)) {
					acceptRouteList.add(newStateList);
				}
			} else if (next.contain(errorState)) {
				List<State> newStateList = new ArrayList<State>();
				for (int i = 0; i < stateList.size(); i++) {
					newStateList.add(stateList.get(i));
				}
				newStateList.add(next);

				if (!isExist(newStateList, errorRouteList)) {
					errorRouteList.add(newStateList);
				}
			} else {
				List<State> newStateList = new ArrayList<State>();
				for (int i = 0; i < stateList.size(); i++) {
					newStateList.add(stateList.get(i));
				}
				newStateList.add(next);
				UncontrollabilityDepthSearch(step - 1, next, newStateList,
						acceptRouteList, errorRouteList);
			}
		}
	}

	public static boolean AnalyzingWithoutTimeRealTime() {// 调不到

		List<Message> messageInstances = new ArrayList<Message>();

		int falseIndex = 0;
		int trueIndex = 0;

		for (int l = 0; l < messageLog.size(); l++) {
			Message message = messageLog.get(l);
			messageInstances.add(message);
		}

		boolean TACreated = false;

		int indexI = 0;

		Message currentI = messageInstances.get(indexI);

		if (currentProcessState.getStateName().equals(
				timedAutomataState.get(0).getStateName())) {
			outer: while (currentI != null) {
				for (int i = 0; i < currentProcessState
						.getStateTimedCondition().size(); i++) {
					if (currentI.getMessageFullText()
							.equals(currentProcessState
									.getStateTimedCondition().get(i))) {
						nextProcessState = currentProcessState
								.getEndStateList().get(i);
						if (!nextProcessState.getStateName().equals(
								currentProcessState.getStateName())) {
							if (!nextProcessState.contain(errorState)) {
								sequentialObservation.add(true);
							} else {
								sequentialObservation.add(false);
								errorMessage.add(currentI.getMessageOrigin());
							}
						}
						if (kSteps > 0) {
							if (!LookAheadErrorState(currentProcessState,
									kSteps)
									|| !LookAheadAcceptState(
											currentProcessState, kSteps)) {
								System.out.println("currentMessage: "
										+ currentI.getMessageOrigin());
								System.out.println("currentState: "
										+ currentProcessState.getStateName());

								if (!LookAheadErrorState(currentProcessState,
										kSteps)) {
									System.out.print("Potential Error Paths: ");
									for (int k = 0; k < currentProcessState
											.getErrorStateInfo().size(); k++) {
										if (currentProcessState
												.getErrorStateSteps().get(k) <= kSteps
												&& currentProcessState
														.getErrorStateSteps()
														.get(k) != 0) {
											System.out
													.print(currentProcessState
															.getErrorStateInfo()
															.get(k)
															.getStateName()
															+ "("
															+ currentProcessState
																	.getErrorStateSteps()
																	.get(k)
															+ ",(");
											for (int l = currentProcessState
													.getErrorStateRoute()
													.get(k).size() - 1; l >= 0; l--) {
												System.out
														.print(currentProcessState
																.getErrorStateRoute()
																.get(k).get(l)
																.getStateName()
																+ " ");
											}
											System.out.print("))  ");
										}
									}
									System.out.println();
								}

								if (!LookAheadAcceptState(currentProcessState,
										kSteps)) {
									System.out
											.print("Potential Accepting paths: ");
									for (int k = 0; k < currentProcessState
											.getAcceptStateInfo().size(); k++) {
										if (currentProcessState
												.getAcceptStateSteps().get(k) <= kSteps
												&& currentProcessState
														.getAcceptStateSteps()
														.get(k) != 0) {
											System.out
													.print(currentProcessState
															.getAcceptStateInfo()
															.get(k)
															.getStateName()
															+ "("
															+ currentProcessState
																	.getAcceptStateSteps()
																	.get(k)
															+ ",(");
											for (int l = currentProcessState
													.getAcceptStateRoute()
													.get(k).size() - 1; l >= 0; l--) {
												System.out
														.print(currentProcessState
																.getAcceptStateRoute()
																.get(k).get(l)
																.getStateName()
																+ " ");
											}
											System.out.print("))  ");
										}
									}
									System.out.println();
								}
								System.out.println();
							}
						}
						break outer;
					}

				}

				if ((indexI + 1) < messageInstances.size())
					currentI = messageInstances.get(++indexI);
				else
					currentI = null;
			}
		}

		if (currentI != null && TACreated == false) {
			TACreated = true;
			currentI = messageInstances.get(++indexI);
			currentProcessState = nextProcessState;

			if (kSteps > 0) {
				if (!LookAheadErrorState(currentProcessState, kSteps)
						|| !LookAheadAcceptState(currentProcessState, kSteps)) {
					System.out.println("currentMessage: "
							+ currentI.getMessageOrigin());
					System.out.println("currentState: "
							+ currentProcessState.getStateName());
					if (!LookAheadErrorState(currentProcessState, kSteps)) {

						System.out.print("Potential Error Paths: ");
						for (int k = 0; k < currentProcessState
								.getErrorStateInfo().size(); k++) {
							if (currentProcessState.getErrorStateSteps().get(k) <= kSteps
									&& currentProcessState.getErrorStateSteps()
											.get(k) != 0) {
								System.out.print(currentProcessState
										.getErrorStateInfo().get(k)
										.getStateName()
										+ "("
										+ currentProcessState
												.getErrorStateSteps().get(k)
										+ ",(");
								for (int l = currentProcessState
										.getErrorStateRoute().get(k).size() - 1; l >= 0; l--) {
									System.out.print(currentProcessState
											.getErrorStateRoute().get(k).get(l)
											.getStateName()
											+ " ");
								}
								System.out.print("))  ");
							}
						}
						System.out.println();
					}
					if (!LookAheadAcceptState(currentProcessState, kSteps)) {

						System.out.print("Potential Accepting paths: ");
						for (int k = 0; k < currentProcessState
								.getAcceptStateInfo().size(); k++) {
							if (currentProcessState.getAcceptStateSteps()
									.get(k) <= kSteps
									&& currentProcessState
											.getAcceptStateSteps().get(k) != 0) {
								System.out.print(currentProcessState
										.getAcceptStateInfo().get(k)
										.getStateName()
										+ "("
										+ currentProcessState
												.getAcceptStateSteps().get(k)
										+ ",(");
								for (int l = currentProcessState
										.getAcceptStateRoute().get(k).size() - 1; l >= 0; l--) {
									System.out.print(currentProcessState
											.getAcceptStateRoute().get(k)
											.get(l).getStateName()
											+ " ");
								}
								System.out.print("))  ");
							}
						}
						System.out.println();
					}
					System.out.println();
				}
			}
			while (currentI != null) {
				outer2: for (int i = 0; i < currentProcessState
						.getStateTimedCondition().size(); i++) {

					if (currentI.getMessageFullText()
							.equals(currentProcessState
									.getStateTimedCondition().get(i))) {
						nextProcessState = currentProcessState
								.getEndStateList().get(i);
						if (!nextProcessState.getStateName().equals(
								currentProcessState.getStateName())) {
							if (!nextProcessState.contain(errorState)) {
								sequentialObservation.add(true);
							} else {
								sequentialObservation.add(false);
								errorMessage.add(currentI.getMessageOrigin());
							}
						}
						break outer2;
					}
				}

				currentProcessState = nextProcessState;
				if (kSteps > 0) {
					if (!LookAheadErrorState(currentProcessState, kSteps)
							|| !LookAheadAcceptState(currentProcessState,
									kSteps)) {
						System.out.println("currentMessage: "
								+ currentI.getMessageOrigin());
						System.out.println("currentState: "
								+ currentProcessState.getStateName());

						if (!LookAheadErrorState(currentProcessState, kSteps)) {

							System.out.print("Potential Error Paths: ");
							for (int k = 0; k < currentProcessState
									.getErrorStateInfo().size(); k++) {
								if (currentProcessState.getErrorStateSteps()
										.get(k) <= kSteps
										&& currentProcessState
												.getErrorStateSteps().get(k) != 0) {
									System.out.print(currentProcessState
											.getErrorStateInfo().get(k)
											.getStateName()
											+ "("
											+ currentProcessState
													.getErrorStateSteps()
													.get(k) + ",(");
									for (int l = currentProcessState
											.getErrorStateRoute().get(k).size() - 1; l >= 0; l--) {
										System.out.print(currentProcessState
												.getErrorStateRoute().get(k)
												.get(l).getStateName()
												+ " ");
									}
									System.out.print("))  ");
								}
							}
							System.out.println();
						}

						if (!LookAheadAcceptState(currentProcessState, kSteps)) {
							System.out.print("Potential Accepting paths: ");
							for (int k = 0; k < currentProcessState
									.getAcceptStateInfo().size(); k++) {
								if (currentProcessState.getAcceptStateSteps()
										.get(k) <= kSteps
										&& currentProcessState
												.getAcceptStateSteps().get(k) != 0) {
									System.out.print(currentProcessState
											.getAcceptStateInfo().get(k)
											.getStateName()
											+ "("
											+ currentProcessState
													.getAcceptStateSteps().get(
															k) + ",(");
									for (int l = currentProcessState
											.getAcceptStateRoute().get(k)
											.size() - 1; l >= 0; l--) {
										System.out.print(currentProcessState
												.getAcceptStateRoute().get(k)
												.get(l).getStateName()
												+ " ");
									}
									System.out.print("))  ");
								}
							}
							System.out.println();
						}
						System.out.println();
					}
				}

				if (currentProcessState.contain(acceptState)) {
					acceptMessage.add(indexI);
					if ((indexI + 1) < messageInstances.size()) {

						trueIndex++;
						currentProcessState = timedAutomataState.get(0);

						outer3: while (currentI != null) {
							for (int k = 0; k < currentProcessState
									.getStateTimedCondition().size(); k++) {
								if (currentI.getMessageFullText().equals(
										currentProcessState
												.getStateTimedCondition()
												.get(k))) {
									nextProcessState = currentProcessState
											.getEndStateList().get(k);

									break outer3;
								}
							}
							if ((indexI + 1) < messageInstances.size())
								currentI = messageInstances.get(++indexI);
							else
								currentI = null;
						}

						if ((indexI + 1) < messageInstances.size())
							currentI = messageInstances.get(++indexI);
						else
							currentI = null;
						// currentProcessState = nextProcessState;

					} else
						currentI = null;
				} else if (currentProcessState.contain(errorState)) {
					falseIndex++;
					currentProcessState = timedAutomataState.get(0);
					currentI = messageInstances.get(++indexI);

					outer3: while (currentI != null) {
						for (int k = 0; k < currentProcessState
								.getStateTimedCondition().size(); k++) {
							if (currentI.getMessageFullText().equals(
									currentProcessState
											.getStateTimedCondition().get(k))) {

								nextProcessState = currentProcessState
										.getEndStateList().get(k);

								break outer3;
							}
						}
						if ((indexI + 1) < messageInstances.size())
							currentI = messageInstances.get(++indexI);
						else
							currentI = null;
					}

					if ((indexI + 1) < messageInstances.size())
						currentI = messageInstances.get(++indexI);
					else
						currentI = null;
					// currentProcessState = nextProcessState;

				} else {
					if ((indexI + 1) < messageInstances.size())
						currentI = messageInstances.get(++indexI);
					else
						currentI = null;
				}
			}
			TACreated = false;
		}

		// System.out.println();
		// System.out.println("The number of accept messages is: "
		// + acceptMessage.size());
		// // for (int i = 0; i < errorMessage.size(); i++) {
		// // System.out.println(errorMessage.get(i));
		// // }
		//
		// System.out.println();
		// System.out.println("The number of error messages is: "
		// + errorMessage.size());
		// for (int i = 0; i < errorMessage.size(); i++) {
		// System.out.println(errorMessage.get(i));
		// }
		// System.out.println();
		return true;
	}

	public static boolean AnalyzingWithoutTime(int steps) {// 间接调不到

		List<Message> messageInstances = new ArrayList<Message>();

		int falseIndex = 0;
		int trueIndex = 0;

		for (int l = 0; l < messageLog.size(); l++) {
			Message message = messageLog.get(l);
			messageInstances.add(message);

		}

		boolean TACreated = false;

		int indexI = 0, indexState = 0;

		Message currentI = messageInstances.get(indexI);

		State currentState = timedAutomataState.get(indexState), nextState = null;

		List<String> errorMessage = new ArrayList<String>();
		List<Integer> acceptMessage = new ArrayList<Integer>();

		outer: while (currentI != null) {
			for (int i = 0; i < currentState.getStateTimedCondition().size(); i++) {
				if (currentI.getMessageFullText().equals(
						currentState.getStateTimedCondition().get(i))) {
					nextState = currentState.getEndStateList().get(i);
					if (!nextState.getStateName().equals(
							currentState.getStateName())) {
						if (!nextState.contain(errorState)) {
							sequentialObservation.add(true);
						} else {
							sequentialObservation.add(false);
							errorMessage.add(currentI.getMessageOrigin());
						}
					}
					if (!LookAheadErrorState(currentState, steps)) {
						System.out.println("currentMessage: "
								+ currentI.getMessageOrigin());
						System.out.println("currentState: "
								+ currentState.getStateName());

						System.out.print("Potential Error Paths: ");
						for (int k = 0; k < currentState.getErrorStateInfo()
								.size(); k++) {
							if (currentState.getErrorStateSteps().get(k) <= steps
									&& currentState.getErrorStateSteps().get(k) != 0) {
								System.out.print(currentState
										.getErrorStateInfo().get(k)
										.getStateName()
										+ "("
										+ currentState.getErrorStateSteps()
												.get(k) + ",(");
								for (int l = currentState.getErrorStateRoute()
										.get(k).size() - 1; l >= 0; l--) {
									System.out.print(currentState
											.getErrorStateRoute().get(k).get(l)
											.getStateName()
											+ " ");
								}
								System.out.print("))  ");
							}
						}
						System.out.println();

						System.out.print("Potential Accepting paths: ");
						for (int k = 0; k < currentState.getAcceptStateInfo()
								.size(); k++) {
							if (currentState.getAcceptStateSteps().get(k) <= steps
									&& currentState.getAcceptStateSteps()
											.get(k) != 0) {
								System.out.print(currentState
										.getAcceptStateInfo().get(k)
										.getStateName()
										+ "("
										+ currentState.getAcceptStateSteps()
												.get(k) + ",(");
								for (int l = currentState.getAcceptStateRoute()
										.get(k).size() - 1; l >= 0; l--) {
									System.out.print(currentState
											.getAcceptStateRoute().get(k)
											.get(l).getStateName()
											+ " ");
								}
								System.out.print("))  ");
							}
						}
						System.out.println();
						System.out.println();
					}
					break outer;
				}

			}

			if ((indexI + 1) < messageInstances.size())
				currentI = messageInstances.get(++indexI);
			else
				currentI = null;
		}

		if (currentI != null && TACreated == false) {
			TACreated = true;
			currentI = messageInstances.get(++indexI);
			currentState = nextState;

			if (!LookAheadErrorState(currentState, steps)) {
				System.out.println("currentMessage: "
						+ currentI.getMessageOrigin());
				System.out.println("currentState: "
						+ currentState.getStateName());

				System.out.print("Potential Error Paths: ");
				for (int k = 0; k < currentState.getErrorStateInfo().size(); k++) {
					if (currentState.getErrorStateSteps().get(k) <= steps
							&& currentState.getErrorStateSteps().get(k) != 0) {
						System.out.print(currentState.getErrorStateInfo()
								.get(k).getStateName()
								+ "("
								+ currentState.getErrorStateSteps().get(k)
								+ ",(");
						for (int l = currentState.getErrorStateRoute().get(k)
								.size() - 1; l >= 0; l--) {
							System.out.print(currentState.getErrorStateRoute()
									.get(k).get(l).getStateName()
									+ " ");
						}
						System.out.print("))  ");
					}
				}
				System.out.println();

				System.out.print("Potential Accepting paths: ");
				for (int k = 0; k < currentState.getAcceptStateInfo().size(); k++) {
					if (currentState.getAcceptStateSteps().get(k) <= steps
							&& currentState.getAcceptStateSteps().get(k) != 0) {
						System.out.print(currentState.getAcceptStateInfo()
								.get(k).getStateName()
								+ "("
								+ currentState.getAcceptStateSteps().get(k)
								+ ",(");
						for (int l = currentState.getAcceptStateRoute().get(k)
								.size() - 1; l >= 0; l--) {
							System.out.print(currentState.getAcceptStateRoute()
									.get(k).get(l).getStateName()
									+ " ");
						}
						System.out.print("))  ");
					}
				}
				System.out.println();
				System.out.println();
			}

			while (currentI != null) {
				outer2: for (int i = 0; i < currentState
						.getStateTimedCondition().size(); i++) {

					if (currentI.getMessageFullText().equals(
							currentState.getStateTimedCondition().get(i))) {
						nextState = currentState.getEndStateList().get(i);
						if (!nextState.getStateName().equals(
								currentState.getStateName())) {
							if (!nextState.contain(errorState)) {
								sequentialObservation.add(true);
							} else {
								sequentialObservation.add(false);
								errorMessage.add(currentI.getMessageOrigin());
							}
						}
						break outer2;
					}
				}

				currentState = nextState;
				if (!LookAheadErrorState(currentState, steps)) {
					System.out.println("currentMessage: "
							+ currentI.getMessageOrigin());
					System.out.println("currentState: "
							+ currentState.getStateName());

					System.out.print("Potential Error Paths: ");
					for (int k = 0; k < currentState.getErrorStateInfo().size(); k++) {
						if (currentState.getErrorStateSteps().get(k) <= steps
								&& currentState.getErrorStateSteps().get(k) != 0) {
							System.out.print(currentState.getErrorStateInfo()
									.get(k).getStateName()
									+ "("
									+ currentState.getErrorStateSteps().get(k)
									+ ",(");
							for (int l = currentState.getErrorStateRoute()
									.get(k).size() - 1; l >= 0; l--) {
								System.out.print(currentState
										.getErrorStateRoute().get(k).get(l)
										.getStateName()
										+ " ");
							}
							System.out.print("))  ");
						}
					}
					System.out.println();

					System.out.print("Potential Accepting paths: ");
					for (int k = 0; k < currentState.getAcceptStateInfo()
							.size(); k++) {
						if (currentState.getAcceptStateSteps().get(k) <= steps
								&& currentState.getAcceptStateSteps().get(k) != 0) {
							System.out.print(currentState.getAcceptStateInfo()
									.get(k).getStateName()
									+ "("
									+ currentState.getAcceptStateSteps().get(k)
									+ ",(");
							for (int l = currentState.getAcceptStateRoute()
									.get(k).size() - 1; l >= 0; l--) {
								System.out.print(currentState
										.getAcceptStateRoute().get(k).get(l)
										.getStateName()
										+ " ");
							}
							System.out.print("))  ");
						}
					}
					System.out.println();
					System.out.println();
				}

				if (currentState.contain(acceptState)) {
					acceptMessage.add(indexI);
					if ((indexI + 1) < messageInstances.size()) {

						trueIndex++;
						currentState = timedAutomataState.get(0);

						outer3: while (currentI != null) {
							for (int k = 0; k < currentState
									.getStateTimedCondition().size(); k++) {
								if (currentI.getMessageFullText().equals(
										currentState.getStateTimedCondition()
												.get(k))) {
									nextState = currentState.getEndStateList()
											.get(k);

									break outer3;
								}
							}
							if ((indexI + 1) < messageInstances.size())
								currentI = messageInstances.get(++indexI);
							else
								currentI = null;
						}

						if ((indexI + 1) < messageInstances.size())
							currentI = messageInstances.get(++indexI);
						else
							currentI = null;
						currentState = nextState;

					} else
						currentI = null;
				} else if (currentState.contain(errorState)) {
					falseIndex++;
					currentState = timedAutomataState.get(0);
					currentI = messageInstances.get(++indexI);

					outer3: while (currentI != null) {
						for (int k = 0; k < currentState
								.getStateTimedCondition().size(); k++) {
							if (currentI.getMessageFullText().equals(
									currentState.getStateTimedCondition()
											.get(k))) {

								nextState = currentState.getEndStateList().get(
										k);

								break outer3;
							}
						}
						if ((indexI + 1) < messageInstances.size())
							currentI = messageInstances.get(++indexI);
						else
							currentI = null;
					}

					if ((indexI + 1) < messageInstances.size())
						currentI = messageInstances.get(++indexI);
					else
						currentI = null;
					currentState = nextState;

				} else {
					if ((indexI + 1) < messageInstances.size())
						currentI = messageInstances.get(++indexI);
					else
						currentI = null;
				}
			}
			TACreated = false;
		}

		return true;
	}

	public static boolean AnalyzingWithTime() {

		List<Message> messageInstances = new ArrayList<Message>();

		List<Double> timeInstances = new ArrayList<Double>();

		// System.out.println("The Analyzer is initialized: state in s0;");

		String firstTime = "";
		if (!messageLog.isEmpty()) {
			firstTime = messageLog.get(0).getTimedCondition();
		}

		int falseIndex = 0;
		int trueIndex = 0;

		for (int l = 0; l < messageLog.size(); l++) {
			Message message = messageLog.get(l);

			message.setTime(firstTime);
			// System.out.println(message.getTime());
			messageInstances.add(message);
			timeInstances.add(message.getTime());

		}

		// for ( int i = 0 ; i < timeInstances.size(); i++)
		// {
		// System.out.println(timeInstances.get(i));
		// }

		double xTimeInitialize = 0.0;

		double yTimeInitialize = 0.0;

		boolean TACreated = false;

		int indexI = 0, indexState = 0;

		Message currentI = messageInstances.get(indexI);

		State currentState = timedAutomataState.get(indexState), nextState = null;

		@SuppressWarnings("unused")
		double currentTime = 0.0;

		errorMessage.clear();
		acceptMessage.clear();

		xTimeInitialize = timeInstances.get(0);

		outer: while (currentI != null) {
			for (int i = 0; i < currentState.getStateTimedCondition().size(); i++) {
				if (currentI.getMessageFullText().equals(
						currentState.getStateTimedCondition().get(i))) {
					nextState = currentState.getEndStateList().get(i);
					if (!nextState.getStateName().equals(
							currentState.getStateName())) {
						xTimeInitialize = timeInstances.get(indexI);
						// System.out.println(currentI.getMessageOrigin());
						if (!nextState.contain(errorState)) {
							// System.out.println("1");
							sequentialObservation.add(true);
							// System.out.println("     "
							// + "Transition found, from "
							// + currentState.getStateName() + " to "
							// + nextState.getStateName() + " at time = "
							// + (currentI.getTime() - currentTime)
							// + "second");
						} else {
							// System.out.println("0");
							sequentialObservation.add(false);
							// System.out.println("     "
							// + "Transition found, from "
							// + currentState.getStateName() + " to "
							// + nextState.getStateName()
							// + "(Error State)" + " at time = "
							// + (currentI.getTime() - currentTime)
							// + "second");
							// System.out.println("       ***Violation***;");
							errorMessage.add(currentI.getMessageOrigin());
						}
						currentTime = currentI.getTime();
					}
					break outer;
				}

			}
			if ((indexI + 1) < messageInstances.size())
				currentI = messageInstances.get(++indexI);
			else
				currentI = null;
		}

		if (currentI != null && TACreated == false) {
			TACreated = true;
			currentI = messageInstances.get(++indexI);
			currentState = nextState;
			// System.out.println(currentState.getStateName());
			while (currentI != null) {
				outer2: for (int i = 0; i < currentState
						.getStateTimedCondition().size(); i++) {

					if (currentState.getXSymbol().get(i) == 3) {
						// xTimeInitialize = timeInstances.get(indexI);
						// System.out.println("--------="+indexI);
						if (currentI.getMessageFullText().equals(
								currentState.getStateTimedCondition().get(i))) {
							nextState = currentState.getEndStateList().get(i);
							if (!nextState.getStateName().equals(
									currentState.getStateName())) {
								// System.out.println(currentI.getMessageOrigin());
								if (!nextState.contain(errorState)) {
									// System.out.println("1");
									sequentialObservation.add(true);
									// System.out
									// .println("     "
									// + "Transition found, from "
									// + currentState
									// .getStateName()
									// + " to "
									// + nextState.getStateName()
									// + " at time = "
									// + (currentI.getTime() - currentTime)
									// + "min");
								} else {
									// System.out.println("0");
									sequentialObservation.add(false);
									// System.out
									// .println("     "
									// + "Transition found, from "
									// + currentState
									// .getStateName()
									// + " to "
									// + nextState.getStateName()
									// + "(Error State)"
									// + " at time = "
									// + (currentI.getTime() - currentTime)
									// + "min");
									// System.out
									// .println("       ***Violation***;");
									errorMessage.add(currentI
											.getMessageOrigin());
								}
								currentTime = currentI.getTime();
							}

						}
						if (currentState.getYSymbol().get(i) == -1) {
							break outer2;
						}
					} else {
						if (currentState.getXSymbol().get(i) == 0
								|| currentState.getXSymbol().get(i) == 1) {
							// System.out.println((timeInstances.get(indexI) -
							// xTimeInitialize)+ " " +
							// currentState.getX().get(i));
							if ((timeInstances.get(indexI) - xTimeInitialize) <= (currentState
									.getX().get(i))) {
								if (currentState.getYesORnoList().get(i)) {
									if (currentI.getMessageFullText().equals(
											currentState
													.getStateTimedCondition()
													.get(i))) {
										nextState = currentState
												.getEndStateList().get(i);
										if (!nextState.getStateName().equals(
												currentState.getStateName())) {
											// System.out.println(currentI
											// .getMessageOrigin());
											if (!nextState.contain(errorState)) {
												// System.out.println("--------<"+indexI);
												// System.out.println("1");
												sequentialObservation.add(true);
												// System.out
												// .println("     "
												// + "Transition found, from "
												// + currentState
												// .getStateName()
												// + " to "
												// + nextState
												// .getStateName()
												// + " at time = "
												// + (currentI
												// .getTime() - currentTime)
												// + "min");
											} else {
												// System.out.println("0");
												sequentialObservation
														.add(false);
												// System.out
												// .println("     "
												// + "Transition found, from "
												// + currentState
												// .getStateName()
												// + " to "
												// + nextState
												// .getStateName()
												// + "(Error State)"
												// + " at time = "
												// + (currentI
												// .getTime() - currentTime)
												// + "min");
												// System.out
												// .println("       ***Violation***;");
												errorMessage.add(currentI
														.getMessageOrigin());
											}
											currentTime = currentI.getTime();
										}
										if (currentState.getYSymbol().get(i) == -1) {
											break outer2;
										} else if (currentState.getYSymbol()
												.get(i) == 3) {
											yTimeInitialize = timeInstances
													.get(indexI);
											break outer2;
										}
									}
								} else {
									if (!currentI.getMessageFullText().equals(
											currentState
													.getStateTimedCondition()
													.get(i))) {
										nextState = currentState
												.getEndStateList().get(i);
										if (!nextState.getStateName().equals(
												currentState.getStateName())) {
											// System.out.println(currentI
											// .getMessageOrigin());
											if (!nextState.contain(errorState)) {
												// System.out.println("1");
												sequentialObservation.add(true);
												// System.out
												// .println("     "
												// + "Transition found, from "
												// + currentState
												// .getStateName()
												// + " to "
												// + nextState
												// .getStateName()
												// + " at time = "
												// + (currentI
												// .getTime() - currentTime)
												// + "min");
											} else {
												// System.out.println("0");
												sequentialObservation.add(true);
												// System.out
												// .println("     "
												// + "Transition found, from "
												// + currentState
												// .getStateName()
												// + " to "
												// + nextState
												// .getStateName()
												// + "(Error State)"
												// + " at time = "
												// + (currentI
												// .getTime() - currentTime)
												// + "min");
												// System.out
												// .println("       ***Violation***;");
												errorMessage.add(currentI
														.getMessageOrigin());
											}
											currentTime = currentI.getTime();
										}
										if (currentState.getYSymbol().get(i) == -1) {
											break outer2;
										} else if (currentState.getYSymbol()
												.get(i) == 3) {
											yTimeInitialize = timeInstances
													.get(indexI);
											break outer2;
										}
									}
								}
							}
						} else if (currentState.getXSymbol().get(i) == 2) {
							if ((timeInstances.get(indexI) - xTimeInitialize) > (currentState
									.getX().get(i))) {
								nextState = currentState.getEndStateList().get(
										i);
								if (!nextState.getStateName().equals(
										currentState.getStateName())) {
									// System.out.println(currentI
									// .getMessageOrigin());
									if (!nextState.contain(errorState)) {
										// System.out.println("1");
										sequentialObservation.add(true);
										// System.out
										// .println("     "
										// + "Transition found, from "
										// + currentState
										// .getStateName()
										// + " to "
										// + nextState
										// .getStateName()
										// + " at time = "
										// + (currentI.getTime() - currentTime)
										// + "min");
									} else {
										// System.out.println("0");
										sequentialObservation.add(false);
										// System.out
										// .println("     "
										// + "Transition found, from "
										// + currentState
										// .getStateName()
										// + " to "
										// + nextState
										// .getStateName()
										// + "(Error State)"
										// + " at time = "
										// + (currentI.getTime() - currentTime)
										// + "min");
										// System.out
										// .println("       ***Violation***;");
										errorMessage.add(currentI
												.getMessageOrigin());
									}
									currentTime = currentI.getTime();
								}
								if (currentState.getYSymbol().get(i) == -1) {
									break outer2;
								} else if (currentState.getYSymbol().get(i) == 3) {
									yTimeInitialize = timeInstances.get(indexI);
									break outer2;
								}
							}

						}
					}

					if (currentState.getYSymbol().get(i) == 0
							|| currentState.getYSymbol().get(i) == 1) {
						if ((timeInstances.get(indexI) - yTimeInitialize) <= (currentState
								.getY().get(i))) {
							if (currentState.getYesORnoList().get(i)) {
								if (currentI.getMessageFullText().equals(
										currentState.getStateTimedCondition()
												.get(i))) {
									nextState = currentState.getEndStateList()
											.get(i);
									if (!nextState.getStateName().equals(
											currentState.getStateName())) {
										// System.out.println(currentI
										// .getMessageOrigin());
										if (!nextState.contain(errorState)) {
											// System.out.println("1");
											sequentialObservation.add(true);
											// System.out
											// .println("     "
											// + "Transition found, from "
											// + currentState
											// .getStateName()
											// + " to "
											// + nextState
											// .getStateName()
											// + " at time = "
											// + (currentI
											// .getTime() - currentTime)
											// + "min");
										} else {
											// System.out.println("0");
											sequentialObservation.add(false);
											// System.out
											// .println("     "
											// + "Transition found, from "
											// + currentState
											// .getStateName()
											// + " to "
											// + nextState
											// .getStateName()
											// + "(Error State)"
											// + " at time = "
											// + (currentI
											// .getTime() - currentTime)
											// + "min");
											// System.out
											// .println("       ***Violation***;");
											errorMessage.add(currentI
													.getMessageOrigin());
										}
										currentTime = currentI.getTime();
									}
									break outer2;
								}
							} else {
								if (!currentI.getMessageFullText().equals(
										currentState.getStateTimedCondition()
												.get(i))) {
									nextState = currentState.getEndStateList()
											.get(i);
									if (!nextState.getStateName().equals(
											currentState.getStateName())) {
										// System.out.println(currentI
										// .getMessageOrigin());
										if (!nextState.contain(errorState)) {
											// System.out.println("1");
											sequentialObservation.add(true);
											// System.out
											// .println("     "
											// + "Transition found, from "
											// + currentState
											// .getStateName()
											// + " to "
											// + nextState
											// .getStateName()
											// + " at time = "
											// + (currentI
											// .getTime() - currentTime)
											// + "min");
										} else {
											// System.out.println("0");
											sequentialObservation.add(false);
											// System.out
											// .println("     "
											// + "Transition found, from "
											// + currentState
											// .getStateName()
											// + " to "
											// + nextState
											// .getStateName()
											// + "(Error State)"
											// + " at time = "
											// + (currentI
											// .getTime() - currentTime)
											// + "min");
											// System.out
											// .println("       ***Violation***;");
											errorMessage.add(currentI
													.getMessageOrigin());
										}
										currentTime = currentI.getTime();
									}
									break outer2;
								}
							}
						}
					} else if (currentState.getYSymbol().get(i) == 2) {
						if ((timeInstances.get(indexI) - yTimeInitialize) > (currentState
								.getY().get(i))) {
							nextState = currentState.getEndStateList().get(i);
							if (!nextState.getStateName().equals(
									currentState.getStateName())) {
								// System.out.println(currentI.getMessageOrigin());
								if (!nextState.contain(errorState)) {
									// System.out.println("1");
									sequentialObservation.add(true);
									// System.out
									// .println("     "
									// + "Transition found, from "
									// + currentState
									// .getStateName()
									// + " to "
									// + nextState.getStateName()
									// + " at time = "
									// + (currentI.getTime() - currentTime)
									// + "min");
								} else {
									// System.out.println("0");
									sequentialObservation.add(false);
									// System.out
									// .println("     "
									// + "Transition found, from "
									// + currentState
									// .getStateName()
									// + " to "
									// + nextState.getStateName()
									// + "(Error State)"
									// + " at time = "
									// + (currentI.getTime() - currentTime)
									// + "min");
									// System.out
									// .println("       ***Violation***;");
									errorMessage.add(currentI
											.getMessageOrigin());
								}
								currentTime = currentI.getTime();
							}
							break outer2;

						}
					}
					// }
				}

				// System.out.println(currentI.getMessageOrigin());
				//
				// System.out.println(currentState.getStateName()+" "+nextState.getStateName());

				// for(int i = 0 ; i< errorState.size();i++){
				// System.out.println(errorState.size()+errorState.get(i).getStateName());
				// }

				currentState = nextState;

				if (currentState.contain(acceptState)) {
					// System.out.println("accept");
					acceptMessage.add(indexI);
					if ((indexI + 1) < messageInstances.size()) {

						trueIndex++;
						currentState = timedAutomataState.get(0);
						// xTimeInitialize = timeInstances.get(indexI);
						// yTimeInitialize = timeInstances.get(indexI);

						outer3: while (currentI != null) {
							for (int k = 0; k < currentState
									.getStateTimedCondition().size(); k++) {
								if (currentI.getMessageFullText().equals(
										currentState.getStateTimedCondition()
												.get(k))) {
									xTimeInitialize = timeInstances.get(indexI);
									yTimeInitialize = timeInstances.get(indexI);
									nextState = currentState.getEndStateList()
											.get(k);
									// if(!nextState.getStateName().equals(currentState.getStateName())){
									// System.out.println(currentI.getMessageOrigin());
									// System.out.println("     "+"Transition found, from "+currentState.getStateName()+" to "+(nextState.contain(errorState)?(nextState.getStateName()+"(Error State)"):nextState.getStateName())+" at time = "+(currentI.getTime()-currentTime)+"min");
									// currentTime = currentI.getTime();
									// }

									break outer3;
								}
							}
							if ((indexI + 1) < messageInstances.size())
								currentI = messageInstances.get(++indexI);
							else
								currentI = null;
						}

						if ((indexI + 1) < messageInstances.size())
							currentI = messageInstances.get(++indexI);
						else
							currentI = null;
						currentState = nextState;

					} else
						currentI = null;
				} else if (currentState.contain(errorState)) {
					// System.out.println("0");
					falseIndex++;
					currentState = timedAutomataState.get(0);
					currentI = messageInstances.get(++indexI);

					// currentState = timedAutomataState.get(0);
					// xTimeInitialize = timeInstances.get(indexI);
					// yTimeInitialize = timeInstances.get(indexI);

					outer3: while (currentI != null) {
						for (int k = 0; k < currentState
								.getStateTimedCondition().size(); k++) {
							if (currentI.getMessageFullText().equals(
									currentState.getStateTimedCondition()
											.get(k))) {
								xTimeInitialize = timeInstances.get(indexI);
								yTimeInitialize = timeInstances.get(indexI);

								nextState = currentState.getEndStateList().get(
										k);
								// if(!nextState.getStateName().equals(currentState.getStateName())){
								// System.out.println(currentI.getMessageOrigin());
								// System.out.println("     "+"Transition found, from "+currentState.getStateName()+" to "+(nextState.contain(errorState)?(nextState.getStateName()+"(Error State)"):nextState.getStateName())+" at time = "+(currentI.getTime()-currentTime)+"min");
								// currentTime = currentI.getTime();
								// }

								break outer3;
							}
						}
						if ((indexI + 1) < messageInstances.size())
							currentI = messageInstances.get(++indexI);
						else
							currentI = null;
					}

					if ((indexI + 1) < messageInstances.size())
						currentI = messageInstances.get(++indexI);
					else
						currentI = null;
					currentState = nextState;

				} else {
					if ((indexI + 1) < messageInstances.size())
						currentI = messageInstances.get(++indexI);
					else
						currentI = null;
				}
			}
			TACreated = false;
		}

		System.out.println();
		System.out.println("The number of accept messages is: "
				+ acceptMessage.size());
		// for (int i = 0; i < errorMessage.size(); i++) {
		// System.out.println(errorMessage.get(i));
		// }

		System.out.println();
		System.out.println("The number of error messages is: "
				+ errorMessage.size());
		for (int i = 0; i < errorMessage.size(); i++) {
			System.out.println(errorMessage.get(i));
		}

		return true;
	}

	public static Boolean GetTimedAutomataStateWithMultiAutomata() {// 被initial开头的方法调用
	// List<State> timedAutomataState = new ArrayList<State>();//tjf

		for (int no = 0; no < IImageKeys.automataFilesName.size(); no++) {
			TimedAutomataSet timedAutomataSet = timedAutomataSetList.get(no);
			List<State> timedAutomataState = new ArrayList<State>();
			List<State> acceptState = new ArrayList<State>();
			List<State> errorState = new ArrayList<State>();

			// 第一次，遍历开始状态
			// 遍历一个自动机文件的所有条目内容,得到一个包含所有起始状态和可接收消息的链表，即timedAutomataState
			for (int i = 0; i < timedAutomataSet.getTimedAutomata().size(); i++) { //billy：这里的size是指timeset里面总共有多少条消息链
				TimedAutomata ta = timedAutomataSet.getTimedAutomata().get(i);//billy：得到自动机文件中第一行
				String startStateString = ta.getStartStatus();
				 
				// if(i==0)
				// initStateNameList.add(ta.getStartStatus());//把每个自动机的起始状态的名称存起来，将来构建所有可能消息组合的时候用

				boolean taIntimedAutomataState = false;
				for (int j = 0; j < timedAutomataState.size(); j++) { //billy：有相同状态的分支进行这里再次加载
					if (startStateString.equals(timedAutomataState.get(j)
							.getStateName())) {
//						System.out.println(timedAutomataState.get(j).getStateName()+" 初始化2 "+ta.getAutomataMessage());
						taIntimedAutomataState = true;
						timedAutomataState.get(j).addStateTimedCondition(
								ta.getAutomataMessage(), ta.getMessageType());
						timedAutomataState.get(j)
								.addYesORnoList(ta.isYesORno());
						timedAutomataState.get(j).addX(ta.getX());
						timedAutomataState.get(j).addXSymbol(ta.getXSymbol());
						timedAutomataState.get(j).addY(ta.getY());
						timedAutomataState.get(j).addYSymbol(ta.getYSymbol());
						timedAutomataState.get(j).setEndStateMap(
								ta.getAutomataMessage(), ta.getEndStatus());// 建立hash映射
																			// tjf
					//	System.out.println("timeAutomata YESORNOLIST"+timedAutomataState.get(j).getYesORnoList());													// 20110922
					}
				}
				if (taIntimedAutomataState == false) {
					State newState = new State();
//					System.out.println(startStateString+" 初始化1 "+ta.getAutomataMessage());
					newState.setStateName(startStateString);
					newState.addStateTimedCondition(ta.getAutomataMessage(),
							ta.getMessageType());
					newState.addYesORnoList(ta.isYesORno());
					newState.addX(ta.getX());
					newState.addXSymbol(ta.getXSymbol());
					newState.addY(ta.getY());
					newState.addYSymbol(ta.getYSymbol());
					newState.setEndStateMap(ta.getAutomataMessage(),
							ta.getEndStatus());// 建立hash映射 tjf 20110922
					timedAutomataState.add(newState);
				//	System.out.println("ta YESORNOLIST"+ta.isYesORno());
				//	System.out.println("newState YESORNOLIST"+newState.getYesORnoList());
				}

			}

			// 第二次遍历终止状态
			for (int i = 0; i < timedAutomataSet.getTimedAutomata().size(); i++) {
				TimedAutomata ta = timedAutomataSet.getTimedAutomata().get(i);
				String endStateString = ta.getEndStatus();

				String otherString = null;

				State endState = null;

				if (endStateString.contains("(")) {
					otherString = endStateString.substring(
							endStateString.indexOf("(") + 1,
							endStateString.indexOf(")"));
					endStateString = endStateString.substring(0,
							endStateString.indexOf("("));
				// System.out.println(endStateString);
				} else {
					otherString = null;
				}

				boolean endInStateList = false;
				for (int j = 0; j < timedAutomataState.size(); j++) {
					if (endStateString.equals(timedAutomataState.get(j)
							.getStateName())) {
						endInStateList = true;
					}
				}

				if (endInStateList == false) {
					endState = new State();
					endState.setStateName(endStateString);
					timedAutomataState.add(endState);
					if (otherString != null) {
						if (otherString.equals("Accept")) {
							acceptState.add(endState);
						} else if (otherString.equals("Error")) {
							errorState.add(endState);
						}
					}
				}
			}

			// 添加初始状态映射终止状态的集合
			for (int i = 0; i < timedAutomataSet.getTimedAutomata().size(); i++) {
				TimedAutomata ta = timedAutomataSet.getTimedAutomata().get(i);
				String startStateString = ta.getStartStatus();

				String endStateString = ta.getEndStatus();

				if (endStateString.contains("(")) {
					endStateString = endStateString.substring(0,
							endStateString.indexOf("("));
					// System.out.println(endStateString);
				}

				State startState = null, endState = null;

				// System.out.println(timedAutomataState.size());
				for (int j = 0; j < timedAutomataState.size(); j++) {

					if (startStateString.equals(timedAutomataState.get(j)
							.getStateName())) {
						startState = timedAutomataState.get(j);
					}
					if (endStateString.equals(timedAutomataState.get(j)
							.getStateName())) {
						endState = timedAutomataState.get(j);
					}

				}
				if (startState != null && endState != null) {
					startState.addEndStateList(endState);
					// System.out.println(startState.getStateName()+"
					// "+endState.getStateName());
				}
			}

			// if (timedAutomataState.size() > 0) {
			// currentProcessStateList.add(timedAutomataState.get(0));
			// nextProcessStateList.add(currentProcessStateList.get(no));
			// }tjf
			
			
			boolean ispsc=true;
			for (int i = 0; i < timedAutomataSet.getTimedAutomata().size(); i++) { //billy：这里的size是指timeset里面总共有多少条消息链
				TimedAutomata ta = timedAutomataSet.getTimedAutomata().get(i);//billy：得到自动机文件中第一行
			    String b="Error";
		//		System.out.println("11");
				if(ta.getEndStatus().contains(b)){
					ispsc=false;
					break;
					
				}
				
			}
			if(ispsc)
			{
				timedAutomataStateList.add(timedAutomataState);
			}
			else{
				timedAutomataStateListPSC.add(timedAutomataState);
			}
			
			
              
			// = new
															// ArrayList<State>();
			// acceptStateList.add(acceptState);// = new ArrayList<State>();
			// errorStateList.add(errorState);// = new ArrayList<State>();tjf

		}
		return true;
	}

	public static Boolean GetTimedAutomataState() {
		timedAutomataState.clear();
		acceptState.clear();
		errorState.clear();

		// 第一次，遍历开始状态
		for (int i = 0; i < timedAutomataSet.getTimedAutomata().size(); i++) {
			TimedAutomata ta = timedAutomataSet.getTimedAutomata().get(i);
			String startStateString = ta.getStartStatus();

			boolean taIntimedAutomataState = false;
			for (int j = 0; j < timedAutomataState.size(); j++) {
				if (startStateString.equals(timedAutomataState.get(j)
						.getStateName())) {
					taIntimedAutomataState = true;
					timedAutomataState.get(j).addStateTimedCondition(
							ta.getAutomataMessage());
					timedAutomataState.get(j).addYesORnoList(ta.isYesORno());
					timedAutomataState.get(j).addX(ta.getX());
					timedAutomataState.get(j).addXSymbol(ta.getXSymbol());
					timedAutomataState.get(j).addY(ta.getY());
					timedAutomataState.get(j).addYSymbol(ta.getYSymbol());
				}
			}
			if (taIntimedAutomataState == false) {
				State newState = new State();
				newState.setStateName(startStateString);
				newState.addStateTimedCondition(ta.getAutomataMessage());
				newState.addYesORnoList(ta.isYesORno());
				newState.addX(ta.getX());
				newState.addXSymbol(ta.getXSymbol());
				newState.addY(ta.getY());
				newState.addYSymbol(ta.getYSymbol());
				timedAutomataState.add(newState);
			}

		}

		// 第二次遍历终止状态
		for (int i = 0; i < timedAutomataSet.getTimedAutomata().size(); i++) {
			TimedAutomata ta = timedAutomataSet.getTimedAutomata().get(i);
			String endStateString = ta.getEndStatus();

			String otherString = null;

			State endState = null;

			if (endStateString.contains("(")) {
				otherString = endStateString.substring(
						endStateString.indexOf("(") + 1,
						endStateString.indexOf(")"));
				endStateString = endStateString.substring(0,
						endStateString.indexOf("("));
				// System.out.println(endStateString);
			} else {
				otherString = null;
			}

			boolean endInStateList = false;
			for (int j = 0; j < timedAutomataState.size(); j++) {
				if (endStateString.equals(timedAutomataState.get(j)
						.getStateName())) {
					endInStateList = true;
				}
			}

			if (endInStateList == false) {
				endState = new State();
				endState.setStateName(endStateString);
				timedAutomataState.add(endState);
				if (otherString != null) {
					if (otherString.equals("Accept")) {
						acceptState.add(endState);
					} else if (otherString.equals("Error")) {
						errorState.add(endState);
					}
				}
			}
		}

		// 添加初始状态映射终止状态的集合
		for (int i = 0; i < timedAutomataSet.getTimedAutomata().size(); i++) {
			TimedAutomata ta = timedAutomataSet.getTimedAutomata().get(i);
			String startStateString = ta.getStartStatus();

			String endStateString = ta.getEndStatus();

			if (endStateString.contains("(")) {
				endStateString = endStateString.substring(0,
						endStateString.indexOf("("));
				// System.out.println(endStateString);
			}

			State startState = null, endState = null;

			// System.out.println(timedAutomataState.size());
			for (int j = 0; j < timedAutomataState.size(); j++) {

				if (startStateString.equals(timedAutomataState.get(j)
						.getStateName())) {
					startState = timedAutomataState.get(j);
				}
				if (endStateString.equals(timedAutomataState.get(j)
						.getStateName())) {
					endState = timedAutomataState.get(j);
				}

			}
			if (startState != null && endState != null) {
				startState.addEndStateList(endState);
				// System.out.println(startState.getStateName()+"
				// "+endState.getStateName());
			}
		}

		if (timedAutomataState.size() > 0) {
			currentProcessState = timedAutomataState.get(0);
			nextProcessState = currentProcessState;
		}

		return true;
	}

	public static Boolean SetTimedAutomataSetWithMultiAutomata(
			List<String> timedAutomataFilesName) {// 由initial开头的方法调用
		// TODO Auto-generated method stub
       
		for (int no = 0; no < timedAutomataFilesName.size(); no++) {
		
			TimedAutomataSet timedAutomataSet = new TimedAutomataSet();
			
			try {
				BufferedReader timedAutomataReader = new BufferedReader(
						new FileReader(timedAutomataFilesName.get(no)));

				String s = new String();
				TimedAutomata timedAutomata = null;

				while ((s = timedAutomataReader.readLine()) != null) {
					String startStatus = s.substring(s.indexOf("(") + 1,
							s.indexOf(","));// 开始状态
					s = s.substring(s.indexOf(",") + 1);
					String automataMessage = s.substring(0, s.indexOf(","));// 消息名

					boolean yesORno = false;
					if (automataMessage.startsWith("!")) {// 判断yesorno
						yesORno = false;
						automataMessage = automataMessage.substring(1);
					} else {
						yesORno = true;
					}    
                    
					// 以下由tjf添加，判断消息名的后面有无表示发送、接收的符号
					int messageType = 0;
					if (automataMessage.endsWith("!")) {
						messageType = 1;
						automataMessage = automataMessage.substring(0,
								automataMessage.length() - 1);
//						System.out.println("init1 "+messageType+"  "+automataMessage);
					} else if (automataMessage.endsWith("?")) {
						messageType = 2;
						automataMessage = automataMessage.substring(0,
								automataMessage.length() - 1);
//						System.out.println("init2 "+messageType+"  "+automataMessage);
					}
					// 以上由tjf添加，判断消息名的后面有无表示发送、接收的符号

					s = s.substring(s.indexOf(",") + 1);

					String timedCondition = s.substring(s.indexOf("(") + 1,
							s.indexOf(")"));// 时间条件

					String endStatus = s.substring(s.indexOf(",") + 1,
							s.lastIndexOf(")"));// 结束状态
					
				
					

					timedAutomata = new TimedAutomata(startStatus, yesORno,
							automataMessage, timedCondition, endStatus,
							messageType);
            //        System.out.println("timeAutomata"+timedAutomata.yesORno);
					if (timedCondition.contains("x")) {
						int xIndex = timedCondition.indexOf("x");
						String symbol = timedCondition.substring(xIndex + 1,
								xIndex + 2);
						if (symbol.equals("<")) {
							timedAutomata.setXSymbol(0);
						} else if (symbol.equals("=")) {
							timedAutomata.setXSymbol(1);
						} else if (symbol.equals(">")) {
							timedAutomata.setXSymbol(2);
						} else if (symbol.equals(":")) {
							timedAutomata.setXSymbol(3);
						}

						int xIntStart = xIndex, xIntEnd;
						while (!Character.isDigit(timedCondition
								.charAt(xIntStart))) {
							xIntStart++;
						}

						int xInt = xIntStart;
						while (xInt < timedCondition.length()
								&& Character.isDigit(timedCondition
										.charAt(xInt))) {
							xInt++;
						}
						xIntEnd = xInt;

						timedAutomata.setX(Integer.parseInt(timedCondition
								.substring(xIntStart, xIntEnd)));

					}

					if (timedCondition.contains("y")) {
						int yIndex = timedCondition.indexOf("y");
						String symbol = timedCondition.substring(yIndex + 1,
								yIndex + 2);
						if (symbol.equals("<")) {
							timedAutomata.setYSymbol(0);
						} else if (symbol.equals("=")) {
							timedAutomata.setYSymbol(1);
						} else if (symbol.equals(">")) {
							timedAutomata.setYSymbol(2);
						} else if (symbol.equals(":")) {
							timedAutomata.setYSymbol(3);
						}

						int yIntStart = yIndex + 2, yIntEnd;
						while (!Character.isDigit(timedCondition
								.charAt(yIntStart))) {
							yIntStart++;
						}

						int yInt = yIntStart;
						while (yInt < timedCondition.length()
								&& Character.isDigit(timedCondition
										.charAt(yInt))) {
							yInt++;
						}
						yIntEnd = yInt;
						timedAutomata.setY(Integer.parseInt(timedCondition
								.substring(yIntStart, yIntEnd)));
					}

					timedAutomataSet.addTimedAutomata(timedAutomata);// 一个set对应一个自动机文件,一个timedAutomata表示自动机文件的一个条目
				}
				timedAutomataReader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			timedAutomataSetList.add(timedAutomataSet);
		}
		return true;
	}

	public static Boolean SetTimedAutomataSet(String timedAutomataFileName) {
		// TODO Auto-generated method stub

		timedAutomataSet.clear();
		try {
			BufferedReader timedAutomataReader = new BufferedReader(
					new FileReader(timedAutomataFileName));

			String s = new String();
			TimedAutomata timedAutomata = null;

			while ((s = timedAutomataReader.readLine()) != null) {
				String startStatus = s.substring(s.indexOf("(") + 1,
						s.indexOf(","));
				s = s.substring(s.indexOf(",") + 1);
				String automataMessage = s.substring(0, s.indexOf(","));

				boolean yesORno = false;
				if (automataMessage.startsWith("!")) {
					yesORno = false;
					automataMessage = automataMessage.substring(1);
				} else {
					yesORno = true;
				}
				s = s.substring(s.indexOf(",") + 1);

				String timedCondition = s.substring(s.indexOf("(") + 1,
						s.indexOf(")"));

				String endStatus = s.substring(s.indexOf(",") + 1,
						s.lastIndexOf(")"));

				timedAutomata = new TimedAutomata(startStatus, yesORno,
						automataMessage, timedCondition, endStatus);

				if (timedCondition.contains("x")) {
					int xIndex = timedCondition.indexOf("x");
					String symbol = timedCondition.substring(xIndex + 1,
							xIndex + 2);
					if (symbol.equals("<")) {
						timedAutomata.setXSymbol(0);
					} else if (symbol.equals("=")) {
						timedAutomata.setXSymbol(1);
					} else if (symbol.equals(">")) {
						timedAutomata.setXSymbol(2);
					} else if (symbol.equals(":")) {
						timedAutomata.setXSymbol(3);
					}

					int xIntStart = xIndex, xIntEnd;
					while (!Character.isDigit(timedCondition.charAt(xIntStart))) {
						xIntStart++;
					}

					int xInt = xIntStart;
					while (xInt < timedCondition.length()
							&& Character.isDigit(timedCondition.charAt(xInt))) {
						xInt++;
					}
					xIntEnd = xInt;

					timedAutomata.setX(Integer.parseInt(timedCondition
							.substring(xIntStart, xIntEnd)));

				}

				if (timedCondition.contains("y")) {
					int yIndex = timedCondition.indexOf("y");
					String symbol = timedCondition.substring(yIndex + 1,
							yIndex + 2);
					if (symbol.equals("<")) {
						timedAutomata.setYSymbol(0);
					} else if (symbol.equals("=")) {
						timedAutomata.setYSymbol(1);
					} else if (symbol.equals(">")) {
						timedAutomata.setYSymbol(2);
					} else if (symbol.equals(":")) {
						timedAutomata.setYSymbol(3);
					}

					int yIntStart = yIndex + 2, yIntEnd;
					while (!Character.isDigit(timedCondition.charAt(yIntStart))) {
						yIntStart++;
					}

					int yInt = yIntStart;
					while (yInt < timedCondition.length()
							&& Character.isDigit(timedCondition.charAt(yInt))) {
						yInt++;
					}
					yIntEnd = yInt;
					timedAutomata.setY(Integer.parseInt(timedCondition
							.substring(yIntStart, yIntEnd)));
				}

				timedAutomataSet.addTimedAutomata(timedAutomata);
			}
			timedAutomataReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public static Boolean GetMessageFromMessageLog(String TimedMessagesFileName) {// 间接调不到
		// TODO Auto-generated method stub

		try {
			BufferedReader messageReader = new BufferedReader(new FileReader(
					TimedMessagesFileName));

			String s = new String();

			while ((s = messageReader.readLine()) != null) {
				if (s.contains(":") && s.contains("[") && s.contains("]")
						&& s.contains("-") && s.contains("(")
						&& s.contains(")")) {
					String messageTimedCondition = s.substring(0,
							s.indexOf("[") - 1);

					// String messageText = s.substring(s.indexOf("["));

					String type = s.substring(s.indexOf("[") + 1,
							s.indexOf("]"));

					String messageFunction = s.substring(s.indexOf("]") + 1,
							s.indexOf("("));
					while (messageFunction.startsWith(" ")) {
						messageFunction = messageFunction.substring(1);
					}

					String parameter = s.substring(s.indexOf("(") + 1,
							s.indexOf(")"));

					String returnValue = "";
					if (s.contains("):"))
						returnValue = s.substring(s.lastIndexOf("):") + 2);
					Message m = new Message(messageTimedCondition, type,
							messageFunction, parameter, returnValue);
					m.setMessageOrigin(s);
					messageLog.add(m);
				}
			}

			messageReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public static Boolean GetMessageFromMessageSequence(
			List<String> TimedMessages) {
		// TODO Auto-generated method stub
		if (TimedMessages.size() == 0) {
			return true;
		}

		messageLog.clear();

		for (int j = 0; j < TimedMessages.size(); j++) {
			String s = TimedMessages.get(j);

			if (s.contains(":") && s.contains("[") && s.contains("]")
					&& s.contains("-") && s.contains("(") && s.contains(")")) {
				String messageTimedCondition = s.substring(0,
						s.indexOf("[") - 1);

				// String messageText = s.substring(s.indexOf("["));

				String type = s.substring(s.indexOf("[") + 1, s.indexOf("]"));

				String messageFunction = s.substring(s.indexOf("]") + 1,
						s.indexOf("("));
				while (messageFunction.startsWith(" ")) {
					messageFunction = messageFunction.substring(1);
				}

				String parameter = s.substring(s.indexOf("(") + 1,
						s.indexOf(")"));

				String returnValue = "";
				if (s.contains("):"))
					returnValue = s.substring(s.lastIndexOf("):") + 2);
				Message m = new Message(messageTimedCondition, type,
						messageFunction, parameter, returnValue);
				m.setMessageOrigin(s);
				messageLog.add(m);
			}
		}

		return true;
	}

}