package zqx.rj.com.lovecar.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.adapter.StatisticeTitleAdapter;
import zqx.rj.com.lovecar.entity.StatisticeData;
import zqx.rj.com.lovecar.ui.StatisticeActivity;

/**
 * author：  HyZhan
 * created： 2018/10/17 11:46
 * desc：    TODO
 */

public class StatisticeFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private List<StatisticeData> dataList;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_statistice;
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        mListView = view.findViewById(R.id.lv_statistice);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();

        dataList = new ArrayList<>();
        dataList.add(new StatisticeData("购票人群", R.drawable.ic_crowd));
        dataList.add(new StatisticeData("发车时间", R.drawable.ic_calendar));
        dataList.add(new StatisticeData("购票情况", R.drawable.ic_record));

        mListView.setAdapter(new StatisticeTitleAdapter(getActivity(), dataList));
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), StatisticeActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("title", dataList.get(position).getTitle());
        startActivity(intent);
    }
}
