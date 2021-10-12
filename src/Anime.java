import java.net.URL;
import java.util.*;
import javafx.util.Pair;
import com.gargoylesoftware.htmlunit.WebClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

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
    private List<Pair<String, Episode>> episodeResources1;
    private List<Pair<String, Episode>> episodeResources2;
    private HashMap<String, String> downloadResources1;
    private HashMap<String, String> downloadResources2;
    WebClient web;


    public Anime(String name, String pageURL) throws IOException {
        this.name = name;
        this.pageURL = pageURL;
        episodeResources1 = new ArrayList<>();
        episodeResources2 = new ArrayList<>();
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

    public List<Pair<String, Episode>> getEpisodeResources1() {
        return episodeResources1;
    }

    public List<Pair<String, Episode>> getEpisodeResources2() {
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
                        episodeResources1.add(new Pair<>(title, new Episode(url)));
                        break;
                    case 2:
                        episodeResources2.add(new Pair<>(title, new Episode(url)));
                        break;
                    default:
                }

            }
            episodeResources1.sort(new CompareEpisodeTitle());
            episodeResources2.sort(new CompareEpisodeTitle());
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
}

class CompareEpisodeTitle implements Comparator<Pair<String, Episode>> {

    @Override
    public int compare(Pair<String, Episode> o1, Pair<String, Episode> o2) {
        return Integer.parseInt(o1.getKey().replaceAll("\\D", ""))
                - Integer.parseInt(o2.getKey().replaceAll("\\D", ""));
    }
}