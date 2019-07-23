package sever;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.Db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLEncoder;
import java.sql.*;

public class TransferThread extends Thread {
    private Socket mSocket;

    public TransferThread(Socket socket) {
        this.mSocket = socket;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        String name = null;
        String password = null;
        BufferedReader is = null;
        Connection conn = Db.getConnect();
        Statement statement = null;
        try {
            System.out.println("中转线程已经开始:");
            is = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), "UTF-8"));
            String line;
            line = is.readLine();
            System.out.println("服务器消息为:" + line);
            JSONObject jsonObject1 = new JSONObject(line);
            /**
             * if get video.
             */
            if (jsonObject1.getString("header").equals("GetVideo")) {
                System.out.println("来自客户端的请求--获取video");
                int page = jsonObject1.getInt("page");
                try {
                    statement = conn.createStatement();
                    String sql = "select * from news_video order by time desc limit  " + 50 * page + ";";
//                    String sql = "select" + " top " + page * 50 + " * from news_video order by time desc";

                    System.out.println(sql);
                    ResultSet result = statement.executeQuery(sql);
                    String json = "{\"video\":[";
                    while (result.next()) {
                        json = json + "{";

                        String descripe = result.getString("descripe");
                        json = json + "\"descripe\":" + "\"" + descripe + "\",";
                        String imgsrc = result.getString("imgsrc");
                        json = json + "\"imgsrc\":" + "\"" + imgsrc + "\",";
                        String videourl = result.getString("videourl");
                        json = json + "\"videourl\":" + "\"" + videourl + "\",";
                        String playurl = result.getString("playurl");
                        json = json + "\"playurl\":" + "\"" + playurl + "\",";
                        String time = result.getString("time");
                        json = json + "\"time\":" + "\"" + time + "\"";
                        json = json + "},";

                    }
                    json = json.substring(0, json.length() - 1);
                    json = json + "]}";
                    System.out.println("发自客户端json : " + json);
                    String str = URLEncoder.encode(json, "utf-8");
                    PrintWriter os = new PrintWriter(mSocket.getOutputStream());
                    os.println(str);
                    os.close();
                    result.close();
                    statement.close();
                    conn.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    // TODO: handle exception
                }

            }
            /**
             * if delete collect news.
             */
            if (jsonObject1.getString("header").equals("删除")) {
                JSONArray array = jsonObject1.getJSONArray("delete");
                String username = "";
                String title = "";
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonobject = array.getJSONObject(i);
                    username = jsonobject.get("user").toString();
                    title = jsonobject.get("title").toString();
                    System.out.println("user:" + username);
                    System.out.println("title:" + title);
                }
                statement = conn.createStatement();
                String sql = "DELETE FROM collect_news WHERE username = '" + username + "' and title = '" + title + "'";
                System.out.println(sql);
                statement.execute(sql);
                System.out.println("已经成功删除");

            }
            /**
             * if alter.
             */
            if (jsonObject1.getString("header").equals("alter")) {
                JSONArray array = jsonObject1.getJSONArray("alter");
                String username = "";
                String label = "";
                String value = "";
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonobject = array.getJSONObject(i);
                    label = jsonobject.get("label").toString();
                    username = jsonobject.get("username").toString();
                    value = jsonobject.get("value").toString();
                    System.out.println("user:" + username);
                    System.out.println("label:" + label);
                    System.out.println("value:" + value);
                }
                statement = conn.createStatement();
                String sql = "UPDATE userdata2 SET " + label + " = '" + value + "' WHERE name = '" + username + "' ";

                System.out.println(sql);
                statement.execute(sql);
                if (label.equals("name")) {
                    String sql1 = "UPDATE comment SET username = '" + value + "' WHERE username = '" + username + "' ";
                    String sql2 = "UPDATE collect_news SET username = '" + value + "' WHERE username = '" + username + "' ";
                    statement.execute(sql1);
                    statement.execute(sql2);

                }
                System.out.println("已经成功修改");

            }
            /**
             * if delete comment .
             */
            if (jsonObject1.getString("header").equals("delete my comment")) {
                JSONArray array = jsonObject1.getJSONArray("delete");
                String username = "";
                String title = "";
                String comment = "";
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonobject = array.getJSONObject(i);
                    username = jsonobject.get("user").toString();
                    comment = jsonobject.get("content").toString();
                    title = jsonobject.get("title").toString();
                    System.out.println("user:" + username);
                    System.out.println("title:" + title);
                    System.out.println("comment:" + comment);
                }
                statement = conn.createStatement();
                String sql = "DELETE FROM comment WHERE username = '" + username + "' and title = '" + title + "'" + " and content = '" + comment + "'";
                System.out.println(sql);
                statement.execute(sql);
                System.out.println("已经成功删除");
                String json = StatusCode.SUCCESS + "";
                String str = URLEncoder.encode(json, "utf-8");
                PrintWriter os = new PrintWriter(mSocket.getOutputStream());
                os.println(str);
                os.flush();

            }
            /**
             * if delete all collect news .
             */
            if (jsonObject1.getString("header").equals("delete_all_collect")) {
                JSONArray array = jsonObject1.getJSONArray("delete");
                String username = "";
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonobject = array.getJSONObject(i);
                    username = jsonobject.get("user").toString();

                    System.out.println("user:" + username);
                }
                statement = conn.createStatement();
                String sql = "DELETE FROM collect_news WHERE username = '" + username + "' ;";
                System.out.println(sql);
                statement.execute(sql);
                System.out.println("已经成功删除");
                String json = StatusCode.SUCCESS + "";
                String str = URLEncoder.encode(json, "utf-8");
                PrintWriter os = new PrintWriter(mSocket.getOutputStream());
                os.println(str);
                os.flush();

            }
            /**
             * if delete all comments .
             */
            if (jsonObject1.getString("header").equals("delete_all_comment")) {
                JSONArray array = jsonObject1.getJSONArray("delete");
                String username = "";
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonobject = array.getJSONObject(i);
                    username = jsonobject.get("user").toString();

                    System.out.println("user:" + username);
                }
                statement = conn.createStatement();
                String sql = "DELETE FROM comment WHERE username = '" + username + "' ;";
                System.out.println(sql);
                statement.execute(sql);
                System.out.println("已经成功删除");
                String json = StatusCode.SUCCESS + "";
                String str = URLEncoder.encode(json, "utf-8");
                PrintWriter os = new PrintWriter(mSocket.getOutputStream());
                os.println(str);
                os.flush();

            }
            /**
             * if get comment.
             */
            if (jsonObject1.getString("header").equals("getComment")) {
                System.out.println("来自客户端的请求--获取comment");
                String ntitle = jsonObject1.getString("title");
                try {
                    statement = conn.createStatement();
                    String sql = "SELECT * FROM myseveer.comment where title = '" + ntitle + "' order by time desc  ";
                    System.out.println(sql);
                    ResultSet result = statement.executeQuery(sql);
                    String json = "{\"comment\":[";
                    int count = 0;
                    while (result.next()) {
                        count = count + 1;
                        json = json + "{";

                        String title = result.getString("title");
                        json = json + "\"title\":" + "\"" + title + "\",";
                        String username = result.getString("username");
                        json = json + "\"username\":" + "\"" + username + "\",";
                        String content = result.getString("content");
                        json = json + "\"content\":" + "\"" + content + "\",";

                        String time = result.getString("time");
                        json = json + "\"time\":" + "\"" + time + "\",";
                        String userimg = result.getString("userimg");
                        json = json + "\"userimg\":" + "\"" + userimg + "\"";
                        json = json + "},";

                    }
                    if (count != 0) {
                        json = json.substring(0, json.length() - 1);
                    }

                    json = json + "]}";
                    System.out.println("发自客户端json : " + json);
                    String str = URLEncoder.encode(json, "utf-8");
                    PrintWriter os = new PrintWriter(mSocket.getOutputStream());
                    os.println(str);
                    os.close();
                    result.close();
                    statement.close();
                    conn.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    // TODO: handle exception
                }

            }
            /**
             * if comment.
             */
            if (jsonObject1.getString("header").equals("comment")) {
                System.out.println("来自客户端的请求--增加评论");
                try {
                    JSONArray array = jsonObject1.getJSONArray("comment");
                    String title = "";
                    String content = "";
                    String time = "";
                    String userimg = "";
                    String username = "";
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonobject = array.getJSONObject(i);
                        username = jsonobject.get("user").toString();
                        title = jsonobject.get("title").toString();
                        content = jsonobject.get("content").toString();
                        time = jsonobject.get("time").toString();
                        userimg = jsonobject.get("userimg").toString();
                        System.out.println("user:" + username);
                        System.out.println("title:" + title);
                        System.out.println("content:" + content);
                        System.out.println("time:" + time);
                        System.out.println("userimg:" + userimg);

                    }

                    statement = conn.createStatement();
                    String sql = "insert into comment(title,username,content,time,userimg)  values('" + title + "','" + username + "','" + content + "','" + time + "','" + userimg + "')";
                    statement.execute(sql);
                    System.out.println("已经成功添加评论");
                    String json = StatusCode.SUCCESS + "";
                    System.out.println("发自客户端json : " + json);
                    String str = URLEncoder.encode(json, "utf-8");
                    PrintWriter os = new PrintWriter(mSocket.getOutputStream());
                    os.println(str);
                    os.close();
                    statement.close();
                    conn.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    // TODO: handle exception
                }

            }

            /**
             * if get picture.
             */
            if (jsonObject1.getString("header").equals("GetPicture")) {
                System.out.println("来自客户端的请求--获取picture");
                int page = jsonObject1.getInt("page");
                try {
                    statement = conn.createStatement();
//                    String sql = "select" + " top " + page * 50 + " * from news_picture1 order by time desc";
                    String sql = "SELECT * FROM myseveer.news_picture1 order by time desc limit " + page * 50 + ";";
                    System.out.println(sql);
                    ResultSet result = statement.executeQuery(sql);
                    String json = "{\"picture\":[";
                    while (result.next()) {
                        json = json + "{";

                        String title = result.getString(1);
                        json = json + "\"title\":" + "\"" + title + "\",";
                        String url = result.getString(2);
                        json = json + "\"url\":" + "\"" + url + "\",";
                        String imgurl = result.getString(3);
                        json = json + "\"imgurl\":" + "\"" + imgurl + "\",";

                        String time = result.getString(4);
                        json = json + "\"time\":" + "\"" + time + "\"";
                        json = json + "},";

                    }
                    json = json.substring(0, json.length() - 1);
                    json = json + "]}";
                    System.out.println("发自客户端json : " + json);
                    String str = URLEncoder.encode(json, "utf-8");
                    PrintWriter os = new PrintWriter(mSocket.getOutputStream());
                    os.println(str);
                    os.close();
                    result.close();
                    statement.close();
                    conn.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    // TODO: handle exception
                }

            }
            /**
             * if select collect news;
             */
            if (jsonObject1.getString("header").equals("select collect_news")) {
                String username = jsonObject1.getString("user");


                statement = conn.createStatement();
                String sql = "select * from collect_news where username = '" + username + "';";
                System.out.println(sql);
                ResultSet result = statement.executeQuery(sql);
                String json = "{\"collectNews\":[";
                int count = 0;
                while (result.next()) {
                    count++;
                    json = json + "{";
                    json = json + "\"user\":" + "\"" + username + "\",";
                    String title = result.getString("title");
                    json = json + "\"title\":" + "\"" + title + "\",";
                    String url = result.getString("url");
                    json = json + "\"url\":" + "\"" + url + "\",";
                    String type = result.getString("type");
                    json = json + "\"type\":" + "\"" + type + "\",";
                    String videourl = result.getString("videourl");
                    json = json + "\"videourl\":" + "\"" + videourl + "\"";
                    json = json + "},";
                }
                if (count != 0) {
                    json = json.substring(0, json.length() - 1);
                }

                json = json + "]}";
                System.out.println("发自客户端json : " + json);
                String str = URLEncoder.encode(json, "utf-8");
                PrintWriter os = new PrintWriter(mSocket.getOutputStream());
                os.println(str);
                os.close();
                result.close();
                statement.close();
                conn.close();

            }
            /**
             * if select my comment news;
             */
            if (jsonObject1.getString("header").equals("select my_comment")) {
                String username = jsonObject1.getString("user");


                statement = conn.createStatement();
                String sql = "select * from comment where username = '" + username + "';";
                System.out.println(sql);
                ResultSet result = statement.executeQuery(sql);
                String json = "{\"comment\":[";
                int count = 0;
                while (result.next()) {
                    count++;
                    json = json + "{";
                    json = json + "\"user\":" + "\"" + username + "\",";
                    String title = result.getString("title");
                    json = json + "\"title\":" + "\"" + title + "\",";
                    String content = result.getString("content");
                    json = json + "\"content\":" + "\"" + content + "\",";
                    String time = result.getString("time");
                    json = json + "\"time\":" + "\"" + time + "\"";

                    json = json + "},";
                }
                if (count != 0) {
                    json = json.substring(0, json.length() - 1);
                }

                json = json + "]}";
                System.out.println("发自客户端json : " + json);
                String str = URLEncoder.encode(json, "utf-8");
                PrintWriter os = new PrintWriter(mSocket.getOutputStream());
                os.println(str);
                os.close();
                result.close();
                statement.close();
                conn.close();

            }
            /**
             * if collect;
             */
            if (jsonObject1.getString("header").equals("收藏")) {
                JSONArray array = jsonObject1.getJSONArray("collect");
                String title = "";
                String url = "";
                String type = "";
                String videourl = "";
                String username = "";
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonobject = array.getJSONObject(i);
                    username = jsonobject.get("user").toString();
                    title = jsonobject.get("title").toString();
                    url = jsonobject.get("url").toString();
                    type = jsonobject.get("type").toString();
                    videourl = jsonobject.get("videourl").toString();
                    System.out.println("user:" + username);
                    System.out.println("title:" + title);
                    System.out.println("url:" + url);
                    System.out.println("type:" + type);
                    System.out.println("videourl:" + videourl);

                }

                statement = conn.createStatement();
                String sql1 = "select * from collect_news where username = '" + username + "' and title = '" + title + "';";
                ResultSet resultSet = statement.executeQuery(sql1);
                int count = 0;
                while (resultSet.next()) {
                    count++;
                }
                if (count == 0) {
                    String sql = "insert into collect_news(username,title,url,type,videourl)  values('" + username + "','" + title + "','" + url + "','" + type + "','" + videourl + "')";
                    statement.execute(sql);
                }
                System.out.println("已经成功收藏");

            }
            /**
             * if register;
             */
            String word = "";
            String imageInfo = "";
            if (jsonObject1.getString("header").equals("注册")) {
                JSONArray array = jsonObject1.getJSONArray("register");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonobject = array.getJSONObject(i);
                    name = jsonobject.get("user").toString();
                    password = jsonobject.get("password").toString();
                    word = jsonobject.get("word").toString();
                    imageInfo = jsonobject.get("imageInfo").toString();
                    System.out.println("name:" + name);
                    System.out.println("password:" + password);
                    System.out.println("word:" + word);
                    System.out.println("imageInfo:" + imageInfo);

                }
                statement = conn.createStatement();
                String query = "select * from UserData2 where name = '" + name + "'";
                ResultSet set = statement.executeQuery(query);
                if (set.first()) {
                    //repeat.
                    System.out.println("重复名字,不予注册");
                    PrintWriter os = new PrintWriter(mSocket.getOutputStream());
                    os.println(StatusCode.REPEAT);
                    os.close();
                    mSocket.close();
                    return;
                }


                String sql = "insert into UserData2(name,password,headImage,myword)  values('" + name + "','" + password + "','" + imageInfo + "','" + word + "')";
                System.out.println(sql);
                statement.execute(sql);
                System.out.println("已经成功注册");
                PrintWriter os = new PrintWriter(mSocket.getOutputStream());
                os.println(StatusCode.SUCCESS);
                os.close();

            }
            /**
             * if login;
             */
            if (jsonObject1.getString("header").equals("登录")) {
                JSONArray array = jsonObject1.getJSONArray("login");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonobject = array.getJSONObject(i);
                    name = jsonobject.get("name").toString();
                    password = jsonobject.get("password").toString();
                    System.out.println("name:" + name);
                    System.out.println("password:" + password);

                }

                statement = conn.createStatement();
                String sql = "select * from UserData2 where name = '" + name + "' and password = '" + password + "';";
                System.out.println(sql);
                ResultSet result = statement.executeQuery(sql);
                PrintWriter os = new PrintWriter(mSocket.getOutputStream());

                if (result.next()) {
                    System.out.println("已经成功登录");
                    String headImage = result.getString("headImage");
                    String myword = result.getString("myword");
                    String json = "{" + "  \"status\": \"" + StatusCode.SUCCESS + "\"," + "    \"info\": ["
                            + "    { \"headImage\":\"" + headImage + "\", " + " \"myword\":\"" + myword + "\"}" + "]"
                            + "}";
                    System.out.println("json: " + json);
                    String str = URLEncoder.encode(json, "utf-8");
                    os.println(str);
                } else {
                    System.out.println("登录失败");
                    String json = "{" + "  \"status\": \"" + StatusCode.FAIL + "\"," + "    \"info\": ["
                            + "    { \"headImage\":\"\", " + "     \"myword\":\"\"}" + "    ]" + "}";
                    System.out.println("json: " + json);
                    String str = URLEncoder.encode(json, "utf-8");
                    os.println(str);
                }
                result.close();

                os.close();

            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                mSocket.close();
                conn.close();
                is.close();
                statement.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }
}
