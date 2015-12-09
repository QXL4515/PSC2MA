package cn.cstv.wspscm.monitor;

import java.util.ArrayList;

public class ExamineMission {
private ArrayList<String> list;//还没判定的消息序列
private ArrayList<String> placeList;//位置序列
private String stateName;

public ExamineMission(String stateName,ArrayList<String> list,ArrayList<String> placeList){
	this.list=list;
	this.stateName=stateName;
	this.placeList=placeList;
}

public ArrayList<String> getList() {
	return list;
}

public void setList(ArrayList<String> list) {
	this.list = list;
}

public String getStateName() {
	return stateName;
}

public void setStateName(String stateName) {
	this.stateName = stateName;
}

public ArrayList<String> getPlaceList() {
	return placeList;
}

public void setPlaceList(ArrayList<String> placeList) {
	this.placeList = placeList;
}


}
