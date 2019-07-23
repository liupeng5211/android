package daywork;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import util.Db;
import util.Log;
import util.insertVideo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class VideoTest {
    //http://channel.chinanews.com/video/cns/vcl/gn.shtml?&pager=0&pagenum=16
    private static String USER_AGENT = "User-Agent";
    private static String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko Core/1.63.6821.400 QQBrowser/10.3.3040.400";
    private static Connection connection;

    public VideoTest(Connection connection) {
        this.connection = connection;
    }

    public void getUrl(String url) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Element document = null;
                try {

                    document = Jsoup.connect(url).header(USER_AGENT, USER_AGENT_VALUE).get().body();
                    String text = document.text();
                    int start = text.indexOf("{");
                    int end = text.indexOf("; newslist = specialcnsdata;");
//                    System.out.println("start:" + start + ", end:" + end);
                    String json = text.substring(start, end);
                    Log.d(json);
//                    System.out.println("json info:" + json);
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray array = jsonObject.getJSONArray("docs");
                    for (int i = 0; i < array.length(); i++) {
                        Map<String, String> map = new HashMap<>();
                        JSONObject jsonobject = array.getJSONObject(i);
                        String title = jsonobject.get("title").toString();
                        String id = jsonobject.get("id").toString();
                        String imgsrc = jsonobject.get("img_cns").toString();
                        String url = jsonobject.get("url").toString();
                        String playurl = GetVideo.getUrl(url);
                        String time = jsonobject.get("pubtime").toString();
                        String content = jsonobject.get("content").toString();

                        map.put("imgsrc", imgsrc);
                        map.put("id", id);
                        map.put("title", title);
                        map.put("time", time);
                        map.put("url", url);
                        map.put("playurl", playurl);
                        System.out.println("title:" + title);
                        System.out.println("id:" + id);

                        System.out.println("imgsrc:" + imgsrc);

                        System.out.println("url:" + url);

                        System.out.println("content:" + content);

                        System.out.println("time:" + time);

                        System.out.println("playurl:" + playurl);


                        insertVideo.insert(connection, map.get("id"), map.get("title"), map.get("imgsrc"), map.get("url"), map.get("playurl"),
                                map.get("time"));

                    }

                } catch (IOException | JSONException e) {
//                    e.printStackTrace();

                }

                try {

//                    System.out.println(document);


                } catch (FailingHttpStatusCodeException e) {
                    // TODO Auto-generated catch block

                    e.printStackTrace();

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }
            }

        }).start();

    }

    public static void main(String[] args) {
        new VideoTest(Db.getConnect()).getUrl("http://channel.chinanews.com/video/cns/vcl/gn.shtml?&pager=1&pagenum=50");
    }
}
