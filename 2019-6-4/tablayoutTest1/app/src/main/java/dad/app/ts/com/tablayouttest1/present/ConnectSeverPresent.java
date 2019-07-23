package dad.app.ts.com.tablayouttest1.present;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dad.app.ts.com.tablayouttest1.MyCommentActivity;
import dad.app.ts.com.tablayouttest1.MyPopupwindow.IPopWindowView;
import dad.app.ts.com.tablayouttest1.R;
import dad.app.ts.com.tablayouttest1.constant.ServerIP;
import dad.app.ts.com.tablayouttest1.util.CollectDbHelper;
import dad.app.ts.com.tablayouttest1.util.DbControl;
import dad.app.ts.com.tablayouttest1.util.GetImageData;
import dad.app.ts.com.tablayouttest1.view.fragment.IPictureView;
import dad.app.ts.com.tablayouttest1.view.fragment.IVideoView;
import dad.app.ts.com.tablayouttest1.view.fragment.MyFragmentView;

public class ConnectSeverPresent {
    private static final String TAG = "ConnectSeverPresent";
    private static final String IP = ServerIP.IP;
    //172.20.10.2
    private static final String LOCAL_IP = "127.0.0.1";
    private MyFragmentView mMyFragmentView;
    private IVideoView mVideoView;
    private IPopWindowView mIPopWindowView;
    private IPictureView mPictureView;
    private Context mContext;

    public ConnectSeverPresent(Context mContext, IPopWindowView view) {
        this.mContext = mContext;
        this.mIPopWindowView = view;
    }

    public ConnectSeverPresent(Context mContext) {
        this.mContext = mContext;
    }

    public ConnectSeverPresent() {
    }

    public ConnectSeverPresent(MyFragmentView mMyFragmentView, Context mContext) {
        this.mMyFragmentView = mMyFragmentView;
        this.mContext = mContext;
    }

    public ConnectSeverPresent(IVideoView view, Context mContext) {
        this.mVideoView = view;
        this.mContext = mContext;
    }

    public ConnectSeverPresent(IPictureView view, Context mContext) {
        this.mPictureView = view;
        this.mContext = mContext;
    }


