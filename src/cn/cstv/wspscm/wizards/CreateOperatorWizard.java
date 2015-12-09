package cn.cstv.wspscm.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CreateOperatorWizard extends Wizard {

	private String type = "par";
	private String num;
	private String typeOfOperator;
	

	public class CreateOperatorPage extends WizardPage {

		private Combo combo;
		private Text numText;

		public CreateOperatorPage(String pageName) {
			super(pageName);
			setTitle("Create Operator");
			//setDescription("Create");
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
			combo.add("Par");
			combo.add("Loop");
			combo.add("Alt");
			
			
			final Label numLabel = new Label(container, SWT.NONE);
			numLabel.setBounds(0, 33, 75, 20);
			numLabel.setText("Num(alt,par)");

			numText = new Text(container, SWT.BORDER);
			numText.setBounds(75, 30, 177, 20);
			numText.setText("");
			/*
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
			*/
			setControl(container);
		}

	}
	
	public CreateOperatorWizard(String typeOfOperator){
		super();
		this.typeOfOperator = typeOfOperator;
	}
	
	public void addPages(){
		addPage(new CreateOperatorPage(this.type));	
	}
	/*
	public String getValue() {
		return value;
	}

	public String getConstraint() {
		return constraint;
	}

	public String getReset() {
		return reset;
	}
	*/
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		CreateOperatorPage page = (CreateOperatorPage)getPage(type);
		setType(page.combo.getText());
		setNum(page.numText.getText());
		return true;
	}
	
	public String getNum(){
		return num;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public void setNum(String num) {
		this.num = num;
	}

	
}
