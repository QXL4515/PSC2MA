package cn.cstv.automata.editPart;

import java.util.List;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import cn.cstv.automata.model.AutomataGraph;
import cn.cstv.automata.model.State;
import cn.cstv.automata.policy.AutomataXYLayoutEditPolicy;

public class AutomataGraphEditPart extends AbstractGraphicalEditPart {

	@Override
	protected IFigure createFigure() {
		FreeformLayer freeformLayer = new FreeformLayer();
		freeformLayer.setLayoutManager(new FreeformLayout());
		return freeformLayer;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE,
				new AutomataXYLayoutEditPolicy());
	}

	@Override
	protected List<State> getModelChildren() {
		List<State> states = ((AutomataGraph) getModel()).getStates();
		return states;
	}
	
	@Override
	protected void refreshVisuals(){
//		ConnectionLayer connectionLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
//		connectionLayer.setConnectionRouter(new ManhattanConnectionRouter());
	}

}
