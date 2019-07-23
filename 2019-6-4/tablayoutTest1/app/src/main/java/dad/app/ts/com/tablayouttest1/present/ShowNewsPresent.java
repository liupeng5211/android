package dad.app.ts.com.tablayouttest1.present;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import dad.app.ts.com.tablayouttest1.InputActivity;

import static android.content.ContentValues.TAG;

public class ShowNewsPresent {
    private Context context;
    private IShowNewsView IShowNewsView;
    private static String USER_AGENT = "User-Agent";
    private static String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko Core/1.63.6821.400 QQBrowser/10.3.3040.400";

    public ShowNewsPresent(Context context, IShowNewsView view) {
        this.context = context;
        this.IShowNewsView = view;
    }

    /**
     * Get image from the net.
     *
     * @param imgsrc The net image url.
     * @return Bitmap the image.
     */
    public Bitmap getNetImage(String imgsrc) throws Exception {
        Bitmap bitmap;
        URL url1 = new URL(imgsrc);
        HttpURLConnection connection = null;
        connection = (HttpURLConnection) url1.openConnection();
        connection.setConnectTimeout(10000);
        connection.setRequestMethod("GET");
        connection.connect();
        InputStream is = connection.getInputStream();
        bitmap = BitmapFactory.decodeStream(is);

        return bitmap;
    }

    /**
     * Get news and show news to activity .
     *
     * @param url The url is the news web url.
     */
    public void showNews(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //request sever on time, handle if video exists.
                    Log.d("", "url:" + url);
                    Document document = null;
                    try {
                        document = Jsoup.connect(url).header(USER_AGENT, USER_AGENT_VALUE).get();

                    } catch (IllegalArgumentException e) {

                        return;
                    }

                    Elements elements = document.select("body");

                    for (Element element : elements) {
                        Elements elementsBrother = element.getAllElements();
                        for (Element element1 : elementsBrother) {
                            String tagname = element1.tagName();
                            Log.d("", "标签名:" + tagname);
                            if (element1.className().equals("sp_tp")) {
                                Log.d(TAG, "已经到了我不想要显示的部分了");
                                break;
                            }
                            //adhzh
                            if (element1.className().equals("div624")) {
                                Log.d(TAG, "已经到了我不想要的那个广告了");
                                break;
                            }
                            if (tagname.equals("p")) {
                                String text = element1.text();
                                if (!text.equals("")) {
                                    //增加一个TextView
                                    Log.d(TAG, "textview :" + text);
                                    IShowNewsView.addTextView(text);
                                }
                            }
                            if (tagname.equals("img")) {


                                String imgsrc = element1.attr("src");
//                                if(element1.attr("alt").equals("")){
//                                    continue;
//                                }
                                Log.d(TAG, "img标题不为空");
                                if (imgsrc.startsWith("/")) {
                                    imgsrc = imgsrc.substring(1);
                                }

                                Log.d(TAG, "imageview " + imgsrc);
                                //增加一个ImageView

                                Bitmap bitmap = null;
                                try {
                                    bitmap = getNetImage(imgsrc);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                IShowNewsView.addImageView(bitmap);

                            }


//                            if (tagname.equals("video")) {
//                                Log.d("", "出现视频 ");
//                                String videoUrl = element1.attr("src");
//                                Log.d(TAG, "视频地址 "+videoUrl);

//                                IShowNewsView.addVideoView(videoUrl);
//                            }


//分析body标签下的每一个标签，利用标签数量循环遍历。分析标签，想要获得的标签有 :
                            //

                        }


                    }
//                    String videoUrl = "http://video.chinanews.com/tvmining//News/MP4ZXW/CCYVNEWS/2019/02/22/Ls3weyy_1550791844721_QtRwogT_6359.mp4";
//                    IShowNewsView.addVideoView(videoUrl);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

}
