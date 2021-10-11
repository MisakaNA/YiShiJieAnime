import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Episode {
    private final String BASE_URL = "http://ysjdm.com";
    private final String htmlURL;

    public Episode(String htmlURL) {
        this.htmlURL = htmlURL;
    }

    public String getVideoUrl() throws IOException {
        Document doc = Jsoup.connect(BASE_URL + this.htmlURL).get();
        String jsLink = doc.select("div[id=ccplay]").select("script").get(0).attr("src");
        WebClient web = new WebClient();
        web.getOptions().setCssEnabled(false);
        web.getOptions().setJavaScriptEnabled(true);
        web.getOptions().setThrowExceptionOnScriptError(false);
        HtmlPage htmlPage = web.getPage(BASE_URL + jsLink);
        String str = Jsoup.parse(htmlPage.asXml()).toString();
        Matcher m = Pattern.compile("https?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]").matcher(str);
        if (m.find()) return m.group();

        return  "";
    }
}
