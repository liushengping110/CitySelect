package wizrole.cityselect.infor;

import java.util.List;

/**
 * Created by liushengping on 2016/10/7.
 * 何人执笔？
 */

public class Data {


    public String p;//省
    private String sortLetters;
    public List<C> c;//市

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public List<C> getC() {
        return c;
    }

    public void setC(List<C> c) {
        this.c = c;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }
}
