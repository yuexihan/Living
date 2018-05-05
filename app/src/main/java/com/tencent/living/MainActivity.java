package com.tencent.living;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.projectoxford.face.FaceServiceClient;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class MainActivity extends AppCompatActivity {

    private ViewPager homeViewPager;
    private BottomNavigationViewEx navigation;
    private TextView title;
    //用于Activity跳转
    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int GALLERY_REQUEST_CODE = 2;
    public static final int COMMENT_EDIT_REQUEST_CODE = 3;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()){
                case R.id.navigation_record:
                    homeViewPager.setCurrentItem(0);
                    break;
                case R.id.navigation_ground:
                    homeViewPager.setCurrentItem(1);
                    break;
                case R.id.navigation_message:
                    homeViewPager.setCurrentItem(2);
                    break;
                case R.id.navigation_user:
                    homeViewPager.setCurrentItem(3);
                    break;
            }

            return true;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = (TextView) findViewById(R.id.toolbar_title);
        Shader textShader=new LinearGradient(0, 0, 500, 0,
                new int[]{Color.argb(0xff, 254, 197, 181),
                        Color.argb(0xff, 0xff, 0xf7, 0xa8)},
                null, Shader.TileMode.CLAMP);
        title.getPaint().setShader(textShader);
        title.setTypeface(title.getTypeface(), Typeface.ITALIC);
        initViewPager();
        initNavigation();
    }

    private void initNavigation(){
        navigation = (BottomNavigationViewEx) findViewById(R.id.navigation);
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        addBadgeAt(2, 2);
    }
    private void initViewPager() {
        homeViewPager = (ViewPager)findViewById(R.id.homeViewPager);
        List<Fragment> fragList = new ArrayList<>();
        fragList.add(new RecordFragment());
        fragList.add(new GroundFragment());
        fragList.add(new MessageFragment());
        fragList.add(new UserFragment());
        homeViewPager.setAdapter(new FragmentAdapter(
                this.getSupportFragmentManager(), fragList));
    }

    /* 一个内部类，只用于ViewPager处理其内部的Fragments */
    private class FragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;
        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    private Badge addBadgeAt(int position, int number) {
        // add badge
        return new QBadgeView(this)
                .setBadgeNumber(number)
                .setShowShadow(false)
                .setGravityOffset(12, 2, true)
                .bindTarget(navigation.getBottomNavigationItemView(position))
                .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                    @Override
                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                        if (Badge.OnDragStateChangedListener.STATE_SUCCEED == dragState)
                            Toast.makeText(MainActivity.this, "Badge 被消除了", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
