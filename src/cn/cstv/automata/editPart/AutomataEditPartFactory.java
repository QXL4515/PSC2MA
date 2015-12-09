package cn.cstv.automata.editPart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import cn.cstv.automata.model.AutomataGraph;
import cn.cstv.automata.model.State;
import cn.cstv.automata.model.Transition;

public class AutomataEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		// TODO Auto-generated method stub
		EditPart editPart = null;
		if (model instanceof State) {
			editPart = new StateEditPart();
		} else if (model instanceof AutomataGraph) {
			editPart = new AutomataGraphEditPart();
		}else if (model instanceof Transition) {
			editPart = new TransitionEditPart();
		}

		if (editPart != null) {
			editPart.setModel(model);
		}

		return editPart;
	}

}
