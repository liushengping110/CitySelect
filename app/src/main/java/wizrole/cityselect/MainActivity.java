package wizrole.cityselect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import wizrole.cityselect.adapter.CityAdapter;
import wizrole.cityselect.adapter.SortAdapter;
import wizrole.cityselect.infor.C;
import wizrole.cityselect.infor.Country;
import wizrole.cityselect.infor.Data;
import wizrole.cityselect.view.CharacterParser;
import wizrole.cityselect.view.PinyinComparator;
import wizrole.cityselect.view.SideBar;

/**
 * Created by liushengping on 2016/10/7.
 * 何人执笔？
 * 所有城市快速筛选
 */

public class MainActivity extends AppCompatActivity {

    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter cityAdapter;
    private CharacterParser characterParser;
    private Toolbar toolbar;
    private String CitysonStr;
    public InputStream inputStream;
    public List<C> cList;
    public TextView text_all;//省市县选择按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置ActionBar
        initUI();
        //获取城市列表数据
        initData();
        setListener();
    }
    public void initUI(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("选择城市");
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        text_all=(TextView)findViewById(R.id.text_all);
        characterParser = CharacterParser.getInstance();
        sideBar.setTextView(dialog);
    }

    public List<Data> datas;//省数据源
    public Country country;
    public void initData(){
        inputStream = getResources().openRawResource(R.raw.contury_city);//获取本地json文件
        CitysonStr=getMenuString(inputStream);//转化输入流
        Gson gson=new Gson();
        country=gson.fromJson(CitysonStr,Country.class);
            //得出数据源
        datas=country.getData();
        cs=new ArrayList<C>();
        for(int i=0;i<datas.size();i++){
            cList=new ArrayList<C>();
            Data data=datas.get(i);
            cList=data.getC();
            cs.addAll(cList);
        }
        cs = filledData(cs);
        Collections.sort(cs, new PinyinComparator());
        cityAdapter  = new SortAdapter(this, cs);
        sortListView.addHeaderView(initHeadView());
        sortListView.setAdapter(cityAdapter);
    }
    /**
     * 添加列表头部---只添加一次W（最外层添加）
     * @return
     */
    private View initHeadView() {
        View headView = getLayoutInflater().inflate(R.layout.item_select_city, null);
        TextView tv_catagory = (TextView) headView.findViewById(R.id.tv_catagory);
        TextView tv_city_name = (TextView) headView.findViewById(R.id.tv_city_name);
        tv_catagory.setText("自动定位");
        tv_city_name.setText("北京");
        tv_city_name.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_city_location), null, null, null);
        tv_city_name.setCompoundDrawablePadding(24);
        return headView;
    }

    public List<C> cs;//当前对应省级下市级列表集合
    public void setListener() {
        /***右侧快速筛选监听*/
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = cityAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position + 1);
                }
            }
        });
        /**左侧列表监听**/
        sortListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if((position-1)<0){//定位市级
                    Toast.makeText(MainActivity.this,"您点击了列表头部", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,cs.get(position-1).getN(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        //省市县选择
        text_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AllSelectActivity.class);
                startActivity(intent);
            }
        });
    }
    /**
     * 本地文件转化为json字符串
     * @param inputStream
     * @return
     */
    public  String getMenuString(InputStream inputStream) {
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        int len=0;
        byte[] data=new byte[409600];
        try {
            while((len=inputStream.read(data))!=-1){
                outputStream.write(data, 0, len);
            }
            inputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new String(outputStream.toByteArray());
    }
    /***市级*/
    public List<C> filledData(List<C> date) {
        List<C> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();
        for (int i = 0; i < date.size(); i++) {
            C sortModel = new C();
            sortModel.setN(date.get(i).getN());
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(date.get(i).getN());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                //对重庆多音字做特殊处理
                if (pinyin.startsWith("zhongqing")) {
                    sortString = "C";
                    sortModel.setSortLetters("C");
                } else if (pinyin.startsWith("shamen")){//厦门
                    sortString = "X";
                    sortModel.setSortLetters("X");
                }else{
                    sortModel.setSortLetters(sortString.toUpperCase());
                }
                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            }
            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
        sideBar.setIndexText(indexString);
        return mSortList;
    }
}
