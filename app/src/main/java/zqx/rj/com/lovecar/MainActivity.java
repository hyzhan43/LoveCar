package zqx.rj.com.lovecar;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.lilei.springactionmenu.ActionMenu;
import com.lilei.springactionmenu.OnActionItemClickListener;

import zqx.rj.com.lovecar.fragment.MainFragment;
import zqx.rj.com.lovecar.fragment.MyFragment;
import zqx.rj.com.lovecar.fragment.NewsFragment;
import zqx.rj.com.lovecar.fragment.OrderFragment;
import zqx.rj.com.lovecar.ui.BaseActivity;
import zqx.rj.com.lovecar.ui.LikeActivity;
import zqx.rj.com.lovecar.ui.PublishActivity;
import zqx.rj.com.lovecar.ui.SearchTicketsActivity;
import zqx.rj.com.lovecar.utils.T;

import static com.ashokvarma.bottomnavigation.BottomNavigationBar.BACKGROUND_STYLE_RIPPLE;

public class MainActivity extends BaseActivity {

    private BottomNavigationBar main_bottom_menu;

    private Fragment mMainFragment;
    private Fragment mNewsFragment;
    private Fragment mOrderFragment;
    private Fragment mMyFragment;
    // 被选中的 mContent
    private Fragment mContent;
    private FragmentManager fm;

    // 悬浮按钮
    private ActionMenu actionMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);


        initView();
        initBottomNavigationBar();
        initActionMenu();
    }

    private void initView() {
        main_bottom_menu = findViewById(R.id.main_bottom_menu);
        fm = getSupportFragmentManager();
        // 显示默认页面
        setDefaultFragment();

        actionMenu = findViewById(R.id.actionMenu);
    }


    private void initBottomNavigationBar() {

        // 设置切换动画模式
        main_bottom_menu.setMode(BottomNavigationBar.MODE_SHIFTING);
        // 波纹背景
        main_bottom_menu.setBackgroundStyle(BACKGROUND_STYLE_RIPPLE);

        // 设置默认颜色
        main_bottom_menu
                .setInActiveColor(R.color.color_gray)               //设置未选中的Item的颜色，包括图片和文字
                .setActiveColor(R.color.color_white)                //设置选中底部菜单栏颜色
                .setBarBackgroundColor(R.color.color_main);   //设置整个控件的背景色

        main_bottom_menu.addItem(new BottomNavigationItem(R.mipmap.b1, getString(R.string.main)))
                .addItem(new BottomNavigationItem(R.mipmap.b2, getString(R.string.news)))
                .addItem(new BottomNavigationItem(R.mipmap.b4, getString(R.string.order)))
                .addItem(new BottomNavigationItem(R.mipmap.b5, getString(R.string.my)))
                .setFirstSelectedPosition(0)
                .initialise();     // 所有的设置需要在调用该方法前完成


        main_bottom_menu.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {

            //未选中 -> 选中
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        if (mMainFragment == null) {
                            mMainFragment = new MainFragment();
                        }
                        switchContent(mContent, mMainFragment);
                        break;
                    case 1:
                        if (mNewsFragment == null) {
                            mNewsFragment = new NewsFragment();
                        }
                        switchContent(mContent, mNewsFragment);
                        break;

                    case 2:
                        if (mOrderFragment == null) {
                            mOrderFragment = new OrderFragment();
                        }
                        switchContent(mContent, mOrderFragment);
                        break;
                    case 3:
                        if (mMyFragment == null) {
                            mMyFragment = new MyFragment();
                        }
                        switchContent(mContent, mMyFragment);
                        break;
                    default:
                        break;
                }
            }

            //选中 -> 未选中
            @Override
            public void onTabUnselected(int position) {

            }

            //选中 -> 选中
            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    // 切换页面
    private void switchContent(Fragment from, Fragment to) {
        //把跳转到那个页面  赋值给mContent  更新当前页面
        if (mContent != to) {
            mContent = to;
            FragmentTransaction transaction = fm.beginTransaction();
            if (!to.isAdded()) {
                // 隐藏当前的 fragment， add 下一个到 Activity中
                transaction.hide(from).add(R.id.fragment_content, to).commit();
            } else {
                // 隐藏当前的 fragment 显示下一个
                transaction.hide(from).show(to).commit();
            }
        }
    }

    //设置默认页面
    private void setDefaultFragment() {

        if (mMainFragment == null) {
            mMainFragment = new MainFragment();
        }
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        //给当前页面赋值
        mContent = mMainFragment;
        //并设置默认页面
        transaction.add(R.id.fragment_content, mContent).commit();
    }

    // 定义悬浮按钮
    private void initActionMenu() {
        actionMenu.addView(R.drawable.search, getItemColor(R.color.menuNormalYellow),
                getItemColor(R.color.menuPressYellow));
        actionMenu.addView(R.drawable.like, getItemColor(R.color.menuNormalRed),
                getItemColor(R.color.menuPressRed));
        actionMenu.addView(R.drawable.write, getItemColor(R.color.menuNormalBlue),
                getItemColor(R.color.menuPressBlue));
        actionMenu.addView(R.drawable.write, getItemColor(R.color.menuNormalBlue),
                getItemColor(R.color.menuPressBlue));

        // 悬浮按钮设置监听
        actionMenu.setItemClickListener(new OnActionItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent intent = null;
                switch (position) {
                    case 0:

                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, SearchTicketsActivity.class);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, LikeActivity.class);
                        break;
                    case 3:
//                        T.show(MainActivity.this, "抱歉你没有权限发布包车信息~");
                        intent = new Intent(MainActivity.this, PublishActivity.class);
                        break;
                }

                if (intent != null) {
                    startActivity(intent);
                }
            }

            @Override
            public void onAnimationEnd(boolean b) {

            }
        });
    }

    //   获取颜色
    private int getItemColor(int colorID) {
        return ContextCompat.getColor(this, colorID);
    }
}
