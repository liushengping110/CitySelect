package wizrole.cityselect.infor;

import java.util.List;

/**
 * Created by liushengping on 2016/10/7.
 * 何人执笔？
 */

public class C {

    public String n;//市
    private String sortLetters;
    public List<A> a;//区

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public List<A> getA() {
        return a;
    }

    public void setA(List<A> a) {
        this.a = a;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }
}
