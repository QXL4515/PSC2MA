package cn.cstv.wspscm.wizards;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.part.FileEditorInput;

import cn.cstv.wspscm.IImageKeys;
import cn.cstv.wspscm.editor.PSCMonitorDiagramEditor;
import cn.cstv.wspscm.model.Diagram;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (psc).
 */

public class PSCMonitorNewWizardPage extends WizardNewFileCreationPage {

	private IWorkbench	workbench;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public PSCMonitorNewWizardPage(IWorkbench aWorkbench, IStructuredSelection selection) {
		super("New PSC-Monitor File wizardPage",selection);
		setTitle("PSC-Monitor File");
		setDescription("This wizard creates a new file with *.psc extension that can be opened by a PSC-Monitor editor.");
		this.setImageDescriptor(IImageKeys.getImageDescriptor(IImageKeys.FILE));
		this.workbench = aWorkbench;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
//		Composite container = new Composite(parent, SWT.NULL);
//		GridLayout layout = new GridLayout();
//		container.setLayout(layout);
//		layout.numColumns = 3;
//		layout.verticalSpacing = 9;
//		Label label = new Label(container, SWT.NULL);
//		label.setText("&Container:");
//
//		containerText = new Text(container, SWT.BORDER | SWT.SINGLE);
//		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
//		containerText.setLayoutData(gd);
//		containerText.addModifyListener(new ModifyListener() {
//			public void modifyText(ModifyEvent e) {
//				dialogChanged();
//			}
//		});
//
//		Button button = new Button(container, SWT.PUSH);
//		button.setText("Browse...");
//		button.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				handleBrowse();
//			}
//		});
//		label = new Label(container, SWT.NULL);
//		label.setText("&File name:");
//
//		fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
//		gd = new GridData(GridData.FILL_HORIZONTAL);
//		fileText.setLayoutData(gd);
//		fileText.addModifyListener(new ModifyListener() {
//			public void modifyText(ModifyEvent e) {
//				dialogChanged();
//			}
//		});
//		initialize();
//		dialogChanged();
//		setControl(container);
		super.createControl(parent);
		this.setFileName("Example" + ".psc");
		@SuppressWarnings("unused")
		Composite composite = (Composite)getControl();
		setPageComplete(validatePage());
	}
	
	public boolean finish() {
		IFile newFile = createNewFile();
		if (newFile == null) 
			return false;  // ie.- creation was unsuccessful

		// Since the file resource was created fine, open it for editing
		// iff requested by the user
		try {
			
			IWorkbenchWindow dwindow = workbench.getActiveWorkbenchWindow();
			IWorkbenchPage page = dwindow.getActivePage();
			IEditorInput input = new FileEditorInput(newFile);
			if (page != null)
				page.openEditor(input, PSCMonitorDiagramEditor.ID, true); 
		} 
//			IWorkbenchWindow dwindow = workbench.getActiveWorkbenchWindow();
//			IWorkbenchPage page = dwindow.getActivePage();
//			if (page != null)
//				IDE.openEditor(page, newFile, true);
//		} 
		catch (org.eclipse.ui.PartInitException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

//	private void initialize() {
//		if (selection != null && selection.isEmpty() == false
//				&& selection instanceof IStructuredSelection) {
//			IStructuredSelection ssel = (IStructuredSelection) selection;
//			if (ssel.size() > 1)
//				return;
//			Object obj = ssel.getFirstElement();
//			if (obj instanceof IResource) {
//				IContainer container;
//				if (obj instanceof IContainer)
//					container = (IContainer) obj;
//				else
//					container = ((IResource) obj).getParent();
//				containerText.setText(container.getFullPath().toString());
//			}
//		}
//		fileText.setText("new_file.psc");
//	}
//
//	/**
//	 * Uses the standard container selection dialog to choose the new value for
//	 * the container field.
//	 */
//
//	private void handleBrowse() {
//		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
//				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
//				"Select new file container");
//		if (dialog.open() == ContainerSelectionDialog.OK) {
//			Object[] result = dialog.getResult();
//			if (result.length == 1) {
//				containerText.setText(((Path) result[0]).toString());
//			}
//		}
//	}
//
//	/**
//	 * Ensures that both text fields are set.
//	 */
//
//	private void dialogChanged() {
//		IResource container = ResourcesPlugin.getWorkspace().getRoot()
//				.findMember(new Path(getContainerName()));
//		String fileName = getFileName();
//
//		if (getContainerName().length() == 0) {
//			updateStatus("File container must be specified");
//			return;
//		}
//		if (container == null
//				|| (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
//			updateStatus("File container must exist");
//			return;
//		}
//		if (!container.isAccessible()) {
//			updateStatus("Project must be writable");
//			return;
//		}
//		if (fileName.length() == 0) {
//			updateStatus("File name must be specified");
//			return;
//		}
//		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
//			updateStatus("File name must be valid");
//			return;
//		}
//		int dotLoc = fileName.lastIndexOf('.');
//		if (dotLoc != -1) {
//			String ext = fileName.substring(dotLoc + 1);
//			if (ext.equalsIgnoreCase("psc") == false) {
//				updateStatus("File extension must be \"psc\"");
//				return;
//			}
//		}
//		updateStatus(null);
//	}

//	private void updateStatus(String message) {
//		setErrorMessage(message);
//		setPageComplete(message == null);
//	}

	protected InputStream getInitialContents() {
		Diagram ld = new Diagram();
		ByteArrayInputStream bais = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(ld);
			oos.flush();
			oos.close();
			baos.close();
			bais = new ByteArrayInputStream(baos.toByteArray());
			bais.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return bais;
	}
}