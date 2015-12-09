package cn.cstv.wspscm.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class MissionForRefactorPSC {
HashMap hm;
ArrayList<String> aa;
ArrayList<String> placeArray;
String stateNameplace;
String firstMessageName;
String MessageSequence;


public MissionForRefactorPSC(ArrayList<String> aa2, String stateNameplace2,
		ArrayList<String> placeArray2) {
	hm=new HashMap();
	this.stateNameplace=stateNameplace2;
	placeArray=placeArray2;
	this.aa=aa2;    
	firstMessageName=aa2.get(0).replaceAll(" ","");
}

public ArrayList<String> getPlaceArray() {
	return placeArray;
}
public ArrayList<String> getMessageSequenceWithoutFirstMessageName() {
	aa.remove(0);
	return aa;
}



public String  getMessageSequence() {
	for(int i=0;i<aa.size();i++){
		MessageSequence=MessageSequence+aa.get(i);
	} 
	 
	return MessageSequence;
}

public String getFirstMessageName() {
	return firstMessageName;
}

public String getstateNameplace() {
	return stateNameplace;
}

}
