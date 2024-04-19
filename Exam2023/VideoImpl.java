// package video;

public class VideoImpl implements Video {
    public String title;
    public String host;
    public String port;

    //Constructor
    public VideoImpl(String title, String host, String port) {
        super();
        this.title = title;
        this.host = host;
        this.port = port;
    }

    @Override
    public String getTitle() {
        return title;
    };

    @Override
    public String getHost() {
        return host;
    };

    @Override
    public String getPort() {
        return port;
    };
}