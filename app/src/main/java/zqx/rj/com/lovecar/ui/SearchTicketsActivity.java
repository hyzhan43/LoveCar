package zqx.rj.com.lovecar.ui;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.adapter.SearchHistoryAdapter;
import zqx.rj.com.lovecar.entity.SearchHistoryData;
import zqx.rj.com.lovecar.utils.Get;
import zqx.rj.com.lovecar.utils.L;
import zqx.rj.com.lovecar.utils.StaticClass;
import zqx.rj.com.lovecar.utils.T;

/**
 * 项目名：  LoveCar
 * 包名：    zqx.rj.com.lovecar.ui
 * 文件名：  SearchTicketsActivity
 * 创建者：  ZQX
 * 创建时间：2018/4/22 15:07
 * 描述：    搜索车票页面
 */

public class SearchTicketsActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, SearchHistoryAdapter.OnButtonListener {

    private TextView title;
    private Button back;

    private TextView tv_start_place;
    private TextView tv_end_place;
    private TextView tv_time;

    // 历史纪录
    private ListView lv_search_history;
    private SearchHistoryAdapter adapter;
    private List<SearchHistoryData> dataList;
    private List<SearchHistoryData> tempList;

    // 交换上车地点和下车地点
    private ImageView iv_change;
    private Button btn_query;

    private String start;
    private String end;
    private String time;

