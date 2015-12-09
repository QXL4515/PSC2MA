package cn.cstv.wspscm.commands;

import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.jface.resource.ImageDescriptor;

public class SetConstraintToolEntry extends ToolEntry {

//	private String PresentConstraintValue = "";
//
//	 private String PresentConstraintConstraint = "";
//	 private String PresentConstraintReset = "";
	private String typeOfConstraint;

	 public SetConstraintToolEntry(String label, String shortDesc,
	 ImageDescriptor iconSmall, ImageDescriptor iconLarge,String
	 typeOfConstraint) {
		// public SetConstraintToolEntry(String label, String shortDesc,
		// ImageDescriptor iconSmall, ImageDescriptor iconLarge,
		// String PresentConstraintValue, String PresentConstraintConstraint,
		// String PresentConstraintReset) {
		super(label, shortDesc, iconSmall, iconLarge);
		this.setTypeOfConstraint(typeOfConstraint);
//		this.PresentConstraintValue = PresentConstraintValue;
//		this.PresentConstraintConstraint = PresentConstraintConstraint;
//		this.PresentConstraintReset = PresentConstraintReset;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Tool createTool() {
		SetConstraintTool tool = new SetConstraintTool(typeOfConstraint);
		tool.setUnloadWhenFinished(false);// Switch to selection tool after
											// performed?
		tool.setDefaultCursor(SharedCursors.CROSS);// Any cursor you like
		return tool;
	}

	public void setTypeOfConstraint(String typeOfConstraint) {
		this.typeOfConstraint = typeOfConstraint;
	}

	public String getTypeOfConstraint() {
		return typeOfConstraint;
	}

}