    /**
     * Collect the news to user account.
     *
     * @param type  the news type.three kind. usual_new, picture_new, video_new.
     * @param title the new title.
     * @param url   the new url.
     */
    public void collectNews(final String type, final String user, final String title, final String url, final String videourl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                PrintWriter os = null;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //add to local db.
                        CollectDbHelper collectDbHelper = new CollectDbHelper(mContext, "collect_news", null, 1);
                        SQLiteDatabase sqLiteDatabase = collectDbHelper.getReadableDatabase();
                        DbControl dbControl = new DbControl();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("user", user);
                        contentValues.put("title", title);
                        contentValues.put("url", url);
                        contentValues.put("type", type);
                        contentValues.put("videourl", videourl);
                        dbControl.insert(sqLiteDatabase, contentValues, "collect_news", "title");


                    }
                }).start();

                try {
                    socket = new Socket(IP, 62224);
                    socket.setSoTimeout(15000);
                    os = new PrintWriter(socket.getOutputStream());
                    String json = "{" + " \"header\":\"收藏\"," +
                            "    \"collect\": [" +
                            "    {  \"user\":\"" + user + "\" , \"title\":\"" + title + "\" , \"url\":\"" + url + "\"   ," +
                            "\"type\":\"" + type + "\"     , \"videourl\":\"" + videourl + "\"   }" +
                            "" +
                            "    ]" +
                            "}";
                    os.println(json);
                    os.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (socket != null) {
                            socket.close();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (os != null) {
                        os.close();
                    }


                }
            }
        }).start();


    }

    /**
     * delete the collect new table info.
     *
     * @param user  the user name.
     * @param title the title.
     */

    public void deleteCollectNew(final String user, final String title) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                PrintWriter os = null;
                try {
                    socket = new Socket(IP, 62224);
                    socket.setSoTimeout(15000);
                    os = new PrintWriter(socket.getOutputStream());
                    String json = "{" + " \"header\":\"删除\"," +
                            "    \"delete\": [" +
                            "    {  \"user\":\"" + user + "\" , \"title\":\"" + title + "\"   }" +
                            "" +
                            "    ]" +
                            "}";
                    os.println(json);
                    os.flush();
                } catch (IOException e) {
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
            }
        }).start();


    }

    /**
     * delete all collect news.
     *
     * @param user the user.
     */
    public void deleteAllCollect(final String user, final ICollectView view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                PrintWriter os = null;
                BufferedReader is = null;
                try {
                    socket = new Socket(IP, 62224);
                    socket.setSoTimeout(15000);
                    os = new PrintWriter(socket.getOutputStream());
                    String json = "{" + " \"header\":\"delete_all_collect\"," +
                            "    \"delete\": [" +
                            "    {  \"user\":\"" + user + "\"}" +
                            "" +
                            "    ]" +
                            "}";
                    os.println(json);
                    os.flush();
                    is = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    String info = is.readLine();
                    Log.d(TAG, "info: " + info);
                    URLDecoder ud = new URLDecoder();

                    String json1 = ud.decode(info, "utf-8");
                    Log.d(TAG, "接收到来自服务器的video消息" + json1);
                    if (json1.equals("200")) {
                        view.refreshAdapter();
                    }
                } catch (IOException e) {
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
            }
        }).start();


    }

    /**
     * delete my comment.
     *
     * @param user  the user .
     * @param title the title.
     */
    public void deleteMyComment(final String user, final String title, final String content, final ICollectView view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                PrintWriter os = null;
                BufferedReader is = null;
                try {
                    socket = new Socket(IP, 62224);
                    socket.setSoTimeout(15000);
                    os = new PrintWriter(socket.getOutputStream());
                    String json = "{" + " \"header\":\"delete my comment\"," +
                            "    \"delete\": [" +
                            "    {  \"user\":\"" + user + "\" , \"title\":\"" + title + "\" , \"content\":\"" + content + "\"  }" +
                            "" +
                            "    ]" +
                            "}";
                    os.println(json);
                    os.flush();
                    is = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    String info = is.readLine();
                    Log.d(TAG, "info: " + info);
                    URLDecoder ud = new URLDecoder();

                    String json1 = ud.decode(info, "utf-8");
                    Log.d(TAG, "接收到来自服务器的video消息" + json1);
                    if (json1.equals("200")) {
                        view.refreshAdapter();
                    }

                } catch (IOException e) {
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
            }
        }).start();


    }

    /**
     * alter the user info.
     *
     * @param column the column.
     * @param value  the column value.
     */
    public void alter(final String username, final String column, final String value) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                PrintWriter os = null;
                BufferedReader is = null;
                try {
                    socket = new Socket(IP, 62224);
                    socket.setSoTimeout(15000);
                    os = new PrintWriter(socket.getOutputStream());
                    String json = "{" + " \"header\":\"alter\"," +
                            "    \"alter\": [" +
                            "    { " + "\"username\":\"" + username + "\"  ," + "\"label\":\"" + column + "\"" + ", \"value\":\"" + value + "\" }" +
                            "" +
                            "    ]" +
                            "}";

                    os.println(json);
                    os.flush();


                } catch (IOException e) {
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
            }
        }).start();


    }

    /**
     * init the video page.
     *
     * @param page the page number index.
     */
    public void initVideo(final int page) {
        Log.d(TAG, " video socket请求连接");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                PrintWriter os = null;
                BufferedReader is = null;

                try {
                    socket = new Socket(IP, 62224);
                    socket.setSoTimeout(15000);
                    os = new PrintWriter(socket.getOutputStream());
                    String json = "{" + " \"header\":\"GetVideo\"," + "\"page\":" + page + "}";

                    os.println(json);
                    os.flush();
                    is = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    String videoInfo = is.readLine();
                    String json1 = "";
                    try {
                        json1 = URLDecoder.decode(videoInfo, "utf-8");
                    } catch (IllegalArgumentException eee) {
                        System.out.println("exception");
                        mVideoView.error();
                        return;

                    }
                    Log.d(TAG, "接收到来自服务器的video消息" + json1);
                    //get the video json;
                    try {
                        JSONObject jsonObject = new JSONObject(json1);
                        JSONArray array = jsonObject.getJSONArray("video");
                        List<Map<String, Object>> list = new ArrayList<>();
                        double progressValue = 0;
                        mVideoView.setProgressMode(false);
                        for (int i = 0; i < array.length(); i++) {
                            Map<String, Object> map = new HashMap<>();
                            JSONObject jsonobject = array.getJSONObject(i);
                            String descripe = jsonobject.get("descripe").toString();
                            String imgsrc = jsonobject.get("imgsrc").toString();
                            String videourl = jsonobject.get("videourl").toString();
                            String playurl = jsonobject.get("playurl").toString();
                            String time = jsonobject.get("time").toString();
                            Bitmap img = getNetImage(imgsrc);
                            map.put("imgsrc", img);
                            map.put("descripe", descripe);
                            map.put("time", time);
                            map.put("videourl", videourl);
                            map.put("playurl", playurl);
                            list.add(map);
                            int value = (int) Math.round(progressValue / array.length() * 100);
                            mVideoView.setValue(value);
                            progressValue++;
                        }
                        SimpleAdapter simpleAdapter = new SimpleAdapter(mContext, list, R.layout.list_view_video,
                                new String[]{"imgsrc", "descripe", "time", "videourl", "playurl"}, new int[]{R.id.image, R.id.title, R.id.time, R.id.videourl, R.id.playurl});
                        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                            public boolean setViewValue(View view, Object data,
                                                        String textRepresentation) {
                                if (view instanceof ImageView) {
                                    ImageView iv = (ImageView) view;
                                    iv.setImageBitmap((Bitmap) data);
                                    return true;

                                } else {
                                    return false;
                                }


                            }
                        });
                        mVideoView.setAdapter(simpleAdapter);

                    } catch (JSONException e) {//JSON
                        Log.d(TAG, "exception");
                        mVideoView.error();
                    }


                } catch (ConnectException e) {
                    Log.d(TAG, "链接超时");
                    mVideoView.showMessage("服务器走丢啦。。。");
                } catch (Exception e) {//IO
                    e.printStackTrace();
                } finally {
                    try {
                        if (socket != null) {
                            socket.close();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        Log.d(TAG, "close null");
                        e.printStackTrace();
                    }
                    if (os != null) {
                        os.close();
                    }


                }
            }
        }).

                start();
    }

    /**
     * init the picture page.Connect the sever to get the picture info.
     *
     * @param page the page number index.
     */
    public void initPicture(final int page) {
        Log.d(TAG, " picture socket请求连接");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                PrintWriter os = null;
                BufferedReader is = null;

                try {
                    socket = new Socket(IP, 62224);
                    socket.setSoTimeout(15000);
                    os = new PrintWriter(socket.getOutputStream());
                    String json = "{" + " \"header\":\"GetPicture\"," + "\"page\":" + page + "}";

                    os.println(json);
                    os.flush();
                    is = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    String videoInfo = is.readLine();
//                    URLDecoder ud = new URLDecoder();
                    String json1 = "";
                    try {
                        json1 = URLDecoder.decode(videoInfo, "utf-8");
                    } catch (IllegalArgumentException eee) {
                        System.out.println("exception");
                        mPictureView.error();
                        return;

                    }


                    Log.d(TAG, "接收到来自服务器的picture消息" + json1);
                    //get the video json;
                    try {
                        JSONObject jsonObject = new JSONObject(json1);
                        JSONArray array = jsonObject.getJSONArray("picture");
                        List<Map<String, Object>> list = new ArrayList<>();
                        double progressValue = 0;
                        mPictureView.setProgressMode(false);
                        for (int i = 0; i < array.length(); i++) {
                            Map<String, Object> map = new HashMap<>();
                            JSONObject jsonobject = array.getJSONObject(i);
                            String title = jsonobject.get("title").toString();
                            if (title.equals("null")) {
                                continue;
                            }
                            String url = jsonobject.get("url").toString();
                            String imgurl = jsonobject.get("imgurl").toString();
                            String time = jsonobject.get("time").toString();
                            Bitmap img = getNetImage(imgurl);
                            map.put("img", img);
                            map.put("title", title);
                            map.put("time", time);
                            map.put("url", url);
                            list.add(map);
                            int value = (int) Math.round(progressValue / array.length() * 100);
                            mPictureView.setValue(value);
                            progressValue++;


                        }
                        SimpleAdapter simpleAdapter = new SimpleAdapter(mContext, list, R.layout.list_view_picture,
                                new String[]{"img", "title", "time", "url"}, new int[]{R.id.image, R.id.title, R.id.time, R.id.url});
                        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                            public boolean setViewValue(View view, Object data,
                                                        String textRepresentation) {
                                if (view instanceof ImageView) {
                                    ImageView iv = (ImageView) view;
                                    iv.setImageBitmap((Bitmap) data);
                                    return true;

                                } else {
                                    return false;
                                }


                            }
                        });
                        mPictureView.setAdapter(simpleAdapter);

                    } catch (JSONException e) {//JSON
                        mPictureView.error();
                    }

                } catch (ConnectException e) {
                    Log.d(TAG, "链接超时");
                    mPictureView.showMessage("服务器走丢啦。。。");
                } catch (Exception e) {//IO
                    e.printStackTrace();
                } finally {
                    try {
                        if (socket != null) {
                            socket.close();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        Log.d(TAG, "close null");
                        e.printStackTrace();
                    }
                    if (os != null) {
                        os.close();
                    }


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
    public Bitmap getNetImage(String imgsrc) {
        Bitmap bitmap;
        try {
            URL url1 = new URL(imgsrc);
            HttpURLConnection connection = null;
            connection = (HttpURLConnection) url1.openConnection();
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (Exception e) {
            return null;
        }


    }

    public void login(final String username, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                PrintWriter os = null;
                BufferedReader is = null;

                try {
                    socket = new Socket(IP, 62224);
                    socket.setSoTimeout(15000);
                    os = new PrintWriter(socket.getOutputStream());
                    String json = "{" + " \"header\":\"登录\"," +
                            "    \"login\": [" +
                            "    { \"name\":\"" + username + "\" , \"password\":\"" + password + "\" }" +
                            "" +
                            "    ]" +
                            "}";
                    os.println(json);
                    os.flush();
                    is = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    String returnInfo = is.readLine();
                    //return the info. use the json to analyze.

                    URLDecoder ud = new URLDecoder();

                    String returnJson = ud.decode(returnInfo, "utf-8");
                    Log.d(TAG, "接收到来自服务器的picture消息" + returnJson);
                    //get the video json;
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(returnJson);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String statusCodeChar = null;
                    try {
                        statusCodeChar = (String) jsonObject.get("status");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    int statusCode = Integer.parseInt(statusCodeChar);
                    if (statusCode != 200) {
                        Log.d(TAG, "登录失败 ");
                        mIPopWindowView.showMessage("failed", "", "", true);
                        return;
                    }


                    JSONArray array = null;
                    String headImage = "";
                    String myword = "";
                    boolean imageFlag = false;
                    try {
                        array = jsonObject.getJSONArray("info");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonobject = null;
                        try {
                            jsonobject = array.getJSONObject(i);
                            headImage = jsonobject.get("headImage").toString();
                            if (headImage.equals("null")) {
                                imageFlag = true;

                            }
                            myword = jsonobject.get("myword").toString();
                            Log.d(TAG, "run: headimage " + headImage);
                            Log.d(TAG, "run: myword " + myword);

//棋盘山
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    if (statusCode == 200) {
                        Log.d(TAG, "登录成功 ");
                        GetImageData getImageData = new GetImageData(mContext);
                        getImageData.writeImgStr(headImage, username);


                        mIPopWindowView.showMessage("successed", headImage, myword, imageFlag);
                    }


                } catch (IOException e) {
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
            }
        }).start();


    }

    /**
     * register user.
     *
     * @return register code.
     */
    public int register() {
        /**
         * net socket test.
         */
        Log.d(TAG, " socket请求连接");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                PrintWriter os = null;
                BufferedReader is = null;

                try {
                    socket = new Socket(IP, 62224);
                    socket.setSoTimeout(5000);
                    os = new PrintWriter(socket.getOutputStream());
                    String json = "{" + " \"header\":\"注册\"," +
                            "    \"zhuce\": [" +
                            "    { \"name\":\"菜鸟教程\" , \"password\":\"www.runoob.com\" }" +
                            "" +
                            "    ]" +
                            "}";
                    os.println(json);
                    os.flush();
                    is = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    String statusCodeChar = is.readLine();
                    int statusCode = Integer.parseInt(statusCodeChar);
                    if (statusCode == 200) {
                        Log.d(TAG, "注册成功 ");
                    } else if (statusCode == 600) {
                        Log.d(TAG, "名称已存在 ");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    os.close();

                }
            }
        }).start();
        return 1;
    }

    /**
     * delete all comment
     *
     * @param user the user.
     * @param view the view.
     */
    public void deleteAllComment(final String user, final ICollectView view) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                PrintWriter os = null;
                BufferedReader is = null;
                try {
                    socket = new Socket(IP, 62224);
                    socket.setSoTimeout(15000);
                    os = new PrintWriter(socket.getOutputStream());
                    String json = "{" + " \"header\":\"delete_all_comment\"," +
                            "    \"delete\": [" +
                            "    {  \"user\":\"" + user + "\"}" +
                            "" +
                            "    ]" +
                            "}";
                    os.println(json);
                    os.flush();
                    is = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    String info = is.readLine();
                    Log.d(TAG, "info: " + info);
                    URLDecoder ud = new URLDecoder();

                    String json1 = ud.decode(info, "utf-8");
                    Log.d(TAG, "接收到来自服务器的video消息" + json1);
                    if (json1.equals("200")) {
                        view.refreshAdapter();
                    }
                } catch (IOException e) {
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
            }
        }).start();


    }
}
