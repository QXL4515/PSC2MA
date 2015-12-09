package cn.cstv.wspscm.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;

import cn.cstv.wspscm.figure.PresentConstraintFigure;
import cn.cstv.wspscm.policy.AttributeEditPolicy;

/**
 * @author hp
 *
 */
public class PresentConstraintEditPart extends EditPartWithListener {

	LineConnectionEditPart line;
	
	protected IFigure createFigure() {
		return new PresentConstraintFigure("");
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new AttributeEditPolicy());
	}
	

	@Override
	protected void refreshVisuals() {
		
	}
}
