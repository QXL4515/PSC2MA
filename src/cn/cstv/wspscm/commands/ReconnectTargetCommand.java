package cn.cstv.wspscm.commands;

import java.util.List;

import org.eclipse.gef.commands.Command;


import cn.cstv.wspscm.model.Lifeline;
import cn.cstv.wspscm.model.LineConnection;


public class ReconnectTargetCommand extends Command {

	private Lifeline source, target, oldTarget;

	private LineConnection edge;

	@SuppressWarnings("unchecked")
	@Override
	public boolean canExecute() {
		if (edge.getSource().equals(target))
			return false;

		List edges = source.getSourceConnections();
		for (int i = 0; i < edges.size(); i++) {
			LineConnection edge1 = ((LineConnection) (edges.get(i)));
			if (edge1.getTarget().equals(target)
					&& !edge1.getTarget().equals(oldTarget))
				return false;
		}
		return true;
	}

	@Override
	public void execute() {
		if (target != null) {
			oldTarget.removeTargetConnection(edge);
			edge.setTarget(target);
			target.addTargetConnection(edge);
		}
	}

	public LineConnection getEdge() {
		return edge;
	}

	public Lifeline getSource() {
		return source;
	}

	public Lifeline getTarget() {
		return target;
	}

	public void setEdge(LineConnection e) {
		this.edge = e;
		source = e.getSource();
		oldTarget = e.getTarget();
	}

	public void setSource(Lifeline source) {
		this.source = source;
	}

	public void setTarget(Lifeline target) {
		this.target = target;
	}

	@Override
	public void undo() {
		target.removeTargetConnection(edge);
		edge.setTarget(oldTarget);
		oldTarget.addTargetConnection(edge);
	}
}
