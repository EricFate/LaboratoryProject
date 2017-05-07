package hl.iss.whu.edu.laboratoryproject.entity;

/**
 * Created by 60440 on 2017/2/8.
 */

public class VideoInfo {

    private String videoTitle;
    private String videoUrl;
    private String imageUrl;

    private int videoId;
    private int playnumber;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public int getPlaynumber() {
        return playnumber;
    }

    public void setPlaynumber(int playnumber) {
        this.playnumber = playnumber;
    }
}
