package daywork;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Db;
import util.insertVideo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;


public class VideoPage {

    public static void getUrl(String url) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                // 用http代理
                // 设置代理
                WebClient webClient = new WebClient(BrowserVersion.CHROME);
                webClient.getOptions().setUseInsecureSSL(true);
                webClient.getOptions().setJavaScriptEnabled(true); // 启用JS解释器，默认为true
                webClient.getOptions().setCssEnabled(false); // 禁用css支持
                webClient.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常
                webClient.getOptions().setTimeout(100000); // 设置连接超时时间 ，这里是10S。如果为0，则无限期等待
                webClient.getOptions().setDoNotTrackEnabled(false);
                try {
                    HtmlPage page = webClient.getPage(url);
                    String html = page.asXml();
                    System.out.println(html);
                    Element document = Jsoup.parse(html).body();
                    System.out.println(document);
                    Elements elements = document.select("div.splist_div");
                    for (Element e : elements) {
//                        boolean flag = true;
//                        String href = e.attr("href");
//                        if (href.equals("") || href == null) {
//                            continue;
//                        }
//                        if (href.startsWith("//")) {
//                            href = "http:" + href;
//                        }
//                        if (href.startsWith("/")) {
//                            href = "http://www.chinanews.com" + href;
//                        }
                        Elements els = e.getAllElements();
                        Map<String, String> map = new HashMap<>();
                        for (Element ee : els) {
                            if (ee.tagName().equals("a")) {
                                String href = ee.attr("href");
                                if (href.equals("")) {
                                    continue;
                                }
                                if (href.startsWith("//")) {
                                    href = "http:" + href;
                                }
                                if (href.startsWith("/")) {
                                    href = "http://www.chinanews.com" + href;
                                }
                                System.out.println(href);
                                map.put("视频链接", href);
                                String videoUrl = GetVideo.getUrl(href);
                                System.out.println("播放链接" + videoUrl);
                                map.put("播放链接", videoUrl);


                            }
                            if (ee.tagName().equals("img")) {
                                String imgsrc = ee.attr("src");
                                if (imgsrc.startsWith("//")) {
                                    imgsrc = imgsrc.substring(2);
                                }
                                if (imgsrc.startsWith("/")) {
                                    imgsrc = "http://www.chinanews.com" + imgsrc;
                                }
                                if (imgsrc.startsWith("www")) {
                                    imgsrc = "http://" + imgsrc;
                                }
                                String imgtext = ee.attr("alt");
                                if (imgtext.equals("")) {
//                                    flag = false;
                                    System.out.println("imgtext为空");
                                    break;
                                }

                                map.put("图片链接", imgsrc);
                                map.put("描述", imgtext);

//                                System.out.println("播放链接" + videoUrl);
                                System.out.println("图片链接" + imgsrc);
                                System.out.println("描述" + imgtext);
//                                System.out.println("视频链接" + href);

                                // www.chinanews.com/sh/shipin/2019/03-05/news806242.shtml
                            }
                            if (ee.tagName().equals("p")) {
                                String text = ee.text();
                                System.out.println("p元素" + text);

                            }
                            if (ee.className().equals("splist_div_day")) {
                                String time = "";
                                if (ee.text() != null) {
                                    time = ee.text();
                                }
                                System.out.println("时间" + time);
                                map.put("时间", time);

                            }
                        }
                        Connection connection = null;
                        connection = Db.getConnect();
                        insertVideo.insert(connection,map.get("id"), map.get("描述"), map.get("图片链接"), map.get("视频链接"), map.get("播放链接"),
                                map.get("时间"));
//                        if (flag) {
//                            if (map.get("描述") == null) {
//                                return;
//                            }

//                        }

                    }

                } catch (FailingHttpStatusCodeException e) {
                    // TODO Auto-generated catch block

                    e.printStackTrace();

                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                } catch (IOException e) {
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
        VideoPage.getUrl("http://www.chinanews.com/shipin/m/rd/views.shtml");
    }
}
