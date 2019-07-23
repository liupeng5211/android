package daywork;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Db;
import util.insertPicture;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;


public class PicturePage {
    private static String USER_AGENT = "User-Agent";
    private static String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko Core/1.63.6821.400 QQBrowser/10.3.3040.400";
    private Connection connection;

    public PicturePage(Connection connection) {
        this.connection = connection;
    }

    public static void main(String[] a) {
        String testUrl = "http://photo.chinanews.com/photo/zxhb.html";
        new PicturePage(Db.getConnect()).getUrl(testUrl);
    }

    public void getUrl(String url) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                // 用http代理
                // 设置代理
                Document document = null;
                try {
                    document = Jsoup.connect(url).header(USER_AGENT, USER_AGENT_VALUE).get();
                } catch (SocketTimeoutException e) {
//                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    // search the div of class for img-kuang4.
                    Elements elements = null;
                    try {
                        elements = document.select("div");
                    } catch (NullPointerException e) {
                        return;

                    }
                    for (Element e : elements) {
                        String className = e.attr("class");
                        if (className.equals("") || className == null) {
                            continue;
                        }
                        if (!className.equals("img-kuang4") && !className.equals("img-kuang")) {
                            continue;
                        }

//						if (href.startsWith("//")) {
//							href = "http:" + href;
//						}
//						if (href.startsWith("/")) {
//							href = "http://www.chinanews.com" + href;
//						}
                        Elements els = e.getAllElements();
                        Map<String, String> map = new HashMap<>();
                        for (Element ee : els) {
                            if (ee.attr("class").equals("date")) {
                                break;
                            }
                            if (ee.tagName().equals("a")) {
                                String href = ee.attr("href");
                                if (href.startsWith("//")) {
                                    href = "http:" + href;
                                }
                                if (href.startsWith("/")) {
                                    href = "http://www.chinanews.com" + href;
                                }
                                System.out.println("网址链接：" + href);
                                if (href.equals("null")) {
                                    continue;
                                }
                                map.put("网址链接", href);
                            }
                            if (ee.tagName().equals("img")) {
                                String src = ee.attr("data-original");
                                if (src.startsWith("//")) {
                                    src = "http:" + src;
                                }
                                if (src.startsWith("/")) {
                                    src = "http://www.chinanews.com" + src;
                                }
                                System.out.println("图片链接：" + src);
                                map.put("图片链接", src);
                            }
                            if (ee.tagName().equals("p")) {
                                String title = ee.text();
                                if (ee.children().size() == 0) {
//                                    System.out.println("时间：" + title);
                                    String[] date_time = title.split(" ");
                                    String date = date_time[0];
                                    String time = date_time[1];
//                                    System.out.println("日期：" + date);
//                                    System.out.println("时间：" + time);
                                    String[] year_month_day = date.split("-");
                                    String year = year_month_day[0];
                                    String month = year_month_day[1];
                                    String day = year_month_day[2];
                                    if (Integer.parseInt(month) < 10) {
                                        month = "0" + month;
                                    }
                                    if (Integer.parseInt(day) < 10) {
                                        day = "0" + day;
                                    }
                                    String text = year + "-" + month + "-" + day + " " + time;
                                    System.out.println("时间:" + text);


                                    map.put("时间", text);
                                } else {
                                    System.out.println("标题：" + title);
                                    map.put("标题", title);
                                }

                            }
                        }
                        System.out.println("标题:" + map.get("标题"));
                        System.out.println("网址链接:" + map.get("网址链接"));
                        System.out.println("图片链接:" + map.get("图片链接"));
                        System.out.println("时间:" + map.get("时间"));
                        insertPicture.insert(connection, map.get("标题"), map.get("网址链接"), map.get("图片链接"), map.get("时间"));
                    }


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
}
