package dad.app.ts.com.tablayouttest1.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dad.app.ts.com.tablayouttest1.R;

public class FraiendFragment extends Fragment {
    private static final String TAG = "FraiendFragment";
    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friend_fragment, container, false);
        mListView = view.findViewById(R.id.listview);
        String[] from = {"image", "name", "net"};
        int[] to = {R.id.head_image, R.id.name, R.id.net};
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            //建立list中的子元素，用键值对的形式来存放数据
            if (i == 0) {
                Map<String, Object> listItem = new HashMap<String, Object>();
                listItem.put("image", R.drawable.head_background);
                listItem.put("name", "青春不散场");
                listItem.put("net", "4G在线");
                //将子元素添加入list中
                list.add(listItem);
            } else if (i == 1) {
                Map<String, Object> listItem = new HashMap<String, Object>();
                listItem.put("image", R.drawable.head_background);
                listItem.put("name", "青春不散场");
                listItem.put("net", "4G在线");
                //将子元素添加入list中
                list.add(listItem);
            } else if (i == 2) {
                Map<String, Object> listItem = new HashMap<String, Object>();
                listItem.put("image", R.drawable.head_background);
                listItem.put("name", "青春不散场");
                listItem.put("net", "4G在线");
                //将子元素添加入list中
                list.add(listItem);
            } else if (i == 3) {
                Map<String, Object> listItem = new HashMap<String, Object>();
                listItem.put("image", R.drawable.head_background);
                listItem.put("name", "青春不散场");
                listItem.put("net", "4G在线");
                //将子元素添加入list中
                list.add(listItem);
            } else if (i == 4) {
                Map<String, Object> listItem = new HashMap<String, Object>();
                listItem.put("image", R.drawable.head_background);
                listItem.put("name", "青春不散场");
                listItem.put("net", "4G在线");
                //将子元素添加入list中
                list.add(listItem);
            } else if (i == 5) {
                Map<String, Object> listItem = new HashMap<String, Object>();
                listItem.put("image", R.drawable.head_background);
                listItem.put("name", "青春不散场");
                listItem.put("net", "4G在线");
                //将子元素添加入list中
                list.add(listItem);
            }

        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(),
                list, R.layout.simple_friend_list, from, to);
        mListView.setAdapter(simpleAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: " + i);
//                view.setBackgroundColor(Color.GRAY);
                Animation myAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_anim);

                view.startAnimation(myAnimation);


            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: ");

    }
}
