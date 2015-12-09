package cn.cstv.wspscm.commands;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.tools.SelectionTool;

import cn.cstv.wspscm.model.LineConnection;
import cn.cstv.wspscm.parts.LineConnectionEditPart;

public class SetConstraintTool extends SelectionTool {

	// private String PresentConstraintValue = "";
	//
	// private String PresentConstraintConstraint = "";
	// private String PresentConstraintReset = "";

	private String typeOfConstraint;

	
	// public SetConstraintTool(String PresentConstraintValue,
	// String PresentConstraintConstraint, String PresentConstraintReset) {
	public SetConstraintTool(String typeOfConstraint) {
		super();
		this.typeOfConstraint = typeOfConstraint;
		// this.PresentConstraintValue = PresentConstraintValue;
		// this.PresentConstraintConstraint = PresentConstraintConstraint;
		// this.PresentConstraintReset = PresentConstraintReset;
	}

	public String getTypeOfConstraint() {
		return typeOfConstraint;
	}

	public void setTypeOfConstraint(String typeOfConstraint) {
		this.typeOfConstraint = typeOfConstraint;
	}

	@Override
	protected boolean handleButtonDown(int button) {
		// Get selected editpart
		EditPart editPart = this.getTargetEditPart();

		if (editPart instanceof LineConnectionEditPart) {
			LineConnectionEditPart nodePart = (LineConnectionEditPart) editPart;
			LineConnection lineConnection = (LineConnection) nodePart
					.getModel();

			// Create an instance of ChangeColorRequest
			SetConstraintRequest request = new SetConstraintRequest(
					lineConnection, typeOfConstraint);

			// Get command from the editpart
			Command command = editPart.getCommand(request);

			// Execute the command
			this.getDomain().getCommandStack().execute(command);

			return true;
		}
		return false;
	}
}
