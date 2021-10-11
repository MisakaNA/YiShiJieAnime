import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        YiShiJieAnime anime = new YiShiJieAnime();
        for (SearchResult res : anime.searchAnime("love live")) {
            System.out.println(res.toString());
        }
    }
}
