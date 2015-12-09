package cn.cstv.wspscm.actions;

import java.util.Comparator;

import cn.cstv.wspscm.model.LineConnection;

/**
 * @author hp
 *
 */
public class LineConnectionComparator<T> implements Comparator<T> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(T o1, T o2) {
		// TODO Auto-generated method stub
		if(o1.equals(o2))return 0;
		return ((LineConnection)o1).getSourceOffset()>((LineConnection)o2).getSourceOffset()?1:-1;
//		return ((Comparable<T>) o1).compareTo(o2) * -1;
	}

}
