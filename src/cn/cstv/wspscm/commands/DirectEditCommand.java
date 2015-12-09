package cn.cstv.wspscm.commands;

import org.eclipse.gef.commands.Command;

import cn.cstv.wspscm.model.Lifeline;

public class DirectEditCommand extends Command {
	private Lifeline lifeline;
	private String oldText, newText;

	@Override
	public void execute() {
		oldText = lifeline.getName();
		lifeline.setName(newText);
	}

	public void setModel(Object lifeline) {
		this.lifeline = (Lifeline) lifeline;
	}

	public void setText(String newText) {
		this.newText = newText;
	}

	@Override
	public void undo() {
		lifeline.setName(oldText);
	}
}
