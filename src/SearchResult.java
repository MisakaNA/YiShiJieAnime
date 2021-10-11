public class SearchResult {

    private String coverPictureURL;
    private String name;
    private String innerURL;
    private String status;
    private String topic;


    public SearchResult(String name, String coverPictureURL, String innerURL, String status, String topic) {
        this.coverPictureURL = coverPictureURL;
        this.name = name;
        this.innerURL = innerURL;
        this.topic = topic;
        this.status = status;
    }

    public SearchResult(String name, String innerURL) {
        this.name = name;
        this.innerURL = innerURL;
    }

    @Override
    public String toString() {
        return "SearchingResult{" +
                "coverPictureURL='" + coverPictureURL + '\'' +
                ", name='" + name + '\'' +
                ", innerURL='" + innerURL + '\'' +
                ", status='" + status + '\'' +
                ", topic='" + topic + '\'' +
                '}';
    }
}
