package cn.cstv.automata.editPart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RelativeBendpoint;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;

import cn.cstv.automata.model.ConnectionBendpoint;
import cn.cstv.automata.model.State;
import cn.cstv.automata.model.Transition;
import cn.cstv.automata.policy.AutomataBendpointEditPolicy;
import cn.cstv.automata.policy.AutomataConnectionEditPolicy;
import cn.cstv.wspscm.figure.MidpointOffsetLocator;

@SuppressWarnings("unchecked")
public class TransitionEditPart extends AbstractConnectionEditPart {

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		installEditPolicy(EditPolicy.CONNECTION_ROLE,
				new AutomataConnectionEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
				new ConnectionEndpointEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE,
				new AutomataBendpointEditPolicy());
	}

	@Override
	protected IFigure createFigure() {
		Transition t = (Transition) getModel();
		State source = t.getSource();
		State target = t.getTarget();
		if (source == target) {
			// ConnectionBendpoint cbp = new ConnectionBendpoint();
		}
		PolylineConnection connection = new PolylineConnection();
		connection.setTargetDecoration(new PolygonDecoration());
		connection.setConnectionRouter(new BendpointConnectionRouter());
		return connection;
	}

	@Override
	protected void refreshVisuals() {
		Transition t = (Transition) getModel();
		State source = t.getSource();
		State target = t.getTarget();
		if (source == target) {
			List bendPoints = t.getBendPoint();
			List constraints = new ArrayList();

			for (int i = 0; i < bendPoints.size(); i++) {
				ConnectionBendpoint cbp = (ConnectionBendpoint) bendPoints
						.get(i);
				RelativeBendpoint rbp = new RelativeBendpoint(
						getConnectionFigure());
				rbp.setRelativeDimensions(cbp.getFirstRelativeDimension(), cbp
						.getSecondRelativeDimension());
				rbp.setWeight((i + 1) / ((float) bendPoints.size() + 1));
				constraints.add(rbp);
			}
			this.getConnectionFigure().setRoutingConstraint(constraints);
			if (!t.isHasAddCondition()) {
				((PolylineConnection) getFigure()).add(new Label(t
						.getCondition()), new MidpointOffsetLocator(
						((PolylineConnection) getFigure()), 0,
						new Point(0, -14)));
			}
		} else if (source.getType() == 1 && target.getType() == 1
				&& source != target) {
			if (!t.isHasAddCondition()) {
				((PolylineConnection) getFigure()).add(new Label(t
						.getCondition()), new MidpointOffsetLocator(
						((PolylineConnection) getFigure()), 0,
						new Point(0, -10)));
				t.setHasAddCondition(true);
			}
		} else if (!(source.getType() == 1 && target.getType() == 1)
				&& (source != target)) {
			Random random = new Random();
			int k = random.nextInt(10);
			if (!t.isHasAddCondition()) {
				if(target.getType()==1){
					((PolylineConnection) getFigure()).add(new Label(t
							.getCondition()), new MidpointOffsetLocator(
							((PolylineConnection) getFigure()), 0, new Point(0,
									-k * 2)));

				}else{
					((PolylineConnection) getFigure()).add(new Label(t
							.getCondition()), new MidpointOffsetLocator(
							((PolylineConnection) getFigure()), 0, new Point(0,
									k * 2)));
				}
				t.setHasAddCondition(true);
			}

		}

	}

}
