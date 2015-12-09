package cn.cstv.wspscm.monitor;

import java.util.ArrayList;
import java.util.HashMap;

public class tmpMissionForRefactorPSC {
	ArrayList<String> aa;
	ArrayList<String> placeArray;
	
	public tmpMissionForRefactorPSC(ArrayList<String> aa,ArrayList<String> place){
		 
		 
		placeArray=place;
		this.aa=aa;
		 
	}

	public ArrayList<String> getPlaceArray() {
		return placeArray;
	}
	 



	public ArrayList<String> getMessageSequence() {
		return aa;
	}

 

}
