package cn.cstv.wspscm.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class JFreeChartView extends ViewPart {

	public static final String ID = "cn.cstv.wspscm.views.JFreeChartView";

	private Composite parent;

	public void setParent(Composite parent) {
		this.parent = parent;
	}

	public Composite getParent() {
		return parent;
	}

	@Override
	public void createPartControl(Composite parent) {
		this.setParent(parent);
//		JFreeChart chart = createChart(createDataset());
//		new ChartComposite(parent, SWT.NONE,
//				chart, true);
	}

	// /**
	// * Creates the Dataset for the Pie chart
	// */
	// private XYDataset createDataset() {
	// XYSeries xyseries1 = new XYSeries("One");
	// xyseries1.add(1987, 50);
	// xyseries1.add(1997, 20);
	// xyseries1.add(2007, 30);
	//	
	// XYSeries xyseries2 = new XYSeries("Two");
	// xyseries2.add(1987, 20);
	// xyseries2.add(1997, 10D);
	// xyseries2.add(2007, 40D);
	//	
	// XYSeries xyseries3 = new XYSeries("Three");
	// xyseries3.add(1987, 40);
	// xyseries3.add(1997, 30.0008);
	// xyseries3.add(2007, 38.24);
	//	
	// XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
	//	
	// xySeriesCollection.addSeries(xyseries1);
	// xySeriesCollection.addSeries(xyseries2);
	// xySeriesCollection.addSeries(xyseries3);
	//	
	// return xySeriesCollection;
	// }
	//	
	// /**
	// * Creates the Chart based on a dataset
	// */
	// private JFreeChart createChart(XYDataset dataset) {
	//	
	// JFreeChart chart = ChartFactory.createXYLineChart("XYLine Chart Demo", //
	// // title
	// "Labels", // domain axis label
	// "Values", // range axis label
	// dataset, // data
	// PlotOrientation.VERTICAL, // orientation
	// true, // include legend
	// false, // tooltips?
	// false // URLs?
	// );
	//	
	// // CategoryPlot plot = (CategoryPlot) chart.getPlot();
	// // plot.setBackgroundPaint(Color.lightGray);
	// // plot.setDomainGridlinePaint(Color.white);
	// // plot.setDomainGridlinesVisible(true);
	// // plot.setRangeGridlinePaint(Color.white);
	// XYPlot plot = (XYPlot) chart.getPlot();
	// // 背景色 透明度
	// plot.setBackgroundAlpha(0.0f);
	// // 前景色 透明度
	// plot.setForegroundAlpha(0.9f);
	// // 其它设置可以参考XYPlot类
	// return chart;
	//	
	// }

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}
