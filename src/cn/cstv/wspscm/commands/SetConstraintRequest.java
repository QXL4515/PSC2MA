package cn.cstv.wspscm.commands;

import org.eclipse.gef.Request;

import cn.cstv.wspscm.model.LineConnection;

public class SetConstraintRequest extends Request {

	final static public String REQ_SET_CONSTRAINT = "REQ_SET_CONSTRAINT";
	private LineConnection lineConnection;
	private String typeOfConstraint;

	// private String PresentConstraintValue = "";
	// private String PresentConstraintConstraint = "";
	// private String PresentConstraintReset = "";

	// public SetConstraintRequest(LineConnection lineConnection,
	// String PresentConstraintValue, String PresentConstraintConstraint,
	// String PresentConstraintReset) {
	public SetConstraintRequest(LineConnection lineConnection, String typeOfConstraint) {
		super();
		this.lineConnection = lineConnection;
		this.typeOfConstraint = typeOfConstraint;
		// this.PresentConstraintValue = PresentConstraintValue;
		// this.PresentConstraintConstraint = PresentConstraintConstraint;
		// this.PresentConstraintReset = PresentConstraintReset;
		setType(REQ_SET_CONSTRAINT);
	}

	public LineConnection getLineConnection() {
		return lineConnection;
	}

	public void setLineConnection(LineConnection lineConnection) {
		this.lineConnection = lineConnection;
	}

	public void setTypeOfConstraint(String typeOfConstraint) {
		this.typeOfConstraint = typeOfConstraint;
	}

	public String getTypeOfConstraint() {
		return typeOfConstraint;
	}

	// public String getPresentConstraintValue() {
	// return PresentConstraintValue;
	// }
	//
	// public void setPresentConstraintValue(String presentConstraintValue) {
	// PresentConstraintValue = presentConstraintValue;
	// }
	//
	// public String getPresentConstraintConstraint() {
	// return PresentConstraintConstraint;
	// }
	//
	// public void setPresentConstraintConstraint(
	// String presentConstraintConstraint) {
	// PresentConstraintConstraint = presentConstraintConstraint;
	// }
	//
	// public String getPresentConstraintReset() {
	// return PresentConstraintReset;
	// }
	//
	// public void setPresentConstraintReset(String presentConstraintReset) {
	// PresentConstraintReset = presentConstraintReset;
	// }
}
