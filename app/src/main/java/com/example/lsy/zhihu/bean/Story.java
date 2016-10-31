package com.example.lsy.zhihu.bean;

import java.util.List;

/**
 * Created by lsy on 16-10-29.
 */

public class Story {
    protected String title;
    protected List<String> images;
    protected String image;
    protected String id;
    protected boolean isNew=true;
    protected String spaceText;

    public String getSpaceText() {
        return spaceText;
    }

    public void setSpaceText(String text) {
        this.spaceText = text;
    }

    public boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(boolean aNew) {
        isNew = aNew;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
