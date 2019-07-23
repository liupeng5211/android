package dad.app.ts.com.tablayouttest1.model;

public class SearchNew {
    private String title;
    private String url;
    private String type;
    public static final String USUAL_NEW = "usual_new";
    public static final String IMAGE_NEW = "image_new";
    public static final String VIDEO_NEW = "video_new";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
