package wizrole.cityselect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.List;

import wizrole.cityselect.R;
import wizrole.cityselect.infor.C;
import wizrole.cityselect.infor.Data;

/**
 * Created by liushengping on 2016/10/7.
 * 何人执笔？
 */

public class CityAdapter extends BaseAdapter{
    private List<C> list = null;
    private Context mContext;

    public CityAdapter(Context mContext, List<C> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {

        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final C mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.allselect_item, null);
            viewHolder.text_pca = (TextView) view.findViewById(R.id.text_pca);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        //设置
        viewHolder.text_pca.setText(this.list.get(position).getN());

        return view;

    }
    final static class ViewHolder {
        TextView text_pca;   //省市区
    }
}