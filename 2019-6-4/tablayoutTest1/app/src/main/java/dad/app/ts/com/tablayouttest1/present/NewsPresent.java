package dad.app.ts.com.tablayouttest1.present;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Message;
import android.util.Log;
import android.widget.SimpleAdapter;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dad.app.ts.com.tablayouttest1.R;
import dad.app.ts.com.tablayouttest1.util.DbControl;
import dad.app.ts.com.tablayouttest1.util.SearchDbHelper;
import dad.app.ts.com.tablayouttest1.view.fragment.FragmentView;

public class NewsPresent {
    private List<Map<String, String>> list;
    private SimpleAdapter simpleAdapter;
    private FragmentView fragmentView;
    private SearchDbHelper mSearchDbHelper;
    private Context context;
    private DbControl mDbControl;
    private SQLiteDatabase mSqLiteDatabase;
    private static final String TABLE_NAME = "news";
    private static final String TITLE_COLUM_NAME = "title";
    private static final String URL_COLUM_NAME = "url";

    private static final String TAG = "news";
    private static String USER_AGENT = "User-Agent";
    private static String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko Core/1.63.6821.400 QQBrowser/10.3.3040.400";

    public NewsPresent(Context context, FragmentView view) {
        this.context = context;
        this.fragmentView = view;
        mSearchDbHelper = new SearchDbHelper(context, TABLE_NAME, null, 1);
    }

    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x12233) {
                Log.d(TAG, "handleMessage: 开始设置适配器");
                fragmentView.setAdapter(simpleAdapter);
                fragmentView.hideProgress();
            }

        }
    };

    public void getAdapter(String url) {
        getNews(url);


    }

    public List<Map<String, String>> getList() {
        return list;
    }


    /**
     * 获取今日头条的网页数据.
     */
    private void getNews(final String url) {
        Log.d(TAG, "getNews: " + url);
        list = new ArrayList<>();
        if (url.equals("")) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    try {
                        if (mSqLiteDatabase == null) {
                            mSqLiteDatabase = mSearchDbHelper.getReadableDatabase();
                            mDbControl = new DbControl();
                        }

                        Element document = Jsoup.connect(url).header(USER_AGENT, USER_AGENT_VALUE).get().body();

//                        Log.d(TAG, "" + document.toString());
                        try {
                            fragmentView.setProgressMode(false);
                            Elements listClass = document.select("a");

                            double progressValue = 1;
                            fragmentView.showProgress();
                            for (Element element : listClass) {
                                Map<String, String> map = new HashMap<>();

                                //文章链接在a标签下，需要单独处理
                                String content = element.attr("href");
                                String title = element.text();
                                if (title.contains("7天无理由退货") || title.contains("工银融e行我的百万梦想")) {
                                    continue;
                                }
                                if (title.contains("娃哈哈:经典食材天生是宝") || title.contains("我的钢铁网")) {
                                    continue;
                                }
                                if (title.contains("网上传播视听节目许可证") || title.contains("京ICP")) {
                                    continue;
                                }
                                String imgsrc = element.getElementsByTag("img").attr("src");
//                                Log.d(TAG, "--------------"+progressValue+"--"+listClass.size()+"----"+progressValue/listClass.size()*100+"---");

                                int value = (int) Math.round(progressValue / listClass.size() * 100);
                                progressValue++;

                                fragmentView.setProgress(value);
//                                Log.d(TAG, "原始数据:" + title + " , " + content);
                                if (title.length() <= 9) {
                                    continue;
                                }
                                if (content.startsWith("//")) {
                                    content = content.substring(2);

                                }
                                if (content.startsWith("/")) {
                                    content = url + content;
                                }
                                if (content.startsWith("www")) {
                                    content = "http://" + content;
                                }

//                                Log.d(TAG, "标题： " + title + " , 网址 ： " + content);
//                                Log.d(TAG, "图片标题： "  + imgsrc);
                                map.put("title", title);
                                map.put("content", content);
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(TITLE_COLUM_NAME, title);
                                contentValues.put(URL_COLUM_NAME, content);
                                mDbControl.insert(mSqLiteDatabase, contentValues, TABLE_NAME, "title");
                                list.add(map);

                            }
                            simpleAdapter = new SimpleAdapter(context, list, R.layout.list_view, new String[]{"title", "content"}, new int[]{R.id.title, R.id.content});
                            handler.sendEmptyMessage(0x12233);

                        } catch (Exception e) {
                            e.printStackTrace();

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "获取页面时出现异常");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (mSqLiteDatabase != null) {
                    mSqLiteDatabase.close();
                }
            }

        }).start();

    }


}
