package cn.cstv.wspscm.actions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import cn.cstv.automata.editPart.AutomataEditPartFactory;
import cn.cstv.automata.model.AutomataGraph;
import cn.cstv.automata.model.ConnectionBendpoint;
import cn.cstv.automata.model.State;
import cn.cstv.automata.model.Transition;
import cn.cstv.wspscm.IImageKeys;
import cn.cstv.wspscm.views.AutomataView;

/**
 * @author hp
 * 
 */
public class RefinementAutomataAction extends Action implements
		IWorkbenchAction, ISelectionListener {

	public static final String ID = "cn.cstv.wspscm.actions.RefinementAutomataAction";

	private IWorkbenchWindow window;

	//private ScrollingGraphicalViewer graphicalViewer;
	//private FigureCanvas canvas;
	//private AutomataGraph diagram;
	private static List<Composite> compositeArray = new ArrayList<Composite>();

	public RefinementAutomataAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("Refinement");
		setToolTipText("Refinement automata");
		setImageDescriptor(IImageKeys.getImageDescriptor(IImageKeys.REFINEMENT));
		window.getSelectionService().addSelectionListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.
	 * IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.actions.ActionFactory.IWorkbenchAction#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		window.getSelectionService().removeSelectionListener(this);
	}

	@Override
	public void run() {
		
		//计算执行时间 
		long startTime = System.currentTimeMillis();   //获取开始时间 

		
		IViewPart view = null;
		try {
			view = window.getActivePage().showView(AutomataView.ID);
		} catch (PartInitException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(int i = 0 ; i < compositeArray.size();i++)
		{
			compositeArray.get(i).dispose();
		}
		compositeArray.clear();
		
		for( int index = 0 ; index < IImageKeys.automataFilesName.size();index++)
		{
			List<String> automataList = new ArrayList<String>();

			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(IImageKeys.automataFilesName.get(index)));
				String s = "";
				while ((s = in.readLine()) != null) {
					automataList.add(s);
				}
				in.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			List<String> refinementAutomataList = Refinement(automataList);

			// 解析String类型的自动机Rule表示
			// 直接用图模型来存储相应的变量
			List<State> states = new ArrayList<State>();
			List<Transition> transitions = new ArrayList<Transition>();

			// 普通状态，自动机布局时用
			List<State> commonStates = new ArrayList<State>();
			// 非普通状态，自动布局用
			List<State> badStates = new ArrayList<State>();

			if (refinementAutomataList.size() > 0) {
				// 首先寻找第一个变量，也就是初始状态,首先保证refinementAutomataList非空
				String start = refinementAutomataList.get(0).substring(1,
						refinementAutomataList.get(0).indexOf(","));
				State s = new State(1, start);
				states.add(s);
				commonStates.add(s);
			}

			for (int i = 0; i < refinementAutomataList.size(); i++) {
				String auto = refinementAutomataList.get(i);
				String start = auto.substring(auto.indexOf("(") + 1, auto
						.indexOf(","));
				String condition = auto.substring(auto.indexOf(",") + 1, auto
						.lastIndexOf(","));
				String end = auto.substring(auto.lastIndexOf(",") + 1, auto
						.lastIndexOf(")"));
				State startState = Contains(states, start);
				if (startState == null) {
					// 一般状态
					startState = new State(1, start);
					states.add(startState);
					commonStates.add(startState);
				}
				State endState;
				if (end.contains("Accept")) {
					endState = Contains(states, end.substring(0, end.indexOf("(")));
					// 出现状态重叠的情况
					if (endState != null && endState.getType() == 1) {
						commonStates.remove(endState);
						endState.setType(2);
						badStates.add(endState);
					} else if (endState != null && endState.getType() == 0) {
						commonStates.remove(endState);
						endState.setType(3);
						badStates.add(endState);
					} else if (endState == null) {

						endState = new State(2, end.substring(0, end.indexOf("(")));
						states.add(endState);
						badStates.add(endState);
					}
				} else if (end.contains("Error")) {
					endState = Contains(states, end.substring(0, end.indexOf("(")));
					// 出现状态重叠的情况
					if (endState != null && endState.getType() == 1) {
						commonStates.remove(endState);
						endState.setType(-1);
						badStates.add(endState);
					} else if (endState != null && endState.getType() == 0) {
						commonStates.remove(endState);
						endState.setType(-2);
						badStates.add(endState);
					} else if (endState == null) {
						endState = new State(-1, end.substring(0, end.indexOf("(")));
						states.add(endState);
						badStates.add(endState);
					}
				} else {
					endState = Contains(states, end);
					if (endState == null) {
						endState = new State(1, end);
						states.add(endState);
						commonStates.add(endState);
					}
				}
				Transition t = new Transition(startState, endState);
				t.setCondition(condition);
				transitions.add(t);
			}

			// System.out.println(badStates.size());

			// ////////////重要!!!布局///////////////////

			List<Integer> lenList = new ArrayList<Integer>();
			for (int f = 0; f < commonStates.size(); f++) {
				lenList.add(f);
			}

			AutomataGraph diagram = new AutomataGraph();

			for (int m = 0; m < transitions.size(); m++) {
				Transition t = transitions.get(m);
				State source = t.getSource();
				State target = t.getTarget();

				if (source == target) {
					ConnectionBendpoint cbp = new ConnectionBendpoint();
					cbp.setRelativeDimensions(new Dimension(-25, -25),
							new Dimension(-20, -25));
					t.addBendPoint(0, cbp);
					ConnectionBendpoint cbp2 = new ConnectionBendpoint();
					cbp2.setRelativeDimensions(new Dimension(25, -25),
							new Dimension(20, -25));
					t.addBendPoint(1, cbp2);
				}

				if (source != target && source.getType() == 1
						&& target.getType() == 1) {
					int len = t.getCondition().length();
					for (int s = 0; s < commonStates.size(); s++) {
						if (source == commonStates.get(s)) {
							lenList.set(s, len);
						}
					}
				}

			}

			// 第一个common状态，固定在某一块
			commonStates.get(0).setLocation(new Point(90, 40));
			diagram.addState(commonStates.get(0));
			int currentX = 90;

			for (int j = 1; j < commonStates.size(); j++) {
				commonStates.get(j).setLocation(
						new Point(currentX = (currentX + 8 * lenList.get(j - 1)),
								40));
				diagram.addState(commonStates.get(j));
			}

			int maxLength = currentX - 90;

			// 设置非普通状态的位置
			for (int k = 0; k < badStates.size(); k++) {
				State target = badStates.get(k);
				// for(int s = 0 ; s<transitions.size();s++){
				// if(transitions.get(s).getTarget() == target){
				// target.setLocation(new
				// Point(transitions.get(s).getSource().getLocation().x+30,150));
				// }
				// }
				if (badStates.size() == 1) {
					target.setLocation(new Point(150 + maxLength / 2, 150));
				} else {
					target.setLocation(new Point(150 + maxLength * k
							/ (badStates.size() - 1), 150));
				}
				diagram.addState(badStates.get(k));
			}

			// ////////////////////////////////////////////////////
			
			Text text = ((AutomataView) view).getTextArray().get(index);
			text.setText("");
			Group group = ((AutomataView) view).getZgraphGroupArray().get(index);
			Composite composite1 = new Composite(group, SWT.NONE);
			composite1.setLayout(new FillLayout());

			ScrollingGraphicalViewer graphicalViewer = new ScrollingGraphicalViewer();
			FigureCanvas canvas = (FigureCanvas) graphicalViewer.createControl(composite1);
			canvas.setBackground(ColorConstants.white);
			// composite.setBackground(ColorConstants.white);
			ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();
			graphicalViewer.setRootEditPart(root);
			graphicalViewer.setEditDomain(new EditDomain());
			graphicalViewer.setEditPartFactory(new AutomataEditPartFactory());
			graphicalViewer.setContents(diagram);
			group.layout();
			compositeArray.add(composite1);
			// graphicalViewer.
			// ////////////////////////////////////////////////////////////////////s

			text.setText("");
			for (int i = 0; i < refinementAutomataList.size(); i++) {
				text.append(refinementAutomataList.get(i) + "\n");
			}
//			if( window.getActivePage().getActiveEditor() != null)
//			{
//				IFile automataFile = ((IFileEditorInput) window.getActivePage()
//						.getActiveEditor().getEditorInput()).getFile();
//				String automataFileName = automataFile.getLocation().toString();
//				automataFileName = automataFileName.substring(0, automataFileName
//						.lastIndexOf("."))
//						+ ".auto";
//				refineFileList.add(automataFileName);			
//				System.out.println(refineFileList.size());			
//			}
//			String automataFileName = IImageKeys.automataFilesName.get(index);
//			if( automataFileName.contains("_unrefined"))
//			{
//				String pre = automataFileName.substring(0,automataFileName.indexOf("_unrefined"));
//				automataFileName = pre + ".auto";
//				IImageKeys.automataFilesName.
//			}

			PrintWriter out = null;
			try {
				out = new PrintWriter(new FileWriter(IImageKeys.automataFilesName.get(index)));
				for (int i = 0; i < refinementAutomataList.size(); i++) {
					out.println(refinementAutomataList.get(i));
				}
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
		
		//((AutomataView) view).getTabFolder().layout();
		
		
//		for(int i = 0; i < refineFileList.size(); i++)
//		{
//			System.out.println(refineFileList.get(i));			
//		}
//		
//		
//		if( refineFileList.size() > 0)
//		{
//			IImageKeys.automataFilesName.clear();
//			IImageKeys.automataFilesName.addAll(refineFileList);
//		}
		
		long endTime=System.currentTimeMillis(); //获取结束时间  
		System.out.println("Time for Automate refinement is "+( endTime - startTime )+"ms"); 

	}

	private State Contains(List<State> states, String start) {
		// TODO Auto-generated method stub
		for (int i = 0; i < states.size(); i++) {
			if (states.get(i).getName().equals(start)) {
				return states.get(i);
			}
		}
		return null;
	}

	/**
	 * @param automataList
	 */
	public List<String> Refinement(List<String> automataList) {
		// TODO Auto-generated method stub
		List<String> refinementAutomataList = new ArrayList<String>();
		List<Integer> acceptIndex = new ArrayList<Integer>();
		List<Integer> glueIndex = new ArrayList<Integer>();
		List<Integer> errorIndex = new ArrayList<Integer>();

		for (int i = 0; i < automataList.size(); i++) {
			if (automataList.get(i).contains("Accept")) {
				acceptIndex.add(new Integer(i));
			}			
			if (automataList.get(i).contains("Glue")) {
				glueIndex.add(new Integer(i));
			}
			if (automataList.get(i).contains("Error")) {
				errorIndex.add(new Integer(i));
			}
		}
		
		if( acceptIndex.size() > 0)
		{
			for (int i = 0; i < automataList.size(); i++) 
			{
				refinementAutomataList.add(automataList.get(i));
			}
			return refinementAutomataList;
		}

		String errorReplace = "";
		if (errorIndex.size() > 0) {
			String temp = automataList.get(errorIndex
					.get(errorIndex.size() - 1));
			errorReplace = temp.substring(temp.lastIndexOf(",") + 1);
		}

		int lastGlueIndex = -1;
		if (glueIndex.size() > 0) {
			lastGlueIndex = glueIndex.get(glueIndex.size() - 1);
		}
		for (int i = 0; i < automataList.size(); i++) {
			String automataTemp = automataList.get(i);
			if (automataTemp.contains("Glue")) {
				if (i == lastGlueIndex) {
					int index = automataTemp.indexOf("Glue");
					if (index > 0) {
						automataTemp = automataTemp.substring(0, index - 1)
								+ "(Accept))";
					}
				} else {
					int index = automataTemp.indexOf("Glue");
					if (index > 0) {
						automataTemp = automataTemp.substring(0, index - 1)
								+ ")";
					}
				}
				refinementAutomataList.add(automataTemp);
			} else if (automataTemp.contains("Error")) {
				int index = automataTemp.lastIndexOf(",");
				if (!automataTemp.substring(index + 1).equals(errorReplace)) {
					automataTemp = automataTemp.substring(0, index + 1)
							+ errorReplace;
				}
				refinementAutomataList.add(automataTemp);
			} else {
				refinementAutomataList.add(automataTemp);
			}
		}
		return refinementAutomataList;
	}
}
