package cn.cstv.wspscm.monitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class MissionForRefactor {
HashMap hm;
ArrayList<String> placeArray;
String messageSequence;
String firstMessageName;

public MissionForRefactor(String messageSequence,ArrayList<String> place){
	hm=new HashMap();
	this.messageSequence=messageSequence;
	placeArray=place;
	firstMessageName=findFirstName(messageSequence);
}

private String findFirstName(String messageSequence){
	StringTokenizer token=new StringTokenizer(messageSequence," , ");
	return token.nextToken();
}

public ArrayList<String> getPlaceArray() {
	return placeArray;
}

public String getMessageSequenceWithoutFirstMessageName() {
	if(messageSequence.indexOf(',')!=-1)
		return messageSequence.substring(messageSequence.indexOf(',')+2);//因为‘，’后面还有一个空格
	else 
		return "";
}



public String getMessageSequence() {
	return messageSequence;
}

public String getFirstMessageName() {
	return firstMessageName;
}


}
