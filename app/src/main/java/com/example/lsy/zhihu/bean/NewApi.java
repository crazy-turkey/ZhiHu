package com.example.lsy.zhihu.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lsy on 16-10-29.
 */

public class NewApi {
    private String body;
    private @SerializedName("image_source") String imageSource;
    private String title;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
