package dad.app.ts.com.tablayouttest1;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import dad.app.ts.com.tablayouttest1.MyPopupwindow.AddPopWindow;
import dad.app.ts.com.tablayouttest1.base.BaseActivity;
import dad.app.ts.com.tablayouttest1.view.fragment.HomePageFragment;
import dad.app.ts.com.tablayouttest1.view.fragment.ImageFragment;
import dad.app.ts.com.tablayouttest1.view.fragment.MyFragment;
import dad.app.ts.com.tablayouttest1.view.fragment.VideoFragment;

public class MainActivity extends BaseActivity {
    private FragmentManager fm;
    private Fragment mHomePageFragment;
    private Fragment mVideoFragment;
    private Fragment mImageFragment;
    private DrawerLayout mDrawer;
    private Fragment mMyFragment;
    private NavigationView mNavigationView;
    private static final String USERDATA = "userdata";
    private SharedPreferences mSharedPreferences;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showFragment(1);
                    return true;
                case R.id.navigation_dashboard:
                    showFragment(2);
                    return true;
                case R.id.navigation_notifications:
                    showFragment(3);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        View view = mNavigationView.getHeaderView(0);
        final TextView nameTv = view.findViewById(R.id.name);
        TextView wordTv = view.findViewById(R.id.my_word);
        ImageView imageview = view.findViewById(R.id.imageView);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                AddPopWindow addPopWindow = new AddPopWindow(MainActivity.this);
                addPopWindow.showPopupWindow(v);
            }
        });
        fm = getSupportFragmentManager();
        showFragment(1);


    }

    @Override
    public void onBackPressed() {

    }


    /**
     * press return button to show info.
     * put in MainActivity.
     * and in MainActivity judge drawlayout if open.
     */
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                mDrawer.closeDrawer(GravityCompat.START);

            } else {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                    System.exit(0);
                }
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showFragment(int index) {
        FragmentTransaction ft = fm.beginTransaction();

        // 想要显示一个fragment,先隐藏所有fragment，防止重叠

        hideFragments(ft);

        switch (index) {


            case 1:
                // 如果fragment1已经存在则将其显示出来
                if (mHomePageFragment != null)
                    ft.show(mHomePageFragment);
                    // 否则是第一次切换则添加fragment1，注意添加后是会显示出来的，replace方法也是先remove后add
                else {
                    mHomePageFragment = new HomePageFragment();
                    ft.add(R.id.content, mHomePageFragment);
                }
                break;
            case 2:
                if (mVideoFragment != null)
                    ft.show(mVideoFragment);
                else {
                    mVideoFragment = new VideoFragment();
                    ft.add(R.id.content, mVideoFragment);
                }
                break;
            case 3:
                if (mImageFragment != null)
                    ft.show(mImageFragment);
                else {
                    mImageFragment = new ImageFragment();
                    ft.add(R.id.content, mImageFragment);
                }
                break;
            case 4:
                if (mMyFragment != null)
                    ft.show(mMyFragment);
                else {
                    mMyFragment = new MyFragment();
                    ft.add(R.id.content, mMyFragment);
                }
                break;
        }
        ft.commit();
    }

    private void hideFragments(FragmentTransaction ft) {
        if (mHomePageFragment != null) {
            ft.hide(mHomePageFragment);
        }
        if (mVideoFragment != null) {
            ft.hide(mVideoFragment);
        }
        if (mImageFragment != null) {
            ft.hide(mImageFragment);
        }
        if (mMyFragment != null) {
            ft.hide(mMyFragment);
        }

    }


}
