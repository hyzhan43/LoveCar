package zqx.rj.com.lovecar.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.w3c.dom.Text;

import zqx.rj.com.lovecar.R;
import zqx.rj.com.lovecar.chart.CrowdFragment;
import zqx.rj.com.lovecar.chart.OutPutTimeFragment;
import zqx.rj.com.lovecar.chart.RecordFragment;

/**
 * author：  HyZhan
 * created： 2018/10/17 11:26
 * desc：    TODO
 */

public class StatisticeActivity extends BaseActivity implements View.OnClickListener{

    private TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistice);

        initView();
        initData();
    }

    private void initView() {

        findViewById(R.id.btn_back).setOnClickListener(this);
        mTvTitle = findViewById(R.id.tv_title);
    }

    private void initData() {
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        String title = intent.getStringExtra("title");

        mTvTitle.setText(title);
        addFragment(position);
    }

    private void addFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (position) {
            case 0:
                transaction.add(R.id.fl_content, new CrowdFragment());
                break;
            case 1:
                transaction.add(R.id.fl_content, new OutPutTimeFragment());
                break;
            case 2:
                transaction.add(R.id.fl_content, new RecordFragment());
                break;
        }
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }
}
