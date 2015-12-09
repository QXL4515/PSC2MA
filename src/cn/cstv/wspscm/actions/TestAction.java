package cn.cstv.wspscm.actions;

import java.text.DecimalFormat;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;

import cn.cstv.wspscm.IImageKeys;
import cn.cstv.wspscm.monitor.AnalyzeMessageSequence;
import cn.cstv.wspscm.views.JFreeChartView;
import cn.cstv.wspscm.views.SWTResourceManager;


public class TestAction extends Action implements ISelectionListener,
		IWorkbenchAction {
	public static final String ID = "cn.cstv.wspscm.actions.TestAction";

	private IWorkbenchWindow window;
	private Shell shell;
	private Text text_3;
	private Text text_2;
	private Text text_1;
	private Text text;
	private Button finishButton;
	private double p0Decimal;
	private double p1Decimal;
	private double alphaDecimal;
	private double betaDecimal;

	private Parameters p;

	public TestAction() {

	}

	public TestAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("Test");
		setToolTipText("Sequential Probability Ratio Test");
		setImageDescriptor(IImageKeys.getImageDescriptor(IImageKeys.TEST));
		window.getSelectionService().addSelectionListener(this);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		window.getSelectionService().removeSelectionListener(this);
	}

	class Parameters {
		int n;
		int c;

		public Parameters() {
		}

		public int getN() {
			return n;
		}

		public void setN(int n) {
			this.n = n;
		}

		public int getC() {
			return c;
		}

		public void setC(int c) {
			this.c = c;
		}
	}

	@Override
	public void run() {

		shell = new Shell(window.getShell(), SWT.MIN | SWT.MAX | SWT.CLOSE);
		shell.setSize(369, 273);
		shell.setText("Sequential Probability Ratio Test");

		final Composite composite = new Composite(shell, SWT.NONE);
		composite.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_WHITE));
		composite.setBounds(0, 0, 364, 64);

		final Label testLabel = new Label(composite, SWT.NONE);
		testLabel.setFont(SWTResourceManager.getFont("", 12, SWT.NONE));
		testLabel.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_WHITE));
		testLabel.setText("Sequential Probability Ratio Test");
		testLabel.setBounds(10, 10, 272, 22);

		final Label inputLabel = new Label(composite, SWT.NONE);
		inputLabel.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_WHITE));
		inputLabel
				.setText("Please input some parameters with restrictive conditions");
		inputLabel.setBounds(20, 38, 344, 12);

		final Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setBounds(0, 68, 364, 147);

		final Label p0Label = new Label(composite_1, SWT.NONE);
		p0Label.setText("p0:");
		p0Label.setBounds(10, 15, 18, 12);

		final Label p1Label = new Label(composite_1, SWT.NONE);
		p1Label.setText("p1:");
		p1Label.setBounds(10, 45, 18, 12);

		final Label alphaLabel = new Label(composite_1, SWT.NONE);
		alphaLabel.setText("α:");
		alphaLabel.setBounds(10, 80, 18, 12);

		final Label betaLabel = new Label(composite_1, SWT.NONE);
		betaLabel.setText("β:");
		betaLabel.setBounds(10, 115, 18, 12);

		text = new Text(composite_1, SWT.BORDER);
		text.setBounds(30, 10, 111, 20);

		text_1 = new Text(composite_1, SWT.BORDER);
		text_1.setBounds(30, 40, 111, 20);

		text_2 = new Text(composite_1, SWT.BORDER);
		text_2.setBounds(30, 75, 111, 20);

		text_3 = new Text(composite_1, SWT.BORDER);
		text_3.setBounds(30, 110, 111, 20);

		addNumberValidateToolTip(text);
		addNumberValidateToolTip(text_1);
		addNumberValidateToolTip(text_2);
		addNumberValidateToolTip(text_3);

		finishButton = new Button(shell, SWT.NONE);
		finishButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				p0Decimal = Double.parseDouble(text.getText());
				p1Decimal = Double.parseDouble(text_1.getText());
				alphaDecimal = Double.parseDouble(text_2.getText());
				betaDecimal = Double.parseDouble(text_3.getText());
				long currentTime1 = System.currentTimeMillis();
				SequentialProbabilityRatioTest(p0Decimal, p1Decimal,
						alphaDecimal, betaDecimal);
				long currentTime2 = System.currentTimeMillis();
				double propertyTime = currentTime2 - currentTime1;
				System.out.println();
				System.out.println("The execution time of test is: "
						+ propertyTime + "ms");
				System.out.println();
				{
					IViewPart view = window.getActivePage().findView(
							JFreeChartView.ID);

					XYSeries xyseries1 = new XYSeries("m-dm");
					// XYSeries xyseries2 = new XYSeries("H0");
					// XYSeries xyseries3 = new XYSeries("H1");

					// System.out.println(" c=" + p.getC() + " , n=" +
					// p.getN());
					// p.setC(12);
					// p.setN(30);
					xyseries1.add(0, 0);
					// xyseries2.add(0, p.getC());
					// if ((p.getC() - p.getN()) >= 0) {
					// xyseries3.add(0, p.getC() - p.getN());
					// }

					List<Boolean> sequence = AnalyzeMessageSequence.sequentialObservation;
					int no = sequence.size();
					int temp = 0;

					for (int i = 0; i < no; i++) {
						if (sequence.get(i)) {
							++temp;
						}
						if (i == (no - 1)) {
							xyseries1.add(i + 1, temp);
							// xyseries2.add(i + 1, p.getC());
							// if ((i + 1 + p.getC() - p.getN()) >= 0) {
							// xyseries3.add(i + 1, i + 1 + p.getC()
							// - p.getN());
							// }
						} else {
							xyseries1.add(i + 1, temp);
							// xyseries2.add(i + 1, p.getC());
							// if ((i + 1 + p.getC() - p.getN()) >= 0) {
							// xyseries3.add(i + 1, i + 1 + p.getC()
							// - p.getN());
							// }
						}
					}

					// XYSeries xyseries2 = new XYSeries("Two");
					// xyseries2.add(1987, 20);
					// xyseries2.add(1997, 10D);
					// xyseries2.add(2007, 40D);
					//
					// XYSeries xyseries3 = new XYSeries("Three");
					// xyseries3.add(1987, 40);
					// xyseries3.add(1997, 30.0008);
					// xyseries3.add(2007, 38.24);

					XYSeriesCollection xySeriesCollection = new XYSeriesCollection();

					xySeriesCollection.addSeries(xyseries1);
					// xySeriesCollection.addSeries(xyseries2);
					// xySeriesCollection.addSeries(xyseries3);

					JFreeChart chart = ChartFactory.createXYLineChart(
							"m-dm Distribution Chart", // chart
							// title
							"m", // domain axis label
							"dm", // range axis label
							xySeriesCollection, // data
							PlotOrientation.VERTICAL, // orientation
							true, // include legend
							false, // tooltips?
							false // URLs?
							);

					// CategoryPlot plot = (CategoryPlot) chart.getPlot();
					// plot.setBackgroundPaint(Color.lightGray);
					// plot.setDomainGridlinePaint(Color.white);
					// plot.setDomainGridlinesVisible(true);
					// plot.setRangeGridlinePaint(Color.white);
					XYPlot plot = (XYPlot) chart.getPlot();
					// 背景色 透明度
					plot.setBackgroundAlpha(0.1f);
					// 前景色 透明度
					plot.setForegroundAlpha(0.9f);

					NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();

					DecimalFormat decimalFormat = new DecimalFormat("0");

					numberaxis.setNumberFormatOverride(decimalFormat);

					new ChartComposite(((JFreeChartView) view).getParent(),
							SWT.NONE, chart, true);
				}

				shell.close();

			}
		});
		finishButton.setEnabled(false);
		finishButton.setText("Finish");
		finishButton.setBounds(254, 221, 48, 22);
		if (!text.getText().isEmpty() && !text_1.getText().isEmpty()
				&& !text_2.getText().isEmpty() && !text_3.getText().isEmpty()) {
			finishButton.setEnabled(true);
		}

		final Button cannelButton = new Button(shell, SWT.NONE);
		cannelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				shell.dispose();
			}
		});
		cannelButton.setText("Cannel");
		cannelButton.setBounds(308, 221, 48, 22);
		shell.open();
		shell.layout();
	}

	protected boolean SequentialProbabilityRatioTest(double p0Decimal2,
			double p1Decimal2, double alphaDecimal2, double betaDecimal2) {
		// TODO Auto-generated method stub
		System.out.println();
		System.out
				.println("The procedure of sequential probability ratio test:");
		System.out.println("p0=" + p0Decimal2 + " , p1=" + p1Decimal2 + " , α="
				+ alphaDecimal2 + " , β=" + betaDecimal2);
		List<Boolean> sequence = AnalyzeMessageSequence.sequentialObservation;
		int no = sequence.size();
		int temp = 0;
		double[] x = new double[no];
		int[] mdm = new int[no];
		System.out.println();
		System.out.println("The sequential m and dm as list below:");
		System.out.println("m:");
		System.out.println("0");

		for (int i = 0; i < no; i++) {
			System.out.println((i + 1));
		}

		System.out.println();
		System.out.println("dm:");
		System.out.println("0");
		for (int i = 0; i < no; i++) {
			if (sequence.get(i)) {
				mdm[i] = temp;
				temp++;
				x[i] = 1;
			} else {
				mdm[i] = temp;
				x[i] = 0;
			}
			System.out.println(temp);
		}
		// p0Decimal2 = 0.98;
		// p1Decimal2 = 0.80;
		// alphaDecimal2 = 0.05;
		// betaDecimal2 = 0.2;
		// double y[]={1,1,1,1,0,1,1,1,1,1,1,1,0,1,1,1,1,0};
		// no = y.length;

		System.out.println();
		if (p0Decimal2 == 1 || p1Decimal2 == 0) {
			return SimpleSquentialTest(p0Decimal2, p1Decimal2, alphaDecimal2,
					betaDecimal2);
		} else if (p0Decimal2 < 1 && p1Decimal2 > 0) {
			double[] f = new double[no+1];
			f[0] = 0.0;
			double min = Math.log10(betaDecimal2 / (1 - alphaDecimal2));
			double max = Math.log10((1 - betaDecimal2) / alphaDecimal2);
			double p1p0 = Math.log10(p1Decimal2 / p0Decimal2);
			double reverseP1p0 = Math.log10((1.0 - p1Decimal2)
					/ (1.0 - p0Decimal2));
			System.out.println("min=" + min + ",max=" + max + ",p1p0=" + p1p0
					+ ",reverseP1p0=" + reverseP1p0);
			int n = 0;
			while (min < f[n] && f[n] < max) {
				System.out.println("n=" + n + "  , dm=" + mdm[n]);
				n++;
				if (n == no)
					break;
				f[n] = f[n - 1] + x[n] * p1p0 + (1 - x[n]) * reverseP1p0;
				// f[n] = f[n - 1] + y[n] * p1p0 + (1 - y[n]) * reverseP1p0;
			}
			System.out.println("==============================");
			if (n == 0) {
				System.out.println("n=0 , dm=0");
			}
			if (n < no && n > 0) {
				System.out.println("n=" + n + "  , dm=" + mdm[n]);
			} else {
				System.out.println("n=" + n );
			}

			System.out
					.print("Result of sequential probability ratio test is: ");
			if (n<no&&f[n] <= min) {
				System.out.println("The hypothesis H0(p>=" + p0Decimal2
						+ ") is accepted.");
				return true;
			} else if (n<no&&f[n] >= max) {
				System.out.println("The hypothesis H1(p<=" + p1Decimal2
						+ ") is accepted.");
				return true;
			} else {
				System.out.println("H0 and H1 are both not accepted!");
				return false;
			}
		}
		return false;

	}

	private boolean SimpleSquentialTest(double p0Decimal2, double p1Decimal2,
			double alphaDecimal2, double betaDecimal2) {
		// TODO Auto-generated method stu
		p = (new TestAction()).SingleSamplingPlan(p0Decimal2,p1Decimal2,alphaDecimal2, betaDecimal2);
		int n = p.getN();
		int c = p.getC();
		int m = 0;
		int dm = 0;

		List<Boolean> sequence = AnalyzeMessageSequence.sequentialObservation;
		int no = sequence.size();
		int[] x = new int[no];

		for (int i = 0; i < no; i++) {
			if (sequence.get(i)) {
				x[i] = 1;
			} else {
				x[i] = 0;
			}
		}
		while ((dm <= c) && ((dm + n - m) > c) && m < no) {
			m = m + 1;
			dm = dm + x[m];
		}
		if (m < no && (dm > c)) {
			System.out.println("The hypothesis H0(p>=" + p0Decimal2
					+ ") is accepted.");
			return true;
		} else if (m < no && ((dm + n - m) <= c)) {
			System.out.println("The hypothesis H1(p<=" + p1Decimal2
					+ ") is accepted.");
			return true;
		} else {
			System.out.println("H0 and H1 are both not accepted!");
			return false;
		}
	}

	public static void main(String[] args) {
		Parameters pp = (new TestAction()).SingleSamplingPlan(0.5, 0.3, 0.2,
				0.1);
		System.out.println(pp.getN() + "  " + pp.getC());
	}

	private Parameters SingleSamplingPlan(double dp0, double dp1,
			double dAlpha, double dBeta) {
		// TODO Auto-generated method stub
		Parameters parameters = new Parameters();
		int iNmin = 1;
		int iNmax = -1;
		int iN = iNmin;

		while ((iNmax < 0) || (iNmin < iNmax)) {
			int ix0 = Binomial.invcumulativeBinomProb2(dAlpha, iN, dp0);
			int ix1 = Binomial.invcumulativeBinomProb2((1 - dBeta), iN, dp1);
			if ((ix0 >= ix1) && (ix0 >= 0))
				iNmax = iN;
			else
				iNmin = iN + 1;

			if (iNmax < 0)
				iN = 2 * iN;
			else
				iN = ((iNmax + iNmin) / 2);

			// System.out.println("nMax =" + iNmax + " nMin=" + iNmin);
		}
		iN = iNmax - 1;

		int iC0 = 0;
		int iC1 = 0;
		do {
			iN = iN + 1;

			iC0 = Binomial.invcumulativeBinomProb2(dAlpha, iN, dp0);
			iC1 = Binomial.invcumulativeBinomProb2((1 - dBeta), iN, dp1) + 1;
			// System.out.println("N =" + iN + " C0=" + iC0 + " C1=" + iC1);
		} while (iC0 < iC1);
		parameters.setC(iN);
		parameters.setN((iC0 + iC1) / 2);
		return parameters;
	}

	private void addNumberValidateToolTip(final Text text) {
		// TODO Auto-generated method stub
		text.addKeyListener(new KeyListener() {
			/**
			 * Display a tip about detail info of this picture when mouse hover
			 * on.
			 */
			final ToolTip errorInfoTip = new ToolTip(text.getShell(),
					SWT.BALLOON | SWT.ICON_ERROR);
			{
				errorInfoTip.setText("Invalid decimal");
				errorInfoTip.setAutoHide(true);
			}

			public void keyPressed(KeyEvent e) {
			}

			/**
			 * When key released, will check for filename.
			 */
			public void keyReleased(KeyEvent e) {
				try {
					double decimal = Double.parseDouble(text.getText());
					if (decimal < 0 || decimal > 1) {
						errorInfoTip
								.setMessage("Error: Please input decimal value between 0 and 1");
						errorInfoTip.setVisible(true);
						finishButton.setEnabled(false);
					} else {
						finishButton.setEnabled(true);
					}
				} catch (java.lang.NumberFormatException e1) {
					// return "Error: Please input numerical value";
					errorInfoTip
							.setMessage("Error: Please input decimal value between 0 and 1");
					errorInfoTip.setVisible(true);
					finishButton.setEnabled(false);
				}
			}

		});
	}

}
