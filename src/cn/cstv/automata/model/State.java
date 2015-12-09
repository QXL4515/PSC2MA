package cn.cstv.automata.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;

public class State {

	// 状态名，通常定义为s0,s1,s2...
	private String name;

	// -1代表错误状态, 0代表初始状态，1代表通常状态，2代表接受状态，3表示 初始状态+接受状态，-2表示初始状态+错误状态
	private int type = 1;// 默认为1

	private Point location = new Point(0, 0);

	private List<Transition> sourceTransitions;

	private List<Transition> targetTransitions;

	// 三个构造函数，对应着不同的参数
	public State() {
		this.setSourceTransitions(new ArrayList<Transition>());
		this.setTargetTransitions(new ArrayList<Transition>());
	}

	public State(String name) {
		super();
		this.name = name;
		this.setSourceTransitions(new ArrayList<Transition>());
		this.setTargetTransitions(new ArrayList<Transition>());
	}

	public State(int type) {
		super();
		this.type = type;
		this.setSourceTransitions(new ArrayList<Transition>());
		this.setTargetTransitions(new ArrayList<Transition>());
	}

	public State(int type, String name) {
		super();
		this.name = name;
		this.type = type;
		this.setSourceTransitions(new ArrayList<Transition>());
		this.setTargetTransitions(new ArrayList<Transition>());
	}

	public boolean equals(State state) {
		return this.name.equals(state.name);
	}

	/**
	 * @param sourceTransitions
	 *            the sourceTransitions to set
	 */
	public void setSourceTransitions(List<Transition> sourceTransitions) {
		this.sourceTransitions = sourceTransitions;
	}

	/**
	 * @param sourceTransition
	 *            the sourceTransition to add
	 */
	public void addSourceTransition(Transition sourceTransition) {
		this.sourceTransitions.add(sourceTransition);
	}

	/**
	 * @param sourceTransition
	 *            the sourceTransition to remove
	 */
	public void removeSourceTransition(Transition sourceTransition) {
		this.sourceTransitions.remove(sourceTransition);
	}

	/**
	 * @return the sourceTransitions
	 */
	public List<Transition> getSourceTransitions() {
		return sourceTransitions;
	}

	/**
	 * @param targetTransitions
	 *            the targetTransitions to set
	 */
	public void setTargetTransitions(List<Transition> targetTransitions) {
		this.targetTransitions = targetTransitions;
	}

	/**
	 * @param targetTransition
	 *            the targetTransition to add
	 */
	public void addTargetTransition(Transition targetTransition) {
		this.targetTransitions.add(targetTransition);
	}

	/**
	 * @param targetTransition
	 *            the targetTransition to remove
	 */
	public void removeTargetTransition(Transition targetTransition) {
		this.targetTransitions.remove(targetTransition);
	}

	/**
	 * @return the targetTransitions
	 */
	public List<Transition> getTargetTransitions() {
		return targetTransitions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(Point p) {
		if (this.location.equals(p)) {
			return;
		}
		this.location = p;
	}

	/**
	 * @return the location
	 */
	public Point getLocation() {
		return location;
	}
}
