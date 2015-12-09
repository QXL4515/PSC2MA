package cn.cstv.wspscm.policy;

import org.eclipse.draw2d.Label;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import cn.cstv.wspscm.commands.DirectEditCommand;

public class LifelineDirectEditPolicy extends DirectEditPolicy {

	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		// TODO Auto-generated method stub
		DirectEditCommand command = new DirectEditCommand();
		command.setModel(getHost().getModel());
		command.setText((String) request.getCellEditor().getValue());
		return command;
	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
		String value = (String) request.getCellEditor().getValue();
		((Label) getHostFigure()).setText(value);
		// hack to prevent async layout from placing the cell editor twice.
		// getHostFigure().getUpdateManager().performUpdate();
	}

}
