package com.boredream.weibo.entity;

import com.boredream.bdcodehelper.lean.entity.Pointer;
import com.boredream.bdcodehelper.utils.StringUtils;

import java.util.ArrayList;

/**
 * <pre>
 *     author : lichunyang
 *     time   : 2017/08/15
 *     desc   :
 * </pre>
 */
public class Goods extends Pointer {

    private User user;
    private String image;
    private String name;
    private String link;
    private int worth;

    public ArrayList<String> getImages() {
        ArrayList<String> images = new ArrayList<>();
        if(StringUtils.isEmpty(image)) {
            return images;
        }
        for (String s : image.split("\\|")) {
            if(StringUtils.isEmpty(s)) {
                continue;
            }
            images.add(s);
        }
        return images;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getWorth() {
        return worth;
    }

    public void setWorth(int worth) {
        this.worth = worth;
    }
}
