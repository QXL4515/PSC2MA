package cn.cstv.wspscm.model;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class test {

	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			test window = new test();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window
	 */
	public void open() {
		final Display display = Display.getDefault();
		final Shell shell = new Shell();
		shell.setSize(500, 375);
		shell.setText("SWT Application");
		//

		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

}
