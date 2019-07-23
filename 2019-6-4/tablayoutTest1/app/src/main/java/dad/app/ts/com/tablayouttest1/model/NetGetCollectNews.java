package dad.app.ts.com.tablayouttest1.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import dad.app.ts.com.tablayouttest1.adatper.CollectNewsAdapter;
import dad.app.ts.com.tablayouttest1.constant.ServerIP;
import dad.app.ts.com.tablayouttest1.util.CollectDbHelper;
import dad.app.ts.com.tablayouttest1.util.DbControl;

public class NetGetCollectNews implements INetGetCollectNews {
    private static final String IP = ServerIP.IP;
    private Socket socket = null;
    private PrintWriter os = null;
    private BufferedReader is = null;
    private CollectDbHelper dbHelper = null;
    private SQLiteDatabase database = null;


    @Override
    public void getCollectNews(final localback localback, final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(IP, 62224);
                    socket.setSoTimeout(15000);
                    dbHelper = new CollectDbHelper(context, "collect_news", null, 1);
                    database = dbHelper.getReadableDatabase();
                    os = new PrintWriter(socket.getOutputStream());
                    String localUserName = context.getSharedPreferences("userdata", 0).getString("username", "null");
                    Log.d("", "username: " + localUserName);
                    String json = "{" +
                            "  \"header\": \"select collect_news\"," +
                            "  \"user\": \"" + localUserName + "\"" +
                            "}";
                    os.println(json);
                    os.flush();
                    is = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    String info = is.readLine();
                    URLDecoder ud = new URLDecoder();

                    String json1 = ud.decode(info, "utf-8");
                    Log.d("", "接收到来自服务器的picture消息" + json1);
                    //get the video json;
                    try {
                        JSONObject jsonObject = new JSONObject(json1);
                        JSONArray array = jsonObject.getJSONArray("collectNews");

                        List<CollectNews> list = new ArrayList<>();
                        DbControl dbControl = new DbControl();
                        dbControl.delete(database, "collect_news");
                        for (int i = 0; i < array.length(); i++) {
                            CollectNews collectNews = new CollectNews();
                            JSONObject jsonobject = array.getJSONObject(i);
                            String username = jsonobject.get("user").toString();
                            String title = jsonobject.get("title").toString();
                            String url = jsonobject.get("url").toString();
                            String type = jsonobject.get("type").toString();
                            String videourl = jsonobject.get("videourl").toString();
                            Log.d("", "username: " + username);
                            Log.d("", "title: " + title);
                            Log.d("", "url: " + url);
                            Log.d("", "type: " + type);
                            Log.d("", "videourl: " + videourl);
                            collectNews.setTitle(title);
                            collectNews.setUrl(url);
                            collectNews.setType(type);
                            collectNews.setVideourl(videourl);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("user", username);
                            contentValues.put("title", title);
                            contentValues.put("url", url);
                            contentValues.put("type", type);
                            contentValues.put("videourl", videourl);
                            dbControl.insert(database, contentValues, "collect_news", "title");
                            list.add(collectNews);
                        }
                        //set adapter;
                        CollectNewsAdapter collectNewsAdapter = new CollectNewsAdapter(context, list);
                        localback.success(collectNewsAdapter);

                    } finally {
                        try {
                            socket.close();
                            database.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (os != null) {
                            os.close();
                        }


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    public interface localback {
        public void success(CollectNewsAdapter adapter);

        public void error(String message);

    }


}