package daywork;


import util.Db;
import util.Log;
import util.NewsUrl;

import java.sql.Connection;

public class DayHandleVideo {
    public static void dayOfVideoWork() {
        Connection connection = Db.getConnect();
        VideoTest videoTest = new VideoTest(connection);
        for (int j = 0; j < NewsUrl.VIDEO_URL.length; j++) {
//            Log.d("开始爬取第" + j + "个网址" + NewsUrl.VIDEO_URL[j]);

            for (int i = 0; i <= NewsUrl.VIDEO_PAGE; i++) {

                String url1 = "?&pager=" + i + "&pagenum=400";
//                Log.d("开始爬取该网址的第" + i + "页.");
                String url = NewsUrl.VIDEO_URL[j] + url1;
                videoTest.getUrl(url);
            }
        }


    }

    public static void main(String[] args) {
        DayHandleVideo.dayOfVideoWork();
    }
}
