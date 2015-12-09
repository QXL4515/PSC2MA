package cn.cstv.wspscm.actions;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;

import cn.cstv.wspscm.IImageKeys;
import cn.cstv.wspscm.commands.CreatePresentConstraintCommand;

public class CreatePresentConstraintAction extends SelectionAction {

	public CreatePresentConstraintAction(IWorkbenchPart part) {
		super(part);
		this.setLazyEnablementCalculation(false);
		// TODO Auto-generated constructor stub
	}
	
	protected void init(){
		setText("Create PresentConstraint");
		setToolTipText("Create PresentConstraint");
		setId(ActionFactory.RENAME.getId());
		setImageDescriptor(IImageKeys.getImageDescriptor(IImageKeys.PRESENTCONSTRAINT));
		setEnabled(false);
	}

	@Override
	protected boolean calculateEnabled() {
		// TODO Auto-generated method stub
		CreatePresentConstraintCommand cmd = new CreatePresentConstraintCommand();
		if(cmd==null)return false;
		return true;
	}

}
