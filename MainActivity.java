package com.yichun.module.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.yichun.module.R;
import com.yichun.module.adapter.FragmentAdapter;
import com.yichun.module.common.base.ViewManager;
import com.yichun.module.common.updateapp.UpdateService;
import com.yichun.module.common.utils.BottomNavigationViewHelper;
import com.yichun.module.common.utils.ToastUtils;
import com.yichun.module.common.widget.NoScrollViewPager;
import com.yichun.module.mvp.BaseActivity;
import com.yichun.module.mvp.BaseFragment;
import com.yichun.module.ui.compare.CompareFragment;
import com.yichun.module.ui.home.HomeFragment;
import com.yichun.module.ui.map.MapFragment;
import com.yichun.module.ui.sort.SortFragment;
import com.yichun.module.ui.warn.WarnFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.container_pager)
    NoScrollViewPager containerPager;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private List<BaseFragment> fragmentList;
    private FragmentAdapter mFragmentAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                containerPager.setCurrentItem(0);
                return true;
            } else if (id == R.id.navigation_map) {
                containerPager.setCurrentItem(1);
                return true;
            } else if (id == R.id.navigation_contrast) {
                containerPager.setCurrentItem(2);
                return true;
            } else if (id == R.id.navigation_sort) {
                containerPager.setCurrentItem(3);
                return true;
            } else if (id == R.id.navigation_warning) {
                containerPager.setCurrentItem(4);
                return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initViewPager();
    }

    private void initView(){
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void initViewPager(){

        fragmentList = new ArrayList<>();
        fragmentList.add(HomeFragment.getInstance());
        fragmentList.add(CompareFragment.getInstance());
        fragmentList.add(MapFragment.getInstance());
        fragmentList.add(SortFragment.getInstance());
        fragmentList.add(WarnFragment.getInstance());

        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),fragmentList);
        containerPager.setPagerEnabled(false);
        containerPager.setAdapter(mFragmentAdapter);

    }


    //App更新
    public void update(String apkUrl) {
        UpdateService.Builder.create(apkUrl)
                .setVersionCode(2)
                .setIsForce(false)
                .build(MainActivity.this);
    }


    private long mExitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //两秒之内按返回键就会退出
            if ((System.currentTimeMillis() - mExitTime) > 3000) {
                ToastUtils.showShortToast(getString(R.string.app_exit_hint));
                mExitTime = System.currentTimeMillis();
            } else {
                ViewManager.getInstance().exitApp(this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
