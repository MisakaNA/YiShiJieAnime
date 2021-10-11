import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class YiShiJieAnime {
    private final String BASE_URL = "http://ysjdm.com";
    private final List<SearchResult> resultList;

    public YiShiJieAnime() {
        resultList = new ArrayList<>();
    }

    public List<SearchResult> searchAnime(String keyword) throws IOException {

        String searchUrl = BASE_URL + "/search.asp?searchword=" + URLEncoder.encode(keyword, "gb2312");

        Document doc = Jsoup.connect(searchUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept-Encoding", "gzip, deflate")
                .timeout(3000)
                .get();

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

    public SearchResult getHotLists() {
        return null;
    }

    private SearchResult parseSearchingResultInfo(Node anime) {
        Node innerUrlAndCoverUrl = anime.childNode(0).childNode(0);
        Node intro = anime.childNode(1);

        String innerUrl = innerUrlAndCoverUrl.attr("href");
        String coverUrl = innerUrlAndCoverUrl.childNode(0).attr("src");
        String name = innerUrlAndCoverUrl.childNode(0).attr("alt");

        String status = intro.childNode(1).childNode(1).childNode(0).toString();
        String topic = intro.childNode(2).childNode(0).toString();

        return new SearchResult(name,
                (coverUrl.charAt(0) == '/') ? BASE_URL + coverUrl : coverUrl,
                BASE_URL + innerUrl, status, topic.substring(3));
    }

}
