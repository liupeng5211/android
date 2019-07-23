package dad.app.ts.com.tablayouttest1.model;

import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLDecoder;

import dad.app.ts.com.tablayouttest1.constant.ServerIP;

public class Register implements IRegister {
    private static final String IP = ServerIP.IP;
    private Socket socket = null;
    private PrintWriter os = null;
    private BufferedReader is = null;

    @Override
    public void register(final RegisterLocalback localback, final Bundle bundle) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(IP, 62224);
                    socket.setSoTimeout(15000);
                    os = new PrintWriter(socket.getOutputStream());
                    String userName = bundle.getString("user");
                    String userPwd = bundle.getString("password");
                    String word = bundle.getString("word");
                    String imageInfo = bundle.getString("imageInfo");

                    String json = "{" + " \"header\":\"注册\"," +
                            "    \"register\": [" +
                            "    {  \"user\":\"" + userName + "\" , \"password\":\"" + userPwd + "\" , \"word\":\"" + word + "\"   ," +
                            "\"imageInfo\":\"" + imageInfo + "\"   }" +
                            "" +
                            "    ]" +
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
                        if (Integer.parseInt(json1) == 600) {
                            localback.error("重复名字，请重新输入名字");

                        } else if (Integer.parseInt(json1) == 200) {
                            localback.success();

                        }


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


    public interface RegisterLocalback {
        public void success();

        public void error(String message);


    }

    ;
}
