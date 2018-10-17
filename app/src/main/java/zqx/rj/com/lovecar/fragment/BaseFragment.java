package zqx.rj.com.lovecar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zqx.rj.com.lovecar.utils.ScreenTools;

/**
 * author：  HyZhan
 * created： 2018/10/17 13:40
 * desc：    TODO
 */

public abstract class BaseFragment extends Fragment {

    private View mRoot;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mRoot == null) {
            mRoot = inflater.inflate(getLayoutId(), container, false);
        }

        // 屏幕适配
        ScreenTools.fragment(mRoot);
        return mRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(mRoot);
        initData();
    }

    public abstract int getLayoutId();

    public void initData() {
    }

    public void initView(View view) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRoot != null) {
            ((ViewGroup) mRoot.getParent()).removeView(mRoot);
        }
    }
}
