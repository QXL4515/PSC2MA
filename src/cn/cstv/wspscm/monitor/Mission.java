package cn.cstv.wspscm.monitor;

import java.util.ArrayList;
import java.util.StringTokenizer;

//这个类代表了由近及远的查找过程中，可能的同步消息组合的某一个任务
public class Mission {
	private String previousMessage;
	private String nextStateName1;
	private String nextStateName2;
	private int steps;
	private int no1,no2;	
	private ArrayList<String> list;//list是一个记录在各个自动机中查找记录的链表，存的是状态名
	private String firstMessageName;
	
//	public Mission(StringBuffer previousMessage,int no1,String nextStateName1,int no2,String nextStateName2,int steps){
//		this.previousMessage=previousMessage;
//		this.no1=no1;
//		this.no2=no2;
//		this.nextStateName1=nextStateName1;
//		this.nextStateName2=nextStateName2;
//	}
	
	public Mission(String previousMessage,ArrayList<String> list,int steps){
		this.previousMessage=previousMessage;
		this.list=list;
		this.steps=steps;
		firstMessageName=findFirstName(previousMessage);
	}	
	
	public MissionForRefactor toReconstrcut(){
		return new MissionForRefactor(previousMessage,list);
	}

	public ArrayList<String> getPlaceList() {
		return list;
	}
	
	public void setPreviousMessage(String s){
		previousMessage=s;
		firstMessageName=findFirstName(s);
	}

	public String getPreviousMessage() {
		return previousMessage;
	}

	public String getNextState1() {
		return nextStateName1;
	}
	
	public String getNextState2() {
		return nextStateName2;
	}
	
	

	public int getNo1() {
		return no1;
	}

	public int getNo2() {
		return no2;
	}

	public int getSteps() {
		return steps;
	}
	
	private String findFirstName(String messageSequence){
		StringTokenizer token=new StringTokenizer(messageSequence," , "); 
		return token.nextToken();
	}
	
	public String getFirstMessageName(){
		return firstMessageName;
	}
	
	public String getMessageSequenceWithoutFirstMessageName() {
		if(previousMessage.indexOf(',')!=-1)
			return previousMessage.substring(previousMessage.indexOf(',')+2);//因为‘，’后面还有一个空格
		else 
			return "";
	}
}
