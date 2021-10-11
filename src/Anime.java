import java.util.HashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import java.io.IOException;

public class Anime {
    private final String pageURL;
    private String name;
    private String nickName;
    private String time;
    private String region;
    private String language;
    private String overview;
    private String cast;
    private String staff;
    private HashMap<String, String> episodeResources1;
    private HashMap<String, String> episodeResources2;


    public Anime(String pageURL) {
        this.pageURL = pageURL;
        getResources();
    }

    private void getResources() {
        Document doc = Jsoup.connect(pageURL)
    }


}
