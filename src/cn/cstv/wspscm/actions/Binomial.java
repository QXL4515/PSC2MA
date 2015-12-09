package cn.cstv.wspscm.actions;

public class Binomial {

	public Binomial() {

	}

	/**
	 * Binomial coefficient n over k.
	 * 
	 * @param n
	 *            Non-negative integer
	 * @param k
	 *            0 <= k <= n
	 */
//	public static int binom(int n, int k) {
//		int rtval = 1;
//		for (int i = 1; i <= k; i++) {
//			rtval = rtval * (n - i + 1) / (i);
//		}
//		return rtval;
//	}

	/**
	 * Binomial coefficient n over k. Long integer results allow wider range of
	 * n and k.
	 * 
	 * @param n
	 *            Non-negative integer
	 * @param k
	 *            0 <= k <= n
	 */
	public static double binomL(int n, int k) {
		double rtval = 1;
		for (int i = 1; i <= k; i++) {
			rtval = rtval * ((double)(n - i + 1)) / ((double)(n-k-i+1));
		}
		return rtval;
	}

	/**
	 * Returns the probability of k successes in n trials given the probability
	 * of one success is p.
	 * 
	 * @param n
	 *            Non-negative
	 * @param k
	 *            0 <= k <= n
	 * @param p
	 *            0.0 <= p <= 1.0
	 * @return Probability of k successes in n trials, probability of one
	 *         success is p
	 */
	public static double binomProb(int n, int k, double p) {
		double rtval = 1;
		for (int i = 0; i < k; i++) {
			rtval *= p;
		}
		for (int i = k; i < n; i++) {
			rtval *= (1.0 - p);
		}
		return rtval * binomL(n, k);
	}

	/**
	 * Returns the probability of k successes in n trials given the probability
	 * of one success is p. This turns out to be more accurate than binomProb.
	 * 
	 * @param n
	 *            Non-negative
	 * @param k
	 *            0 <= k <= n
	 * @param p
	 *            0.0 <= p <= 1.0
	 * @return Probability of k successes in n trials, probability of one
	 *         success is p
	 */
//	public static double binomProb2(int n, int k, double p) {
//		double rtval = 1;
//		for (int i = 1; i <= k; i++) {
//			rtval *= p * (n - i + 1) / i;
//		}
//		for (int i = k + 1; i <= n; i++) {
//			rtval *= (1.0 - p);
//		}
//		return rtval;
//	}

	/**
	 * Returns the probability of k or fewer successes in n binomial trials. p
	 * is the probability of a success in one trial.
	 * 
	 * @param n
	 *            Number of binomial trials. Must be at least 0.
	 * @param c
	 *            Maximum number of successes. Must be non-negative and no
	 *            bigger than n.
	 * @param p
	 *            The probability of success in one trial. Must be betwee 0.0
	 *            and 1.0, inclusive.
	 * @return The probability of k or fewer successes in n trials where p is
	 *         the probability of success in one trial.
	 */
	public static double cumulativeBinomProb2(int n, int c, double p) {
		double rtval = 0.0;
		for (int i = 0; i <= c; i++) {
			rtval += binomProb(n, i, p);
		}
		return rtval;
	}

	public static int invcumulativeBinomProb2(double y, int n, double p) {
		double dSum = 0.0;
		int iRet;
		for (iRet = 0; iRet <= n; iRet++) {
			dSum += binomProb(n, iRet, p);
			if (dSum >= y)
				return iRet;
		}
		return iRet;
//		return (int)(2/(cumulativeBinomProb2((int)y,n,p)+cumulativeBinomProb2((int)y+1,n,p)));
	}

}
