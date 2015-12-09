package cn.cstv.automata.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;

@SuppressWarnings("unchecked")
public class Transition {

	private State source;
	private State target;

	private String condition;

	private List bendPoint = new ArrayList();
	
	//ÊÇ·ñÌí¼Ócondition
	private boolean hasAddCondition = false;

	
	public Transition() {
		super();
	}

	public Transition(State source, State target) {
		super();
		setSource(source);
		setTarget(target);
		// source.addSourceTransition(this);
		// target.addTargetTransition(this);
	}

	/**
	 * @param condition
	 *            the condition to set
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}

	/**
	 * @return the condition
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(State source) {
		if (this.source != null) {
			this.source.removeSourceTransition(this);
		}
		this.source = source;
		if (this.source != null) {
			this.source.addSourceTransition(this);
		}
	}

	/**
	 * @return the source
	 */
	public State getSource() {
		return source;
	}

	/**
	 * @param target
	 *            the target to set
	 */
	public void setTarget(State target) {
		if (this.target != null) {
			this.target.removeTargetTransition(this);
		}
		this.target = target;
		if (this.target != null) {
			this.target.addTargetTransition(this);
		}
	}

	/**
	 * @return the target
	 */
	public State getTarget() {
		return target;
	}

	/**
	 * @param bendPoint
	 *            the bendPoint to set
	 */
	public void setBendPoint(List bendPoint) {
		this.bendPoint = bendPoint;
	}

	/**
	 * @return the bendPoint
	 */
	public List getBendPoint() {
		return bendPoint;
	}

	public void addBendPoint(int index, ConnectionBendpoint point) {
		bendPoint.add(index, point);
	}

	public void setBendpointRelativeDimensions(int index, Dimension d1,
			Dimension d2) {
		ConnectionBendpoint cbp = (ConnectionBendpoint) getBendPoint().get(
				index);
		cbp.setRelativeDimensions(d1, d2);
	}

	/**
	 * @param hasAddCondition the hasAddCondition to set
	 */
	public void setHasAddCondition(boolean hasAddCondition) {
		this.hasAddCondition = hasAddCondition;
	}

	/**
	 * @return the hasAddCondition
	 */
	public boolean isHasAddCondition() {
		return hasAddCondition;
	}

}
