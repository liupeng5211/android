package dad.app.ts.com.tablayouttest1.present;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShowPicturePresent {
    private Context mContext;
    private IShowPictureView mIShowPictureView;
    private static String USER_AGENT = "User-Agent";
    private static String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko Core/1.63.6821.400 QQBrowser/10.3.3040.400";
    private static final String TAG = "ShowPicturePresent";

    public ShowPicturePresent(Context mContext, IShowPictureView mIShowPictureView) {
        this.mContext = mContext;
        this.mIShowPictureView = mIShowPictureView;
    }

    public void showPictureNews(final String url) {

    }

    /**
     * get the next url.
     *
     * @param document the document.
     * @return next url.
     */
    public String getNextUrl(Document document) {
        Elements ele = document.select("a[title=点击查看下一张]");
        String href = null;
        for (Element e2 : ele) {
            href = e2.attr("href");
            if (href.startsWith("/")) {
                href = "http://www.chinanews.com" + href;
            }
        }
        return href;
    }

    /**
     * handle the url to get the next url.
     * and show news use shownewspresent function.
     *
     * @param url the url of picture news Page.
     */
    public void handleUrl(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //parse the url.get document
                Log.d("", "url:" + url);
                Document document = null;
                try {
                    document = Jsoup.connect(url).header(USER_AGENT, USER_AGENT_VALUE).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String total = "0";
                Elements eles = document.select("span[id^=showTotal]");
                for (Element e : eles) {
                    total = e.text();
                }
                int count = Integer.parseInt(total);
                Log.d(TAG, "count=" + count);
                String pageurl = url;
                for (int i = 0; i < count - 1; i++) {
                    Document document1 = null;
                    try {
                        document1 = Jsoup.connect(pageurl).header(USER_AGENT, USER_AGENT_VALUE).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String nexturl = getNextUrl(document1);
                    pageurl = nexturl;
                    //handle the nexturl use showNesPresent.
                    showNews(pageurl);
                }
            }
        }).start();
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
                    Document document = Jsoup.connect(url).header(USER_AGENT, USER_AGENT_VALUE).get();

                    Elements elements = document.select("body");

                    for (Element element : elements) {
                        Elements elementsBrother = element.getAllElements();
                        for (Element element1 : elementsBrother) {
                            String tagname = element1.tagName();
                            String classname = element1.className();
                            Log.d("", "标签名:" + tagname);
                            if (element1.className().equals("sp_tp")) {
                                Log.d(TAG, "已经到了我不想要显示的部分了");
                                break;
                            }
                            if (element1.className().equals("div624")) {
                                Log.d(TAG, "已经到了我不想要的那个广告了");
                                break;
                            }
                            if (tagname.equals("p")) {
                                String text = element1.text();
                                if (!text.equals("")) {
                                    //add a TextView
                                    Log.d(TAG, "textview :" + text);
                                    mIShowPictureView.addTextView(text);
                                }
                            }
                            if (classname.equals("t3")) {
                                String text = element1.text();
                                if (!text.equals("")) {
                                    //add a TextView
                                    Log.d(TAG, "textview :" + text);
                                    mIShowPictureView.addTextView(text);
                                }
                            }
                            if (tagname.equals("img")) {
                                String imgsrc = element1.attr("src");
                                Log.d(TAG, "img标题不为空");
                                if (imgsrc.startsWith("/")) {
                                    imgsrc = imgsrc.substring(1);
                                }
                                Log.d(TAG, "imageview " + imgsrc);
                                //add a ImageView
                                Bitmap bitmap = null;
                                try {
                                    bitmap = getNetImage(imgsrc);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                mIShowPictureView.addImageView(bitmap);

                            }
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

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
}
