package dad.app.ts.com.tablayouttest1.model;

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

import dad.app.ts.com.tablayouttest1.adatper.MyCommentAdapter;
import dad.app.ts.com.tablayouttest1.constant.ServerIP;
import dad.app.ts.com.tablayouttest1.util.CollectDbHelper;

public class MyComment implements IMyComment {
    private static final String USERDATA = "userdata";
    private static final String IP = ServerIP.IP;
    private Socket socket = null;
    private PrintWriter os = null;
    private BufferedReader is = null;
    private CollectDbHelper dbHelper = null;
    private SQLiteDatabase database = null;

    @Override
    public void getAllComment(final Context context, final MyCommentLocalback localback) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(IP, 62224);
                    socket.setSoTimeout(15000);
                    os = new PrintWriter(socket.getOutputStream());
                    String localUserName = context.getSharedPreferences("userdata", 0).getString("username", "null");
                    Log.d("", "username: " + localUserName);
                    String json = "{" +
                            "  \"header\": \"select my_comment\"," +
                            "  \"user\": \"" + localUserName + "\"" +
                            "}";
                    os.println(json);
                    os.flush();
                    is = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    String info = is.readLine();
                    URLDecoder ud = new URLDecoder();

                    String json1 = ud.decode(info, "utf-8");
                    Log.d("", "接收到来自服务器的my comment消息" + json1);
                    //get the video json;
                    try {
                        JSONObject jsonObject = new JSONObject(json1);
                        JSONArray array = jsonObject.getJSONArray("comment");
                        List<CommentNew> list = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            CommentNew commentNew = new CommentNew();
                            JSONObject jsonobject = array.getJSONObject(i);

                            String title = jsonobject.get("title").toString();
                            String content = jsonobject.get("content").toString();
                            String time = jsonobject.get("time").toString();
                            Log.d("", "content: " + content);
                            Log.d("", "title: " + title);
                            Log.d("", "time: " + time);
                            commentNew.setTitle(title);
                            commentNew.setTime(time);
                            commentNew.setContent(content);

                            list.add(commentNew);
                        }
                        //set adapter;
                        MyCommentAdapter myCommentAdapter = new MyCommentAdapter(context, list);
                        localback.success(myCommentAdapter);

                    } finally {
                        try {
                            socket.close();
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

    public interface MyCommentLocalback {
        public void success(MyCommentAdapter commentAdapter);

        public void error(String message);
    }
}