    // 时间选择器
    private TimePickerView pvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search_tickets);
        super.onCreate(savedInstanceState);

        initActionBar();

        initView();
        initDate();
    }

    private void initActionBar() {
        title = findViewById(R.id.tv_title);
        back = findViewById(R.id.btn_back);
    }

    private void initDate() {
        //时间选择器
        pvTime = new TimePickerBuilder(SearchTicketsActivity.this, new OnTimeListener())
                //分别控制“年”“月”“日”“时”“分”“秒”的显示或隐藏。
                .setType(new boolean[]{false, true, true, true, true, false})
                .setLabel(null, "月", "日", "点", "分", null)
                .setTitleSize(20)//标题文字大小
                .setSubmitColor(getResources().getColor(R.color.color_main))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.color_main))//取消按钮文字颜色
                .setDate(Calendar.getInstance())
                .build();
    }

    private void initView() {
        title.setText(getString(R.string.query));
        back.setOnClickListener(SearchTicketsActivity.this);

        tv_start_place = findViewById(R.id.tv_start_place);
        tv_start_place.setOnClickListener(this);
        tv_start_place.setCursorVisible(false);

        tv_end_place = findViewById(R.id.tv_end_place);
        tv_end_place.setOnClickListener(this);
        tv_end_place.setCursorVisible(false);

        tv_time = findViewById(R.id.tv_time);
        tv_time.setOnClickListener(this);

        iv_change = findViewById(R.id.iv_change);
        iv_change.setOnClickListener(this);
        btn_query = findViewById(R.id.btn_query);
        btn_query.setOnClickListener(this);

        lv_search_history = findViewById(R.id.lv_search_history);

        dataList = new ArrayList<>();
        tempList = DataSupport.findAll(SearchHistoryData.class);
        for (int i = tempList.size() - 1; i >= 0; i--) {
            SearchHistoryData data = tempList.get(i);
            data.setId(i + 1);
            dataList.add(data);
        }

        adapter = new SearchHistoryAdapter(this, dataList);
        lv_search_history.setAdapter(adapter);
        lv_search_history.setOnItemClickListener(this);
        adapter.setButtonListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;

            case R.id.tv_start_place:
                tv_start_place.setCursorVisible(true);
                Intent intent = new Intent(this, InputTipsActivity.class);
                startActivityForResult(intent, StaticClass.REQUEST_START);
                break;
            case R.id.tv_end_place:
                tv_end_place.setCursorVisible(true);
                Intent intent2 = new Intent(this, InputTipsActivity.class);
                startActivityForResult(intent2, StaticClass.REQUEST_END);
                break;
            case R.id.tv_time:
                pvTime.show();
                break;

            case R.id.btn_query:
                Intent intent3 = new Intent(this, SearchResultActivity.class);

                start = tv_start_place.getText().toString();
                end = tv_end_place.getText().toString();
                time = tv_time.getText().toString();

                if (!TextUtils.isEmpty(start) & !TextUtils.isEmpty(end)) {
                    intent3.putExtra("startPlace", start);
                    intent3.putExtra("endPlace", end);
                    intent3.putExtra("time", time);

                    List<SearchHistoryData> nowList = DataSupport.findAll(SearchHistoryData.class);
                    if (nowList.size() >= 5) {
                        // 如果添加到数据库 大于 5条则删除 之前的第一条
                        DataSupport.delete(SearchHistoryData.class, dataList.get(0).getId());
                        dataList.remove(dataList.size() - 1);
                        adapter.notifyDataSetChanged();
                    }
                    // 存入历史纪录
                    saveDataToDb(start, end, time);
                    // 添加 进 listView
                    changeDataToTopList(start, end, time);
                    startActivity(intent3);
                } else {
                    T.show(this, "请选择上车/下车地点~");
                }

                break;
            case R.id.iv_change:
                String start = Get.text(tv_start_place);
                String end = Get.text(tv_end_place);
                if ((!TextUtils.isEmpty(start))
                        && (!TextUtils.isEmpty(end))) {
                    tv_start_place.setText(end);
                    tv_end_place.setText(start);
                } else {
                    T.show(SearchTicketsActivity.this, "请输入上车地点/下车地点");
                }
                break;
        }
    }

    // 添加到 listview 顶部
    private void changeDataToTopList(String start, String end, String time) {

        SearchHistoryData data = new SearchHistoryData();
        data.setStartPlace(start);
        data.setEndPlace(end);
        data.setTime(time);
        data.setId(tempList.size() + 1);

        // 如果 list中存在 则置顶 否则就 add 到顶部
        if (dataList.contains(data)) {

            for (SearchHistoryData searchData : dataList) {
                if (searchData.getStartPlace().equals(start)
                        && searchData.getEndPlace().equals(end)) {

                    dataList.remove(searchData);
                    dataList.add(0, searchData);
                }
            }

        } else {
            dataList.add(0, data);
        }

        adapter.notifyDataSetChanged();
    }

    // 存入数据库
    private void saveDataToDb(String start, String end, String time) {

        // 查询数据 是否存在
        List<SearchHistoryData> dataList = DataSupport.where("startPlace = ?", start)
                .where("endPlace = ?", end)
                .find(SearchHistoryData.class);

        // 如果不存在则 add
        if (dataList.size() == 0) {
            SearchHistoryData data = new SearchHistoryData();
            data.setStartPlace(start);
            data.setEndPlace(end);
            data.setTime(time);
            data.save();
        } else {
            // 否则 更新时间
            ContentValues values = new ContentValues();
            values.put("time", time);
            DataSupport.update(SearchHistoryData.class, values, dataList.get(0).getId());
        }
    }

    // 历史数据 点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        SearchHistoryData data = dataList.get(position);
        tv_start_place.setText(data.getStartPlace());
        tv_end_place.setText(data.getEndPlace());

    }

    // 历史数据 删除事件
    @Override
    public void OnItemClick(View view, int position) {
        if (view.getId() == R.id.btn_delete) {
            DataSupport.delete(SearchHistoryData.class, dataList.get(position).getId());
            dataList.remove(position);
            adapter.notifyDataSetChanged();
        }
    }

    // 时间监听器
    public class OnTimeListener implements OnTimeSelectListener {
        @Override
        public void onTimeSelect(Date date, View v) {
            L.i(date.toString());
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
            String time = sdf.format(date);
            tv_time.setText(time);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == StaticClass.REQUEST_START) {
            if (resultCode == StaticClass.RESULT_CODE_INPUTTIPS && data != null) {
                Tip tip = data.getParcelableExtra("tip");
                if (tip.getName() != null) {
                    tv_start_place.setText(tip.getName());
                }
            }

            if (resultCode == StaticClass.RESULT_CODE_KEYWORDS && data != null) {
                String keywords = data.getStringExtra("query");
                tv_start_place.setText(keywords);
            }
        } else if (requestCode == StaticClass.REQUEST_END) {
            if (resultCode == StaticClass.RESULT_CODE_INPUTTIPS && data != null) {
                Tip tip = data.getParcelableExtra("tip");
                if (tip.getName() != null) {
                    tv_end_place.setText(tip.getName());
                }
            }

            if (resultCode == StaticClass.RESULT_CODE_KEYWORDS && data != null) {
                String keywords = data.getStringExtra("query");
                tv_end_place.setText(keywords);
            }
        }
    }
}
