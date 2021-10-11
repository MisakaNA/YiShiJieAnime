import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Anime {
    private final String BASE_URL = "http://ysjdm.com";
    private final String pageURL;
    private String name;
    private String time;
    private String region;
    private String language;
    private String overview;
    private String cast;
    private String staff;
    private HashMap<String, Episode> episodeResources1;
    private HashMap<String, Episode> episodeResources2;
    private HashMap<String, String> downloadResources1;
    private HashMap<String, String> downloadResources2;
    WebClient web;

    public Anime(String pageURL) throws IOException {
        this.pageURL = pageURL;
        episodeResources1 = new HashMap<>();
        episodeResources2 = new HashMap<>();
        downloadResources1 = new HashMap<>();
        downloadResources2 = new HashMap<>();
        web = new WebClient();
        getResources();

    }

    public HashMap<String, String> getDownloadResources1() {
        return downloadResources1;
    }

    public HashMap<String, String> getDownloadResources2() {
        return downloadResources2;
    }

    public HashMap<String, Episode> getEpisodeResources1() {
        return episodeResources1;
    }

    public HashMap<String, Episode> getEpisodeResources2() {
        return episodeResources2;
    }

    private void getResources() throws IOException {

        Document doc = Jsoup.parse(new URL(pageURL).openStream(), "GBK", pageURL);
        Elements resources = doc.select("div.playurl");
        int onlineTracker = 0, downloadTracker = 0;
        for(Element resource : resources) {
            if(resource.toString().contains("下载地址")) {
                getDownloadResources(resource, ++downloadTracker);
            }

            if (resource.toString().contains("播放")) {
                getOnlineResources(resource, ++onlineTracker);
            }
        }

    }

    private void getOnlineResources(Element resource, int tracker) {
        Elements episodeList = resource.select("div.bfdz").select("li");
        episodeList.remove(0);
        try {
            for(Element episodeElement : episodeList) {
                String title = episodeElement.childNode(0).attr("title");
                String url = episodeElement.childNode(0).attr("href");
                switch(tracker) {
                    case 1:
                        episodeResources1.put(title, new Episode(url));
                        break;
                    case 2:
                        episodeResources2.put(title, new Episode(url));
                        break;
                    default:
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDownloadResources(Element resource, int tracker) {
        Elements episodeList = resource.select("div.bfdz").select("li");
        episodeList.remove(0);
        try {
            for(Element episodeElement : episodeList) {
                String url = episodeElement.childNode(0).attr("href");
                url = url.contains("http") ? url : BASE_URL + url;
                String title = episodeElement.child(0).childNode(0).toString();
                switch(tracker) {
                    case 1:
                        downloadResources1.put(title, url);
                        break;
                    case 2:
                        downloadResources2.put(title, url);
                        break;
                    default:
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private String getVideoUrl(String url) throws IOException, InterruptedException {
        Document doc = Jsoup.connect(BASE_URL + url).get();
        String jsLink = doc.select("div[id=ccplay]").select("script").get(0).attr("src");

        web.getOptions().setCssEnabled(false);
        web.getOptions().setJavaScriptEnabled(true);
        web.getOptions().setThrowExceptionOnScriptError(false);
        HtmlPage htmlPage = web.getPage(BASE_URL + jsLink);
        String str = Jsoup.parse(htmlPage.asXml()).toString();
        Matcher m = Pattern.compile("https?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]").matcher(str);
        if (m.find()) return m.group();

        return  "";
    }*/
}
