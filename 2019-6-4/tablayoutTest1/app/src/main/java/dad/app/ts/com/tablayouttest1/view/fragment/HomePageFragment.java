package dad.app.ts.com.tablayouttest1.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import dad.app.ts.com.tablayouttest1.CollectActivity;
import dad.app.ts.com.tablayouttest1.HelpActivity;
import dad.app.ts.com.tablayouttest1.HistoryActivity;
import dad.app.ts.com.tablayouttest1.InputActivity;
import dad.app.ts.com.tablayouttest1.MyCommentActivity;
import dad.app.ts.com.tablayouttest1.R;
import dad.app.ts.com.tablayouttest1.SearchResultActivity;
import dad.app.ts.com.tablayouttest1.SettingActivity;
import dad.app.ts.com.tablayouttest1.adatper.FmPagerAdapter;

public class HomePageFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {
    private TabLayout tabLayout;

    private EditText mEditText;
    private ImageView mImageView;
    private ViewPager viewPager;

    private FmPagerAdapter pagerAdapter;
    private ArrayList<Fragment> fragments;
    private static final String USERDATA = "userdata";
    private SharedPreferences mSharedPreferences;
    private static final String TAG = "HomePage";
    private String[] titles = new String[]{"推荐", "热点", "文化", "娱乐", "生活", "体育", "财经", "商务", "国际"};
//    private String[] titles = new String[]{"推荐", "热点"};


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        Log.d(TAG, "onCreateView: home");

//        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mImageView = view.findViewById(R.id.settings);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        mEditText = view.findViewById(R.id.search);
        mSharedPreferences = getActivity().getSharedPreferences(USERDATA, 0);
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View leftView = navigationView.getHeaderView(0);
        final TextView nameTv = leftView.findViewById(R.id.name);
        //set the myword textview .
        final TextView wordTv = leftView.findViewById(R.id.my_word);
        //set the imageview .
        final ImageView imageview = leftView.findViewById(R.id.imageView);
        final boolean loginstatus = mSharedPreferences.getBoolean("login", false);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //find the navigationView ,and through the view find image ,text.
                //and set the sharedprefences info to the view.
                final boolean loginstatus1 = mSharedPreferences.getBoolean("login", false);

                if (loginstatus1) {
                    String name = mSharedPreferences.getString("username", "请先点击头像登录");
                    String myword = mSharedPreferences.getString("word", "");
                    boolean imgflag = mSharedPreferences.getBoolean("imgflag", true);
                    Log.d("username = ", " " + name);
                    Log.d("imgflag = ", " " + imgflag);
                    nameTv.setText(name);
                    wordTv.setText(myword);
                    if (!imgflag) {
                        FileInputStream fileInputStream = null;
                        try {
                            fileInputStream = new FileInputStream(String.valueOf(getActivity().getDir(name + ".jpg", getActivity().MODE_PRIVATE)) + "/" + name + ".jpg");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                        imageview.setImageBitmap(bitmap);

                    }

                } else {
                    nameTv.setText("Android Studio");
                    wordTv.setText("android.studio@android.com");
                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_round);
                    imageview.setImageBitmap(bmp);
                }

                final DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);


            }
        });
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER))

                {

                    //do something;
                    String searchText = mEditText.getText().toString().trim();
                    Log.d(TAG, "要搜索的zifuc: " + searchText + ";");
                    mEditText.setText("");
                    Intent intent = new Intent(getContext(), SearchResultActivity.class);
                    intent.putExtra("search_info", searchText);
                    startActivity(intent);

                    return true;

                }

                return false;

            }

        });

        fragments = new ArrayList<>();

        for (int i = 0; i < titles.length; i++) {
            Log.d(TAG, "onResume: " + i);

            fragments.add(TabLayoutFragment.newInstance(i));
            tabLayout.addTab(tabLayout.newTab());
        }
        Log.d(TAG, "fragments长度----: " + fragments.size());

        tabLayout.setupWithViewPager(viewPager, false);

        pagerAdapter = new FmPagerAdapter(fragments, getFragmentManager());


        viewPager.setAdapter(pagerAdapter);

        for (int i = 0; i < titles.length; i++) {
            tabLayout.getTabAt(i).setText(titles[i]);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("", "onTabReselected: ");
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: ");
        tabLayout.requestFocus();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onStop() {
        super.onStop();
//        super.onDestroy();
        Log.d(TAG, "onStop: ");

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        final boolean loginstatus = mSharedPreferences.getBoolean("login", false);
        int id = menuItem.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Log.d("", "click" + id);
            System.out.println("通知");
            Intent intent = new Intent(getActivity(), InputActivity.class);
            getActivity().startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            if (!loginstatus) {
                Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_LONG).show();

            } else {
                Log.d("", "click" + id);
                Intent intent = new Intent(getActivity(), CollectActivity.class);
                getActivity().startActivity(intent);
            }


        } else if (id == R.id.nav_share) {
            Log.d("", "click" + id);
            Intent intent = new Intent(getActivity(), HelpActivity.class);
            getActivity().startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            if (!loginstatus) {
                Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_LONG).show();

            } else {
                Log.d("", "click" + id);
                System.out.println("浏览历史");
                Intent intent = new Intent(getActivity(), HistoryActivity.class);
                getActivity().startActivity(intent);
            }

        } else if (id == R.id.nav_manage) {
            if (!loginstatus) {
                Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_LONG).show();

            } else {
                Log.d("", "click" + id);
                System.out.println("设置");
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                getActivity().startActivity(intent);
            }
        } else if (id == R.id.nav_send) {
            if (!loginstatus) {
                Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_LONG).show();

            } else {
                Log.d("", "click" + id);
                System.out.println("comment");
                Intent intent = new Intent(getActivity(), MyCommentActivity.class);
                getActivity().startActivity(intent);
            }
        }

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}
