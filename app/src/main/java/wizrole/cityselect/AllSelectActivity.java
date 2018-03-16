package wizrole.cityselect;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import wizrole.cityselect.adapter.AreaAdapter;
import wizrole.cityselect.adapter.CityAdapter;
import wizrole.cityselect.adapter.ProvinceAdapter;
import wizrole.cityselect.infor.A;
import wizrole.cityselect.infor.C;
import wizrole.cityselect.infor.Country;
import wizrole.cityselect.infor.Data;

/**
 * Created by liushengping on 2016/10/7.
 * 何人执笔？
 * 省市区逐一选择
 */

public class AllSelectActivity extends AppCompatActivity{

    public Toolbar toolbar;
    public ListView list_pro;
    public ListView list_city;
    public ListView list_area;
    public InputStream inputStream;
    private String CitysonStr;
    public Country country;
    public List<Data> datas;
    public TextView text_back;
    public TextView text_title;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allsel);
        ininUI();
        setData();
        setListener();
    }

    public void ininUI(){
        text_back = (TextView) findViewById(R.id.text_back);
        text_title = (TextView) findViewById(R.id.text_title);
        text_title.setText("选择省份");
        list_pro=(ListView)findViewById(R.id.all_provece);
        list_city=(ListView)findViewById(R.id.all_city);
        list_area=(ListView)findViewById(R.id.all_area);
    }
    public void setData(){
        inputStream = getResources().openRawResource(R.raw.contury_city);//获取本地json文件
        CitysonStr=getMenuString(inputStream);//转化输入流
        Gson gson=new Gson();
        country=gson.fromJson(CitysonStr,Country.class);
        //得出数据源
        datas=country.getData();
        //默认初始化设置省级适配器
        provinceAdapter=new ProvinceAdapter(this,datas);
        list_pro.setAdapter(provinceAdapter);
    }

    public boolean pro_sel=true;//【默认】省份选中标记
    public boolean city_sel=false;//城市选中标记
    public boolean area_sel=false;//区县选中标记
    public  List<C> cs;//城市列表集合
    public  List<A> as;//城市列表集合
    public String provice;//获取到省份
    public String city;//获取到市
    public String area;//获取到县区
    public ProvinceAdapter provinceAdapter;
    public CityAdapter cityAdapter;
    public AreaAdapter areaAdapter;
    public void setListener(){
        //返回按钮--要切换三层列表
        text_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(area_sel){
                    list_city.setVisibility(View.VISIBLE);
                    list_pro.setVisibility(View.INVISIBLE);
                    list_area.setVisibility(View.INVISIBLE);
                    area_sel=false;
                }else if(city_sel){
                    list_pro.setVisibility(View.VISIBLE);
                    list_city.setVisibility(View.INVISIBLE);
                    list_area.setVisibility(View.INVISIBLE);
                    city_sel=false;
                }else if(pro_sel){
                    finish();
                }else{
                    finish();
                }
            }
        });
        //省份item点击
        list_pro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    city_sel=true;
                    text_title.setText("选择城市");
                    Data data = datas.get(i);
                    provice = data.getP();//获取到点击的省份
                    cs = data.getC();
                    list_pro.setVisibility(View.INVISIBLE);
                    list_area.setVisibility(View.INVISIBLE);
                    list_city.setVisibility(View.VISIBLE);
                    cityAdapter = new CityAdapter(AllSelectActivity.this, cs);
                    list_city.setAdapter(cityAdapter);
            }
        });
        //城市列表点击
        list_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                area_sel=true;
                text_title.setText("选择县区");
                C c=cs.get(i);
                city=c.getN();//获取点击到的市
                as=c.getA();
                list_pro.setVisibility(View.INVISIBLE);
                list_city.setVisibility(View.INVISIBLE);
                list_area.setVisibility(View.VISIBLE);
                areaAdapter=new AreaAdapter(AllSelectActivity.this,as);
                list_area.setAdapter(areaAdapter);
            }
        });
        //区域列表监听
        list_area.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                A a=as.get(i);
                area=a.getS();//获取点击的区
                finish();
                Toast.makeText(AllSelectActivity.this,provice+city+area,Toast.LENGTH_SHORT).show();
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
}
