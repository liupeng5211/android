package daywork;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;


public class GetVideo {
    public static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0";
    private static String USER_AGENT = "User-Agent";
    private static String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko Core/1.63.6821.400 QQBrowser/10.3.3040.400";

    public static String getUrl(String url) {
        String html = "";
        try {
            Document document1 = Jsoup.connect(url).header(USER_AGENT, USER_AGENT_VALUE).timeout(10000).get();

            html = document1.toString();
//            System.out.println("html字符串解析完成");

            int start = html.indexOf("var videojsList = [\"");
            if (start == -1) {
//                System.out.println("start == -1");
                Document document = Jsoup.parse(html);
                Elements elements = document.select("video");
                if (elements.size() == 0) {
//                    System.out.println("elements.size() == 0");
                    int start_1 = html.indexOf("b.innerHTML=\"<video id='html5video' src='");
                    String html_1 = html.substring(start_1 + 41);
                    int end_1 = html_1.indexOf("'");
                    String html_2 = html_1.substring(0, end_1);
//                    System.out.println(html_2);
                    if (!html_2.startsWith("http://")) {
//                        System.out.println("不是一个有效的链接，继续处理");
                        String need_info = "so.addVariable(\"vInfo\", \"";
                        int length = need_info.length();
                        int start_index = html.indexOf("so.addVariable(\"vInfo\", \"");
                        String html_3 = html.substring(start_index + length);
//                        System.out.println(html_3);
                        int end_index = html_3.indexOf("\")");
                        String html_4 = html_3.substring(0, end_index);
//                        System.out.println(html_4);
                        return html_4;


                    } else {
                        System.out.println("sout:" + html_2.startsWith("http"));
                    }
                    return html_2;
                }
                String src = "";
                // ScriptException
                for (Element e1 : elements) {
                    Elements source = e1.getElementsByTag("source");
                    for (Element e11 : source) {
                        src = e11.attr(src);
                    }

                }
                System.out.println("src:" + src);
                return src;
            }
            String html1 = html.substring(start + 20);
            int end = html1.indexOf("\"]");
            String html2 = html1.substring(0, end);
//            System.out.println(html2);
            return html2;
        } catch (SocketTimeoutException e) {
            // TODO Auto-generated catch block

            return "";
        } catch (ConnectException e) {
            // TODO Auto-generated catch block

            return "";
        } catch (FailingHttpStatusCodeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } catch (StringIndexOutOfBoundsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Document document = Jsoup.parse(html);
            Elements elements = document.select("video");
            String src = "";
            // ScriptException
            for (Element e1 : elements) {
                src = e1.attr("src");

            }
//            System.out.println(src);
            return src;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }

    public static void main(String[] args) {
        //http://www.chinanews.com/shipin/wsj/2016/1011/wsj150.html
//        System.out.println("执行");
        String videoUrl = GetVideo.getUrl("http://www.chinanews.com/gn/shipin/2019/05-10/news815185.shtml");
        System.out.println(videoUrl);
    }
}