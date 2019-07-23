package dad.app.ts.com.tablayouttest1.present;

import android.content.Context;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class ShowVideoPresent {
    private Context context;
    private IShowVideoView mIshowVideoView;
    private static String USER_AGENT = "User-Agent";
    private static String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko Core/1.63.6821.400 QQBrowser/10.3.3040.400";

    public ShowVideoPresent(Context context, IShowVideoView mIshowVideoView) {
        this.context = context;
        this.mIshowVideoView = mIshowVideoView;
    }

    public void showVideoNews(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //request sever on time, handle if video exists.
                    Log.d("", "url:" + url);
                    Document document = Jsoup.connect(url).header(USER_AGENT, USER_AGENT_VALUE).get();
                    Elements elements = document.select("p");
                    for (Element element : elements) {
                        String text = element.text();
                        String classname = element.parent().parent().className();
                        if(classname.equals("splist_div2")){
                            Log.d(TAG, "已经到了我不想显示的部分了");
                            break;
                        }
                        if (!text.equals("")) {
                            //add a TextView
                            Log.d(TAG, "textview :" + text);
                            mIshowVideoView.addTextView(text);
                        }

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }
}
