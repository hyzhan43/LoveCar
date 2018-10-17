package zqx.rj.com.lovecar.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * author：  HyZhan
 * created： 2018/10/17 11:40
 * desc：    TODO
 */

public class NewTabAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public NewTabAdapter(FragmentManager fm, List<Fragment> fragmentLsit) {
        super(fm);
        this.fragments = fragmentLsit;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
