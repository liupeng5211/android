package dad.app.ts.com.tablayouttest1.model;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import dad.app.ts.com.tablayouttest1.adatper.CollectNewsAdapter;
import dad.app.ts.com.tablayouttest1.adatper.CommentAdapter;
import dad.app.ts.com.tablayouttest1.constant.ServerIP;
import dad.app.ts.com.tablayouttest1.util.DbControl;
import dad.app.ts.com.tablayouttest1.util.GetImageData;

public class CommentHandle implements ICommentHandle {
    private static final String IP = ServerIP.IP;
    private Socket socket = null;
    private PrintWriter os = null;
    private BufferedReader is = null;
    private List<CommentNew> mList;
    private Context mContext;

    public CommentHandle(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void comment(final Commentlocalback commentlocalback, final String title, final String content) {
        //add the comment to server.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(IP, 62224);
                    socket.setSoTimeout(15000);
                    os = new PrintWriter(socket.getOutputStream());
                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    String dateInfo = simpleDateFormat.format(date);
                    String userName = mContext.getSharedPreferences("userdata", 0).getString("username", "null");
                    String userimg = mContext.getSharedPreferences("userdata", 0).getString("image", "null");
                    Log.d("", "title: " + title);
                    Log.d("", "user: " + userName);
                    Log.d("", "content: " + content);
                    Log.d("", "time: " + dateInfo);
                    String json = "{" + " \"header\":\"comment\"," +
                            "    \"comment\": [" +
                            "    { \"title\":\"" + title + "\" , \"user\":\"" + userName + "\", \"content\":\"" + content + "\" , \"time\":\"" + dateInfo + "\" , \"userimg\":\"" + userimg + "\" }" +
                            "" +
                            "    ]" +
                            "}";
                    os.println(json);
                    os.flush();
                    is = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    String info = is.readLine();
                    URLDecoder ud = new URLDecoder();

                    String json1 = ud.decode(info, "utf-8");
                    Log.d("", "接收到来自服务器的消息" + json1);
                    if (json1.equals("200")) {
                        FileInputStream fileInputStream = null;
                        try {
                            fileInputStream = new FileInputStream(String.valueOf(mContext.getDir(userName + ".jpg", mContext.MODE_PRIVATE)) + "/" + userName + ".jpg");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                        CommentNew commentNew = new CommentNew();
                        commentNew.setUser(userName);
                        commentNew.setBitmap(bitmap);
                        commentNew.setTime(dateInfo);
                        commentNew.setContent(content);
                        mList.add(commentNew);
                        CommentAdapter commentAdapter = new CommentAdapter(mContext, mList);
                        commentlocalback.success(commentAdapter);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void getCommentFromServer(final CommentHandle.Commentlocalback commentlocalback, final String title) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(IP, 62224);
                    socket.setSoTimeout(15000);
                    os = new PrintWriter(socket.getOutputStream());

                    String json = "{" + " \"header\":\"getComment\"," + " \"title\":\"" + title + "\"" + "}";

                    os.println(json);
                    os.flush();
                    is = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    String info = is.readLine();
                    URLDecoder ud = new URLDecoder();

                    String json1 = ud.decode(info, "utf-8");
                    Log.d("", json1);


                    try {
                        JSONObject jsonObject = new JSONObject(json1);
                        JSONArray array = jsonObject.getJSONArray("comment");

                        mList = new ArrayList<>();

                        for (int i = 0; i < array.length(); i++) {
                            CommentNew commentNew = new CommentNew();
                            JSONObject jsonobject = array.getJSONObject(i);
                            String username = jsonobject.get("username").toString();
                            String userimg = jsonobject.get("userimg").toString();
                            String content = jsonobject.get("content").toString();
                            String time = jsonobject.get("time").toString();

                            commentNew.setUser(username);
                            commentNew.setContent(content);
                            commentNew.setTime(time);
                            GetImageData getImageData = new GetImageData(mContext);
                            getImageData.writeImgStr(userimg, username);
                            FileInputStream fileInputStream = null;
                            try {
                                fileInputStream = new FileInputStream(String.valueOf(mContext.getDir(username + ".jpg", mContext.MODE_PRIVATE)) + "/" + username + ".jpg");
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);

                            commentNew.setBitmap(bitmap);

                            mList.add(commentNew);
                        }
                        //set adapter;
//                        CollectNewsAdapter collectNewsAdapter = new CollectNewsAdapter(context, list);
//                        localback.success(collectNewsAdapter);
                        CommentAdapter commentAdapter = new CommentAdapter(mContext, mList);
                        commentlocalback.setAdapter(commentAdapter);


                    } catch (JSONException e) {
                        e.printStackTrace();
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
                }
            }
        }).start();

    }

    public interface Commentlocalback {
        public void success(CommentAdapter commentAdapter);

        public void setAdapter(CommentAdapter commentAdapter);

    }
}
