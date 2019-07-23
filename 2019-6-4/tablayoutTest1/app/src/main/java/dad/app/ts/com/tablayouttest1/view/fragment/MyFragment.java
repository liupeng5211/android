package dad.app.ts.com.tablayouttest1.view.fragment;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dad.app.ts.com.tablayouttest1.MyPopupwindow.AddPopWindow;
import dad.app.ts.com.tablayouttest1.R;

public class MyFragment extends Fragment implements MyFragmentView {
    private static final String TAG = "MyFragment";
    private ListView mListView;
    private ImageView mImageView;

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_fragment, container, false);
        mListView = view.findViewById(R.id.listview);
        mImageView = view.findViewById(R.id.head_image);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPopWindow addPopWindow = new AddPopWindow(getActivity());
                addPopWindow.showPopupWindow(mImageView);
            }
        });
        String[] from = {"image1", "text", "image2"};
        int[] to = {R.id.image_start, R.id.text, R.id.image_end};
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            //建立list中的子元素，用键值对的形式来存放数据
            if (i == 0) {
                Map<String, Object> listItem = new HashMap<String, Object>();
                listItem.put("image1", R.drawable.unread_mail);
                listItem.put("text", "通知");
                listItem.put("image2", R.drawable.more);
                //将子元素添加入list中
                list.add(listItem);
            } else if (i == 1) {
                Map<String, Object> listItem = new HashMap<String, Object>();
                listItem.put("image1", R.drawable.star);
                listItem.put("text", "收藏");
                listItem.put("image2", R.drawable.more);
                //将子元素添加入list中
                list.add(listItem);
            } else if (i == 2) {
                Map<String, Object> listItem = new HashMap<String, Object>();
                listItem.put("image1", R.drawable.directions);
                listItem.put("text", "反馈");
                listItem.put("image2", R.drawable.more);
                //将子元素添加入list中
                list.add(listItem);
            } else if (i == 3) {
                Map<String, Object> listItem = new HashMap<String, Object>();
                listItem.put("image1", R.drawable.scope);
                listItem.put("text", "帮助");
                listItem.put("image2", R.drawable.more);
                //将子元素添加入list中
                list.add(listItem);
            } else if (i == 4) {
                Map<String, Object> listItem = new HashMap<String, Object>();
                listItem.put("image1", R.drawable.id);
                listItem.put("text", "联系我");
                listItem.put("image2", R.drawable.more);
                //将子元素添加入list中
                list.add(listItem);
            } else {
                Map<String, Object> listItem = new HashMap<String, Object>();
                listItem.put("image1", R.drawable.settings);
                listItem.put("text", "设置");
                listItem.put("image2", R.drawable.more);
                //将子元素添加入list中
                list.add(listItem);
            }

        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(),
                list, R.layout.simple_adapter_list, from, to);
        mListView.setAdapter(simpleAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: " + i);
//                view.setBackgroundColor(Color.GRAY);
                Animation myAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_anim);
                view.startAnimation(myAnimation);
                switch (i) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;


                }


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
