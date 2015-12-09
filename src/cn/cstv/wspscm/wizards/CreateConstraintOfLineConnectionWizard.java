package cn.cstv.wspscm.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CreateConstraintOfLineConnectionWizard extends Wizard {

	private String value;
	private String constraint;
	private String reset;
	private String typeOfConstraint;
	private String pastOrFuture;

	public class CreateConstraintPage extends WizardPage {

		private Combo combo;
		private Text resetText;
		private Text constraintText;
		private Text valueText;

		public CreateConstraintPage(String pageName) {
			super(pageName);
			setTitle("Create Constraint");
			setDescription("Create");
		}

		@Override
		public void createControl(Composite parent) {
			// TODO Auto-generated method stub
			Composite container = new Composite(parent, SWT.NONE);
			
			final Label typeLabel = new Label(container, SWT.NONE);
			typeLabel.setBounds(46, 9, 29, 25);
			typeLabel.setText("Type");

			combo = new Combo(container, SWT.NONE);
			combo.setBounds(75, 5, 177, 10);
			combo.add("Past");
			combo.add("Future");
			
			final Label valueLabel = new Label(container, SWT.NONE);
			valueLabel.setBounds(40, 33, 35, 20);
			valueLabel.setText("Value");

			valueText = new Text(container, SWT.BORDER);
			valueText.setBounds(75, 30, 177, 20);
			valueText.setText("");
			
			final Label constraintLabel = new Label(container, SWT.NONE);
			constraintLabel.setBounds(10, 56, 60, 20);
			constraintLabel.setText("Constraint");

			constraintText = new Text(container, SWT.BORDER);
			constraintText.setBounds(75, 53, 177, 20);
			constraintText.setText("");
			
			final Label resetLabel = new Label(container, SWT.NONE);
			resetLabel.setBounds(35, 79, 35, 350);
			resetLabel.setText("Reset");

			resetText = new Text(container, SWT.BORDER);
			resetText.setBounds(75, 76, 177, 20);
			resetText.setText("");
			setControl(container);
		}

	}
	
	public CreateConstraintOfLineConnectionWizard(String typeOfConstraint){
		super();
		this.typeOfConstraint = typeOfConstraint;
	}

	public void addPages(){
		addPage(new CreateConstraintPage(this.typeOfConstraint));	
	}
	
	public String getValue() {
		return value;
	}

	public String getConstraint() {
		return constraint;
	}

	public String getReset() {
		return reset;
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		CreateConstraintPage page = (CreateConstraintPage)getPage(typeOfConstraint);
//		if (page.valueText.getText().isEmpty()) {
//			page.setErrorMessage("You must input value!");
//			return false;
//		}
//		if (!page.valueText.getText().isEmpty()) {
			setValue(page.valueText.getText());
//		}
//		if (!page.constraintText.getText().isEmpty()) {
			setConstraint(page.constraintText.getText());
//		}
//		if (!page.resetText.getText().isEmpty()) {
			setReset(page.resetText.getText());
//		}
		setPastOrFuture(page.combo.getText());
		return true;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setConstraint(String constraint) {
		this.constraint = constraint;
	}

	public void setReset(String reset) {
		this.reset = reset;
	}

	public void setTypeOfConstraint(String typeOfConstraint) {
		this.typeOfConstraint = typeOfConstraint;
	}

	public String getTypeOfConstraint() {
		return typeOfConstraint;
	}

	public void setPastOrFuture(String pastOrFuture) {
		this.pastOrFuture = pastOrFuture;
	}

	public String getPastOrFuture() {
		return pastOrFuture;
	}

}
