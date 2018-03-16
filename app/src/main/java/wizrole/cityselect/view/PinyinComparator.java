package wizrole.cityselect.view;

import java.util.Comparator;

import wizrole.cityselect.infor.C;

/**
 * Created by liushengping on 2016/10/7.
 * 何人执笔？
 */

public class PinyinComparator implements Comparator<C> {
	//这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
	public int compare(C o1, C o2) {
		if (o1.getSortLetters().equals("@")|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}
}
