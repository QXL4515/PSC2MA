package cn.cstv.wspscm.tests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import cn.cstv.wspscm.actions.Binomial;

public class ProbabilityTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 测试的次数
		int times = 100;
		// 所有结果（包括error，accept）的总个数
		int number = 1000;
		// 四个参数，用于测试
		double p0 = 0.89;
		double p1 = 0.84;
		double alpha = 0.05;
		double beta = 0.2;


		List<Integer> sequence = new ArrayList<Integer>();
		for (int j = 0; j < number; j++) {// 结果0-1初始化
			sequence.add(j, 1);
		}
		
		List<Double> percentList = new ArrayList<Double>();
		List<Integer> H0NumberList = new ArrayList<Integer>();
		List<Integer> H1NumberList = new ArrayList<Integer>();
		List<Double> mNumberList = new ArrayList<Double>();
		for(int eN = 0;eN<30;eN++){
			// 存储结果
			List<Integer> mArray = new ArrayList<Integer>();
			Double mNumber = 0.0;
			int H0Number = 0;
			int H1Number = 0;

			// 结果中error的比率
			double percent = 0.01*(eN+1);

			int errorNumber = new Double(number * percent).intValue();// 结果中0的个数
			for (int i = 0; i < times; i++) {
				// 结果中0的位置是随机的，产生number内20个随机数
				int temp = errorNumber;
				Random random = new Random();
				int errorIndex = random.nextInt(number);
				// 存储这些error在序列中的位置
				List<Integer> errorArray = new ArrayList<Integer>();
				// 将错误的结果插入结果序列中，为后续测试所用
				while (temp > 0) {
					if (sequence.get(errorIndex) == 1) {
						sequence.set(errorIndex, 0);
						errorArray.add(errorIndex);
						temp--;
						errorIndex = random.nextInt(number);
					} else {
						errorIndex = random.nextInt(number);
					}
				}
				int m = SequentialProbabilityRatioTest(p0, p1, alpha, beta,
						sequence);
				int no = sequence.size();
				if (Math.abs(m) < no) {
					if (m >= 0)
						H0Number++;
					if (m < 0)
						H1Number++;
				}
				mArray.add(Math.abs(m));
				mNumber+=Math.abs(m);

				// 重新初始化为Accept结果
				Iterator<Integer> it = errorArray.iterator();
				while (it.hasNext()) {
					sequence.set(it.next(), 1);
				}
			}
			System.out.printf("%6.2f", percent);
			System.out.printf("%6d",H0Number);
			System.out.printf("%6d",H1Number);
			System.out.printf("%9.2f\n",mNumber/times);
			
			percentList.add(percent);
			H0NumberList.add(H0Number);
			H1NumberList.add(H1Number);
			mNumberList.add(mNumber/times);
		}
		//存在List里，输出
		System.out.println("==========accpet H0=============");
		for(int i = 0; i<H0NumberList.size();i++)
			System.out.println(H0NumberList.get(i));
		System.out.println("==========accpet H1=============");
		for(int i = 0; i<H1NumberList.size();i++)
			System.out.println(H1NumberList.get(i));
		System.out.println("==========m=============");
	for(int i = 0; i<mNumberList.size();i++)
			System.out.println(mNumberList.get(i));

	}

	protected static int SequentialProbabilityRatioTest(double p0Decimal2,
			double p1Decimal2, double alphaDecimal2, double betaDecimal2,
			List<Integer> sequence) {
		int no = sequence.size();
		int temp = 0;
		double[] x = new double[no];
		int[] mdm = new int[no];
		for (int i = 0; i < no; i++) {
			if (sequence.get(i) == 1) {
				mdm[i] = temp;
				temp++;
				x[i] = 1;
			} else {
				mdm[i] = temp;
				x[i] = 0;
			}
		}

		if (p0Decimal2 == 1 || p1Decimal2 == 0) {
			return SimpleSquentialTest(p0Decimal2, p1Decimal2, alphaDecimal2,
					betaDecimal2, sequence);
		} else if (p0Decimal2 < 1 && p1Decimal2 > 0) {
			double[] f = new double[no + 1];
			f[0] = 0.0;
			double min = Math.log10(betaDecimal2 / (1 - alphaDecimal2));
			double max = Math.log10((1 - betaDecimal2) / alphaDecimal2);
			double p1p0 = Math.log10(p1Decimal2 / p0Decimal2);
			double reverseP1p0 = Math.log10((1.0 - p1Decimal2)
					/ (1.0 - p0Decimal2));
			int n = 0;
			while (min < f[n] && f[n] < max) {
				n++;
				if (n == no)
					break;
				f[n] = f[n - 1] + x[n] * p1p0 + (1 - x[n]) * reverseP1p0;
			}


			if (n < no && f[n] <= min) {
				return n;
			} else if (n < no && f[n] >= max) {
				return -n;// 加负号，表明接受error
			} else {
				return no;// 到最后即没有接受H0，也没有接受H1
			}
		}
		return no;
	}

	private static int SimpleSquentialTest(double p0Decimal2,
			double p1Decimal2, double alphaDecimal2, double betaDecimal2,
			List<Integer> sequence) {
		// TODO Auto-generated method stu
		Integer n = 0;
		Integer c = 0;
		SingleSamplingPlan(p0Decimal2, p1Decimal2, alphaDecimal2, betaDecimal2,
				n, c);
		int m = 0;
		int dm = 0;

		int no = sequence.size();
		int[] x = new int[no];

		for (int i = 0; i < no; i++) {
			if (sequence.get(i) == 1) {
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
			return m;
		} else if (m < no && ((dm + n - m) <= c)) {
			return -m;
		} else {
			return no;
		}
	}

	private static void SingleSamplingPlan(double dp0, double dp1,
			double dAlpha, double dBeta, Integer n, Integer c) {
		// TODO Auto-generated method stub
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
		c = iN;
		n = (iC0 + iC1) / 2;
	}

}
