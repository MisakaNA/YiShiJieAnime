import java.io.IOException;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        YiShiJieAnime anime = new YiShiJieAnime();
        List<SearchResult> list = anime.searchAnime("love live");
        assert list != null;
        for (SearchResult res : list) {
            System.out.println(res.toString());
        }
        Date date=java.util.Calendar.getInstance().getTime();
        System.out.println(date);
        Anime anime1 = new Anime("http://ysjdm.com/XFDM/21047/");
        System.out.println(anime1.getEpisodeResources2().get("第927集").getVideoUrl());
        date=java.util.Calendar.getInstance().getTime();
        System.out.println(date);
    }
}
