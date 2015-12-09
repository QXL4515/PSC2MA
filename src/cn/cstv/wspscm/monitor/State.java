package cn.cstv.wspscm.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class State {

	private String StateName;

	private List<String> StateTimedCondition = new ArrayList<String>();

	private List<Integer> x = new ArrayList<Integer>(); // 消息条件中的x值得范围

	private List<Integer> xSymbol = new ArrayList<Integer>(); // 0表示<, 1表示=,
																// 2表示>, 3表示:=

	private List<Integer> y = new ArrayList<Integer>(); // 消息条件中的y值得范围

	private List<Integer> ySymbol = new ArrayList<Integer>(); // 0表示<, 1表示=,
																// 2表示>, 3表示:=

	private List<Boolean> yesORnoList = new ArrayList<Boolean>();

	private List<State> endStateList = new ArrayList<State>();
	private HashMap<String,String> map=new HashMap<String,String>();//建立消息名和结束状态的映射表	tjf20110922

	private List<State> errorStateInfo = new ArrayList<State>();
	private List<Integer> errorStateSteps = new ArrayList<Integer>();
	private List<List<State>> errorStateRoute = new ArrayList<List<State>>();

	private List<State> acceptStateInfo = new ArrayList<State>();
	private List<Integer> acceptStateSteps = new ArrayList<Integer>();
	private List<List<State>> acceptStateRoute = new ArrayList<List<State>>();

	private Integer controllableStatus = -1; // -1表示不确定，0表示不可控制，1表示可控
	
	private List<String> innerMessageList=new ArrayList<String>();//内部消息链表
	private List<String> sendMessageList=new ArrayList<String>();//发送消息链表
	private List<String> receiveMessageList=new ArrayList<String>();//接收消息链表

	public String getEndStateName(String key){//tjf 20110922
		return map.get(key); //返回map指定键  映射的值
	}
	
	public void setEndStateMap(String messageName,String stateName){//tjf 20110922
		map.put(messageName,stateName);
	}

	public void setErrorStateInfo(List<State> errorStateInfo) {
		this.errorStateInfo = errorStateInfo;
	}

	public void addErrorStateInfo(State errorState) {
		this.errorStateInfo.add(errorState);
	}

	public List<State> getErrorStateInfo() {
		return errorStateInfo;
	}

	public void setErrorStateSteps(List<Integer> errorStateSteps) {
		this.errorStateSteps = errorStateSteps;
	}

	public void addErrorStateSteps(Integer steps) {
		this.errorStateSteps.add(steps);
	}

	public List<Integer> getErrorStateSteps() {
		return errorStateSteps;
	}

	public List<State> getAcceptStateInfo() {
		return acceptStateInfo;
	}

	public void setAcceptStateInfo(List<State> acceptStateInfo) {
		this.acceptStateInfo = acceptStateInfo;
	}

	public void addAcceptStateInfo(State acceptState) {
		this.acceptStateInfo.add(acceptState);
	}

	public List<Integer> getAcceptStateSteps() {
		return acceptStateSteps;
	}

	public void setAcceptStateSteps(List<Integer> acceptStateSteps) {
		this.acceptStateSteps = acceptStateSteps;
	}

	public void addAcceptStateSteps(Integer steps) {
		this.acceptStateSteps.add(steps);
	}

	public String getStateName() {
		return StateName;
	}

	public void setStateName(String stateName) {
		StateName = stateName;
	}

	public List<String> getStateTimedCondition() {
		return StateTimedCondition;
	}

	public void setStateTimedCondition(List<String> stateTimedCondition) {
		StateTimedCondition = stateTimedCondition;
	}

	public void addStateTimedCondition(String timedCondition) {
		StateTimedCondition.add(timedCondition);
	}
	
	public void addStateTimedCondition(String timedCondition,int messageType) {//tjf 20110922 
		switch(messageType){
		case 0://内部消息
			this.innerMessageList.add(timedCondition);
			break;
		case 1://发送消息
			this.sendMessageList.add(timedCondition);
			break;
		case 2://接收消息
			this.receiveMessageList.add(timedCondition);
			break;			
		}
	}
	

	public List<String> getInnerMessageList() {
		return innerMessageList;
	}

	public List<String> getSendMessageList() {
		return sendMessageList;
	}

	public List<String> getReceiveMessageList() {
		return receiveMessageList;
	}

	public List<State> getEndStateList() {
		return endStateList;
	}

	public void setEndStateList(List<State> endState) {
		this.endStateList = endState;
	}

	public void addEndStateList(State endState) {
		this.endStateList.add(endState);
	}

	public boolean contain(List<State> stateList) {
		for (int i = 0; i < stateList.size(); i++) {
			if (this.getStateName().equals(stateList.get(i).getStateName()))
				return true;
		}
		return false;
	}

	public boolean containErrorRoute(List<State> stateList) {
		int size = stateList.size();
		for (int i = 0; i < errorStateRoute.size(); i++) {
			if (errorStateRoute.get(i).size() == size) {
				boolean isTheSame = true;
				for (int j = 0; j < size; j++) {
					if (!errorStateRoute.get(i).get(j).getStateName().equals(
							stateList.get(j).getStateName())) {
						isTheSame = false;
						break;
					}
				}
				if (isTheSame) {
					return true;
				}

			}
		}
		return false;
	}

	public boolean containAcceptRoute(List<State> stateList) {
		int size = stateList.size();
		for (int i = 0; i < acceptStateRoute.size(); i++) {
			if (acceptStateRoute.get(i).size() == size) {
				boolean isTheSame = true;
				for (int j = 0; j < size; j++) {
					if (!acceptStateRoute.get(i).get(j).getStateName().equals(
							stateList.get(j).getStateName())) {
						isTheSame = false;
						break;
					}
				}
				if (isTheSame) {
					return true;
				}

			}
		}
		return false;
	}

	public List<Boolean> getYesORnoList() {
		return yesORnoList;
	}

	public void setYesORnoList(List<Boolean> yesORnoList) {
		this.yesORnoList = yesORnoList;
	}

	public void addYesORnoList(Boolean yesORno) {
		this.yesORnoList.add(yesORno);
	}

	public List<Integer> getX() {
		return x;
	}

	public void setX(List<Integer> x) {
		this.x = x;
	}

	public void addX(Integer x) {
		this.x.add(x);
	}

	public List<Integer> getXSymbol() {
		return xSymbol;
	}

	public void setXSymbol(List<Integer> symbol) {
		xSymbol = symbol;
	}

	public void addXSymbol(Integer symbol) {
		xSymbol.add(symbol);
	}

	public List<Integer> getY() {
		return y;
	}

	public void setY(List<Integer> y) {
		this.y = y;
	}

	public void addY(Integer y) {
		this.y.add(y);
	}

	public List<Integer> getYSymbol() {
		return ySymbol;
	}

	public void setYSymbol(List<Integer> symbol) {
		ySymbol = symbol;
	}

	public void addYSymbol(Integer symbol) {
		ySymbol.add(symbol);
	}

	public void setErrorStateRoute(List<List<State>> errorStateRoute) {
		this.errorStateRoute = errorStateRoute;
	}

	public void addErrorStateRoute(List<State> errorStateRoute) {
		this.errorStateRoute.add(errorStateRoute);
	}

	public List<List<State>> getErrorStateRoute() {
		return errorStateRoute;
	}

	public void setAcceptStateRoute(List<List<State>> acceptStateRoute) {
		this.acceptStateRoute = acceptStateRoute;
	}

	public void addAcceptStateRoute(List<State> acceptStateRoute) {
		this.acceptStateRoute.add(acceptStateRoute);
	}

	public List<List<State>> getAcceptStateRoute() {
		return acceptStateRoute;
	}

	public void setControllableStatus(Integer controllableStatus) {
		this.controllableStatus = controllableStatus;
	}

	public Integer getControllableStatus() {
		return controllableStatus;
	}

	public boolean containRoute(List<List<State>> route) {
		for ( int i = 0; i < route.size(); i++)
		{
			List<State> listTemp = route.get(i);
			if( listTemp.get(listTemp.size() - 1).getStateName().equals(StateName))
				return true;
		}
		return false;

	}

}
