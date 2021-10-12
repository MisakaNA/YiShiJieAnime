import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class YiShiJieAnime {
    private final String BASE_URL = "http://ysjdm.com";
    HashMap<String, List<AnimeOutline>> hotLists;
    List<AnimeOutline> newAnimes;

    public YiShiJieAnime() throws InterruptedException, IOException {
        this.hotLists = new HashMap<>();
        this.newAnimes = new ArrayList<>();
        Document doc = getWebpage(BASE_URL);
        parseHotLists(doc);
        parseNewAnimes(doc);
    }

    public HashMap<String, List<AnimeOutline>> getHotLists() {
        return hotLists;
    }

    public List<AnimeOutline> getNewAnimes() {
        return newAnimes;
    }

    public List<AnimeOutline> searchAnime(String keyword) throws IOException, InterruptedException {
        String searchUrl = BASE_URL + "/search.asp?searchword=" + URLEncoder.encode(keyword, "gb2312");
        List<AnimeOutline> resultList = new ArrayList<>();

        Document doc = getWebpage(searchUrl);

        if (doc == null) return null;

        Element animeResults = doc.select("div.movie-chrList").select("ul").get(0);
        if (animeResults.getElementsByTag("li").size() == 0) {
            return resultList;
        }

        for (Node node : animeResults.childNodes()) {
            if (node.childNodeSize() == 0) continue;
            resultList.add(parseSearchingResultInfo(node));
        }

        return resultList;
    }

    private Document getWebpage(String searchUrl) throws InterruptedException, IOException {
        Document doc = null;

        for (int i = 0; i < 5; i++) {
            try {
                doc = Jsoup.connect(searchUrl)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Accept-Encoding", "gzip, deflate")
                        .timeout(3000)
                        .get();
            } catch (Exception ignored) {

            }
            if (doc != null) break;
            Thread.sleep(1000);
        }

        return doc;
    }

    private void parseHotLists(Document doc) {
        assert doc != null;
        Elements hotListElements = doc.select("div.home-top-new").select("dl");
        this.hotLists.put("无修", extractHotListAnimes(hotListElements.get(0)));
        this.hotLists.put("后宫", extractHotListAnimes(hotListElements.get(1)));
        this.hotLists.put("热血", extractHotListAnimes(hotListElements.get(2)));
        this.hotLists.put("恋爱", extractHotListAnimes(hotListElements.get(3)));
        this.hotLists.put("奇幻", extractHotListAnimes(hotListElements.get(4)));
        this.hotLists.put("冒险", extractHotListAnimes(hotListElements.get(5)));
    }

    private List<AnimeOutline> extractHotListAnimes(Element listElement) {
        List<AnimeOutline> animes = new ArrayList<>();

        Elements animeElements = listElement.select("a");
        for (Element element : animeElements) {
            animes.add(new AnimeOutline(element.childNode(0).toString(), BASE_URL + element.attr("href")));
        }
        return animes;
    }

    private void parseNewAnimes(Document doc) {
        Elements newAnimeElements = doc.select("ul[id=tabcontent_10]").get(0).children();

        for (Element element : newAnimeElements) {
            Element item = element.select("a").get(0);
            String name = item.childNode(0).toString();
            String url = BASE_URL + item.attr("href");
            String status = element.select("span.setnum").get(0).childNode(0).toString();

            this.newAnimes.add(new AnimeOutline(name, url, status));
        }
    }

    private AnimeOutline parseSearchingResultInfo(Node anime) {
        Node innerUrlAndCoverUrl = anime.childNode(0).childNode(0);
        Node intro = anime.childNode(1);

        String innerUrl = innerUrlAndCoverUrl.attr("href");
        String coverUrl = innerUrlAndCoverUrl.childNode(0).attr("src");
        String name = innerUrlAndCoverUrl.childNode(0).attr("alt");

        String status = intro.childNode(1).childNode(1).childNode(0).toString();
        String topic = intro.childNode(2).childNode(0).toString();

        return new AnimeOutline(name,
                (coverUrl.charAt(0) == '/') ? BASE_URL + coverUrl : coverUrl,
                BASE_URL + innerUrl, status, topic.substring(3));
    }
}
