package cn.cstv.wspscm.commands;

import org.eclipse.gef.commands.Command;

import cn.cstv.wspscm.model.LineConnection;

/**
 * @author hp
 *
 */
public class CreatePresentConstraintCommand extends Command {

	private LineConnection connection;
//	private PresentConstraint presentConstraint;
	private String value;
	private String constraint;
	private String reset;
	private String oldValue="";
	private String oldConstraint="";
	private String oldReset="";
	
	public boolean canExecute(){
		if(connection == null)
			return false;
		return true;
	}

	public void execute(){
		if(!connection.getPresentConstraintValue().isEmpty()){
			oldValue = connection.getPresentConstraintValue();
		}
		if(!connection.getPresentConstraintConstraint().isEmpty()){
			oldConstraint = connection.getPresentConstraintConstraint();
		}
		if(!connection.getPresentConstraintReset().isEmpty()){
			oldReset = connection.getPresentConstraintReset();
		}
		connection.setPresentConstraintValue(value);
		connection.setPresentConstraintConstraint(constraint);
		connection.setPresentConstraintReset(reset);
//		connection.setPresentConstraint("true");		
	}

	/**
	 * @param connection the connection to set
	 */
	public void setConnection(Object connection) {
		this.connection = (LineConnection)connection;
	}

	public void setValue(Object value) {
		this.value = (String)value;
	}

	public void setConstraint(Object constraint) {
		this.constraint = (String)constraint;
	}

	public void setReset(Object reset) {
		this.reset = (String)reset;
	}

	/**
	 * @param presentConstraint the presentConstraint to set
	 */
//	public void setPresentConstraint(Object presentConstraint) {
//		this.presentConstraint = (PresentConstraint)presentConstraint;
//	}

	public void undo(){
		connection.setPresentConstraintValue(oldValue);
		connection.setPresentConstraintConstraint(oldConstraint);
		connection.setPresentConstraintReset(oldReset);
	}

}
