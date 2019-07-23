package dad.app.ts.com.tablayouttest1.view.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dad.app.ts.com.tablayouttest1.ShowNewsActivity;
import dad.app.ts.com.tablayouttest1.R;
import dad.app.ts.com.tablayouttest1.constant.NewsUrl;
import dad.app.ts.com.tablayouttest1.present.NewsPresent;
import dad.app.ts.com.tablayouttest1.util.HistoryDbHelper;

/**
 *
 */
public class TabLayoutFragment extends Fragment implements FragmentView {
    public static String TABLAYOUT_FRAGMENT = "tab_fragment";
    private TextView txt;
    private ListView listView;
    public int type;
    public NewsPresent newsPresent;
    private ProgressBar mProgressBar;
    private SimpleAdapter simpleAdapter1;
    private static final String TAG = "TablayoutFragment";

    public static TabLayoutFragment newInstance(int i) {
        TabLayoutFragment fragment = new TabLayoutFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TABLAYOUT_FRAGMENT, i);
        fragment.setArguments(bundle);
        return fragment;
    }

    Handler mHandelr = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x101) {
                Bundle bundle = msg.getData();
                int value = bundle.getInt("value");
                mProgressBar.setProgress(value);
            }
            if (msg.what == 0x102) {
                mProgressBar.setVisibility(ProgressBar.GONE);
            }
            if (msg.what == 0x103) {
                mProgressBar.setVisibility(ProgressBar.VISIBLE);
            }
            if (msg.what == 0x104) {
                Bundle bundle = msg.getData();
                boolean flag = bundle.getBoolean("flag");
                mProgressBar.setIndeterminate(flag);
            }
            if (msg.what == 0x001234) {
                Bundle bundle = msg.getData();
                int position = bundle.getInt("position");

//                listView.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.whitesmoke));
                listView.setSelection(position);

            }
            if (msg.what == 0x123) {

                simpleAdapter1.notifyDataSetChanged();
                listView.setAdapter(simpleAdapter1);
//                listView.setSelection(20);
                Log.d(TAG, "handleMessage: 设置完成");
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        if (getArguments() != null) {
            type = (int) getArguments().getSerializable(TABLAYOUT_FRAGMENT);
            Log.d(TAG, "type-----------------: " + type);
        }
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_tablayout, container, false);

        newsPresent = new NewsPresent(getContext(), this);


        listView = (ListView) view.findViewById(R.id.listview);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundColor(getResources().getColor(R.color.whitesmoke));
                TextView titleText = view.findViewById(R.id.title);
                TextView urlText = view.findViewById(R.id.content);
                String title = titleText.getText().toString();
                String url = urlText.getText().toString();
                Log.d("标题和网址", title + ", " + url);
                HistoryDbHelper historyDbHelper = new HistoryDbHelper(getActivity(), "history_news", null, 1);
                SQLiteDatabase database = historyDbHelper.getReadableDatabase();
                ContentValues contentValues = new ContentValues();
                String user = getActivity().getSharedPreferences("userdata", 0).getString("username", "null");
                contentValues.put("user", user);
                contentValues.put("title", title);
                contentValues.put("url", url);
                contentValues.put("type", "usual_new");
                contentValues.put("videourl", "");
                Cursor cursor = database.rawQuery("select * from history_news where user = ? and title = ?", new String[]{user, title});
                if (cursor.getCount() == 0) {
                    database.insert("history_news", null, contentValues);
                }
                database.close();
                historyDbHelper.close();
                Intent intent = new Intent(getContext(), ShowNewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                bundle.putString("title", title);
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                startActivityForResult(intent, 123);
//                startActivity(intent);
            }
        });
        boolean isConn = isNetworkConnected(getActivity());
        if (isConn) {
            newsPresent.getAdapter(NewsUrl.URLS[type]);
        } else {
            Toast.makeText(getActivity(), "当前网络不可用", Toast.LENGTH_LONG).show();
        }

        mProgressBar = view.findViewById(R.id.progress);
        listView.requestFocus();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
    }

    @Override
    public void onResume() {
        super.onResume();
//        newsPresent.getAdapter(NewsUrl.URLS[type]);
        listView.requestFocus();
        listView.setAdapter(simpleAdapter1);

    }


    @Override
    public void setAdapter(SimpleAdapter simpleAdapter) {
        Log.d(TAG, "setAdapter: 接收--");
        this.simpleAdapter1 = simpleAdapter;
        mHandelr.sendEmptyMessage(0x123);


    }

    @Override
    public void setProgress(int value) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("value", value);
        message.setData(bundle);
        message.what = 0x101;
        mHandelr.sendMessage(message);
    }

    @Override
    public void hideProgress() {

        mHandelr.sendEmptyMessage(0x102);
    }

    @Override
    public void showProgress() {

        mHandelr.sendEmptyMessage(0x103);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123 && resultCode == 4) {
            int position = data.getIntExtra("position", 1);
            Message message = new Message();
            message.what = 0x001234;
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            message.setData(bundle);
            mHandelr.sendMessage(message);

        }
    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    @Override
    public void setProgressMode(boolean flag) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putBoolean("flag", flag);
        message.setData(bundle);
        message.what = 0x104;
        mHandelr.sendMessage(message);
    }
}
