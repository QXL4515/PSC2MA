package cn.cstv.wspscm.editor;

import org.eclipse.ui.views.properties.PropertySheetPage;

/**
 * @author hp
 *
 */
public class MyPropertySheetPage extends PropertySheetPage {
	public MyPropertySheetPage(){
		this.setSorter(new MyPropertySheetSorter());
		}
}
