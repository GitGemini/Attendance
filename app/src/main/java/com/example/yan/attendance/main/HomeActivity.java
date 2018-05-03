package com.example.yan.attendance.main;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.yan.attendance.R;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationBar mMainBtnBar;
    private Fragment currentFragment = null;
    private AttendanceFragment mAttendance = new AttendanceFragment();
    private MessageFragment mMessage = new MessageFragment();
    private PersonalFragment mPersonal = new PersonalFragment();
    private FloatingActionButton homeFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT){
            WindowManager.LayoutParams layoutParams= getWindow().getAttributes();
            layoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        homeFab = (FloatingActionButton) findViewById(R.id.home_fab);
        homeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go_new_groups = new Intent(HomeActivity.this, NewGroupsActivity.class);
                startActivity(go_new_groups);
            }
        });

        mMainBtnBar = (BottomNavigationBar) findViewById(R.id.main_bottom_Bar);
        mMainBtnBar.setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT);
        mMainBtnBar.setAutoHideEnabled(true);
        mMainBtnBar.addItem(new BottomNavigationItem(R.drawable.item2,"主页").setActiveColorResource(R.color.blue))
                .addItem(new BottomNavigationItem(R.drawable.item3,"消息").setActiveColorResource(R.color.blue))
                .addItem(new BottomNavigationItem(R.drawable.item5,"我").setActiveColorResource(R.color.blue))
                .setFirstSelectedPosition(0)
                .initialise();
        mMainBtnBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position){
                    case 0:
                        switchFragment(mAttendance);
                        showFab();
                        break;
                    case 1:
                        switchFragment(mMessage);
                        hideFab();
                        break;
                    case 2:
                        switchFragment(mPersonal);
                        hideFab();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });

    }
    private void hideFab(){
        if (homeFab.getVisibility() == View.VISIBLE){
            homeFab.setVisibility(View.GONE);
        }
    }
    private void showFab(){
        if (homeFab.getVisibility() == View.GONE){
            homeFab.setVisibility(View.VISIBLE);
        }
    }

    //切换fragment功能
    private void switchFragment(Fragment targetfragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetfragment.isAdded()) {
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.main_fragment, targetfragment)
                    .commit();
        } else {
            transaction.hide(currentFragment)
                    .show(targetfragment)
                    .commit();
        }
        currentFragment = targetfragment;
    }
}
