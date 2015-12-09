package cn.cstv.wspscm.commands;

import java.util.List;

import org.eclipse.gef.commands.Command;


import cn.cstv.wspscm.model.Lifeline;
import cn.cstv.wspscm.model.LineConnection;


public class ReconnectSourceCommand extends Command {

	private Lifeline source, target, oldSource;

	private LineConnection connection;

	@SuppressWarnings("unchecked")
	@Override
	public boolean canExecute() {
		if (connection.getTarget().equals(source))
			return false;

		List edges = source.getSourceConnections();
		for (int i = 0; i < edges.size(); i++) {
			LineConnection edge1 = ((LineConnection) (edges.get(i)));
			if (edge1.getTarget().equals(target)
					&& !edge1.getSource().equals(oldSource))
				return false;
		}
		return true;
	}

	@Override
	public void execute() {
		if (source != null) {
			oldSource.removeSourceConnection(connection);
			connection.setSource(source);
			source.addSourceConnection(connection);
		}
	}

	public LineConnection getEdge() {
		return connection;
	}

	public Lifeline getSource() {
		return source;
	}

	public Lifeline getTarget() {
		return target;
	}

	public void setEdge(LineConnection e) {
		this.connection = e;
		target = e.getTarget();
		oldSource = e.getSource();
	}

	public void setSource(Lifeline source) {
		this.source = source;
	}

	public void setTarget(Lifeline target) {
		this.target = target;
	}

	@Override
	public void undo() {
		source.removeSourceConnection(connection);
		connection.setSource(oldSource);
		oldSource.addSourceConnection(connection);
	}

}
