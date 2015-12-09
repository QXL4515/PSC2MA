package cn.cstv.wspscm.policy;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;

import cn.cstv.wspscm.commands.SetConstraintOfLineConnectionCommand;
import cn.cstv.wspscm.commands.SetConstraintRequest;
import cn.cstv.wspscm.model.LineConnection;

/**
 * @author hp
 * 
 */
public class AttributeEditPolicy extends AbstractEditPolicy {
	final static public String SET_CONSTRAINT_ROLE = "SET_CONSTRAINT_ROLE";

	public Command getCommand(Request request) {
		// TODO Auto-generated method stub
		if (request.getType() == SetConstraintRequest.REQ_SET_CONSTRAINT) {
			SetConstraintRequest theRequest = (SetConstraintRequest) request;
			LineConnection lineConnection = theRequest.getLineConnection();
			String typeOfConstraint = theRequest.getTypeOfConstraint();

			SetConstraintOfLineConnectionCommand command = new SetConstraintOfLineConnectionCommand(
					lineConnection, typeOfConstraint, getHost().getViewer().getControl().getShell());
			return command;
		}
		return null;
	}

}
