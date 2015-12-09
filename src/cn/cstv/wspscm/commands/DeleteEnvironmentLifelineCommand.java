package cn.cstv.wspscm.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;

import cn.cstv.wspscm.model.Diagram;
import cn.cstv.wspscm.model.EnvironmentLifeline;
import cn.cstv.wspscm.model.Lifeline;
import cn.cstv.wspscm.model.LineConnection;

@SuppressWarnings("unchecked")
public class DeleteEnvironmentLifelineCommand extends Command {

	private Diagram parent;
	private EnvironmentLifeline child;


	private List sources = new ArrayList();
	private List targets = new ArrayList();

	@Override
	public void execute() {
		sources.addAll(child.getSourceConnections());
		targets.addAll(child.getTargetConnections());

		for (int i = 0; i < sources.size(); i++) {
			LineConnection model = (LineConnection) sources.get(i);
			model.detachSource();
			model.detachTarget();
		}

		for (int i = 0; i < targets.size(); i++) {
			LineConnection model = (LineConnection) targets.get(i);
			model.detachSource();
			model.detachTarget();
		}
		
		parent.removeChild(child);
	}

	public void setChild(Object child) {
		this.child = (EnvironmentLifeline)child;
	}

	public void setParent(Object parent) {
		this.parent = (Diagram)parent;
	}

	@Override
	public void undo() {
		parent.addChild(child);
		
		for (int i = 0; i < sources.size(); i++) {
			LineConnection model = (LineConnection) sources.get(i);
			model.attachSource();
			model.attachTarget();
		}

		for (int i = 0; i < targets.size(); i++) {
			LineConnection model = (LineConnection) targets.get(i);
			model.attachSource();
			model.attachTarget();
		}

		sources.clear();
		targets.clear();

	}
}
