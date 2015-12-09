package cn.cstv.automata.model;

import java.util.ArrayList;
import java.util.List;

public class AutomataGraph {

	List<State> states;

	public AutomataGraph() {
		this.states = new ArrayList<State>();
//		createNodes();
	}

//	protected void createNodes() {
//		for (int i = 0; i < 5; i++) {
//			State node = new State(i,"s"+i);
//			getStates().add(node);
//			if (i != 0) {
//				Transition edge = new Transition();
//				edge.setSource((State)getStates().get(i - 1));
//				edge.setTarget(node);
//			}
//		}
//	}

	public List<State> getStates() {
		return states;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}
	
	public void addState(State state){
		this.states.add(state);
	}

}
