package cn.cstv.automata.editPart;

import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import cn.cstv.automata.figure.StateFigure;
import cn.cstv.automata.model.State;
import cn.cstv.automata.model.Transition;

public class StateEditPart extends AbstractGraphicalEditPart implements
		NodeEditPart {

	@Override
	protected IFigure createFigure() {
		// TODO Auto-generated method stub
		int type = ((State) getModel()).getType();
		String name = ((State) getModel()).getName();
		Point loc = ((State) getModel()).getLocation();
		return new StateFigure(type, name, loc);
		// return new StateFigure(type);
	}

	@Override
	protected void createEditPolicies() {
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection) {
		// TODO Auto-generated method stub
		return new EllipseAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		// TODO Auto-generated method stub
		return new EllipseAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connection) {
		// TODO Auto-generated method stub
		return new EllipseAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		// TODO Auto-generated method stub
		return new EllipseAnchor(getFigure());
	}

	@Override
	protected List<Transition> getModelSourceConnections() {
		return ((State) getModel()).getSourceTransitions();
	}

	/*
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections
	 * ()
	 */
	@Override
	protected List<Transition> getModelTargetConnections() {
		return ((State) getModel()).getTargetTransitions();
	}

	// 下面可能需要重新设置
	@Override
	protected void refreshVisuals() {
		// State state =(State)getModel();
		// Point loc = state.getLocation();
		// Dimension size = new Dimension(40, 40);
		// Rectangle bounds = new Rectangle(loc, size);
		//		
		// // Rectangle bounds = new Rectangle(((StateFigure) getModel())
		// // .getLocation(), ((StateFigure) getModel()).getSize());
		// ((GraphicalEditPart) getParent()).setLayoutConstraint(this,
		// getFigure(), bounds);
		StateFigure nodeFigure = (StateFigure) getFigure();
		Point location = nodeFigure.getEllipse().getLocation();
		Dimension size = nodeFigure.getEllipse().getSize();
		AutomataGraphEditPart graph = (AutomataGraphEditPart) getParent();
		Rectangle constraint = new Rectangle(location, size);
		graph.setLayoutConstraint(this, nodeFigure, constraint);
	}
}
